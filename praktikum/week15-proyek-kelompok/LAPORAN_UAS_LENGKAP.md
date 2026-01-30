# LAPORAN PROYEK AKHIR SEMESTER
## APLIKASI AGRI-POS: SISTEM POINT OF SALE UNTUK PERTANIAN
### Mata Kuliah: Pemrograman Berorientasi Objek (OOP)
### Universitas Negeri Semarang | Semester Ganjil 2025/2026

---

## INFORMASI DOKUMEN

| Aspek | Keterangan |
|-------|-----------|
| **Nama Proyek** | Agri-POS (Sistem Point of Sale Pertanian) |
| **Mata Kuliah** | Pemrograman Berorientasi Objek (OOP) |
| **Dosen Pengampu** | Pemrograman Berorientasi Objek |
| **Kelas** | 3IKRB |
| **Semester** | Ganjil 2025/2026 |
| **Tanggal Penyerahan** | 18 Januari 2026 |
| **Status Proyek** | Selesai âœ“ |

---

## TIM PENGEMBANG

| No | Nama Lengkap | NIM | Peran Utama | Kontribusi Spesifik |
|---|---|---|---|---|
| 1 | Indah Ruwahna Anugraheni | 240202866 | Project Lead & Backend Architect | Perancangan arsitektur sistem, Database design & optimization, Service layer logic, DAO implementation, Code review |
| 2 | Lia Lusianti | 240202869 | Frontend Developer & UI/UX | Interface design, JavaFX implementation, Transaction views, User experience optimization |
| 3 | Fikianto | 240202899 | Backend Developer | Payment system implementation, Service layer, Business logic, Integration |
| 4 | Rizal Ramadhani | 240202883 | QA Engineer & Documentation Lead | Quality assurance, Bug detection & fixing, Comprehensive testing, Technical documentation, Database migration |

---

# BAGIAN I: PENDAHULUAN

## 1.1 Latar Belakang

Dalam era transformasi digital, industri pertanian menghadapi tantangan untuk memodernisasi sistem transaksi penjualan. Toko-toko pertanian tradisional membutuhkan solusi teknologi yang dapat:

### Kebutuhan Bisnis:
- **Efisiensi Operasional**: Mengurangi waktu transaksi dan meminimalkan kesalahan manusia dalam perhitungan
- **Akurasi Data**: Memastikan data produk, stok, dan transaksi tercatat dengan akurat dan real-time
- **Fleksibilitas Pembayaran**: Mendukung berbagai metode pembayaran modern (Cash, E-Wallet, QRIS)
- **Insight Bisnis**: Menyediakan laporan penjualan dan analisis untuk pengambilan keputusan
- **Manajemen Stok**: Tracking stok otomatis dengan alert stok rendah
- **Promosi & Diskon**: Sistem diskon terstruktur untuk meningkatkan penjualan

### Kebutuhan Pembelajaran:
Proyek ini memberikan kesempatan untuk mengintegrasikan semua konsep Pemrograman Berorientasi Objek (OOP) yang telah dipelajari selama satu semester:

- **Encapsulation** (Pembungkus Data): Membungkus data dan method dalam kelas dengan access modifier yang tepat
- **Inheritance** (Pewarisan): Memanfaatkan pewarisan untuk reusability dan polimorfisme
- **Polymorphism** (Polimorfisme): Method overriding, interface implementation, dynamic dispatch
- **Abstraction** (Abstraksi): Menggunakan interface dan abstract class untuk menyembunyikan kompleksitas
- **Design Patterns**: Singleton, Strategy, Factory, DAO, Observer, Command patterns
- **SOLID Principles**: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- **Clean Architecture**: Layered architecture dengan clear separation of concerns
- **Testing Best Practices**: Unit testing, mocking, coverage analysis
- **Database Integration**: JDBC, connection pooling, prepared statements

## 1.2 Tujuan Proyek

### Tujuan Umum
Mengembangkan aplikasi desktop yang menerapkan konsep OOP mendalam, design patterns enterprise-level, dan arsitektur berlapis untuk membangun sistem POS yang fungsional, maintainable, scalable, dan berkualitas tinggi.

### Tujuan Spesifik Pembelajaran

#### 1. **Penguasaan Konsep OOP**
   - âœ“ Mengintegrasikan encapsulation, inheritance, polymorphism, dan abstraction dalam desain kelas-kelas aplikasi
   - âœ“ Membuat class hierarchy yang logis dengan method dan atribut yang terorganisir
   - âœ“ Menggunakan access modifiers (public, private, protected) dengan tepat
   - âœ“ Mengimplementasikan interface dan abstract class untuk kontrak kode

#### 2. **Penerapan Design Patterns**
   - âœ“ **Singleton Pattern**: Untuk DatabaseConnection (satu instance koneksi database)
   - âœ“ **Singleton Pattern**: Untuk DiscountConfigService (shared discount config Admin-Kasir)
   - âœ“ **Strategy Pattern**: Untuk PaymentMethod (Cash, E-Wallet, QRIS sebagai strategy berbeda)
   - âœ“ **Factory Pattern**: Untuk PaymentMethodFactory (pembuatan payment method instances)
   - âœ“ **DAO Pattern**: Untuk abstraksi akses database
   - âœ“ **Repository Pattern**: Untuk data persistence abstraction

#### 3. **Arsitektur Berlapis (Layered Architecture)**
   - âœ“ **Presentation Layer**: JavaFX GUI dengan FXML dan controllers
   - âœ“ **Controller Layer**: Event handling dan orchestration
   - âœ“ **Service Layer**: Business logic dan validasi
   - âœ“ **DAO Layer**: Data access abstraction
   - âœ“ **Database Layer**: PostgreSQL dengan JDBC

#### 4. **Fitur Aplikasi yang Komprehensif**
   - âœ“ Manajemen produk (CRUD), pencarian, filter kategori
   - âœ“ Sistem transaksi lengkap dengan cart management
   - âœ“ Sistem pembayaran multi-metode
   - âœ“ Sistem diskon fleksibel (per-item dan per-transaksi)
   - âœ“ Manajemen diskon oleh Admin dengan sync real-time ke Kasir
   - âœ“ Struk penjualan dengan detail lengkap
   - âœ“ Riwayat transaksi dengan filter dan cetak ulang
   - âœ“ Laporan penjualan untuk analisis bisnis
   - âœ“ Dashboard admin dengan statistik real-time
   - âœ“ Responsive design (support mobile dan desktop)

#### 5. **Testing dan Quality Assurance**
   - âœ“ Unit testing dengan JUnit 5
   - âœ“ Mocking dengan Mockito
   - âœ“ Test coverage >70% untuk critical path
   - âœ“ Bug detection dan fixing

#### 6. **Database Integration**
   - âœ“ Normalisasi database (3NF)
   - âœ“ Relasi antar tabel dengan foreign keys
   - âœ“ Prepared statements untuk keamanan SQL Injection
   - âœ“ Transaction management

#### 7. **Documentation**
   - âœ“ Code documentation dengan JavaDoc
   - âœ“ Architecture documentation
   - âœ“ User manual dan panduan penggunaan
   - âœ“ Team roles dan contribution tracking
   - âœ“ API documentation

## 1.3 Ruang Lingkup Proyek

### Fitur yang Diimplementasikan

#### A. **Sistem Login & Autentikasi**
Pengguna (Kasir dan Admin) harus login terlebih dahulu sebelum mengakses aplikasi. Sistem menyimpan kredensial di database dan memvalidasi setiap login request.

- Username dan password validation
- Role-based access control (Admin, Kasir)
- Session management
- Logout functionality

#### B. **Modul Manajemen Produk (Admin)**
Admin dapat mengelola master data produk pertanian dengan operasi CRUD lengkap.

**Fitur Detail:**
- Tambah produk baru (kode unik, nama, kategori, harga, stok, unit)
- Edit informasi produk
- Hapus produk dari sistem
- Cari produk berdasarkan kode atau nama
- Filter produk berdasarkan kategori
- Alert otomatis untuk stok rendah (<10 unit)
- Tracking stok real-time
- View daftar produk dengan pagination

#### C. **Modul Transaksi Penjualan (Kasir)**
Kasir dapat membuat transaksi penjualan lengkap dari pencarian produk hingga checkout.

**Workflow Transaksi:**
1. Buat transaksi baru (auto-generated kode transaksi)
2. Search produk berdasarkan kode atau nama
3. Tambahkan produk ke keranjang dengan quantity
4. Ubah quantity produk di keranjang
5. Hapus produk dari keranjang (jika diperlukan)
6. Aplikasikan diskon (per-item atau per-transaksi)
7. Lihat ringkasan: subtotal, diskon, pajak (10%), total
8. Pilih metode pembayaran
9. Input nominal pembayaran
10. Sistem otomatis hitung kembalian
11. Cetak struk
12. Transaksi tersimpan ke database

