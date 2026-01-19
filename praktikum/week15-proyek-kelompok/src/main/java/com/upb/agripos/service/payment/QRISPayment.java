package com.upb.agripos.service.payment;

/**
 * Implementasi pembayaran via QRIS (Quick Response Code Indonesian Standard)
 * Strategy Pattern - Concrete Strategy
 */
public class QRISPayment implements PaymentMethod {
    
    @Override
    public String getMethodName() {
        return "QRIS";
    }

    @Override
    public double processPayment(double amount, double paid) {
        // QRIS selalu pembayaran pas (exact amount)
        // Dalam praktik nyata, ini akan memverifikasi dengan payment gateway
        if (paid < amount) {
            throw new IllegalArgumentException("Pembayaran QRIS kurang dari total: " + paid + " < " + amount);
        }
        return paid - amount; // Kembalian (biasanya 0 untuk QRIS)
    }

    @Override
    public boolean validatePayment(double amount, double paid) {
        return paid >= amount;
    }

    @Override
    public String getReceiptDescription(double amountPaid, double change) {
        return String.format("Pembayaran QRIS: Rp %.0f", amountPaid);
    }
}
