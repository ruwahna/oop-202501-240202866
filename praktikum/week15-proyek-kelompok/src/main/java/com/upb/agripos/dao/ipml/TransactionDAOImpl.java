package com.upb.agripos.dao.ipml;

import com.upb.agripos.dao.DatabaseConnection;
import com.upb.agripos.dao.interfaces.TransactionDAO;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
    @Override
    public void saveTransaction(Transaction trans) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaksi Atomik dimulai

            try {
                // 1. Simpan ke tabel transactions
                String sqlT = "INSERT INTO transactions (tanggal, total, status, metode_pembayaran) VALUES (NOW(), ?, ?, ?)";
                try (PreparedStatement psT = conn.prepareStatement(sqlT, Statement.RETURN_GENERATED_KEYS)) {
                    psT.setDouble(1, trans.getTotal());
                    psT.setString(2, trans.getStatus() != null ? trans.getStatus() : "SUCCESS");
                    psT.setString(3, trans.getMetodePembayaran() != null ? trans.getMetodePembayaran() : "Tunai");
                    psT.executeUpdate();

                    ResultSet rs = psT.getGeneratedKeys();
                    if (rs.next()) {
                        int transId = rs.getInt(1);
                        trans.setId(transId);

                        // 2. Simpan item ke tabel transaction_items
                        String sqlI = "INSERT INTO transaction_items (transaction_id, product_kode, qty, subtotal) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement psI = conn.prepareStatement(sqlI)) {
                            for (TransactionItem item : trans.getItems()) {
                                psI.setInt(1, transId);
                                psI.setString(2, item.getProduct().getKode());
                                psI.setInt(3, item.getQty());
                                psI.setDouble(4, item.getSubtotal());
                                psI.addBatch();
                            }
                            psI.executeBatch();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback(); // Rollback jika terjadi error
                System.err.println("Error saving transaction - rollback executed: " + e.getMessage());
                throw e;
            } finally {
                conn.setAutoCommit(true); // Reset autocommit
            }
        } catch (SQLException e) {
            System.err.println("Error in saveTransaction: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Transaction getTransactionById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Transaction trans = mapResultSetToTransaction(rs);
                // Load transaction items
                List<TransactionItem> items = getTransactionItems(id);
                trans.setItems(items);
                return trans;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Transaction trans = mapResultSetToTransaction(rs);
                // Load transaction items untuk setiap transaksi
                List<TransactionItem> items = getTransactionItems(rs.getInt("id"));
                trans.setItems(items);
                transactions.add(trans);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void updateTransaction(Transaction trans) {
        String sql = "UPDATE transactions SET total = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, trans.getTotal());
            ps.setString(2, trans.getStatus());
            ps.setInt(3, trans.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<TransactionItem> getTransactionItems(int transactionId) {
        List<TransactionItem> items = new ArrayList<>();
        String sql = "SELECT ti.*, p.nama, p.harga FROM transaction_items ti " +
                     "JOIN products p ON ti.product_kode = p.kode WHERE ti.transaction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TransactionItem item = new TransactionItem();
                item.setId(rs.getInt("id"));
                item.setQty(rs.getInt("qty"));
                item.setSubtotal(rs.getDouble("subtotal"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction items: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String status) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction trans = mapResultSetToTransaction(rs);
                // Load transaction items
                List<TransactionItem> items = getTransactionItems(rs.getInt("id"));
                trans.setItems(items);
                transactions.add(trans);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions by status: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction trans = new Transaction();
        trans.setId(rs.getInt("id"));
        // Convert SQL Timestamp to LocalDateTime
        java.sql.Timestamp sqlTimestamp = rs.getTimestamp("tanggal");
        if (sqlTimestamp != null) {
            trans.setTanggal(sqlTimestamp.toLocalDateTime());
        }
        trans.setTotal(rs.getDouble("total"));
        trans.setStatus(rs.getString("status"));
        trans.setMetodePembayaran(rs.getString("metode_pembayaran"));
        return trans;
    }
}