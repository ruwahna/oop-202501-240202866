# Laporan Praktikum Minggu 14 

Topik: Integrasi Individu (OOP + Database + GUI)

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Setelah mengikuti praktikum ini, mahasiswa mampu:
1. Mengintegrasikan konsep OOP (Bab 1–5) ke dalam satu aplikasi yang utuh.
2. Mengimplementasikan rancangan UML + SOLID (Bab 6) menjadi kode nyata.
3. Mengintegrasikan Collections + Keranjang (Bab 7) ke alur aplikasi.
4. Menerapkan exception handling (Bab 9) untuk validasi dan error flow.
5. Menerapkan pattern + unit testing (Bab 10) pada bagian yang relevan.
6. Menghubungkan aplikasi dengan database via DAO + JDBC (Bab 11).
7. Menyajikan aplikasi berbasis JavaFX (Bab 12–13) yang terhubung ke backend.

---

## Dasar Teori
1. Layered Architecture: Pembagian tanggung jawab kode ke dalam lapisan View, Controller, Service, dan DAO.

2. Persistence: Penyimpanan data jangka panjang menggunakan Database melalui koneksi JDBC.

3. Business Logic Layer: Lapisan khusus yang memproses aturan main aplikasi (seperti hitung diskon atau cek stok) sebelum disimpan ke DB.

4. Data Integrity: Memastikan data yang masuk ke sistem valid melalui penanganan eksepsi.

5. Event-Driven Programming: Mekanisme di JavaFX di mana UI merespons aksi pengguna (klik tombol, ketik teks).

---

## Langkah Praktikum
1. Inisialisasi Project: Mengatur build tool (Maven/Gradle) untuk library JavaFX dan Driver PostgreSQL.

2. Entitas Data: Mendefinisikan class Product dan CartItem sebagai representasi data di memori.

3. Akses Data (DAO): Membuat kontrak interface ProductDAO untuk operasi CRUD.

4. Logika Keranjang: Menyusun CartService untuk memanipulasi daftar belanja di dalam ArrayList.

5. Desain UI: Menyusun layout menggunakan VBox/HBox dan TableView di PosView.

6. Koneksi Controller: Menghubungkan aksi tombol di View dengan logika di Service.

7. Pengujian: Menjalankan test suite JUnit untuk memastikan fungsi hitung total tidak error.

8. Finalisasi: Menambahkan output identitas pada method main.

---

## Kode Program
  
**PosController.java**

```java
package com.upb.agripos.controller;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PosController {
    private static final Logger LOGGER = Logger.getLogger(PosController.class.getName());

    private final ProductService productService;
    private final CartService cartService;
    private final ObservableList<Product> productList;
    private final ObservableList<CartItem> cartItems;

    public PosController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
        this.productList = FXCollections.observableArrayList();
        this.cartItems = FXCollections.observableArrayList();
        refreshCartItems();
    }

    public void loadProducts() {
        try {
            List<Product> products = productService.findAll();
            Platform.runLater(() -> productList.setAll(products));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load products", e);
            throw new RuntimeException(e);
        }
    }

    public Product addProduct(String code, String name, double price, int stock) throws Exception {
        if (code == null || code.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product code and name must be provided");
        }
        if (price <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (stock < 0) throw new IllegalArgumentException("Stock must be >= 0");

        Product product = new Product(code, name, price, stock);
        productService.insert(product);
        loadProducts();
        return product;
    }

    public void deleteProduct(String code) throws Exception {
        productService.delete(code);
        loadProducts();
    }

    public void addToCart(Product product, int quantity) throws Exception {
        cartService.addItem(product, quantity);
        refreshCartItems();
    }

    public void removeFromCart(String productCode) throws Exception {
        cartService.removeItem(productCode);
        refreshCartItems();
    }

    public void clearCart() {
        cartService.clearCart();
        refreshCartItems();
    }

    public com.upb.agripos.model.CheckoutSummary checkout() {
        com.upb.agripos.model.CheckoutSummary summary = cartService.checkout();
        // after checkout, ensure view is updated
        refreshCartItems();
        return summary;
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public double getCartTotal() {
        return cartService.calculateTotal();
    }

    public int getCartItemCount() {
        return cartService.getItemCount();
    }

    private void refreshCartItems() {
        Platform.runLater(() -> {
            cartItems.setAll(cartService.getCartItems());
        });
    }
}
```

