# Laporan Praktikum Week 15: Proyek Kelompok
## Agri-POS - Sistem Point of Sale Pertanian

### Informasi Praktikan
- **Nama+Nim**: 
                
                [1. Indah Ruwahna Anugraheni (240202866)] 

                [2. Lia Lusianti (240202869)]



- **Kelas**: [3IKRB]
- **Tanggal**: [18 Januari 2026]


---

## 1. Pendahuluan

### 1.1 Latar Belakang
Praktikum Week 15 merupakan proyek kelompok yang mengintegrasikan semua konsep OOP yang telah dipelajari selama semester ini. Proyek ini mengembangkan aplikasi Agri-POS, sebuah sistem Point of Sale untuk toko pertanian.

### 1.2 Tujuan
1. Mengintegrasikan konsep OOP: encapsulation, inheritance, polymorphism, abstraction
2. Menerapkan design patterns: Singleton, Strategy, Factory, DAO
3. Menerapkan prinsip SOLID dan Dependency Inversion Principle
4. Mengembangkan aplikasi desktop berbasis JavaFX
5. Melakukan testing dengan JUnit 5 dan Mockito
6. Mengimplementasikan sistem diskon dan promosi

### 1.3 Ruang Lingkup
Aplikasi Agri-POS mencakup:
- Manajemen produk pertanian (CRUD)
- Transaksi penjualan dengan keranjang belanja
- Sistem diskon dan promosi untuk produk
- Multi metode pembayaran (Cash, E-Wallet, QRIS)
- Pencetakan struk pembelian dengan detail diskon
- Laporan penjualan (harian dan periodik)
- Riwayat transaksi dengan tampilan detail
- Autentikasi dan otorisasi berbasis role (Admin & Kasir)

---

## 2. Analisis Kebutuhan

### 2.1 Functional Requirements

| ID | Requirement | Deskripsi | Priority |
|----|-------------|-----------|----------|
| FR-1 | Manajemen Produk | Admin dapat CRUD produk pertanian | High |
| FR-2 | Transaksi Penjualan | Kasir dapat melakukan transaksi penjualan | High |
| FR-3 | Sistem Diskon | Kasir dapat menerapkan diskon pada transaksi | High |
| FR-4 | Multi Payment | Mendukung Cash, E-Wallet, QRIS | High |
| FR-5 | Struk & Laporan | Generate struk dan laporan penjualan | High |
| FR-6 | Login & Akses Kontrol | Autentikasi berbasis role (Admin/Kasir) | High |
| FR-7 | Manajemen Diskon | Admin dapat CRUD diskon, sync ke Kasir | High |
| FR-8 | Responsive UI | UI menyesuaikan ukuran layar (mobile/desktop) | Medium |


### 2.3 Non-Functional Requirements

| ID | Requirement | Deskripsi |
|----|-------------|-----------|
| NFR-1 | Performance | Response time < 2 detik |
| NFR-2 | Usability | Interface intuitif dan mudah dipelajari |
| NFR-3 | Security | SQL Injection prevention, password validation |
| NFR-4 | Maintainability | Clean code, dokumentasi lengkap |
| NFR-5 | Scalability | Arsitektur berlapis untuk kemudahan extend |



### 2.3 Use Case Diagram


![UseCase](/praktikum/week15-proyek-kelompok/screenshots/USEEECASEEE-agripost.drawio.png)



### 2.4 Actor Description

| Actor | Description | Access Level |
|-------|-------------|--------------|
| **Kasir** | Operator transaksi penjualan | Login, View Produk (read-only), Transaksi, Keranjang, Checkout, Apply Diskon/Voucher, Cetak Struk, Riwayat Transaksi |
| **Admin** | Administrator sistem | Full Access: Dashboard, CRUD Produk, CRUD Diskon, Laporan Penjualan, Export Report, Low Stock Alert |

### 2.5 Use Case Detail per Actor

