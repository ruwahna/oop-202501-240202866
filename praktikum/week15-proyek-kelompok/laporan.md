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

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-1 | Manajemen Produk (CRUD) | High |
| FR-2 | Transaksi Penjualan | High |
| FR-3 | Sistem Diskon & Promosi | High |
| FR-4 | Multi Payment Methods | High |
| FR-5 | Struk dan Laporan | High |
| FR-6 | Login dan Akses Kontrol | High |



### 2.2 Use Case Diagram


![UseCase](/praktikum/week15-proyek-kelompok/screenshots/usecase%20agripos.jpg)



### 2.3 Actor Description

| Actor | Description | Access Level |
|-------|-------------|--------------|
| **Kasir** | Operator transaksi penjualan | Login, View Produk (read-only), Transaksi, Keranjang, Checkout, Cetak Struk, Riwayat Transaksi |
| **Admin** | Administrator sistem | Full Access: Dashboard, CRUD Produk, Laporan Penjualan, Export Report, Low Stock Alert |

### 2.4 Use Case Detail per Actor

#### 🏪 Kasir - Use Case List
| No | Use Case | Deskripsi | Tab Menu |
|----|----------|-----------|----------|
| 1 | Login | Autentikasi masuk sistem | LoginView |
| 2 | Logout | Keluar dari sistem | Header |
| 3 | New Transaction | Membuat transaksi penjualan baru | 🛒 Transaksi Baru |
| 4 | Search Product | Mencari produk berdasarkan nama/kode | 🛒 Transaksi Baru |
| 5 | Add to Cart | Menambahkan produk ke keranjang | 🛒 Transaksi Baru |
| 6 | Update Cart Qty | Mengubah jumlah item di keranjang | 🛒 Transaksi Baru |
| 7 | Remove from Cart | Menghapus item dari keranjang | 🛒 Transaksi Baru |
| 8 | Clear Cart | Mengosongkan seluruh keranjang | 🛒 Transaksi Baru |
| 9 | Checkout (Cash) | Proses pembayaran tunai | 🛒 Transaksi Baru |
| 10 | Checkout (E-Wallet) | Proses pembayaran e-wallet (OVO, GoPay, Dana, ShopeePay) | 🛒 Transaksi Baru |
| 11 | Checkout (QRIS) | Proses pembayaran QRIS | 🛒 Transaksi Baru |
| 12 | Apply Discount | Menerapkan diskon untuk produk/transaksi | 🛒 Transaksi Baru |
| 13 | Print Receipt | Mencetak struk pembelian dengan detail diskon | 🛒 Transaksi Baru |
| 14 | View Product List | Melihat daftar produk (read-only) | 📦 Daftar Produk |
| 15 | View Transaction History | Melihat riwayat transaksi dengan detail | 📋 Riwayat Transaksi |
| 16 | View Receipt History | Melihat struk transaksi sebelumnya | 📋 Riwayat Transaksi |

#### 👔 Admin - Use Case List
| No | Use Case | Deskripsi | Tab Menu |
|----|----------|-----------|----------|
| 1 | Login | Autentikasi masuk sistem | LoginView |
| 2 | Logout | Keluar dari sistem | Header |
| 3 | View Dashboard | Melihat ringkasan statistik (penjualan, produk, grafik) | 📊 Dashboard |
| 4 | Add Product | Menambah produk baru (kode, nama, kategori, harga, stok) | 📦 Manajemen Produk |
| 5 | Edit Product | Mengubah data produk yang sudah ada | 📦 Manajemen Produk |
| 6 | Delete Product | Menghapus produk dari sistem | 📦 Manajemen Produk |
| 7 | Search Product | Mencari produk untuk di-edit/hapus | 📦 Manajemen Produk |
| 8 | View Low Stock Alert | Melihat produk dengan stok di bawah batas minimum | 📊 Dashboard |
| 9 | Daily Sales Report | Generate laporan penjualan harian | 📈 Laporan Penjualan |
| 10 | Period Sales Report | Generate laporan penjualan periode tertentu | 📈 Laporan Penjualan |
| 11 | Export Report | Export laporan ke file | 📈 Laporan Penjualan |

---

## 3. Desain Sistem

