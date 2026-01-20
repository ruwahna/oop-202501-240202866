package com.upb.agripos.service.discount;

/**
 * Implementasi Diskon Fixed Amount (Rp 50.000, Rp 100.000, etc)
 */
public class FixedDiscount implements DiscountStrategy {
    private final double amount;
    private final double minPurchase;
    private final String name;
    
    /**
     * Constructor dengan amount saja
     */
    public FixedDiscount(double amount, String name) {
        this(amount, 0, name);
    }
    
    /**
     * Constructor dengan amount dan minimal pembelian
     */
    public FixedDiscount(double amount, double minPurchase, String name) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Jumlah diskon harus positif");
        }
        if (minPurchase < 0) {
            throw new IllegalArgumentException("Minimal pembelian tidak boleh negatif");
        }
        this.amount = amount;
        this.minPurchase = minPurchase;
        this.name = name;
    }
    
    @Override
    public double calculateDiscount(double subtotal, int itemCount) {
        if (isApplicable(subtotal, itemCount)) {
            // Diskon tidak boleh lebih besar dari subtotal
            return Math.min(amount, subtotal);
        }
        return 0;
    }
    
    @Override
    public boolean isApplicable(double subtotal, int itemCount) {
        return subtotal >= minPurchase;
    }
    
    @Override
    public String getDescription() {
        if (minPurchase > 0) {
            return name + " (Rp " + String.format("%.0f", amount) + " min Rp " + String.format("%.0f", minPurchase) + ")";
        }
        return name + " (Rp " + String.format("%.0f", amount) + ")";
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getMinPurchase() {
        return minPurchase;
    }
}