#### 🏪 Kasir - Use Case List
| No | Use Case | Deskripsi | Tab Menu |
|----|----------|-----------|----------|
| 1 | Login | Autentikasi masuk sistem dengan pilih role | LoginView |
| 2 | Logout | Keluar dari sistem | Header |
| 3 | New Transaction | Membuat transaksi penjualan baru | 🛒 Transaksi Baru |
| 4 | Search Product | Mencari produk berdasarkan nama/kode | 🛒 Transaksi Baru |
| 5 | Filter Category | Filter produk berdasarkan kategori | 🛒 Transaksi Baru |
| 6 | Add to Cart | Menambahkan produk ke keranjang | 🛒 Transaksi Baru |
| 7 | Update Cart Qty | Mengubah jumlah item di keranjang | 🛒 Transaksi Baru |
| 8 | Remove from Cart | Menghapus item dari keranjang | 🛒 Transaksi Baru |
| 9 | Clear Cart | Mengosongkan seluruh keranjang | 🛒 Transaksi Baru |
| 10 | Checkout (Cash) | Proses pembayaran tunai | 🛒 Transaksi Baru |
| 11 | Checkout (E-Wallet) | Proses pembayaran e-wallet | 🛒 Transaksi Baru |
| 12 | Checkout (QRIS) | Proses pembayaran QRIS | 🛒 Transaksi Baru |
| 13 | Apply Discount | Menerapkan diskon dari dropdown | 🛒 Transaksi Baru |
| 14 | Apply Voucher | Memasukkan kode voucher manual | 🛒 Transaksi Baru |
| 15 | Refresh Discount | Memperbarui daftar diskon dari Admin | 🛒 Transaksi Baru |
| 16 | Print Receipt | Mencetak struk pembelian | 🛒 Transaksi Baru |
| 17 | View Transaction History | Melihat riwayat transaksi | 📋 Riwayat Transaksi |
| 18 | Reprint Receipt | Cetak ulang struk transaksi lama | 📋 Riwayat Transaksi |

#### 👔 Admin - Use Case List

| No | Use Case | Deskripsi | Tab Menu |
|----|----------|-----------|----------|
| 1 | Login | Autentikasi masuk sistem dengan pilih role | LoginView |
| 2 | Logout | Keluar dari sistem | Header |
| 3 | View Dashboard | Melihat statistik penjualan dan grafik | 📊 Dashboard |
| 4 | View Low Stock Alert | Melihat produk dengan stok rendah | 📊 Dashboard |
| 5 | Add Product | Menambah produk baru | 📦 Manajemen Produk |
| 6 | Edit Product | Mengubah data produk | 📦 Manajemen Produk |
| 7 | Delete Product | Menghapus produk dari sistem | 📦 Manajemen Produk |
| 8 | Search Product | Mencari produk | 📦 Manajemen Produk |
| 9 | Daily Sales Report | Generate laporan harian | 📈 Laporan Penjualan |
| 10 | Period Sales Report | Generate laporan periode | 📈 Laporan Penjualan |
| 11 | Export Report | Export laporan ke file | 📈 Laporan Penjualan |
| 12 | Add Discount | Menambah diskon baru | 🎁 Manajemen Diskon |
| 13 | Edit Discount | Mengubah konfigurasi diskon | 🎁 Manajemen Diskon |
| 14 | Delete Discount | Menghapus diskon | 🎁 Manajemen Diskon |
| 15 | Toggle Discount Status | Aktifkan/nonaktifkan diskon | 🎁 Manajemen Diskon |
| 16 | Search Discount | Mencari diskon | 🎁 Manajemen Diskon |


---

## 3. Desain Sistem

### 3.1 Arsitektur Sistem (Layered Architecture + DIP)

![Arsitektur Sistem (Layered Architecture+DIP)](/praktikum/week15-proyek-kelompok/screenshots/Arsitektur%20Sistem%20(Layered%20Architecture)-Arsitektur%20Sistem%20(Layered%20Architecture).drawio.png)

### 3.2 Class Diagram

**model classes**

![Model classes](/praktikum/week15-proyek-kelompok/screenshots/Model%20classes.png)

**Strategy Pattern(payment)**

![strategi pattern-paymentment-menthod](/praktikum/week15-proyek-kelompok/screenshots/strategi%20pattern-paymentment-menthod.drawio.png)

**DAO Pattern**

![DAO interface](/praktikum/week15-proyek-kelompok/screenshots/DAO%20interface.drawio%20(1).png)



### 3.3 Sequence Diagrams

#### 3.3.1 Login Sequence

![Login Sequence](/praktikum/week15-proyek-kelompok/screenshots/Login%20Sequence.png)


#### 3.3.2 Checkout Transaction

