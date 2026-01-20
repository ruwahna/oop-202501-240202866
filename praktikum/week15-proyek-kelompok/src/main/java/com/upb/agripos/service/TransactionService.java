package com.upb.agripos.service;

import com.upb.agripos.dao.TransactionDAO;
import com.upb.agripos.exception.PaymentException;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.model.*;
import com.upb.agripos.service.payment.PaymentMethod;
import com.upb.agripos.service.payment.PaymentMethodFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service layer untuk Transaction (FR-2, FR-3, FR-4)
 * Mengelola proses checkout, pembayaran, dan transaksi
 */
public class TransactionService {
    private static final double TAX_RATE = 0.10; // 10% pajak
    private static final AtomicInteger transactionCounter = new AtomicInteger(1);
    
    private final TransactionDAO transactionDAO;
    private final ProductService productService;
    private final CartService cartService;

    public TransactionService(TransactionDAO transactionDAO, ProductService productService, CartService cartService) {
        this.transactionDAO = transactionDAO;
        this.productService = productService;
        this.cartService = cartService;
    }

    /**
     * Proses checkout dengan pembayaran
     * @param cashierUsername username kasir
     * @param paymentMethodName nama metode pembayaran
     * @param amountPaid jumlah yang dibayarkan
     * @return CheckoutSummary hasil checkout
     */
    public CheckoutSummary processCheckout(String cashierUsername, String paymentMethodName, double amountPaid) 
            throws Exception {
        
        // Validasi keranjang
        if (cartService.isCartEmpty()) {
            throw new ValidationException("Keranjang kosong, tidak dapat checkout");
        }

        // Ambil metode pembayaran
        PaymentMethod paymentMethod = PaymentMethodFactory.getPaymentMethod(paymentMethodName);
        if (paymentMethod == null) {
            throw new ValidationException("Metode pembayaran tidak valid: " + paymentMethodName);
        }

        // Hitung total
        double subtotal = cartService.getCartTotal();
        double discount = cartService.calculateTotalDiscount();
        double tax = (subtotal - discount) * TAX_RATE;
        double total = subtotal - discount + tax;

        // Proses pembayaran
        double change = paymentMethod.processPayment(total, amountPaid);

        // Buat transaksi
        Transaction transaction = createTransaction(cashierUsername, subtotal, discount, tax, total, 
                                                   paymentMethodName, amountPaid, change);

        // Simpan transaksi ke database
        transactionDAO.insert(transaction);

        // Kurangi stok produk
        for (CartItem item : cartService.getCartItems()) {
            productService.reduceStock(item.getProductCode(), item.getQuantity());
        }

        // Buat summary dengan discount
        CheckoutSummary summary = new CheckoutSummary(
            subtotal, discount, tax, total,
            cartService.getItemCount(),
            cartService.getTotalQuantity(),
            paymentMethodName,
            amountPaid,
            change
        );

        // Kosongkan keranjang
        cartService.clearCart();

        return summary;
    }

    /**
     * Preview checkout tanpa melakukan transaksi
     */
    public CheckoutSummary previewCheckout() throws ValidationException {
        if (cartService.isCartEmpty()) {
            throw new ValidationException("Keranjang kosong");
        }

        double subtotal = cartService.getCartTotal();
        double discount = cartService.calculateTotalDiscount();
        double tax = (subtotal - discount) * TAX_RATE;
        double total = subtotal - discount + tax;

        return new CheckoutSummary(
            subtotal, discount, tax, total,
            cartService.getItemCount(),
            cartService.getTotalQuantity(),
            "Preview", 0, 0
        );
    }

    /**
     * Mendapatkan semua transaksi
     */
    public List<Transaction> getAllTransactions() throws Exception {
        return transactionDAO.findAll();
    }

    /**
     * Mendapatkan transaksi berdasarkan tanggal
     */
    public List<Transaction> getTransactionsByDate(LocalDate date) throws Exception {
        return transactionDAO.findByDate(date);
    }

    /**
     * Mendapatkan transaksi dalam rentang tanggal
     */
    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        return transactionDAO.findByDateRange(startDate, endDate);
    }

    /**
     * Mendapatkan total penjualan harian
     */
    public double getDailySalesTotal(LocalDate date) throws Exception {
        return transactionDAO.getDailySalesTotal(date);
    }

    /**
     * Mendapatkan jumlah transaksi harian
     */
    public int getDailyTransactionCount(LocalDate date) throws Exception {
        return transactionDAO.getDailyTransactionCount(date);
    }

    /**
     * Generate kode transaksi unik
     */
    private String generateTransactionCode() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "TRX" + now.format(formatter) + String.format("%04d", transactionCounter.getAndIncrement());
    }

    /**
     * Membuat objek Transaction dari data checkout
     */
    private Transaction createTransaction(String cashierUsername, double subtotal, double discount, double tax, 
                                         double total, String paymentMethod, double amountPaid, 
                                         double change) {
        Transaction transaction = new Transaction();
        transaction.setTransactionCode(generateTransactionCode());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCashierUsername(cashierUsername);
        transaction.setSubtotal(subtotal);
        transaction.setDiscount(discount);
        transaction.setTax(tax);
        transaction.setTotal(total);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setAmountPaid(amountPaid);
        transaction.setChangeAmount(change);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        // Tambah items dari cart
        for (CartItem cartItem : cartService.getCartItems()) {
            TransactionItem item = TransactionItem.fromCartItem(cartItem);
            transaction.addItem(item);
        }

        return transaction;
    }

    /**
     * Mendapatkan tax rate yang digunakan
     */
    public double getTaxRate() {
        return TAX_RATE;
    }
}
