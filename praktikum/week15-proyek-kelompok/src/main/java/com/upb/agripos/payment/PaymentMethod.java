package com.upb.agripos.payment;

import com.upb.agripos.exception.PaymentException;

public interface PaymentMethod {
    /**
     * Memproses logika pembayaran.
     * @param total Harga yang harus dibayar
     * @param amount Uang yang diberikan pelanggan
     * @return Nilai kembalian (jika ada)
     * @throws PaymentException Jika saldo/uang tidak cukup
     */
    double process(double total, double amount) throws PaymentException;
    
    String getName();
}