# Test Cases
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Test Cases Overview

| Test Suite | Total Cases | Coverage Area |
|------------|-------------|---------------|
| Authentication | 5 | Login, Logout, Role-based access |
| Product Management | 8 | CRUD operations |
| Cart Operations | 6 | Add, Remove, Update cart |
| Payment Processing | 5 | Payment methods, validation |
| Transaction | 4 | Checkout, Receipt |
| Reports | 3 | Daily, Period reports |

---

### 2. Authentication Test Cases

#### TC-AUTH-001: Login dengan kredensial valid

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-AUTH-001 |
| **Description** | Verifikasi login berhasil dengan username dan password valid |
| **Precondition** | User "admin" dengan password "admin123" ada di database |
| **Test Steps** | 1. Buka aplikasi<br>2. Input username: "admin"<br>3. Input password: "admin123"<br>4. Klik tombol Login |
| **Expected Result** | User berhasil login, MainView ditampilkan dengan akses Admin |
| **Status** | ✅ Pass |

#### TC-AUTH-002: Login dengan kredensial tidak valid

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-AUTH-002 |
| **Description** | Verifikasi login gagal dengan password salah |
| **Precondition** | User "admin" ada di database |
| **Test Steps** | 1. Buka aplikasi<br>2. Input username: "admin"<br>3. Input password: "wrongpassword"<br>4. Klik tombol Login |
| **Expected Result** | Login gagal, pesan error "Username atau password salah" ditampilkan |
| **Status** | ✅ Pass |

#### TC-AUTH-003: Login dengan username tidak terdaftar

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-AUTH-003 |
| **Description** | Verifikasi login gagal dengan username tidak terdaftar |
| **Test Steps** | 1. Buka aplikasi<br>2. Input username: "unknownuser"<br>3. Input password: "anypassword"<br>4. Klik tombol Login |
| **Expected Result** | Login gagal, pesan error ditampilkan |
| **Status** | ✅ Pass |

#### TC-AUTH-004: Logout

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-AUTH-004 |
| **Description** | Verifikasi logout berhasil |
| **Precondition** | User sudah login |
| **Test Steps** | 1. Klik tombol Logout<br>2. Konfirmasi logout |
| **Expected Result** | User keluar, LoginView ditampilkan kembali |
| **Status** | ✅ Pass |

#### TC-AUTH-005: Role-based tab visibility

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-AUTH-005 |
| **Description** | Verifikasi kasir hanya melihat tab Transaction |
| **Precondition** | Login sebagai Kasir |
| **Test Steps** | 1. Login sebagai "kasir1"<br>2. Observasi tabs yang tersedia |
| **Expected Result** | Hanya tab Transaksi yang tersedia, tab Product Management dan Report tidak visible |
| **Status** | ✅ Pass |

---

### 3. Product Management Test Cases

#### TC-PROD-001: Menampilkan daftar produk

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-001 |
| **Description** | Verifikasi daftar produk ditampilkan dengan benar |
| **Precondition** | Database memiliki data produk |
| **Test Steps** | 1. Login sebagai Admin<br>2. Buka tab Product Management |
| **Expected Result** | Tabel produk menampilkan semua produk dengan kolom: Kode, Nama, Kategori, Harga, Stok |
| **Status** | ✅ Pass |

#### TC-PROD-002: Tambah produk valid

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-002 |
| **Description** | Verifikasi penambahan produk dengan data valid |
| **Test Steps** | 1. Isi form: Kode="TEST01", Nama="Test Product", Kategori="Test", Harga=10000, Stok=50<br>2. Klik Save |
| **Expected Result** | Produk tersimpan, muncul di tabel, pesan sukses ditampilkan |
| **Status** | ✅ Pass |

#### TC-PROD-003: Tambah produk - kode kosong

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-003 |
| **Description** | Verifikasi validasi kode produk tidak boleh kosong |
| **Test Steps** | 1. Isi form dengan kode kosong<br>2. Klik Save |
| **Expected Result** | Validasi gagal, pesan error "Kode produk harus diisi" |
| **Status** | ✅ Pass |

#### TC-PROD-004: Tambah produk - harga negatif

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-004 |
| **Description** | Verifikasi validasi harga tidak boleh negatif |
| **Test Steps** | 1. Isi form dengan harga=-5000<br>2. Klik Save |
| **Expected Result** | Validasi gagal, pesan error "Harga tidak boleh negatif" |
| **Status** | ✅ Pass |

#### TC-PROD-005: Update produk

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-005 |
| **Description** | Verifikasi update data produk |
| **Test Steps** | 1. Pilih produk di tabel<br>2. Ubah nama menjadi "Updated Name"<br>3. Klik Update |
| **Expected Result** | Produk terupdate di database dan tabel |
| **Status** | ✅ Pass |

