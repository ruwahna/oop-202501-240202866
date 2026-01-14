package com.upb.agripos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductDAOImpl.class.getName());

    private final Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) throws Exception {
        if (p == null) throw new IllegalArgumentException("Product must not be null");
        if (p.getCode() == null || p.getCode().isBlank()) throw new IllegalArgumentException("Product code required");
        if (p.getPrice() <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (p.getStock() < 0) throw new IllegalArgumentException("Stock must be >= 0");

        String sql = "INSERT INTO products(code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Insert failed, no rows affected for product code: " + p.getCode());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to insert product: " + p, e);
            throw e;
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT code, name, price, stock FROM products WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to find product by code: " + code, e);
            throw e;
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT code, name, price, stock FROM products ORDER BY code";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
            throw e;
        }
        return list;
    }

    @Override
    public void update(Product p) throws Exception {
        if (p == null) throw new IllegalArgumentException("Product must not be null");
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getCode());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Update failed, no product found with code: " + p.getCode());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update product: " + p, e);
            throw e;
        }
    }

    @Override
    public void delete(String code) throws Exception {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Code required");
        String sql = "DELETE FROM products WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Delete failed, no product found with code: " + code);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product code: " + code, e);
            throw e;
        }
    }
}