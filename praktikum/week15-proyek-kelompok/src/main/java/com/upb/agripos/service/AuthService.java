package com.upb.agripos.service;

import com.upb.agripos.dao.UserDAO;
import com.upb.agripos.exception.AuthenticationException;
import com.upb.agripos.exception.ValidationException;
import com.upb.agripos.model.User;

/**
 * Service layer untuk Authentication (FR-5 Login dan Hak Akses)
 * Menerapkan SRP - Single Responsibility Principle
 */
public class AuthService {
    private final UserDAO userDAO;
    private User currentUser;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.currentUser = null;
    }

    /**
     * Login user
     * @param username username pengguna
     * @param password password pengguna
     * @return User yang berhasil login
     * @throws AuthenticationException jika login gagal
     */
    public User login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username tidak boleh kosong");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password tidak boleh kosong");
        }

        User user = userDAO.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new AuthenticationException("Username atau password salah");
        }

        this.currentUser = user;
        return user;
    }

    /**
     * Logout user
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Mendapatkan user yang sedang login
     * @return User atau null jika belum login
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Cek apakah ada user yang sedang login
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Cek apakah user saat ini adalah Admin
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Cek apakah user saat ini adalah Kasir
     */
    public boolean isKasir() {
        return currentUser != null && currentUser.isKasir();
    }

    /**
     * Cek apakah user memiliki akses ke fitur tertentu
     * @param requiredRole role yang dibutuhkan
     * @return true jika memiliki akses
     */
    public boolean hasAccess(User.Role requiredRole) {
        if (currentUser == null) {
            return false;
        }
        // Admin memiliki akses ke semua fitur
        if (currentUser.isAdmin()) {
            return true;
        }
        // Kasir hanya bisa akses fitur kasir
        return currentUser.getRole() == requiredRole;
    }

    /**
     * Mendapatkan username user saat ini
     */
    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "Guest";
    }

    /**
     * Register user baru (hanya untuk admin)
     */
    public void registerUser(User user) throws Exception {
        if (!isAdmin()) {
            throw new AuthenticationException("Hanya Admin yang dapat mendaftarkan user baru");
        }

        if (user == null) {
            throw new ValidationException("User tidak boleh null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username tidak boleh kosong");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidationException("Password tidak boleh kosong");
        }

        if (userDAO.existsByUsername(user.getUsername())) {
            throw new ValidationException("Username sudah digunakan: " + user.getUsername());
        }

        userDAO.insert(user);
    }
}
