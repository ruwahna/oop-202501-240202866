package com.upb.agripos.controller;

import com.upb.agripos.exception.AuthenticationException;
import com.upb.agripos.model.User;
import com.upb.agripos.service.AuthService;

/**
 * Controller untuk Login/Authentication (FR-5)
 */
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Proses login user
     * @return User yang berhasil login
     * @throws AuthenticationException jika login gagal
     */
    public User login(String username, String password) throws Exception {
        return authService.login(username, password);
    }

    /**
     * Proses logout user
     */
    public void logout() {
        authService.logout();
    }

    /**
     * Mendapatkan user yang sedang login
     */
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }

    /**
     * Cek apakah user sudah login
     */
    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    /**
     * Cek apakah user adalah admin
     */
    public boolean isAdmin() {
        return authService.isAdmin();
    }

    /**
     * Cek apakah user adalah kasir
     */
    public boolean isKasir() {
        return authService.isKasir();
    }

    /**
     * Mendapatkan role display name
     */
    public String getRoleDisplayName() {
        User user = authService.getCurrentUser();
        if (user == null) return "Guest";
        return user.getRole().getDisplayName();
    }
}
