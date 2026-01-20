package com.upb.agripos;

import com.upb.agripos.controller.LoginController;
import com.upb.agripos.controller.PosController;
import com.upb.agripos.dao.*;
import com.upb.agripos.service.*;
import com.upb.agripos.service.payment.*;
import com.upb.agripos.util.DatabaseConnection;
import com.upb.agripos.util.DatabaseMigration;
import com.upb.agripos.view.LoginView;
import com.upb.agripos.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Main entry point untuk Agri-POS JavaFX Application
 * Menerapkan Dependency Injection Pattern
 */
public class AppJavaFx extends Application {

    private PosController posController;
    private LoginController loginController;
    private AuthService authService;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Agri-POS - Sistem Point of Sale Pertanian");

        try {
            // Jalankan database migration terlebih dahulu
            DatabaseMigration.runMigrations();
            
            initializeApplication();
            showLoginScreen();
        } catch (Exception e) {
            showErrorDialog("Error Inisialisasi", 
                "Gagal menginisialisasi aplikasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inisialisasi semua dependency menggunakan DI Pattern
     */
    private void initializeApplication() throws Exception {
        // 1. DAO Layer (menggunakan DatabaseConnection singleton internally)
        ProductDAO productDAO = new JdbcProductDAO();
        UserDAO userDAO = new JdbcUserDAO();
        TransactionDAO transactionDAO = new JdbcTransactionDAO();

        // 2. Register Payment Methods (Strategy Pattern)
        // Sudah di-register di static block PaymentMethodFactory, tidak perlu register lagi

        // 3. Service Layer (DI into Services)
        ProductService productService = new ProductService(productDAO);
        authService = new AuthService(userDAO);
        CartService cartService = new CartService(productService);
        TransactionService transactionService = new TransactionService(transactionDAO, productService, cartService);
        ReceiptService receiptService = new ReceiptService();
        ReportService reportService = new ReportService(transactionService);

        // 4. Controller Layer (DI into Controllers)
        posController = new PosController(
            productService,
            cartService,
            transactionService,
            receiptService,
            reportService,
            authService
        );

        loginController = new LoginController(authService);

        System.out.println("✅ Agri-POS berhasil diinisialisasi!");
    }

    /**
     * Tampilkan Login Screen
     */
    private void showLoginScreen() {
        LoginView loginView = new LoginView(loginController, this::onLoginSuccess);
        Scene loginScene = loginView.createScene(primaryStage);
        
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Callback ketika login berhasil
     */
    private void onLoginSuccess() {
        try {
            // Load products first
            posController.loadProducts();
            
            MainView mainView = new MainView(posController, loginController, this::onLogout, primaryStage);
            Scene mainScene = mainView.createScene();
            
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Agri-POS - " + authService.getCurrentUser().getUsername() + 
                                  " (" + authService.getCurrentUser().getRole() + ")");
        } catch (Exception e) {
            showErrorDialog("Error", "Gagal membuka halaman utama: " + e.getMessage());
        }
    }

    /**
     * Callback ketika logout
     */
    private void onLogout() {
        authService.logout();
        posController.clearCart();
        showLoginScreen();
        primaryStage.setTitle("Agri-POS - Sistem Point of Sale Pertanian");
    }

    /**
     * Tampilkan error dialog
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        // Cleanup resources
        System.out.println("👋 Agri-POS ditutup. Sampai jumpa!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
