package main.java.com.upb.agripos;

public class MainCartMap {
    public static void main(String[] args) {
        System.out.println("Hello, I am Indah Ruwahna Anugraheni-240202866 (Week7)");

        // Menambahkan parameter stok (misal: 10) agar sesuai dengan class Product yang baru
        Product p1 = new Product("P01", "Beras", 50000, 10);
        Product p2 = new Product("P02", "Pupuk", 30000, 10);
        Product p3 = new Product("P03", "Insektisida", 150000, 10);

        ShoppingCartMap cart = new ShoppingCartMap();
        
        // Menambahkan produk ke dalam Map
        cart.addProduct(p1);
        cart.addProduct(p1); // Beras jadi x2
        cart.addProduct(p2);
        cart.addProduct(p2); // Pupuk jadi x2
        cart.addProduct(p3);
        
        cart.printCart();

        System.out.println("\nSetelah menghapus " + p1.getCode() + " " + p1.getName() + " dari keranjang:");
        
        // Menghapus satu buah p1 (Beras)
        cart.removeProduct(p1);
        cart.printCart();
    }
}