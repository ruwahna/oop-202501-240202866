package com.upb.agripos.service.payment;

import com.upb.agripos.exception.PaymentException;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Implementasi pembayaran E-Wallet (FR-3)
 * Menerapkan Strategy Pattern
 * Mock implementation untuk demo
 */
public class EWalletPayment implements PaymentMethod {
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private String walletProvider;
    private String transactionRef;

    public EWalletPayment() {
        this.walletProvider = "E-Wallet";
    }

    public EWalletPayment(String walletProvider) {
        this.walletProvider = walletProvider;
    }

    @Override
    public String getMethodName() {
        return "E-Wallet (" + walletProvider + ")";
    }

    @Override
    public double processPayment(double total, double amountPaid) throws PaymentException {
        if (!validatePayment(total, amountPaid)) {
            throw new PaymentException(String.format(
                "Pembayaran E-Wallet tidak valid. Total: %s, Saldo: %s",
                currencyFormat.format(total), currencyFormat.format(amountPaid)));
        }
        
        // Generate mock transaction reference
        this.transactionRef = "EW" + System.currentTimeMillis();
        
        // E-Wallet biasanya exact amount (tidak ada kembalian)
        // Tapi untuk fleksibilitas, bisa kembalikan ke saldo
        return amountPaid - total;
    }

    @Override
    public boolean validatePayment(double total, double amountPaid) {
        // E-Wallet payment harus exact atau lebih
        return amountPaid >= total && total > 0;
    }

    @Override
    public String getReceiptDescription(double amountPaid, double change) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode: ").append(getMethodName()).append("\n");
        sb.append("Ref: ").append(transactionRef != null ? transactionRef : "-").append("\n");
        sb.append("Dibayar: ").append(currencyFormat.format(amountPaid));
        if (change > 0) {
            sb.append("\nKembali ke Saldo: ").append(currencyFormat.format(change));
        }
        return sb.toString();
    }

    public String getWalletProvider() {
        return walletProvider;
    }

    public void setWalletProvider(String walletProvider) {
        this.walletProvider = walletProvider;
    }

    public String getTransactionRef() {
        return transactionRef;
    }
}
