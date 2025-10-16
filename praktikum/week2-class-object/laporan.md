# Laporan Praktikum Minggu 1 (sesuaikan minggu ke berapa?)
Topik: [Class dan Object (Produk Pertanian)]

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
1. Mahasiswa mampu menjelaskan konsep class, object, atribut, dan method dalam OOP.
2. Mahasiswa mampu menerapkan access modifier dan enkapsulasi dalam pembuatan class.
3. Mahasiswa mampu mengimplementasikan class Produk pertanian dengan atribut dan method yang sesuai.
4. Mahasiswa mampu mendemonstrasikan instansiasi object serta menampilkan data produk pertanian di console.
5. Mahasiswa mampu menyusun laporan praktikum dengan bukti kode, hasil eksekusi, dan analisis sederhana.

---

## Dasar Teori
1. Class adalah blueprint atau cetak biru dari sebuah objek yang berisi atribut dan method.

2. Object merupakan instansiasi dari class yang memiliki nilai nyata dari atribut dan dapat menjalankan method.

3. Atribut merepresentasikan data atau properti dari sebuah objek (misalnya: nama, harga, stok).

4. Method adalah fungsi atau perilaku yang dimiliki oleh sebuah objek.

5. Enkapsulasi adalah cara untuk melindungi data dengan menjadikan atribut bersifat private dan menyediakan akses melalui getter dan setter.

6. Dalam konteks sistem Agri-POS, produk pertanian seperti benih, pupuk, dan alat pertanian dapat direpresentasikan sebagai objek agar lebih mudah dikelola secara terstruktur.

---

## Langkah Praktikum
1. Membuat Class Produk
   - Buat file Produk.java pada package model.
   - Tambahkan atribut: kode, nama, harga, dan stok.
   - Gunakan enkapsulasi dengan menjadikan atribut bersifat private dan membuat getter serta setter untuk masing-masing atribut.
2. Membuat Class CreditBy
   - Buat file CreditBy.java pada package util.
   - Isi class dengan method statis untuk menampilkan identitas mahasiswa di akhir output: credit by: <NIM> - <Nama>.
   - Membuat Objek Produk dan Menampilkan Credit
3. Buat file MainProduk.java.
   - Instansiasi minimal tiga objek produk, misalnya "Benih Padi", "Pupuk Urea", dan satu produk alat pertanian.
   - Tampilkan informasi produk melalui method getter.
   - Panggil CreditBy.print("<NIM>", "<Nama>") di akhir main untuk menampilkan identitas.
4. Commit dan Push
   - Commit dengan pesan: week2-class-object.

---

## Kode Program
Produk

```java
package com.upb.agripos.model;

public class Produk {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

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
}

```
CreditBy

```java
package com.upb.agripos.util;

public class CreditBy {
    public static void print(String nim, String nama) {
        System.out.println("\ncredit by: " + nim + " - " + nama);
    }
}
```
MainProduk

```java

package com.upb.agripos;

import com.upb.agripos.model.Produk;
import com.upb.agripos.util.CreditBy;

public class MainProduk {
    public static void main(String[] args) {
        Produk p1 = new Produk("BNH-001", "Benih Padi IR64", 25000, 100);
        Produk p2 = new Produk("PPK-101", "Pupuk Urea 50kg", 350000, 40);
        Produk p3 = new Produk("ALT-501", "Cangkul Baja", 90000, 15);

        System.out.println("Kode: " + p1.getKode() + ", Nama: " + p1.getNama() + ", Harga: " + p1.getHarga() + ", Stok: " + p1.getStok());
        System.out.println("Kode: " + p2.getKode() + ", Nama: " + p2.getNama() + ", Harga: " + p2.getHarga() + ", Stok: " + p2.getStok());
        System.out.println("Kode: " + p3.getKode() + ", Nama: " + p3.getNama() + ", Harga: " + p3.getHarga() + ", Stok: " + p3.getStok());

       p1.tambahStok(1000);
       p2.kurangiStok(20);
       p3.kurangiStok(15);

        System.out.println("\n===Update Stok===");
        System.out.println("Kode: " + p1.getKode() + ", Nama: " + p1.getNama() + ", Harga: " + p1.getHarga() + ", Stok: " + p1.getStok());
        System.out.println("Kode: " + p2.getKode() + ", Nama: " + p2.getNama() + ", Harga: " + p2.getHarga() + ", Stok: " + p2.getStok());
        System.out.println("Kode: " + p3.getKode() + ", Nama: " + p3.getNama() + ", Harga: " + p3.getHarga() + ", Stok: " + p3.getStok());


        // Tampilkan identitas mahasiswa
        CreditBy.print("<240202866>", "<Indah Ruwahna Anugraheni>");
    }
}
```