### 3.1 Arsitektur Sistem (Layered Architecture + DIP)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           PRESENTATION LAYER                                │
│  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐   │
│  │   LoginView   │ │   MainView    │ │TransactionView│ │  ReportView   │   │
│  └───────────────┘ └───────────────┘ └───────────────┘ └───────────────┘   │
│  ┌───────────────┐ ┌───────────────┐                                        │
│  │ DashboardView │ │ProductMgmtView│  (JavaFX GUI Components)              │
│  └───────────────┘ └───────────────┘                                        │
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
│  ┌──────────────┐ ┌──────────────┐                                          │
│  │ReportService │ │ReceiptService│  (Business Logic & Rules)               │
│  └──────────────┘ └──────────────┘                                          │
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


![Model classes](/praktikum/week15-proyek-kelompok/screenshots/class%20diagram-model%20classes.drawio.png)

*model classes*

![strategi pattern-paymentment-menthod](/praktikum/week15-proyek-kelompok/screenshots/strategy%20pattern-paymentmethod.drawio.png)

*strategi pattern-paymentment-menthod*

![DAO interface](/praktikum/week15-proyek-kelompok/screenshots/DAO%20interface.drawio.png)

*DAO interface*


#### 3.3.2 Checkout Transaction Sequence

![checkout](/praktikum/week15-proyek-kelompok/screenshots/Checkout%20Transaction%20Sequence-Page-4.drawio.png)

*checkout transaktion*


#### 3.3.3 Admin - Add Product Sequence

![admin sequence](/praktikum/week15-proyek-kelompok/screenshots/Admin%20-%20Add%20Product%20Sequence-Page-5.drawio.png)

*admin sequence*


### 3.4 Design Patterns

| Pattern | Implementation | Purpose |
|---------|----------------|---------|
| **Singleton** | `DatabaseConnection` | Single database connection instance untuk seluruh aplikasi |
| **Strategy** | `PaymentMethod`, `CashPayment`, `EWalletPayment`, `QRISPayment` | Metode pembayaran yang dapat di-extend tanpa ubah kode inti |
| **Factory** | `PaymentMethodFactory` | Membuat instance payment method berdasarkan nama |
| **DAO** | `ProductDAO`, `UserDAO`, `TransactionDAO` + implementasi JDBC | Abstraksi akses database |

### 3.5 Database Schema (ERD)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            DATABASE: agripos                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌──────────────────────────────┐         ┌──────────────────────────────┐     │
│  │           users              │         │          products            │     │
│  ├──────────────────────────────┤         ├──────────────────────────────┤     │
│  │ * id: SERIAL (PK)            │         │ * id: SERIAL (PK)            │     │
│  │   username: VARCHAR(50) [UQ] │         │   code: VARCHAR(20) [UQ]     │     │
│  │   password: VARCHAR(255)     │         │   name: VARCHAR(100)         │     │
│  │   full_name: VARCHAR(100)    │         │   category: VARCHAR(50)      │     │
│  │   role: VARCHAR(20)          │         │   price: DECIMAL(12,2)       │     │
│  │   active: BOOLEAN            │         │   stock: INTEGER             │     │
│  │   created_at: TIMESTAMP      │         │   unit: VARCHAR(20)          │     │
│  │   updated_at: TIMESTAMP      │         │   description: TEXT          │     │
│  └──────────────┬───────────────┘         │   active: BOOLEAN            │     │
│                 │                         │   created_at: TIMESTAMP      │     │
│                 │ 1                       │   updated_at: TIMESTAMP      │     │
│                 │                         └──────────────┬───────────────┘     │
│                 │                                        │                     │
│                 │                                        │ 1                   │
│                 │                                        │                     │
│                 ▼ *                                      │                     │
│  ┌──────────────────────────────┐                       │                     │
│  │        transactions          │                       │                     │
│  ├──────────────────────────────┤                       │                     │
│  │ * id: SERIAL (PK)            │                       │                     │
│  │   transaction_code: VARCHAR  │                       │                     │
│  │       (30) [UQ]              │                       │                     │
│  │   transaction_date: TIMESTAMP│                       │                     │
│  │   cashier_username: VARCHAR  │                       │                     │
│  │   user_id: INTEGER (FK)──────┼───────────────────────┘                     │
│  │   subtotal: DECIMAL(15,2)    │                                             │
│  │   discount: DECIMAL(12,2)    │ ← NEW: Kolom untuk diskon                   │
│  │   tax: DECIMAL(15,2)         │                                             │
│  │   total: DECIMAL(15,2)       │                                             │
│  │   payment_method: VARCHAR(50)│                                             │
│  │   amount_paid: DECIMAL(15,2) │                                             │
│  │   change_amount: DECIMAL     │                                             │
│  │   status: VARCHAR(20)        │                                             │
│  │   notes: TEXT                │                                             │
│  └──────────────┬───────────────┘                                             │
│                 │ 1                                                            │
│                 │                                                              │
│                 ▼ *                                                            │
│  ┌──────────────────────────────┐         ┌──────────────────────────────┐    │
│  │     transaction_items        │         │                              │    │
│  ├──────────────────────────────┤    *    │                              │    │
│  │ * id: SERIAL (PK)            │◄────────┤        (FK to products)      │    │
│  │   transaction_id: INT (FK)───┼─────────┤                              │    │
│  │   product_id: INTEGER (FK)   │         │                              │    │
│  │   product_code: VARCHAR(20)  │         └──────────────────────────────┘    │
│  │   product_name: VARCHAR(100) │                                             │
│  │   quantity: INTEGER          │                                             │
│  │   unit_price: DECIMAL(12,2)  │                                             │
│  │   subtotal: DECIMAL(15,2)    │                                             │
│  └──────────────────────────────┘                                             │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘

