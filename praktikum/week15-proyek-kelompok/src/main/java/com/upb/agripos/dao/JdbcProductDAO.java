package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import com.upb.agripos.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementasi JDBC untuk ProductDAO (Bab 11)
 * Menggunakan PreparedStatement untuk keamanan dari SQL Injection
 */
public class JdbcProductDAO implements ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(JdbcProductDAO.class.getName());

    @Override
    public void insert(Product product) throws Exception {
        validateProduct(product);

        String sql = "INSERT INTO products (code, name, category, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStock());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Insert gagal, tidak ada baris yang terpengaruh untuk produk: " + product.getCode());
            }
            LOGGER.info("Produk berhasil ditambahkan: " + product.getCode());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menambah produk: " + product, e);
            throw e;
        }
    }

    @Override
    public void update(Product product) throws Exception {
        validateProduct(product);

        String sql = "UPDATE products SET name=?, category=?, price=?, stock=? WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getCode());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Update gagal, produk tidak ditemukan: " + product.getCode());
            }
            LOGGER.info("Produk berhasil diupdate: " + product.getCode());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mengupdate produk: " + product, e);
            throw e;
        }
    }

    @Override
    public void delete(String code) throws Exception {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }

        String sql = "DELETE FROM products WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Delete gagal, produk tidak ditemukan: " + code);
            }
            LOGGER.info("Produk berhasil dihapus: " + code);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menghapus produk: " + code, e);
            throw e;
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }

        String sql = "SELECT code, name, category, price, stock FROM products WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari produk: " + code, e);
            throw e;
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        String sql = "SELECT code, name, category, price, stock FROM products ORDER BY code";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mengambil daftar produk", e);
            throw e;
        }
        return products;
    }

    @Override
    public List<Product> findByCategory(String category) throws Exception {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Kategori tidak boleh kosong");
        }

        String sql = "SELECT code, name, category, price, stock FROM products WHERE category=? ORDER BY code";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari produk berdasarkan kategori: " + category, e);
            throw e;
        }
        return products;
    }

    @Override
    public void updateStock(String code, int newStock) throws Exception {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }
        if (newStock < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }

        String sql = "UPDATE products SET stock=? WHERE code=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newStock);
            stmt.setString(2, code);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Update stok gagal, produk tidak ditemukan: " + code);
            }
            LOGGER.info("Stok produk " + code + " diupdate menjadi: " + newStock);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mengupdate stok produk: " + code, e);
            throw e;
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setCode(rs.getString("code"));
        product.setName(rs.getString("name"));
        product.setCategory(rs.getString("category"));
        product.setPrice(rs.getDouble("price"));
        product.setStock(rs.getInt("stock"));
        return product;
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produk tidak boleh null");
        }
        if (product.getCode() == null || product.getCode().isBlank()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }
    }
}
