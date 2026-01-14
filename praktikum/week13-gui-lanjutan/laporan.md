# Laporan Praktikum Minggu 13 

Topik: GUI Lanjutan JavaFX (TableView dan Lambda Expression)

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Setelah mengikuti praktikum ini, mahasiswa mampu:
1. Menampilkan data menggunakan TableView JavaFX.
2. Mengintegrasikan koleksi objek dengan GUI.
3. Menggunakan lambda expression untuk event handling.
4. Menghubungkan GUI dengan DAO secara penuh.
5. Membangun antarmuka GUI Agri-POS yang lebih interaktif.
---

## Dasar Teori

1. JavaFX TableView

   TableView merupakan komponen JavaFX yang digunakan untuk menampilkan data dalam bentuk tabel. TableView bekerja dengan koleksi data berbasis ObservableList dan mendukung pengikatan data ke kolom menggunakan PropertyValueFactory.

2. Lambda Expression

   Lambda expression adalah fitur Java yang memungkinkan penulisan fungsi anonim secara ringkas. Pada JavaFX, lambda expression banyak digunakan untuk menangani event seperti klik tombol, sehingga kode menjadi lebih sederhana dan mudah dibaca.

3. Arsitektur MVC + DAO

   Aplikasi Agri-POS menggunakan arsitektur:

   - Model : Representasi data (Product)

   - View : Antarmuka pengguna (ProductTableView)

   - Controller : Penghubung View dan Service

   - Service & DAO : Logika bisnis dan akses database

   Pendekatan ini mendukung prinsip Single Responsibility dan Dependency Inversion Principle (DIP).

---

## Langkah Praktikum
Langkah-langkah yang dilakukan dalam praktikum ini meliputi:

1. Persiapan Project
Menyusun dan mengonfigurasi file pom.xml untuk kebutuhan JavaFX dan koneksi database PostgreSQL.

2. Pengembangan Backend
Mengimplementasikan kelas ProductDAO, ProductDAOImpl, dan ProductService sebagai pengelola akses data.

3. Pembuatan Antarmuka (View)
Merancang ProductTableView yang berisi tabel daftar produk serta tombol aksi.

4. Implementasi Controller
Mengembangkan ProductController sebagai penghubung antara View dan Service, serta menangani aksi pengguna menggunakan lambda expression.

5. Integrasi Aplikasi
Menggabungkan seluruh komponen aplikasi melalui kelas AppJavaFX.

6. Version Control
Melakukan commit dan push ke repository dengan pesan:

---

## Kode Program 

**Berikut adalah contoh implementasi lambda expression pada event tombol hapus produk di Controller:**

```java
// Event Handler: Hapus Produk
view.getBtnDelete().setOnAction(e -> {
    Product selectedProduct = view.getTable().getSelectionModel().getSelectedItem();
    if (selectedProduct != null) {
        try {
            service.deleteProduct(selectedProduct.getCode());
            loadData(); // Memperbarui data pada TableView
        } catch (Exception ex) {
            showAlert("Error", "Gagal menghapus produk: " + ex.getMessage());
        }
    } else {
        showAlert("Peringatan", "Silakan pilih produk yang akan dihapus.");
    }
});

```

---

## Hasil Eksekusi
  
![Screenshot hasil](/oop-202501-240202866/praktikum/week13-gui-lanjutan/screenshots/hasilweek13.png)

---

## Analisis dan Pembahasan

1. TableView JavaFX
Penggunaan TableView memungkinkan penyajian data yang lebih terstruktur dibandingkan ListView. Setiap kolom merepresentasikan atribut dari class Product.

2. Lambda Expression
Lambda expression mempersingkat kode event handling dan meningkatkan keterbacaan serta efisiensi kode.

3. Integrasi Database
Data yang ditampilkan bersifat dinamis karena langsung diambil dari database. Setiap perubahan data tercermin baik di UI maupun di database.

4. Kesesuaian dengan UML Bab 6
Alur interaksi View → Controller → Service → DAO telah sesuai dengan use case, activity diagram, dan sequence diagram yang telah dirancang.
---

## Kesimpulan
Berdasarkan hasil praktikum Minggu 13, dapat disimpulkan bahwa:

- Komponen TableView JavaFX berhasil digunakan untuk menampilkan data produk.

- Lambda expression mempermudah dan menyederhanakan penanganan event.

- Integrasi GUI dengan DAO melalui Service telah berjalan sesuai prinsip MVC dan SOLID.

- Aplikasi Agri-POS menjadi lebih responsif dan siap dikembangkan ke tahap selanjutnya.
---


## Analisis Implementasi GUI Lanjutan

| Aspek Analisis | Implementasi | Penjelasan |
|---------------|--------------|------------|
| TableView JavaFX | `TableView<Product>` | Digunakan untuk menampilkan data produk dalam bentuk tabel dengan kolom kode, nama, harga, dan stok. Lebih terstruktur dibandingkan ListView. |
| Binding Data | `PropertyValueFactory` | Setiap kolom TableView dihubungkan dengan atribut pada class Product sehingga data dapat ditampilkan otomatis. |
| Integrasi Database | DAO melalui Service | Data diambil dari database PostgreSQL menggunakan `ProductDAO.findAll()` melalui `ProductService`. |
| Lambda Expression | Event Handler Button | Lambda expression digunakan untuk menangani event tombol seperti hapus produk sehingga kode lebih ringkas dan mudah dibaca. |
| Arsitektur Aplikasi | MVC + DAO | View hanya menangani tampilan, sedangkan logika bisnis dan akses database berada pada Service dan DAO. |
| Prinsip SOLID | Dependency Inversion Principle | View tidak mengakses database secara langsung, melainkan melalui Controller dan Service. |
| Pengalaman Pengguna | UI Interaktif | TableView memudahkan pengguna memilih data dan memastikan aksi hapus dilakukan dengan validasi. |


## Traceability Bab 6 (UML) terhadap Implementasi GUI

| Artefak Bab 6 | Referensi | Interaksi GUI | Controller / Service | DAO | Dampak UI / DB |
|--------------|-----------|---------------|----------------------|-----|---------------|
| Use Case | UC-02 Lihat Daftar Produk | `loadData()` saat view dibuka | `ProductController.loadData()` → `ProductService.findAll()` | `ProductDAO.findAll()` | Data produk tampil pada TableView |
| Use Case | UC-03 Hapus Produk | Klik tombol Hapus | `ProductController.delete()` → `ProductService.delete(code)` | `ProductDAO.delete(code)` | Data terhapus di database dan UI diperbarui |
| Sequence Diagram | SD-02 Hapus Produk | Event klik tombol | View → Controller → Service | DAO → Database | Alur eksekusi sesuai sequence diagram |
