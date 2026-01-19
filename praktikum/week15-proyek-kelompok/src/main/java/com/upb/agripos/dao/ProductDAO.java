package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.List;

/**
 * Interface DAO untuk Product (FR-1 Manajemen Produk)
 * Menerapkan DIP - Dependency Inversion Principle (Bab 6)
 */
public interface ProductDAO {
    
    /**
     * Menambah produk baru ke database
     * @param product produk yang akan ditambahkan
     * @throws Exception jika gagal menyimpan
     */
    void insert(Product product) throws Exception;

    /**
     * Mengupdate data produk
     * @param product produk dengan data terbaru
     * @throws Exception jika gagal update
     */
    void update(Product product) throws Exception;

    /**
     * Menghapus produk berdasarkan kode
     * @param code kode produk
     * @throws Exception jika gagal menghapus
     */
    void delete(String code) throws Exception;

    /**
     * Mencari produk berdasarkan kode
     * @param code kode produk
     * @return Product atau null jika tidak ditemukan
     * @throws Exception jika gagal mencari
     */
    Product findByCode(String code) throws Exception;

    /**
     * Mendapatkan semua produk
     * @return List semua produk
     * @throws Exception jika gagal membaca
     */
    List<Product> findAll() throws Exception;

    /**
     * Mencari produk berdasarkan kategori
     * @param category kategori produk
     * @return List produk dengan kategori tersebut
     * @throws Exception jika gagal mencari
     */
    List<Product> findByCategory(String category) throws Exception;

    /**
     * Mengupdate stok produk
     * @param code kode produk
     * @param newStock stok baru
     * @throws Exception jika gagal update
     */
    void updateStock(String code, int newStock) throws Exception;
}