**Kalkulasi:**
```
Subtotal = Î£(Harga per unit Ã— Jumlah)
Diskon Per Item = Jumlah Ã— Harga satuan Ã— Persentase diskon
Subtotal Setelah Diskon = Subtotal - Total Diskon Per Item - Diskon Transaksi
Pajak = Subtotal Setelah Diskon Ã— 10%
Total Akhir = Subtotal Setelah Diskon + Pajak
Kembalian = Jumlah Bayar - Total Akhir
```

#### D. **Sistem Pembayaran (Multiple Payment Methods)**
Aplikasi mendukung tiga metode pembayaran dengan validasi otomatis.

**Metode 1 - Cash Payment:**
- Input nominal pembayaran tunai
- Sistem hitung kembalian
- Validasi pembayaran â‰¥ total
- Catatan: Kembalian tidak dapat digunakan untuk transaksi berikutnya

**Metode 2 - E-Wallet Payment:**
- Support OVO, GoPay, Dana, ShopeePay
- Input nominal pembayaran dari e-wallet
- Sistem validasi pembayaran = total akhir
- Tidak ada kembalian (pembayaran harus sama persis)

**Metode 3 - QRIS Payment:**
- Generate QRIS code untuk pembayaran
- Scan menggunakan smartphone
- Konfirmasi pembayaran melalui app
- Sistem validasi pembayaran sesuai total

**Implementasi:** Strategy Pattern - setiap metode pembayaran adalah strategy yang berbeda dengan `validatePayment()` dan `processPayment()` method yang berbeda.

#### E. **Sistem Diskon & Promosi**
Aplikasi menyediakan fleksibilitas untuk memberikan diskon kepada pelanggan dengan manajemen terpusat oleh Admin.

**Jenis Diskon:**
- **Persentase**: Diskon berdasarkan persentase (5%, 10%, 15%, dll)
- **Nominal**: Potongan harga tetap (Rp 50.000, Rp 100.000, dll)
- **Bulk Discount**: Diskon untuk pembelian quantity tertentu (min 5 item dapat 15%)
- **Voucher**: Kode promo dengan nilai tetap atau persentase

**Fitur Kasir:**
- Pilih diskon dari dropdown (dikelola Admin)
- Input kode voucher manual
- Refresh daftar diskon terbaru dari Admin
- Kalkulasi otomatis pajak setelah diskon
- Diskon tercatat dalam database dan struk

**Fitur Admin (NEW - Manajemen Diskon):**
- Tambah diskon baru dengan berbagai tipe
- Edit konfigurasi diskon yang ada
- Hapus diskon dari sistem
- Aktifkan/nonaktifkan diskon
- Search dan filter diskon
- Real-time sync dengan tampilan Kasir

**Implementasi:**
- `DiscountConfigService` (Singleton) - Shared service untuk konfigurasi diskon
- `DiscountManagementView` - UI Admin untuk CRUD diskon
- `TransactionView` - Integrasi dengan DiscountConfigService
- Model `DiscountConfig` - Inner class untuk data diskon
- Method `applyDiscount()` dalam `CartService`
- Column `discount` dalam `transactions` table
- Display diskon dalam `ReceiptService`

**Diskon Default:**
| Kode | Nama | Tipe | Nilai |
|------|------|------|-------|
| UMUM5 | Diskon Umum | Persentase | 5% |
| MEMBER10 | Diskon Member | Persentase | 10% |
| BULK15 | Diskon Bulk | Persentase | 15% (min 5 item) |
| WELCOME | Welcome Discount | Persentase | 5% |
| PROMO50K | Promo 50K | Nominal | Rp 50.000 |

#### F. **Struk Penjualan (Receipt)**
Sistem otomatis generate struk detail setiap transaksi berhasil.

**Isi Struk:**
```
â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        TOKO PERTANIAN "AGRI"
           Struk Penjualan
â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
No. Transaksi: TRX001
Tanggal/Waktu : 18-01-2026 14:30:45
Kasir        : Ahmad (ID: 1)
â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

PRODUK                  HARGA    QTY   TOTAL
â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
Beras Premium        50.000      2    100.000
Pupuk Urea          12.500      3     37.500
Bibit Padi           8.000      1      8.000
â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
SUBTOTAL                              145.500
Diskon                                 (5.000)
â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
Subtotal Setelah Diskon                140.500
Pajak (10%)                             14.050
â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
TOTAL                                 154.550
Pembayaran (Cash)                     160.000
Kembalian                               5.450
â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

   Terima kasih telah berbelanja!
   Silakan datang kembali :)

â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
```

#### G. **Riwayat Transaksi**
Kasir dapat melihat semua transaksi yang pernah dibuat dan melakukan berbagai operasi.

**Fitur:**
- Tampilkan daftar transaksi dengan pagination
- Filter berdasarkan tanggal tunggal atau periode
- Filter berdasarkan kasir (username)
- Klik item untuk melihat detail transaksi lengkap
- Cetak ulang struk untuk transaksi lama
- Lihat metode pembayaran dan kembalian
- Lihat total diskon untuk setiap transaksi
- Export history (future enhancement)

#### H. **Dashboard Admin**
Admin melihat overview bisnis dengan statistik real-time.

**Informasi Dashboard:**
- **Total Transaksi Hari Ini**: Jumlah transaksi dalam 24 jam terakhir
- **Revenue Hari Ini**: Total penjualan (setelah diskon dan pajak)
- **Total Items Terjual**: Jumlah item yang terjual hari ini
- **Top 5 Produk Terjual**: Produk paling laris
- **Stock Alert**: Produk dengan stok rendah (<10)
- **Metode Pembayaran Breakdown**: Grafik pie dengan Cash/E-Wallet/QRIS

#### I. **Laporan Penjualan (Admin)**
Admin dapat generate laporan terstruktur untuk analisis bisnis.

**Jenis Laporan:**

1. **Daily Sales Report (Laporan Penjualan Harian)**
   - Pilih tanggal tertentu
   - Tampilkan total transaksi, revenue, items terjual
   - Breakdown per produk
   - Export ke format PDF/Excel (future)

2. **Period Sales Report (Laporan Penjualan Periode)**
   - Input start date dan end date
   - Aggregate data untuk periode tersebut
   - Tampilkan trend penjualan
   - Perbandingan dengan periode sebelumnya (future)

3. **Product Sales Report (Laporan Penjualan Per Produk)**
   - Ranking produk berdasarkan quantity terjual
   - Ranking produk berdasarkan revenue
   - Top sellers vs slow movers
   - Rekomendasi stock management

4. **Payment Method Report (Laporan Metode Pembayaran)**
   - Breakdown penggunaan metode pembayaran
   - Persentase per metode
   - Trend perubahan preferensi pembayaran
   - Implikasi untuk strategi promosi

### Teknologi yang Digunakan

| Kategori | Teknologi | Versi | Alasan Pemilihan |
|----------|-----------|-------|---|
| **Bahasa Pemrograman** | Java | 17 (OpenJDK) | Modern, LTS, type-safe, strong OOP support, backward compatible |
| **Build Tool** | Apache Maven | 3.8.x | Dependency management, build automation, plugin ecosystem |
| **GUI Framework** | JavaFX | 17 LTS | Modern, rich UI components, CSS styling, FXML support |
| **Database** | PostgreSQL | 13+ | Reliable, ACID compliance, JSON support, powerful query engine |
| **JDBC Driver** | PostgreSQL JDBC | 42.x | Native PostgreSQL support, modern protocol |
| **Testing Framework** | JUnit 5 (Jupiter) | 5.9.x | Parameterized tests, new annotation model, better integration |
| **Mocking Library** | Mockito | 5.x | Intuitive mock creation, behavior verification |
| **Logging** | Java Util Logging | Built-in | Lightweight, integrated, no external dependency |
| **Version Control** | Git | Latest | Distributed, collaboration, GitHub integration |
| **IDE** | IntelliJ IDEA / VS Code | Latest | Code completion, debugging, Git integration |

---

# BAGIAN II: DESKRIPSI SISTEM DAN FITUR

## 2.1 Analisis Kebutuhan

### Kebutuhan Fungsional (Functional Requirements)

#### FR-1: Manajemen User & Autentikasi
- FR-1.1: Sistem harus mendukung login dengan username dan password
- FR-1.2: Sistem harus membedakan akses berdasarkan role (Admin vs Kasir)
- FR-1.3: Admin dapat mengelola data pengguna (CRUD kasir)
- FR-1.4: Sistem harus mencatat siapa kasir melakukan transaksi
- FR-1.5: Session timeout untuk keamanan

