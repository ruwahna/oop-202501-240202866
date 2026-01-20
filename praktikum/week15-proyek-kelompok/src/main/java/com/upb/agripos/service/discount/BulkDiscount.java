package com.upb.agripos.service.discount;

/**
 * Implementasi Diskon Bulk (X% untuk >= N items)
 * Contoh: 15% diskon untuk 5+ items
 */
public class BulkDiscount implements DiscountStrategy {
    private final int minQuantity;
    private final double percentage;
    private final String name;
    
    /**
     * @param minQuantity minimum jumlah items untuk mendapat diskon
     * @param percentage persentase diskon
     * @param name nama/deskripsi diskon
     */
    public BulkDiscount(int minQuantity, double percentage, String name) {
        if (minQuantity <= 0) {
            throw new IllegalArgumentException("Minimum quantity harus > 0");
        }
        if (percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException("Persentase harus antara 0-100");
        }
        this.minQuantity = minQuantity;
        this.percentage = percentage;
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
        return itemCount >= minQuantity;
    }
    
    @Override
    public String getDescription() {
        return name + " (" + percentage + "% untuk >= " + minQuantity + " items)";
    }
    
    public int getMinQuantity() {
        return minQuantity;
    }
    
    public double getPercentage() {
        return percentage;
    }
}