![Checkout Transaction](/praktikum/week15-proyek-kelompok/screenshots/Checkout%20Transaction.drawio.png)


#### 3.3.3 Admin Discount Management 

![ Admin Discount Management ](/praktikum/week15-proyek-kelompok/screenshots/Admin%20Discount%20Management%20.drawio.png)


#### 3.3.4 Kasir Apply Discount 

![Kasir Apply Discount ](/praktikum/week15-proyek-kelompok/screenshots/Kasir%20Apply%20Discount%20.drawio.png)


 
### 3.4 Design Patterns summary

| Pattern | Class/Interface | Tujuan |
|---------|-----------------|--------|
| **Singleton** | `DatabaseConnection` | Single database connection instance |
| **Singleton** | `DiscountConfigService` | Shared discount config Admin-Kasir |
| **Strategy** | `PaymentMethod`, `CashPayment`, `EWalletPayment`, `QRISPayment` | Metode pembayaran yang dapat di-extend |
| **Strategy** | `DiscountStrategy`, `PercentageDiscount`, `FixedDiscount`, `BulkDiscount` | Tipe diskon yang fleksibel |
| **Factory** | `PaymentMethodFactory` | Membuat instance payment method |
| **DAO** | `ProductDAO`, `UserDAO`, `TransactionDAO` | Abstraksi akses database |
| **Observer** | JavaFX `ObservableList` | Real-time sync discount changes |
| **MVC** | View, Controller, Model | Separation of concerns |


### 3.5 Database Schema (ERD)


![Database schema(erd)](/praktikum/week15-proyek-kelompok/screenshots/database%20schema%20(ERD).png)



RELASI:
═══════
• users (1) ──────< (*) transactions     : Satu user bisa punya banyak transaksi
• transactions (1) ──────< (*) transaction_items : Satu transaksi punya banyak item
• products (1) ──────< (*) transaction_items     : Satu produk bisa ada di banyak item




## 4. Implementasi

### 4.1 Package Structure

```
src/main/java/com/upb/agripos/
├── AppJavaFx.java                 # Main entry point
├── controller/
│   ├── LoginController.java       # Handle login
│   └── PosController.java         # Handle business logic
├── dao/
│   ├── ProductDAO.java            # Interface
│   ├── UserDAO.java               # Interface
│   ├── TransactionDAO.java        # Interface
│   └── impl/
│       ├── JdbcProductDAO.java
│       ├── JdbcUserDAO.java
│       └── JdbcTransactionDAO.java
├── model/
│   ├── Product.java
│   ├── User.java
│   ├── Transaction.java
│   ├── CartItem.java
│   └── CheckoutSummary.java
├── service/
│   ├── ProductService.java
│   ├── CartService.java
│   ├── TransactionService.java
│   ├── AuthService.java
│   ├── ReportService.java
│   ├── ReceiptService.java
│   └── DiscountConfigService.java    # NEW: Singleton
├── strategy/
│   ├── payment/
│   │   ├── PaymentMethod.java        # Interface
│   │   ├── CashPayment.java
│   │   ├── EWalletPayment.java
│   │   ├── QRISPayment.java
│   │   └── PaymentMethodFactory.java
│   └── discount/
│       ├── DiscountStrategy.java     # Interface
│       ├── PercentageDiscount.java
│       ├── FixedDiscount.java
│       ├── BulkDiscount.java
│       └── VoucherDiscount.java
├── util/
│   └── DatabaseConnection.java       # Singleton
└── view/
    ├── LoginView.java
    ├── MainView.java
    ├── TransactionView.java
    ├── DashboardView.java
    ├── ProductManagementView.java
    ├── ReportView.java
    ├── HistoryView.java
    └── DiscountManagementView.java   # NEW
```

### 4.2 SOLID Principles Implementation

| Principle | Implementation |
|-----------|----------------|
| **S** - Single Responsibility | ProductService hanya menangani logika produk |
| **O** - Open/Closed | PaymentMethod dapat di-extend tanpa modifikasi |
| **L** - Liskov Substitution | CashPayment & EWalletPayment interchangeable |
| **I** - Interface Segregation | DAO interfaces terpisah per entity |
| **D** - Dependency Inversion | Services depend on DAO interfaces |

