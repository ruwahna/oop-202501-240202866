package com.upb.agripos.service.discount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Discount Strategy Tests")
class DiscountStrategyTest {
    
    private PercentageDiscount percentageDiscount;
    private FixedDiscount fixedDiscount;
    private BulkDiscount bulkDiscount;
    private VoucherDiscount voucherDiscount;

    @BeforeEach
    void setUp() {
        percentageDiscount = new PercentageDiscount(10, "Diskon 10%");
        fixedDiscount = new FixedDiscount(50000, "Rp 50.000");
        bulkDiscount = new BulkDiscount(5, 15, "Bulk 15%");
        voucherDiscount = new VoucherDiscount("WELCOME", new PercentageDiscount(5, "Diskon 5%"), 100);
    }

    @Test
    @DisplayName("Test Percentage Discount - 10% dari Rp 200.000")
    void testPercentageDiscount() {
        double subtotal = 200000;
        double discount = percentageDiscount.calculateDiscount(subtotal, 1);
        assertEquals(20000, discount, "10% dari 200.000 harus 20.000");
    }

    @Test
    @DisplayName("Test Fixed Discount - Rp 50.000")
    void testFixedDiscount() {
        double subtotal = 300000;
        double discount = fixedDiscount.calculateDiscount(subtotal, 1);
        assertEquals(50000, discount, "Fixed diskon harus 50.000");
    }

    @Test
    @DisplayName("Test Fixed Discount - Tidak boleh melebihi subtotal")
    void testFixedDiscountCapped() {
        double subtotal = 30000; // Subtotal lebih kecil dari diskon (50.000)
        double discount = fixedDiscount.calculateDiscount(subtotal, 1);
        assertEquals(30000, discount, "Diskon harus capped ke subtotal");
    }

    @Test
    @DisplayName("Test Bulk Discount - 15% untuk 5+ items")
    void testBulkDiscount() {
        double subtotal = 250000;
        double discount = bulkDiscount.calculateDiscount(subtotal, 5); // 5 items
        assertEquals(37500, discount, "15% dari 250.000 harus 37.500");
    }

    @Test
    @DisplayName("Test Bulk Discount - Tidak applicable untuk < 5 items")
    void testBulkDiscountNotApplicable() {
        double subtotal = 200000;
        double discount = bulkDiscount.calculateDiscount(subtotal, 4); // 4 items
        assertEquals(0, discount, "Bulk diskon tidak berlaku untuk 4 items");
    }

    @Test
    @DisplayName("Test Voucher Discount - Wrap strategy")
    void testVoucherDiscount() {
        double subtotal = 100000;
        double discount = voucherDiscount.calculateDiscount(subtotal, 1);
        assertEquals(5000, discount, "Voucher 5% dari 100.000 harus 5.000");
    }

    @Test
    @DisplayName("Test Voucher Discount - Use tracking")
    void testVoucherUsageTracking() {
        assertTrue(voucherDiscount.use(), "Use harus berhasil (usage < max)");
        assertEquals(1, voucherDiscount.getCurrentUsage(), "Current usage harus 1");
    }

    @Test
    @DisplayName("Test Voucher Discount - Max usage limit")
    void testVoucherMaxUsageLimit() {
        VoucherDiscount limited = new VoucherDiscount("TEST", new PercentageDiscount(10, "Test"), 2);
        
        assertTrue(limited.use(), "Use pertama harus berhasil");
        assertTrue(limited.use(), "Use kedua harus berhasil");
        assertFalse(limited.use(), "Use ketiga harus gagal (sudah max)");
        
        assertTrue(limited.isExceeded(), "Harus exceeded setelah max usage");
    }

    @Test
    @DisplayName("Test Percentage Discount dengan minimum purchase")
    void testPercentageDiscountWithMinimum() {
        PercentageDiscount withMin = new PercentageDiscount(10, 100000, "Diskon 10% min 100k");
        
        // Subtotal 80.000 < 100.000 (minimum)
        assertFalse(withMin.isApplicable(80000, 1), "Tidak applicable jika subtotal < minimum");
        assertEquals(0, withMin.calculateDiscount(80000, 1), "Diskon harus 0");
        
        // Subtotal 150.000 >= 100.000
        assertTrue(withMin.isApplicable(150000, 1), "Applicable jika subtotal >= minimum");
        assertEquals(15000, withMin.calculateDiscount(150000, 1), "Diskon harus 15.000");
    }

    @Test
    @DisplayName("Test invalid percentage (must be 0-100)")
    void testInvalidPercentage() {
        assertThrows(IllegalArgumentException.class, () -> new PercentageDiscount(150, "Invalid"));
        assertThrows(IllegalArgumentException.class, () -> new PercentageDiscount(-10, "Invalid"));
        assertThrows(IllegalArgumentException.class, () -> new PercentageDiscount(0, "Invalid"));
    }

    @Test
    @DisplayName("Test invalid fixed amount (must be > 0)")
    void testInvalidFixedAmount() {
        assertThrows(IllegalArgumentException.class, () -> new FixedDiscount(0, "Invalid"));
        assertThrows(IllegalArgumentException.class, () -> new FixedDiscount(-50000, "Invalid"));
    }

    @Test
    @DisplayName("Test invalid bulk quantity (must be > 0)")
    void testInvalidBulkQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new BulkDiscount(0, 10, "Invalid"));
        assertThrows(IllegalArgumentException.class, () -> new BulkDiscount(-5, 10, "Invalid"));
    }

    @Test
    @DisplayName("Test description methods")
    void testDescriptions() {
        String percentDesc = percentageDiscount.getDescription();
        assertTrue(percentDesc.contains("10%"), "Description harus contain persentase");
        
        String fixedDesc = fixedDiscount.getDescription();
        assertTrue(fixedDesc.contains("50000"), "Description harus contain amount");
        
        String bulkDesc = bulkDiscount.getDescription();
        assertTrue(bulkDesc.contains("5") && bulkDesc.contains("15%"), "Description harus contain quantity dan percentage");
        
        String voucherDesc = voucherDiscount.getDescription();
        assertTrue(voucherDesc.contains("WELCOME"), "Description harus contain voucher code");
    }
}
