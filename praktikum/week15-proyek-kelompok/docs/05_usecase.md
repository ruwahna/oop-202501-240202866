# Use Case Diagram dan Deskripsi
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Use Case Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                               AGRI-POS SYSTEM                                        │
│                                                                                      │
│                                                                                      │
│    ┌─────────┐                                                    ┌─────────┐       │
│    │         │                                                    │         │       │
│    │  Kasir  │                                                    │  Admin  │       │
│    │         │                                                    │         │       │
│    └────┬────┘                                                    └────┬────┘       │
│         │                                                              │            │
│         │                     ┌────────────────┐                       │            │
│         │────────────────────>│    UC-1        │<──────────────────────│            │
│         │                     │    Login       │                       │            │
│         │                     └────────────────┘                       │            │
│         │                                                              │            │
│         │                     ┌────────────────┐                       │            │
│         │────────────────────>│    UC-2        │<──────────────────────│            │
│         │                     │  View Products │                       │            │
│         │                     └────────────────┘                       │            │
│         │                                                              │            │
│         │                     ┌────────────────┐                       │            │
│         │────────────────────>│    UC-3        │                       │            │
│         │                     │  Add to Cart   │                       │            │
│         │                     └────────────────┘                       │            │
│         │                                                              │            │
│         │                     ┌────────────────┐                       │            │
│         │────────────────────>│    UC-4        │                       │            │
│         │                     │   Checkout     │                       │            │
│         │                     └───────┬────────┘                       │            │
│         │                             │ <<include>>                    │            │
│         │                             ▼                                │            │
│         │                     ┌────────────────┐                       │            │
│         │────────────────────>│    UC-5        │                       │            │
│         │                     │Process Payment │                       │            │
│         │                     └───────┬────────┘                       │            │
│         │                             │ <<include>>                    │            │
│         │                             ▼                                │            │
│         │                     ┌────────────────┐                       │            │
│         │────────────────────>│    UC-6        │                       │            │
│         │                     │Print Receipt   │                       │            │
│         │                     └────────────────┘                       │            │
│         │                                                              │            │
│         │                     ┌────────────────┐                       │            │
│                               │    UC-7        │<──────────────────────│            │
│                               │ Manage Product │                       │            │
│                               │    (CRUD)      │                       │            │
│                               └────────────────┘                       │            │
│                                                                        │            │
│                               ┌────────────────┐                       │            │
│                               │    UC-8        │<──────────────────────│            │
│                               │  View Reports  │                       │            │
│                               └────────────────┘                       │            │
│                                                                        │            │
│                               ┌────────────────┐                       │            │
│         │────────────────────>│    UC-9        │<──────────────────────│            │
│         │                     │    Logout      │                       │            │
│                               └────────────────┘                       │            │
│                                                                        │            │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

### 2. Deskripsi Use Case

#### UC-1: Login

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-1 |
| **Use Case Name** | Login |
| **Actor** | Kasir, Admin |
| **Description** | Pengguna melakukan autentikasi untuk masuk ke sistem |
| **Precondition** | Aplikasi sudah berjalan, pengguna belum login |
| **Postcondition** | Pengguna berhasil masuk sesuai role |

**Main Flow:**
1. Sistem menampilkan form login
2. Pengguna memasukkan username
3. Pengguna memasukkan password
4. Pengguna menekan tombol Login
5. Sistem memvalidasi kredensial
6. Sistem menampilkan Main View sesuai role

**Alternative Flow:**
- 5a. Kredensial tidak valid
  - Sistem menampilkan pesan error
  - Kembali ke langkah 1

---

#### UC-2: View Products

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-2 |
| **Use Case Name** | View Products |
| **Actor** | Kasir, Admin |
| **Description** | Pengguna melihat daftar produk |
| **Precondition** | Pengguna sudah login |
| **Postcondition** | Daftar produk ditampilkan |

**Main Flow:**
1. Pengguna membuka tab Transaction/Product
2. Sistem menampilkan daftar produk dalam tabel
3. Pengguna dapat mencari produk dengan keyword

---

#### UC-3: Add to Cart

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-3 |
| **Use Case Name** | Add to Cart |
| **Actor** | Kasir |
| **Description** | Kasir menambahkan produk ke keranjang |
| **Precondition** | Pengguna sudah login sebagai Kasir/Admin |
| **Postcondition** | Item ditambahkan ke keranjang |

**Main Flow:**
1. Kasir memilih produk dari daftar
2. Kasir memasukkan jumlah
3. Kasir menekan tombol "Tambah ke Keranjang"
4. Sistem memvalidasi stok
5. Sistem menambahkan item ke keranjang
6. Sistem menampilkan update keranjang

