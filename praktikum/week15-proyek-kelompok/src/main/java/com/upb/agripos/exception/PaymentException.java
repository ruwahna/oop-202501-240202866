package com.upb.agripos.exception;

/**
 * Exception untuk pembayaran yang gagal atau tidak valid
 */
public class PaymentException extends Exception {

    public PaymentException() {
        super("Pembayaran gagal");
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
