package com.upb.agripos.payment;

import com.upb.agripos.exception.PaymentException;

public class QRCodePayment implements PaymentMethod {

    @Override
    public double process(double total, double amount) throws PaymentException {
        // Pembayaran QRIS harus nominal pas
        if (amount != total) {
            throw new PaymentException("Pembayaran QRIS harus menggunakan nominal pas: " + total);
        }
        
        // Simulasi verifikasi QRIS
        if (!verifyQRCode()) {
            throw new PaymentException("Gagal memverifikasi kode QRIS");
        }
        
        return 0.0; // Tidak ada kembalian untuk QRIS
    }

    @Override
    public String getName() {
        return "QRIS";
    }
    
    /**
     * Simulasi verifikasi kode QRIS
     */
    private boolean verifyQRCode() {
        // Dalam implementasi real, ini akan connect ke payment gateway
        return true;
    }
}
