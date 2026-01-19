package com.upb.agripos.model;

/**
 * Model class untuk User/Login (FR-5 Login dan Hak Akses)
 * Mendukung 2 peran: KASIR dan ADMIN
 */
public class User {
    private int id;
    private String username;
    private String password; // Untuk demo, plain text. Produksi: hash
    private Role role;

    /**
     * Enum untuk mendefinisikan role pengguna
     * Sesuai dengan Use Case Diagram Week 6:
     * - KASIR: Menangani transaksi checkout dan cetak struk
     * - ADMIN: Mengelola data akun pengguna, produk, dan memantau laporan
     */
    public enum Role {
        KASIR("Kasir"),
        ADMIN("Admin");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public User() {}

    public User(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, Role role) {
        this(0, username, password, role);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Cek apakah user adalah Admin
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    /**
     * Cek apakah user adalah Kasir
     */
    public boolean isKasir() {
        return role == Role.KASIR;
    }

    @Override
    public String toString() {
        return String.format("User[%d] %s (%s)", id, username, role.getDisplayName());
    }
}
