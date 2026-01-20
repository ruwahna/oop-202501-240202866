package com.upb.agripos.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Service untuk mengelola diskon yang tersedia di sistem
 * Shared antara Admin (mengelola) dan Kasir (menggunakan)
 */
public class DiscountConfigService {
    
    private static DiscountConfigService instance;
    private final ObservableList<DiscountConfig> discountConfigs;
    
    private DiscountConfigService() {
        discountConfigs = FXCollections.observableArrayList();
        loadDefaultDiscounts();
    }
    
    public static synchronized DiscountConfigService getInstance() {
        if (instance == null) {
            instance = new DiscountConfigService();
        }
        return instance;
    }
    
    private void loadDefaultDiscounts() {
        // Default discounts
        discountConfigs.addAll(
            new DiscountConfig("Diskon Umum", "UMUM5", "Persentase", 5, 0, 0, true),
            new DiscountConfig("Diskon Member", "MEMBER10", "Persentase", 10, 0, 0, true),
            new DiscountConfig("Diskon Bulk", "BULK15", "Persentase", 15, 0, 5, true),
            new DiscountConfig("Welcome Discount", "WELCOME", "Persentase", 5, 0, 0, true),
            new DiscountConfig("Promo 50K", "PROMO50K", "Nominal", 50000, 200000, 0, true)
        );
    }
    
    public ObservableList<DiscountConfig> getDiscountConfigs() {
        return discountConfigs;
    }
    
    public ObservableList<DiscountConfig> getActiveDiscounts() {
        return discountConfigs.filtered(DiscountConfig::isActive);
    }
    
    public void addDiscount(DiscountConfig config) {
        discountConfigs.add(config);
    }
    
    public void updateDiscount(String code, DiscountConfig updated) {
        for (int i = 0; i < discountConfigs.size(); i++) {
            if (discountConfigs.get(i).getCode().equals(code)) {
                discountConfigs.set(i, updated);
                break;
            }
        }
    }
    
    public void removeDiscount(String code) {
        discountConfigs.removeIf(d -> d.getCode().equals(code));
    }
    
    public DiscountConfig findByCode(String code) {
        return discountConfigs.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
    
    public boolean isValidVoucher(String code) {
        DiscountConfig config = findByCode(code);
        return config != null && config.isActive();
    }
    
    /**
     * Inner class untuk konfigurasi diskon
     */
    public static class DiscountConfig {
        private String name;
        private String code;
        private String type; // "Persentase" atau "Nominal"
        private double value;
        private double minPurchase;
        private int minItems;
        private boolean active;
        
        public DiscountConfig(String name, String code, String type, double value, 
                             double minPurchase, int minItems, boolean active) {
            this.name = name;
            this.code = code;
            this.type = type;
            this.value = value;
            this.minPurchase = minPurchase;
            this.minItems = minItems;
            this.active = active;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        
        public double getMinPurchase() { return minPurchase; }
        public void setMinPurchase(double minPurchase) { this.minPurchase = minPurchase; }
        
        public int getMinItems() { return minItems; }
        public void setMinItems(int minItems) { this.minItems = minItems; }
        
        // Alias untuk getMinItems (untuk kompatibilitas)
        public int getMinQty() { return minItems; }
        
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        
        public String getDisplayName() {
            if (type.equals("Persentase")) {
                return name + " (" + (int)value + "%)";
            } else {
                return name + " (Rp " + String.format("%,.0f", value) + ")";
            }
        }
    }
}
