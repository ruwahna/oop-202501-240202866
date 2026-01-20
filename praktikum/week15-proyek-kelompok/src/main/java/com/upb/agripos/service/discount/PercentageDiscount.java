package com.upb.agripos.service.discount;

/**
 * Implementasi Diskon Persentase (5%, 10%, 15%, etc)
 */
public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;
    private final double minPurchase; // Minimal pembelian untuk mendapat diskon
    private final String name;
    
    /**
     * Constructor dengan persentase saja (minimal pembelian = 0)
     */
    public PercentageDiscount(double percentage, String name) {
        this(percentage, 0, name);
    }
    
    /**
     * Constructor dengan persentase dan minimal pembelian
     */
    public PercentageDiscount(double percentage, double minPurchase, String name) {
        if (percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException("Persentase harus antara 0-100");
        }
        if (minPurchase < 0) {
            throw new IllegalArgumentException("Minimal pembelian tidak boleh negatif");
        }
        this.percentage = percentage;
        this.minPurchase = minPurchase;
        this.name = name;
    }
    
    @Override
    public double calculateDiscount(double subtotal, int itemCount) {
        if (isApplicable(subtotal, itemCount)) {
            return subtotal * percentage / 100;
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
            return name + " (" + percentage + "% min Rp " + String.format("%.0f", minPurchase) + ")";
        }
        return name + " (" + percentage + "%)";
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public double getMinPurchase() {
        return minPurchase;
    }
}
