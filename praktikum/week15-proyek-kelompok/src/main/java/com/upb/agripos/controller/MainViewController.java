package com.upb.agripos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller untuk Main View
 */
public class MainViewController {
    @FXML private Label lblUser;
    @FXML private TabPane tabPane;
    @FXML private Tab tabTransaksi;
    @FXML private Tab tabStokProduk;
    @FXML private Tab tabLaporan;
    
    private String currentUsername;
    private String currentRole;
    
    @FXML
    public void initialize() {
        // Tampilkan user info jika tersedia
        if (currentUsername != null) {
            lblUser.setText("User: " + currentUsername + " (" + currentRole + ")");
            setupTabsBasedOnRole();
        }
    }
    
    /**
     * Set user info untuk ditampilkan
     */
    public void setUserInfo(String username, String role) {
        this.currentUsername = username;
        this.currentRole = role;
        if (lblUser != null) {
            lblUser.setText("User: " + username + " (" + role + ")");
        }
        // Update window title
        try {
            Stage stage = (Stage) lblUser.getScene().getWindow();
            stage.setTitle("Agri-POS - Sistem Kasir Pertanian | User: " + username + " (" + role + ")");
        } catch (Exception e) {
            System.err.println("Error setting window title: " + e.getMessage());
        }
        if (tabPane != null) {
            setupTabsBasedOnRole();
        }
    }
    
    /**
     * Setup tabs berdasarkan role user
     */
    private void setupTabsBasedOnRole() {
        if (currentRole == null) return;
        
        if ("Admin".equalsIgnoreCase(currentRole)) {
            // Admin: tampilkan tab Admin dan Laporan
            if (tabTransaksi != null) tabTransaksi.setDisable(true);
            if (tabStokProduk != null) tabStokProduk.setDisable(false);
            if (tabLaporan != null) tabLaporan.setDisable(false);
            
            // Pilih tab Admin sebagai default
            if (tabPane != null && tabStokProduk != null) {
                tabPane.getSelectionModel().select(tabStokProduk);
            }
            System.out.println("✓ Admin mode - Menampilkan tab Manajemen Stok & Laporan");
        } else if ("Kasir".equalsIgnoreCase(currentRole)) {
            // Kasir: hanya tampilkan tab Transaksi
            if (tabTransaksi != null) tabTransaksi.setDisable(false);
            if (tabStokProduk != null) tabStokProduk.setDisable(true);
            if (tabLaporan != null) tabLaporan.setDisable(true);
            
            // Pilih tab Transaksi sebagai default
            if (tabPane != null && tabTransaksi != null) {
                tabPane.getSelectionModel().select(tabTransaksi);
            }
            System.out.println("✓ Kasir mode - Hanya tab Transaksi yang aktif");
        }
    }
    
    /**
     * Handle logout button
     */
    @FXML
    private void handleLogout() {
        try {
            // Load login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/upb/agripos/view/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);
            
            // Get current stage dan set ke login view
            Stage stage = (Stage) lblUser.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Agri-POS - Login");
            stage.show();
            
            System.out.println("✓ Logged out - kembali ke login screen");
        } catch (IOException e) {
            System.err.println("Error loading login view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