#### FR-2: Manajemen Produk
- FR-2.1: Admin dapat menambahkan produk baru dengan kode unik
- FR-2.2: Admin dapat mengubah informasi produk
- FR-2.3: Admin dapat menghapus produk
- FR-2.4: Sistem tracking stok real-time
- FR-2.5: Alert stok rendah (< 10 unit)
- FR-2.6: Kasir dapat melihat daftar produk (read-only)

#### FR-3: Sistem Transaksi Penjualan
- FR-3.1: Kasir dapat membuat transaksi baru dengan kode auto-generated
- FR-3.2: Kasir dapat search produk berdasarkan kode atau nama
- FR-3.3: Kasir dapat tambah produk ke keranjang dengan quantity
- FR-3.4: Kasir dapat ubah quantity atau hapus produk dari keranjang
- FR-3.5: Sistem auto-kalkulasi subtotal, pajak, dan total
- FR-3.6: Sistem menyimpan riwayat transaksi

#### FR-4: Sistem Diskon
- FR-4.1: Kasir dapat aplikasikan diskon per-item (dalam %)
- FR-4.2: Kasir dapat aplikasikan diskon per-transaksi (dalam %)
- FR-4.3: Pajak dikalkulasi berdasarkan harga setelah diskon
- FR-4.4: Diskon tercatat dalam database dan struk
- FR-4.5: Admin dapat menambah diskon baru (persentase, nominal, bulk, voucher)
- FR-4.6: Admin dapat mengubah konfigurasi diskon
- FR-4.7: Admin dapat menghapus diskon dari sistem
- FR-4.8: Admin dapat mengaktifkan/menonaktifkan diskon
- FR-4.9: Diskon yang dikelola Admin otomatis tersinkron ke Kasir (real-time)

#### FR-5: Sistem Pembayaran
- FR-5.1: Sistem mendukung pembayaran Cash dengan kalkulasi kembalian
- FR-5.2: Sistem mendukung pembayaran E-Wallet (OVO, GoPay, Dana, ShopeePay)
- FR-5.3: Sistem mendukung pembayaran QRIS
- FR-5.4: Sistem memvalidasi nominal pembayaran
- FR-5.5: Sistem mencatat metode pembayaran dalam transaksi

#### FR-6: Struk Penjualan
- FR-6.1: Sistem auto-generate struk setiap transaksi berhasil
- FR-6.2: Struk berisi detail produk, harga, diskon, pajak, total
- FR-6.3: Struk mencakup informasi kasir dan waktu transaksi
- FR-6.4: Kasir dapat print struk atau view digital

#### FR-7: Riwayat Transaksi
- FR-7.1: Kasir dapat melihat daftar semua transaksi
- FR-7.2: Kasir dapat filter berdasarkan tanggal atau periode
- FR-7.3: Kasir dapat view detail transaksi
- FR-7.4: Kasir dapat cetak ulang struk untuk transaksi lama

#### FR-8: Dashboard & Laporan (Admin)
- FR-8.1: Dashboard menampilkan statistik real-time (transaksi, revenue, items)
- FR-8.2: Admin dapat generate daily sales report
- FR-8.3: Admin dapat generate period sales report
- FR-8.4: Admin dapat generate product sales report
- FR-8.5: Admin dapat generate payment method report

#### FR-9: Manajemen Diskon (Admin) - NEW
- FR-9.1: Admin dapat menambah diskon baru (persentase, nominal, bulk, voucher)
- FR-9.2: Admin dapat mengubah konfigurasi diskon yang sudah ada
- FR-9.3: Admin dapat menghapus diskon dari sistem
- FR-9.4: Admin dapat mengaktifkan/menonaktifkan diskon
- FR-9.5: Admin dapat mencari diskon berdasarkan nama/kode
- FR-9.6: Diskon yang dikelola Admin otomatis tersinkron ke Kasir (real-time)

### Kebutuhan Non-Fungsional (Non-Functional Requirements)

#### NFR-1: Performance
- Respon time maksimal 2 detik untuk query data
- Support minimum 100 concurrent users
- Load time aplikasi < 3 detik

#### NFR-2: Reliability
- Uptime target 99.5%
- ACID compliance untuk database
- Backup otomatis database

#### NFR-3: Security
- Password hashing (tidak plain text)
- SQL Injection prevention (prepared statements)
- Session management & timeout
- Role-based access control

#### NFR-4: Usability
- Interface intuitif dan mudah dipelajari
- Help system dan tooltip tersedia
- Keyboard shortcuts untuk operasi umum

#### NFR-5: Maintainability
- Clean code dengan naming convention
- JavaDoc documentation
- Modular architecture
- Comprehensive testing

#### NFR-6: Scalability
- Layered architecture untuk mudah extend fitur
- Database normalization untuk performa
- Stateless service layer untuk load balancing (future)

## 2.3 Use Case Diagram

![UseCase](/praktikum/week15-proyek-kelompok/screenshots/USEEECASEEE-agripost.drawio.png)

### 2.4 Actor Description

| Actor | Description | Access Level |
|-------|-------------|--------------|
| **Kasir** | Operator transaksi penjualan | Login, View Produk (read-only), Transaksi, Keranjang, Checkout, Apply Diskon/Voucher, Cetak Struk, Riwayat Transaksi |
| **Admin** | Administrator sistem | Full Access: Dashboard, CRUD Produk, CRUD Diskon, Laporan Penjualan, Export Report, Low Stock Alert |

### 2.5 Use Case Detail per Actor

#### ðŸª Kasir - Use Case List

| No | Use Case | Deskripsi | Tab Menu |
|----|----------|-----------|----------|
| 1 | Login | Autentikasi masuk sistem dengan pilih role | LoginView |
| 2 | Logout | Keluar dari sistem | Header |
| 3 | New Transaction | Membuat transaksi penjualan baru | ðŸ›’ Transaksi Baru |
| 4 | Search Product | Mencari produk berdasarkan nama/kode | ðŸ›’ Transaksi Baru |
| 5 | Filter Category | Filter produk berdasarkan kategori | ðŸ›’ Transaksi Baru |
| 6 | Add to Cart | Menambahkan produk ke keranjang | ðŸ›’ Transaksi Baru |
| 7 | Update Cart Qty | Mengubah jumlah item di keranjang | ðŸ›’ Transaksi Baru |
| 8 | Remove from Cart | Menghapus item dari keranjang | ðŸ›’ Transaksi Baru |
| 9 | Clear Cart | Mengosongkan seluruh keranjang | ðŸ›’ Transaksi Baru |
| 10 | Checkout (Cash) | Proses pembayaran tunai | ðŸ›’ Transaksi Baru |
| 11 | Checkout (E-Wallet) | Proses pembayaran e-wallet | ðŸ›’ Transaksi Baru |
| 12 | Checkout (QRIS) | Proses pembayaran QRIS | ðŸ›’ Transaksi Baru |
| 13 | Apply Discount | Menerapkan diskon dari dropdown | ðŸ›’ Transaksi Baru |
| 14 | Apply Voucher | Memasukkan kode voucher manual | ðŸ›’ Transaksi Baru |
| 15 | Refresh Discount | Memperbarui daftar diskon dari Admin | ðŸ›’ Transaksi Baru |
| 16 | Print Receipt | Mencetak struk pembelian | ðŸ›’ Transaksi Baru |
| 17 | View Transaction History | Melihat riwayat transaksi | ðŸ“‹ Riwayat Transaksi |
| 18 | Reprint Receipt | Cetak ulang struk transaksi lama | ðŸ“‹ Riwayat Transaksi |

#### ðŸ‘” Admin - Use Case List

| No | Use Case | Deskripsi | Tab Menu |
|----|----------|-----------|----------|
| 1 | Login | Autentikasi masuk sistem dengan pilih role | LoginView |
| 2 | Logout | Keluar dari sistem | Header |
| 3 | View Dashboard | Melihat statistik penjualan dan grafik | ðŸ“Š Dashboard |
| 4 | View Low Stock Alert | Melihat produk dengan stok rendah | ðŸ“Š Dashboard |
| 5 | Add Product | Menambah produk baru | ðŸ“¦ Manajemen Produk |
| 6 | Edit Product | Mengubah data produk | ðŸ“¦ Manajemen Produk |
| 7 | Delete Product | Menghapus produk dari sistem | ðŸ“¦ Manajemen Produk |
| 8 | Search Product | Mencari produk | ðŸ“¦ Manajemen Produk |
| 9 | Daily Sales Report | Generate laporan harian | ðŸ“ˆ Laporan Penjualan |
| 10 | Period Sales Report | Generate laporan periode | ðŸ“ˆ Laporan Penjualan |
| 11 | Export Report | Export laporan ke file | ðŸ“ˆ Laporan Penjualan |
| 12 | Add Discount | Menambah diskon baru | ðŸŽ Manajemen Diskon |
| 13 | Edit Discount | Mengubah konfigurasi diskon | ðŸŽ Manajemen Diskon |
| 14 | Delete Discount | Menghapus diskon | ðŸŽ Manajemen Diskon |
| 15 | Toggle Discount Status | Aktifkan/nonaktifkan diskon | ðŸŽ Manajemen Diskon |
| 16 | Search Discount | Mencari diskon | ðŸŽ Manajemen Diskon |

