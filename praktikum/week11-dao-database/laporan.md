# Laporan Praktikum Minggu 11
Topik: Data Access Object (DAO) dan CRUD Database dengan JDBC

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Setelah mengikuti praktikum ini, mahasiswa mampu:

1. Menjelaskan konsep Data Access Object (DAO) dalam pengembangan aplikasi OOP.
2. Menghubungkan aplikasi Java dengan basis data menggunakan JDBC.
3. Mengimplementasikan operasi CRUD (Create, Read, Update, Delete) secara lengkap.
4. Mengintegrasikan DAO dengan class aplikasi OOP sesuai prinsip desain yang baik.
---

## Dasar Teori
1. Data Access Object (DAO) Pattern
Data Access Object merupakan design pattern yang bertujuan memisahkan logika pengaksesan data dari logika bisnis aplikasi. Dengan pola ini, perubahan pada teknologi penyimpanan data (misalnya dari PostgreSQL ke MySQL) tidak akan berdampak langsung pada kode logika bisnis utama.
Keuntungan penerapan DAO:

   Struktur kode menjadi lebih rapi dan terorganisir dengan baik
Mengurangi ketergantungan (loose coupling) antara komponen aplikasi dan database
Memudahkan proses testing karena dapat menggunakan mock implementation
Mendukung skalabilitas dan maintainability aplikasi

2. JDBC (Java Database Connectivity)
JDBC adalah API standar Java yang menyediakan cara untuk berinteraksi dengan berbagai jenis database relasional. Dalam praktikum ini, JDBC digunakan untuk menghubungkan aplikasi dengan PostgreSQL.
Komponen penting dalam JDBC:

   DriverManager: Mengelola driver database dan membuat koneksi
Connection: Merepresentasikan sesi koneksi aktif ke database
PreparedStatement: Digunakan untuk mengeksekusi SQL query yang telah di-precompile
ResultSet: Objek yang menampung hasil dari query SELECT

3. PreparedStatement vs Statement
PreparedStatement lebih direkomendasikan dibanding Statement biasa karena:

   Melindungi aplikasi dari serangan SQL Injection
Lebih efisien untuk query yang dijalankan berulang kali karena sudah di-precompile
Secara otomatis menangani escape character untuk string input

---

## Langkah Praktikum

1.  Persiapan Database
Membuat database agripos dan tabel products di PostgreSQL dengan struktur yang telah ditentukan. Tabel products memiliki 4 kolom: code (PK), name, price, dan stock.
2. Pembuatan Model Class
Membuat class Product.java di package com.upb.agripos.model yang berfungsi sebagai representasi objek produk. Class ini menggunakan prinsip encapsulation dengan atribut private dan method accessor public.
3. Definisi Interface DAO
Membuat interface ProductDAO.java yang mendefinisikan method-method untuk operasi CRUD. Interface ini berfungsi sebagai kontrak yang harus diimplementasikan oleh class konkrit.
4. Implementasi Concrete DAO Class
Membuat class ProductDAOImpl.java yang mengimplementasikan interface ProductDAO. Semua method menggunakan PreparedStatement untuk keamanan dan performa optimal.
5. Testing dan Integrasi
Membuat class MainDAOTest.java untuk menguji seluruh fungsionalitas CRUD. Program menjalankan skenario: Insert produk → Update data → Read data → Delete data.
6. Version Control
Melakukan commit dengan pesan deskriptif:

---

## Kode Program

**ProdukDAO.java**

```java
package com.upb.agripos.dao;

import java.util.List;
import com.upb.agripos.model.Product;

public interface ProductDAO {
    void insert(Product product) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;
    void update(Product product) throws Exception;
    void delete(String code) throws Exception;
}
```
**ProductDAOImpl.java**

```java
package com.upb.agripos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {

    private final Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) throws Exception {
        String sql = "INSERT INTO products(code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        }
        return list;
    }

    @Override
    public void update(Product p) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getCode());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String code) throws Exception {
        String sql = "DELETE FROM products WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }
}
```

**Product.java**

```java
package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}
```
**Products.sql**

```java
CREATE TABLE products (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE PRECISION,
    stock INT
);
```

