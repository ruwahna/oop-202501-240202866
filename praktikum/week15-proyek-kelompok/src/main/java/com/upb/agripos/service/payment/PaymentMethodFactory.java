package com.upb.agripos.service.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Factory/Registry untuk metode pembayaran
 * Memudahkan penambahan metode pembayaran baru (OCP)
 */
public class PaymentMethodFactory {
    private static final Map<String, PaymentMethod> paymentMethods = new HashMap<>();

    static {
        // Register default payment methods
        // Nama method harus sesuai dengan getMethodName() masing-masing class
        registerPaymentMethod(new CashPayment());              // "Tunai"
        registerPaymentMethod(new EWalletPayment("OVO"));      // "E-Wallet (OVO)"
        registerPaymentMethod(new EWalletPayment("GoPay"));    // "E-Wallet (GoPay)"
        registerPaymentMethod(new EWalletPayment("DANA"));     // "E-Wallet (DANA)"
        registerPaymentMethod(new EWalletPayment("ShopeePay"));// "E-Wallet (ShopeePay)"
        registerPaymentMethod(new EWalletPayment("LinkAja"));  // "E-Wallet (LinkAja)"
        registerPaymentMethod(new QRISPayment());              // "QRIS"
    }

    /**
     * Mendaftarkan metode pembayaran baru
     * @param paymentMethod metode pembayaran
     */
    public static void registerPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.put(paymentMethod.getMethodName(), paymentMethod);
    }

    /**
     * Mendapatkan metode pembayaran berdasarkan nama
     * @param methodName nama metode
     * @return PaymentMethod atau null jika tidak ditemukan
     */
    public static PaymentMethod getPaymentMethod(String methodName) {
        return paymentMethods.get(methodName);
    }

    /**
     * Mendapatkan semua nama metode pembayaran yang tersedia
     * @return Set nama metode pembayaran
     */
    public static Set<String> getAvailableMethods() {
        return paymentMethods.keySet();
    }

    /**
     * Cek apakah metode pembayaran tersedia
     * @param methodName nama metode
     * @return true jika tersedia
     */
    public static boolean isMethodAvailable(String methodName) {
        return paymentMethods.containsKey(methodName);
    }
}