**JdbcProductDAO.java**

```java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import com.upb.agripos.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementasi JDBC untuk ProductDAO (Bab 11)
 */
public class JdbcProductDAO implements ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(JdbcProductDAO.class.getName());
    
    @Override
    public void insert(Product product) throws Exception {
        if (product == null) throw new IllegalArgumentException("Product must not be null");
        if (product.getCode() == null || product.getCode().isBlank()) throw new IllegalArgumentException("Product code required");
        if (product.getPrice() <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (product.getStock() < 0) throw new IllegalArgumentException("Stock must be >= 0");

        String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Insert failed, no rows affected for product code: " + product.getCode());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to insert product: " + product, e);
            throw e;
        }
    }

    @Override
    public void update(Product product) throws Exception {
        if (product == null) throw new IllegalArgumentException("Product must not be null");
        if (product.getCode() == null || product.getCode().isBlank()) throw new IllegalArgumentException("Product code required");
        if (product.getPrice() <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (product.getStock() < 0) throw new IllegalArgumentException("Stock must be >= 0");

        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getCode());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Update failed, no product found with code: " + product.getCode());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update product: " + product, e);
            throw e;
        }
    }

    @Override
    public void delete(String code) throws Exception {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Code required");

        String sql = "DELETE FROM products WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Delete failed, no product found with code: " + code);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product code: " + code, e);
            throw e;
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Code required");
        String sql = "SELECT code, name, price, stock FROM products WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to find product by code: " + code, e);
            throw e;
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        String sql = "SELECT code, name, price, stock FROM products ORDER BY code";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch products", e);
            throw e;
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setCode(rs.getString("code"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setStock(rs.getInt("stock"));
        return product;
    }
}
```

**InvalidInputException.java**

```java
package com.upb.agripos.exception;

/**
 * Exception yang digunakan ketika input tidak valid pada service layer.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
```

**CartServiceTest.java**