RELASI:
═══════
• users (1) ──────< (*) transactions     : Satu user bisa punya banyak transaksi
• transactions (1) ──────< (*) transaction_items : Satu transaksi punya banyak item
• products (1) ──────< (*) transaction_items     : Satu produk bisa ada di banyak item
```

### 3.6 SQL DDL (Data Definition Language)

```sql
-- ============================================
-- Tabel Users (untuk autentikasi)
-- ============================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'KASIR',  -- KASIR, ADMIN
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Tabel Products (produk pertanian)
-- ============================================
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL DEFAULT 'kg',
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Tabel Transactions (transaksi penjualan)
-- ============================================
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    transaction_code VARCHAR(30) UNIQUE NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cashier_username VARCHAR(50),
    user_id INTEGER REFERENCES users(id),
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0,
    tax DECIMAL(15,2) NOT NULL DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,        -- Tunai, E-Wallet, QRIS
    amount_paid DECIMAL(15,2) NOT NULL,
    change_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'COMPLETED',     -- PENDING, COMPLETED, CANCELLED
    notes TEXT
);

-- ============================================
-- Tabel Transaction Items (detail transaksi)
-- ============================================
CREATE TABLE transaction_items (
    id SERIAL PRIMARY KEY,
    transaction_id INTEGER REFERENCES transactions(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(id),
    product_code VARCHAR(20) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL
);

-- ============================================
-- Indexes untuk performa query
-- ============================================
CREATE INDEX idx_products_code ON products(code);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_transactions_code ON transactions(transaction_code);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_user ON transactions(user_id);
```

---

## 4. Implementasi

### 4.1 Package Structure

```
com.upb.agripos/
├── AppJavaFx.java              # Entry point & DI setup
├── model/                      # 7 classes
├── dao/                        # 6 classes (3 interfaces + 3 implementations)
├── service/                    # 6 classes
├── payment/                    # 4 classes (Strategy pattern)
├── controller/                 # 2 classes
├── view/                       # 5 classes
├── exception/                  # 5 classes
└── util/                       # 1 class
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

### 7.3 Implementasi Diskon (New Feature)
- [x] Model Transaction dengan field discount
- [x] Service layer yang menghitung dan mempropagasi diskon
- [x] DAO layer yang menyimpan/mengambil diskon dari database
- [x] Database schema update dengan kolom discount
- [x] Receipt service yang menampilkan diskon di struk
- [x] Automatic database migration saat startup
- [x] Diskon ditampilkan di struk riwayat transaksi

### 7.4 Future Improvements
1. Password hashing dengan BCrypt
2. Connection pooling dengan HikariCP
3. Export laporan ke PDF/Excel
4. Advanced discount rules (quantity-based, category-based)
5. Inventory management dengan low stock alerts

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