---

## Hasil Eksekusi

![ praktikum/week2-class-object/screenshots/Class&Object.png](Class&Object-1.png)
---

## Analisis

1. Jelaskan bagaimana kode berjalan. 
   Program dimulai dari file MainProduk.java karena berisi method main() sebagai titik awal eksekusi.
   - Di dalam main(), tiga objek produk dibuat menggunakan constructor dari class Produk.
   ```
   Produk p1 = new Produk("BNH-001", "Benih Padi IR64", 25000, 100);
   ```
   Saat baris ini dijalankan, Java akan memanggil constructor di class Produk dan menyimpan nilai atribut kode, nama, harga, dan stok ke dalam objek p1.
   - Setelah ketiga objek (p1, p2, p3) terbentuk, program memanggil getter seperti getKode(), getNama(), dll., untuk menampilkan data produk ke console:
   ```
   System.out.println("Kode: " + p1.getKode() + ", Nama: " + p1.getNama() + ...);
   ```
   - Di akhir program, method CreditBy.print() dipanggil:
   ```
   CreditBy.print("240202866", "Indah Ruwahna Anugraheni");
   ```
   Method ini mencetak identitas mahasiswa sebagai tanda pembuat program.
   - Seluruh proses berjalan secara berurutan — mulai dari pembuatan objek, pengambilan nilai atribut melalui getter, hingga menampilkan identitas.

2. Apa perbedaan pendekatan minggu ini dibanding minggu sebelumnya.

- Minggu ini sudah menerapkan konsep OOP (Object-Oriented Programming) dengan penggunaan class dan object, sedangkan minggu sebelumnya masih fokus pada dasar Java seperti variabel, input-output, dan struktur program sederhana.

- Pendekatan minggu ini lebih terstruktur karena setiap data dan fungsinya dikemas dalam class, sedangkan minggu sebelumnya masih bersifat prosedural dengan urutan perintah langsung di dalam satu fungsi utama.
Selain itu, minggu ini juga mulai menerapkan enkapsulasi menggunakan getter dan setter untuk melindungi data dalam class.

3. Kendala yang dihadapi dan cara mengatasinya. 
- Kendala utama yang dihadapi adalah memahami hubungan antar class serta cara memanggil method dari package lain.
Pada awalnya, program tidak bisa dijalankan karena class dari package berbeda belum di-import dengan benar.

- Solusinya adalah dengan mempelajari kembali cara kerja import di Java dan memahami konsep pemanggilan method melalui objek maupun method statis.
Setelah memahami alur tersebut, setiap class dapat saling terhubung dengan baik dan program berhasil dijalankan tanpa error. 

---

## Kesimpulan
Melalui praktikum ini, dipelajari bagaimana membuat class dan object dalam Java dengan menerapkan prinsip enkapsulasi. Program Produk Pertanian berhasil menampilkan data produk menggunakan konsep OOP yang terstruktur dan efisien.

---

## Quiz
1. Mengapa atribut sebaiknya dideklarasikan sebagai private dalam class?
   **Jawaban:** 
   Agar data tidak bisa diakses langsung dari luar class, sehingga menjaga keamanan dan konsistensi data (prinsip data hiding).

2. Apa fungsi getter dan setter dalam enkapsulasi?
   **Jawaban:** 
   Getter digunakan untuk membaca nilai atribut, sedangkan setter digunakan untuk mengubah nilai atribut secara terkendali.

3. Bagaimana cara class Produk mendukung pengembangan aplikasi POS yang lebih kompleks? 
   **Jawaban:** 
   Class Produk dapat menjadi dasar untuk pengelolaan inventori produk pertanian di aplikasi POS. Dengan menambahkan fitur tambahan (seperti kategori, transaksi, dan laporan stok), sistem dapat dikembangkan menjadi lebih lengkap dan modular.