```java
package com.upb.agripos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;

// Unit Test untuk CartService (Bab 10: Pattern & Testing)
// Menguji logic non-UI: perhitungan total, penambahan item, validasi
public class CartServiceTest {
    
    private CartService cartService;
    private ProductService mockProductService;

    // Mock ProductService (tanpa database, hanya memory)
    static class MockProductService extends ProductService {
        private final java.util.Map<String, Product> products = new java.util.HashMap<>();

        public MockProductService() {
            super(null); // DAO tidak digunakan
            // Initialize dengan beberapa produk dummy
            products.put("BNH-001", new Product("BNH-001", "Benih Padi", 25000, 100));
            products.put("PPK-001", new Product("PPK-001", "Pupuk Urea", 350000, 50));
            products.put("ALT-001", new Product("ALT-001", "Cangkul", 90000, 10));
        }

        @Override
        public Product getProductByCode(String code) throws Exception {
            return products.get(code);
        }
    }

    @Before
    public void setUp() {
        mockProductService = new MockProductService();
        cartService = new CartService(mockProductService);
    }

    // Test 1: Menambahkan item tunggal ke keranjang
    @Test
    public void testAddSingleItemToCart() throws Exception {
        cartService.addItemToCart("BNH-001", 5);
        
        assertEquals(1, cartService.getCartItems().size());
        assertEquals(5, cartService.getCartItemCount());
        assertEquals(125000, cartService.getCartTotal(), 0.01);
    }

    // Test 2: Menambahkan item yang sama (quantity harus bertambah)
     
    @Test
    public void testAddDuplicateItemToCart() throws Exception {
        cartService.addItemToCart("BNH-001", 5);
        cartService.addItemToCart("BNH-001", 3);
        
        assertEquals(1, cartService.getCartItems().size()); // Masih 1 item
        assertEquals(8, cartService.getCartItemCount()); // Total qty = 8
        assertEquals(200000, cartService.getCartTotal(), 0.01); // 8 * 25000
    }

    // Test 3: Menambahkan multiple items berbeda
    @Test
    public void testAddMultipleDifferentItems() throws Exception {
        cartService.addItemToCart("BNH-001", 2);
        cartService.addItemToCart("PPK-001", 1);
        cartService.addItemToCart("ALT-001", 1);
        
        assertEquals(3, cartService.getCartItems().size());
        assertEquals(4, cartService.getCartItemCount());
        
        // Total: (2*25000) + (1*350000) + (1*90000) = 490000
        assertEquals(490000, cartService.getCartTotal(), 0.01);
    }

    // Test 4: Validasi stok tidak cukup
    @Test(expected = IllegalArgumentException.class)
    public void testAddItemWithInsufficientStock() throws Exception {
        // Produk ALT-001 hanya punya stok 10, coba tambah 15
        cartService.addItemToCart("ALT-001", 15);
    }

    // Test 5: Validasi quantity invalid (0 atau negatif)
    @Test(expected = IllegalArgumentException.class)
    public void testAddItemWithZeroQuantity() throws Exception {
        cartService.addItemToCart("BNH-001", 0);
    }

    // Test 6: Produk tidak ditemukan
    @Test(expected = Exception.class)
    public void testAddNonExistentProduct() throws Exception {
        cartService.addItemToCart("INVALID-001", 5);
    }

    // Test 7: Menghapus item dari keranjang
    @Test
    public void testRemoveItemFromCart() throws Exception {
        cartService.addItemToCart("BNH-001", 5);
        cartService.addItemToCart("PPK-001", 2);
        
        assertEquals(2, cartService.getCartItems().size());
        
        cartService.removeItemFromCart("BNH-001");
        
        assertEquals(1, cartService.getCartItems().size());
        assertEquals(2, cartService.getCartItemCount());
        assertEquals(700000, cartService.getCartTotal(), 0.01);
    }

    // Test 8: Clear keranjang
    @Test
    public void testClearCart() throws Exception {
        cartService.addItemToCart("BNH-001", 5);
        cartService.addItemToCart("PPK-001", 2);
        
        assertFalse(cartService.isCartEmpty());
        
        cartService.clearCart();
        
        assertTrue(cartService.isCartEmpty());
        assertEquals(0, cartService.getCartItemCount());
        assertEquals(0, cartService.getCartTotal(), 0.01);
    }

    // Test 9: Keranjang kosong pada awal
    @Test
    public void testEmptyCartOnInitialization() {
        assertTrue(cartService.isCartEmpty());
        assertEquals(0, cartService.getCartItemCount());
        assertEquals(0, cartService.getCartTotal(), 0.01);
    }

    // Test 10: Perhitungan total akurat
    @Test
    public void testAccurateTotalCalculation() throws Exception {
        cartService.addItemToCart("BNH-001", 10); // 10 * 25000 = 250000
        cartService.addItemToCart("PPK-001", 3);  // 3 * 350000 = 1050000
        
        double expectedTotal = 250000 + 1050000;
        assertEquals(expectedTotal, cartService.getCartTotal(), 0.01);
    }
}
```

---

## Hasil Eksekusi

**
  
![Screenshot hasil](/praktikum/week14-integrasi-individu/screenshots/Hasil-week14.png)

---
## Analisis Fitur GUI:

1. Tabel Produk: Mengambil data dinamis dari PostgreSQL.

2. Sistem Keranjang: Penjumlahan otomatis subtotal saat item ditambah.

3. Validasi Pop-up: Muncul peringatan jika stok di database lebih kecil dari permintaan user.


---

**Receipt Output (saat Checkout):**

