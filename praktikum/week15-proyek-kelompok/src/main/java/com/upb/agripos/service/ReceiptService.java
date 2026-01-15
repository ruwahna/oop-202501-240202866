package com.upb.agripos.service;

import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.util.CurrencyFormatter;
import com.upb.agripos.exception.ValidationException;
import java.time.format.DateTimeFormatter;

public class ReceiptService {

    /**
     * Generate struk receipt dalam format text
     */
    public String generateTextReceipt(Transaction trans, double bayar, double kembali) throws ValidationException {
        if (trans == null || trans.getItems() == null || trans.getItems().isEmpty()) {
            throw new ValidationException("Transaksi", "Transaksi tidak valid atau kosong");
        }
        
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sb.append("========== AGRIPOS ==========\n");
        sb.append("ID Trans  : ").append(trans.getId()).append("\n");
        sb.append("Tgl       : ").append(trans.getTanggal().format(dtf)).append("\n");
        sb.append("Status    : ").append(trans.getStatus()).append("\n");
        sb.append("-----------------------------\n");

        for (TransactionItem item : trans.getItems()) {
            sb.append(String.format("%-15s x%d\n", item.getProduct().getNama(), item.getQty()));
            sb.append(String.format("  @%-12s %10s\n", 
                CurrencyFormatter.toRupiah(item.getProduct().getHarga()),
                CurrencyFormatter.toRupiah(item.getSubtotal())));
        }

        sb.append("-----------------------------\n");
        sb.append(String.format("TOTAL     : %s\n", CurrencyFormatter.toRupiah(trans.getTotal())));
        sb.append(String.format("BAYAR     : %s\n", CurrencyFormatter.toRupiah(bayar)));
        sb.append(String.format("KEMBALI   : %s\n", CurrencyFormatter.toRupiah(kembali)));
        sb.append("=============================\n");
        sb.append("    Terima Kasih Belanja!    \n");

        return sb.toString();
    }

    /**
     * Generate detailed receipt
     */
    public String generateDetailedReceipt(Transaction trans, String cashier, double bayar, double kembali) throws ValidationException {
        if (trans == null) {
            throw new ValidationException("Transaksi", "Transaksi tidak boleh null");
        }
        
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        sb.append("============== STRUK BELANJA ==============\n");
        sb.append("Toko: AGRIPOS POS System\n");
        sb.append("==========================================\n");
        sb.append("No. Transaksi : ").append(trans.getId()).append("\n");
        sb.append("Tanggal       : ").append(trans.getTanggal().format(dtf)).append("\n");
        sb.append("Kasir         : ").append(cashier != null ? cashier : "Unknown").append("\n");
        sb.append("------------------------------------------\n");

        for (TransactionItem item : trans.getItems()) {
            sb.append(String.format("%-20s %3d x %8s = %12s\n", 
                item.getProduct().getNama(),
                item.getQty(),
                CurrencyFormatter.toRupiah(item.getProduct().getHarga()),
                CurrencyFormatter.toRupiah(item.getSubtotal())));
        }

        sb.append("------------------------------------------\n");
        sb.append(String.format("Total         : %35s\n", CurrencyFormatter.toRupiah(trans.getTotal())));
        sb.append(String.format("Bayar         : %35s\n", CurrencyFormatter.toRupiah(bayar)));
        sb.append(String.format("Kembalian     : %35s\n", CurrencyFormatter.toRupiah(kembali)));
        sb.append("==========================================\n");
        sb.append("       TERIMA KASIH TELAH BERBELANJA       \n");
        sb.append("==========================================\n");

        return sb.toString();
    }
}