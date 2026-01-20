package com.upb.agripos.view;

import com.upb.agripos.controller.LoginController;
import com.upb.agripos.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;

/**
 * View untuk Login (FR-5) - Responsive untuk Mobile dan Desktop
 */
public class LoginView {
    private final LoginController loginController;
    private final Runnable onLoginSuccess;
    
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;
    private ToggleButton adminButton;
    private ToggleButton kasirButton;
    
    // UI Components for responsive design
    private Label titleLabel;
    private Label subtitleLabel;
    private Label loginLabel;
    private Label roleLabel;
    private Label usernameLabel;
    private Label passwordLabel;
    private Button loginButton;
    private VBox formBox;
    private VBox root;
    private HBox roleButtons;

    public LoginView(LoginController loginController, Runnable onLoginSuccess) {
        this.loginController = loginController;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene createScene(Stage stage) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #2E7D32);");

        // Logo/Title
        titleLabel = new Label("🌾 AGRI-POS");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        subtitleLabel = new Label("Point of Sale System");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E8F5E9;");

        // Login Form
        formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(350);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Role Selection Buttons
        roleLabel = new Label("Pilih Role");
        roleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        ToggleGroup roleGroup = new ToggleGroup();
        
        adminButton = new ToggleButton("👨‍💼 Admin");
        adminButton.setToggleGroup(roleGroup);
        adminButton.setPrefWidth(130);
        adminButton.setPrefHeight(40);
        adminButton.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; " +
                            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 5;");
        
        kasirButton = new ToggleButton("🛒 Kasir");
        kasirButton.setToggleGroup(roleGroup);
        kasirButton.setPrefWidth(130);
        kasirButton.setPrefHeight(40);
        kasirButton.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; " +
                            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 5;");
        
        // Style when selected
        adminButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                adminButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                    "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 5;");
            } else {
                adminButton.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; " +
                                    "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 5;");
            }
        });
        
        kasirButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                kasirButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                                    "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 5;");
            } else {
                kasirButton.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; " +
                                    "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 5;");
            }
        });
        
        roleButtons = new HBox(10);
        roleButtons.setAlignment(Pos.CENTER);
        roleButtons.getChildren().addAll(adminButton, kasirButton);

        // Username
        usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        usernameField.setPrefHeight(40);

        // Password
        passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        passwordField.setPrefHeight(40);
        passwordField.setOnAction(e -> handleLogin());

        // Message
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 12px;");
        messageLabel.setWrapText(true);

        // Login Button
        loginButton = new Button("Login");
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

        formBox.getChildren().addAll(
            loginLabel,
            roleLabel, roleButtons,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            messageLabel,
            loginButton
        );

        root.getChildren().addAll(titleLabel, subtitleLabel, formBox);

        // Create scene dengan ukuran default desktop
        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Agri-POS - Login");
        stage.setMinWidth(320);
        stage.setMinHeight(500);
        
        // Responsive listener untuk ukuran window
        ChangeListener<Number> responsiveListener = (obs, oldVal, newVal) -> {
            applyResponsiveStyles(scene.getWidth(), scene.getHeight());
        };
        
        scene.widthProperty().addListener(responsiveListener);
        scene.heightProperty().addListener(responsiveListener);
        
        // Apply initial responsive styles
        applyResponsiveStyles(500, 600);
        
        return scene;
    }
    
    /**
     * Menerapkan style responsive berdasarkan ukuran layar
     */
    private void applyResponsiveStyles(double width, double height) {
        boolean isMobile = width < 400;
        boolean isSmall = width < 500;
        
        if (isMobile) {
            // Mobile Layout (< 400px)
            root.setSpacing(10);
            root.setPadding(new Insets(20, 15, 20, 15));
            
            titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
            subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #E8F5E9;");
            
            formBox.setPadding(new Insets(20, 15, 20, 15));
            formBox.setMaxWidth(Double.MAX_VALUE);
            formBox.setSpacing(10);
            
            loginLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
            roleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            usernameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            passwordLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            
            adminButton.setPrefWidth(100);
            adminButton.setPrefHeight(35);
            kasirButton.setPrefWidth(100);
            kasirButton.setPrefHeight(35);
            
            usernameField.setPrefHeight(35);
            usernameField.setStyle("-fx-font-size: 12px; -fx-padding: 8;");
            passwordField.setPrefHeight(35);
            passwordField.setStyle("-fx-font-size: 12px; -fx-padding: 8;");
            
            loginButton.setPrefWidth(150);
            loginButton.setPrefHeight(40);
            loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                               "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;");
            
            roleButtons.setSpacing(5);
            
        } else if (isSmall) {
            // Small/Tablet Layout (400-500px)
            root.setSpacing(15);
            root.setPadding(new Insets(30));
            
            titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
            subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #E8F5E9;");
            
            formBox.setPadding(new Insets(25));
            formBox.setMaxWidth(320);
            formBox.setSpacing(12);
            
            loginLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");
            roleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
            usernameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
            passwordLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
            
            adminButton.setPrefWidth(115);
            adminButton.setPrefHeight(38);
            kasirButton.setPrefWidth(115);
            kasirButton.setPrefHeight(38);
            
            usernameField.setPrefHeight(38);
            usernameField.setStyle("-fx-font-size: 13px; -fx-padding: 9;");
            passwordField.setPrefHeight(38);
            passwordField.setStyle("-fx-font-size: 13px; -fx-padding: 9;");
            
            loginButton.setPrefWidth(180);
            loginButton.setPrefHeight(42);
            loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                               "-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 5;");
            
            roleButtons.setSpacing(8);
            
        } else {
            // Desktop Layout (>= 500px)
            root.setSpacing(20);
            root.setPadding(new Insets(40));
            
            titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
            subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E8F5E9;");
            
            formBox.setPadding(new Insets(30));
            formBox.setMaxWidth(350);
            formBox.setSpacing(15);
            
            loginLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
            roleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            passwordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            
            adminButton.setPrefWidth(130);
            adminButton.setPrefHeight(40);
            kasirButton.setPrefWidth(130);
            kasirButton.setPrefHeight(40);
            
            usernameField.setPrefHeight(40);
            usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
            passwordField.setPrefHeight(40);
            passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
            
            loginButton.setPrefWidth(200);
            loginButton.setPrefHeight(45);
            loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                               "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");
            
            roleButtons.setSpacing(10);
        }
    }

    private void handleLogin() {
        // Validasi role harus dipilih
        if (!adminButton.isSelected() && !kasirButton.isSelected()) {
            showError("Silakan pilih role (Admin atau Kasir) terlebih dahulu");
            return;
        }

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