### 4.3 Implementasi Singleton - DatabaseConnection

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/agripos",
                "postgres", 
                "password"
            );
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}
```

#### 4.4 Implementasi Singleton - DiscountConfigService (NEW)

```java
public class DiscountConfigService {
    private static DiscountConfigService instance;
    private final ObservableList<DiscountConfig> discountConfigs;
    
    private DiscountConfigService() {
        discountConfigs = FXCollections.observableArrayList();
        loadDefaultDiscounts();
    }
    
    public static synchronized DiscountConfigService getInstance() {
        if (instance == null) {
            instance = new DiscountConfigService();
        }
        return instance;
    }
    
    private void loadDefaultDiscounts() {
        discountConfigs.addAll(
            new DiscountConfig("Diskon Umum", "UMUM5", "Persentase", 5, 0, 0, true),
            new DiscountConfig("Diskon Member", "MEMBER10", "Persentase", 10, 0, 0, true),
            new DiscountConfig("Diskon Bulk", "BULK15", "Persentase", 15, 0, 5, true),
            new DiscountConfig("Welcome Discount", "WELCOME", "Persentase", 5, 0, 0, true),
            new DiscountConfig("Promo 50K", "PROMO50K", "Nominal", 50000, 200000, 0, true)
        );
    }
    
    public ObservableList<DiscountConfig> getActiveDiscounts() {
        return discountConfigs.filtered(DiscountConfig::isActive);
    }
    
    public void addDiscount(DiscountConfig config) {
        discountConfigs.add(config);
    }
    
    public DiscountConfig findByCode(String code) {
        return discountConfigs.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
    
    // Inner class
    public static class DiscountConfig {
        private String name, code, type;
        private double value, minPurchase;
        private int minItems;
        private boolean active;
        
        // Constructor, getters, setters...
    }
}
```
#### 4.5 Implementasi Strategy Pattern - Payment

```java
// Interface
public interface PaymentMethod {
    boolean validate();
    boolean pay(double amount);
    String getMethodName();
    String getDetails();
}

// Concrete Strategy - Cash
public class CashPayment implements PaymentMethod {
    private double amountPaid;
    private double totalAmount;
    private double change;
    
    public CashPayment(double amountPaid, double totalAmount) {
        this.amountPaid = amountPaid;
        this.totalAmount = totalAmount;
        this.change = amountPaid - totalAmount;
    }
    
    @Override
    public boolean validate() {
        return amountPaid >= totalAmount;
    }
    
    @Override
    public boolean pay(double amount) {
        if (validate()) {
            this.change = amountPaid - amount;
            return true;
        }
        return false;
    }
    
    @Override
    public String getMethodName() {
        return "Tunai";
    }
    
    @Override
    public String getDetails() {
        return String.format("Dibayar: Rp %,.0f | Kembalian: Rp %,.0f", 
            amountPaid, change);
    }
    
    public double getChange() {
        return change;
    }
}

// Concrete Strategy - E-Wallet
public class EWalletPayment implements PaymentMethod {
    private String provider;
    private String phoneNumber;
    private double amount;
    
    @Override
    public String getMethodName() {
        return "E-Wallet (" + provider + ")";
    }
    // ... implementation
}

// Factory
public class PaymentMethodFactory {
    public static PaymentMethod createPayment(String type, Object... params) {
        switch (type.toLowerCase()) {
            case "tunai":
            case "cash":
                return new CashPayment((Double) params[0], (Double) params[1]);
            case "e-wallet":
                return new EWalletPayment((String) params[0], (String) params[1], (Double) params[2]);
            case "qris":
                return new QRISPayment((Double) params[0]);
            default:
                throw new IllegalArgumentException("Unknown payment type: " + type);
        }
    }
}
```
#### 4.6 Implementasi Strategy Pattern - Discount

```java
// Interface
public interface DiscountStrategy {
    double calculate(double subtotal, int itemCount);
    String getDescription();
}

// Percentage Discount
public class PercentageDiscount implements DiscountStrategy {
    private double percentage;
    private String name;
    
    public PercentageDiscount(double percentage, String name) {
        this.percentage = percentage;
        this.name = name;
    }
    
    @Override
    public double calculate(double subtotal, int itemCount) {
        return subtotal * (percentage / 100);
    }
    
    @Override
    public String getDescription() {
        return name + " (" + (int)percentage + "%)";
    }
}

// Fixed Discount
public class FixedDiscount implements DiscountStrategy {
    private double amount;
    private String name;
    
