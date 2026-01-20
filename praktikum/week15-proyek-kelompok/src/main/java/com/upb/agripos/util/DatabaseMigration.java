package com.upb.agripos.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Utility untuk migrasi database
 * Memastikan kolom discount ada di tabel transactions
 */
public class DatabaseMigration {
    private static final Logger LOGGER = Logger.getLogger(DatabaseMigration.class.getName());

    /**
     * Jalankan migrasi database jika diperlukan
     */
    public static void runMigrations() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            // Check apakah kolom discount sudah ada
            if (!columnExists(conn, "transactions", "discount")) {
                LOGGER.info("Menjalankan migrasi: menambahkan kolom discount...");
                addDiscountColumn(conn);
                LOGGER.info("✓ Kolom discount berhasil ditambahkan");
            } else {
                LOGGER.info("✓ Kolom discount sudah ada, migrasi tidak diperlukan");
            }
            
            conn.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Gagal menjalankan migrasi database", e);
            throw new RuntimeException("Database migration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Cek apakah kolom ada di tabel
     */
    private static boolean columnExists(Connection conn, String tableName, String columnName) throws Exception {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(null, "public", tableName.toLowerCase(), columnName.toLowerCase())) {
            return rs.next();
        }
    }

    /**
     * Tambahkan kolom discount ke tabel transactions
     */
    private static void addDiscountColumn(Connection conn) throws Exception {
        String sql = """
            ALTER TABLE transactions ADD COLUMN discount DECIMAL(12,2) NOT NULL DEFAULT 0
            """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            conn.commit();
        }
    }
}
