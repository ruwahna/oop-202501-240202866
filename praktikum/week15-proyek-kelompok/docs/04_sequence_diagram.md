# Sequence Diagrams
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Sequence Diagram: Login

```
┌──────┐     ┌───────────┐     ┌─────────────────┐     ┌─────────────┐     ┌─────────┐
│ User │     │ LoginView │     │ LoginController │     │ AuthService │     │ UserDAO │
└──┬───┘     └─────┬─────┘     └────────┬────────┘     └──────┬──────┘     └────┬────┘
   │               │                    │                     │                 │
   │ Input username/password            │                     │                 │
   │──────────────>│                    │                     │                 │
   │               │                    │                     │                 │
   │               │ authenticate(u,p)  │                     │                 │
   │               │───────────────────>│                     │                 │
   │               │                    │                     │                 │
   │               │                    │ login(username, password)             │
   │               │                    │────────────────────>│                 │
   │               │                    │                     │                 │
   │               │                    │                     │ findByUsername()│
   │               │                    │                     │────────────────>│
   │               │                    │                     │                 │
   │               │                    │                     │ Optional<User>  │
   │               │                    │                     │<────────────────│
   │               │                    │                     │                 │
   │               │                    │                     │ validate password
   │               │                    │                     │ set currentUser │
   │               │                    │                     │                 │
   │               │                    │ User                │                 │
   │               │                    │<────────────────────│                 │
   │               │                    │                     │                 │
   │               │ User               │                     │                 │
   │               │<───────────────────│                     │                 │
   │               │                    │                     │                 │
   │               │ onLoginSuccess()   │                     │                 │
   │               │ show MainView      │                     │                 │
   │               │                    │                     │                 │
   │ MainView displayed                 │                     │                 │
   │<──────────────│                    │                     │                 │
   │               │                    │                     │                 │
```

### 2. Sequence Diagram: Add Product to Cart

```
┌──────┐     ┌─────────────────┐     ┌───────────────┐     ┌─────────────┐
│ User │     │ TransactionView │     │ PosController │     │ CartService │
└──┬───┘     └────────┬────────┘     └───────┬───────┘     └──────┬──────┘
   │                  │                      │                    │
   │ Select product   │                      │                    │
   │ Enter quantity   │                      │                    │
   │ Click Add        │                      │                    │
   │─────────────────>│                      │                    │
   │                  │                      │                    │
   │                  │ addToCart(product, qty)                   │
   │                  │─────────────────────>│                    │
   │                  │                      │                    │
   │                  │                      │ addToCart(product, qty)
   │                  │                      │───────────────────>│
   │                  │                      │                    │
   │                  │                      │                    │ check stock
   │                  │                      │                    │ if (qty > stock)
   │                  │                      │                    │   throw OutOfStock
   │                  │                      │                    │
   │                  │                      │                    │ add/update CartItem
   │                  │                      │                    │
   │                  │                      │ void               │
   │                  │                      │<───────────────────│
   │                  │                      │                    │
   │                  │ update cartItems list│                    │
   │                  │<─────────────────────│                    │
   │                  │                      │                    │
   │ Cart updated     │                      │                    │
   │<─────────────────│                      │                    │
   │                  │                      │                    │
```

### 3. Sequence Diagram: Checkout Transaction

```
┌──────┐     ┌─────────────────┐     ┌───────────────┐     ┌───────────────────┐     ┌────────────────────┐     ┌───────────────┐
│ User │     │ TransactionView │     │ PosController │     │ TransactionService│     │ PaymentMethodFactory│    │ TransactionDAO│
└──┬───┘     └────────┬────────┘     └───────┬───────┘     └─────────┬─────────┘     └──────────┬─────────┘    └───────┬───────┘
   │                  │                      │                       │                          │                      │
   │ Select payment   │                      │                       │                          │                      │
   │ Enter amount     │                      │                       │                          │                      │
   │ Click Checkout   │                      │                       │                          │                      │
   │─────────────────>│                      │                       │                          │                      │
   │                  │                      │                       │                          │                      │
   │                  │ processCheckout(method, amount)              │                          │                      │
   │                  │─────────────────────>│                       │                          │                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │ processCheckout(userId, method, amount)          │                      │
   │                  │                      │──────────────────────>│                          │                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ getPaymentMethod(method) │                      │
   │                  │                      │                       │─────────────────────────>│                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ PaymentMethod            │                      │
   │                  │                      │                       │<─────────────────────────│                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ validatePayment()        │                      │
   │                  │                      │                       │ processPayment()         │                      │
   │                  │                      │                       │ calculate change         │                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ create Transaction       │                      │
   │                  │                      │                       │ with TransactionItems    │                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ save(transaction)        │                      │
   │                  │                      │                       │─────────────────────────────────────────────────>│
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ boolean                  │                      │
   │                  │                      │                       │<─────────────────────────────────────────────────│
   │                  │                      │                       │                          │                      │
   │                  │                      │                       │ reduce product stock     │                      │
   │                  │                      │                       │ clear cart               │                      │
   │                  │                      │                       │                          │                      │
   │                  │                      │ CheckoutSummary       │                          │                      │
   │                  │                      │<──────────────────────│                          │                      │
   │                  │                      │                       │                          │                      │
   │                  │ CheckoutSummary      │                       │                          │                      │
   │                  │<─────────────────────│                       │                          │                      │
   │                  │                      │                       │                          │                      │
   │                  │ generateReceipt()    │                       │                          │                      │
   │                  │ show success message │                       │                          │                      │
   │                  │                      │                       │                          │                      │
   │ Receipt displayed│                      │                       │                          │                      │
   │<─────────────────│                      │                       │                          │                      │
```