#### TC-PROD-006: Hapus produk

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-006 |
| **Description** | Verifikasi penghapusan produk |
| **Test Steps** | 1. Pilih produk di tabel<br>2. Klik Delete<br>3. Konfirmasi |
| **Expected Result** | Produk terhapus dari database dan tabel |
| **Status** | ✅ Pass |

#### TC-PROD-007: Pencarian produk

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PROD-007 |
| **Description** | Verifikasi pencarian produk |
| **Test Steps** | 1. Input "beras" di field pencarian |
| **Expected Result** | Tabel hanya menampilkan produk yang mengandung "beras" |
| **Status** | ✅ Pass |

---

### 4. Cart Operations Test Cases

#### TC-CART-001: Tambah item ke keranjang

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-CART-001 |
| **Description** | Verifikasi penambahan item ke keranjang |
| **Test Steps** | 1. Pilih produk "Beras Premium"<br>2. Input qty=2<br>3. Klik Tambah ke Keranjang |
| **Expected Result** | Item muncul di keranjang dengan qty=2 |
| **Status** | ✅ Pass |

#### TC-CART-002: Tambah item - stok tidak cukup

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-CART-002 |
| **Description** | Verifikasi validasi stok saat menambah ke keranjang |
| **Test Steps** | 1. Pilih produk dengan stok=10<br>2. Input qty=100<br>3. Klik Tambah |
| **Expected Result** | Error "Stok tidak mencukupi" ditampilkan |
| **Status** | ✅ Pass |

#### TC-CART-003: Update quantity item

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-CART-003 |
| **Description** | Verifikasi update quantity item di keranjang |
| **Test Steps** | 1. Tambah item dengan qty=2<br>2. Tambah lagi item yang sama dengan qty=3 |
| **Expected Result** | Item di keranjang memiliki qty=5 |
| **Status** | ✅ Pass |

#### TC-CART-004: Hapus item dari keranjang

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-CART-004 |
| **Description** | Verifikasi penghapusan item dari keranjang |
| **Test Steps** | 1. Tambah item ke keranjang<br>2. Klik tombol hapus pada item |
| **Expected Result** | Item dihapus dari keranjang |
| **Status** | ✅ Pass |

#### TC-CART-005: Kosongkan keranjang

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-CART-005 |
| **Description** | Verifikasi mengosongkan keranjang |
| **Test Steps** | 1. Tambah beberapa item<br>2. Klik "Kosongkan Keranjang" |
| **Expected Result** | Semua item dihapus dari keranjang |
| **Status** | ✅ Pass |

#### TC-CART-006: Perhitungan subtotal

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-CART-006 |
| **Description** | Verifikasi perhitungan subtotal keranjang |
| **Test Steps** | 1. Tambah Beras (Rp 65.000) qty=2<br>2. Tambah Gula (Rp 14.000) qty=3 |
| **Expected Result** | Subtotal = (65.000*2) + (14.000*3) = Rp 172.000 |
| **Status** | ✅ Pass |

---

### 5. Payment Test Cases

#### TC-PAY-001: Pembayaran tunai berhasil

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PAY-001 |
| **Description** | Verifikasi pembayaran tunai dengan uang pas |
| **Test Steps** | 1. Total = Rp 110.000<br>2. Pilih metode CASH<br>3. Input Rp 110.000<br>4. Klik Bayar |
| **Expected Result** | Pembayaran berhasil, kembalian = Rp 0 |
| **Status** | ✅ Pass |

#### TC-PAY-002: Pembayaran tunai dengan kembalian

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PAY-002 |
| **Description** | Verifikasi perhitungan kembalian |
| **Test Steps** | 1. Total = Rp 110.000<br>2. Pilih metode CASH<br>3. Input Rp 150.000<br>4. Klik Bayar |
| **Expected Result** | Pembayaran berhasil, kembalian = Rp 40.000 |
| **Status** | ✅ Pass |

#### TC-PAY-003: Pembayaran tunai - uang kurang

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PAY-003 |
| **Description** | Verifikasi validasi uang tidak cukup |
| **Test Steps** | 1. Total = Rp 110.000<br>2. Pilih metode CASH<br>3. Input Rp 50.000<br>4. Klik Bayar |
| **Expected Result** | Error "Pembayaran tidak mencukupi" |
| **Status** | ✅ Pass |

#### TC-PAY-004: Pembayaran E-Wallet

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-PAY-004 |
| **Description** | Verifikasi pembayaran dengan E-Wallet |
| **Test Steps** | 1. Total = Rp 110.000<br>2. Pilih metode E-WALLET<br>3. Input Rp 110.000<br>4. Klik Bayar |
| **Expected Result** | Pembayaran berhasil diproses |
| **Status** | ✅ Pass |

