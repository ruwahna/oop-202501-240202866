package com.upb.agripos.service;

import com.upb.agripos.model.Product;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.dao.ipml.ProductDAOImpl;
import com.upb.agripos.dao.interfaces.ProductDAO;
import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.exception.ValidationException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk TransactionService
 * Test coverage: Checkout, transaction retrieval, transaction management
 */
@DisplayName("TransactionService Tests")
public class TransactionServiceTest {

    private TransactionService transactionService;
    private ProductService productService;
    private ProductDAO productDAO;
    private final String KODE_PRODUK_TEST_1 = "P-TEST-01";
    private final String KODE_PRODUK_TEST_2 = "P-TEST-02";

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
        productService = new ProductService();
        productDAO = new ProductDAOImpl();

        // Clean up data test sebelumnya
        try {
            productDAO.delete(KODE_PRODUK_TEST_1);
            productDAO.delete(KODE_PRODUK_TEST_2);
        } catch (Exception e) {
            System.out.println("Cleanup note: Previous test data might not exist");
        }
        
        // Siapkan data produk dummy di database untuk ditransaksikan
        Product p1 = new Product(KODE_PRODUK_TEST_1, "Pupuk Tes Transaksi", "Pupuk", 10000.0, 10);
        Product p2 = new Product(KODE_PRODUK_TEST_2, "Benih Tes Transaksi", "Benih", 5000.0, 20);
        productDAO.save(p1);
        productDAO.save(p2);
        
