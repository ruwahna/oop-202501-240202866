// Baris ini disesuaikan dengan struktur folder di VS Code Anda agar error hilang
package main.java.com.upb.agripos;

// library java hashmap dan map
import java.util.HashMap;
import java.util.Map;

// Constructor 
public class ShoppingCartMap {
    private final Map<Product, Integer> items = new HashMap<>();

    // factory method dari class ShoppingCartMap
    public void addProduct(Product p) { 
        items.put(p, items.getOrDefault(p, 0) + 1); 
    }

    // fungsi menghapus produk turunan dari class Product
    public void removeProduct(Product p) {
        if (!items.containsKey(p)) return;
        int qty = items.get(p);
        if (qty > 1) items.put(p, qty - 1);
        else items.remove(p);
    }
    
    // fungsi mendapatkan total harga
    public double getTotal() {
        double total = 0;
        // mapping product dan quantity
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
    
    // fungsi mencetak isi keranjang 
    public void printCart() {
        System.out.println("Isi Keranjang (Map):");
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            System.out.println("- " + e.getKey().getCode() + " " + e.getKey().getName() + " x" + e.getValue());
        }
        System.out.println("Total: " + getTotal());
        System.out.println("-----------------------------------");
    }
}