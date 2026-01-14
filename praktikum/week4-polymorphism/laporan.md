# Laporan Praktikum Minggu 4
Topik: Polymorphism (Info Produk)

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
- Mahasiswa mampu menjelaskan konsep polymorphism dalam OOP.
- Mahasiswa mampu membedakan method overloading dan overriding.
- Mahasiswa mampu mengimplementasikan polymorphism (overriding, overloading, dynamic binding) dalam program.
- Mahasiswa mampu menganalisis contoh kasus polymorphism pada sistem nyata (Agri-POS).

---

## Dasar Teori
Polymorphism berarti “banyak bentuk” dan memungkinkan objek yang berbeda merespons panggilan method yang sama dengan cara yang berbeda.

- Overloading → mendefinisikan method dengan nama sama tetapi parameter berbeda.
- Overriding → subclass mengganti implementasi method dari superclass.
Dynamic Binding → pemanggilan method ditentukan saat runtime, bukan compile time.

Dalam konteks Agri-POS, misalnya:

- Method getInfo() pada Produk dioverride oleh Benih, Pupuk, AlatPertanian untuk menampilkan detail spesifik.
- Method tambahStok() bisa dibuat overload dengan parameter berbeda (int, double).

---

## Langkah Praktikum
- Overloading
  - Tambahkan method tambahStok(int jumlah) dan tambahStok(double jumlah) pada class Produk.

- Overriding
  - Tambahkan method getInfo() pada superclass Produk.
  - Override method getInfo() pada subclass Benih, Pupuk, dan AlatPertanian.

- Dynamic Binding
  - Buat array Produk[] daftarProduk yang berisi objek Benih, Pupuk, dan AlatPertanian.
  - Loop array tersebut dan panggil getInfo(). Perhatikan bagaimana Java memanggil method sesuai jenis objek aktual.

- Main Class
  - Buat MainPolymorphism.java untuk mendemonstrasikan overloading, overriding, dan dynamic binding.

- CreditBy
  - Tetap panggil CreditBy.print("<NIM>", "<Nama>").

- Commit dan Push
  - Commit dengan pesan: week4-polymorphism.
  
---

## Kode Program

AlatPertanian.java
```java
package com.upb.agripos.model;

public class AlatPertanian extends Product{

    private String material;

    public AlatPertanian(String kode, String nama, double harga, int stok, String material) {
        super(kode, nama, harga, stok);
        this.material = material;
    }

    @Override
    public String getInfo() {
        return "Alat: "+  super.getInfo() + ", Material: " + material ;
    }
}

```

Benih.java
```java
package com.upb.agripos.model;

public class Benih extends  Product{

    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    @Override
    public String getInfo() {
        return "Benih: " + super.getInfo() + ", Varietas: " + varietas;
    }
}

```

ObatHama.java
```java
package com.upb.agripos.model;

public class ObatHama extends Product{

    String fungsi;

    public ObatHama(String kode, String nama, double harga, int stok, String fungsi) {
        super(kode, nama, harga, stok);
        this.fungsi = fungsi;
    }

    @Override
    public String getInfo() {
        return "Obat: " + super.getInfo() + ", Fungsi: " + fungsi;
    }
}

```
Product.java
```java
package com.upb.agripos.model;

public class Product {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Product(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public void kurangiStok(int jumlah) {
        if (this.stok >= jumlah) {
            this.stok -= jumlah;
        } else {
            System.out.println("Stok tidak mencukupi!");
        }
    }
    public String getInfo() {
        return nama + " (Kode: " + kode + ")" + " Harga :" + "Rp." + Double.toString(harga);
    }
}
```
Pupuk.java
```java

package com.upb.agripos.model;

public class Pupuk extends  Product{

    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", jenis: " + jenis;
    }
}


```
MainPolymorphism.java
```java

package com.upb.agripos;

import com.upb.agripos.model.*;
import com.upb.agripos.util.CreditBy;

public class MainPolymorphism {
    public static void main(String[] args) {
        Product[] daftarProduk = {
                new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64"),
                new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea"),
                new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja"),
                new ObatHama("OBT-220", "Wastak",25000,4,"basmi wereng")
        };

        for (Product p : daftarProduk) {
            System.out.println(p.getInfo()); // Dynamic Binding
        }

        CreditBy.print("240202866", "Indah Ruwahna Anugraheni");
    }
}
```

---

## Hasil Eksekusi
  
![praktikum/week4-polymorphism/screenshots/polymorphism.png](/praktikum/week4-polymorphism/screenshots/polymorphism.png)

---

## Analisis

- Jelaskan bagaimana kode berjalan.  
Program ini menunjukkan bahwa setiap subclass dari Product memiliki implementasi getInfo() yang berbeda.
Ketika program dijalankan, Java secara otomatis memanggil method getInfo() sesuai dengan tipe objek aktual di dalam array daftarProduk, bukan berdasarkan tipe referensinya (Product).

- Apa perbedaan pendekatan minggu ini dibanding minggu sebelumnya.  
  - Minggu sebelumnya, class hanya berfokus pada konsep inheritance (pewarisan) tanpa perilaku berbeda.

  - Minggu ini, kita menambahkan polymorphism untuk memberikan perilaku yang spesifik pada setiap subclass.

- Kendala yang dihadapi dan cara mengatasinya.
  - Awalnya, output pada class Pupuk tidak sesuai karena method getInfo() masih menambahkan label "Nama:" di depan super.getInfo(), sehingga hasilnya berulang.

  - Setelah diperbaiki menjadi return super.getInfo() + ", Jenis: " + jenis;, output menjadi benar.  

  Cara mengatasinya:

  - Memahami urutan pewarisan dan bagaimana super bekerja dalam memanggil method dari superclass.

  - Mengecek kembali format getInfo() agar konsisten dengan subclass lainnya.

---

## Kesimpulan
Dengan menerapkan konsep polymorphism, program dapat memiliki satu antarmuka (getInfo()) yang berperilaku berbeda-beda tergantung jenis objek yang menggunakannya.
Hal ini membuat program lebih fleksibel, mudah dikembangkan, dan terstruktur.
Setiap subclass dapat menambahkan detailnya sendiri tanpa mengubah struktur utama pada superclass.

---

## Quiz
1. Apa perbedaan overloading dan overriding?  
   **Jawaban:** 
   - Overloading terjadi ketika dua atau lebih method memiliki nama sama tetapi parameter berbeda dalam satu class.

   - Overriding terjadi ketika subclass menggantikan implementasi method dari superclass dengan

2. Bagaimana Java menentukan method mana yang dipanggil dalam dynamic binding?  
   **Jawaban:**

   Java menentukan method yang dipanggil berdasarkan tipe objek aktual pada runtime, bukan berdasarkan tipe referensi variabelnya.

3. Berikan contoh kasus polymorphism dalam sistem POS selain produk pertanian.  
   **Jawaban:** 

   Dalam sistem POS restoran, class MenuItem bisa memiliki subclass seperti Makanan, Minuman, dan Dessert.
   Masing-masing mengoverride method getInfo() untuk menampilkan detail berbeda seperti bahan utama, ukuran, atau suhu penyajian.