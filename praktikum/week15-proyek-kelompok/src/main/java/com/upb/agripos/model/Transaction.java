package com.upb.agripos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class untuk Transaction (FR-2 Transaksi Penjualan)
 * Menyimpan data transaksi lengkap termasuk item dan pembayaran
 */
public class Transaction {
    private int id;
    private String transactionCode;
    private LocalDateTime transactionDate;
    private String cashierUsername;
    private List<TransactionItem> items;
    private double subtotal;
    private double tax;
    private double total;
    private String paymentMethod;
    private double amountPaid;
    private double changeAmount;
    private TransactionStatus status;

    /**
     * Status transaksi
     */
    public enum TransactionStatus {
        PENDING("Pending"),
        COMPLETED("Selesai"),
        CANCELLED("Dibatalkan");

        private final String displayName;

        TransactionStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Transaction() {
        this.items = new ArrayList<>();
        this.transactionDate = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

    public Transaction(String transactionCode, String cashierUsername) {
        this();
        this.transactionCode = transactionCode;
        this.cashierUsername = cashierUsername;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCashierUsername() {
        return cashierUsername;
    }

    public void setCashierUsername(String cashierUsername) {
        this.cashierUsername = cashierUsername;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }

    public void addItem(TransactionItem item) {
        this.items.add(item);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * Menghitung ulang subtotal dari items
     */
    public void calculateSubtotal() {
        this.subtotal = items.stream()
                .mapToDouble(TransactionItem::getSubtotal)
                .sum();
    }

    /**
     * Menghitung total dengan pajak
     * @param taxRate persentase pajak (mis: 0.10 untuk 10%)
     */
    public void calculateTotal(double taxRate) {
        calculateSubtotal();
        this.tax = subtotal * taxRate;
        this.total = subtotal + tax;
    }

    /**
     * Mendapatkan jumlah item berbeda
     */
    public int getDistinctItemCount() {
        return items.size();
    }

    /**
     * Mendapatkan total quantity
     */
    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(TransactionItem::getQuantity)
                .sum();
    }

    @Override
    public String toString() {
        return String.format("Transaction[%s] %s - Total: Rp %.2f (%s)", 
            transactionCode, transactionDate, total, status.getDisplayName());
    }
}