---

# BAGIAN III: DESAIN DAN ARSITEKTUR

## 3.1 Arsitektur Sistem

### Konsep Layered Architecture

Agri-POS menggunakan **Layered Architecture (N-Tier Architecture)** dengan 5 layer utama:

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚           PRESENTATION LAYER (View)                     â”‚
â”‚  - JavaFX GUI (FXML)                                   â”‚
â”‚  - LoginView, MainView (Transactions, Products, etc)   â”‚
â”‚  - Event handlers dan user interaction                 â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                   â”‚ (User Actions / Display Updates)
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚         CONTROLLER LAYER                                â”‚
â”‚  - LoginController, PosController                       â”‚
â”‚  - Handle events dari View                              â”‚
â”‚  - Call Service untuk business logic                    â”‚
â”‚  - Update View dengan hasil                             â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                   â”‚ (Business Logic Request)
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚         SERVICE LAYER (Business Logic)                  â”‚
â”‚  - ProductService, CartService, TransactionService     â”‚
â”‚  - ReceiptService, ReportService, AuthService          â”‚
â”‚  - DiscountConfigService (NEW - Singleton Pattern)     â”‚
â”‚  - Business rules, validasi, kalkulasi                 â”‚
â”‚  - Orchestrate DAO calls                               â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                   â”‚ (DAO Calls)
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚         DAO LAYER (Data Access)                         â”‚
â”‚  - ProductDAO, UserDAO, TransactionDAO (Interface)     â”‚
â”‚  - JdbcProductDAO, JdbcUserDAO, JdbcTransactionDAO     â”‚
â”‚  - CRUD operations, Query building                      â”‚
â”‚  - Abstraction dari database detail                     â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                   â”‚ (SQL Queries via JDBC)
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚         DATABASE LAYER                                  â”‚
â”‚  - PostgreSQL 13+                                       â”‚
â”‚  - Tables: products, users, transactions, etc          â”‚
â”‚  - Foreign keys, constraints, indexes                  â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

### Manfaat Layered Architecture

| Aspek | Manfaat |
|-------|---------|
| **Separation of Concerns** | Setiap layer punya tanggung jawab spesifik, mudah dipahami |
| **Reusability** | Service layer dapat digunakan oleh multiple controllers |
| **Testability** | Mudah mock dependencies, unit test independen |
| **Maintainability** | Change di satu layer tidak berdampak layer lain |
| **Scalability** | Mudah tambah fitur baru tanpa ubah layer lain |
| **Flexibility** | Database bisa switch (PostgreSQL â†’ MySQL) tanpa ubah service |
| **Security** | Business logic terpusat di service layer |

### Penjelasan Konseptual: Mengapa Layered Architecture?

**Pertanyaan Desain:** *"Mengapa tidak langsung buat semua logika di satu class?"*

**Jawaban Konseptual:**

Dalam pengembangan aplikasi enterprise seperti POS, kompleksitas akan terus bertambah. Jika semua logika (tampilan, business logic, database) dicampur dalam satu class:

1. **Problem 1 - God Class Anti-Pattern**: Satu class menjadi terlalu besar dan sulit dipahami
2. **Problem 2 - Ripple Effect**: Perubahan kecil di database menyebabkan perubahan di seluruh aplikasi
3. **Problem 3 - Testing Nightmare**: Tidak bisa test business logic tanpa menyiapkan database sungguhan

**Solusi dengan Layered Architecture:**

```
ANALOGI: Seperti struktur organisasi perusahaan
â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•

Direktur (Controller)     â†’ Menerima permintaan, mendelegasikan tugas
         â”‚
Manager (Service)         â†’ Menjalankan business logic, koordinasi tim
         â”‚
Staff Database (DAO)      â†’ Eksekusi query, tidak peduli business logic
         â”‚
Database (PostgreSQL)     â†’ Tempat penyimpanan data
```

Dengan struktur ini:
- **Controller** tidak peduli bagaimana data disimpan
- **Service** tidak peduli bagaimana UI ditampilkan
- **DAO** tidak peduli apa business rule-nya
- Setiap layer **hanya tahu layer di bawahnya**, tidak tahu layer di atasnya

**Bukti Keberhasilan Desain:**
- Saat menambah diskon tipe "Voucher", hanya perlu tambah class `VoucherDiscount` tanpa mengubah UI atau database
- Saat migrasi database (jika diperlukan), hanya perlu ubah implementasi DAO, service layer tetap sama

## 3.2 Package Structure

```
src/main/java/com/upb/agripos/
â”œâ”€â”€ AppJavaFx.java                 # Main entry point
â”œâ”€â”€ controller/
â”‚   â”œâ”€â”€ LoginController.java       # Handle login
â”‚   â””â”€â”€ PosController.java         # Handle business logic
â”œâ”€â”€ dao/
â”‚   â”œâ”€â”€ ProductDAO.java            # Interface
â”‚   â”œâ”€â”€ UserDAO.java               # Interface
â”‚   â”œâ”€â”€ TransactionDAO.java        # Interface
â”‚   â”œâ”€â”€ JdbcProductDAO.java        # Implementation
â”‚   â”œâ”€â”€ JdbcUserDAO.java           # Implementation
â”‚   â””â”€â”€ JdbcTransactionDAO.java    # Implementation
â”œâ”€â”€ exception/
â”‚   â”œâ”€â”€ AuthenticationException.java
â”‚   â”œâ”€â”€ DataNotFoundException.java
â”‚   â”œâ”€â”€ OutOfStockException.java
â”‚   â”œâ”€â”€ PaymentException.java
â”‚   â””â”€â”€ ValidationException.java
â”œâ”€â”€ model/
â”‚   â”œâ”€â”€ Product.java
â”‚   â”œâ”€â”€ User.java
â”‚   â”œâ”€â”€ Transaction.java
â”‚   â”œâ”€â”€ TransactionItem.java
â”‚   â”œâ”€â”€ Cart.java
â”‚   â”œâ”€â”€ CartItem.java
â”‚   â””â”€â”€ CheckoutSummary.java
â”œâ”€â”€ service/
â”‚   â”œâ”€â”€ ProductService.java
â”‚   â”œâ”€â”€ CartService.java
â”‚   â”œâ”€â”€ TransactionService.java
â”‚   â”œâ”€â”€ AuthService.java
â”‚   â”œâ”€â”€ ReportService.java
â”‚   â”œâ”€â”€ ReceiptService.java
â”‚   â”œâ”€â”€ DiscountConfigService.java    # Singleton
â”‚   â”œâ”€â”€ payment/
â”‚   â”‚   â”œâ”€â”€ PaymentMethod.java        # Interface
â”‚   â”‚   â”œâ”€â”€ CashPayment.java
â”‚   â”‚   â”œâ”€â”€ EWalletPayment.java
â”‚   â”‚   â”œâ”€â”€ QRISPayment.java
â”‚   â”‚   â””â”€â”€ PaymentMethodFactory.java
â”‚   â””â”€â”€ discount/
â”‚       â”œâ”€â”€ DiscountStrategy.java     # Interface
â”‚       â”œâ”€â”€ PercentageDiscount.java
â”‚       â”œâ”€â”€ FixedDiscount.java
â”‚       â”œâ”€â”€ BulkDiscount.java
â”‚       â””â”€â”€ VoucherDiscount.java
â”œâ”€â”€ util/
â”‚   â”œâ”€â”€ DatabaseConnection.java       # Singleton
â”‚   â””â”€â”€ DatabaseMigration.java        # Auto migration
â””â”€â”€ view/
    â”œâ”€â”€ LoginView.java
    â”œâ”€â”€ MainView.java
    â”œâ”€â”€ TransactionView.java
    â”œâ”€â”€ DashboardView.java
    â”œâ”€â”€ ProductManagementView.java
    â”œâ”€â”€ ReportView.java
    â””â”€â”€ DiscountManagementView.java
```

## 3.3 SOLID Principles Implementation