**Alternative Flow:**
- 4a. Stok tidak mencukupi
  - Sistem menampilkan pesan error "Stok tidak cukup"
  - Kembali ke langkah 1

---

#### UC-4: Checkout

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-4 |
| **Use Case Name** | Checkout |
| **Actor** | Kasir |
| **Description** | Kasir memproses checkout transaksi |
| **Precondition** | Keranjang tidak kosong |
| **Postcondition** | Transaksi selesai diproses |

**Main Flow:**
1. Sistem menampilkan ringkasan keranjang
2. Sistem menghitung subtotal, pajak, dan total
3. Include UC-5: Process Payment
4. Include UC-6: Print Receipt
5. Sistem mengosongkan keranjang

---

#### UC-5: Process Payment

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-5 |
| **Use Case Name** | Process Payment |
| **Actor** | Kasir |
| **Description** | Kasir memproses pembayaran |
| **Precondition** | Checkout dimulai |
| **Postcondition** | Pembayaran berhasil diproses |

**Main Flow:**
1. Kasir memilih metode pembayaran
2. Kasir memasukkan jumlah dibayar
3. Sistem memvalidasi pembayaran
4. Sistem memproses pembayaran (Strategy Pattern)
5. Sistem menghitung kembalian
6. Sistem menyimpan transaksi ke database
7. Sistem mengurangi stok produk

**Alternative Flow:**
- 3a. Jumlah pembayaran tidak cukup
  - Sistem menampilkan pesan error
  - Kembali ke langkah 2

---

#### UC-6: Print Receipt

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-6 |
| **Use Case Name** | Print Receipt |
| **Actor** | Kasir |
| **Description** | Sistem generate dan menampilkan struk |
| **Precondition** | Pembayaran berhasil |
| **Postcondition** | Struk ditampilkan |

**Main Flow:**
1. Sistem generate struk dengan format standard
2. Sistem menampilkan struk di area preview
3. (Optional) Kasir dapat mencetak/copy struk

---

#### UC-7: Manage Product (CRUD)

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-7 |
| **Use Case Name** | Manage Product |
| **Actor** | Admin |
| **Description** | Admin mengelola data produk |
| **Precondition** | Pengguna sudah login sebagai Admin |
| **Postcondition** | Data produk berubah sesuai operasi |

**Main Flow (Create):**
1. Admin membuka tab Product Management
2. Admin mengisi form produk baru
3. Admin menekan tombol Save
4. Sistem memvalidasi input
5. Sistem menyimpan produk ke database
6. Sistem refresh daftar produk

**Alternative Flow (Update):**
1. Admin memilih produk dari tabel
2. Form terisi dengan data produk
3. Admin mengubah data
4. Admin menekan tombol Update
5. Sistem menyimpan perubahan

**Alternative Flow (Delete):**
1. Admin memilih produk dari tabel
2. Admin menekan tombol Delete
3. Sistem meminta konfirmasi
4. Admin mengkonfirmasi
5. Sistem menghapus produk

---

#### UC-8: View Reports

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-8 |
| **Use Case Name** | View Reports |
| **Actor** | Admin |
| **Description** | Admin melihat laporan penjualan |
| **Precondition** | Pengguna sudah login sebagai Admin |
| **Postcondition** | Laporan ditampilkan |

**Main Flow:**
1. Admin membuka tab Report
2. Admin memilih jenis laporan (Harian/Periode)
3. Admin memilih tanggal/range
4. Admin menekan tombol Generate
5. Sistem query data dari database
6. Sistem generate format laporan
7. Sistem menampilkan laporan

---

#### UC-9: Logout

| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-9 |
| **Use Case Name** | Logout |
| **Actor** | Kasir, Admin |
| **Description** | Pengguna keluar dari sistem |
| **Precondition** | Pengguna sudah login |
| **Postcondition** | Pengguna keluar, session dihapus |

**Main Flow:**
1. Pengguna menekan tombol Logout
2. Sistem menghapus session
3. Sistem mengosongkan keranjang
4. Sistem menampilkan Login View

### 3. Actor Description

| Actor | Description | Capabilities |
|-------|-------------|--------------|
| **Kasir** | Operator yang melakukan transaksi penjualan | Login, View Products, Add to Cart, Checkout, Logout |
| **Admin** | Administrator sistem dengan akses penuh | Semua kemampuan Kasir + Manage Product, View Reports |
