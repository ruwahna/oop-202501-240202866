# Traceability Matrix
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Requirement to Design Traceability

Matriks ini menghubungkan Functional Requirements ke komponen desain.

| FR ID | Requirement | Class/Component | Method/Feature |
|-------|-------------|-----------------|----------------|
| **FR-1.1** | Tampilkan daftar produk | ProductDAO, ProductService, PosController | findAll(), getAllProducts() |
| **FR-1.2** | Tambah produk baru | ProductDAO, ProductService, ProductManagementView | save(), addProduct() |
| **FR-1.3** | Ubah data produk | ProductDAO, ProductService, ProductManagementView | update(), updateProduct() |
| **FR-1.4** | Hapus produk | ProductDAO, ProductService, ProductManagementView | delete(), deleteProduct() |
| **FR-1.5** | Cari produk | ProductDAO, ProductService, TransactionView | search(), searchProducts() |
| **FR-2.1** | Tambah item ke keranjang | Cart, CartService, TransactionView | addItem(), addToCart() |
| **FR-2.2** | Ubah jumlah item | CartService, TransactionView | updateQuantity() |
| **FR-2.3** | Hapus item dari keranjang | Cart, CartService, TransactionView | removeItem(), removeFromCart() |
| **FR-2.4** | Hitung subtotal/pajak/total | CartService, TransactionService | calculateSubtotal(), previewCheckout() |
| **FR-2.5** | Validasi stok | CartService | addToCart() with stock check |
| **FR-3.1** | Pembayaran tunai | CashPayment, PaymentMethodFactory | processPayment() |
| **FR-3.2** | Pembayaran e-wallet | EWalletPayment, PaymentMethodFactory | processPayment() |
| **FR-3.3** | Hitung kembalian | PaymentMethod implementations | processPayment() returns change |
| **FR-3.4** | Extensible payment methods | PaymentMethod interface, Factory | Strategy Pattern |
| **FR-4.1** | Generate struk | ReceiptService, TransactionView | generateReceipt() |
| **FR-4.2** | Laporan harian | ReportService, ReportView | generateDailyReport() |
| **FR-4.3** | Laporan periode | ReportService, ReportView | generatePeriodReport() |
| **FR-5.1** | Login required | LoginView, LoginController, AuthService | authenticate(), login() |
| **FR-5.2** | Role-based access | User.Role, MainView | Role enum, tab visibility |
| **FR-5.3** | Kasir access | MainView | createMainContent() role check |
| **FR-5.4** | Admin full access | MainView | createMainContent() role check |

---

### 2. Requirement to Test Case Traceability

| FR ID | Requirement | Test Case IDs |
|-------|-------------|---------------|
| **FR-1.1** | Tampilkan daftar produk | TC-PROD-001 |
| **FR-1.2** | Tambah produk baru | TC-PROD-002, TC-PROD-003, TC-PROD-004 |
| **FR-1.3** | Ubah data produk | TC-PROD-005 |
| **FR-1.4** | Hapus produk | TC-PROD-006 |
| **FR-1.5** | Cari produk | TC-PROD-007 |
| **FR-2.1** | Tambah item ke keranjang | TC-CART-001, TC-CART-002 |
| **FR-2.2** | Ubah jumlah item | TC-CART-003 |
| **FR-2.3** | Hapus item dari keranjang | TC-CART-004, TC-CART-005 |
| **FR-2.4** | Hitung subtotal/pajak/total | TC-CART-006 |
| **FR-2.5** | Validasi stok | TC-CART-002 |
| **FR-3.1** | Pembayaran tunai | TC-PAY-001, TC-PAY-002, TC-PAY-003 |
| **FR-3.2** | Pembayaran e-wallet | TC-PAY-004 |
| **FR-3.3** | Hitung kembalian | TC-PAY-002 |
| **FR-4.1** | Generate struk | TC-TRX-002 |
| **FR-4.2** | Laporan harian | TC-RPT-001 |
| **FR-4.3** | Laporan periode | TC-RPT-002 |
| **FR-5.1** | Login required | TC-AUTH-001, TC-AUTH-002, TC-AUTH-003 |
| **FR-5.2** | Role-based access | TC-AUTH-005 |
| **FR-5.3** | Kasir access | TC-AUTH-005 |
| **FR-5.4** | Admin full access | TC-AUTH-005 |

---

### 3. Design to Implementation Traceability