| Principle | Implementation |
|-----------|----------------|
| **S** - Single Responsibility | ProductService hanya menangani logika produk |
| **O** - Open/Closed | PaymentMethod dapat di-extend tanpa modifikasi |
| **L** - Liskov Substitution | CashPayment & EWalletPayment interchangeable |
| **I** - Interface Segregation | DAO interfaces terpisah per entity |
| **D** - Dependency Inversion | Services depend on DAO interfaces |

## 3.4 Class Diagram (Visual)

### Model Classes

![Model classes](/praktikum/week15-proyek-kelompok/screenshots/Model%20classes.png)

### Strategy Pattern (Payment)

![Strategy Pattern - Payment Method](/praktikum/week15-proyek-kelompok/screenshots/strategi%20pattern-paymentment-menthod.drawio.png)

### DAO Pattern

![DAO Interface](/praktikum/week15-proyek-kelompok/screenshots/DAO%20interface.drawio%20(1).png)

## 3.5 Sequence Diagrams

### 3.6.1 Login Sequence

![Login Sequence](/praktikum/week15-proyek-kelompok/screenshots/Login%20Sequence.png)

### 3.6.2 Checkout Transaction

![Checkout Transaction](/praktikum/week15-proyek-kelompok/screenshots/Checkout%20Transaction.drawio.png)

### 3.6.3 Admin Discount Management

![Admin Discount Management](/praktikum/week15-proyek-kelompok/screenshots/Admin%20Discount%20Management%20.drawio.png)

### 3.6.4 Kasir Apply Discount

![Kasir Apply Discount](/praktikum/week15-proyek-kelompok/screenshots/Kasir%20Apply%20Discount%20.drawio.png)

## 3.6 Design Patterns Summary

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

## 3.7 Database Schema (ERD)

![Database Schema (ERD)](/praktikum/week15-proyek-kelompok/screenshots/database%20schema%20(ERD).png)

### Database Relationships

```
RELASI:
â•â•â•â•â•â•â•
â€¢ users (1) â”€â”€â”€â”€â”€â”€< (*) transactions     : Satu user bisa punya banyak transaksi
â€¢ transactions (1) â”€â”€â”€â”€â”€â”€< (*) transaction_items : Satu transaksi punya banyak item
â€¢ products (1) â”€â”€â”€â”€â”€â”€< (*) transaction_items     : Satu produk bisa ada di banyak item
```

## 3.8 Class Diagram (Simplified - Domain Model)

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                      MODEL CLASSES (Entities)                    â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜

â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚         User              â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ - id: long                 â”‚
â”‚ - username: String         â”‚
â”‚ - password: String         â”‚
â”‚ - email: String            â”‚
â”‚ - role: UserRole (ENUM)    â”‚
â”‚ - active: boolean          â”‚
â”‚ - createdAt: LocalDateTime â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ + getters/setters()        â”‚
â”‚ + toString()               â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
         â–²
         â”‚ role type
         â”‚
    (ADMIN / KASIR)


â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚       Product             â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ - id: long                 â”‚
â”‚ - code: String [UNIQUE]    â”‚
â”‚ - name: String             â”‚
â”‚ - category: String         â”‚
â”‚ - price: double            â”‚
â”‚ - stock: int               â”‚
â”‚ - unit: String             â”‚
â”‚ - description: String      â”‚
â”‚ - active: boolean          â”‚
â”‚ - createdAt: LocalDateTime â”‚
â”‚ - updatedAt: LocalDateTime â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ + getters/setters()        â”‚
â”‚ + isLowStock(): boolean    â”‚
â”‚ + toString()               â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜


â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚      CartItem             â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ - product: Product         â”‚
â”‚ - quantity: int            â”‚
â”‚ - discountPercent: double  â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ + getSubtotal(): double    â”‚
â”‚ + getDiscountAmount(): double
â”‚ + getTotalAfterDiscount()  â”‚
â”‚ + setQuantity(qty)         â”‚
â”‚ + toString()               â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜


â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚    Transaction           â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ - id: long                 â”‚
â”‚ - code: String [UNIQUE]    â”‚
â”‚ - cashierUsername: String  â”‚
â”‚ - items: List<CartItem>    â”‚
â”‚ - subtotal: double         â”‚
â”‚ - discount: double         â”‚
â”‚ - tax: double              â”‚
â”‚ - total: double            â”‚
â”‚ - paymentMethod: String    â”‚
â”‚ - amountPaid: double       â”‚
â”‚ - change: double           â”‚
â”‚ - createdAt: LocalDateTime â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ + getters/setters()        â”‚
â”‚ + toString()               â”‚
â”‚ + toReceiptString()        â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜


â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚    PaymentMethod (I)      â”‚ â—„â”€â”€â”€ Interface
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ + validatePayment(): bool  â”‚
â”‚ + processPayment(): double â”‚
â”‚ + getMethodName(): String  â”‚
â”‚ + getReceiptDescription()  â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
    â–²        â–²        â–²
    â”‚        â”‚        â”‚
    â”‚        â”‚        â””â”€â”€â”€ QrisPayment
    â”‚        â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ EWalletPayment
    â”‚
    â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ CashPayment
    
    â†‘
    â””â”€â”€â”€ Strategy Pattern Implementation
```

---



# BAGIAN IV: IMPLEMENTASI OOP & DESIGN PATTERNS

## 4.1 Penerapan Konsep OOP

Agri-POS mengintegrasikan keempat pilar OOP:

| Konsep | Implementasi | Contoh |
|--------|--------------|--------|
| **Encapsulation** | Private fields + validasi di setter | `Product.setStock()` menolak nilai negatif |
| **Inheritance** | Interface implementation | `CashPayment` implements `PaymentMethod` |
| **Polymorphism** | Runtime method dispatch | `processPayment()` berbeda per payment type |
| **Abstraction** | DAO interfaces | `ProductDAO` menyembunyikan SQL dari service |

**Contoh Encapsulation:**
```java
public class Product {
    private int stock;
    
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock tidak boleh negatif");
        this.stock = stock;
    }
}
```

**Contoh Polymorphism:**
```java
// Service layer - polymorphic call
PaymentMethod method = PaymentMethodFactory.getPaymentMethod("Tunai");
double change = method.processPayment(total, paid); // Behavior sesuai concrete class
```

### Mengapa OOP Penting dalam Agri-POS?

Tanpa OOP, kode akan menjadi spaghetti code yang sulit di-maintain. Contoh masalah:
- Tanpa **Encapsulation**: Stock bisa diset negatif → data korup
- Tanpa **Polymorphism**: Setiap payment method butuh if-else panjang
- Tanpa **Abstraction**: Perubahan database mempengaruhi seluruh aplikasi

## 4.2 Design Patterns yang Diterapkan

| Pattern | Implementasi | Tujuan |
|---------|-------------|--------|
| **Singleton** | `DatabaseConnection`, `DiscountConfigService` | Single shared instance |
| **Strategy** | `PaymentMethod` (Cash, E-Wallet, QRIS) | Encapsulate algorithms |
| **Factory** | `PaymentMethodFactory` | Centralized object creation |
| **DAO** | `ProductDAO`, `UserDAO`, `TransactionDAO` | Database abstraction |
| **Observer** | JavaFX `ObservableList` | Real-time UI update |

**Singleton - DatabaseConnection:**
```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    private DatabaseConnection() { }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) instance = new DatabaseConnection();
        return instance;
    }
}
```

**Strategy - PaymentMethod:**
```java
public interface PaymentMethod {
    double processPayment(double total, double amountPaid);
    boolean validatePayment(double total, double amountPaid);
}

public class CashPayment implements PaymentMethod {
    public boolean validatePayment(double total, double paid) {
        return paid >= total; // Tunai: bayar >= total
    }
}

