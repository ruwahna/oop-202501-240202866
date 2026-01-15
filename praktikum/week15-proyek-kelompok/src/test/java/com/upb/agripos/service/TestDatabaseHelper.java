package com.upb.agripos.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.lang.reflect.Field;

/**
 * Test database helper untuk unit testing
 */
public class TestDatabaseHelper {
    
    public static void initializeTestDatabase() throws SQLException {
        // Reset database connection dari DatabaseConnection singleton
        resetDatabaseConnection();
        
        String url = "jdbc:h2:mem:agripos_db";
        String user = "sa";
        String password = "";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            
            // Create tables
            stmt.execute("DROP TABLE IF EXISTS transaction_items");
            stmt.execute("DROP TABLE IF EXISTS payments");
            stmt.execute("DROP TABLE IF EXISTS transactions");
            stmt.execute("DROP TABLE IF EXISTS products");
            stmt.execute("DROP TABLE IF EXISTS users");
            
            stmt.execute("CREATE TABLE users (" +
                "  id SERIAL PRIMARY KEY," +
                "  username VARCHAR(50) UNIQUE NOT NULL," +
                "  password VARCHAR(255) NOT NULL," +
                "  role VARCHAR(20) NOT NULL" +
                ")");
            
            stmt.execute("CREATE TABLE products (" +
                "  kode VARCHAR(20) PRIMARY KEY," +
                "  nama VARCHAR(100) NOT NULL," +
                "  kategori VARCHAR(50)," +
                "  harga DECIMAL(12, 2) NOT NULL," +
                "  stok INT NOT NULL" +
                ")");
            
            stmt.execute("CREATE TABLE transactions (" +
                "  id SERIAL PRIMARY KEY," +
                "  tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  total DECIMAL(12, 2) NOT NULL," +
                "  status VARCHAR(20) DEFAULT 'SUCCESS'" +
                ")");
            
            stmt.execute("CREATE TABLE transaction_items (" +
                "  id SERIAL PRIMARY KEY," +
                "  transaction_id INT REFERENCES transactions(id) ON DELETE CASCADE," +
                "  product_kode VARCHAR(20) REFERENCES products(kode)," +
                "  qty INT NOT NULL," +
                "  subtotal DECIMAL(12, 2) NOT NULL" +
                ")");
            
            stmt.execute("CREATE TABLE payments (" +
                "  id SERIAL PRIMARY KEY," +
                "  transaction_id INT REFERENCES transactions(id)," +
                "  metode VARCHAR(20) NOT NULL," +
                "  jumlah_bayar DECIMAL(12, 2) NOT NULL," +
                "  kembalian DECIMAL(12, 2) NOT NULL" +
                ")");
            
            // Insert users
            stmt.execute("INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'Admin')");
            stmt.execute("INSERT INTO users (username, password, role) VALUES ('kasir', 'kasir123', 'Kasir')");
            
            // Insert products
            stmt.execute("INSERT INTO products (kode, nama, kategori, harga, stok) VALUES ('P001', 'Pupuk Urea 5kg', 'Pupuk', 75000, 50)");
            stmt.execute("INSERT INTO products (kode, nama, kategori, harga, stok) VALUES ('P002', 'Benih Padi Unggul', 'Benih', 45000, 100)");
            stmt.execute("INSERT INTO products (kode, nama, kategori, harga, stok) VALUES ('P003', 'Cangkul Baja', 'Alat', 120000, 15)");
            stmt.execute("INSERT INTO products (kode, nama, kategori, harga, stok) VALUES ('P004', 'Pestisida Cair 1L', 'Obat', 85000, 30)");
            
            System.out.println("✓ Test database initialized");
        }
    }
    
    private static void resetDatabaseConnection() {
        try {
            Field connectionField = Class.forName("com.upb.agripos.dao.DatabaseConnection")
                .getDeclaredField("connection");
            connectionField.setAccessible(true);
            Connection oldConn = (Connection) connectionField.get(null);
            if (oldConn != null && !oldConn.isClosed()) {
                oldConn.close();
            }
            connectionField.set(null, null);
            
            Field initializedField = Class.forName("com.upb.agripos.dao.DatabaseConnection")
                .getDeclaredField("initialized");
            initializedField.setAccessible(true);
            initializedField.set(null, false);
            
            System.out.println("✓ Database connection reset");
        } catch (Exception e) {
            System.err.println("Warning: Could not reset database connection: " + e.getMessage());
        }
    }
}
