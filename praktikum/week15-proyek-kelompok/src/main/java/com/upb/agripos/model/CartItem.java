package com.upb.agripos.model;

/**
 * Model class untuk item dalam keranjang (FR-2 Transaksi Penjualan)
 * Menerapkan composition dengan Product
 */
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Menghitung subtotal item (harga x quantity)
     */
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    /**
     * Mendapatkan kode produk
     */
    public String getProductCode() {
        return product.getCode();
    }

    /**
     * Mendapatkan nama produk
     */
    public String getProductName() {
        return product.getName();
    }

    /**
     * Mendapatkan harga satuan produk
     */
    public double getUnitPrice() {
        return product.getPrice();
    }

    @Override
    public String toString() {
        return String.format("%s x%d = Rp %.2f", 
            product.getName(), quantity, getSubtotal());
    }
}
