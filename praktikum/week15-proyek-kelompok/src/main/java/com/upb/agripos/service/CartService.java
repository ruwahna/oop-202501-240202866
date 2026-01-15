package com.upb.agripos.service;

import com.upb.agripos.model.Product;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final List<TransactionItem> cart = new ArrayList<>();

    /**
     * Tambah item ke cart dengan validasi stok
     */
    public void addItem(Product p, int qty) throws OutOfStockException, ValidationException {
        if (p == null) {
            throw new ValidationException("Produk", "Produk tidak boleh null");
        }
        if (qty <= 0) {
            throw new ValidationException("Qty", "Jumlah harus lebih dari 0");
        }
        if (p.getStok() < qty) {
            throw new OutOfStockException(p.getNama(), p.getStok(), qty);
        }
        
        // Cek apakah produk sudah ada di cart
        for (TransactionItem item : cart) {
            if (item.getProduct().getKode().equals(p.getKode())) {
                // Update qty jika sudah ada
                item.setQty(item.getQty() + qty);
                item.setSubtotal(item.getQty() * p.getHarga());
                return;
            }
        }
        
        // Tambah item baru jika belum ada
        cart.add(new TransactionItem(p, qty));
    }

    /**
     * Update qty item di cart
     */
    public void updateItemQty(String kode, int newQty) throws ValidationException, OutOfStockException {
        if (newQty <= 0) {
            removeItemByKode(kode);
            return;
        }
        
        for (TransactionItem item : cart) {
            if (item.getProduct().getKode().equals(kode)) {
                if (item.getProduct().getStok() < newQty) {
                    throw new OutOfStockException(item.getProduct().getNama(), item.getProduct().getStok(), newQty);
                }
                item.setQty(newQty);
                item.setSubtotal(newQty * item.getProduct().getHarga());
                return;
            }
        }
        throw new ValidationException("Produk", "Produk dengan kode " + kode + " tidak ada di cart");
    }

    /**
     * Hitung total cart
     */
    public double calculateTotal() {
        return cart.stream().mapToDouble(TransactionItem::getSubtotal).sum();
    }

    /**
     * Ambil items di cart
     */
    public List<TransactionItem> getCartItems() {
        return new ArrayList<>(cart);
    }

    /**
     * Hapus item dari cart
     */
    public void removeItem(TransactionItem item) {
        cart.remove(item);
    }

    /**
     * Hapus item berdasarkan kode
     */
    public void removeItemByKode(String kode) {
        cart.removeIf(item -> item.getProduct().getKode().equals(kode));
    }

    /**
     * Kosongkan cart
     */
    public void clearCart() {
        cart.clear();
    }

    /**
     * Cek apakah cart kosong
     */
    public boolean isEmpty() {
        return cart.isEmpty();
    }

    /**
     * Ambil jumlah item di cart
     */
    public int getCartSize() {
        return cart.size();
    }

    /**
     * Ambil jumlah total qty
     */
    public int getTotalQty() {
        return (int) cart.stream().mapToInt(TransactionItem::getQty).sum();
    }
}