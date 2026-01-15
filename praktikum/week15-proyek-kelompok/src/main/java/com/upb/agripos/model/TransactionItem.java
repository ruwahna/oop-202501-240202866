package com.upb.agripos.model;

public class TransactionItem {
    private int id;
    private Product product;
    private int qty;
    private double subtotal;

    // Constructor default untuk database
    public TransactionItem() {
    }

    // Constructor dengan product dan qty
    public TransactionItem(Product product, int qty) {
        this.product = product;
        this.qty = qty;
        this.subtotal = product.getHarga() * qty;
    }

    // Constructor dengan product, qty, dan subtotal (untuk testing dan DB)
    public TransactionItem(Product product, int qty, double subtotal) {
        this.product = product;
        this.qty = qty;
        this.subtotal = subtotal;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}