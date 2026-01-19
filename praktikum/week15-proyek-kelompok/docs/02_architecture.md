# Arsitektur Sistem
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Overview Arsitektur

Agri-POS menggunakan **Layered Architecture** (Arsitektur Berlapis) dengan penerapan prinsip **Dependency Inversion Principle (DIP)**.

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │  LoginView  │ │  MainView   │ │TransactionV │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐                            │
│  │ProductMgmtV │ │ ReportView  │                            │
│  └─────────────┘ └─────────────┘                            │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                     CONTROLLER LAYER                         │
│  ┌─────────────────────┐ ┌─────────────────────┐            │
│  │    PosController    │ │   LoginController   │            │
│  └─────────────────────┘ └─────────────────────┘            │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                           │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │ProductServce│ │ AuthService │ │ CartService │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │TransactionS │ │ReceiptServce│ │ReportService│            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                       DAO LAYER                              │
│  ┌───────────────────────────────────────────────┐          │
│  │              <<interface>>                    │          │
│  │    ProductDAO    UserDAO    TransactionDAO    │          │
│  └───────────────────────────────────────────────┘          │
│                         △                                    │
│                         │                                    │
│  ┌───────────────────────────────────────────────┐          │
│  │           JDBC Implementations                │          │
│  │  JdbcProductDAO  JdbcUserDAO  JdbcTransactionDAO│        │
│  └───────────────────────────────────────────────┘          │
└─────────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                     DATABASE LAYER                           │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              PostgreSQL Database                     │    │
│  │    ┌──────────┐ ┌──────────┐ ┌──────────────┐       │    │
│  │    │  users   │ │ products │ │ transactions │       │    │
│  │    └──────────┘ └──────────┘ └──────────────┘       │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 2. Komponen Utama

#### 2.1 Presentation Layer (View)
Bertanggung jawab untuk tampilan GUI menggunakan JavaFX.

| Komponen | Tanggung Jawab |
|----------|----------------|
| `LoginView` | Form login pengguna |
| `MainView` | Layout utama dengan tab navigation |
| `ProductManagementView` | CRUD produk (Admin) |
| `TransactionView` | Transaksi dan checkout |
| `ReportView` | Tampilan laporan |

#### 2.2 Controller Layer
Menghubungkan View dengan Service layer.

| Komponen | Tanggung Jawab |
|----------|----------------|
| `PosController` | Koordinasi operasi POS utama |
| `LoginController` | Proses autentikasi |

#### 2.3 Service Layer
Business logic terpisah dari akses data.

| Komponen | Tanggung Jawab |
|----------|----------------|
| `ProductService` | Logika bisnis produk |
| `AuthService` | Autentikasi & otorisasi |
| `CartService` | Manajemen keranjang |
| `TransactionService` | Proses transaksi |
| `ReceiptService` | Generate struk |
| `ReportService` | Generate laporan |

#### 2.4 DAO Layer
Abstraksi akses database.

| Interface | Implementation | Tanggung Jawab |
|-----------|----------------|----------------|
| `ProductDAO` | `JdbcProductDAO` | CRUD produk |
| `UserDAO` | `JdbcUserDAO` | CRUD pengguna |
| `TransactionDAO` | `JdbcTransactionDAO` | CRUD transaksi |

### 3. Design Patterns

#### 3.1 Singleton Pattern - DatabaseConnection
```
┌─────────────────────────────────────────┐
│          DatabaseConnection             │
├─────────────────────────────────────────┤
│ - instance: DatabaseConnection          │
│ - connection: Connection                │
├─────────────────────────────────────────┤
│ - DatabaseConnection()                  │
│ + getInstance(): DatabaseConnection     │
│ + getConnection(): Connection           │
│ + closeConnection(): void               │
└─────────────────────────────────────────┘
```

