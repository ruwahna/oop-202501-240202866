package com.upb.agripos.model;

/**
 * Data Transfer Object untuk ringkasan checkout
 * Immutable class untuk menyimpan hasil perhitungan checkout
 */
public class CheckoutSummary {
    private final double subtotal;
    private final double discount;
    private final double tax;
    private final double total;
    private final int distinctItemCount;
    private final int totalQuantity;
    private final String paymentMethod;
    private final double amountPaid;
    private final double changeAmount;

    public CheckoutSummary(double subtotal, double tax, double total, 
                          int distinctItemCount, int totalQuantity) {
        this(subtotal, 0, tax, total, distinctItemCount, totalQuantity, "Tunai", total, 0);
    }

    public CheckoutSummary(double subtotal, double discount, double tax, double total, 
                          int distinctItemCount, int totalQuantity,
                          String paymentMethod, double amountPaid, double changeAmount) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.tax = tax;
        this.total = total;
        this.distinctItemCount = distinctItemCount;
        this.totalQuantity = totalQuantity;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.changeAmount = changeAmount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public int getDistinctItemCount() {
        return distinctItemCount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    @Override
    public String toString() {
        return String.format("CheckoutSummary[Items: %d, Qty: %d, Subtotal: %.2f, Discount: %.2f, Tax: %.2f, Total: %.2f]",
            distinctItemCount, totalQuantity, subtotal, discount, tax, total);
    }
}