    @Override
    public double calculate(double subtotal, int itemCount) {
        return Math.min(amount, subtotal);
    }
}

// Bulk Discount
public class BulkDiscount implements DiscountStrategy {
    private int minQuantity;
    private double percentage;
    
    @Override
    public double calculate(double subtotal, int itemCount) {
        if (itemCount >= minQuantity) {
            return subtotal * (percentage / 100);
        }
        return 0;
    }
}

```
### 4.7 Implementasi DAO Pattern

```java
// Interface
public interface ProductDAO {
    List<Product> findAll();
    Optional<Product> findByCode(String code);
    List<Product> findByCategory(String category);
    void insert(Product product);
    void update(Product product);
    void delete(String code);
    void updateStock(String code, int quantity);
}

// JDBC Implementation
public class JdbcProductDAO implements ProductDAO {
    private final Connection connection;
    
    public JdbcProductDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE active = true ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }
    
    @Override
    public void insert(Product product) {
        String sql = "INSERT INTO products (code, name, category, price, stock, unit, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStock());
            stmt.setString(6, product.getUnit());
            stmt.setString(7, product.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting product: " + e.getMessage());
        }
    }
    
    // ... other implementations
}

```

### 4.8 Implementasi Login dengan Role Selection (NEW)

```java
public class LoginView {
    private TextField usernameField;
    private PasswordField passwordField;
    private ToggleGroup roleToggle;
    private ToggleButton adminButton, kasirButton;
    private String selectedRole = null;
    
    private VBox createRoleSelection() {
        Label roleLabel = new Label("Pilih Role:");
        
        adminButton = new ToggleButton("👔 Admin");
        adminButton.setOnAction(e -> selectedRole = "ADMIN");
        
        kasirButton = new ToggleButton("🏪 Kasir");
        kasirButton.setOnAction(e -> selectedRole = "KASIR");
        
        roleToggle = new ToggleGroup();
        adminButton.setToggleGroup(roleToggle);
        kasirButton.setToggleGroup(roleToggle);
        
        HBox roleBox = new HBox(10, adminButton, kasirButton);
        return new VBox(5, roleLabel, roleBox);
    }
    
    private void handleLogin() {
        // Validasi role harus dipilih
        if (selectedRole == null) {
            showError("Silakan pilih role terlebih dahulu!");
            return;
        }
        
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        User user = loginController.authenticate(username, password, selectedRole);
        if (user != null) {
            openMainView(user);
        } else {
            showError("Login gagal! Username, password, atau role tidak sesuai.");
        }
    }
}

```
### 4.9 Implementasi Responsive Design

```java
public class LoginView {
    private void applyResponsiveStyles(Scene scene) {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            
            if (width < 400) {
                // Mobile portrait
                applyMobileStyles();
            } else if (width < 600) {
                // Tablet
                applyTabletStyles();
            } else {
                // Desktop
                applyDesktopStyles();
            }
        });
    }
    
    private void applyMobileStyles() {
        loginCard.setPrefWidth(300);
        titleLabel.setStyle("-fx-font-size: 20px;");
        usernameField.setPrefWidth(250);
    }
    
    private void applyDesktopStyles() {
        loginCard.setPrefWidth(450);
        titleLabel.setStyle("-fx-font-size: 28px;");
        usernameField.setPrefWidth(350);
    }
}

```


---

## 5. Testing

### 5.1 Unit Testing dengan JUnit 5

```java
@DisplayName("Product Service Tests")
class ProductServiceTest {
    
    private ProductService productService;
    
