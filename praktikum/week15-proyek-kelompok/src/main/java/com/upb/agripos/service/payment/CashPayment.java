package com.upb.agripos.service.payment;

import com.upb.agripos.exception.PaymentException;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Implementasi pembayaran Tunai (FR-3)
 * Menerapkan Strategy Pattern
 */
public class CashPayment implements PaymentMethod {
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    public String getMethodName() {
        return "Tunai";
    }

    @Override
    public double processPayment(double total, double amountPaid) throws PaymentException {
        if (!validatePayment(total, amountPaid)) {
            throw new PaymentException(String.format(
                "Pembayaran tunai tidak valid. Total: %s, Dibayar: %s",
                currencyFormat.format(total), currencyFormat.format(amountPaid)));
        }
        return amountPaid - total;
    }

    @Override
    public boolean validatePayment(double total, double amountPaid) {
        // Pembayaran tunai harus >= total
        return amountPaid >= total && total > 0;
    }

    @Override
    public String getReceiptDescription(double amountPaid, double change) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode: TUNAI\n");
        sb.append("Dibayar: ").append(currencyFormat.format(amountPaid)).append("\n");
        sb.append("Kembalian: ").append(currencyFormat.format(change));
        return sb.toString();
    }
}
