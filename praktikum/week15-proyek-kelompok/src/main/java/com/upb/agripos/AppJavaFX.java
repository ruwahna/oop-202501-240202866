package com.upb.agripos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main entry point untuk aplikasi Agri-POS JavaFX
 */
public class AppJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Load main layout dari FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/upb/agripos/view/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);

            // Setup stage
            primaryStage.setTitle("Agri-POS - Sistem Kasir Pertanian");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(700);
            
            // Center stage pada layar
            primaryStage.centerOnScreen();

            // Setup icon jika ada
            try {
                Image icon = new Image(getClass().getResourceAsStream("/com/upb/agripos/assets/icon.png"));
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("Icon file not found, continuing without icon");
            }

            primaryStage.setOnCloseRequest(event -> {
                System.out.println("Aplikasi ditutup");
                System.exit(0);
            });

            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method untuk menjalankan aplikasi
     */
    public static void main(String[] args) {
        System.out.println("Starting Agri-POS Application...");
        launch(args);
    }
}