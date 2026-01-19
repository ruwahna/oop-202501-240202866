# Class Diagram
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Class Diagram Utama

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                               MODEL LAYER                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│  ┌─────────────────────┐      ┌─────────────────────┐                           │
│  │       Product       │      │        User         │                           │
│  ├─────────────────────┤      ├─────────────────────┤                           │
│  │ - code: String      │      │ - id: int           │                           │
│  │ - name: String      │      │ - username: String  │                           │
│  │ - category: String  │      │ - password: String  │                           │
│  │ - price: double     │      │ - role: Role        │                           │
│  │ - stock: int        │      ├─────────────────────┤                           │
│  ├─────────────────────┤      │ + getters/setters   │                           │
│  │ + getters/setters   │      │ + isAdmin(): boolean│                           │
│  │ + reduceStock()     │      └─────────────────────┘                           │
│  │ + addStock()        │                                                        │
│  └─────────────────────┘      ┌─────────────────────┐                           │
│           △                   │     <<enum>>        │                           │
│           │                   │       Role          │                           │
│  ┌────────┴────────┐          ├─────────────────────┤                           │
│  │    CartItem     │          │ KASIR               │                           │
│  ├─────────────────┤          │ ADMIN               │                           │
│  │ - product: Prod │          └─────────────────────┘                           │
│  │ - quantity: int │                                                            │
│  ├─────────────────┤                                                            │
│  │ + getSubtotal() │                                                            │
│  └─────────────────┘                                                            │
│           △                                                                      │
│           │                                                                      │
│  ┌────────┴────────┐                                                            │
│  │      Cart       │                                                            │
│  ├─────────────────┤                                                            │
│  │ - items: Map    │                                                            │
│  ├─────────────────┤                                                            │
│  │ + addItem()     │                                                            │
│  │ + removeItem()  │                                                            │
│  │ + getSubtotal() │                                                            │
│  │ + clear()       │                                                            │
│  └─────────────────┘                                                            │
│                                                                                  │
│  ┌─────────────────────┐      ┌─────────────────────┐                           │
│  │    Transaction      │◇─────│  TransactionItem    │                           │
│  ├─────────────────────┤      ├─────────────────────┤                           │
│  │ - id: int           │      │ - productCode: Str  │                           │
│  │ - transactionCode   │      │ - productName: Str  │                           │
│  │ - userId: int       │      │ - unitPrice: double │                           │
│  │ - items: List       │      │ - quantity: int     │                           │
│  │ - subtotal: double  │      │ - subtotal: double  │                           │
│  │ - tax: double       │      └─────────────────────┘                           │
│  │ - total: double     │                                                        │
│  │ - paymentMethod     │      ┌─────────────────────┐                           │
│  │ - status: Status    │      │  CheckoutSummary    │                           │
│  │ - transactionDate   │      ├─────────────────────┤                           │
│  └─────────────────────┘      │ - subtotal: double  │                           │
│                               │ - tax: double       │                           │
│  ┌─────────────────────┐      │ - total: double     │                           │
│  │     <<enum>>        │      │ - paymentMethod     │                           │
│  │ TransactionStatus   │      │ - amountPaid        │                           │
│  ├─────────────────────┤      │ - changeAmount      │                           │
│  │ PENDING             │      └─────────────────────┘                           │
│  │ COMPLETED           │                                                        │
│  │ CANCELLED           │                                                        │
│  └─────────────────────┘                                                        │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                               DAO LAYER                                          │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│  ┌───────────────────────────┐    ┌───────────────────────────┐                 │
│  │    <<interface>>          │    │    <<interface>>          │                 │
│  │      ProductDAO           │    │       UserDAO             │                 │
│  ├───────────────────────────┤    ├───────────────────────────┤                 │
│  │ + findAll(): List<Prod>   │    │ + findByUsername(): Opt   │                 │
│  │ + findByCode(): Optional  │    │ + findById(): Optional    │                 │
│  │ + findByCategory(): List  │    │ + save(): boolean         │                 │
│  │ + search(): List          │    │ + update(): boolean       │                 │
│  │ + save(): boolean         │    │ + delete(): boolean       │                 │
│  │ + update(): boolean       │    └──────────────┬────────────┘                 │
│  │ + delete(): boolean       │                   │                              │
│  └──────────────┬────────────┘                   │                              │
│                 │                                │                              │
│                 ▼                                ▼                              │
│  ┌───────────────────────────┐    ┌───────────────────────────┐                 │
│  │    JdbcProductDAO         │    │     JdbcUserDAO           │                 │
│  ├───────────────────────────┤    ├───────────────────────────┤                 │
│  │ - connection: Connection  │    │ - connection: Connection  │                 │
│  ├───────────────────────────┤    ├───────────────────────────┤                 │
│  │ + implements ProductDAO   │    │ + implements UserDAO      │                 │
│  └───────────────────────────┘    └───────────────────────────┘                 │
│                                                                                  │
│  ┌───────────────────────────┐                                                  │
│  │    <<interface>>          │                                                  │
│  │    TransactionDAO         │                                                  │
│  ├───────────────────────────┤                                                  │
│  │ + save(): boolean         │                                                  │
│  │ + findByCode(): Optional  │                                                  │
│  │ + findByDate(): List      │                                                  │
│  │ + findByDateRange(): List │                                                  │
│  │ + findByUserId(): List    │                                                  │
│  └──────────────┬────────────┘                                                  │
│                 │                                                               │
│                 ▼                                                               │
│  ┌───────────────────────────┐                                                  │
│  │  JdbcTransactionDAO       │                                                  │
│  ├───────────────────────────┤                                                  │
│  │ - connection: Connection  │                                                  │
│  ├───────────────────────────┤                                                  │
│  │ + implements Transaction  │                                                  │
│  └───────────────────────────┘                                                  │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                            SERVICE LAYER                                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│  ┌───────────────────────────┐    ┌───────────────────────────┐                 │
│  │     ProductService        │    │      AuthService          │                 │
│  ├───────────────────────────┤    ├───────────────────────────┤                 │
│  │ - productDAO: ProductDAO  │    │ - userDAO: UserDAO        │                 │
│  ├───────────────────────────┤    │ - currentUser: User       │                 │
│  │ + getAllProducts()        │    ├───────────────────────────┤                 │
│  │ + getProductByCode()      │    │ + login(): User           │                 │
│  │ + addProduct()            │    │ + logout(): void          │                 │
│  │ + updateProduct()         │    │ + isLoggedIn(): boolean   │                 │
│  │ + deleteProduct()         │    │ + getCurrentUser(): User  │                 │
│  │ + searchProducts()        │    │ + isAdmin(): boolean      │                 │
│  └───────────────────────────┘    └───────────────────────────┘                 │
│                                                                                  │
│  ┌───────────────────────────┐    ┌───────────────────────────┐                 │
│  │      CartService          │    │   TransactionService      │                 │
│  ├───────────────────────────┤    ├───────────────────────────┤                 │
│  │ - cart: Cart              │    │ - transactionDAO          │                 │
│  ├───────────────────────────┤    │ - cartService             │                 │
│  │ + addToCart()             │    ├───────────────────────────┤                 │
│  │ + removeFromCart()        │    │ + processCheckout()       │                 │
│  │ + updateQuantity()        │    │ + previewCheckout()       │                 │
│  │ + clearCart()             │    │ + generateTransCode()     │                 │
│  │ + calculateSubtotal()     │    │ + getByCode()             │                 │
│  │ + isCartEmpty()           │    └───────────────────────────┘                 │
│  └───────────────────────────┘                                                  │
│                                                                                  │
│  ┌───────────────────────────┐    ┌───────────────────────────┐                 │
│  │     ReceiptService        │    │     ReportService         │                 │
│  ├───────────────────────────┤    ├───────────────────────────┤                 │
│  │ + generateReceipt()       │    │ - transactionDAO          │                 │
│  │ + formatCurrency()        │    ├───────────────────────────┤                 │
│  │ + generatePreview()       │    │ + generateDailyReport()   │                 │
│  └───────────────────────────┘    │ + generatePeriodReport()  │                 │
│                                   └───────────────────────────┘                 │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                         PAYMENT STRATEGY PATTERN                                 │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│                    ┌─────────────────────────────────────┐                       │
│                    │        <<interface>>                │                       │
│                    │        PaymentMethod                │                       │
│                    ├─────────────────────────────────────┤                       │
│                    │ + getMethodName(): String           │                       │
│                    │ + processPayment(total, paid): dbl  │                       │
│                    │ + validatePayment(total, paid): bool│                       │
│                    │ + getReceiptDescription(): String   │                       │
│                    └───────────────┬─────────────────────┘                       │
│                                    │                                             │
│                    ┌───────────────┼───────────────┐                             │
│                    │               │               │                             │
│                    ▼               ▼               ▼                             │
│  ┌─────────────────────┐ ┌─────────────────┐ ┌─────────────────┐                │
│  │    CashPayment      │ │  EWalletPayment │ │  <<future>>     │                │
│  ├─────────────────────┤ ├─────────────────┤ │  CardPayment    │                │
│  │ + getMethodName()   │ │ + getMethodName │ └─────────────────┘                │
│  │ + processPayment()  │ │ + processPayment│                                    │
│  │ + validatePayment() │ │ + validatePay...│                                    │
│  │ + getReceiptDesc()  │ │ + getReceiptDsc │                                    │
│  └─────────────────────┘ └─────────────────┘                                    │
│                                                                                  │
│  ┌─────────────────────────────────────────┐                                    │
│  │       PaymentMethodFactory              │                                    │
│  ├─────────────────────────────────────────┤                                    │
│  │ - methods: Map<String, PaymentMethod>   │                                    │
│  ├─────────────────────────────────────────┤                                    │
│  │ + registerPaymentMethod(name, impl)     │                                    │
│  │ + getPaymentMethod(name): PaymentMethod │                                    │
│  │ + getAvailableMethods(): Set<String>    │                                    │
│  └─────────────────────────────────────────┘                                    │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                           UTILITY & EXCEPTION                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│  ┌─────────────────────────────────────────┐                                    │
│  │      DatabaseConnection <<Singleton>>   │                                    │
│  ├─────────────────────────────────────────┤                                    │
│  │ - instance: DatabaseConnection          │                                    │
│  │ - connection: Connection                │                                    │
│  ├─────────────────────────────────────────┤                                    │
│  │ - DatabaseConnection()                  │                                    │
│  │ + getInstance(): DatabaseConnection     │                                    │
│  │ + getConnection(): Connection           │                                    │
│  │ + closeConnection(): void               │                                    │
│  └─────────────────────────────────────────┘                                    │
│                                                                                  │
│       Exception Classes:                                                         │
│  ┌─────────────────────┐ ┌─────────────────────┐ ┌─────────────────────┐        │
│  │ ValidationException │ │ OutOfStockException │ │AuthenticationExcept │        │
│  └─────────────────────┘ └─────────────────────┘ └─────────────────────┘        │
│  ┌─────────────────────┐ ┌─────────────────────┐                                │
│  │  PaymentException   │ │DataNotFoundException│                                │
│  └─────────────────────┘ └─────────────────────┘                                │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 2. Relasi Antar Class

#### 2.1 Association
- `Cart` ◇─── `CartItem` (Composition)
- `Transaction` ◇─── `TransactionItem` (Composition)
- `CartItem` ───> `Product` (Association)

#### 2.2 Dependency
- `JdbcProductDAO` ───> `Connection`
- `ProductService` ───> `ProductDAO`
- `PosController` ───> Multiple Services

#### 2.3 Realization (Interface Implementation)
- `JdbcProductDAO` ──▷ `ProductDAO`
- `JdbcUserDAO` ──▷ `UserDAO`
- `JdbcTransactionDAO` ──▷ `TransactionDAO`
- `CashPayment` ──▷ `PaymentMethod`
- `EWalletPayment` ──▷ `PaymentMethod`

### 3. Multiplicity

| Relasi | Multiplicity |
|--------|--------------|
| Cart - CartItem | 1 .. * |
| Transaction - TransactionItem | 1 .. * |
| User - Transaction | 1 .. * |
| Product - CartItem | 1 .. * |