    @Mock
    private ProductDAO productDAO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productDAO);
    }
    
    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() {
        // Given
        List<Product> mockProducts = Arrays.asList(
            new Product("PRD001", "Beras", "Pangan", 15000, 100, "kg"),
            new Product("PRD002", "Pupuk", "Pertanian", 50000, 50, "kg")
        );
        when(productDAO.findAll()).thenReturn(mockProducts);
        
        // When
        List<Product> result = productService.getAllProducts();
        
        // Then
        assertEquals(2, result.size());
        verify(productDAO, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Should calculate discount correctly")
    void testPercentageDiscount() {
        // Given
        DiscountStrategy discount = new PercentageDiscount(10, "Member");
        
        // When
        double result = discount.calculate(100000, 5);
        
        // Then
        assertEquals(10000, result, 0.01);
    }
    
    @Test
    @DisplayName("Should validate cash payment")
    void testCashPaymentValidation() {
        // Given
        PaymentMethod payment = new CashPayment(150000, 125000);
        
        // When & Then
        assertTrue(payment.validate());
        assertEquals(25000, ((CashPayment) payment).getChange(), 0.01);
    }
}

```
### 5.2 Test Coverage Summary

| Package | Class Coverage | Method Coverage | Line Coverage |
|---------|---------------|-----------------|---------------|
| model | 100% | 95% | 92% |
| service | 100% | 88% | 85% |
| strategy | 100% | 90% | 88% |
| dao | 80% | 75% | 70% |
| **Total** | **95%** | **87%** | **84%** |


### 5.2 Sample Test Cases

```java
@Test
void shouldAddItemToCart() throws OutOfStockException {
    Product product = new Product("P001", "Beras", "Beras", 65000, 100);
    cartService.addToCart(product, 2);
    assertFalse(cartService.isCartEmpty());
}

@Test
void shouldThrowExceptionWhenInsufficientStock() {
    Product product = new Product("P001", "Beras", "Beras", 65000, 5);
    assertThrows(OutOfStockException.class, () -> 
        cartService.addToCart(product, 100));
}
```

---

## 6. Screenshot Aplikasi

### 6.1 Login Screen
![Login Screen](/praktikum/week15-proyek-kelompok/screenshots/login%20agripos.png)

Fitur:

- Input username dan password
- Pilihan role (Admin/Kasir) dengan toggle button
- Validasi role sebelum login
- Responsive design (mobile/desktop)

### 6.2 Dashboard Admin

![Dasboard admin](/praktikum/week15-proyek-kelompok/screenshots/dasboard%20admin.png)

Fitur:

- Total transaksi hari ini
- Revenue harian
- Produk terjual
- Low stock alert
- Grafik penjualan

### 6.3 Manajemen Produk (Admin)

![Manajemen Product (admin)](/praktikum/week15-proyek-kelompok/screenshots/manajemen%20product%20admin.png)

Fitur:

- Tabel produk dengan pagination
- Form tambah/edit produk
- Search dan filter kategori
- CRUD operations

### 6.4 Manajemen Diskon (Admin)

![Manajemen Diskon](/praktikum/week15-proyek-kelompok/screenshots/manajement%20diskon%20admin.png)

Fitur:

- Tabel diskon dengan status aktif/nonaktif
- Form tambah diskon (Persentase, Nominal, Bulk, Voucher)
- Edit dan hapus diskon
- Toggle aktif/nonaktif
- Search diskon
- Real-time sync ke Kasir

### 6.5 Transaksi (kasir)

![Transaksi (kasir)](/praktikum/week15-proyek-kelompok/screenshots/transaksi%20kasir.png)

Fitur:

- Pencarian produk
- Keranjang belanja
- Apply diskon dari dropdown (dikelola Admin)
- Input voucher code
- Multi payment method
- Preview struk

### 6.6 Laporan Penjualan(admin)

![Laporan Penjualan(admin)](/praktikum/week15-proyek-kelompok/screenshots/Laporan%20Penjualan(admin).jpg)

Fitur:

- Laporan harian
- Laporan periode
- Export report

### Riwayat Transaksi (kasir)

![Riwayat Transaksi (kasir)](/praktikum/week15-proyek-kelompok/screenshots/Riwayat%20Transaksi%20(kasir).jpg)

Fitur:

- Daftar transaksi
- Filter tanggal
- Detail transaksi
- Cetak ulang struk


---

## 7. Hasil yang didapat

### 7.1 

### 7.1 Hasil Pembelajaran
1. ✅ Berhasil mengintegrasikan semua konsep OOP
2. ✅ Menerapkan Design Patterns (Singleton, Strategy, Factory, DAO)
3. ✅ Menerapkan SOLID Principles dan DIP
4. ✅ Mengembangkan GUI dengan JavaFX
5. ✅ Melakukan Unit Testing dengan JUnit 5 & Mockito

### 7.2 Fitur yang Diimplementasi
- [x] FR-1: Manajemen Produk (CRUD)
- [x] FR-2: Transaksi Penjualan
- [x] FR-3: Sistem Diskon & Promosi
- [x] FR-4: Multi Payment Methods (Cash, E-Wallet, QRIS)
- [x] FR-5: Struk dan Laporan Penjualan
- [x] FR-6: Login dan Akses Kontrol (Admin & Kasir)
- [x] FR-7: Manajemen Diskon oleh Admin (NEW)

### 7.3 Implementasi Diskon (New Feature)
- [x] Model Transaction dengan field discount
- [x] Service layer yang menghitung dan mempropagasi diskon
- [x] DAO layer yang menyimpan/mengambil diskon dari database
- [x] Database schema update dengan kolom discount
- [x] Receipt service yang menampilkan diskon di struk
- [x] Automatic database migration saat startup
- [x] Diskon ditampilkan di struk riwayat transaksi

### 7.4 Manajemen Diskon Admin-Kasir Sync (NEW Feature)
Fitur baru yang memungkinkan Admin mengelola diskon dan otomatis tersinkron dengan tampilan Kasir.

#### Komponen Utama:
- **DiscountConfigService (Singleton)**: Service shared untuk menyimpan konfigurasi diskon
- **DiscountManagementView**: UI Admin untuk CRUD diskon
- **TransactionView Integration**: Kasir dapat menggunakan diskon yang dikelola Admin

#### Tipe Diskon yang Didukung:
| Tipe | Deskripsi | Contoh |
|------|-----------|--------|
| Persentase | Diskon berdasarkan persentase | Diskon 5%, 10%, 15% |
| Nominal | Potongan harga tetap | Rp 50.000, Rp 100.000 |
| Bulk | Diskon untuk pembelian quantity tertentu | Min 5 item dapat 15% |
| Voucher | Kode promo dengan nilai tetap | PROMO50K |

#### Alur Kerja:
1. Admin menambah/edit/hapus diskon di tab "🎁 Manajemen Diskon"
2. Perubahan langsung tersimpan di `DiscountConfigService` (singleton)
3. Kasir dapat melihat diskon terbaru dengan klik tombol refresh 🔄
4. Kasir dapat menggunakan diskon via dropdown atau input kode voucher

#### Class Baru:
```java
// DiscountConfigService.java - Singleton untuk shared discount data
public class DiscountConfigService {
    private static DiscountConfigService instance;
    private final ObservableList<DiscountConfig> discountConfigs;
    
    public static synchronized DiscountConfigService getInstance();
    public ObservableList<DiscountConfig> getActiveDiscounts();
    public void addDiscount(DiscountConfig config);
    public void updateDiscount(String code, DiscountConfig updated);
    public void removeDiscount(String code);
    public DiscountConfig findByCode(String code);
}

// DiscountManagementView.java - UI untuk Admin mengelola diskon
public class DiscountManagementView {
    public VBox createContent();  // Form CRUD + TableView diskon
}
```

### 7.5 Future Improvements
1. Password hashing dengan BCrypt
2. Connection pooling dengan HikariCP
3. Export laporan ke PDF/Excel
4. ~~Advanced discount rules (quantity-based, category-based)~~ ✅ DONE
5. Inventory management dengan low stock alerts
6. Persist discount config ke database (saat ini hanya di memory)
7. Multi-store support dengan centralized discount management
8. Discount scheduling (diskon dengan periode waktu tertentu)
9. Customer-specific discounts (loyalty program)

---

## Lampiran

### A. Cara Menjalankan

```bash
# Setup database (first time only)
psql -h localhost -U postgres -d agripos -f sql/schema.sql
psql -h localhost -U postgres -d agripos -f sql/seed.sql

# Build project
mvn clean compile

# Run aplikasi
mvn javafx:run

# Run tests
mvn test

# Database migration akan otomatis berjalan saat aplikasi startup
# (Jika kolom discount belum ada di tabel transactions)
```

### B. Demo Credentials
**Admin Dashboard:**
- Username: `admin`
- Password: `admin123`
- Akses: Manajemen produk, dashboard, laporan penjualan

**Kasir:**
- Username: `kasir1`
- Password: `kasir123`
- Akses: Transaksi penjualan, riwayat transaksi, daftar produk (read-only)


---

## Kode Program Utama

### A. Implementasi Diskon (Model)

```java
// Transaction.java - Menambahkan field discount
public class Transaction {
    private double subtotal;
    private double discount;      // ← NEW: Menyimpan jumlah diskon
    private double tax;
    private double total;
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
```

### B. Kalkulasi Diskon (Service)

```java
// TransactionService.java - Menghitung dan mempropagasi diskon
public CheckoutSummary checkout(String cashierUsername, String paymentMethodName,
                                double amountPaid) throws Exception {
    double subtotal = cartService.getCartTotal();
    double discount = cartService.calculateTotalDiscount();  // ← Hitung diskon
    double tax = (subtotal - discount) * TAX_RATE;          // ← Tax dari (subtotal - discount)
    double total = subtotal - discount + tax;
    
    // Pass diskon ke transaction
    Transaction transaction = createTransaction(cashierUsername, subtotal, discount, 
                                               tax, total, paymentMethodName, 
                                               amountPaid, change);
    
    // Simpan transaksi ke database
    transactionDAO.insert(transaction);
    
    // Return summary untuk UI
    return new CheckoutSummary(subtotal, discount, tax, total, ...);
}
```

### C. Tampilan Diskon di Struk

```java
// ReceiptService.java - Menampilkan diskon di struk
public String generateReceipt(Transaction transaction) {
    StringBuilder sb = new StringBuilder();
    
    // ... header dan items ...
    
    // Summary dengan diskon
    sb.append(String.format("%-15s %15s\n", "Subtotal:", formatCurrency(transaction.getSubtotal())));
    if (transaction.getDiscount() > 0) {
        sb.append(String.format("%-15s %15s\n", "Diskon:", "-" + formatCurrency(transaction.getDiscount())));
    }
    sb.append(String.format("%-15s %15s\n", "Pajak (10%):", formatCurrency(transaction.getTax())));
    sb.append("================================\n");
    sb.append(String.format("%-15s %15s\n", "TOTAL:", formatCurrency(transaction.getTotal())));
    
    return sb.toString();
}
```

### D. Penyimpanan Diskon (DAO)

```java
// JdbcTransactionDAO.java - INSERT dengan diskon
public void insert(Transaction transaction) throws Exception {
    String sql = """
        INSERT INTO transactions (transaction_code, transaction_date, cashier_username,
        subtotal, discount, tax, total, payment_method, amount_paid, change_amount, status)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, transaction.getTransactionCode());
        stmt.setTimestamp(2, Timestamp.valueOf(transaction.getTransactionDate()));
        stmt.setString(3, transaction.getCashierUsername());
        stmt.setDouble(4, transaction.getSubtotal());
        stmt.setDouble(5, transaction.getDiscount());      // ← Simpan diskon
        stmt.setDouble(6, transaction.getTax());
        // ... parameter lainnya ...
        
        stmt.executeUpdate();
    }
}
```

### E. Database Migration Otomatis

```java
// DatabaseMigration.java - Jalankan saat startup
public static void runMigrations() {
    try {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        
        // Cek apakah kolom discount sudah ada
        if (!columnExists(conn, "transactions", "discount")) {
            // Tambahkan kolom jika belum ada
            String sql = "ALTER TABLE transactions ADD COLUMN discount DECIMAL(12,2) NOT NULL DEFAULT 0";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                LOGGER.info("✓ Kolom discount berhasil ditambahkan");
            }
        } else {
            LOGGER.info("✓ Kolom discount sudah ada");
        }
        
        conn.close();
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Gagal menjalankan migrasi", e);
        throw new RuntimeException("Database migration failed", e);
    }
}
```

---

## Kesimpulan

Dengan mengimplementasikan sistem diskon yang terintegrasi di seluruh layer aplikasi (Model → Service → DAO → Database → Presentation), aplikasi Agri-POS menjadi lebih lengkap dan siap untuk use case bisnis yang lebih kompleks. 

**Key Learning Points:**
1. **Layered Architecture** memudahkan maintenance dan testing
2. **Design Patterns** (Singleton, Strategy, DAO) membuat kode lebih flexible
3. **Automatic Database Migration** memastikan consistency antara code dan database
4. **Unit Testing** sangat penting untuk validasi business logic
5. **JavaFX GUI** memberikan user experience yang baik untuk desktop application

Aplikasi ini dapat di-scale lebih lanjut dengan menambahkan fitur-fitur seperti advanced discount rules, inventory management, dan reporting yang lebih kompleks.


