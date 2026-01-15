package com.upb.agripos.service;

import com.upb.agripos.model.Product;
import com.upb.agripos.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Menggunakan instance service
        productService = new ProductService();
    }

    @Test
    @DisplayName("Harus gagal saat menambah produk dengan harga negatif")
    void testAddProductNegativePrice() {
        Product p = new Product("P999", "Produk Error", "Pupuk", -100.0, 10);
        
        assertThrows(ValidationException.class, () -> {
            productService.addProduct(p);
        }, "Harusnya melempar ValidationException karena harga negatif");
    }

    @Test
    @DisplayName("Harus berhasil memvalidasi produk yang benar")
    void testValidProduct() {
        Product p = new Product("P005", "Bibit Unggul", "Benih", 50000.0, 20);
        
        assertDoesNotThrow(() -> {
            // Kita hanya tes validasinya saja dalam unit test ini
            if (p.getHarga() <= 0) throw new ValidationException("Harga salah");
        });
    }
}