# Laporan Praktikum Minggu 7
Topik: Collections dan Implementasi Keranjang Belanja
## Identitas
- Nama  : [Indah Ruwahna Anugraheni]
- NIM   : [240202866]
- Kelas : [3IKRB]

---

## Tujuan

Mahasiswa mampu:
1. Menjelaskan konsep collection dalam Java (List, Map, Set).
2. Menggunakan ArrayList untuk menyimpan dan mengelola objek.
3. Mengimplementasikan Map atau Set sesuai kebutuhan pengelolaan data.
4. Melakukan operasi dasar pada collection: tambah, hapus, dan hitung total.
5. Menganalisis efisiensi penggunaan collection dalam konteks sistem Agri-POS.

---

## Dasar Teori
1. Java Collections Framework (JCF) Struktur data dinamis yang mampu menyimpan objek tanpa ditentukan ukurannya di awal. Berbeda dengan Array biasa, Collection dapat bertambah dan berkurang secara otomatis.

2. Karakteristik Utama

   - ArrayList: Menyimpan data berdasarkan Indeks. Keunggulannya adalah akses cepat secara berurutan, namun lambat untuk pencarian data spesifik.

   - HashMap: Menyimpan data berdasarkan Key-Value (Kunci-Nilai). Sangat efektif untuk sistem POS karena satu produk (Key) bisa langsung dihubungkan dengan jumlah belinya (Value).

   - HashSet: Koleksi yang menjamin Keunikan. Jika ada data duplikat yang masuk, otomatis akan ditolak.

3. Konsep Generics (<T>) Penggunaan tanda kurung sudut (misal: <Product>) memastikan koleksi hanya berisi tipe data yang ditentukan. Hal ini mencegah error saat aplikasi dijalankan (Type Safety).

4. Efisiensi Operasi Dalam keranjang belanja, Map lebih efisien daripada List karena tidak perlu melakukan perulangan (looping) berulang kali hanya untuk memperbarui jumlah (quantity) barang yang sama.

---

## Langkah Praktikum

**1. Membuat Class Product**

```java
// Contoh
Produk p1 = new Produk("BNH-001", "Benih Padi", 25000, 100);
System.out.println(p1.getNama());

```



---

## Kode Program
(Tuliskan kode utama yang dibuat, contoh:  

```java
// Contoh
Produk p1 = new Produk("BNH-001", "Benih Padi", 25000, 100);
System.out.println(p1.getNama());
```
)
---

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
![Screenshot hasil](screenshots/hasil.png)
)
---

## Analisis
(
- Jelaskan bagaimana kode berjalan.  
- Apa perbedaan pendekatan minggu ini dibanding minggu sebelumnya.  
- Kendala yang dihadapi dan cara mengatasinya.  
)
---

## Kesimpulan
(Tuliskan kesimpulan dari praktikum minggu ini.  
Contoh: *Dengan menggunakan class dan object, program menjadi lebih terstruktur dan mudah dikembangkan.*)

---

## Quiz
(1. [Tuliskan kembali pertanyaan 1 dari panduan]  
   **Jawaban:** …  

2. [Tuliskan kembali pertanyaan 2 dari panduan]  
   **Jawaban:** …  

3. [Tuliskan kembali pertanyaan 3 dari panduan]  
   **Jawaban:** …  )
