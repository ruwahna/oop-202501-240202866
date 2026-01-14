# Laporan Praktikum Minggu 12
Topik: GUI Dasar JavaFX (Event-Driven Programming)

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Setelah mengikuti praktikum ini, mahasiswa mampu:
1. Menjelaskan konsep event-driven programming.
2. Membangun antarmuka grafis sederhana menggunakan JavaFX.
3. Membuat form input data produk.
4. Menampilkan daftar produk pada GUI.
5. Mengintegrasikan GUI dengan modul backend yang telah dibuat (DAO & Service).

---

## Dasar Teori
1. Event-Driven Programming
Event-Driven Programming adalah paradigma pemrograman di mana alur program ditentukan oleh event (kejadian) seperti klik mouse, input keyboard, atau aksi pengguna lainnya. Program tidak berjalan secara linear, melainkan menunggu event terjadi dan merespons dengan menjalankan event handler yang telah didefinisikan.
Karakteristik:

   Alur program bersifat reaktif, bukan sequential
Menggunakan callback atau handler untuk merespons event
Cocok untuk aplikasi yang membutuhkan interaksi user tinggi

2. JavaFX
JavaFX adalah platform untuk membuat aplikasi desktop dengan GUI modern. JavaFX menyediakan komponen UI yang kaya seperti Button, TextField, TableView, dan mendukung styling dengan CSS.
Komponen Utama:

   Stage: Window utama aplikasi
Scene: Container untuk elemen UI
Node: Elemen UI individual (Button, Label, TextField)
Layout Panes: Container untuk mengatur tata letak (VBox, HBox, GridPane)

3. Arsitektur MVC
Praktikum ini menerapkan pola Model-View-Controller (MVC) dengan layer tambahan:

   Model: Class Product sebagai representasi data
View: JavaFX components untuk tampilan
Controller: ProductController yang menghubungkan View dengan Service
Service: ProductService untuk business logic
DAO: ProductDAO untuk operasi database

4. Dependency Injection
Dependency Injection adalah teknik di mana dependency (seperti Service atau DAO) diberikan ke object yang membutuhkan, bukan dibuat di dalam object tersebut. Ini meningkatkan fleksibilitas dan memudahkan testing.

---

## Langkah Praktikum
1. Setup Project: Memastikan library JavaFX terkonfigurasi dengan benar pada project.
2. Membuat Layout GUI: Membuat class ProductFormView yang berisi komponen TextField untuk input (Kode, Nama, Harga, Stok), Button untuk aksi tambah, dan ListView untuk menampilkan data.
3. Implementasi Controller: Membuat ProductController yang menangani logika saat tombol ditekan.
4. Integrasi Backend: Menghubungkan Controller dengan ProductService untuk menyimpan data ke database melalui DAO.
5. Event Handling: Menambahkan event listener pada tombol "Tambah Produk" untuk memicu proses penyimpanan dan pembaruan tampilan.
6. Commit dan Push: Menyimpan perubahan ke repository dengan pesan week12-gui-dasar: implement basic javafx gui for product entry.

---

## Kode Program

**controller/ProductController,java**

```java
package com.upb.agripos.controller;

import com.upb.agripos.model.Product; 
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductFormView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductController {
    private ProductService service;
    private ProductFormView view;

    public ProductController(ProductService service, ProductFormView view) {
        this.service = service;
        this.view = view;
        initController();
    }

    private void initController() {
        loadData(); // Memasukkan data ke list saat aplikasi dibuka

        // Memberi aksi pada tombol Tambah Produk
        view.btnTambah.setOnAction(e -> {
            try {
                // 1. Ambil data dari kotak input (TextField)
                String code = view.txtKode.getText();
                String name = view.txtNama.getText();
                double price = Double.parseDouble(view.txtHarga.getText());
                int stock = Integer.parseInt(view.txtStok.getText());

                // 2. Buat objek Product (Sesuai dengan constructor Product.java kamu)
                Product newProduct = new Product(code, name, price, stock);
                
                // 3. Simpan lewat Service
                service.addProduct(newProduct);
                
                // 4. Refresh tampilan dan kosongkan inputan
                loadData();
                clearFields();
                System.out.println("Data berhasil disimpan!");

            } catch (NumberFormatException ex) {
                System.err.println("Harga dan Stok harus angka!");
            } catch (Exception ex) {
                System.err.println("Gagal simpan: " + ex.getMessage());
            }
        });
    }

    private void loadData() {
        try {
            ObservableList<String> displayList = FXCollections.observableArrayList();
            // Gunakan getName() dan getStock() sesuai file Product.java kamu
            for (Product p : service.getAllProducts()) {
                displayList.add(p.getCode() + " - " + p.getName() + " (Stok: " + p.getStock() + ")");
            }
            view.listProduk.setItems(displayList);
        } catch (Exception e) {
            System.err.println("Gagal load data: " + e.getMessage());
        }
    }

    private void clearFields() {
        view.txtKode.clear();
        view.txtNama.clear();
        view.txtHarga.clear();
        view.txtStok.clear();
    }
}
```

