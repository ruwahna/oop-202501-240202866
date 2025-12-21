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
package main.java.com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}

```
**2.  Implementasi Keranjang dengan ArrayList**

```java
package main.java.com.upb.agripos;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p) { items.add(p); }
    public void removeProduct(Product p) { items.remove(p); }

    public double getTotal() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void printCart() {
        System.out.println("Isi Keranjang:");
        for (Product p : items) {
            System.out.println("- " + p.getCode() + " " + p.getName() + " = " + p.getPrice());
        }
        System.out.println("Total: " + getTotal());
    }
}

```

**3. Main Program (WAJIB DIISI)**


```java
package main.java.com.upb.agripos;

public class MainCart {
    public static void main(String[] args) {
        System.out.println("Hello, I am Indah Ruwahna Anugraheni (Week7)");

        Product p1 = new Product("P01", "Beras", 50000);
        Product p2 = new Product("P02", "Pupuk", 30000);
        Product p3 = new Product("P03", "Insektisida", 150000);

        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.printCart();
        
        System.out.println("\nSetelah menghapus " + p1.getCode() + " " + p1.getName() + " dari keranjang:");
        cart.removeProduct(p1);
        cart.printCart();
    }
}
```

**4. Implementasi Alternatif Menggunakan Map (Dengan Quantity)**

```java
package main.java.com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartMap {
    private final Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p) { items.put(p, items.getOrDefault(p, 0) + 1); }

    public void removeProduct(Product p) {
        if (!items.containsKey(p)) return;
        int qty = items.get(p);
        if (qty > 1) items.put(p, qty - 1);
        else items.remove(p);
    }

    public double getTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void printCart() {
        System.out.println("Isi Keranjang (Map):");
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            System.out.println("- " + e.getKey().getCode() + " " + e.getKey().getName() + " = " + e.getKey().getPrice() + " x" + e.getValue());
        }
        System.out.println("Total: " + getTotal());
    }
}

```

---

## Kode Program
 
**Menggunakan ArrayList**

```java
package main.java.com.upb.agripos;

public class MainCart {
    public static void main(String[] args) {
        System.out.println("Hello, I am Indah Ruwahna Anugraheni (Week7)");

        Product p1 = new Product("P01", "Beras", 50000);
        Product p2 = new Product("P02", "Pupuk", 30000);
        Product p3 = new Product("P03", "Insektisida", 150000);

        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.printCart();
        
        System.out.println("\nSetelah menghapus " + p1.getCode() + " " + p1.getName() + " dari keranjang:");
        cart.removeProduct(p1);
        cart.printCart();
    }
}
```

**Menggunakan Map**
```java
package main.java.com.upb.agripos;

public class MainCartMap {
    public static void main(String[] args) {
        System.out.println("Indah Ruwahna Anugraheni-240202866 (Week7)");

        Product p1 = new Product("P01", "Beras", 50000);
        Product p2 = new Product("P02", "Pupuk", 30000);
        Product p3 = new Product("P03", "Insektisida", 150000);

        ShoppingCartMap cart = new ShoppingCartMap();
        cart.addProduct(p1);
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.printCart();

        System.out.println("\nSetelah menghapus " + p1.getCode() + " " + p1.getName() + " dari keranjang:");
        
        cart.removeProduct(p1);
        cart.printCart();
    }
}
```
---

## Hasil Eksekusi

**Menggunakan ArrayList**

![Screenshot hasil](/praktikum/week7-koleksi-keranjang/screenshots/week%207%20Menggunakan%20ArrayList.png)


**Menggunakan Map**

![Screenshot hasil](/praktikum/week7-koleksi-keranjang/screenshots/week%207%20menggunakan%20map.png)

---

## Analisis
Berdasarkan hasil eksekusi program yang telah dilakukan:

1. Mekanisme Operasi: Pada penggunaan ArrayList, jika pembeli membeli 3 buah "Pupuk", maka list akan menyimpan 3 objek yang sama secara terpisah. Hal ini kurang efisien dalam penggunaan memori dan tampilan struk. Sedangkan pada HashMap, sistem secara cerdas mengenali objek yang sama dan hanya mengupdate angka kuantitasnya.

2. Kompleksitas Pencarian: Menggunakan Map memberikan keuntungan pada metode removeProduct. Kita tidak perlu mencari indeks barang satu per satu; cukup panggil kunci produknya, dan sistem akan langsung melakukan pengurangan stok atau penghapusan data.

3. Kendala Praktikum: Awalnya terjadi kebingungan saat menghitung total harga pada Map. Namun, kendala ini teratasi dengan menggunakan Map.Entry untuk mengambil harga dari kunci (Key) dan mengalikannya dengan jumlah dari nilai (Value).
---

## Kesimpulan
Melalui praktikum ini, dapat disimpulkan bahwa Java Collections Framework memberikan kemudahan dalam mengelola data dinamis. ArrayList sangat mudah digunakan untuk daftar sederhana, namun HashMap memberikan efisiensi yang lebih tinggi untuk kasus keranjang belanja karena kemampuan pemetaan key-value yang mencegah redundansi data dan mempermudah pengelolaan kuantitas barang.

---

## Quiz
1. Kapan sebaiknya kita menggunakan ArrayList dibandingkan LinkedList?

   Jawaban: Kita menggunakan ArrayList saat aplikasi lebih sering melakukan operasi membaca (akses) data berdasarkan indeks karena kecepatannya konstan ($O(1)$). Sedangkan LinkedList lebih baik jika aplikasi sangat sering melakukan tambah/hapus data di tengah daftar.

2. Apa kegunaan dari Generics (misal: <Product>) pada Collection?

   Jawaban: Generics memberikan Type Safety. Hal ini memastikan bahwa koleksi tersebut hanya bisa diisi oleh objek tipe Product, sehingga mencegah error ClassCastException saat program dijalankan dan memudahkan proses pemanggilan method tanpa perlu casting manual.

3. Apa yang terjadi jika kita memasukkan Key yang sama ke dalam sebuah HashMap? 

   Jawaban: HashMap tidak mengizinkan adanya Key yang duplikat. Jika kita memasukkan Key yang sudah ada, maka nilai (Value) yang lama akan ditimpa dengan nilai yang baru (update).