public class EWalletPayment implements PaymentMethod {
    public boolean validatePayment(double total, double paid) {
        return paid == total; // E-Wallet: harus pas
    }
}
```

---

# BAGIAN V: IMPLEMENTASI FITUR UTAMA

## 5.1 Fitur Manajemen Produk

| Fitur | Deskripsi | Implementasi |
|-------|-----------|---------------|
| **CRUD Produk** | Tambah, edit, hapus produk | `ProductService` + `JdbcProductDAO` |
| **Search** | Cari berdasarkan nama/kode | `findByCode()`, `findByName()` |
| **Filter Kategori** | Filter produk per kategori | ComboBox + `findByCategory()` |
| **Low Stock Alert** | Peringatan stok < 10 | `isLowStock()` method |

## 5.2 Fitur Transaksi

**Workflow Transaksi:**
```
Pilih Produk → Add to Cart → Set Quantity → Apply Diskon → Pilih Payment → Checkout → Print Struk
```

| Komponen | Fungsi |
|----------|--------|
| `CartService` | Kelola item keranjang, hitung total |
| `TransactionService` | Proses checkout, simpan ke database |
| `ReceiptService` | Generate struk transaksi |

**Kalkulasi Otomatis:**
```
Subtotal = Σ(harga × quantity)
Diskon = Subtotal × persentase_diskon
Pajak = (Subtotal - Diskon) × 10%
Total = Subtotal - Diskon + Pajak
```

## 5.3 Fitur Diskon

| Tipe Diskon | Contoh | Cara Kerja |
|-------------|--------|------------|
| **Persentase** | Diskon 10% | `subtotal × 0.10` |
| **Nominal** | Potongan Rp50.000 | `subtotal - 50000` |
| **Bulk** | Beli 5+ diskon 15% | Cek quantity, apply jika memenuhi |
| **Voucher** | Kode PROMO2026 | Input kode, validasi, apply |

**Sync Admin-Kasir:** Admin menambah diskon → `DiscountConfigService` (Singleton) → Kasir refresh → Diskon muncul di dropdown

---

# BAGIAN VI: TESTING DAN QUALITY ASSURANCE

## 6.1 Strategi Testing

Testing dilakukan dengan pendekatan **unit testing** menggunakan JUnit 5 dan Mockito:

| Jenis Test | Tools | Tujuan |
|------------|-------|--------|
| Unit Test | JUnit 5 | Test method individual |
| Mock Test | Mockito | Isolasi dependency (DAO) |
| Integration | JUnit + DB | Test koneksi database |

## 6.2 Contoh Unit Test

**Test Payment Validation:**
```java
@Test
void testCashPaymentValidation() {
    CashPayment payment = new CashPayment();
    assertTrue(payment.validatePayment(100000, 150000)); // paid >= total ✓
    assertFalse(payment.validatePayment(100000, 50000)); // paid < total ✗
    assertEquals(50000, payment.processPayment(100000, 150000)); // kembalian
}
```

**Test Cart Service:**
```java
@Test
void testCartServiceAddItem() {
    Product product = new Product("P001", "Beras", 50000);
    product.setStock(100);
    cartService.addItem(product, 2);
    assertEquals(100000, cartService.getCartTotal());
    assertEquals(1, cartService.getCartItems().size());
}

@Test
void testCartServiceInsufficientStock() {
    Product product = new Product("P001", "Beras", 50000);
    product.setStock(5);
    assertThrows(OutOfStockException.class, () -> cartService.addItem(product, 10));
}
```

## 6.3 Test Coverage

| Package | Class | Method | Line | Status |
|---------|-------|--------|------|--------|
| model | 100% | 95% | 92% | ✅ |
| service | 100% | 88% | 85% | ✅ |
| service.payment | 100% | 90% | 88% | ✅ |
| dao | 80% | 75% | 70% | ✅ |
| **Total** | **95%** | **87%** | **84%** | ✅ |

> Target coverage >70% tercapai untuk semua critical path.

## 6.4 Pembuktian Koneksi Database

**Test Koneksi:**
```java
@Test
void testDatabaseConnection() {
    boolean connected = DatabaseConnection.getInstance().testConnection();
    assertTrue(connected, "Database harus terhubung");
}

@Test
void testCRUDProduct() {
    Product p = new Product("TEST01", "Test Product", 10000);
    productDAO.insert(p);           // CREATE
    Product found = productDAO.findByCode("TEST01"); // READ
    assertNotNull(found);
    productDAO.delete("TEST01");    // DELETE
}
```

**Hasil Test:**
- ✅ Database terhubung ke PostgreSQL
- ✅ Tabel `users`: 3 rows (admin, kasir1, kasir2)
- ✅ Tabel `products`: 5 rows (data seed)
- ✅ CRUD operations berjalan normal

---

# BAGIAN VII: SCREENSHOT APLIKASI

| Screenshot | Deskripsi |
|------------|-----------|
| ![Login](/praktikum/week15-proyek-kelompok/screenshots/login%20agripos.png) | Login dengan role selection |
| ![Dashboard](/praktikum/week15-proyek-kelompok/screenshots/dasboard%20admin.png) | Dashboard Admin |
| ![Produk](/praktikum/week15-proyek-kelompok/screenshots/manajemen%20product%20admin.png) | Manajemen Produk |
| ![Diskon](/praktikum/week15-proyek-kelompok/screenshots/manajement%20diskon%20admin.png) | Manajemen Diskon |
| ![Transaksi](/praktikum/week15-proyek-kelompok/screenshots/transaksi%20kasir.png) | Transaksi Kasir |
| ![Laporan](/praktikum/week15-proyek-kelompok/screenshots/Laporan%20Penjualan(admin).jpg) | Laporan Penjualan |
| ![Riwayat](/praktikum/week15-proyek-kelompok/screenshots/Riwayat%20Transaksi%20(kasir).jpg) | Riwayat Transaksi |

---

# BAGIAN VII-B: ANALISIS KUALITAS DAN KETERPADUAN

## Evaluasi Keterpaduan Sistem

| Aspek | Evaluasi | Rating |
|-------|----------|--------|
| **OOP Integration** | 4 pilar terintegrasi dengan baik |  |
| **Database Integration** | JDBC + PreparedStatement + DAO |  |
| **GUI Integration** | JavaFX + ObservableList + MVC |  |
| **Design Patterns** | Singleton, Strategy, Factory, DAO |  |

**Alur Integrasi:**
```
GUI (JavaFX)  Controller  Service  DAO  Database (PostgreSQL)
                                                |
      Data Response 
```

---

# BAGIAN VIII: KESIMPULAN

Proyek Agri-POS berhasil mengintegrasikan seluruh konsep Pemrograman Berorientasi Objek (OOP), database, dan GUI dalam satu sistem Point of Sale yang fungsional, maintainable, dan scalable.

**Pencapaian Pembelajaran:**
- ✅ **Konsep OOP** - Encapsulation, Inheritance, Polymorphism, Abstraction terintegrasi dalam arsitektur sistem
- ✅ **Design Patterns** - Singleton, Strategy, Factory, DAO diterapkan sesuai kebutuhan
- ✅ **Layered Architecture** - 5 layer terpisah dengan clean separation of concerns
- ✅ **Database Integration** - PostgreSQL + JDBC + PreparedStatement untuk keamanan
- ✅ **GUI** - JavaFX dengan MVC pattern dan responsive design
- ✅ **Testing** - JUnit 5 + Mockito dengan coverage 84%

Pengalaman mengembangkan proyek ini memberikan pemahaman praktis tentang pengembangan aplikasi enterprise-level dengan arsitektur yang baik dan dapat di-maintain untuk pengembangan lebih lanjut.

---

# BAGIAN IX: LAMPIRAN PENDUKUNG

## A. Slide Presentasi
- **Google Slides**: [Link Presentasi](https://docs.google.com/presentation/d/YOUR_PRESENTATION_ID)

## B. Video Demo
- **YouTube**: [Demo Agri-POS](https://youtube.com/watch?v=YOUR_VIDEO_ID)

## C. Manual Book Penggunaan Aplikasi

---

### 📘 MANUAL BOOK AGRI-POS
### Sistem Point of Sale untuk Toko Pertanian
**Versi 1.0 | Januari 2026**

---

### DAFTAR ISI

1. [Pendahuluan](#c1-pendahuluan)
2. [Persyaratan Sistem](#c2-persyaratan-sistem)
3. [Panduan Instalasi](#c3-panduan-instalasi)
4. [Memulai Aplikasi (Login)](#c4-memulai-aplikasi)
5. [Panduan Kasir](#c5-panduan-kasir)
6. [Panduan Admin](#c6-panduan-admin)
7. [FAQ & Troubleshooting](#c7-faq--troubleshooting)

---

### C.1 Pendahuluan

#### Apa itu Agri-POS?

**Agri-POS** adalah aplikasi Point of Sale (POS) desktop yang dirancang khusus untuk toko pertanian. Aplikasi ini membantu proses penjualan, manajemen stok, dan pencatatan transaksi secara digital dan terintegrasi.

#### Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| 🛒 **Transaksi Penjualan** | Proses penjualan cepat dengan berbagai metode pembayaran |
| 📦 **Manajemen Produk** | Kelola data produk, kategori, dan stok |
| 🎁 **Sistem Diskon** | Berbagai jenis diskon (persentase, nominal, voucher) |
| 📈 **Laporan Penjualan** | Laporan harian dan periodik |
| 👥 **Multi-User** | Dukungan role Admin dan Kasir |
| 🧾 **Cetak Struk** | Struk digital untuk setiap transaksi |

#### Target Pengguna

- **Admin**: Pemilik toko atau manajer yang mengelola produk, diskon, dan melihat laporan
- **Kasir**: Staff yang melakukan transaksi penjualan sehari-hari

---

### C.2 Persyaratan Sistem

#### Spesifikasi Minimum

| Komponen | Minimum | Rekomendasi |
|----------|---------|-------------|
| **Sistem Operasi** | Windows 10 / Linux | Windows 11 / Ubuntu 22.04 |
| **RAM** | 4 GB | 8 GB |
| **Penyimpanan** | 500 MB free space | 1 GB free space |
| **Resolusi Layar** | 1366 x 768 | 1920 x 1080 |

#### Software yang Dibutuhkan

| Software | Versi | Fungsi |
|----------|-------|--------|
| Java JDK | 17+ | Runtime aplikasi |
| Apache Maven | 3.8+ | Build tool |
| PostgreSQL | 13+ | Database server |
| Git | Latest | Version control |

#### Verifikasi Instalasi

Buka terminal/command prompt dan jalankan perintah berikut untuk memastikan software terinstall:

```bash
java -version      # Output: openjdk version "17.x.x"
mvn -version       # Output: Apache Maven 3.8.x
psql --version     # Output: psql (PostgreSQL) 13.x
git --version      # Output: git version x.x.x
```

---

### C.3 Panduan Instalasi

#### Langkah 1: Download Source Code

```bash
# Clone repository dari GitHub
git clone https://github.com/YOUR_USERNAME/agripos.git

