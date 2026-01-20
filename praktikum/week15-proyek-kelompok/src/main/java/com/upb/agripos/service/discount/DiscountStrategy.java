package com.upb.agripos.service.discount;

/**
 * Interface untuk Strategy Pattern - Diskon (OFR-2)
 * Menerapkan Open/Closed Principle: terbuka untuk extension, tertutup untuk modification
 */
public interface DiscountStrategy {
    /**
     * Hitung besarnya diskon
     * @param subtotal harga sebelum diskon
     * @param itemCount jumlah item di keranjang
     * @return besarnya diskon dalam Rp
     */
    double calculateDiscount(double subtotal, int itemCount);
    
    /**
     * Cek apakah diskon bisa diterapkan
     * @param subtotal harga sebelum diskon
     * @param itemCount jumlah item di keranjang
     * @return true jika bisa diterapkan
     */
    boolean isApplicable(double subtotal, int itemCount);
    
    /**
     * Deskripsi diskon untuk ditampilkan ke user
     * @return string deskripsi
     */
    String getDescription();
}
