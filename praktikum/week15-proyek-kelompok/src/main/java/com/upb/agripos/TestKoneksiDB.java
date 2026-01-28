package com.upb.agripos;

import com.upb.agripos.util.DatabaseConnection;
import java.sql.*;

/**
 * Class untuk testing koneksi database
 * Jalankan dengan: mvn compile exec:java -Dexec.mainClass="com.upb.agripos.TestKoneksiDB"
 */
public class TestKoneksiDB {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║       TEST KONEKSI DATABASE - AGRI-POS                 ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Test 1: Koneksi Database
        System.out.println("🔄 [TEST 1] Mengecek koneksi database...");
        boolean isConnected = DatabaseConnection.getInstance().testConnection();
        
        if (isConnected) {
            System.out.println("✅ [TEST 1] BERHASIL: Database PostgreSQL terhubung!");
        } else {
            System.out.println("❌ [TEST 1] GAGAL: Tidak dapat terhubung ke database!");
            System.out.println("   Pastikan:");
            System.out.println("   - PostgreSQL service berjalan");
            System.out.println("   - Database 'agripos' sudah dibuat");
            System.out.println("   - Username/password di DatabaseConnection.java benar");
            return;
        }
        
        System.out.println();
        
        // Test 2: Query tabel users
        System.out.println("🔄 [TEST 2] Mengecek tabel users...");
        testQuery("SELECT username, role FROM users", "users");
        
        System.out.println();
        
        // Test 3: Query tabel products
        System.out.println("🔄 [TEST 3] Mengecek tabel products...");
        testQuery("SELECT code, name, price, stock FROM products LIMIT 5", "products");
        
        System.out.println();
        
        // Test 4: Query tabel transactions
        System.out.println("🔄 [TEST 4] Mengecek tabel transactions...");
        testQuery("SELECT code, cashier_username, total, payment_method FROM transactions LIMIT 5", "transactions");
        
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              SEMUA TEST SELESAI                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
    
    private static void testQuery(String sql, String tableName) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            
            // Print header
            StringBuilder header = new StringBuilder("   | ");
            for (int i = 1; i <= columnCount; i++) {
                header.append(String.format("%-20s | ", meta.getColumnName(i)));
            }
            System.out.println(header);
            System.out.println("   " + "-".repeat(header.length() - 3));
            
            // Print data
            int rowCount = 0;
            while (rs.next()) {
                StringBuilder row = new StringBuilder("   | ");
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null && value.length() > 18) {
                        value = value.substring(0, 15) + "...";
                    }
                    row.append(String.format("%-20s | ", value != null ? value : "NULL"));
                }
                System.out.println(row);
                rowCount++;
            }
            
            System.out.println();
            System.out.println("✅ [TEST] BERHASIL: Tabel '" + tableName + "' ada, berisi " + rowCount + " baris data");
            
        } catch (SQLException e) {
            if (e.getMessage().contains("does not exist")) {
                System.out.println("⚠️  [TEST] WARNING: Tabel '" + tableName + "' belum ada");
                System.out.println("   Jalankan: psql -U postgres -d agripos -f sql/schema.sql");
            } else {
                System.out.println("❌ [TEST] GAGAL: " + e.getMessage());
            }
        }
    }
}