# Masuk ke folder project
cd agripos
```

#### Langkah 2: Setup Database PostgreSQL

**Windows (Command Prompt):**
```bash
# Login ke PostgreSQL
psql -U postgres

# Buat database baru
CREATE DATABASE agripos;

# Pindah ke database agripos
\c agripos

# Jalankan script schema (membuat tabel)
\i sql/schema.sql

# Jalankan script seed (data awal)
\i sql/seed.sql

# Keluar dari psql
\q
```

**Linux/Mac:**
```bash
sudo -u postgres psql -c "CREATE DATABASE agripos"
sudo -u postgres psql -d agripos -f sql/schema.sql
sudo -u postgres psql -d agripos -f sql/seed.sql
```

#### Langkah 3: Konfigurasi Koneksi Database

Edit file `src/main/java/com/upb/agripos/util/DatabaseConnection.java`:

```java
// Sesuaikan dengan konfigurasi PostgreSQL Anda
private static final String URL = "jdbc:postgresql://localhost:5432/agripos";
private static final String USER = "postgres";       // Username PostgreSQL Anda
private static final String PASS = "password123";    // Password PostgreSQL Anda
```

#### Langkah 4: Build dan Jalankan Aplikasi

```bash
# Compile project dengan Maven
mvn clean compile

# Jalankan aplikasi
mvn javafx:run
```

#### ✅ Tanda Instalasi Berhasil

Jika berhasil, akan muncul jendela login Agri-POS.

---

### C.4 Memulai Aplikasi (Login)

#### Tampilan Login

Saat aplikasi dibuka, Anda akan melihat halaman login dengan:

| Field | Keterangan |
|-------|------------|
| **Username** | Nama pengguna yang terdaftar |
| **Password** | Kata sandi pengguna |
| **Role** | Pilih peran: Admin atau Kasir (toggle button) |

#### Akun Default

| Role | Username | Password | Akses |
|------|----------|----------|-------|
| 👔 Admin | `admin` | `admin123` | Full access |
| 🏪 Kasir 1 | `kasir1` | `kasir123` | Transaksi & Riwayat |
| 🏪 Kasir 2 | `kasir2` | `kasir123` | Transaksi & Riwayat |

#### Cara Login

1. Buka aplikasi Agri-POS
2. Masukkan **Username** (contoh: `kasir1`)
3. Masukkan **Password** (contoh: `kasir123`)
4. Klik tombol **Role** yang sesuai (Admin/Kasir)
5. Klik tombol **🔐 Login**

> ⚠️ **Penting**: Pastikan role yang dipilih sesuai dengan akun. Admin tidak bisa login sebagai Kasir dan sebaliknya.

#### Cara Logout

1. Klik tombol **🚪 Logout** di pojok kanan atas
2. Anda akan kembali ke halaman login

---

### C.5 Panduan Kasir

#### 5.1 Tampilan Utama Kasir

Setelah login sebagai Kasir, Anda akan melihat tampilan dengan 2 tab:
- 🛒 **Transaksi Baru** - Untuk membuat penjualan
- 📋 **Riwayat Transaksi** - Melihat transaksi sebelumnya

---

#### 5.2 Membuat Transaksi Baru

**Langkah-langkah Lengkap:**

| Step | Aksi | Cara |
|------|------|------|
| 1 | **Buka tab Transaksi** | Klik tab 🛒 Transaksi Baru |
| 2 | **Cari produk** | Ketik nama/kode di search box ATAU pilih kategori dari dropdown |
| 3 | **Tambah ke keranjang** | Klik produk yang diinginkan, produk akan masuk ke keranjang |
| 4 | **Atur jumlah** | Ubah quantity dengan tombol +/- atau ketik angka langsung |
| 5 | **Hapus item (jika perlu)** | Klik tombol ❌ di samping item |
| 6 | **Pilih diskon (opsional)** | Pilih dari dropdown diskon ATAU masukkan kode voucher |
| 7 | **Pilih metode bayar** | Klik salah satu: Tunai / E-Wallet / QRIS |
| 8 | **Input nominal bayar** | Masukkan jumlah uang yang diterima dari pelanggan |
| 9 | **Proses checkout** | Klik tombol **✅ Checkout** |
| 10 | **Cetak struk** | Klik **🖨️ Cetak Struk** atau **Selesai** |

---

**Detail Tampilan Keranjang:**

```
┌─────────────────────────────────────────────────────┐
│ KERANJANG BELANJA                                   │
├──────────────────┬──────┬───────────┬───────────────┤
│ Nama Produk      │ Qty  │ Harga     │ Subtotal      │
├──────────────────┼──────┼───────────┼───────────────┤
│ Beras Premium    │ [2]  │ Rp 50.000 │ Rp 100.000    │
│ Pupuk Urea       │ [5]  │ Rp 12.500 │ Rp  62.500    │
│ Bibit Jagung     │ [3]  │ Rp  8.000 │ Rp  24.000    │
├──────────────────┴──────┴───────────┼───────────────┤
│                          Subtotal   │ Rp 186.500    │
│                          Diskon 5%  │ Rp  -9.325    │
│                          Pajak 10%  │ Rp  17.718    │
├─────────────────────────────────────┼───────────────┤
│                          TOTAL      │ Rp 194.893    │
└─────────────────────────────────────┴───────────────┘
```

---

**Metode Pembayaran:**

| Metode | Cara Penggunaan | Kembalian |
|--------|-----------------|-----------|
| 💵 **Tunai** | Input nominal ≥ total | Ya, dihitung otomatis |
| 📱 **E-Wallet** | Input nominal = total (OVO, GoPay, Dana) | Tidak ada |
| 📷 **QRIS** | Scan QR code, konfirmasi pembayaran | Tidak ada |

---

**Menerapkan Diskon:**

| Tipe Diskon | Cara | Contoh |
|-------------|------|--------|
| **Diskon Umum** | Pilih dari dropdown "Pilih Diskon" | Diskon 5%, 10% |
| **Diskon Member** | Pilih dari dropdown | Member 10% |
| **Voucher** | Ketik kode di field "Kode Voucher" → Klik "Terapkan" | PROMO50K |
| **Bulk Discount** | Otomatis jika beli ≥ jumlah tertentu | Beli 5+ dapat 15% |

> 💡 **Tips**: Klik tombol **🔄 Refresh Diskon** untuk memperbarui daftar diskon terbaru dari Admin.

---

#### 5.3 Melihat Riwayat Transaksi

**Langkah-langkah:**

1. Klik tab **📋 Riwayat Transaksi**
2. Gunakan filter tanggal untuk mencari transaksi tertentu:
   - **Tanggal Tunggal**: Pilih satu tanggal
   - **Periode**: Pilih tanggal mulai dan akhir
3. Klik baris transaksi untuk melihat **detail lengkap**
4. Klik **🖨️ Cetak Ulang** untuk mencetak struk lagi

**Informasi yang Ditampilkan:**

| Kolom | Keterangan |
|-------|------------|
| ID Transaksi | Kode unik transaksi (TRX-00001) |
| Tanggal | Tanggal dan waktu transaksi |
| Total | Jumlah total pembayaran |
| Metode Bayar | Tunai / E-Wallet / QRIS |
| Kasir | Username kasir yang melayani |

---

### C.6 Panduan Admin

#### 6.1 Tampilan Utama Admin

Setelah login sebagai Admin, Anda akan melihat tampilan dengan 4 tab:
- 📊 **Dashboard** - Statistik dan overview bisnis
- 📦 **Manajemen Produk** - CRUD produk
- 🎁 **Manajemen Diskon** - Kelola diskon
- 📈 **Laporan Penjualan** - Lihat dan export laporan

---

#### 6.2 Dashboard

Dashboard menampilkan ringkasan bisnis secara real-time:

| Widget | Deskripsi |
|--------|-----------|
| **💰 Total Transaksi Hari Ini** | Jumlah transaksi dalam 24 jam terakhir |
| **📈 Revenue Hari Ini** | Total pendapatan (setelah diskon & pajak) |
| **📦 Items Terjual** | Jumlah unit produk yang terjual |
| **⚠️ Low Stock Alert** | Produk dengan stok < 10 unit |
| **🏆 Top 5 Produk** | Produk terlaris hari ini |

---

#### 6.3 Manajemen Produk

**A. Menambah Produk Baru**

| Step | Aksi |
|------|------|
| 1 | Klik tab **📦 Manajemen Produk** |
| 2 | Klik tombol **➕ Tambah Produk** |
| 3 | Isi form: |
| | • **Kode Produk**: Kode unik (contoh: PRD001) |
| | • **Nama Produk**: Nama lengkap (contoh: Beras Premium) |
| | • **Kategori**: Pilih dari dropdown (Beras, Pupuk, Bibit, dll) |
| | • **Harga**: Harga jual per unit (contoh: 50000) |
| | • **Stok**: Jumlah stok awal (contoh: 100) |
| | • **Satuan**: Unit satuan (kg, liter, pack, pcs) |
| 4 | Klik tombol **💾 Simpan** |

**B. Mengedit Produk**

| Step | Aksi |
|------|------|
| 1 | Klik baris produk yang ingin diedit di tabel |
| 2 | Data produk akan muncul di form sebelah kanan |
| 3 | Ubah data yang diperlukan |
| 4 | Klik tombol **🔄 Update** |

**C. Menghapus Produk**

| Step | Aksi |
|------|------|
| 1 | Klik baris produk yang ingin dihapus |
| 2 | Klik tombol **🗑️ Hapus** |
| 3 | Konfirmasi penghapusan pada dialog |

> ⚠️ **Perhatian**: Produk yang sudah ada dalam transaksi tidak dapat dihapus.

**D. Mencari Produk**

- Gunakan **search box** di atas tabel
- Ketik nama produk atau kode
- Hasil filter tampil secara real-time

---

#### 6.4 Manajemen Diskon

**Tipe Diskon yang Tersedia:**

| Tipe | Deskripsi | Contoh |
|------|-----------|--------|
| **PERCENTAGE** | Potongan persentase dari total | 10% off |
| **NOMINAL** | Potongan nominal tetap | Rp 50.000 off |
| **BULK** | Diskon jika beli jumlah tertentu | Beli 10+, diskon 15% |
| **VOUCHER** | Diskon dengan kode promo | Kode: AGRI2026 |

**A. Menambah Diskon Baru**

| Step | Aksi |
|------|------|
| 1 | Klik tab **🎁 Manajemen Diskon** |
| 2 | Klik tombol **➕ Tambah Diskon** |
| 3 | Isi form: |
| | • **Nama Diskon**: Nama deskriptif (Diskon Lebaran) |
| | • **Kode**: Kode unik untuk voucher (LEBARAN25) |
| | • **Tipe**: Pilih jenis diskon |
| | • **Nilai**: Persentase (10) atau Nominal (50000) |
| | • **Min. Pembelian**: Minimal belanja (opsional) |
| | • **Min. Item**: Minimal item untuk bulk discount |
| 4 | Klik **💾 Simpan** |

**B. Mengaktifkan/Menonaktifkan Diskon**

- Klik **toggle switch** di kolom "Status" untuk mengubah status
- ✅ Aktif = Diskon muncul di tampilan Kasir
- ❌ Nonaktif = Diskon tidak tersedia untuk Kasir

> 💡 **Tips**: Perubahan diskon langsung tersinkron ke semua Kasir secara real-time.

---

#### 6.5 Laporan Penjualan

**Jenis Laporan:**

| Jenis | Deskripsi | Cara Generate |
|-------|-----------|---------------|
| **Laporan Harian** | Transaksi pada tanggal tertentu | Pilih tanggal → Klik "Generate" |
| **Laporan Periode** | Transaksi dalam rentang tanggal | Pilih start & end date → Klik "Generate" |
| **Laporan per Produk** | Penjualan per item produk | Pilih tab "Per Produk" → Generate |

**Langkah Generate Laporan:**

1. Klik tab **📈 Laporan Penjualan**
2. Pilih **Jenis Laporan** dari dropdown
3. Pilih **Tanggal** atau **Periode**
4. Klik **📊 Generate Laporan**
5. Laporan ditampilkan di area bawah
6. (Opsional) Klik **📥 Export** untuk download

---

### C.7 FAQ & Troubleshooting

#### ❓ Frequently Asked Questions

**Q1: Lupa password, bagaimana cara reset?**
> Hubungi Admin untuk mereset password. Admin dapat mengubah password melalui database.

**Q2: Transaksi tidak tersimpan, apa yang harus dilakukan?**
> Pastikan koneksi database aktif. Cek apakah PostgreSQL service berjalan. Restart aplikasi jika perlu.

**Q3: Struk tidak tercetak?**
> Pastikan printer terhubung dan driver terinstall. Cek setting printer default di sistem operasi.

**Q4: Produk tidak muncul di pencarian Kasir?**
> Pastikan produk memiliki stok > 0 dan status aktif. Admin dapat mengecek di Manajemen Produk.

**Q5: Diskon tidak terapply?**
> Cek apakah diskon masih aktif. Klik "Refresh Diskon" untuk memperbarui daftar.

---

#### 🔧 Troubleshooting

**Error: "Database Connection Failed"**
```
Solusi:
1. Pastikan PostgreSQL service berjalan
   - Windows: Services → PostgreSQL → Start
   - Linux: sudo systemctl start postgresql

