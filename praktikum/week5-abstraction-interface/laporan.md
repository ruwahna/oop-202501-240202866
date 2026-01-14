# Laporan Praktikum Minggu 5 
Topik: Abstraction (Abstract Class & Interface)

## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan
- Mahasiswa mampu **menjelaskan perbedaan abstract class dan interface**.
- Mahasiswa mampu **mendesain abstract class dengan method abstrak** sesuai kebutuhan kasus.
- Mahasiswa mampu **membuat interface dan mengimplementasikannya pada class**.
- Mahasiswa mampu **menerapkan multiple inheritance melalui interface** pada rancangan kelas.
- Mahasiswa mampu **mendokumentasikan kode** (komentar kelas/method, README singkat pada folder minggu).

---

## Dasar Teori
**Abstraksi** adalah proses menyederhanakan kompleksitas dengan menampilkan elemen penting dan menyembunyikan detail implementasi.
- **Abstract class**: tidak dapat diinstansiasi, dapat memiliki method abstrak (tanpa badan) dan non-abstrak. Dapat menyimpan state (field).
- **Interface**: kumpulan kontrak (method tanpa implementasi konkret). Sejak Java 8 mendukung default method. Mendukung **multiple inheritance** (class dapat mengimplementasikan banyak interface).
- Gunakan **abstract class** bila ada _shared state_ dan perilaku dasar; gunakan **interface** untuk mendefinisikan kemampuan/kontrak lintas hierarki.

Dalam konteks Agri-POS, **Pembayaran** dapat dimodelkan sebagai abstract class dengan method abstrak `prosesPembayaran()` dan `biaya()`. Implementasi konkritnya: `Cash` dan `EWallet`. Kemudian, interface seperti `Validatable` (mis. verifikasi OTP) dan `Receiptable` (mencetak bukti) dapat diimplementasikan oleh jenis pembayaran yang relevan.

---

## Langkah Praktikum
1. **Abstract Class – Pembayaran**
   - Buat `Pembayaran` (abstract) dengan field `invoiceNo`, `total` dan method:
     - `double biaya()` (abstrak) → biaya tambahan (fee).
     - `boolean prosesPembayaran()` (abstrak) → mengembalikan status berhasil/gagal.
     - `double totalBayar()` (konkrit) → `return total + biaya();`.

2. **Subclass Konkret**
   - `Cash` → biaya = 0, proses = selalu berhasil jika `tunai >= totalBayar()`.
   - `EWallet` → biaya = 1.5% dari `total`; proses = membutuhkan validasi.

3. **Interface**
   - `Validatable` → `boolean validasi();` (contoh: OTP).
   - `Receiptable` → `String cetakStruk();`

4. **Multiple Inheritance via Interface**
   - `EWallet` mengimplementasikan **dua interface**: `Validatable`, `Receiptable`.
   - `Cash` setidaknya mengimplementasikan `Receiptable`.

5. **Main Class**
    - Buat `MainAbstraction.java` untuk mendemonstrasikan pemakaian `Pembayaran` (polimorfik).
    - Tampilkan hasil proses dan struk. Di akhir, panggil `CreditBy.print("[NIM]", "[Nama]")`.

6. **Commit dan Push**
   - Commit dengan pesan: `week5-abstraction-interface`.

---

## Kode Program

Receiptable.java

```java
package main.java.com.upb.agripos.model.kontrak;

public interface Receiptable {
    String cetakStruk();
}

```

Validatable.java
```java
package main.java.com.upb.agripos.model.kontrak;

public interface Validatable {
    boolean validasi(); // misal validasi OTP/ PIN
}
```
Cash.java
```java
package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class Cash extends Pembayaran implements Receiptable {
    private double tunai;

    public Cash(String invoiceNo, double total, double tunai) {
        super(invoiceNo, total);
        this.tunai = tunai;
    }

    @Override
    public double biaya() {
        return 0.0;
    }

    @Override
    public boolean prosesPembayaran() {
        return tunai >= totalBayar(); // sederhana: cukup uang tunai
    }

    @Override
    public String cetakStruk() {
        return String.format("INVOICE %s | TOTAL: %.2f | BAYAR CASH: %.2f | KEMBALI: %.2f",
                invoiceNo, totalBayar(), tunai, Math.max(0, tunai - totalBayar()));
    }
}
```
EWallet.java
```java
package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Validatable;
import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class EWallet extends Pembayaran implements Validatable, Receiptable {
    private String akun;
    private String otp; // sederhana untuk simulasi

    public EWallet(String invoiceNo, double total, String akun, String otp) {
        super(invoiceNo, total);
        this.akun = akun;
        this.otp = otp;
    }

    @Override
    public double biaya() {
        return total * 0.015; // 1.5% fee
    }

    @Override
    public boolean validasi() {
        return otp != null && otp.length() == 6; // contoh validasi sederhana
    }

    @Override
    public boolean prosesPembayaran() {
        return validasi(); // jika validasi lolos, anggap berhasil
    }

    @Override
    public String cetakStruk() {
        return String.format("INVOICE %s | TOTAL+FEE: %.2f | E-WALLET: %s | STATUS: %s",
                invoiceNo, totalBayar(), akun, prosesPembayaran() ? "BERHASIL" : "GAGAL");
    }
}
```
Pembayaran.java
```java
package main.java.com.upb.agripos.model.pembayaran;

public abstract class Pembayaran {
    protected String invoiceNo;
    protected double total;

    public Pembayaran(String invoiceNo, double total) {
        this.invoiceNo = invoiceNo;
        this.total = total;
    }

    public abstract double biaya();               // fee/biaya tambahan
    public abstract boolean prosesPembayaran();   // proses spesifik tiap metode

    public double totalBayar() {
        return total + biaya();
    }

    public String getInvoiceNo() { return invoiceNo; }
    public double getTotal() { return total; }
}
```
TransferBank.java
```java
package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class TransferBank extends Pembayaran implements Receiptable {
    private String bankName;
    private String accountNo;

    public TransferBank(String invoiceNo, double total, String bankName, String accountNo) {
        super(invoiceNo, total);
        this.bankName = bankName;
        this.accountNo = accountNo;
    }

    @Override
    public double biaya() {
        return 3500.0; // biaya tetap untuk transfer bank
    }

    @Override
    public boolean prosesPembayaran() {
        // sederhana: anggap selalu berhasil
        return true;
    }

    @Override
    public String cetakStruk() {
        return String.format("INVOICE %s | TOTAL+FEE: %.2f | TRANSFER BANK: %s (%s) | STATUS: %s",
                invoiceNo, totalBayar(), bankName, accountNo, prosesPembayaran() ? "BERHASIL" : "GAGAL");
    }
}
```
---

