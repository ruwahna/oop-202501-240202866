package com.upb.agripos.service;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.CheckoutSummary;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Service untuk generate struk/receipt (FR-4)
 */
public class ReceiptService {
    private static final String STORE_NAME = "AGRI-POS";
    private static final String STORE_ADDRESS = "Jl. Pertanian No. 123";
    private static final String STORE_PHONE = "Tel: (021) 123-4567";
    
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    public ReceiptService() {
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    /**
     * Generate struk dari data checkout
     */
    public String generateReceipt(List<CartItem> items, CheckoutSummary summary, String cashierName) {
        StringBuilder sb = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();

        // Header
        sb.append(centerText("================================", 40)).append("\n");
        sb.append(centerText(STORE_NAME, 40)).append("\n");
        sb.append(centerText(STORE_ADDRESS, 40)).append("\n");
        sb.append(centerText(STORE_PHONE, 40)).append("\n");
        sb.append(centerText("================================", 40)).append("\n\n");

        // Info transaksi
        sb.append("Tanggal : ").append(now.format(dateFormatter)).append("\n");
        sb.append("Waktu   : ").append(now.format(timeFormatter)).append("\n");
        sb.append("Kasir   : ").append(cashierName).append("\n");
        sb.append("--------------------------------\n");

        // Items
        for (CartItem item : items) {
            sb.append(String.format("%-20s\n", truncate(item.getProductName(), 20)));
            sb.append(String.format("  %d x %s = %s\n",
                item.getQuantity(),
                formatCurrency(item.getUnitPrice()),
                formatCurrency(item.getSubtotal())));
        }

        sb.append("--------------------------------\n");

        // Summary
        sb.append(String.format("%-15s %15s\n", "Subtotal:", formatCurrency(summary.getSubtotal())));
        if (summary.getDiscount() > 0) {
            sb.append(String.format("%-15s %15s\n", "Diskon:", "-" + formatCurrency(summary.getDiscount())));
        }
        sb.append(String.format("%-15s %15s\n", "Pajak (10%):", formatCurrency(summary.getTax())));
        sb.append("================================\n");
        sb.append(String.format("%-15s %15s\n", "TOTAL:", formatCurrency(summary.getTotal())));
        sb.append("================================\n");

        // Pembayaran
        sb.append(String.format("%-15s %15s\n", "Metode:", summary.getPaymentMethod()));
        sb.append(String.format("%-15s %15s\n", "Dibayar:", formatCurrency(summary.getAmountPaid())));
        sb.append(String.format("%-15s %15s\n", "Kembalian:", formatCurrency(summary.getChangeAmount())));

        // Footer
        sb.append("\n");
        sb.append(centerText("--------------------------------", 40)).append("\n");
        sb.append(centerText("Terima kasih telah berbelanja!", 40)).append("\n");
        sb.append(centerText("Barang yang sudah dibeli", 40)).append("\n");
        sb.append(centerText("tidak dapat dikembalikan", 40)).append("\n");
        sb.append(centerText("================================", 40));

        return sb.toString();
    }

    /**
     * Generate struk dari Transaction yang tersimpan
     */
    public String generateReceipt(Transaction transaction) {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append(centerText("================================", 40)).append("\n");
        sb.append(centerText(STORE_NAME, 40)).append("\n");
        sb.append(centerText(STORE_ADDRESS, 40)).append("\n");
        sb.append(centerText(STORE_PHONE, 40)).append("\n");
        sb.append(centerText("================================", 40)).append("\n\n");

        // Info transaksi
        sb.append("No. Trans: ").append(transaction.getTransactionCode()).append("\n");
        sb.append("Tanggal : ").append(transaction.getTransactionDate().format(dateFormatter)).append("\n");
        sb.append("Waktu   : ").append(transaction.getTransactionDate().format(timeFormatter)).append("\n");
        sb.append("Kasir   : ").append(transaction.getCashierUsername()).append("\n");
        sb.append("--------------------------------\n");

        // Items
        for (TransactionItem item : transaction.getItems()) {
            sb.append(String.format("%-20s\n", truncate(item.getProductName(), 20)));
            sb.append(String.format("  %d x %s = %s\n",
                item.getQuantity(),
                formatCurrency(item.getUnitPrice()),
                formatCurrency(item.getSubtotal())));
        }

        sb.append("--------------------------------\n");

        // Summary
        sb.append(String.format("%-15s %15s\n", "Subtotal:", formatCurrency(transaction.getSubtotal())));
        if (transaction.getDiscount() > 0) {
            sb.append(String.format("%-15s %15s\n", "Diskon:", "-" + formatCurrency(transaction.getDiscount())));
        }
        sb.append(String.format("%-15s %15s\n", "Pajak (10%):", formatCurrency(transaction.getTax())));
        sb.append("================================\n");
        sb.append(String.format("%-15s %15s\n", "TOTAL:", formatCurrency(transaction.getTotal())));
        sb.append("================================\n");

        // Pembayaran
        sb.append(String.format("%-15s %15s\n", "Metode:", transaction.getPaymentMethod()));
        sb.append(String.format("%-15s %15s\n", "Dibayar:", formatCurrency(transaction.getAmountPaid())));
        sb.append(String.format("%-15s %15s\n", "Kembalian:", formatCurrency(transaction.getChangeAmount())));

        // Footer
        sb.append("\n");
        sb.append(centerText("--------------------------------", 40)).append("\n");
        sb.append(centerText("Terima kasih telah berbelanja!", 40)).append("\n");
        sb.append(centerText("================================", 40));

        return sb.toString();
    }

    /**
     * Generate preview struk (sebelum pembayaran)
     */
    public String generatePreview(List<CartItem> items, double subtotal, double tax, double total, 
                                 String cashierName) {
        StringBuilder sb = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();

        sb.append(centerText("======= PREVIEW STRUK =======", 40)).append("\n");
        sb.append(centerText(STORE_NAME, 40)).append("\n\n");

        sb.append("Tanggal : ").append(now.format(dateFormatter)).append("\n");
        sb.append("Kasir   : ").append(cashierName).append("\n");
        sb.append("--------------------------------\n");

        for (CartItem item : items) {
            sb.append(String.format("%-20s\n", truncate(item.getProductName(), 20)));
            sb.append(String.format("  %d x %s = %s\n",
                item.getQuantity(),
                formatCurrency(item.getUnitPrice()),
                formatCurrency(item.getSubtotal())));
        }

        sb.append("--------------------------------\n");
        sb.append(String.format("%-15s %15s\n", "Subtotal:", formatCurrency(subtotal)));
        sb.append(String.format("%-15s %15s\n", "Pajak (10%):", formatCurrency(tax)));
        sb.append("================================\n");
        sb.append(String.format("%-15s %15s\n", "TOTAL:", formatCurrency(total)));
        sb.append("================================\n");
        sb.append(centerText("** BELUM DIBAYAR **", 40));

        return sb.toString();
    }

    private String formatCurrency(double value) {
        return currencyFormat.format(value);
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
