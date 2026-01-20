package com.upb.agripos.service.discount;

/**
 * Implementasi Diskon Voucher dengan usage tracking (Decorator Pattern)
 * Wraps strategi diskon lain dan menambahkan tracking penggunaan
 */
public class VoucherDiscount implements DiscountStrategy {
    private final String code;
    private final DiscountStrategy wrappedStrategy;
    private final int maxUsage;
    private int currentUsage;
    
    /**
     * @param code kode voucher (WELCOME, MEMBER10, PROMO50K, dll)
     * @param wrappedStrategy strategi diskon yang dibungkus
     * @param maxUsage maksimal penggunaan voucher
     */
    public VoucherDiscount(String code, DiscountStrategy wrappedStrategy, int maxUsage) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode voucher tidak boleh kosong");
        }
        if (wrappedStrategy == null) {
            throw new IllegalArgumentException("Wrapped strategy tidak boleh null");
        }
        if (maxUsage <= 0) {
            throw new IllegalArgumentException("Max usage harus > 0");
        }
        
        this.code = code;
        this.wrappedStrategy = wrappedStrategy;
        this.maxUsage = maxUsage;
        this.currentUsage = 0;
    }
    
    /**
     * Gunakan voucher ini (increment usage counter)
     * @return true jika berhasil digunakan, false jika sudah melebihi max usage
     */
    public boolean use() {
        if (currentUsage < maxUsage) {
            currentUsage++;
            return true;
        }
        return false;
    }
    
    @Override
    public double calculateDiscount(double subtotal, int itemCount) {
        return wrappedStrategy.calculateDiscount(subtotal, itemCount);
    }
    
    @Override
    public boolean isApplicable(double subtotal, int itemCount) {
        return currentUsage < maxUsage && wrappedStrategy.isApplicable(subtotal, itemCount);
    }
    
    @Override
    public String getDescription() {
        return "Voucher " + code + ": " + wrappedStrategy.getDescription();
    }
    
    public String getCode() {
        return code;
    }
    
    public int getCurrentUsage() {
        return currentUsage;
    }
    
    public int getMaxUsage() {
        return maxUsage;
    }
    
    public boolean isExceeded() {
        return currentUsage >= maxUsage;
    }
}
