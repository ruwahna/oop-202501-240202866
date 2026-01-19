package com.upb.agripos.view;

import com.upb.agripos.controller.LoginController;
import com.upb.agripos.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * View untuk Login (FR-5)
 */
public class LoginView {
    private final LoginController loginController;
    private final Runnable onLoginSuccess;
    
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginView(LoginController loginController, Runnable onLoginSuccess) {
        this.loginController = loginController;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene createScene(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #2E7D32);");

        // Logo/Title
        Label titleLabel = new Label("🌾 AGRI-POS");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("Point of Sale System");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E8F5E9;");

        // Login Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(350);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        Label loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Username
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        usernameField.setPrefHeight(40);

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        passwordField.setPrefHeight(40);
        passwordField.setOnAction(e -> handleLogin());

        // Message
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 12px;");

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(45);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                           "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");
        loginButton.setOnAction(e -> handleLogin());
        loginButton.setOnMouseEntered(e -> 
            loginButton.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; " +
                               "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));
        loginButton.setOnMouseExited(e -> 
            loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                               "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        // Demo credentials info
        Label infoLabel = new Label("Demo: admin/admin123 atau kasir/kasir123");
        infoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

        formBox.getChildren().addAll(
            loginLabel,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            messageLabel,
            loginButton,
            infoLabel
        );

        root.getChildren().addAll(titleLabel, subtitleLabel, formBox);

        Scene scene = new Scene(root, 500, 550);
        stage.setTitle("Agri-POS - Login");
        return scene;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password harus diisi");
            return;
        }

        try {
            User user = loginController.login(username, password);
            messageLabel.setStyle("-fx-text-fill: #4CAF50;");
            messageLabel.setText("Login berhasil! Selamat datang, " + user.getUsername());
            
            // Delay sebentar lalu pindah ke main view
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.millis(500));
            pause.setOnFinished(e -> onLoginSuccess.run());
            pause.play();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: #f44336;");
        messageLabel.setText(message);
    }
}
