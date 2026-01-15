package com.upb.agripos.payment;

import com.upb.agripos.exception.PaymentException;

public class EWalletPayment implements PaymentMethod {

    @Override
    public double process(double total, double amount) throws PaymentException {
        // Pada E-Wallet, jumlah bayar biasanya harus tepat sama dengan total
        if (amount != total) {
            throw new PaymentException("Pembayaran E-Wallet harus menggunakan nominal pas: " + total);
        }
        return 0.0; // Tidak ada kembalian untuk E-Wallet
    }

    @Override
    public String getName() {
        return "E-WALLET";
    }
}