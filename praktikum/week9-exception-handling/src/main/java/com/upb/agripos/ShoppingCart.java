package main.java.com.upb.agripos;
import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        // Pesan disesuaikan dengan gambar Anda
        if (qty <= 0) throw new InvalidQuantityException("Quantity harus lebih dari 0.");
        for (int i = 0; i < qty; i++) {
            items.add(p);
        }
    }

    public void removeProduct(Product p) throws ProductNotFoundException {
        // Pesan disesuaikan dengan gambar Anda
        if (!items.contains(p)) throw new ProductNotFoundException("Produk tidak ada dalam keranjang.");
        items.remove(p);
    }

    public void checkout() throws InsufficientStockException {
        for (Product p : items) {
            // Jika jumlah di keranjang melebihi stok yang ada
            if (p.getStock() < 1) { 
                throw new InsufficientStockException("Stok tidak cukup untuk: " + p.getName());
            }
        }
        System.out.println("Checkout Berhasil!");
        items.clear();
    }
}