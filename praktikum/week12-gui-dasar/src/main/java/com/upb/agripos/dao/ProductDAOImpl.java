package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductDAOImpl.class.getName());
    private Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) {
        if (p == null) throw new IllegalArgumentException("Product must not be null");
        if (p.getCode() == null || p.getCode().isBlank()) throw new IllegalArgumentException("Product code required");
        if (p.getPrice() <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (p.getStock() < 0) throw new IllegalArgumentException("Stock must be >= 0");

        String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getCode());
            stmt.setString(2, p.getName());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getStock());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Insert failed, no rows affected for product code: " + p.getCode());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to insert product: " + p, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product p) {
        if (p == null) throw new IllegalArgumentException("Product must not be null");
        String sql = "UPDATE products SET name = ?, price = ?, stock = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getStock());
            stmt.setString(4, p.getCode());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Update failed, no product found with code: " + p.getCode());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update product: " + p, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String code) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Code required");
        String sql = "DELETE FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Delete failed, no product found with code: " + code);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product code: " + code, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findByCode(String code) {
        String sql = "SELECT code, name, price, stock FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to find product by code: " + code, e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT code, name, price, stock FROM products ORDER BY code";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch products", e);
            throw new RuntimeException(e);
        }
        return list;
    }
}