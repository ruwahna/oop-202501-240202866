package com.upb.agripos.service;

import com.upb.agripos.dao.ipml.TransactionDAOImpl;
import com.upb.agripos.dao.ipml.ProductDAOImpl;
import com.upb.agripos.dao.interfaces.TransactionDAO;
import com.upb.agripos.dao.interfaces.ProductDAO;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.exception.ValidationException;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transDAO = new TransactionDAOImpl();
    private final ProductDAO prodDAO = new ProductDAOImpl();
    private final ProductService productService;

    public TransactionService() {
        this.productService = new ProductService();
    }

    /**
     * Checkout transaksi dengan update stok
     * @param trans - Objek transaksi dengan items
     * @throws SQLException - Jika error DB
     * @throws OutOfStockException - Jika stok tidak cukup
     * @throws ValidationException - Jika transaksi tidak valid
     */
    public void checkout(Transaction trans) throws SQLException, OutOfStockException, ValidationException {
        // Validasi input
        if (trans == null || trans.getItems() == null || trans.getItems().isEmpty()) {
            throw new ValidationException("Transaksi", "Transaksi tidak boleh kosong");
        }

        if (trans.getTotal() <= 0) {
            throw new ValidationException("Total", "Total transaksi harus lebih dari 0");
        }

        // Validasi dan decrease stok untuk semua items
        // Harus berhasil untuk SEMUA sebelum save
        for (TransactionItem item : trans.getItems()) {
            if (item.getProduct() == null || item.getProduct().getKode() == null) {
                throw new ValidationException("Produk", "Produk dalam item tidak valid");
            }
            
            if (item.getQty() <= 0) {
                throw new ValidationException("Qty", "Jumlah item harus lebih dari 0");
            }
            
            // Cek stok terlebih dahulu sebelum decrease
            String kode = item.getProduct().getKode();
            int qty = item.getQty();
            Product prod = productService.findByCode(kode);
            
            if (prod == null) {
                throw new ValidationException("Produk", "Produk dengan kode " + kode + " tidak ditemukan");
            }
            
            if (prod.getStok() < qty) {
                throw new OutOfStockException(
                    prod.getNama(),
                    prod.getStok(),
                    qty
                );
            }
        }

        // Semua validasi berhasil, lakukan stock decrease dan save
        for (TransactionItem item : trans.getItems()) {
            String kode = item.getProduct().getKode();
            int qty = item.getQty();
            productService.decreaseStock(kode, qty);
        }

        // Set status default jika belum ada
        if (trans.getStatus() == null || trans.getStatus().isEmpty()) {
            trans.setStatus("SUCCESS");
        }

        // Simpan transaksi ke DB
        transDAO.saveTransaction(trans);
        System.out.println("✓ Transaction ID " + trans.getId() + " checkout successfully");
    }

    /**
     * Get transaction berdasarkan ID
     */
    public Transaction getTransactionById(int id) {
        return transDAO.getTransactionById(id);
    }

    /**
     * Get semua transactions
     */
    public List<Transaction> getAllTransactions() {
        return transDAO.getAllTransactions();
    }

    /**
     * Get transactions berdasarkan status
     */
    public List<Transaction> getTransactionsByStatus(String status) {
        return transDAO.getTransactionsByStatus(status);
    }

    /**
     * Update transaction
     */
    public void updateTransaction(Transaction trans) throws ValidationException {
        if (trans == null || trans.getId() <= 0) {
            throw new ValidationException("Transaksi", "ID transaksi tidak valid");
        }
        transDAO.updateTransaction(trans);
    }

    /**
     * Delete transaction
     */
    public void deleteTransaction(int id) {
        transDAO.deleteTransaction(id);
    }

    /**
     * Get transaction items
     */
    public List<TransactionItem> getTransactionItems(int transactionId) {
        return transDAO.getTransactionItems(transactionId);
    }
}