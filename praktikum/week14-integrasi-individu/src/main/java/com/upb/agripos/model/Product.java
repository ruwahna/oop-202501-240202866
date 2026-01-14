package com.upb.agripos.model;

/**
 * Model class untuk Product
 * Menerapkan encapsulation (Bab 2)
 */
public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product() {}

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
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

    @Override
    public String toString() {
        return String.format("%s - %s (Rp %.2f) Stock: %d", 
            code, name, price, stock);
    }
}