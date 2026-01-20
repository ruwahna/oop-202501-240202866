package com.upb.agripos.service;

import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.discount.DiscountStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer untuk Cart/Keranjang (FR-2 Transaksi Penjualan)
 * Menerapkan SRP - Single Responsibility Principle
 */
public class CartService {
    private final Cart cart;
    private final ProductService productService;
    private final List<DiscountStrategy> appliedDiscounts;

    public CartService(ProductService productService) {
        this.cart = new Cart();
        this.productService = productService;
        this.appliedDiscounts = new ArrayList<>();
    }

    /**
     * Menambah item ke keranjang
     * @param productCode kode produk
     * @param quantity jumlah
     * @throws ValidationException jika input tidak valid
     * @throws OutOfStockException jika stok tidak mencukupi
     */
    public void addItem(String productCode, int quantity) throws Exception {
        if (productCode == null || productCode.trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }
        if (quantity <= 0) {
            throw new ValidationException("Jumlah harus lebih dari 0");
        }

        Product product = productService.findByCode(productCode);
        if (product == null) {
            throw new ValidationException("Produk tidak ditemukan: " + productCode);
        }

        // Cek stok
        int currentQtyInCart = 0;
        if (cart.containsProduct(productCode)) {
            currentQtyInCart = cart.getItem(productCode).getQuantity();
        }
        int totalRequested = currentQtyInCart + quantity;

        if (totalRequested > product.getStock()) {
            throw new OutOfStockException(productCode, totalRequested, product.getStock());
        }

        cart.addItem(product, quantity);
    }

    /**
     * Menambah item ke keranjang dengan objek Product
     */
    public void addItem(Product product, int quantity) throws Exception {
        if (product == null) {
            throw new ValidationException("Produk tidak boleh null");
        }
        if (quantity <= 0) {
            throw new ValidationException("Jumlah harus lebih dari 0");
        }
        if (quantity > product.getStock()) {
            throw new OutOfStockException(product.getCode(), quantity, product.getStock());
        }

        cart.addItem(product, quantity);
    }

    /**
     * Menghapus item dari keranjang
     */
    public void removeItem(String productCode) throws ValidationException {
        if (productCode == null || productCode.trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }
        cart.removeItem(productCode);
    }

    /**
     * Mengubah quantity item dalam keranjang
     */
    public void updateQuantity(String productCode, int newQuantity) throws Exception {
        if (productCode == null || productCode.trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }

        if (newQuantity <= 0) {
            // Jika quantity <= 0, hapus item
            cart.removeItem(productCode);
            return;
        }

        // Cek stok
        Product product = productService.findByCode(productCode);
        if (product != null && newQuantity > product.getStock()) {
            throw new OutOfStockException(productCode, newQuantity, product.getStock());
        }

        cart.updateQuantity(productCode, newQuantity);
    }

    /**
     * Mengosongkan keranjang
     */
    public void clearCart() {
        cart.clear();
    }

    /**
     * Mendapatkan semua item dalam keranjang
     */
    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    /**
     * Mendapatkan total harga keranjang
     */
    public double getCartTotal() {
        return cart.getTotal();
    }

    /**
     * Mendapatkan jumlah jenis item
     */
    public int getItemCount() {
        return cart.getItemCount();
    }

    /**
     * Mendapatkan total quantity semua item
     */
    public int getTotalQuantity() {
        return cart.getTotalQuantity();
    }

    /**
     * Cek apakah keranjang kosong
     */
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    /**
     * Mendapatkan objek Cart internal (untuk TransactionService)
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Apply/terapkan diskon ke keranjang
     * @param discount strategi diskon yang akan diterapkan
     * @throws ValidationException jika diskon tidak applicable
     */
    public void applyDiscount(DiscountStrategy discount) throws ValidationException {
        if (discount == null) {
            throw new ValidationException("Diskon tidak boleh null");
        }
        if (!discount.isApplicable(getCartTotal(), getTotalQuantity())) {
            throw new ValidationException("Diskon tidak dapat diterapkan: " + discount.getDescription());
        }
        appliedDiscounts.add(discount);
    }

    /**
     * Hapus diskon dari keranjang
     * @param discount diskon yang akan dihapus
     */
    public void removeDiscount(DiscountStrategy discount) {
        appliedDiscounts.remove(discount);
    }

    /**
     * Hapus semua diskon
     */
    public void clearAllDiscounts() {
        appliedDiscounts.clear();
    }

    /**
     * Hitung total diskon (kombinasi semua diskon yang diterapkan)
     * @return total diskon dalam Rp
     */
    public double calculateTotalDiscount() {
        double totalDiscount = 0;
        double subtotal = cart.getTotal();
        int itemCount = getTotalQuantity();
        
        for (DiscountStrategy discount : appliedDiscounts) {
            totalDiscount += discount.calculateDiscount(subtotal, itemCount);
        }
        
        // Diskon tidak boleh lebih besar dari subtotal
        return Math.min(totalDiscount, subtotal);
    }

    /**
     * Dapatkan daftar diskon yang sedang diterapkan
     * @return list of applied discounts
     */
    public List<DiscountStrategy> getAppliedDiscounts() {
        return new ArrayList<>(appliedDiscounts);
    }

    /**
     * Dapatkan ringkasan diskon untuk ditampilkan ke user
     * @return string ringkasan
     */
    public String getDiscountSummary() {
        if (appliedDiscounts.isEmpty()) {
            return "Tidak ada diskon";
        }
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < appliedDiscounts.size(); i++) {
            if (i > 0) summary.append(" + ");
            summary.append(appliedDiscounts.get(i).getDescription());
        }
        return summary.toString();
    }
}