---

### 6. Transaction Test Cases

#### TC-TRX-001: Checkout lengkap

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-TRX-001 |
| **Description** | Verifikasi proses checkout end-to-end |
| **Test Steps** | 1. Tambah items ke keranjang<br>2. Pilih metode pembayaran<br>3. Input jumlah bayar<br>4. Klik Bayar |
| **Expected Result** | Transaksi tersimpan, stok berkurang, struk ditampilkan, keranjang kosong |
| **Status** | ✅ Pass |

#### TC-TRX-002: Struk transaksi

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-TRX-002 |
| **Description** | Verifikasi format struk |
| **Test Steps** | 1. Selesaikan transaksi<br>2. Observasi struk |
| **Expected Result** | Struk menampilkan: header, items, subtotal, pajak, total, metode bayar, kembalian |
| **Status** | ✅ Pass |

---

### 7. Report Test Cases

#### TC-RPT-001: Laporan harian

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-RPT-001 |
| **Description** | Verifikasi generate laporan harian |
| **Test Steps** | 1. Pilih "Laporan Harian"<br>2. Pilih tanggal<br>3. Klik Generate |
| **Expected Result** | Laporan menampilkan total transaksi dan pendapatan hari tersebut |
| **Status** | ✅ Pass |

#### TC-RPT-002: Laporan periode

| Attribute | Value |
|-----------|-------|
| **Test Case ID** | TC-RPT-002 |
| **Description** | Verifikasi generate laporan periode |
| **Test Steps** | 1. Pilih "Laporan Periode"<br>2. Pilih tanggal mulai dan akhir<br>3. Klik Generate |
| **Expected Result** | Laporan menampilkan ringkasan transaksi dalam periode |
| **Status** | ✅ Pass |

---

### 8. Unit Test Summary

```
ProductServiceTest
├── getAllProducts Tests
│   ├── shouldReturnAllProducts ✅
│   └── shouldReturnEmptyListWhenNoProducts ✅
├── getProductByCode Tests
│   ├── shouldReturnProductWhenFound ✅
│   └── shouldThrowExceptionWhenNotFound ✅
├── addProduct Tests
│   ├── shouldAddValidProduct ✅
│   ├── shouldThrowExceptionWhenCodeEmpty ✅
│   ├── shouldThrowExceptionWhenNameEmpty ✅
│   ├── shouldThrowExceptionWhenPriceNegative ✅
│   └── shouldThrowExceptionWhenStockNegative ✅
├── updateProduct Tests
│   ├── shouldUpdateProduct ✅
│   └── shouldThrowExceptionForInvalidProduct ✅
└── deleteProduct Tests
    └── shouldDeleteProduct ✅

CartServiceTest
├── addToCart Tests
│   ├── shouldAddItemToCart ✅
│   ├── shouldThrowExceptionWhenInsufficientStock ✅
│   └── shouldIncreaseQuantityIfProductExists ✅
├── removeFromCart Tests
│   ├── shouldRemoveItemFromCart ✅
│   └── shouldNotErrorWhenRemovingNonExistentItem ✅
├── updateQuantity Tests
│   ├── shouldUpdateQuantity ✅
│   └── shouldThrowExceptionWhenExceedingStock ✅
├── calculateSubtotal Tests
│   ├── shouldCalculateSubtotalCorrectly ✅
│   └── shouldReturnZeroWhenCartEmpty ✅
└── clearCart Tests
    └── shouldClearCart ✅

PaymentMethodTest
├── CashPayment Tests
│   ├── shouldReturnCorrectMethodName ✅
│   ├── shouldProcessSufficientPayment ✅
│   ├── shouldThrowExceptionWhenPaymentInsufficient ✅
│   ├── shouldValidateSuccessfully ✅
│   ├── shouldFailValidationWhenInsufficient ✅
│   └── shouldReturnCorrectReceiptDescription ✅
├── EWalletPayment Tests
│   ├── shouldReturnCorrectMethodName ✅
│   ├── shouldProcessEWalletPayment ✅
│   ├── shouldReturnChangeWhenOverpaid ✅
│   └── shouldValidateSuccessfully ✅
└── PaymentMethodFactory Tests
    ├── shouldReturnCashPaymentForCash ✅
    ├── shouldReturnEWalletPaymentForEWallet ✅
    ├── shouldReturnNullForUnknownMethod ✅
    ├── shouldReturnAllAvailableMethods ✅
    └── shouldRegisterNewMethod ✅

Total: 35 test cases
Passed: 35
Failed: 0
Coverage: ~85%
```
