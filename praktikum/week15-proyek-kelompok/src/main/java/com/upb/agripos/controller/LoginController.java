package com.upb.agripos.controller;

import com.upb.agripos.service.AuthService;
import com.upb.agripos.model.User;
import com.upb.agripos.exception.AuthenticationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for user login functionality.
 */
public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    private final AuthService authService = AuthService.getInstance();

    /**
     * Handles user login with validation.
     */
    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Warning", "Username dan password harus diisi!", Alert.AlertType.WARNING);
            return;
        }

        try {
            User user = authService.login(username, password);
            System.out.println("Login Berhasil: " + user.getRole());
            navigateToMain(user);
        } catch (AuthenticationException e) {
            showAlert("Login Gagal", e.getMessage(), Alert.AlertType.ERROR);
            txtPassword.clear();
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Navigates to the main application view.
     */
    private void navigateToMain(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/upb/agripos/view/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);
            
            // Get controller dan pass user info
            MainViewController controller = loader.getController();
            controller.setUserInfo(user.getUsername(), user.getRole());
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("AgriPOS - Main");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Gagal membuka main view: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}