package main.java.com.upb.agripos;

public class MainCart {
    // TAMBAHKAN 'throws Exception' di sini agar error handling Week 9 tidak memblokir Week 7
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, I am Indah Ruwahna Anugraheni-240202866 (Week 7)");

        // Pastikan ada 4 parameter: (Kode, Nama, Harga, Stok)
        Product p1 = new Product("P01", "Beras", 50000, 10);
        Product p2 = new Product("P02", "Pupuk", 30000, 10);

        ShoppingCart cart = new ShoppingCart();
        
        // Sekarang ini tidak akan error karena kita sudah pakai Overloading
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.printCart(); // Error 'undefined' akan hilang jika method sudah ditambah di ShoppingCart
    }
}