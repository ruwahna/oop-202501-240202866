package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection connection;
    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Gunakan H2 file-based database untuk persistence
            String url = "jdbc:h2:./agripos_db;MODE=PostgreSQL;AUTO_SERVER=TRUE";
            String user = "sa";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
            
            // Initialize schema jika belum
            if (!initialized) {
                initializeDatabase();
                initialized = true;
            }
        }
        return connection;
    }
    
    private static void initializeDatabase() throws SQLException {
        String[] sqlStatements = {
            // Create tables
            "CREATE TABLE IF NOT EXISTS users (" +
            "  id SERIAL PRIMARY KEY," +
            "  username VARCHAR(50) UNIQUE NOT NULL," +
            "  password VARCHAR(255) NOT NULL," +
            "  role VARCHAR(20) NOT NULL" +
            ")",
            
            "CREATE TABLE IF NOT EXISTS products (" +
            "  kode VARCHAR(20) PRIMARY KEY," +
            "  nama VARCHAR(100) NOT NULL," +
            "  kategori VARCHAR(50)," +
            "  harga DECIMAL(12, 2) NOT NULL," +
            "  stok INT NOT NULL" +
            ")",
            
            "CREATE TABLE IF NOT EXISTS transactions (" +
            "  id SERIAL PRIMARY KEY," +
            "  tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  total DECIMAL(12, 2) NOT NULL," +
            "  status VARCHAR(20) DEFAULT 'SUCCESS'," +
            "  metode_pembayaran VARCHAR(50)" +
            ")",
            
            "CREATE TABLE IF NOT EXISTS transaction_items (" +
            "  id SERIAL PRIMARY KEY," +
            "  transaction_id INT REFERENCES transactions(id) ON DELETE CASCADE," +
            "  product_kode VARCHAR(20) REFERENCES products(kode)," +
            "  qty INT NOT NULL," +
            "  subtotal DECIMAL(12, 2) NOT NULL" +
            ")",
            
            "CREATE TABLE IF NOT EXISTS payments (" +
            "  id SERIAL PRIMARY KEY," +
            "  transaction_id INT REFERENCES transactions(id)," +
            "  metode VARCHAR(20) NOT NULL," +
            "  jumlah_bayar DECIMAL(12, 2) NOT NULL," +
            "  kembalian DECIMAL(12, 2) NOT NULL" +
            ")",
            
            // Insert default users (use INSERT IGNORE untuk H2)
            "MERGE INTO users (username, password, role) KEY(username) VALUES ('admin', 'admin123', 'Admin')",
            "MERGE INTO users (username, password, role) KEY(username) VALUES ('kasir', 'kasir123', 'Kasir')",
            
            // Insert sample products
            "MERGE INTO products (kode, nama, kategori, harga, stok) KEY(kode) VALUES ('P001', 'Pupuk Urea 5kg', 'Pupuk', 75000, 50)",
            "MERGE INTO products (kode, nama, kategori, harga, stok) KEY(kode) VALUES ('P002', 'Benih Padi Unggul', 'Benih', 45000, 100)",
            "MERGE INTO products (kode, nama, kategori, harga, stok) KEY(kode) VALUES ('P003', 'Cangkul Baja', 'Alat', 120000, 15)",
            "MERGE INTO products (kode, nama, kategori, harga, stok) KEY(kode) VALUES ('P004', 'Pestisida Cair 1L', 'Obat', 85000, 30)"
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String sql : sqlStatements) {
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    // Ignore duplicate table/data errors
                    if (!e.getMessage().contains("already exists")) {
                        System.err.println("Warning executing: " + sql + " - " + e.getMessage());
                    }
                }
            }
            System.out.println("✓ Database initialized successfully");
        }
    }
    
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}