## Hasil Eksekusi


![praktikum/week5-abstraction-interface/screenshots/week 5-abstraction-interface.png](/praktikum/week5-abstraction-interface/screenshots/week%205-abstraction-interface.png)

---

## Analisis

- Jelaskan bagaimana kode berjalan.  
  Pada praktikum ini, program menerapkan konsep abstraksi melalui abstract class Pembayaran, yang menyediakan kerangka dasar untuk semua metode pembayaran. Method abstrak biaya() dan prosesPembayaran() wajib diimplementasikan oleh subclass sehingga setiap jenis pembayaran dapat memiliki logika sendiri.

  Interface Validatable dan Receiptable memungkinkan penambahan kemampuan tanpa harus berada dalam satu hierarki pewarisan. Penerapan multiple inheritance digunakan pada EWallet, yang mengimplementasikan dua interface sekaligus.

- Apa perbedaan pendekatan minggu ini dibanding minggu sebelumnya.
  Dibanding minggu sebelumnya yang hanya membahas class dan object dasar, minggu ini lebih menekankan pemisahan struktur (abstract class) dan kemampuan (interface), sehingga desain lebih fleksibel dan mudah dikembangkan.

- Kendala yang dihadapi dan cara mengatasinya.  
  Kesalahan cast interface saat memanggil cetakStruk().
  Solusi: Memastikan objek yang di-cast memang mengimplementasikan interface tersebut.

  Kesalahan perhitungan fee.
  Solusi: Memindahkan logika perhitungan fee ke method biaya().

---

## Kesimpulan
Pada praktikum ini, mahasiswa berhasil memahami dan menerapkan konsep abstract class dan interface. Abstract class digunakan untuk menyatukan struktur dasar metode pembayaran, sementara interface digunakan untuk menambahkan kemampuan seperti validasi dan pencetakan struk. Dengan desain ini, program menjadi lebih fleksibel, terorganisir, dan mudah dikembangkan melalui konsep multiple inheritance.

---

## Quiz
1. Jelaskan perbedaan konsep dan penggunaan **abstract class** dan **interface**. 
 
   **Jawaban:** 
   Abstract class adalah kelas dasar yang dapat berisi atribut dan method dengan atau tanpa implementasi. Ia dipakai ketika objek-objek memiliki struktur dan perilaku dasar yang sama.
   Interface hanya berisi kontrak perilaku tanpa membawa state. Interface dipakai untuk memberi “kemampuan tambahan” yang bisa dimiliki berbagai kelas berbeda.

2. Mengapa **multiple inheritance** lebih aman dilakukan dengan interface pada Java?  

   **Jawaban:**
   Karena interface tidak membawa state atau implementasi, sehingga tidak menimbulkan konflik pewarisan. Interface hanya berisi kontrak, jadi sebuah class aman mengimplementasikan banyak interface tanpa risiko bentrok seperti pada pewarisan banyak class.

3. Pada contoh Agri-POS, bagian mana yang **paling tepat** menjadi abstract class dan mana yang menjadi interface? Jelaskan alasannya.

   **Jawaban:** 
   Yang paling tepat menjadi abstract class adalah Product, karena semua produk memiliki atribut dan perilaku dasar yang sama.
   Yang cocok menjadi interface adalah fitur-fitur seperti pembayaran (QRIS, cash), diskon, cetak struk, dan sinkronisasi, karena itu hanyalah kemampuan tambahan yang tidak harus dimiliki semua class.
