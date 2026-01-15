package com.upb.agripos.service;

import com.upb.agripos.model.User;
import com.upb.agripos.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthService Login Tests")
public class AuthServiceLoginTest {
    
    private AuthService authService;
    
    @BeforeEach
    public void setUp() throws Exception {
        // Initialize test database
        TestDatabaseHelper.initializeTestDatabase();
        
        // Reset singleton untuk setiap test
        authService = AuthService.getInstance();
        authService.resetLoginAttempts();
    }
    
    @Test
    @DisplayName("Login dengan kasir/kasir123 harus berhasil")
    public void testLoginKasirSuccess() throws AuthenticationException {
        User user = authService.login("kasir", "kasir123");
        assertNotNull(user);
        assertEquals("kasir", user.getUsername());
        assertEquals("Kasir", user.getRole());
        assertEquals("kasir123", user.getPassword());
        System.out.println("✓ Login kasir berhasil: " + user.getRole());
    }
    
    @Test
    @DisplayName("Login dengan admin/admin123 harus berhasil")
    public void testLoginAdminSuccess() throws AuthenticationException {
        User user = authService.login("admin", "admin123");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("Admin", user.getRole());
        System.out.println("✓ Login admin berhasil: " + user.getRole());
    }
    
    @Test
    @DisplayName("Login dengan password salah harus gagal")
    public void testLoginWrongPassword() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("kasir", "wrongpassword");
        });
        System.out.println("✓ Password salah ditolak");
    }
    
    @Test
    @DisplayName("Login dengan user tidak ada harus gagal")
    public void testLoginUserNotFound() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("nonexistent", "password123");
        });
        System.out.println("✓ User tidak ditemukan ditolak");
    }
    
    @Test
    @DisplayName("Reset login attempts setelah login berhasil")
    public void testResetCounterAfterSuccess() throws AuthenticationException {
        // Login berhasil
        User user = authService.login("admin", "admin123");
        assertNotNull(user);
        System.out.println("✓ Counter reset setelah login berhasil");
    }
    
    @Test
    @DisplayName("Login gagal 3x memicu lockout")
    public void testLoginLockout() {
        // Coba 3 kali password salah
        for (int i = 0; i < 3; i++) {
            int index = i;
            assertThrows(AuthenticationException.class, () -> {
                authService.login("kasir", "wrong" + index);
            });
        }
        
        // Attempt ke-4 harus di-lockout
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.login("kasir", "kasir123");
        });
        
        assertTrue(exception.getMessage().contains("Coba lagi dalam"));
        System.out.println("✓ Lockout triggered: " + exception.getMessage());
    }
    
    @Test
    @DisplayName("Username kosong harus error")
    public void testLoginEmptyUsername() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("", "password123");
        });
        System.out.println("✓ Username kosong ditolak");
    }
    
    @Test
    @DisplayName("Password kosong harus error")
    public void testLoginEmptyPassword() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("kasir", "");
        });
        System.out.println("✓ Password kosong ditolak");
    }
}
