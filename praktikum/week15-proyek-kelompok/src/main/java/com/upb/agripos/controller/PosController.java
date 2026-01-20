package com.upb.agripos.controller;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.CheckoutSummary;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.service.*;
import com.upb.agripos.service.discount.*;
import com.upb.agripos.service.payment.PaymentMethodFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main POS Controller - menghubungkan View dengan Service layer
 * Tidak ada SQL/CRUD di sini (sesuai SOLID/DIP)
 */
public class PosController {
    private static final Logger LOGGER = Logger.getLogger(PosController.class.getName());

    private final ProductService productService;
    private final CartService cartService;
    private final TransactionService transactionService;
    private final ReceiptService receiptService;
    private final ReportService reportService;
    private final AuthService authService;

    private final ObservableList<Product> productList;
    private final ObservableList<CartItem> cartItems;
    private final NumberFormat currencyFormat;

    public PosController(ProductService productService, CartService cartService,
                        TransactionService transactionService, ReceiptService receiptService,
                        ReportService reportService, AuthService authService) {
        this.productService = productService;
        this.cartService = cartService;
        this.transactionService = transactionService;
        this.receiptService = receiptService;
        this.reportService = reportService;
        this.authService = authService;

        this.productList = FXCollections.observableArrayList();
        this.cartItems = FXCollections.observableArrayList();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    }

    // === Product Management (FR-1) ===

    public void loadProducts() {
        try {
            List<Product> products = productService.findAll();
            Platform.runLater(() -> productList.setAll(products));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Gagal memuat produk", e);
            throw new RuntimeException("Gagal memuat produk: " + e.getMessage(), e);
        }
    }

    public void addProduct(String code, String name, String category, double price, int stock) throws Exception {
        Product product = new Product(code, name, category, price, stock);
        productService.addProduct(product);
        loadProducts();
    }

    public void updateProduct(String code, String name, String category, double price, int stock) throws Exception {
        Product product = new Product(code, name, category, price, stock);
        productService.updateProduct(product);
        loadProducts();
    }

    public void deleteProduct(String code) throws Exception {
        productService.deleteProduct(code);
        loadProducts();
    }

