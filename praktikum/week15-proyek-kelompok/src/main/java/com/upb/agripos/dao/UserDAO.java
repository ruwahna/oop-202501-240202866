package com.upb.agripos.dao;

import com.upb.agripos.model.User;

/**
 * Interface DAO untuk User (FR-5 Login dan Hak Akses)
 * Menerapkan DIP - Dependency Inversion Principle
 */
public interface UserDAO {
    
    /**
     * Mencari user berdasarkan username
     * @param username username pengguna
     * @return User atau null jika tidak ditemukan
     * @throws Exception jika gagal mencari
     */
    User findByUsername(String username) throws Exception;

    /**
     * Mencari user berdasarkan username dan password (untuk login)
     * @param username username pengguna
     * @param password password pengguna
     * @return User atau null jika tidak cocok
     * @throws Exception jika gagal mencari
     */
    User findByUsernameAndPassword(String username, String password) throws Exception;

    /**
     * Menambah user baru
     * @param user user yang akan ditambahkan
     * @throws Exception jika gagal menyimpan
     */
    void insert(User user) throws Exception;

    /**
     * Cek apakah username sudah ada
     * @param username username yang dicek
     * @return true jika sudah ada
     * @throws Exception jika gagal mengecek
     */
    boolean existsByUsername(String username) throws Exception;
}
