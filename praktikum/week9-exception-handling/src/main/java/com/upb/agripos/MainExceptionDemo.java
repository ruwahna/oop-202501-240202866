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