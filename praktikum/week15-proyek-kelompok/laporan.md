# Laporan Praktikum Week 15: Proyek Kelompok
## Agri-POS - Sistem Point of Sale Pertanian

### Informasi Praktikan
- **Nama+Nim**: 
                
                [1. Indah Ruwahna Anugraheni (240202866)] 

                [2. Lia Lusianti (240202869)]

                [3. Fikianto (240202899)]

                [4. Rizal Ramadhani (240202883)]

- **Kelas**: [3IKRB]
- **Tanggal**: [18 Januari 2026]

---

## Daftar Isi
1. [Pendahuluan](#1-pendahuluan)
2. [Analisis Kebutuhan](#2-analisis-kebutuhan)
3. [Desain Sistem](#3-desain-sistem)
4. [Implementasi](#4-implementasi)
5. [Testing](#5-testing)
6. [Screenshot](#6-screenshot)
7. [Kesimpulan](#7-kesimpulan)

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

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           PRESENTATION LAYER                                │
│  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐   │
│  │   LoginView   │ │   MainView    │ │TransactionView│ │  ReportView   │   │
│  └───────────────┘ └───────────────┘ └───────────────┘ └───────────────┘   │
│  ┌───────────────┐ ┌───────────────┐ ┌────────────────────┐                 │
│  │ DashboardView │ │ProductMgmtView│ │DiscountMgmtView    │ (JavaFX GUI)   │
│  └───────────────┘ └───────────────┘ └────────────────────┘                 │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ Events & User Actions
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           CONTROLLER LAYER                                  │
│         ┌───────────────────┐           ┌───────────────────┐               │
│         │   PosController   │           │  LoginController  │               │
│         │  - productService │           │  - authService    │               │
│         │  - cartService    │           │  - currentUser    │               │
│         │  - transService   │           └───────────────────┘               │
│         │  - reportService  │                                               │
│         │  - receiptService │                                               │
│         └───────────────────┘                                               │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ Business Logic Calls
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                            SERVICE LAYER                                    │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐       │
│  │ProductService│ │ CartService  │ │TransService  │ │ AuthService  │       │
│  │ - productDAO │ │ - cart: Cart │ │ - transDAO   │ │ - userDAO    │       │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘       │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐                     │
│  │ReportService │ │ReceiptService│ │DiscountConfig    │ (Business Logic)   │
│  └──────────────┘ └──────────────┘ │Service (Singleton)│                    │
│                                    └──────────────────┘                     │
│                                                                             │
│  ┌─────────────────────── PAYMENT (Strategy Pattern) ─────────────────────┐ │
│  │    <<interface>>         ┌─────────────┐ ┌─────────────┐ ┌───────────┐ │ │
│  │    PaymentMethod    ────>│ CashPayment │ │EWalletPayment│ │QRISPayment│ │ │
│  │                          └─────────────┘ └─────────────┘ └───────────┘ │ │
│  │    PaymentMethodFactory (Factory Pattern - Create payment instances)   │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ Data Access via Interfaces (DIP)
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DAO LAYER                                      │
│                                                                             │
│   <<interface>>            <<interface>>            <<interface>>           │
│  ┌──────────────┐         ┌──────────────┐         ┌──────────────┐        │
│  │  ProductDAO  │         │   UserDAO    │         │TransactionDAO│        │
│  │ + insert()   │         │ + insert()   │         │ + findById()  │        │
│  │ + update()   │         │ + existsBy() │         │ + findByDate  │        │
│  │ + delete()   │         └──────┬───────┘         │ + findByCode  │        │
│  │ + findAll()  │                │                 │ + findAll()   │        │
│  │ + findBy..() │                │                 └──────┬───────┘        │
│  └──────┬───────┘                │                        │                │
│         │                        │                        │                │
│         ▼                        ▼                        ▼                │
│  ┌──────────────┐         ┌──────────────┐         ┌──────────────┐        │
│  │JdbcProductDAO│         │ JdbcUserDAO  │         │JdbcTransDAO  │        │
│  │ - connection │         │ - connection │         │ - connection │        │
│  └──────────────┘         └──────────────┘         └──────────────┘        │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ JDBC Connection
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         DATABASE LAYER                                      │
│                                                                             │
│       ┌─────────────────────────────────────────────────────────┐          │
│       │           DatabaseConnection (Singleton Pattern)         │          │
│       │                   - instance: Connection                 │          │
│       │                   + getInstance(): Connection            │          │
│       └─────────────────────────────────────────────────────────┘          │
│                                   │                                         │
│                                   ▼                                         │
│                        ┌─────────────────┐                                  │
│                        │   PostgreSQL    │                                  │
│                        │   Database      │                                  │
│                        └─────────────────┘                                  │
└─────────────────────────────────────────────────────────────────────────────┘
```

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

### 4.3 Key Implementation Code

#### Dependency Injection di AppJavaFx.java
```java
private void initializeApplication() {
    Connection conn = DatabaseConnection.getInstance().getConnection();
    
    // DAO Layer
    ProductDAO productDAO = new JdbcProductDAO(conn);
    UserDAO userDAO = new JdbcUserDAO(conn);
    
    // Register Payment Methods (Strategy)
    PaymentMethodFactory.registerPaymentMethod("CASH", new CashPayment());
    PaymentMethodFactory.registerPaymentMethod("E-WALLET", new EWalletPayment());
    
    // Service Layer (DI)
    ProductService productService = new ProductService(productDAO);
    AuthService authService = new AuthService(userDAO);
    
    // Controller (DI)
    PosController controller = new PosController(productService, ...);
}
```

#### Strategy Pattern - Payment
```java
public interface PaymentMethod {
    String getMethodName();
    double processPayment(double total, double amountPaid) throws PaymentException;
    boolean validatePayment(double total, double amountPaid);
}

public class CashPayment implements PaymentMethod {
    @Override
    public double processPayment(double total, double amountPaid) throws PaymentException {
        if (amountPaid < total) {
            throw new PaymentException("Pembayaran tidak mencukupi");
        }
        return amountPaid - total;
    }
}
```

---

## 5. Testing

### 5.1 Unit Test Results

| Test Class | Tests | Passed | Coverage |
|------------|-------|--------|----------|
| ProductServiceTest | 12 | 12 ✅ | 95% |
| CartServiceTest | 10 | 10 ✅ | 90% |
| PaymentMethodTest | 13 | 13 ✅ | 100% |
| **Total** | **35** | **35** | **~85%** |

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

## 6. Screenshot

### 6.1 Login Screen
![Login Screen](screenshots/login.png)
*Form login dengan username dan password untuk admin & kasir*


**KASIR**

### 6.2 Transaction View
![Transaction](/praktikum/week15-proyek-kelompok/screenshots/transaksi%20agripos.png)

*Tampilan transaksi dengan produk, keranjang, dan checkout*

![Transaction](/praktikum/week15-proyek-kelompok/screenshots/transaksi%20agripos2.png)

*Tampilan transaksi dengan produk, keranjang, dan checkout*

![Transaction](/praktikum/week15-proyek-kelompok/screenshots/Daftra%20produk%20kasir.png)

*Tampilan daftar product kasir*

![Transaction](/praktikum/week15-proyek-kelompok/screenshots/riwayat%20transaksi%20kasir.png)

*tampilan riwayat transaksi kasir*

![Transaction](/praktikum/week15-proyek-kelompok/screenshots/struk%20.png)

*Tampilan struk kasir*

### 6.3 Product Management (Admin)
![Product Management](/praktikum/week15-proyek-kelompok/screenshots/dasboard%20admin.png)

*dasboard admin*

![Product Management](/praktikum/week15-proyek-kelompok/screenshots/dasboasrd2%20admin.png)

*dasboard admin*

![Product Management](/praktikum/week15-proyek-kelompok/screenshots/manajemen%20product%20admin.png)

*manajement produk admin*

![Product Management](/praktikum/week15-proyek-kelompok/screenshots/laporan%20penjualan%20admin.png)

*laporan penjualan admin*


---

## 7. Kesimpulan

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

### C. Struktur Folder Project

```
week15-proyek-kelompok/
├── src/main/java/com/upb/agripos/
│   ├── AppJavaFx.java              (Main entry point)
│   ├── controller/                 (Controller layer)
│   │   ├── LoginController.java
│   │   └── PosController.java
│   ├── service/                    (Business logic layer)
│   │   ├── ProductService.java
│   │   ├── CartService.java
│   │   ├── TransactionService.java
│   │   ├── AuthService.java
│   │   ├── ReceiptService.java
│   │   ├── ReportService.java
│   │   └── payment/
│   │       ├── PaymentMethod.java (interface)
│   │       ├── CashPayment.java
│   │       ├── EWalletPayment.java
│   │       ├── QRISPayment.java
│   │       └── PaymentMethodFactory.java
│   ├── dao/                        (Data access layer)
│   │   ├── ProductDAO.java
│   │   ├── UserDAO.java
│   │   ├── TransactionDAO.java
│   │   ├── JdbcProductDAO.java
│   │   ├── JdbcUserDAO.java
│   │   └── JdbcTransactionDAO.java
│   ├── model/                      (Data model)
│   │   ├── Product.java
│   │   ├── User.java
│   │   ├── Transaction.java
│   │   ├── TransactionItem.java
│   │   ├── Cart.java
│   │   ├── CartItem.java
│   │   ├── CheckoutSummary.java
│   │   └── Discount.java
│   ├── util/
│   │   ├── DatabaseConnection.java (Singleton)
│   │   └── DatabaseMigration.java  (Auto migration)
│   └── view/                       (JavaFX UI)
│       ├── LoginView.java
│       ├── MainView.java
│       └── ...
├── sql/                            (Database scripts)
│   ├── schema.sql
│   ├── seed.sql
│   ├── update_transactions.sql
│   └── migration_add_discount_column.sql
├── pom.xml
├── laporan.md                      (This report)
└── screenshots/
```

### D. Dokumentasi Tambahan

Lihat file-file berikut untuk dokumentasi lengkap:
- `FIX_DISCOUNT_HISTORY_RECEIPT.md` - Detail implementasi fitur diskon
- `docs/` folder - Dokumentasi teknis lengkap

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


