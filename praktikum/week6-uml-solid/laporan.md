# Laporan Praktikum Minggu 6 
Topik: [Bab 6 – Desain Arsitektur Sistem dengan UML dan Prinsip SOLID]

## Identitas
- Nama  : [INDAH RUWAHNA ANUGRAHENI]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Mahasiswa mampu:

1. Mahasiswa mampu mengidentifikasi kebutuhan sistem ke dalam diagram UML.
2. Mahasiswa mampu menggambar UML Class Diagram dengan relasi antar class yang tepat.
3. Mahasiswa mampu menjelaskan prinsip desain OOP (SOLID).
4. Mahasiswa mampu menerapkan minimal dua prinsip SOLID dalam kode program.

---

## Deskripsi Sistem Agri-POS
Agri-POS adalah aplikasi kasir digital yang dirancang untuk mendigitalisasi operasional toko pertanian (Saprodi). Sistem ini mengintegrasikan manajemen stok dengan proses transaksi secara real-time.

**Fitur Utama:**
   - Manajemen Inventaris: Kontrol stok otomatis untuk benih, pupuk, dan alat tani.
   
   - Transaksi Cepat: Pemrosesan checkout barang dengan kalkulasi total otomatis.

   - Dual-Payment: Mendukung pembayaran Tunai dan E-Wallet dengan fitur fallback (otomatis beralih ke tunai jika saldo e-wallet gagal).

**Hak Akses Pengguna (Role):**
   - Kasir: Menangani transaksi checkout dan cetak struk.

   - Admin: Mengelola data akun pengguna dan memantau laporan.

   - Gudang: Mengelola data produk (CRUD) dan memantau ketersediaan barang.

**Alur Kerja Singkat:**

   Kasir melakukan scanning barang → Sistem menghitung total belanja → Pelanggan membayar (Tunai/E-Wallet) → Jika sukses, stok berkurang otomatis → Sistem mencetak struk.


---

## 1. Desain Arsitektur UML

Desain arsitektur UML digunakan untuk memberikan gambaran menyeluruh tentang struktur dan perilaku sistem secara visual. Dalam proyek Agri-POS ini, arsitektur dirancang menggunakan standar industri untuk memastikan setiap kebutuhan fungsional (FR) terakomodasi dengan baik. Fokus utama desain ini adalah menciptakan sistem yang memiliki ketergantungan rendah (loose coupling) namun fungsi yang terfokus (high cohesion) melalui penerapan prinsip SOLID. Dengan demikian, sistem tidak hanya siap digunakan sekarang, tetapi juga mudah untuk dikembangkan di masa depan tanpa harus merombak struktur dasarnya.

Selanjutnya, empat diagram UML disusun sebagai berikut:

**ACTIVITY DIAGRAM (Alur Kerja Operasional)**

Memodelkan logika transaksi dari penyerahan barang hingga finalisasi.

- Multi-Swimlane: Membagi proses ke dalam empat zona tanggung jawab: Pelanggan, Kasir, POS System, dan Payment Service.

- Error Handling: Alur mencakup skenario "Saldo Kurang" pada pembayaran E-Wallet, di mana sistem akan secara otomatis mengalihkan (reroute) Kasir untuk meminta pembayaran tunai sebagai fallback.

  **Activity Diagram**
  ![alt text](/praktikum/week6-uml-solid/docs/uml_activity%20diagram.png)


**SEQUENCE DIAGRAM (Interaksi Antar-Objek)**

Menekankan pada aspek waktu dan urutan pesan selama tahap scanning, pembayaran, hingga finalisasi.

- Tahap Transaksi: Terbagi menjadi tiga fase utama: Scanning & Checkout, Pembayaran, dan Finalisasi.

- Logic Gate (Alt): Menggunakan blok alternative untuk menangani logika percabangan pembayaran (E-Wallet vs Tunai) secara dinamis.

  **Sequence Diagram**
  ![alt text](/praktikum/week6-uml-solid/docs/uml_sequence%20diagram.png)

**CLASS DIAGRAM (Struktur Arsitektur SOLID)**

Ini adalah cetak biru teknis yang menerapkan prinsip SOLID untuk skalabilitas.

- Dependency Inversion (DIP): CheckoutService tidak bergantung langsung pada database, melainkan pada interface ProductRepository.

- Open/Closed (OCP): Penggunaan PaymentFactory memungkinkan penambahan metode pembayaran baru tanpa mengubah kode pada CheckoutService.

  **Class Diagram**
  ![alt text](/praktikum/week6-uml-solid/docs/uml_class%20diagram.png)



**USE CASE DIAGRAM**

Diagram ini mendefinisikan batasan sistem dan interaksi pengguna.