2. Cek konfigurasi di DatabaseConnection.java
   - URL, Username, Password harus sesuai

3. Pastikan database 'agripos' sudah dibuat
```

**Error: "Login Failed - Invalid Credentials"**
```
Solusi:
1. Pastikan username dan password benar (case-sensitive)
2. Pastikan role yang dipilih sesuai dengan akun
3. Coba dengan akun default: admin/admin123 atau kasir1/kasir123
```

**Error: "JavaFX Runtime Components Missing"**
```
Solusi:
1. Pastikan menggunakan JDK 17 atau lebih tinggi
2. Jalankan dengan Maven: mvn javafx:run
3. Jangan jalankan langsung dengan java -jar
```

**Aplikasi Berjalan Lambat**
```
Solusi:
1. Tutup aplikasi lain yang tidak digunakan
2. Pastikan RAM tersedia minimal 4GB
3. Restart aplikasi
```

---

### 📞 Kontak & Dukungan

| Nama | Role | Email |
|------|------|-------|
| Indah Ruwahna A. | Project Lead | 240202866@student.upb.ac.id |
| Lia Lusianti | Frontend Dev | 240202869@student.upb.ac.id |
| Fikianto | Backend Dev | 240202899@student.upb.ac.id |
| Rizal Ramadhani | QA & Docs | 240202883@student.upb.ac.id |

---

**📘 END OF MANUAL BOOK**

*Agri-POS v1.0 - © 2026 Tim Pengembang UPB*

---

## D. GitHub Repository
- **URL**: [https://github.com/YOUR_USERNAME/agripos](https://github.com/YOUR_USERNAME/agripos)

## E. Bukti Implementasi

### Potongan Kode Inti

**Singleton Pattern:**
```java
public static synchronized DatabaseConnection getInstance() {
    if (instance == null) instance = new DatabaseConnection();
    return instance;
}
```

**Strategy Pattern:**
```java
public interface PaymentMethod {
    double processPayment(double total, double amountPaid);
}
```

**DAO Pattern:**
```java
public interface ProductDAO {
    void insert(Product product);
    List<Product> findAll();
}
```

## F. Pembagian Peran Tim

| Nama | NIM | Peran | Kontribusi |
|------|-----|-------|------------|
| Indah Ruwahna A. | 240202866 | Project Lead | Arsitektur, DAO, Service (30%) |
| Lia Lusianti | 240202869 | Frontend | UI/UX JavaFX (25%) |
| Fikianto | 240202899 | Backend | Payment, Business Logic (25%) |
| Rizal Ramadhani | 240202883 | QA & Docs | Testing, Documentation (20%) |

---

** END OF DOCUMENT**
