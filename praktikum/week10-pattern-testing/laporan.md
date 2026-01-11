# Laporan Praktikum Minggu 10
Topik: Design Pattern (Singleton, MVC) dan Unit Testing menggunakan JUnit

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Setelah mengikuti praktikum ini, mahasiswa mampu:

1. Menjelaskan konsep dasar design pattern dalam rekayasa perangkat lunak.
2. Mengimplementasikan Singleton Pattern dengan benar.
3. Menjelaskan dan menerapkan Model–View–Controller (MVC) pada aplikasi sederhana.
4. Membuat dan menjalankan unit test menggunakan JUnit.
5. Menganalisis manfaat penerapan design pattern dan unit testing terhadap kualitas perangkat lunak.


---

## Dasar Teori
1. Design Pattern merupakan pola solusi yang sudah umum digunakan untuk menyelesaikan masalah berulang dalam desain program.

2. Singleton Pattern bertujuan membatasi objek yang dibuat sehingga hanya ada satu instance dalam sistem.

3. Model–View–Controller (MVC) memisahkan area kerja program menjadi bagian data (Model), tampilan (View), dan pengendali alur (Controller).

4. Unit Testing menguji potongan kode secara terisolasi agar kesalahan dapat ditemukan lebih awal.

5. JUnit adalah framework pengujian otomatis pada Java yang mendukung penulisan dan eksekusi test case.

---

## Langkah Praktikum
1. Membuat project Java baru sesuai struktur direktori praktikum minggu 10.

2. Mengembangkan class dengan pattern Singleton untuk simulasi koneksi database.

3. Mengimplementasikan konsep MVC pada fitur produk (Model, View, Controller).

4. Menambahkan JUnit test case untuk menguji fungsi pada class Product.

5. Menjalankan seluruh unit test hingga statusnya passed.

6. Melakukan commit repository dengan pesan:

---

## Kode Program

**config/DatabaseConnection.java**
```java
// ============================================================
// FILE 4: src/main/java/com/upb/agripos/config/DatabaseConnection.java
// ============================================================
package com.upb.agripos.config;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    // Private constructor untuk mencegah instantiasi dari luar
    private DatabaseConnection() {
        System.out.println("Database Connection Created!");
    }

    // Static method untuk mendapatkan instance tunggal
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void connect() {
        System.out.println("Connected to database...");
    }

    public void disconnect() {
        System.out.println("Disconnected from database...");
    }
}
```

**controller/ProductController**
```java
// ============================================================
// FILE 3: src/main/java/com/upb/agripos/controller/ProductController.java
// ============================================================
package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.view.ConsoleView;

public class ProductController {
    private final Product model;
    private final ConsoleView view;

    public ProductController(Product model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    public void showProduct() {
        view.showMessage("Produk: " + model.getCode() + " - " + model.getName());
    }
}

```

**model/Product.java**
```java
// ============================================================
// FILE 1: src/main/java/com/upb/agripos/model/Product.java
// ============================================================
package com.upb.agripos.model;

public class Product {
    private final String code;
    private final String name;

    public Product(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

```

**view/AppMVC.java**
```java
// ============================================================
// FILE 5: src/main/java/com/upb/agripos/AppMVC.java
// ============================================================
package com.upb.agripos;

import com.upb.agripos.model.Product;
import com.upb.agripos.view.ConsoleView;
import com.upb.agripos.controller.ProductController;
import com.upb.agripos.config.DatabaseConnection;

public class AppMVC {
    public static void main(String[] args) {
        // GANTI dengan Nama dan NIM Anda
        System.out.println("Hello, I am [Indah Ruwahna Anugraheni]-[240202866] (Week10)");
        System.out.println("===========================================\n");

        // Demonstrasi Singleton Pattern
        System.out.println("=== SINGLETON PATTERN DEMO ===");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        
        System.out.println("db1 == db2? " + (db1 == db2)); // Should be true
        db1.connect();
        System.out.println();

        // Demonstrasi MVC Pattern
        System.out.println("=== MVC PATTERN DEMO ===");
        Product product = new Product("P01", "Pupuk Organik");
        ConsoleView view = new ConsoleView();
        ProductController controller = new ProductController(product, view);
        
        controller.showProduct();
        
        // Contoh produk lain
        Product product2 = new Product("P02", "Benih Jagung Hibrida");
        ProductController controller2 = new ProductController(product2, view);
        controller2.showProduct();
        
        System.out.println();
        db1.disconnect();
    }
}

```

**ProductTest.java**
```java
// ============================================================
// FILE 6: src/test/java/com/upb/agripos/ProductTest.java
// ============================================================
package com.upb.agripos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.Product;

public class ProductTest {
    
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product("P01", "Benih Jagung");
    }

    @Test
    @DisplayName("Test Product Name")
    public void testProductName() {
        assertEquals("Benih Jagung", product.getName());
    }

    @Test
    @DisplayName("Test Product Code")
    public void testProductCode() {
        assertEquals("P01", product.getCode());
    }

    @Test
    @DisplayName("Test Product Not Null")
    public void testProductNotNull() {
        assertNotNull(product);
        assertNotNull(product.getCode());
        assertNotNull(product.getName());
    }

    @Test
    @DisplayName("Test Singleton Pattern - Same Instance")
    public void testSingletonInstance() {
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        
        // Kedua instance harus sama (referensi objek sama)
        assertSame(db1, db2, "DatabaseConnection harus menghasilkan instance yang sama");
    }

    @Test
    @DisplayName("Test Singleton Pattern - Not Null")
    public void testSingletonNotNull() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        assertNotNull(db, "DatabaseConnection instance tidak boleh null");
    }
}

```

---

## Hasil Eksekusi
**AppMVC**

![Screenshot hasil](/praktikum/week10-pattern-testing/screenshots/AppMVC.png)

**junut_result**

![Screenshot hasil](/praktikum/week10-pattern-testing/screenshots/junit_result.png)

---

## Hasil Percobaan
1. Program berhasil menampilkan informasi produk melalui console.

2. Objek DatabaseConnection hanya dibuat satu kali selama eksekusi program.

3. Seluruh test JUnit berhasil dijalankan tanpa error (status: green bar).

---
## Analisis

1. Pola Singleton efektif untuk kelas yang hanya membutuhkan satu objek global.

2. Pemisahan MVC membuat struktur aplikasi lebih jelas dan mudah dikembangkan.

3. Unit testing membantu menjaga stabilitas fungsi saat program mengalami perubahan.

4. Kendala yang ditemui biasanya terkait konfigurasi library JUnit dan struktur package.

---

## Kesimpulan
Penerapan design pattern serta unit testing pada praktikum ini membuat program:

1. lebih terorganisir

2. lebih mudah dirawat

3. lebih mudah diuji

4. memiliki kualitas yang lebih baik

---

## Quiz
1. Apa tujuan utama dari Singleton Pattern?

   Jawaban: Membatasi jumlah objek yang dibuat agar hanya ada satu instance dari suatu class.

2. Mengapa MVC dianggap memudahkan pengembangan aplikasi?

   Jawaban: Karena logika program, tampilan, dan pengontrol dipisahkan sehingga tidak saling bergantung secara langsung.

3. Apa keuntungan melakukan unit testing sejak awal pengembangan?

   Jawaban: Kesalahan dapat ditemukan lebih cepat sehingga biaya perbaikan lebih rendah.

4. Apa yang terjadi jika satu class yang seharusnya Singleton dapat dibuat banyak objeknya?

   Jawaban: Bisa terjadi inkonsistensi data, penggunaan memori berlebih, serta perilaku sistem yang tidak dapat diprediksi.
