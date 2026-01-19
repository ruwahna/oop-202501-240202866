package com.upb.agripos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class untuk Shopping Cart (FR-2 Transaksi Penjualan)
 * Menerapkan Collections (Bab 7)
 */
public class Cart {
    private Map<String, CartItem> items; // key: product code

    public Cart() {
        this.items = new HashMap<>();
    }

    /**
     * Menambah item ke keranjang
     * Jika produk sudah ada, quantity akan ditambahkan
     */
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) return;
        
        String code = product.getCode();
        if (items.containsKey(code)) {
            CartItem existing = items.get(code);
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(code, new CartItem(product, quantity));
        }
    }

    /**
     * Menghapus item dari keranjang berdasarkan kode produk
     */
    public void removeItem(String productCode) {
        items.remove(productCode);
    }

    /**
     * Mengubah quantity item dalam keranjang
     */
    public void updateQuantity(String productCode, int newQuantity) {
        if (items.containsKey(productCode)) {
            if (newQuantity <= 0) {
                items.remove(productCode);
            } else {
                items.get(productCode).setQuantity(newQuantity);
            }
        }
    }

    /**
     * Mengosongkan keranjang
     */
    public void clear() {
        items.clear();
    }

    /**
     * Mendapatkan semua item dalam keranjang
     */
    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    /**
     * Mendapatkan item berdasarkan kode produk
     */
    public CartItem getItem(String productCode) {
        return items.get(productCode);
    }

    /**
     * Menghitung total harga keranjang
     */
    public double getTotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    /**
     * Mendapatkan jumlah jenis item (distinct)
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Mendapatkan total quantity semua item
     */
    public int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Cek apakah keranjang kosong
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Cek apakah produk ada dalam keranjang
     */
    public boolean containsProduct(String productCode) {
        return items.containsKey(productCode);
    }
}