**MainDAOTest.java**
```java

package com.upb.agripos;

import java.sql.Connection;
import java.sql.DriverManager;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;

public class MainDAOTest {
    public static void main(String[] args) {
        try {
            System.out.println("====================================");
            System.out.println("MENGHUBUNGKAN KE DATABASE...");
            System.out.println("====================================");

            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/agripos",
                "postgres",
                "1234"
            );

            System.out.println("KONEKSI DATABASE BERHASIL");
            System.out.println("Database : agripos");
            System.out.println("====================================\n");

            ProductDAO dao = new ProductDAOImpl(conn);

            // INSERT
            System.out.println("[INSERT]");
            dao.insert(new Product("P01", "Pupuk Organik", 25000, 10));
            System.out.println("Produk berhasil ditambahkan\n");

            // UPDATE
            System.out.println("[UPDATE]");
            dao.update(new Product("P01", "Pupuk Organik Premium", 30000, 8));
            System.out.println("Produk berhasil diperbarui\n");

            // READ
            System.out.println("[READ]");
            Product p = dao.findByCode("P01");
            if (p != null) {
                System.out.println("Kode  : " + p.getCode());
                System.out.println("Nama  : " + p.getName());
                System.out.println("Harga : " + p.getPrice());
                System.out.println("Stok  : " + p.getStock());
            }

            // DELETE
            System.out.println("\n[DELETE]");
            dao.delete("P01");
            System.out.println("Produk berhasil dihapus\n");

            conn.close();

            System.out.println("====================================");
            System.out.println("PROGRAM SELESAI");
            System.out.println("====================================");

        } catch (Exception e) {
            System.out.println("KONEKSI DATABASE GAGAL");
            e.printStackTrace();
        }
    }
}

```


---

## Hasil Eksekusi
 
![Screenshot hasil](/praktikum/week11-dao-database/screenshots/hasil%20week11.png)

---

## Analisis
1. Alur Kerja Program
Program dimulai dengan membangun koneksi ke database PostgreSQL melalui DriverManager.getConnection(). Setelah koneksi terbentuk, objek ProductDAOImpl diinisialisasi dengan meng-inject koneksi tersebut. Class MainDAOTest kemudian mengorkestrasi seluruh operasi CRUD secara berurutan:

   CREATE: Menyimpan produk baru ke database
   UPDATE: Mengubah informasi produk yang sudah ada
   READ: Mengambil data produk baik secara individual maupun keseluruhan
   DELETE: Menghapus produk dari database

   Setiap operasi database dibungkus dalam try-with-resources untuk memastikan resource seperti PreparedStatement dan ResultSet otomatis ditutup setelah digunakan.

2. Perbandingan dengan Praktikum Sebelumnya
Praktikum Minggu Sebelumnya:

   Data disimpan dalam struktur data Collection (ArrayList/HashMap) yang berada di memori
   Data bersifat volatile - hilang ketika aplikasi ditutup
   Tidak ada persistensi data
   Lebih sederhana namun tidak realistis untuk aplikasi production

   Praktikum Minggu Ini:

   Data disimpan dalam database relasional PostgreSQL
   Data bersifat persistent - tetap ada meski aplikasi restart
   Memerlukan penanganan exception yang lebih kompleks (SQLException)
   Lebih mendekati skenario aplikasi dunia nyata

3. Tantangan dan Penyelesaian
Masalah 1: Error "PSQLException: ERROR: duplicate key value violates unique constraint"

   Penyebab: Mencoba insert produk dengan kode yang sudah ada di database
   Solusi: Menambahkan pengecekan dengan findByCode() sebelum insert, atau melakukan delete terlebih dahulu untuk testing

   Masalah 2: Koneksi database tidak tertutup dengan baik

   Penyebab: Lupa menutup connection setelah operasi selesai
   Solusi: Menggunakan try-with-resources atau memanggil connection.close() di blok finally

   Masalah 3: ResultSet kosong saat query SELECT

   Penyebab: Query WHERE clause tidak match dengan data di database
   Solusi: Melakukan debugging dengan print statement dan verifikasi data di database menggunakan pgAdmin

4. Penerapan Prinsip SOLID

   Single Responsibility: Setiap class memiliki tanggung jawab tunggal (Product untuk model, DAO untuk akses data)
   Open/Closed: Interface ProductDAO terbuka untuk extension (bisa ada implementasi lain), tertutup untuk modification
   Dependency Inversion: MainDAOTest bergantung pada abstraksi (interface) bukan konkret class
---

## Kesimpulan

Praktikum ini berhasil mengimplementasikan DAO Pattern sebagai solusi untuk memisahkan concern antara logika bisnis dan akses data. Penggunaan JDBC dengan PreparedStatement memastikan komunikasi yang aman dan efisien dengan database PostgreSQL.
Pembelajaran Utama:

1. DAO pattern meningkatkan modularitas dan fleksibilitas aplikasi
2. PreparedStatement adalah best practice untuk menghindari SQL Injection
3. Persistensi data membuat aplikasi lebih robust dan production-ready
4. Penanganan exception yang proper sangat penting dalam operasi database
---