        System.out.println("✓ Test setup completed");
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Checkout Berhasil dan Stok Berkurang")
    void testCheckoutSuccess() throws SQLException, OutOfStockException, ValidationException {
        System.out.println("\n--- Test Checkout Success ---");
        
        // Verifikasi stok awal
        Product pBefore = productService.findByCode(KODE_PRODUK_TEST_1);
        assertNotNull(pBefore, "Product P-TEST-01 harus ada");
        int stokAwal = pBefore.getStok();
        assertEquals(10, stokAwal, "Stok awal harus 10");
        System.out.println("✓ Initial stock verified: " + stokAwal);
        
        // Buat transaksi
        Transaction trans = new Transaction();
        trans.setTotal(20000.0);
        trans.setStatus("SUCCESS");
        
        List<TransactionItem> items = new ArrayList<>();
        items.add(new TransactionItem(pBefore, 2, 20000.0)); // Beli 2 qty
        trans.setItems(items);

        // Checkout
        transactionService.checkout(trans);
        System.out.println("✓ Checkout executed");

        // Verifikasi stok berkurang (10 - 2 = 8)
        Product pAfter = productService.findByCode(KODE_PRODUK_TEST_1);
        assertEquals(8, pAfter.getStok(), "Stok harusnya berkurang menjadi 8");
        System.out.println("✓ Stock reduction verified: " + pAfter.getStok());
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Checkout Multiple Items")
    void testCheckoutMultipleItems() throws SQLException, OutOfStockException, ValidationException {
        System.out.println("\n--- Test Checkout Multiple Items ---");
        
        Product p1 = productService.findByCode(KODE_PRODUK_TEST_1);
        Product p2 = productService.findByCode(KODE_PRODUK_TEST_2);
        assertNotNull(p1, "Product 1 harus ada");
        assertNotNull(p2, "Product 2 harus ada");
        
        int stok1Awal = p1.getStok();
        int stok2Awal = p2.getStok();
        
        // Buat transaksi dengan 2 item
        Transaction trans = new Transaction();
        trans.setTotal(30000.0); // (10000 x 2) + (5000 x 2)
        trans.setStatus("SUCCESS");
        
        List<TransactionItem> items = new ArrayList<>();
        items.add(new TransactionItem(p1, 2, 20000.0)); // 2 x P-TEST-01
        items.add(new TransactionItem(p2, 2, 10000.0)); // 2 x P-TEST-02
        trans.setItems(items);

        // Checkout
        transactionService.checkout(trans);
        System.out.println("✓ Checkout with 2 items executed");

        // Verifikasi stok berkurang untuk kedua produk
        Product p1After = productService.findByCode(KODE_PRODUK_TEST_1);
        Product p2After = productService.findByCode(KODE_PRODUK_TEST_2);
        
        assertEquals(stok1Awal - 2, p1After.getStok(), "Stok P1 harus berkurang 2");
        assertEquals(stok2Awal - 2, p2After.getStok(), "Stok P2 harus berkurang 2");
        System.out.println("✓ Stock reduction verified for both products");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Checkout dengan Stok Tidak Cukup - Gagal")
    void testCheckoutInsufficientStock() throws ValidationException {
        System.out.println("\n--- Test Checkout Insufficient Stock ---");
        
        Product p = productService.findByCode(KODE_PRODUK_TEST_1); // Stok 10
        assertNotNull(p, "Product harus ada");
        
        Transaction trans = new Transaction();
        trans.setTotal(150000.0);
        trans.setStatus("PENDING");
        
        List<TransactionItem> items = new ArrayList<>();
        items.add(new TransactionItem(p, 20, 150000.0)); // Minta 20 tapi stok hanya 10
        trans.setItems(items);

        // Harus throw OutOfStockException
        assertThrows(OutOfStockException.class, () -> {
            transactionService.checkout(trans);
        }, "Checkout harus gagal karena stok tidak cukup");
        System.out.println("✓ Insufficient stock exception thrown correctly");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Checkout dengan Transaksi Kosong - Gagal")
    void testCheckoutEmptyTransaction() {
        System.out.println("\n--- Test Checkout Empty Transaction ---");
        
        Transaction trans = new Transaction();
        trans.setTotal(0);
        trans.setItems(new ArrayList<>()); // Empty items

        assertThrows(ValidationException.class, () -> {
            transactionService.checkout(trans);
        }, "Checkout harus gagal karena transaksi kosong");
        System.out.println("✓ Empty transaction validation passed");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Get All Transactions")
    void testGetAllTransactions() {
        System.out.println("\n--- Test Get All Transactions ---");
        
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertNotNull(transactions, "Transaction list tidak boleh null");
        assertTrue(transactions.size() >= 0, "Transaction list valid");
        System.out.println("✓ Get all transactions returned: " + transactions.size());
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Get Transaction by ID")
    void testGetTransactionById() throws SQLException, OutOfStockException, ValidationException {
        System.out.println("\n--- Test Get Transaction by ID ---");
        
        // Buat transaksi dulu
        Product p = productService.findByCode(KODE_PRODUK_TEST_1);
        assertNotNull(p, "Product P-TEST-01 harus ada");
        
        Transaction trans = new Transaction();
        trans.setTotal(10000.0);
        trans.setStatus("SUCCESS");
        
        List<TransactionItem> items = new ArrayList<>();
        items.add(new TransactionItem(p, 1, 10000.0));
        trans.setItems(items);
        
        transactionService.checkout(trans);
        System.out.println("✓ Test transaction created");
        
        // Get by ID
        int transId = trans.getId();
        assertTrue(transId > 0, "Transaction ID harus ter-assign");
        
        Transaction retrieved = transactionService.getTransactionById(transId);
        assertNotNull(retrieved, "Transaction harus ditemukan");
        assertEquals(transId, retrieved.getId(), "ID harus sama");
        System.out.println("✓ Transaction retrieved successfully: ID " + transId);
    }

    @Test
    @Order(7)
    @DisplayName("Test 7: Get Transactions by Status")
    void testGetTransactionsByStatus() {
        System.out.println("\n--- Test Get Transactions by Status ---");
        
        List<Transaction> successTransactions = transactionService.getTransactionsByStatus("SUCCESS");
        assertNotNull(successTransactions, "Status list tidak boleh null");
        System.out.println("✓ Transactions with SUCCESS status: " + successTransactions.size());
    }

    @Test
    @Order(8)
    @DisplayName("Test 8: Update Transaction Status")
    void testUpdateTransaction() throws SQLException, OutOfStockException, ValidationException {
        System.out.println("\n--- Test Update Transaction ---");
        
        // Buat transaksi
        Product p = productService.findByCode(KODE_PRODUK_TEST_1);
        assertNotNull(p, "Product harus ada");
        
        Transaction trans = new Transaction();
        trans.setTotal(10000.0);
        trans.setStatus("SUCCESS");
        
        List<TransactionItem> items = new ArrayList<>();
        items.add(new TransactionItem(p, 1, 10000.0));
        trans.setItems(items);
        
        transactionService.checkout(trans);
        int transId = trans.getId();
        assertTrue(transId > 0, "Transaction ID harus valid");
        System.out.println("✓ Transaction created with ID: " + transId);
        
        // Update status
        trans.setStatus("COMPLETED");
        transactionService.updateTransaction(trans);
        System.out.println("✓ Transaction status updated");
        
        // Verify
        Transaction updated = transactionService.getTransactionById(transId);
        assertNotNull(updated, "Transaction harus tetap ada");
        assertEquals("COMPLETED", updated.getStatus(), "Status harus terupdate");
        System.out.println("✓ Status update verified");
    }

    @Test
    @Order(9)
    @DisplayName("Test 9: Get Transaction Items")
    void testGetTransactionItems() throws SQLException, OutOfStockException, ValidationException {
        System.out.println("\n--- Test Get Transaction Items ---");
        
        // Buat transaksi dengan 2 items
        Product p1 = productService.findByCode(KODE_PRODUK_TEST_1);
        Product p2 = productService.findByCode(KODE_PRODUK_TEST_2);
        assertNotNull(p1, "Product 1 harus ada");
        assertNotNull(p2, "Product 2 harus ada");
        
        Transaction trans = new Transaction();
        trans.setTotal(30000.0);
        trans.setStatus("SUCCESS");
        
        List<TransactionItem> items = new ArrayList<>();
        items.add(new TransactionItem(p1, 2, 20000.0));
        items.add(new TransactionItem(p2, 2, 10000.0));
        trans.setItems(items);
        
        transactionService.checkout(trans);
        int transId = trans.getId();
        assertTrue(transId > 0, "Transaction ID harus valid");
        System.out.println("✓ Transaction with 2 items created");
        
        // Get items
        List<TransactionItem> retrievedItems = transactionService.getTransactionItems(transId);
        assertNotNull(retrievedItems, "Items list tidak boleh null");
        assertEquals(2, retrievedItems.size(), "Harus ada 2 items");
        System.out.println("✓ Transaction items retrieved: " + retrievedItems.size());
    }

    @AfterEach
    void tearDown() {
        System.out.println("\n--- Test Cleanup ---");
        // Bersihkan data sampah hasil testing
        try {
            productDAO.delete(KODE_PRODUK_TEST_1);
            productDAO.delete(KODE_PRODUK_TEST_2);
            System.out.println("✓ Test data cleanup completed");
        } catch (Exception e) {
            System.out.println("Note: Cleanup completed with exceptions: " + e.getMessage());
        }
    }
}