| Design Component | Implementation File | Package |
|-----------------|---------------------|---------|
| Product Model | Product.java | com.upb.agripos.model |
| User Model | User.java | com.upb.agripos.model |
| Cart Model | Cart.java, CartItem.java | com.upb.agripos.model |
| Transaction Model | Transaction.java, TransactionItem.java | com.upb.agripos.model |
| CheckoutSummary DTO | CheckoutSummary.java | com.upb.agripos.model |
| ProductDAO Interface | ProductDAO.java | com.upb.agripos.dao |
| UserDAO Interface | UserDAO.java | com.upb.agripos.dao |
| TransactionDAO Interface | TransactionDAO.java | com.upb.agripos.dao |
| JDBC Product DAO | JdbcProductDAO.java | com.upb.agripos.dao |
| JDBC User DAO | JdbcUserDAO.java | com.upb.agripos.dao |
| JDBC Transaction DAO | JdbcTransactionDAO.java | com.upb.agripos.dao |
| Product Service | ProductService.java | com.upb.agripos.service |
| Auth Service | AuthService.java | com.upb.agripos.service |
| Cart Service | CartService.java | com.upb.agripos.service |
| Transaction Service | TransactionService.java | com.upb.agripos.service |
| Receipt Service | ReceiptService.java | com.upb.agripos.service |
| Report Service | ReportService.java | com.upb.agripos.service |
| PaymentMethod Interface | PaymentMethod.java | com.upb.agripos.payment |
| Cash Payment Strategy | CashPayment.java | com.upb.agripos.payment |
| EWallet Payment Strategy | EWalletPayment.java | com.upb.agripos.payment |
| Payment Factory | PaymentMethodFactory.java | com.upb.agripos.payment |
| POS Controller | PosController.java | com.upb.agripos.controller |
| Login Controller | LoginController.java | com.upb.agripos.controller |
| Login View | LoginView.java | com.upb.agripos.view |
| Main View | MainView.java | com.upb.agripos.view |
| Product Management View | ProductManagementView.java | com.upb.agripos.view |
| Transaction View | TransactionView.java | com.upb.agripos.view |
| Report View | ReportView.java | com.upb.agripos.view |
| Database Connection | DatabaseConnection.java | com.upb.agripos.util |
| Validation Exception | ValidationException.java | com.upb.agripos.exception |
| OutOfStock Exception | OutOfStockException.java | com.upb.agripos.exception |
| Auth Exception | AuthenticationException.java | com.upb.agripos.exception |
| Payment Exception | PaymentException.java | com.upb.agripos.exception |
| DataNotFound Exception | DataNotFoundException.java | com.upb.agripos.exception |

---

### 4. Test to Implementation Traceability

| Test Class | Target Class | Test Coverage |
|------------|--------------|---------------|
| ProductServiceTest | ProductService | getAllProducts, getProductByCode, addProduct, updateProduct, deleteProduct |
| CartServiceTest | CartService | addToCart, removeFromCart, updateQuantity, calculateSubtotal, clearCart |
| PaymentMethodTest | CashPayment, EWalletPayment, PaymentMethodFactory | processPayment, validatePayment, getMethodName |

---

### 5. Use Case to Implementation Traceability

| Use Case | Primary Actor | Implementation Classes |
|----------|--------------|------------------------|
| UC-1: Login | Kasir, Admin | LoginView, LoginController, AuthService, UserDAO |
| UC-2: View Products | Kasir, Admin | TransactionView, ProductManagementView, PosController, ProductService |
| UC-3: Add to Cart | Kasir | TransactionView, PosController, CartService |
| UC-4: Checkout | Kasir | TransactionView, PosController, TransactionService |
| UC-5: Process Payment | Kasir | PaymentMethod, CashPayment, EWalletPayment, PaymentMethodFactory |
| UC-6: Print Receipt | Kasir | ReceiptService, TransactionView |
| UC-7: Manage Product | Admin | ProductManagementView, PosController, ProductService, ProductDAO |
| UC-8: View Reports | Admin | ReportView, PosController, ReportService, TransactionDAO |
| UC-9: Logout | Kasir, Admin | MainView, AuthService |

---

### 6. SOLID Principles Traceability

| Principle | Implementation Evidence |
|-----------|------------------------|
| **S** - Single Responsibility | Setiap service memiliki satu tanggung jawab: ProductService (produk), AuthService (auth), CartService (keranjang), dll. |
| **O** - Open/Closed | PaymentMethod interface memungkinkan penambahan metode pembayaran baru tanpa mengubah kode existing |
| **L** - Liskov Substitution | CashPayment dan EWalletPayment dapat menggantikan PaymentMethod tanpa masalah |
| **I** - Interface Segregation | DAO interfaces dipisah per entity: ProductDAO, UserDAO, TransactionDAO |
| **D** - Dependency Inversion | Service layer bergantung pada interface DAO, bukan implementasi konkret. Controller bergantung pada Service layer |

---

### 7. Coverage Summary Matrix

```
┌───────────────────────────────────────────────────────────────┐
│                    COVERAGE MATRIX                             │
├─────────────────┬───────────┬───────────┬───────────┬─────────┤
│ Requirement     │ Design    │ Implement │ Test      │ Status  │
├─────────────────┼───────────┼───────────┼───────────┼─────────┤
│ FR-1 Product    │    ✅     │    ✅     │    ✅     │ Complete│
│ FR-2 Transaction│    ✅     │    ✅     │    ✅     │ Complete│
│ FR-3 Payment    │    ✅     │    ✅     │    ✅     │ Complete│
│ FR-4 Report     │    ✅     │    ✅     │    ✅     │ Complete│
│ FR-5 Auth       │    ✅     │    ✅     │    ✅     │ Complete│
├─────────────────┼───────────┼───────────┼───────────┼─────────┤
│ NFR Performance │    ✅     │    ✅     │    -      │ Partial │
│ NFR Security    │    ✅     │    ✅     │    -      │ Partial │
│ NFR Usability   │    ✅     │    ✅     │    -      │ Manual  │
│ NFR Maintain    │    ✅     │    ✅     │    -      │ By Design│
└─────────────────┴───────────┴───────────┴───────────┴─────────┘
```
