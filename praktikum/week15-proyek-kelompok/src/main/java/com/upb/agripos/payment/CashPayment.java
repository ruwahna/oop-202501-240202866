package com.upb.agripos.payment;

import com.upb.agripos.exception.PaymentException;

public class CashPayment implements PaymentMethod {

    @Override
    public double process(double total, double amount) throws PaymentException {
        if (amount < total) {
            throw new PaymentException("Uang tunai tidak mencukupi! Kurang: " + (total - amount));
        }
        return amount - total; // Mengembalikan uang kembalian
    }

    @Override
    public String getName() {
        return "TUNAI";
    }
}