package com.upb.agripos.dao;

import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementasi JDBC untuk TransactionDAO (Bab 11)
 */
public class JdbcTransactionDAO implements TransactionDAO {
    private static final Logger LOGGER = Logger.getLogger(JdbcTransactionDAO.class.getName());

    @Override
    public int insert(Transaction transaction) throws Exception {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaksi tidak boleh null");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert transaction header
            String sqlHeader = """
                INSERT INTO transactions (transaction_code, transaction_date, cashier_username, 
                subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            int transactionId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, transaction.getTransactionCode());
                stmt.setTimestamp(2, Timestamp.valueOf(transaction.getTransactionDate()));
                stmt.setString(3, transaction.getCashierUsername());
                stmt.setDouble(4, transaction.getSubtotal());
                stmt.setDouble(5, transaction.getDiscount());
                stmt.setDouble(6, transaction.getTax());
                stmt.setDouble(7, transaction.getTotal());
                stmt.setString(8, transaction.getPaymentMethod());
                stmt.setDouble(9, transaction.getAmountPaid());
                stmt.setDouble(10, transaction.getChangeAmount());
                stmt.setString(11, transaction.getStatus().name());

                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("Insert transaksi gagal");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transactionId = generatedKeys.getInt(1);
                        transaction.setId(transactionId);
                    } else {
                        throw new SQLException("Gagal mendapatkan ID transaksi");
                    }
                }
            }

            // Insert transaction items
            String sqlItem = """
                INSERT INTO transaction_items (transaction_id, product_code, product_name, 
                unit_price, quantity, subtotal) VALUES (?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
                for (TransactionItem item : transaction.getItems()) {
                    stmt.setInt(1, transactionId);
                    stmt.setString(2, item.getProductCode());
                    stmt.setString(3, item.getProductName());
                    stmt.setDouble(4, item.getUnitPrice());
                    stmt.setInt(5, item.getQuantity());
                    stmt.setDouble(6, item.getSubtotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            LOGGER.info("Transaksi berhasil disimpan: " + transaction.getTransactionCode());
            return transactionId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Gagal rollback", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Gagal menyimpan transaksi", e);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Gagal menutup koneksi", e);
                }
            }
        }
    }

    @Override
    public Transaction findById(int id) throws Exception {
        String sql = """
            SELECT id, transaction_code, transaction_date, cashier_username, 
            subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status 
            FROM transactions WHERE id=?
            """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    loadTransactionItems(conn, transaction);
                    return transaction;
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari transaksi: " + id, e);
            throw e;
        }
    }

    @Override
    public Transaction findByCode(String code) throws Exception {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Kode transaksi tidak boleh kosong");
        }

        String sql = """
            SELECT id, transaction_code, transaction_date, cashier_username, 
            subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status 
            FROM transactions WHERE transaction_code=?
            """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    loadTransactionItems(conn, transaction);
                    return transaction;
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari transaksi: " + code, e);
            throw e;
        }
    }

    @Override
    public List<Transaction> findAll() throws Exception {
        String sql = """
            SELECT id, transaction_code, transaction_date, cashier_username, 
            subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status 
            FROM transactions ORDER BY transaction_date DESC
            """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                loadTransactionItems(conn, transaction);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mengambil daftar transaksi", e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByDate(LocalDate date) throws Exception {
        if (date == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh null");
        }

        String sql = """
            SELECT id, transaction_code, transaction_date, cashier_username, 
            subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status 
            FROM transactions WHERE DATE(transaction_date)=? ORDER BY transaction_date DESC
            """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    loadTransactionItems(conn, transaction);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari transaksi tanggal: " + date, e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh null");
        }

        String sql = """
            SELECT id, transaction_code, transaction_date, cashier_username, 
            subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status 
            FROM transactions WHERE DATE(transaction_date) BETWEEN ? AND ? 
            ORDER BY transaction_date DESC
            """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    loadTransactionItems(conn, transaction);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari transaksi rentang tanggal", e);
            throw e;
        }
        return transactions;
    }

    @Override
    public double getDailySalesTotal(LocalDate date) throws Exception {
        if (date == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh null");
        }

        String sql = "SELECT COALESCE(SUM(total), 0) FROM transactions WHERE DATE(transaction_date)=? AND status='COMPLETED'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menghitung total penjualan harian", e);
            throw e;
        }
    }

    @Override
    public int getDailyTransactionCount(LocalDate date) throws Exception {
        if (date == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh null");
        }

        String sql = "SELECT COUNT(*) FROM transactions WHERE DATE(transaction_date)=? AND status='COMPLETED'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menghitung jumlah transaksi harian", e);
            throw e;
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setTransactionCode(rs.getString("transaction_code"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        transaction.setCashierUsername(rs.getString("cashier_username"));
        transaction.setSubtotal(rs.getDouble("subtotal"));
        transaction.setDiscount(rs.getDouble("discount"));
        transaction.setTax(rs.getDouble("tax"));
        transaction.setTotal(rs.getDouble("total"));
        transaction.setPaymentMethod(rs.getString("payment_method"));
        transaction.setAmountPaid(rs.getDouble("amount_paid"));
        transaction.setChangeAmount(rs.getDouble("change_amount"));
        transaction.setStatus(Transaction.TransactionStatus.valueOf(rs.getString("status")));
        return transaction;
    }

    private void loadTransactionItems(Connection conn, Transaction transaction) throws SQLException {
        String sql = """
            SELECT id, transaction_id, product_code, product_name, unit_price, quantity, subtotal 
            FROM transaction_items WHERE transaction_id=?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TransactionItem item = new TransactionItem();
                    item.setId(rs.getInt("id"));
                    item.setTransactionId(rs.getInt("transaction_id"));
                    item.setProductCode(rs.getString("product_code"));
                    item.setProductName(rs.getString("product_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setSubtotal(rs.getDouble("subtotal"));
                    transaction.addItem(item);
                }
            }
        }
    }
}
