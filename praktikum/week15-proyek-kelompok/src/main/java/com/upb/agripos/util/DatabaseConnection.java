package com.upb.agripos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton utility untuk koneksi database PostgreSQL
 * Menerapkan Singleton Pattern (Bab 10)
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            // Memastikan driver PostgreSQL dimuat
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found: " + e.getMessage());
        }
    }

    /**
     * Mendapatkan instance singleton
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Mendapatkan koneksi database baru
     * @return Connection ke database
     * @throws SQLException jika koneksi gagal
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Test koneksi database
     * @return true jika koneksi berhasil
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
