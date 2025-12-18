package main.java.com.upb.agripos;
import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    // Versi Week 9: Mendukung kuantitas dan melempar Exception
    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        if (qty <= 0) throw new InvalidQuantityException("Quantity harus lebih dari 0.");
        for (int i = 0; i < qty; i++) {
            items.add(p);
        }
    }

    // Versi Week 7: Mendukung pemanggilan tanpa qty (memperbaiki error di image_93e7db.png)
    public void addProduct(Product p) {
        items.add(p);
    }

    public void removeProduct(Product p) throws ProductNotFoundException {
        if (!items.contains(p)) throw new ProductNotFoundException("Produk tidak ada dalam keranjang.");
        items.remove(p);
    }

    public void checkout() throws InsufficientStockException {
        for (Product p : items) {
            if (p.getStock() < 1) { 
                throw new InsufficientStockException("Stok tidak cukup untuk: " + p.getName());
            }
        }
        System.out.println("Checkout Berhasil!");
        items.clear();
    }

    // Memperbaiki error "printCart() is undefined"
    public void printCart() {
        System.out.println("Isi Keranjang:");
        for (Product p : items) {
            System.out.println("- " + p.getName() + " = " + p.getPrice());
        }
    }
}