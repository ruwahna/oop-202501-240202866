package com.upb.agripos.exception;

public class PaymentException extends Exception {
    private String paymentMethod;
    private double amount;
    private String errorCode;

    public PaymentException(String message) {
        super(message);
        this.paymentMethod = "Unknown";
        this.amount = 0;
        this.errorCode = "UNKNOWN";
    }

    public PaymentException(String paymentMethod, String message) {
        super("Payment error with " + paymentMethod + ": " + message);
        this.paymentMethod = paymentMethod;
        this.amount = 0;
        this.errorCode = "ERROR";
    }

    public PaymentException(String paymentMethod, double amount, String errorCode, String message) {
        super("Payment error - Method: " + paymentMethod + ", Amount: " + amount + ", Code: " + errorCode + ", Message: " + message);
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.errorCode = errorCode;
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
        this.paymentMethod = "Unknown";
        this.amount = 0;
        this.errorCode = "UNKNOWN";
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isInsufficientFunds() {
        return errorCode != null && errorCode.contains("INSUFFICIENT");
    }

    public boolean isConnectionError() {
        return errorCode != null && errorCode.contains("CONNECTION");
    }
}