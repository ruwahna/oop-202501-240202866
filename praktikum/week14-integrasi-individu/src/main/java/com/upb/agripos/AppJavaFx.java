package com.upb.agripos;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.dao.JdbcProductDAO;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.PosView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Application JavaFX untuk Agri-POS
 * Mengintegrasikan semua layer: View, Controller, Service, DAO
 * Menerapkan Dependency Injection (Bab 6 - SOLID)
 */
public class AppJavaFx extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Bab 1: Identitas Praktikum
        System.out.println("Hello World, I am [Indah Ruwahna Anugraheni]-[240202866]");
        System.out.println("=== Agri-POS System Started ===");

        // Dependency Injection - mengikuti DIP (Dependency Inversion Principle)
        ProductDAO productDAO = new JdbcProductDAO();
        ProductService productService = new ProductService(productDAO);
        CartService cartService = new CartService();
        
        PosController controller = new PosController(productService, cartService);
        controller.loadProducts(); // Load data awal

        PosView view = new PosView(controller);
        Scene scene = view.createScene(primaryStage);

        primaryStage.setTitle("Agri-POS - Point of Sale System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}