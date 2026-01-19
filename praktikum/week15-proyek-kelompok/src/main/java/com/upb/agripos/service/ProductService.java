package com.upb.agripos.service;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.exception.DataNotFoundException;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.model.Product;

import java.util.List;

/**
 * Service layer untuk Product (FR-1 Manajemen Produk)
 * Menerapkan SRP - Single Responsibility Principle
 */
public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Menambah produk baru
     */
    public void addProduct(Product product) throws Exception {
        validateProduct(product);
        
        // Cek apakah kode sudah ada
        Product existing = productDAO.findByCode(product.getCode());
        if (existing != null) {
            throw new ValidationException("Kode produk sudah digunakan: " + product.getCode());
        }
        
        productDAO.insert(product);
    }

    /**
     * Mengupdate produk
     */
    public void updateProduct(Product product) throws Exception {
        validateProduct(product);
        
        // Cek apakah produk ada
        Product existing = productDAO.findByCode(product.getCode());
        if (existing == null) {
            throw new DataNotFoundException("Product", product.getCode());
        }
        
        productDAO.update(product);
    }

    /**
     * Menghapus produk
     */
    public void deleteProduct(String code) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }
        
        Product existing = productDAO.findByCode(code);
        if (existing == null) {
            throw new DataNotFoundException("Product", code);
        }
        
        productDAO.delete(code);
    }

    /**
     * Mencari produk berdasarkan kode
     */
    public Product findByCode(String code) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }
        return productDAO.findByCode(code);
    }

    /**
     * Mendapatkan semua produk
     */
    public List<Product> findAll() throws Exception {
        return productDAO.findAll();
    }

    /**
     * Mencari produk berdasarkan kategori
     */
    public List<Product> findByCategory(String category) throws Exception {
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("Kategori tidak boleh kosong");
        }
        return productDAO.findByCategory(category);
    }

    /**
     * Mengupdate stok produk
     */
    public void updateStock(String code, int newStock) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }
        if (newStock < 0) {
            throw new ValidationException("Stok tidak boleh negatif");
        }
        
        Product existing = productDAO.findByCode(code);
        if (existing == null) {
            throw new DataNotFoundException("Product", code);
        }
        
        productDAO.updateStock(code, newStock);
    }

    /**
     * Mengurangi stok produk (untuk transaksi)
     */
    public void reduceStock(String code, int quantity) throws Exception {
        Product product = findByCode(code);
        if (product == null) {
            throw new DataNotFoundException("Product", code);
        }
        
        int newStock = product.getStock() - quantity;
        if (newStock < 0) {
            throw new ValidationException("Stok tidak mencukupi untuk produk: " + code);
        }
        
        productDAO.updateStock(code, newStock);
    }

    /**
     * Validasi data produk
     */
    private void validateProduct(Product product) throws ValidationException {
        if (product == null) {
            throw new ValidationException("Produk tidak boleh null");
        }
        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ValidationException("Nama produk tidak boleh kosong");
        }
        if (product.getPrice() < 0) {
            throw new ValidationException("Harga tidak boleh negatif");
        }
        if (product.getStock() < 0) {
            throw new ValidationException("Stok tidak boleh negatif");
        }
    }
}
