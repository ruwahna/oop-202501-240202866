# Laporan Praktikum Minggu 1 (sesuaikan minggu ke berapa?)
Topik: [Pengenalan Paradigma Pemrograman (Prosedural, OOP, dan Fungsional)]

## Identitas
- Nama  : [INDAH RUWAHNA ANUGRAHENI]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
1. Mahasiswa mampu mendefinisikan paradigma prosedural, OOP, dan fungsional.
2. Mahasiswa mampu membandingkan kelebihan dan keterbatasan tiap paradigma.
3. Mahasiswa mampu memberikan contoh program sederhana untuk masing-masing paradigma.
4. Mahasiswa aktif dalam diskusi kelas (bertanya, menjawab, memberi opini).

---

## Dasar Teori
 
1. Prosedural: program dibangun sebagai rangkaian perintah (fungsi/prosedur).
2. OOP (Object-Oriented Programming): program dibangun dari objek yang memiliki data (atribut) dan perilaku (method).
3. Fungsional: program dipandang sebagai pemetaan fungsi matematika, lebih menekankan ekspresi dan transformasi data.
4. Paradigma pemrograman adalah cara pandang dalam menyusun dan menulis program komputer.
5. Dalam konteks Agri-POS, OOP sangat membantu karena setiap entitas seperti Produk, Transaksi, dan Pembayaran dapat dimodelkan sebagai objek yang mudah dikembangkan dan dipelihara. 

---

## Langkah Praktikum
1. **Setup Project**
   - Pastikan sudah menginstall JDK (Java Development Kit), IDE (misal: IntelliJ IDEA, VS Code, NetBeans), Git, PostgreSQL, dan JavaFX di komputer.
   - Buat folder project oop-pos-<nim>.
   - Inisialisasi repositori Git.
   - Buat struktur awal src/main/java/com/upb/agripos/.
   - Pastikan semua tools dapat berjalan (uji dengan membuat dan menjalankan program Java sederhana).

2. **Program Sederhana dalam 3 Paradigma**

   - Prosedural: program untuk menghitung total harga dua produk.
   - OOP: class Produk dengan atribut nama dan harga, buat minimal tiga objek, lalu hitung total.
   - Fungsional: gunakan Stream atau lambda untuk menghitung total harga dari minimal tiga objek.

3. **Commit dan Push**
   - Commit dengan pesan:  week1-setup-hello-pos.
   
---

## Kode Program

Functional

```java
// HelloFunctional.java
import java.util.function.BiConsumer;
public class HelloFunctional {
    public static void main(String[] args) {
        BiConsumer<String,String> sapa =
        (nama, NIM) -> System.out.println("Hello World, I am" + " " + nama + "-" + NIM);
        sapa.accept("Indah", "240202866");
    }
}
```
OOP

```java
// HelloOOP.java
class Mahasiswa {
   String nama;
   String NIM;
   Mahasiswa(String nama, String NIM) {
      this.nama = nama;
      this.NIM = NIM;
   }
   void sapa(){ System.out.println("Hello World, I am" + " " + nama + "-" + NIM);}
}

public class HelloOOP {
   public static void main(String[] args) {
       Mahasiswa m = new Mahasiswa ("Indah", "240202866");
       m.sapa();
    }
}
```
Procedural

```java
// HelloProcedural.java
public class HelloProcedural {
    public static void main(String[] args) {
        String nim = "240202866";
        String nama = "Indah";

        System.out.println("Hello World, I am" + " " + nama + "-" + nim);
    }
}
```

---

## Hasil Eksekusi

- HelloFunvtional
![praktikum/week1-setup-hello-pos/screenshots/Functional.png](/praktikum/week1-setup-hello-pos/screenshots/Functional.png)

- HelloOOP
![praktikum/week1-setup-hello-pos/screenshots/OOP.png](/praktikum/week1-setup-hello-pos/screenshots/OOP.png)

- HelloProcedural
![praktikum/week1-setup-hello-pos/screenshots/Procedural.png](/praktikum/week1-setup-hello-pos/screenshots/Procedural.png)

---

