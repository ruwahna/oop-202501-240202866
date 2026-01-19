package com.upb.agripos.service.payment;

import com.upb.agripos.exception.PaymentException;

/**
 * Interface Strategy untuk metode pembayaran (FR-3)
 * Menerapkan Strategy Pattern untuk Open-Closed Principle (OCP)
 * Penambahan metode pembayaran baru tidak mengubah kode inti
 */
public interface PaymentMethod {
    
    /**
     * Mendapatkan nama metode pembayaran
     * @return nama metode pembayaran
     */
    String getMethodName();

    /**
     * Memproses pembayaran
     * @param total jumlah yang harus dibayar
     * @param amountPaid jumlah yang dibayarkan
     * @return kembalian (jika ada)
     * @throws PaymentException jika pembayaran gagal
     */
    double processPayment(double total, double amountPaid) throws PaymentException;

    /**
     * Validasi apakah pembayaran dapat dilakukan
     * @param total jumlah yang harus dibayar
     * @param amountPaid jumlah yang dibayarkan
     * @return true jika valid
     */
    boolean validatePayment(double total, double amountPaid);

    /**
     * Mendapatkan deskripsi metode pembayaran untuk struk
     * @param amountPaid jumlah yang dibayarkan
     * @param change kembalian
     * @return deskripsi untuk struk
     */
    String getReceiptDescription(double amountPaid, double change);
}
