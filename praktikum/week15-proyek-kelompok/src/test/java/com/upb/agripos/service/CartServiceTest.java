package com.upb.agripos.service;

import com.upb.agripos.model.Product;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.exception.ValidationException;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk CartService
 * Test coverage: Add item, update qty, remove item, calculate total, clear cart
 */
@DisplayName("CartService Tests")
public class CartServiceTest {

    private CartService cartService;
    private Product productDummy;
    private Product product2;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        // Menyiapkan produk contoh: Harga 10.000, Stok 5
        productDummy = new Product("P001", "Pupuk Tes", "Pupuk", 10000.0, 5);
        // Produk kedua: Harga 5.000, Stok 10
        product2 = new Product("P002", "Benih", "Benih", 5000.0, 10);
    }

    @Test
    @DisplayName("Test 1: Tambah Produk ke Keranjang - Berhasil")
    void testAddItemSuccess() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 2);
        
        assertEquals(1, cartService.getCartItems().size(), "Jumlah item di keranjang harus 1");
        assertEquals(20000.0, cartService.calculateTotal(), "Total harga harus 20.000 (10rb x 2)");
        assertEquals(2, cartService.getTotalQty(), "Total qty harus 2");
        System.out.println("✓ Add item success test passed");
    }

    @Test
    @DisplayName("Test 2: Tambah Produk Sama Dua Kali - Update Qty")
    void testAddSameProductTwice() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 2);
        cartService.addItem(productDummy, 1);
        
        assertEquals(1, cartService.getCartItems().size(), "Harus tetap 1 item (qty di-update)");
        assertEquals(30000.0, cartService.calculateTotal(), "Total harus 30.000 (10rb x 3)");
        assertEquals(3, cartService.getTotalQty(), "Total qty harus 3");
        System.out.println("✓ Add same product twice test passed");
    }

    @Test
    @DisplayName("Test 3: Tambah Produk Karena Stok Kurang - Gagal")
    void testAddItemOutOfStock() {
        // Stok produk cuma 5, kita minta 10
        assertThrows(OutOfStockException.class, () -> {
            cartService.addItem(productDummy, 10);
        }, "Harusnya melempar OutOfStockException karena stok tidak cukup");
        System.out.println("✓ Out of stock test passed");
    }

    @Test
    @DisplayName("Test 4: Tambah Produk dengan Qty 0 atau Negatif - Gagal")
    void testAddItemInvalidQty() {
        assertThrows(ValidationException.class, () -> {
            cartService.addItem(productDummy, 0);
        }, "Qty 0 harus throw ValidationException");
        
        assertThrows(ValidationException.class, () -> {
            cartService.addItem(productDummy, -5);
        }, "Qty negatif harus throw ValidationException");
        System.out.println("✓ Invalid qty test passed");
    }

    @Test
    @DisplayName("Test 5: Tambah Produk Null - Gagal")
    void testAddNullProduct() {
        assertThrows(ValidationException.class, () -> {
            cartService.addItem(null, 1);
        }, "Produk null harus throw ValidationException");
        System.out.println("✓ Null product test passed");
    }

    @Test
    @DisplayName("Test 6: Hitung Total Multiple Items")
    void testCalculateTotalMultipleItems() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 1); // 10.000
        cartService.addItem(product2, 2);     // 10.000
        
        assertEquals(20000.0, cartService.calculateTotal(), "Total kumulatif harus 20.000");
        assertEquals(3, cartService.getTotalQty(), "Total qty harus 3");
        System.out.println("✓ Multiple items total test passed");
    }

    @Test
    @DisplayName("Test 7: Update Qty Item di Keranjang - Berhasil")
    void testUpdateItemQty() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 2);
        cartService.updateItemQty("P001", 4);
        
        List<TransactionItem> items = cartService.getCartItems();
        assertEquals(1, items.size());
        assertEquals(4, items.get(0).getQty(), "Qty harus diupdate menjadi 4");
        assertEquals(40000.0, cartService.calculateTotal(), "Total harus 40.000 (10rb x 4)");
        System.out.println("✓ Update qty test passed");
    }

    @Test
    @DisplayName("Test 8: Update Qty dengan Nilai 0 - Remove Item")
    void testUpdateQtyToZero() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 2);
        cartService.updateItemQty("P001", 0);
        
        assertTrue(cartService.getCartItems().isEmpty(), "Item harus dihapus saat qty 0");
        assertEquals(0.0, cartService.calculateTotal());
        System.out.println("✓ Update qty to zero test passed");
    }

    @Test
    @DisplayName("Test 9: Hapus Item dari Keranjang")
    void testRemoveItem() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 2);
        cartService.addItem(product2, 1);
        
        // Remove item P001
        cartService.removeItemByKode("P001");
        
        assertEquals(1, cartService.getCartItems().size(), "Harus tersisa 1 item");
        assertEquals(5000.0, cartService.calculateTotal(), "Total harus 5.000 (hanya P002)");
        System.out.println("✓ Remove item test passed");
    }

    @Test
    @DisplayName("Test 10: Kosongkan Keranjang")
    void testClearCart() throws OutOfStockException, ValidationException {
        cartService.addItem(productDummy, 1);
        cartService.addItem(product2, 2);
        
        assertTrue(!cartService.isEmpty(), "Keranjang tidak kosong sebelum clear");
        
        cartService.clearCart();
        
        assertTrue(cartService.isEmpty(), "Keranjang harus kosong setelah di-clear");
        assertEquals(0, cartService.getCartSize(), "Size harus 0");
        assertEquals(0, cartService.getTotalQty(), "Total qty harus 0");
        assertEquals(0.0, cartService.calculateTotal(), "Total harus kembali ke 0");
        System.out.println("✓ Clear cart test passed");
    }

    @Test
    @DisplayName("Test 11: Cek Status Cart (isEmpty, getCartSize)")
    void testCartStatus() throws OutOfStockException, ValidationException {
        assertTrue(cartService.isEmpty(), "Cart awalnya harus kosong");
        assertEquals(0, cartService.getCartSize());
        
        cartService.addItem(productDummy, 1);
        assertFalse(cartService.isEmpty(), "Cart tidak boleh kosong");
        assertEquals(1, cartService.getCartSize());
        
        cartService.addItem(product2, 2);
        assertEquals(2, cartService.getCartSize(), "Size harus 2 item");
        assertEquals(3, cartService.getTotalQty(), "Qty total harus 3");
        System.out.println("✓ Cart status test passed");
    }

    @Test
    @DisplayName("Test 12: Scenario Kompleks - Multiple Operations")
    void testComplexScenario() throws OutOfStockException, ValidationException {
        // Add items
        cartService.addItem(productDummy, 2);      // 20.000
        cartService.addItem(product2, 3);          // 15.000, total: 35.000
        assertEquals(35000.0, cartService.calculateTotal());
        
        // Update qty
        cartService.updateItemQty("P001", 1);      // Update ke 1, total: 15.000
        assertEquals(15000.0, cartService.calculateTotal());
        
        // Add more
        cartService.addItem(productDummy, 2);      // Add 2 lagi (total 3), total: 30.000
        assertEquals(30000.0, cartService.calculateTotal());
        
        // Remove one item
        cartService.removeItemByKode("P002");      // Remove P002, total: 30.000 (hanya P001)
        assertEquals(30000.0, cartService.calculateTotal());
        assertEquals(1, cartService.getCartSize());
        
        System.out.println("✓ Complex scenario test passed");
    }
}