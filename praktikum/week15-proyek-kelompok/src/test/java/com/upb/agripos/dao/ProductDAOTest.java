package com.upb.agripos.dao;

import com.upb.agripos.dao.ipml.ProductDAOImpl;
import com.upb.agripos.dao.interfaces.ProductDAO;
import com.upb.agripos.model.Product;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk ProductDAO
 * Test coverage: CRUD operations
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDAOTest {

    private static ProductDAO productDAO;
    private static String testKode = "T-999";
    private static String testKode2 = "T-888";

    @BeforeAll
    static void setup() {
        productDAO = new ProductDAOImpl();
        // Pastikan koneksi bisa terjalin sebelum menjalankan tes
        try (Connection conn = DatabaseConnection.getConnection()) {
            assertNotNull(conn, "Koneksi database harus tersedia");
            System.out.println("✓ Database connection established");
        } catch (SQLException e) {
            fail("Gagal menyambung ke database testing: " + e.getMessage());
        }
    }

    @AfterAll
    static void cleanup() {
        // Clean up test data
        try {
            productDAO.delete(testKode);
            productDAO.delete(testKode2);
            System.out.println("✓ Test cleanup completed");
        } catch (Exception e) {
            System.out.println("Note: Cleanup may have failed if data doesn't exist");
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Simpan Produk ke Database")
    void testCreateProduct() {
        System.out.println("\n--- Test Create Product ---");
        Product p = new Product(testKode, "Pupuk Tes", "Kategori Tes", 10000.0, 50);
        productDAO.save(p);
        System.out.println("✓ Product saved: " + testKode);
        
        // Verifikasi data berhasil masuk
        List<Product> list = productDAO.findAll();
        boolean found = list.stream().anyMatch(item -> item.getKode().equals(testKode));
        assertTrue(found, "Produk harusnya tersimpan di database");
        System.out.println("✓ Product verified in database");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Cari Produk berdasarkan Kode")
    void testFindByKode() {
        System.out.println("\n--- Test Find by Kode ---");
        Product pResult = productDAO.findByKode(testKode);
        
        assertNotNull(pResult, "Produk harus ditemukan");
        assertEquals(testKode, pResult.getKode(), "Kode harus sesuai");
        assertEquals("Pupuk Tes", pResult.getNama(), "Nama harus sesuai");
        System.out.println("✓ Product found: " + pResult.getNama());
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Update Produk")
    void testUpdateProduct() {
        System.out.println("\n--- Test Update Product ---");
        Product pUpdate = productDAO.findByKode(testKode);
        assertNotNull(pUpdate, "Produk harus ada untuk diupdate");
        
        pUpdate.setNama("Pupuk Tes Updated");
        pUpdate.setHarga(12000.0);
        pUpdate.setStok(75);
        productDAO.update(pUpdate);
        System.out.println("✓ Product updated");
        
        // Verifikasi update
        Product pResult = productDAO.findByKode(testKode);
        assertNotNull(pResult);
        assertEquals(12000.0, pResult.getHarga(), "Harga harus terupdate");
        assertEquals(75, pResult.getStok(), "Stok harus terupdate ke 75");
        System.out.println("✓ Update verified");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Ambil Semua Produk")
    void testFindAll() {
        System.out.println("\n--- Test Find All Products ---");
        List<Product> list = productDAO.findAll();
        
        assertNotNull(list, "List tidak boleh null");
        assertFalse(list.isEmpty(), "List harus minimal ada 1 produk (test data)");
        System.out.println("✓ Total products found: " + list.size());
        
        boolean found = list.stream().anyMatch(p -> p.getKode().equals(testKode));
        assertTrue(found, "Produk test harus ada di list");
        System.out.println("✓ Test product found in list");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Tambah Produk Kedua")
    void testCreateSecondProduct() {
        System.out.println("\n--- Test Create Second Product ---");
        Product p2 = new Product(testKode2, "Pupuk Organik", "Pupuk", 8000.0, 100);
        productDAO.save(p2);
        System.out.println("✓ Second product saved: " + testKode2);
        
        Product pResult = productDAO.findByKode(testKode2);
        assertNotNull(pResult, "Produk kedua harus tersimpan");
        System.out.println("✓ Second product verified");
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Cari yang Tidak Ada (Negative Test)")
    void testFindNonExistent() {
        System.out.println("\n--- Test Find Non-Existent Product ---");
        Product pResult = productDAO.findByKode("NON-EXIST");
        assertNull(pResult, "Produk yang tidak ada harus null");
        System.out.println("✓ Correctly returned null for non-existent product");
    }

    @Test
    @Order(7)
    @DisplayName("Test 7: Hapus Produk")
    void testDeleteProduct() {
        System.out.println("\n--- Test Delete Product ---");
        productDAO.delete(testKode);
        System.out.println("✓ Product deleted: " + testKode);
        
        List<Product> list = productDAO.findAll();
        boolean found = list.stream().anyMatch(item -> item.getKode().equals(testKode));
        assertFalse(found, "Produk harusnya sudah terhapus dari database");
        System.out.println("✓ Delete verified");
    }

    @Test
    @Order(8)
    @DisplayName("Test 8: Verifikasi Cleanup")
    void testCleanup() {
        System.out.println("\n--- Test Cleanup Verification ---");
        productDAO.delete(testKode2);
        
        List<Product> list = productDAO.findAll();
        boolean found1 = list.stream().anyMatch(item -> item.getKode().equals(testKode));
        boolean found2 = list.stream().anyMatch(item -> item.getKode().equals(testKode2));
        
        assertFalse(found1, "Produk pertama harus terhapus");
        assertFalse(found2, "Produk kedua harus terhapus");
        System.out.println("✓ Cleanup verified - all test data removed");
    }
}