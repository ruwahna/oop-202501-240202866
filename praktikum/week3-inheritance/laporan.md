# Laporan Praktikum Minggu 3
Topik: Bab 3 – Inheritance

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
- Mahasiswa mampu menjelaskan konsep inheritance (pewarisan class) dalam OOP.
- Mahasiswa mampu membuat superclass dan subclass untuk produk pertanian.
- Mahasiswa mampu mendemonstrasikan hierarki class melalui contoh kode.
- Mahasiswa mampu menggunakan super untuk memanggil konstruktor dan method parent class.
- Mahasiswa mampu membuat laporan praktikum yang menjelaskan perbedaan penggunaan inheritance dibanding class tunggal.

---

## Dasar Teori
Inheritance adalah mekanisme dalam Object Oriented Programming (OOP) yang memungkinkan sebuah class mewarisi atribut dan method dari class lain.

- Superclass merupakan class induk yang berisi atribut dan method umum yang dapat digunakan oleh class lain.

- Subclass adalah class turunan yang mewarisi atribut dan method dari superclass, serta dapat menambahkan fitur baru sesuai kebutuhan.

Kata kunci super digunakan untuk memanggil konstruktor atau method milik superclass dari dalam subclass.

---

## Langkah Praktikum
- Membuat Superclass Produk
  - Gunakan class Produk dari Bab 2 sebagai superclass.

- Membuat Subclass
  - Benih.java → atribut tambahan: varietas.
  - Pupuk.java → atribut tambahan: jenis pupuk (Urea, NPK, dll).
  - AlatPertanian.java → atribut tambahan: material (baja, kayu, plastik).

- Membuat Main Class
  - Instansiasi minimal satu objek dari tiap subclass.
  - Tampilkan data produk dengan memanfaatkan inheritance.

- Menambahkan CreditBy
  - Panggil class CreditBy untuk menampilkan identitas mahasiswa.

- Commit dan Push
  - Commit dengan pesan: week3-inheritance.

---

## Kode Program

AlatPertanian.java
```java
package com.upb.agripos.model;

public class AlatPertanian extends Produk {
    private String material;

    public AlatPertanian(String kode, String nama, double harga, int stok, String material) {
        super(kode, nama, harga, stok);
        this.material = material;
    }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public void deskripsiAlat() {
        System.out.println("Alat Pertanian " + getNama() + " terbuat dari " + material + " dengan harga " + getHarga()+ " kode: " + getKode() + " stok: " + getStok());
    }
}

```
Benih.java
```java
package com.upb.agripos.model;



public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    public String getVarietas() { return varietas; }
    public void setVarietas(String varietas) { this.varietas = varietas; }

   public void deskripsiBenih() {
        System.out.println("Benih: " + getNama() + " varietas: " + varietas + " dengan harga: " + getHarga() + " kode: " + getKode() + " stok: " + getStok());
    }
}
```

Pupuk.java
```java
package com.upb.agripos.model;



public class Pupuk extends Produk {
    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    // deskripsiPupuk
    public void deskripsiPupuk() {
        System.out.println("Pupuk :" + getNama() + " jenis pupuk: " + jenis + " harga: " + getHarga() + " kode: " + getKode() + " stok: " + getStok());
    }
}
```
MainInheitance.java
```java
package com.upb.agripos;

import com.upb.agripos.model.*;
import com.upb.agripos.util.*;

public class MainInheritance {
    public static void main(String[] args) {
        Benih b = new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64");
        Pupuk p = new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea");
        AlatPertanian a = new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja");

        System.out.println("------------Deskripsi Produk-------------");
        b.deskripsiBenih();
        p.deskripsiPupuk();
        a.deskripsiAlat();
        System.out.println("-----------------------------------");
        CreditBy.print("240202866", "Indah Ruwahna Anugraheni");
    }
}
```
---

## Hasil Eksekusi
hasil eksekusi program
![c:\Users\hp\Pictures\Screenshots\week3-inheriatance.png](/praktikum/week3-inheritance/screenshots/week3-inheriatance.png)

---

## Analisis

- Jelaskan bagaimana kode berjalan.  
  Program dijalankan dari MainInheritance.java.
Objek Benih, Pupuk, dan AlatPertanian dibuat dari subclass yang mewarisi class Produk.
Konstruktor subclass memanggil konstruktor superclass dengan super() untuk mengisi atribut umum seperti kode, nama, harga, dan stok.
Lalu program menampilkan data setiap produk dan identitas mahasiswa melalui CreditBy

- Apa perbedaan pendekatan minggu ini dibanding minggu sebelumnya.  
  Pendekatan pada minggu ini menerapkan konsep pewarisan (inheritance) untuk membentuk struktur class yang lebih teratur. Dengan cara ini, kode dapat digunakan kembali dan setiap jenis produk bisa memiliki ciri khasnya sendiri.
Berbeda dari minggu sebelumnya yang masih memakai class tunggal atau class terpisah tanpa hubungan, metode ini mengurangi pengulangan atribut umum seperti nama dan harga pada tiap produk.

- Kendala yang dihadapi dan cara mengatasinya.  





---

## Kesimpulan

Implementasi *Inheritance* (Pewarisan) dalam OOP dilakukan dengan membuat hierarki class untuk produk pertanian. Superclass `Produk` mendefinisikan atribut dasar yang umum seperti kode, nama, harga, dan stok. Sementara itu, subclass seperti `Benih`, `Pupuk`, dan `AlatPertanian` mewarisi atribut tersebut serta menambahkan properti yang lebih spesifik seperti varietas, jenis, dan material. Penggunaan keyword `super()` pada konstruktor subclass memastikan proses inisialisasi data superclass berjalan efisien. Hasilnya, kode menjadi lebih *reusable*, terstruktur, dan mudah dipelihara dibandingkan pendekatan dengan class tunggal. Pendekatan ini menjadi dasar penting bagi sistem Agri-POS untuk mengelola berbagai jenis produk secara modular dan fleksibel.


---

## Quiz
1. Apa keuntungan menggunakan inheritance dibanding membuat class terpisah tanpa hubungan?  
   **Jawaban:** 

   Inheritance membuat kode lebih efisien, mudah dikelola, dan menghindari duplikasi atribut atau method yang sama di banyak class. 

2. Bagaimana cara subclass memanggil konstruktor superclass?  
   **Jawaban:** 

   Subclass dapat memanggil konstruktor dari superclass dengan menggunakan keyword super() yang diletakkan pada baris pertama di dalam konstruktor subclass. Pemanggilan ini berfungsi untuk menginisialisasi atribut yang dimiliki oleh superclass sebelum atribut milik subclass diatur. Contohnya pada konstruktor class Benih, perintah super(kode, nama, harga, stok); akan memanggil konstruktor class Produk agar data dasarnya dapat diatur terlebih dahulu.

3. Berikan contoh kasus di POS pertanian selain Benih, Pupuk, dan Alat Pertanian yang bisa dijadikan subclass.  
   **Jawaban:** 

   Contoh lain yang bisa dijadikan subclass dalam sistem POS pertanian adalah class Pestisida dengan atribut tambahan seperti bahan aktif dan jenis hama sasaran, class ObatTanaman yang menyimpan data tanggal kedaluwarsa dan cara penggunaan, serta class BibitSayuran dengan atribut jenis sayuran dan lama panen. Selain itu, dapat juga dibuat class HasilPanen untuk mencatat berat bersih dan kualitas hasil pertanian, serta PeralatanIrigasi yang menyimpan informasi panjang selang atau kapasitas air.


