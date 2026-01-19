package com.upb.agripos.dao;

import com.upb.agripos.model.User;
import com.upb.agripos.util.DatabaseConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementasi JDBC untuk UserDAO (Bab 11)
 */
public class JdbcUserDAO implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(JdbcUserDAO.class.getName());

    @Override
    public User findByUsername(String username) throws Exception {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }

        String sql = "SELECT id, username, password, role FROM users WHERE username=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mencari user: " + username, e);
            throw e;
        }
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws Exception {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }

        String sql = "SELECT id, username, password, role FROM users WHERE username=? AND password=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Untuk demo, plain text. Produksi: hash
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal login user: " + username, e);
            throw e;
        }
    }

    @Override
    public void insert(User user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("User tidak boleh null");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Insert gagal, tidak ada baris yang terpengaruh");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            LOGGER.info("User berhasil ditambahkan: " + user.getUsername());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal menambah user: " + user.getUsername(), e);
            throw e;
        }
    }

    @Override
    public boolean existsByUsername(String username) throws Exception {
        if (username == null || username.isBlank()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM users WHERE username=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal mengecek username: " + username, e);
            throw e;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        return user;
    }
}
