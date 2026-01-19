package com.upb.agripos.model;

/**
 * Model class untuk item dalam transaksi
 * Menyimpan snapshot data produk saat transaksi
 */
public class TransactionItem {
    private int id;
    private int transactionId;
    private String productCode;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double subtotal;

    public TransactionItem() {}

    public TransactionItem(String productCode, String productName, double unitPrice, int quantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    /**
     * Factory method dari CartItem
     */
    public static TransactionItem fromCartItem(CartItem cartItem) {
        return new TransactionItem(
            cartItem.getProductCode(),
            cartItem.getProductName(),
            cartItem.getUnitPrice(),
            cartItem.getQuantity()
        );
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) x%d @ Rp %.2f = Rp %.2f", 
            productName, productCode, quantity, unitPrice, subtotal);
    }
}
