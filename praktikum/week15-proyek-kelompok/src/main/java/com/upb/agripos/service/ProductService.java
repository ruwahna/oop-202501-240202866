package com.upb.agripos.service;

import com.upb.agripos.dao.ipml.ProductDAOImpl;
import com.upb.agripos.dao.interfaces.ProductDAO;
import com.upb.agripos.model.Product;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.exception.OutOfStockException;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAOImpl();

    /**
     * Ambil semua produk
     */
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Tambah produk baru dengan validasi
     */
    public void addProduct(Product p) throws ValidationException {
        if (p.getKode() == null || p.getKode().isEmpty()) {
            throw new ValidationException("Kode", "Kode produk tidak boleh kosong");
        }
        if (p.getNama() == null || p.getNama().isEmpty()) {
            throw new ValidationException("Nama", "Nama produk tidak boleh kosong");
        }
        if (p.getHarga() <= 0) {
            throw new ValidationException("Harga", "Harga harus lebih dari 0");
        }
        if (p.getStok() < 0) {
            throw new ValidationException("Stok", "Stok tidak boleh negatif");
        }
        productDAO.save(p);
    }

    /**
     * Cari produk berdasarkan kode
     */
    public Product findByCode(String kode) {
        return productDAO.findByKode(kode);
    }

    /**
     * Update produk
     */
    public void updateProduct(Product p) throws ValidationException {
        if (p == null || p.getKode() == null) {
            throw new ValidationException("Produk tidak valid");
        }
        if (p.getHarga() <= 0) {
            throw new ValidationException("Harga", "Harga harus lebih dari 0");
        }
        if (p.getStok() < 0) {
            throw new ValidationException("Stok", "Stok tidak boleh negatif");
        }
        productDAO.update(p);
    }

    /**
     * Hapus produk berdasarkan kode
     */
    public void deleteProduct(String kode) {
        productDAO.delete(kode);
    }

    /**
     * Update stok produk
     */
    public void updateStock(String kode, int newStock) throws ValidationException {
        if (newStock < 0) {
            throw new ValidationException("Stok", "Stok tidak boleh negatif");
        }
        Product p = findByCode(kode);
        if (p == null) {
            throw new ValidationException("Produk", "Produk dengan kode " + kode + " tidak ditemukan");
        }
        p.setStok(newStock);
        productDAO.update(p);
    }

    /**
     * Kurangi stok produk dengan validasi
     */
    public void decreaseStock(String kode, int qty) throws OutOfStockException, ValidationException {
        if (qty <= 0) {
            throw new ValidationException("Qty", "Jumlah harus lebih dari 0");
        }
        Product p = findByCode(kode);
        if (p == null) {
            throw new ValidationException("Produk", "Produk dengan kode " + kode + " tidak ditemukan");
        }
        if (p.getStok() < qty) {
            throw new OutOfStockException(p.getNama(), p.getStok(), qty);
        }
        p.setStok(p.getStok() - qty);
        productDAO.update(p);
    }

    /**
     * Tambah stok produk
     */
    public void increaseStock(String kode, int qty) throws ValidationException {
        if (qty <= 0) {
            throw new ValidationException("Qty", "Jumlah harus lebih dari 0");
        }
        Product p = findByCode(kode);
        if (p == null) {
            throw new ValidationException("Produk", "Produk dengan kode " + kode + " tidak ditemukan");
        }
        p.setStok(p.getStok() + qty);
        productDAO.update(p);
    }
}