### 4. Sequence Diagram: Add New Product (Admin)

```
┌───────┐     ┌───────────────────────┐     ┌───────────────┐     ┌────────────────┐     ┌────────────┐
│ Admin │     │ ProductManagementView │     │ PosController │     │ ProductService │     │ ProductDAO │
└───┬───┘     └───────────┬───────────┘     └───────┬───────┘     └────────┬───────┘     └──────┬─────┘
    │                     │                         │                      │                    │
    │ Fill product form   │                         │                      │                    │
    │ Click Save          │                         │                      │                    │
    │────────────────────>│                         │                      │                    │
    │                     │                         │                      │                    │
    │                     │ addProduct(product)     │                      │                    │
    │                     │────────────────────────>│                      │                    │
    │                     │                         │                      │                    │
    │                     │                         │ addProduct(product)  │                    │
    │                     │                         │─────────────────────>│                    │
    │                     │                         │                      │                    │
    │                     │                         │                      │ validate product   │
    │                     │                         │                      │ check required fields
    │                     │                         │                      │ check price >= 0   │
    │                     │                         │                      │ check stock >= 0   │
    │                     │                         │                      │                    │
    │                     │                         │                      │ save(product)      │
    │                     │                         │                      │───────────────────>│
    │                     │                         │                      │                    │
    │                     │                         │                      │ boolean            │
    │                     │                         │                      │<───────────────────│
    │                     │                         │                      │                    │
    │                     │                         │ void                 │                    │
    │                     │                         │<─────────────────────│                    │
    │                     │                         │                      │                    │
    │                     │                         │ refresh productList  │                    │
    │                     │                         │                      │                    │
    │                     │ success / update table  │                      │                    │
    │                     │<────────────────────────│                      │                    │
    │                     │                         │                      │                    │
    │ Success message     │                         │                      │                    │
    │<────────────────────│                         │                      │                    │
```

### 5. Sequence Diagram: Generate Daily Report

```
┌───────┐     ┌────────────┐     ┌───────────────┐     ┌───────────────┐     ┌────────────────┐
│ Admin │     │ ReportView │     │ PosController │     │ ReportService │     │ TransactionDAO │
└───┬───┘     └─────┬──────┘     └───────┬───────┘     └───────┬───────┘     └────────┬───────┘
    │               │                    │                     │                      │
    │ Select date   │                    │                     │                      │
    │ Click Generate│                    │                     │                      │
    │──────────────>│                    │                     │                      │
    │               │                    │                     │                      │
    │               │ generateDailyReport(date)                │                      │
    │               │───────────────────>│                     │                      │
    │               │                    │                     │                      │
    │               │                    │ generateDailyReport(date)                  │
    │               │                    │────────────────────>│                      │
    │               │                    │                     │                      │
    │               │                    │                     │ findByDate(date)     │
    │               │                    │                     │─────────────────────>│
    │               │                    │                     │                      │
    │               │                    │                     │ List<Transaction>    │
    │               │                    │                     │<─────────────────────│
    │               │                    │                     │                      │
    │               │                    │                     │ calculate totals     │
    │               │                    │                     │ format report        │
    │               │                    │                     │                      │
    │               │                    │ String (report)     │                      │
    │               │                    │<────────────────────│                      │
    │               │                    │                     │                      │
    │               │ String (report)    │                     │                      │
    │               │<───────────────────│                     │                      │
    │               │                    │                     │                      │
    │ Report shown  │                    │                     │                      │
    │<──────────────│                    │                     │                      │
```

### 6. Sequence Diagram: Application Startup (Dependency Injection)

```
┌────────────┐     ┌────────────────────┐     ┌─────────────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│ AppJavaFx  │     │ DatabaseConnection │     │ DAO Layer       │     │ Services │     │Controllers│    │  Views   │
└─────┬──────┘     └──────────┬─────────┘     └────────┬────────┘     └────┬─────┘     └────┬─────┘    └────┬─────┘
      │                       │                        │                   │                │               │
      │ start()               │                        │                   │                │               │
      │                       │                        │                   │                │               │
      │ getInstance()         │                        │                   │                │               │
      │──────────────────────>│                        │                   │                │               │
      │                       │                        │                   │                │               │
      │ connection            │                        │                   │                │               │
      │<──────────────────────│                        │                   │                │               │
      │                       │                        │                   │                │               │
      │ new JdbcProductDAO(conn)                       │                   │                │               │
      │ new JdbcUserDAO(conn)                          │                   │                │               │
      │ new JdbcTransactionDAO(conn)                   │                   │                │               │
      │───────────────────────────────────────────────>│                   │                │               │
      │                       │                        │                   │                │               │
      │ registerPaymentMethods()                       │                   │                │               │
      │                       │                        │                   │                │               │
      │ new ProductService(productDAO)                 │                   │                │               │
      │ new AuthService(userDAO)                       │                   │                │               │
      │ new CartService()                              │                   │                │               │
      │ new TransactionService(transDAO, cartSvc)      │                   │                │               │
      │──────────────────────────────────────────────────────────────────>│                │               │
      │                       │                        │                   │                │               │
      │ new PosController(services...)                 │                   │                │               │
      │ new LoginController(authService)               │                   │                │               │
      │──────────────────────────────────────────────────────────────────────────────────>│               │
      │                       │                        │                   │                │               │
      │ new LoginView(loginController, callback)       │                   │                │               │
      │ show LoginView                                 │                   │                │               │
      │─────────────────────────────────────────────────────────────────────────────────────────────────>│
      │                       │                        │                   │                │               │
```