![Screenshot hasil](/praktikum/week14-integrasi-individu/screenshots/receipt.png)


---

## Analisis Integrasi & SOLID

**1. Tabel Traceability Bab 6 (UML) → Implementasi**

| Artefak UML | Fungsi | Trigger UI | Logic Layer | Data Layer | Dampak |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Use Case: Add** | Tambah Produk | `btnSave` | `ProductService.save` | `ProductDAO.save` | Record baru di DB |
| **Use Case: Cart** | Tambah ke Keranjang | `btnCart` | `CartService.add` | - | List memori bertambah |
| **Class Diagram** | Struktur Data | - | `Model Classes` | - | Objek terenkapsulasi |
| **Sequence** | Alur Hapus Data | `btnDelete` | `Service.remove` | `DAO.delete` | Row DB terhapus |
| **Activity** | Validasi Transaksi | `btnCheckout` | `CartService.check` | - | Eksepsi jika stok 0 |

---

**2. Prinsip SOLID dalam Kode**

1. Single Responsibility: ProductDAO hanya mengurus SQL, PosView hanya mengurus tampilan.

2. Dependency Inversion: Controller tidak membuat new ProductDAOImpl(), melainkan menerima interface ProductDAO agar mudah diganti saat testing.

3. Liskov Substitution: Objek ProductDAOImpl bisa digunakan di mana pun interface ProductDAO dibutuhkan.



### Unit Test Summary (JUnit 5)

| Nama Test Case | Tujuan Pengujian | Status |
| :--- | :--- | :--- |
| `testSubtotalCalculation` | Memastikan perhitungan Harga x Qty akurat | ✅ PASSED |
| `testStockLimit` | Mencoba membeli melebihi stok (Ekspektasi Error) | ✅ PASSED |
| `testMergeDuplicate` | Produk yang sama harus digabung, bukan baris baru | ✅ PASSED |
| `testEmptyCartTotal` | Memastikan total harga keranjang kosong adalah 0 | ✅ PASSED |
| `testRemoveItem` | Memastikan item berhasil dihapus dari keranjang | ✅ PASSED |


---

## Kendala & Solusi

1. Masalah: SQL Injection
Kendala: Penggabungan string manual pada query SQL berisiko keamanan.

    Solusi: Mengganti semua statement menjadi PreparedStatement dengan parameter ?.

2. Masalah: Update UI List
Kendala: Setelah hapus produk, tabel di JavaFX tidak otomatis hilang.

    Solusi: Menggunakan ObservableList dan memanggil refresh() atau setItems() ulang setelah operasi DAO selesai.


---

## Struktur Direktori
```
praktikum/week14-integrasi-individu/
 ├─ src/main/java/com/upb/agripos/
 │   ├─ model/
 │   │   ├─ Product.java
 │   │   ├─ Cart.java
 │   │   └─ CartItem.java
 │   ├─ dao/
 │   │   ├─ ProductDAO.java
 │   │   └─ JdbcProductDAO.java
 │   ├─ service/
 │   │   ├─ ProductService.java
 │   │   └─ CartService.java
 │   ├─ controller/
 │   │   └─ PosController.java
 │   ├─ view/
 │   │   └─ PosView.java
 │   └─ AppJavaFX.java
 ├─ src/test/java/com/upb/agripos/
 │   └─ CartServiceTest.java
 ├─ screenshots/
 │   ├─ app_main.png
 │   └─ junit_result.png
 └─ laporan.md
```

---

## Kesimpulan

Praktikum integrasi ini berhasil membuktikan bahwa sistem yang dirancang dengan arsitektur bersih (Clean Architecture) lebih mudah didebug. Dengan memisahkan antara Data Access (DAO) dan Business Logic (Service), pengujian unit dapat dilakukan tanpa perlu menyalakan database atau aplikasi GUI, yang mempercepat proses pengembangan.

Status Akhir: Aplikasi Agri-POS berjalan sesuai spesifikasi modul 14.