#### 3.2 Strategy Pattern - PaymentMethod
```
                    <<interface>>
┌─────────────────────────────────────────┐
│              PaymentMethod              │
├─────────────────────────────────────────┤
│ + getMethodName(): String               │
│ + processPayment(): double              │
│ + validatePayment(): boolean            │
│ + getReceiptDescription(): String       │
└─────────────────────────────────────────┘
                     △
          ┌──────────┴──────────┐
          │                     │
┌─────────────────┐   ┌─────────────────┐
│   CashPayment   │   │  EWalletPayment │
├─────────────────┤   ├─────────────────┤
│ + getMethodName │   │ + getMethodName │
│ + processPayment│   │ + processPayment│
│ + validate...   │   │ + validate...   │
└─────────────────┘   └─────────────────┘
```

#### 3.3 Factory Pattern - PaymentMethodFactory
```
┌─────────────────────────────────────────┐
│          PaymentMethodFactory           │
├─────────────────────────────────────────┤
│ - methods: Map<String, PaymentMethod>   │
├─────────────────────────────────────────┤
│ + registerPaymentMethod(): void         │
│ + getPaymentMethod(): PaymentMethod     │
│ + getAvailableMethods(): Set<String>    │
└─────────────────────────────────────────┘
```

### 4. Data Flow

#### 4.1 Flow Transaksi
```
User Input → View → Controller → Service → DAO → Database
                                    ↓
User Display ← View ← Controller ← Service Result
```

#### 4.2 Flow Autentikasi
```
1. User input credentials di LoginView
2. LoginController.authenticate() dipanggil
3. AuthService.login() memvalidasi dengan UserDAO
4. Session disimpan di AuthService
5. MainView ditampilkan sesuai role
```

### 5. Dependency Injection Flow

```java
// Di AppJavaFx.java (Main Entry Point)

// 1. Database Connection
Connection conn = DatabaseConnection.getInstance().getConnection();

// 2. DAO Layer (depend on Connection)
ProductDAO productDAO = new JdbcProductDAO(conn);
UserDAO userDAO = new JdbcUserDAO(conn);
TransactionDAO transactionDAO = new JdbcTransactionDAO(conn);

// 3. Service Layer (depend on DAO)
ProductService productService = new ProductService(productDAO);
AuthService authService = new AuthService(userDAO);
CartService cartService = new CartService();
TransactionService transactionService = new TransactionService(transactionDAO, cartService);

// 4. Controller Layer (depend on Services)
PosController posController = new PosController(
    productService, cartService, transactionService,
    receiptService, reportService, authService
);
```

### 6. Package Structure

```
com.upb.agripos/
├── AppJavaFx.java              # Entry point & DI setup
├── model/                      # Domain models
│   ├── Product.java
│   ├── User.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Transaction.java
│   ├── TransactionItem.java
│   └── CheckoutSummary.java
├── dao/                        # Data Access Objects
│   ├── ProductDAO.java         # Interface
│   ├── UserDAO.java            # Interface
│   ├── TransactionDAO.java     # Interface
│   ├── JdbcProductDAO.java     # Implementation
│   ├── JdbcUserDAO.java        # Implementation
│   └── JdbcTransactionDAO.java # Implementation
├── service/                    # Business Logic
│   ├── ProductService.java
│   ├── AuthService.java
│   ├── CartService.java
│   ├── TransactionService.java
│   ├── ReceiptService.java
│   └── ReportService.java
├── payment/                    # Strategy Pattern
│   ├── PaymentMethod.java      # Interface
│   ├── CashPayment.java
│   ├── EWalletPayment.java
│   └── PaymentMethodFactory.java
├── controller/                 # Controllers
│   ├── PosController.java
│   └── LoginController.java
├── view/                       # JavaFX Views
│   ├── LoginView.java
│   ├── MainView.java
│   ├── ProductManagementView.java
│   ├── TransactionView.java
│   └── ReportView.java
├── exception/                  # Custom Exceptions
│   ├── ValidationException.java
│   ├── OutOfStockException.java
│   ├── AuthenticationException.java
│   ├── PaymentException.java
│   └── DataNotFoundException.java
└── util/                       # Utilities
    └── DatabaseConnection.java
```