## Analisis
(
1. Jelaskan bagaimana kode berjalan.  
a. Kode Functional (HelloFunctional.java)
   - Program menggunakan fungsi bawaan Java 8, yaitu BiConsumer<T,U> dari package java.util.function.
   - BiConsumer menerima dua parameter tanpa mengembalikan nilai.

   Baris ini:
   ```
   BiConsumer<String, String> sapa = (nama, NIM) -> System.out.println("Hello World, I am " + nama + "-" + NIM);

   ```
   mendefinisikan fungsi lambda yang langsung mencetak sapaan.

   - Fungsi dipanggil melalui:
   ```
   sapa.accept("Indah", "240202866");

   ```
   sehingga output-nya:
   ```
   Hello World, I am Indah-240202866
   
   ```
   Jadi program ini berjalan dengan paradigma fungsional, di mana logika dibuat sebagai fungsi (lambda) yang dapat dipanggil tanpa membuat objek atau langkah prosedural panjang.

b. Kode OOP (HelloOOP.java)

   - Program mendefinisikan class Mahasiswa yang memiliki atribut nama dan NIM, serta method sapa().
   - Di dalam main, dibuat objek m dari class Mahasiswa:
   ```
   Mahasiswa m = new Mahasiswa("Indah", "240202866");

   ```
   - Kemudian dipanggil method:
   ```
   m.sapa();

   ```
   untuk mencetak hasil:
   ```
   Hello World, I am Indah-240202866
   
   ```
   Pendekatan ini berorientasi objek, dengan fokus pada pembuatan class dan objek untuk menyimpan data dan perilaku.

c. Kode Procedural (HelloProcedural.java)

   - Program berjalan secara berurutan (step by step):
   - Mendeklarasikan variabel nama dan nim.
   - Mencetak hasil menggunakan System.out.println.
   - Tidak ada fungsi khusus atau class yang digunakan selain main.

2. Apa perbedaan pendekatan minggu ini dibanding minggu sebelumnya.  

   - Minggu sebelumnya memakai prosedural (langkah berurutan) dan OOP (menggunakan class & objek).

   - Minggu ini memakai functional, yaitu logika dibuat dalam bentuk fungsi lambda tanpa objek.

   - Functional lebih sederhana dan efisien, fokus pada apa yang dilakukan fungsi, bukan urutan langkah atau objeknya.


3. Kendala yang dihadapi dan cara mengatasinya.  

   - Versi Java lama → lambda butuh Java 8 ke atas → gunakan JDK terbaru.
  
)
---

## Kesimpulan
(Tuliskan kesimpulan dari praktikum minggu ini.  
Contoh: *Dengan menggunakan class dan object, program menjadi lebih terstruktur dan mudah dikembangkan.*)

---

## Quiz
1. Apakah OOP selalu lebih baik dari prosedural? 

   **Jawaban:** OOP tidak selalu lebih baik dari prosedural karena keduanya memiliki keunggulan masing-masing; OOP lebih cocok untuk program besar dan kompleks, sedangkan prosedural lebih efisien untuk program kecil dan sederhana. 

2. Kapan functional programming lebih cocok digunakan dibanding OOP atau prosedural?
   **Jawaban:** Functional programming lebih cocok digunakan saat program memerlukan pengolahan data yang kompleks, banyak perhitungan matematis, atau membutuhkan hasil yang konsisten tanpa efek samping, seperti pada analisis data, kecerdasan buatan, dan pemrosesan paralel.

3. Bagaimana paradigma (prosedural, OOP, fungsional) memengaruhi maintainability dan scalability aplikasi? 
   **Jawaban:** Paradigma pemrograman memengaruhi maintainability dan scalability dengan cara berbeda: pemrograman prosedural lebih mudah untuk program kecil namun sulit dikelola saat proyek membesar; OOP meningkatkan maintainability dan scalability karena kode terstruktur dalam class dan object yang mudah diperluas; sedangkan functional programming mempermudah pemeliharaan dan skalabilitas melalui kode yang bebas efek samping dan mudah dijalankan secara paralel.
 

4. Mengapa OOP lebih cocok untuk mengembangkan aplikasi POS dibanding prosedural?
   **Jawaban:**  OOP lebih cocok untuk mengembangkan aplikasi POS karena dapat memodelkan komponen sistem seperti produk, pelanggan, dan transaksi sebagai objek yang saling berinteraksi. Dengan konsep class, inheritance, dan enkapsulasi, OOP membuat kode lebih terstruktur, mudah dikembangkan, serta memudahkan penambahan fitur baru tanpa mengubah keseluruhan program.


5. Bagaimana paradigma fungsional dapat membantu mengurangi kode berulang (boilerplate code)?
   **Jawaban:** Paradigma fungsional dapat membantu mengurangi kode berulang karena menggunakan fungsi-fungsi murni dan konsep seperti higher-order function serta lambda expression yang memungkinkan penulisan logika umum sekali saja untuk digunakan berulang kali, sehingga kode menjadi lebih ringkas, modular, dan mudah dipelihara.