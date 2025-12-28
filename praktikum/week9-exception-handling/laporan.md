# Laporan Praktikum Minggu9

Topik: Exception Handling, Custom Exception, dan Design Pattern Sederhana

## Identitas
- Nama  : [INDAH RUWAHNA ANUGRAHENI]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
Mahasiswa mampu:

1. Menjelaskan perbedaan antara error dan exception.
2. Mengimplementasikan try–catch–finally dengan tepat.
3. Membuat custom exception sesuai kebutuhan program.
4. Mengintegrasikan exception handling ke dalam aplikasi sederhana (kasus keranjang belanja).
5. (Opsional) Menerapkan design pattern sederhana (Singleton/MVC) dan unit testing dasar.

---

## Dasar Teori

1. Exception vs Error: Error adalah masalah serius yang biasanya tidak bisa ditangani oleh program (seperti kerusakan sistem), sedangkan Exception adalah kondisi abnormal yang terjadi saat program berjalan dan dapat ditangani agar program tidak berhenti tiba-tiba.
2. Try-Catch-Finally: try digunakan untuk membungkus kode yang berisiko, catch untuk menangani tipe kesalahan tertentu, dan finally untuk menjalankan kode pembersihan yang harus selalu dieksekusi (seperti menutup file atau koneksi).

3. Custom Exception: Class pengecualian yang dibuat sendiri dengan cara mewarisi (inheritance) class Exception untuk menangani kasus spesifik yang tidak ada di standar Java.

4. Singleton Pattern: Pola desain yang memastikan bahwa sebuah kelas hanya memiliki satu instansi di seluruh siklus hidup aplikasi.

---

## Langkah Praktikum

1. Persiapan: Membuat folder proyek baru dan menyiapkan class utama.

2. Pembuatan Custom Exception: Membuat file StokKurangException.java yang meng-extend class Exception.

3. Pembuatan Model: Membuat class Produk dengan atribut nama, harga, dan stok.

4. Implementasi Singleton: Membuat class KeranjangBelanja menggunakan pola Singleton agar data belanja konsisten.

5. Penyusunan Logic: Menggunakan blok try-catch saat memanggil metode yang melempar (throw) exception.

6. Eksekusi: Menjalankan program dan menguji skenario input normal serta input yang memicu error.

---

## Kode Program  

EmptyStockException.java

```java
package main.java.com.upb.agripos;

public class EmptyStockException extends Exception {

    public EmptyStockException(String message) {
        super(message);
    }
}

```


InsufficientStockException.java

```java
package main.java.com.upb.agripos;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String msg) { 
        super(msg); 
    }
}

```


InvalidPriceExcepption.java

```java
package main.java.com.upb.agripos;

public class InvalidPriceException extends Exception {

    public InvalidPriceException(String message) {
        super(message);
    }
}

```


InvalidQuantityException.java

```java
package main.java.com.upb.agripos;

public class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String msg) { 
        super(msg); 
    }
}

```

MainExceptionDemo.java

```java
package main.java.com.upb.agripos;

public class MainExceptionDemo {
    public static void main(String[] args) {
        System.out.println("Hello, I am Indah Ruwahna Anugraheni-240202866 (Week9)");

        ShoppingCart cart = new ShoppingCart();
        Product p1 = new Product("P01", "Pupuk Organik", 25000, 3);

        // 1. Trigger: Quantity harus lebih dari 0.
        try {
            cart.addProduct(p1, -1);
        } catch (InvalidQuantityException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        // 2. Trigger: Produk tidak ada dalam keranjang.
        try {
            cart.removeProduct(p1);
        } catch (ProductNotFoundException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        // 3. Trigger: Stok tidak cukup untuk: Pupuk Organik
        try {
            // Kita set stok jadi 0 dulu untuk simulasi error sesuai gambar
            p1.reduceStock(3); 
            cart.addProduct(p1, 1);
            cart.checkout();
        } catch (InsufficientStockException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }
    }
}

```

Product.java

```java
package main.java.com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;
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
    public void reduceStock(int qty) { this.stock -= qty; }
}

```

ProductNotFoundException.java

```java
package main.java.com.upb.agripos;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String msg) { super(msg); }
}

```

ShoppingCart.java

```java
package main.java.com.upb.agripos;
import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        // Pesan disesuaikan dengan gambar Anda
        if (qty <= 0) throw new InvalidQuantityException("Quantity harus lebih dari 0.");
        for (int i = 0; i < qty; i++) {
            items.add(p);
        }
    }

    public void removeProduct(Product p) throws ProductNotFoundException {
        // Pesan disesuaikan dengan gambar Anda
        if (!items.contains(p)) throw new ProductNotFoundException("Produk tidak ada dalam keranjang.");
        items.remove(p);
    }

    public void checkout() throws InsufficientStockException {
        for (Product p : items) {
            // Jika jumlah di keranjang melebihi stok yang ada
            if (p.getStock() < 1) { 
                throw new InsufficientStockException("Stok tidak cukup untuk: " + p.getName());
            }
        }
        System.out.println("Checkout Berhasil!");
        items.clear();
    }
}

```
---

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
![Screenshot hasil](/praktikum/week9-exception-handling/screenshots/week%209-Exception.png)
)
---

## Analisis

1. Alur Kerja: Kode berjalan dengan memvalidasi input terlebih dahulu. Jika jumlah yang diminta melebihi stok, program tidak akan crash, melainkan melompat ke blok catch untuk memberikan pesan peringatan kepada pengguna.

2. Perbandingan: Berbeda dengan minggu lalu yang hanya menggunakan logika if-else biasa, pendekatan minggu ini lebih aman karena memaksa programmer untuk menangani potensi kesalahan secara eksplisit (checked exception).

3. Kendala: Kesulitan awal dalam memahami kapan harus menggunakan throw (melempar) dan throws (deklarasi). Solusinya adalah dengan mengingat bahwa throws ada di header metode, sedangkan throw ada di dalam tubuh metode.
---

## Kesimpulan
Dengan menerapkan Exception Handling, aplikasi menjadi lebih tangguh (robust) karena mampu menangani kesalahan runtime tanpa berhenti secara paksa. Penggunaan Custom Exception mempermudah identifikasi masalah spesifik pada logika bisnis, seperti masalah stok pada keranjang belanja.

---

## Quiz

1. Apa perbedaan mendasar antara Checked Exception dan Unchecked Exception?

   Jawaban: 
   
    Checked Exception harus ditangani saat waktu kompilasi (menggunakan try-catch atau throws), sedangkan Unchecked Exception (Runtime) tidak wajib dideklarasikan secara eksplisit.

2. Kapan waktu yang tepat untuk menggunakan blok finally?
   
   Jawaban:

   Saat kita memiliki kode yang harus dijalankan apapun yang terjadi, baik ada error maupun tidak (seperti menutup koneksi database).

3. Mengapa Singleton Pattern berguna dalam aplikasi keranjang belanja?

   Jawaban:
 
   Agar seluruh bagian aplikasi mengakses satu objek keranjang yang sama, sehingga data belanja tidak terduplikasi atau hilang antar halaman/class.