- Generalisasi Pembayaran: Use case Bayar Tunai dan Bayar E-Wallet merupakan spesialisasi dari Proses Checkout.

- Relasi Dependency: Proses Checkout memiliki hubungan include dengan Cetak Struk, memastikan bukti transaksi selalu diterbitkan.

- Pemisahan Tugas: Aktor Gudang memiliki akses khusus untuk manajemen stok, memisahkan tanggung jawab dari fungsi penjualan Kasir.

   **Use Case Diagram**

   ![alt text](/praktikum/week6-uml-solid/docs/uml_usecase%20diagram.png)


---

## 2. Penerapan Prinsip SOLID

| Prinsip | Manifestasi dalam Agri-POS | Dampak Teknis |
| :--- | :--- | :--- |
| **S (SRP)** | Pemisahan tanggung jawab antara `ProductService` (logika bisnis) dan `ProductRepository` (akses data). | Meminimalisir risiko kesalahan saat terjadi perubahan struktur database. |
| **O (OCP)** | Penggunaan `PaymentFactory` dan interface `PaymentMethod` dalam proses pembuatan objek. | Memungkinkan penambahan metode pembayaran baru tanpa mengubah kode inti transaksi. |
| **L (LSP)** | Kelas `CashPayment` dan `EWalletPayment` dapat menggantikan interface `PaymentMethod` secara konsisten. | Menjamin stabilitas perilaku sistem terlepas dari jenis pembayaran yang dipilih. |
| **I (ISP)** | Interface didesain secara spesifik sehingga kelas hanya mengimplementasikan metode yang diperlukan. | Kode menjadi lebih efisien, modular, dan lebih mudah dikelola dalam jangka panjang. |
| **D (DIP)** | Modul tinggi `CheckoutService` bergantung pada abstraksi interface, bukan pada implementasi konkret. | Mempermudah pengujian (unit testing) dengan menggunakan data tiruan atau mock. |
 
---

## 3. Traceability Matrix (FR → Desain)

Tabel ini memetakan hubungan antara Kebutuhan Fungsional (FR) dengan representasi desain pada diagram UML:

| Kebutuhan Fungsional (FR) | Use Case Diagram | Activity/Sequence Diagram | Realisasi Kelas & Interface |
| :--- | :--- | :--- | :--- |
| **Manajemen Produk (CRUD)** | UC-Kelola Produk (CRUD) | - | `Product`, `ProductRepository`, `JdbcProductRepository` |
| **Transaksi Penjualan** | UC-Proses Checkout | Activity Checkout (Tahap Scanning) | `CheckoutService`, `Product` |
| **Pembayaran Tunai & E-Wallet** | UC-Bayar Tunai & UC-Bayar E-Wallet | Sequence Pembayaran (Blok Alt) | `PaymentMethod`, `CashPayment`, `EWalletPayment`, `PaymentFactory` |
| **Pencetakan Struk** | UC-Cetak Struk | Langkah akhir pada Activity & Sequence Diagram | `Receipt` (sebagai hasil kembalian `ProcessCheckout`) |
| **Manajemen Stok Gudang** | UC-Menambah/Mengurangi Barang Gudang | - | `Product` (metode `ReduceStok`) |
| **Otentikasi Pengguna** | UC-Login | - | Kelas pendukung User/Auth (Implisit) |


---
 
## 4. Quiz & Argumentasi Desain

1. Aggregation vs Composition:
   - Aggregation: Hubungan antara Store dan Kasir (Kasir tetap ada meskipun toko tutup).
   - Composition: Hubungan antara Product dan StockData. Jika data produk dihapus, data stok terkait secara logis harus ikut terhapus karena tidak memiliki arti mandiri.

2. Keunggulan OCP: Sistem menjadi "kebal" terhadap modifikasi kode lama. Saat Agri-POS ingin menambah fitur diskon atau loyalty member, kita hanya menambah kelas baru, menjaga stabilitas modul inti yang sudah stabil.

3. DIP & Testability: Tanpa DIP, kita butuh database asli untuk testing. Dengan DIP, kita bisa membuat "Fake Repository" untuk mengetes logika checkout dalam hitungan milidetik, meningkatkan kualitas kode melalui Continuous Integration.



---

## Kesimpulan

Perancangan Agri-POS ini telah memenuhi standar industri dengan menerapkan arsitektur yang Low Coupling dan High Cohesion. Penggunaan UML yang presisi memudahkan tim pengembang dalam memahami alur kerja, sementara penerapan prinsip SOLID menjamin sistem ini siap menghadapi ekspansi bisnis di masa depan tanpa perlu menulis ulang (refactor) kode secara besar-besaran.

---


