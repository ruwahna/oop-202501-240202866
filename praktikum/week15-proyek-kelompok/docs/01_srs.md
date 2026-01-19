# Software Requirement Specification (SRS)
## Agri-POS - Sistem Point of Sale Pertanian

### 1. Pendahuluan

#### 1.1 Tujuan Dokumen
Dokumen ini menjelaskan spesifikasi kebutuhan perangkat lunak untuk sistem Agri-POS, sebuah aplikasi Point of Sale (POS) berbasis desktop yang dirancang khusus untuk toko pertanian.

#### 1.2 Ruang Lingkup Sistem
Agri-POS adalah aplikasi desktop berbasis Java yang memungkinkan:
- Pengelolaan produk pertanian (CRUD)
- Transaksi penjualan dengan berbagai metode pembayaran
- Pencetakan struk pembelian
- Pembuatan laporan penjualan
- Kontrol akses berdasarkan peran pengguna

#### 1.3 Definisi dan Akronim
| Istilah | Definisi |
|---------|----------|
| POS | Point of Sale - Sistem titik penjualan |
| CRUD | Create, Read, Update, Delete |
| DAO | Data Access Object |
| DIP | Dependency Inversion Principle |
| GUI | Graphical User Interface |

### 2. Deskripsi Umum

#### 2.1 Perspektif Produk
Agri-POS adalah sistem standalone yang berjalan di desktop dengan koneksi ke database PostgreSQL lokal atau server.

#### 2.2 Fungsi Produk
1. Manajemen Produk
2. Transaksi Penjualan
3. Pembayaran Multi-Metode
4. Struk dan Laporan
5. Autentikasi dan Otorisasi

#### 2.3 Karakteristik Pengguna
| Peran | Deskripsi | Hak Akses |
|-------|-----------|-----------|
| Admin | Administrator sistem | Full access |
| Kasir | Operator transaksi | Transaksi & view laporan |

#### 2.4 Batasan
- Memerlukan Java Runtime Environment 21+
- Memerlukan koneksi ke PostgreSQL
- Single currency (Rupiah)

### 3. Kebutuhan Fungsional

#### FR-1: Manajemen Produk
| ID | Requirement | Priority |
|----|-------------|----------|
| FR-1.1 | Sistem harus dapat menampilkan daftar produk | High |
| FR-1.2 | Admin dapat menambah produk baru | High |
| FR-1.3 | Admin dapat mengubah data produk | High |
| FR-1.4 | Admin dapat menghapus produk | Medium |
| FR-1.5 | Sistem dapat mencari produk berdasarkan kata kunci | High |

#### FR-2: Transaksi Penjualan
| ID | Requirement | Priority |
|----|-------------|----------|
| FR-2.1 | Kasir dapat menambah item ke keranjang | High |
| FR-2.2 | Kasir dapat mengubah jumlah item | High |
| FR-2.3 | Kasir dapat menghapus item dari keranjang | High |
| FR-2.4 | Sistem menghitung subtotal, pajak, dan total | High |
| FR-2.5 | Sistem validasi stok sebelum checkout | High |

#### FR-3: Pembayaran
| ID | Requirement | Priority |
|----|-------------|----------|
| FR-3.1 | Sistem mendukung pembayaran tunai | High |
| FR-3.2 | Sistem mendukung pembayaran e-wallet | High |
| FR-3.3 | Sistem menghitung kembalian | High |
| FR-3.4 | Sistem dapat dikembangkan dengan metode baru (Strategy Pattern) | Medium |

#### FR-4: Struk dan Laporan
| ID | Requirement | Priority |
|----|-------------|----------|
| FR-4.1 | Sistem generate struk setelah pembayaran | High |
| FR-4.2 | Admin dapat melihat laporan harian | High |
| FR-4.3 | Admin dapat melihat laporan periode | Medium |

#### FR-5: Autentikasi
| ID | Requirement | Priority |
|----|-------------|----------|
| FR-5.1 | Sistem memerlukan login untuk akses | High |
| FR-5.2 | Sistem membedakan hak akses berdasarkan peran | High |
| FR-5.3 | Kasir hanya dapat melakukan transaksi | High |
| FR-5.4 | Admin dapat mengakses semua fitur | High |

### 4. Kebutuhan Non-Fungsional

#### NFR-1: Kinerja
- Waktu respons < 2 detik untuk operasi normal
- Mendukung hingga 10.000 produk

#### NFR-2: Keamanan
- Password tersimpan terenkripsi (future enhancement)
- Session timeout setelah 30 menit tidak aktif

#### NFR-3: Usability
- Interface intuitif dengan navigasi tab
- Pesan error yang informatif

#### NFR-4: Maintainability
- Arsitektur berlapis (Layered Architecture)
- Penerapan prinsip SOLID
- Dependency Injection untuk loose coupling

### 5. Antarmuka Sistem

#### 5.1 Antarmuka Pengguna
- Login Screen: Form username/password
- Main Screen: Tab-based navigation
- Product Tab: Tabel produk dengan form CRUD
- Transaction Tab: Keranjang + produk + checkout
- Report Tab: Date picker + report display

#### 5.2 Antarmuka Database
- JDBC Connection ke PostgreSQL
- Connection Pool: HikariCP (optional enhancement)

### 6. Acceptance Criteria

| ID | Kriteria | Status |
|----|----------|--------|
| AC-1 | Login dengan kredensial valid berhasil | ✅ |
| AC-2 | Login dengan kredensial invalid ditolak | ✅ |
| AC-3 | CRUD produk berfungsi untuk Admin | ✅ |
| AC-4 | Transaksi dapat diselesaikan | ✅ |
| AC-5 | Struk tergenerate setelah checkout | ✅ |
| AC-6 | Laporan menampilkan data yang benar | ✅ |
