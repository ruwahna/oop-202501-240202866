package com.upb.agripos.service;

import com.upb.agripos.payment.PaymentMethod;
import com.upb.agripos.exception.PaymentException;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.dao.ipml.PaymentDAOImpl;
import com.upb.agripos.dao.interfaces.PaymentDAO;
import com.upb.agripos.model.Payment;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    /**
     * Process pembayaran dengan method tertentu
     */
    public double processPayment(PaymentMethod method, double total, double amount, int transId) throws PaymentException, ValidationException {
        if (method == null) {
            throw new ValidationException("Payment Method", "Payment method tidak boleh null");
        }
        if (total <= 0) {
            throw new ValidationException("Total", "Total harus lebih dari 0");
        }
        if (amount <= 0) {
            throw new ValidationException("Amount", "Jumlah pembayaran harus lebih dari 0");
        }
        if (amount < total) {
            throw new PaymentException(method.getName(), total, "INSUFFICIENT_PAYMENT", 
                "Jumlah pembayaran kurang. Dibutuhkan: " + total + ", Diberikan: " + amount);
        }

        try {
            // Eksekusi strategi (Cash atau E-Wallet)
            double kembalian = method.process(total, amount);

            // Simpan data pembayaran ke DB via DAO
            Payment p = new Payment();
            p.setTransactionId(transId);
            p.setMetode(method.getName());
            p.setJumlahBayar(amount);
            p.setKembalian(kembalian);
            paymentDAO.save(p);

            return kembalian;
        } catch (Exception e) {
            throw new PaymentException(method.getName(), amount, "PROCESS_ERROR", e.getMessage());
        }
    }

    /**
     * Get payment berdasarkan transaction ID
     */
    public Payment getPaymentByTransactionId(int transactionId) {
        return paymentDAO.getByTransactionId(transactionId);
    }

    /**
     * Get semua payments
     */
    public java.util.List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    /**
     * Validate pembayaran
     */
    public boolean validatePayment(double total, double amount) {
        return amount >= total && amount > 0 && total > 0;
    }

    /**
     * Hitung kembalian
     */
    public double calculateChange(double total, double amount) throws PaymentException {
        if (!validatePayment(total, amount)) {
            throw new PaymentException("Pembayaran tidak valid");
        }
        return amount - total;
    }
}