**dao/ProductDAO,java**

```java
package com.upb.agripos.dao;

import java.util.List;

import com.upb.agripos.model.Product;

public interface ProductDAO {
    void insert(Product p);
    void update(Product p);
    void delete(String code);
    Product findByCode(String code);
    List<Product> findAll();
}
```

**dao/ProductDAOImpl,java**

```java
package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {
    private Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) {
        String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getCode());
            stmt.setString(2, p.getName());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getStock());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product p) {
        String sql = "UPDATE products SET name = ?, price = ?, stock = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getStock());
            stmt.setString(4, p.getCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String code) {
        String sql = "DELETE FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product findByCode(String code) {
        String sql = "SELECT * FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
```

**model/Product.java**

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
```

**servise/ProductServise.java**

```java
package com.upb.agripos.service;

import java.util.List;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void addProduct(Product product) {
        // Di sini bisa ditambahkan validasi bisnis jika perlu
        productDAO.insert(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }
}
```

**view/AppJavaFX.java**

```java
package com.upb.agripos;

import com.upb.agripos.view.ProductFormView;
import com.upb.agripos.controller.ProductController;
import com.upb.agripos.dao.*;
import com.upb.agripos.service.ProductService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Tampilkan Jendela Dulu
        ProductFormView view = new ProductFormView();
        Scene scene = new Scene(view, 400, 550);
        stage.setTitle("Agri-POS - Week 12");
        stage.setScene(scene);
        stage.show();

        // 2. Coba Hubungkan Database (Gunakan try-catch agar jika gagal, app tidak mati)
        try {
            // GANTI PASSWORD SESUAI LAPTOP KAMU
            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/agripos", "postgres", "1234"
            );

            // Inisialisasi MVC
            ProductDAO dao = new ProductDAOImpl(conn);
            ProductService service = new ProductService(dao);
            new ProductController(service, view);

            System.out.println("Koneksi Database Berhasil!");
        } catch (Exception e) {
            System.err.println("Database Error (Aplikasi tetap jalan): " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## Hasil Eksekusi

![Screenshot hasil](/praktikum/week12-gui-dasar/screenshots/hasil%20week12.png)

---

## Analisis
1. Ketahanan Aplikasi (Resilience): Selama praktikum, ditemukan bahwa jika koneksi database gagal di awal, GUI tidak akan muncul jika perintah show() diletakkan di akhir blok try. Solusinya adalah memisahkan inisialisasi UI dengan inisialisasi database.

2. Konsistensi Penamaan: Penggunaan nama variabel bahasa Inggris pada model (Product.java) menuntut ketelitian pada Controller saat memanggil getter seperti getCode() daripada getKode().

3. Feedback Pengguna: Penggunaan ListView memberikan umpan balik langsung kepada pengguna bahwa data telah berhasil masuk ke dalam sistem.
---

## Traceability Bab 6 (UML) -> Implementasi GUI

| Artefak Bab 6 | Referensi | Handler GUI (View) | Controller & Service | Data Access Object (DAO) | Dampak pada UI & Database |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Use Case** | UC-01 Tambah Produk | Tombol `btnTambah` | `ProductController.handleAddProduct()` memanggil `service.addProduct(p)` | `ProductDAO.insert(product)` | Baris data baru tersimpan di tabel produk dan muncul di ListView |
| **Activity Diagram** | AD-01 Alur Input Produk | `TextField` (txtKode, txtNama, dll) | Validasi data menggunakan `Double.parseDouble` & `Integer.parseInt` | `productDAO.insert()` dipicu setelah validasi sukses | Mencegah aplikasi crash jika user salah memasukkan format harga/stok |
| **Sequence Diagram** | SD-01 Interaksi Simpan | `setOnAction(e -> ...)` pada tombol | Alur data sekuensial: View mengirim data -> Controller membungkus Model -> Service memproses | `DAO` mengeksekusi Query SQL `INSERT INTO...` menggunakan JDBC | Menjamin data berpindah dari layar (GUI) ke penyimpanan permanen (DB) secara urut |
| **Class Diagram** | Struktur MVC & SOLID | Komponen `VBox` & `ListView` | `ProductController` menghubungkan View dengan `ProductService` | `ProductDAOImpl` mengimplementasikan interface DAO | Terciptanya pemisahan logika (Decoupling) sehingga kode mudah dikembangkan |

---

## Kesimpulan

Implementasi GUI pada minggu ke-12 ini berhasil menjembatani kode backend (JDBC & DAO) menjadi aplikasi yang interaktif. Penggunaan JavaFX memungkinkan pembuatan form yang user-friendly, sementara pola MVC memastikan bahwa setiap perubahan pada tampilan tidak merusak logika bisnis yang sudah ada.