    public Product getProductByCode(String code) throws Exception {
        return productService.findByCode(code);
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    // === Cart Management (FR-2) ===

    public void addToCart(Product product, int quantity) throws Exception {
        cartService.addItem(product, quantity);
        refreshCartItems();
    }

    public void addToCart(String productCode, int quantity) throws Exception {
        cartService.addItem(productCode, quantity);
        refreshCartItems();
    }

    public void removeFromCart(String productCode) throws Exception {
        cartService.removeItem(productCode);
        refreshCartItems();
    }

    public void updateCartQuantity(String productCode, int newQuantity) throws Exception {
        cartService.updateQuantity(productCode, newQuantity);
        refreshCartItems();
    }

    public void clearCart() {
        cartService.clearCart();
        refreshCartItems();
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public double getCartTotal() {
        return cartService.getCartTotal();
    }

    public int getCartItemCount() {
        return cartService.getItemCount();
    }

    public boolean isCartEmpty() {
        return cartService.isCartEmpty();
    }

    // === Discount Management (FR-2 OFR-2) ===

    public void applyPercentageDiscount(double percentage, String name) throws Exception {
        if (isCartEmpty()) {
            throw new Exception("Keranjang kosong");
        }
        PercentageDiscount discount = new PercentageDiscount(percentage, name);
        cartService.applyDiscount(discount);
    }

    public void applyFixedDiscount(double amount, String name) throws Exception {
        if (isCartEmpty()) {
            throw new Exception("Keranjang kosong");
        }
        FixedDiscount discount = new FixedDiscount(amount, name);
        cartService.applyDiscount(discount);
    }

    public void applyBulkDiscount(int minQuantity, double percentage, String name) throws Exception {
        if (isCartEmpty()) {
            throw new Exception("Keranjang kosong");
        }
        BulkDiscount discount = new BulkDiscount(minQuantity, percentage, name);
        cartService.applyDiscount(discount);
    }

    public void applyVoucherDiscount(String code) throws Exception {
        if (isCartEmpty()) {
            throw new Exception("Keranjang kosong");
        }
        
        VoucherDiscount voucher = null;
        switch (code.toUpperCase()) {
            case "WELCOME":
                voucher = new VoucherDiscount("WELCOME", new PercentageDiscount(5, "Diskon 5%"), 999);
                break;
            case "MEMBER10":
                voucher = new VoucherDiscount("MEMBER10", new PercentageDiscount(10, "Diskon 10%"), 999);
                break;
            case "PROMO50K":
                voucher = new VoucherDiscount("PROMO50K", new FixedDiscount(50000, "Rp 50.000"), 100);
                break;
            case "BULKDISKON":
                voucher = new VoucherDiscount("BULKDISKON", new BulkDiscount(5, 15, "Bulk 15%"), 999);
                break;
            default:
                throw new Exception("Kode voucher tidak valid: " + code);
        }
        
        if (voucher != null && !voucher.use()) {
            throw new Exception("Voucher sudah exceed max usage");
        }
        
        cartService.applyDiscount(voucher);
    }

    public void clearAllDiscounts() {
        cartService.clearAllDiscounts();
    }

    public String getDiscountSummary() {
        return cartService.getDiscountSummary();
    }

    public double getTotalDiscount() {
        return cartService.calculateTotalDiscount();
    }

    private void refreshCartItems() {
        Platform.runLater(() -> {
            cartItems.setAll(cartService.getCartItems());
        });
    }

    // === Checkout & Payment (FR-2, FR-3) ===

    public CheckoutSummary previewCheckout() throws Exception {
        return transactionService.previewCheckout();
    }

    public CheckoutSummary processCheckout(String paymentMethod, double amountPaid) throws Exception {
        String cashier = authService.getCurrentUsername();
        CheckoutSummary summary = transactionService.processCheckout(cashier, paymentMethod, amountPaid);
        loadProducts(); // Refresh products untuk update stok
        refreshCartItems();
        return summary;
    }

    public Set<String> getAvailablePaymentMethods() {
        return PaymentMethodFactory.getAvailableMethods();
    }

    // === Receipt (FR-4) ===

    public String generateReceipt(CheckoutSummary summary) {
        List<CartItem> items = cartService.getCartItems();
        String cashier = authService.getCurrentUsername();
        return receiptService.generateReceipt(items, summary, cashier);
    }

    public String generateReceipt(Transaction transaction) {
        return receiptService.generateReceipt(transaction);
    }

    public String generatePreviewReceipt() throws Exception {
        CheckoutSummary preview = previewCheckout();
        List<CartItem> items = cartService.getCartItems();
        String cashier = authService.getCurrentUsername();
        return receiptService.generatePreview(items, preview.getSubtotal(), 
                                             preview.getTax(), preview.getTotal(), cashier);
    }

    // === Reports (FR-4) ===

    public String generateDailyReport(LocalDate date) throws Exception {
        return reportService.generateDailyReport(date);
    }

    public String generatePeriodReport(LocalDate startDate, LocalDate endDate) throws Exception {
        return reportService.generatePeriodReport(startDate, endDate);
    }

    public ReportService.DashboardSummary getDashboardSummary() throws Exception {
        return reportService.getDashboardSummary(LocalDate.now());
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) throws Exception {
        return transactionService.getTransactionsByDate(date);
    }

    public List<Transaction> getAllTransactions() throws Exception {
        return transactionService.getAllTransactions();
    }

    // === Authentication (FR-5) ===

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    public boolean isAdmin() {
        return authService.isAdmin();
    }

    public boolean isKasir() {
        return authService.isKasir();
    }

    public String getCurrentUsername() {
        return authService.getCurrentUsername();
    }

    // === Dashboard Statistics ===

    public double getTodaySales() {
        try {
            return reportService.getSalesByDate(LocalDate.now());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Gagal mendapatkan penjualan hari ini", e);
            return 0;
        }
    }

    public double getWeekSales() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(7);
            return reportService.getSalesByPeriod(weekAgo, today);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Gagal mendapatkan penjualan minggu ini", e);
            return 0;
        }
    }

    public double getMonthSales() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate monthStart = today.withDayOfMonth(1);
            return reportService.getSalesByPeriod(monthStart, today);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Gagal mendapatkan penjualan bulan ini", e);
            return 0;
        }
    }

    public int getTotalTransactionsToday() {
        try {
            return reportService.getTransactionCountByDate(LocalDate.now());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Gagal mendapatkan jumlah transaksi", e);
            return 0;
        }
    }

    public double getSalesByDate(LocalDate date) {
        try {
            return reportService.getSalesByDate(date);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Gagal mendapatkan penjualan per tanggal", e);
            return 0;
        }
    }

    // === Utility ===

    public String formatCurrency(double value) {
        return currencyFormat.format(value);
    }

    public double getTaxRate() {
        return transactionService.getTaxRate();
    }
}
