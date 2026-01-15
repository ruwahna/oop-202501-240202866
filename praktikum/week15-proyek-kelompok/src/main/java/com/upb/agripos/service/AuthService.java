package com.upb.agripos.service;

import com.upb.agripos.dao.ipml.UserDAOImpl;
import com.upb.agripos.dao.interfaces.UserDAO;
import com.upb.agripos.model.User;
import com.upb.agripos.exception.AuthenticationException;
import com.upb.agripos.exception.ValidationException;
import java.util.List;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AuthService {
    private final UserDAO userDAO = new UserDAOImpl();
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int LOCKOUT_MINUTES = 5; // Lockout selama 5 menit
    private int loginAttempts = 0;
    private String lastFailedUsername = null;
    private LocalDateTime lockoutTime = null;
    
    // Singleton pattern
    private static AuthService instance;
    
    private AuthService() {}
    
    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Login dengan username dan password
     */
    public User login(String username, String password) throws AuthenticationException {
        if (username == null || username.isEmpty()) {
            throw new AuthenticationException(username, "Username tidak boleh kosong");
        }
        if (password == null || password.isEmpty()) {
            throw new AuthenticationException(username, "Password tidak boleh kosong");
        }
        
        // Cek apakah masih dalam periode lockout
        if (lastFailedUsername != null && lastFailedUsername.equals(username) && 
            loginAttempts >= MAX_LOGIN_ATTEMPTS && lockoutTime != null) {
            long minutesElapsed = ChronoUnit.MINUTES.between(lockoutTime, LocalDateTime.now());
            if (minutesElapsed < LOCKOUT_MINUTES) {
                long remainingMinutes = LOCKOUT_MINUTES - minutesElapsed;
                throw new AuthenticationException(username, 
                    "Terlalu banyak percobaan login gagal. Coba lagi dalam " + remainingMinutes + " menit");
            } else {
                // Reset lockout jika waktu sudah lewat
                loginAttempts = 0;
                lastFailedUsername = null;
                lockoutTime = null;
            }
        }
        
        User user = userDAO.findByUsername(username);
        
        if (user == null) {
            recordFailedAttempt(username);
            throw new AuthenticationException(username, "User tidak ditemukan");
        }
        
        if (!user.getPassword().equals(password)) {
            recordFailedAttempt(username);
            throw new AuthenticationException(username, "Password salah");
        }
        
        // Reset login attempts saat login berhasil
        loginAttempts = 0;
        lastFailedUsername = null;
        lockoutTime = null;
        
        return user;
    }
    
    /**
     * Catat percobaan login gagal
     */
    private void recordFailedAttempt(String username) {
        if (lastFailedUsername != null && !lastFailedUsername.equals(username)) {
            // Reset counter jika username berbeda
            loginAttempts = 0;
        }
        
        loginAttempts++;
        lastFailedUsername = username;
        
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            lockoutTime = LocalDateTime.now();
        }
    }

    /**
     * Authenticate user dengan username dan password (return boolean)
     */
    public boolean authenticate(String username, String password) {
        try {
            login(username, password);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    /**
     * Register user baru dengan validasi lengkap
     */
    public void register(String username, String password, String role) throws AuthenticationException, ValidationException {
        // Validasi username
        if (username == null || username.isEmpty()) {
            throw new AuthenticationException("Username tidak boleh kosong");
        }
        if (username.length() < 4) {
            throw new ValidationException("Username", "Username minimal 4 karakter");
        }
        if (!username.matches("[a-zA-Z0-9_]+")) {
            throw new ValidationException("Username", "Username hanya boleh mengandung huruf, angka, dan underscore");
        }
        
        // Validasi password
        if (password == null || password.isEmpty()) {
            throw new AuthenticationException("Password tidak boleh kosong");
        }
        if (password.length() < 6) {
            throw new ValidationException("Password", "Password minimal 6 karakter");
        }
        
        // Cek apakah username sudah ada
        if (userDAO.usernameExists(username)) {
            throw new AuthenticationException(username, "Username sudah terdaftar");
        }
        
        String userRole = role != null && !role.isEmpty() ? role : "USER";
        User newUser = new User(username, password, userRole);
        userDAO.save(newUser);
    }

    /**
     * Update password user
     */
    public void changePassword(String username, String oldPassword, String newPassword) throws AuthenticationException, ValidationException {
        // Verifikasi old password terlebih dahulu
        User user = login(username, oldPassword);
        
        // Validasi new password
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ValidationException("Password Baru", "Password baru tidak boleh kosong");
        }
        if (newPassword.length() < 6) {
            throw new ValidationException("Password Baru", "Password minimal 6 karakter");
        }
        if (newPassword.equals(oldPassword)) {
            throw new ValidationException("Password Baru", "Password baru tidak boleh sama dengan password lama");
        }
        
        // Update password
        userDAO.updatePassword(username, newPassword);
    }

    /**
     * Get user berdasarkan username
     */
    public User getUserByUsername(String username) throws AuthenticationException {
        if (username == null || username.isEmpty()) {
            throw new AuthenticationException("Username tidak boleh kosong");
        }
        
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException(username, "User tidak ditemukan");
        }
        return user;
    }

    /**
     * Get semua users dengan role tertentu
     */
    public List<User> getUsersByRole(String role) throws ValidationException {
        if (role == null || role.isEmpty()) {
            throw new ValidationException("Role", "Role tidak boleh kosong");
        }
        return userDAO.findByRole(role);
    }

    /**
     * Delete user
     */
    public void deleteUser(String username) throws ValidationException {
        if (username == null || username.isEmpty()) {
            throw new ValidationException("Username", "Username tidak boleh kosong");
        }
        userDAO.delete(username);
    }

    /**
     * Get semua users
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Reset login attempts counter
     */
    public void resetLoginAttempts() {
        loginAttempts = 0;
        lastFailedUsername = null;
    }

    /**
     * Validasi format username
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.length() >= 4 && username.matches("[a-zA-Z0-9_]+");
    }

    /**
     * Validasi format password
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }
}