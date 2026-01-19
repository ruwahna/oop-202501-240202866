package com.upb.agripos.model;

/**
 * Model class untuk Product (FR-1 Manajemen Produk)
 * Atribut: kode, nama, kategori, harga, stok
 * Menerapkan encapsulation (Bab 2)
 */
public class Product {
    private String code;
    private String name;
    private String category;
    private double price;
    private int stock;

    public Product() {}

    public Product(String code, String name, String category, double price, int stock) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Constructor tanpa kategori untuk backward compatibility
    public Product(String code, String name, double price, int stock) {
        this(code, name, "Umum", price, stock);
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Mengurangi stok produk
     * @param quantity jumlah yang dikurangi
     * @return true jika berhasil, false jika stok tidak mencukupi
     */
    public boolean reduceStock(int quantity) {
        if (quantity <= 0 || quantity > stock) {
            return false;
        }
        this.stock -= quantity;
        return true;
    }

    /**
     * Menambah stok produk
     * @param quantity jumlah yang ditambah
     */
    public void addStock(int quantity) {
        if (quantity > 0) {
            this.stock += quantity;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Rp %.2f | Stok: %d", 
            code, name, category, price, stock);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return code != null && code.equals(product.code);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
