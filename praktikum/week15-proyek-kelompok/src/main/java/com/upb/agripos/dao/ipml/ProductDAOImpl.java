package com.upb.agripos.dao.ipml;

import com.upb.agripos.dao.DatabaseConnection;
import com.upb.agripos.dao.interfaces.ProductDAO;
import com.upb.agripos.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public void save(Product p) {
        String sql = "INSERT INTO products (kode, nama, kategori, harga, stok) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getKode());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getKategori());
            ps.setDouble(4, p.getHarga());
            ps.setInt(5, p.getStok());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                    rs.getString("kode"), rs.getString("nama"),
                    rs.getString("kategori"), rs.getDouble("harga"), rs.getInt("stok")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void update(Product p) {
        String sql = "UPDATE products SET nama = ?, kategori = ?, harga = ?, stok = ? WHERE kode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getKategori());
            ps.setDouble(3, p.getHarga());
            ps.setInt(4, p.getStok());
            ps.setString(5, p.getKode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String kode) {
        String sql = "DELETE FROM products WHERE kode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Product findByKode(String kode) {
        String sql = "SELECT * FROM products WHERE kode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getString("kode"), rs.getString("nama"),
                    rs.getString("kategori"), rs.getDouble("harga"), rs.getInt("stok")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding product by kode: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}