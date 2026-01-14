package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ProductController {
    private ProductService productService = new ProductService();

    public ObservableList<Product> loadData() {
        return FXCollections.observableArrayList(productService.findAll());
    }

    // Methods expected by ProductTableView
    public void addProduct(Product p) {
        productService.insert(p);
    }

    public void deleteProduct(String code) {
        productService.delete(code);
    }

    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    // Backwards-compatible alias
    public void delete(String code) {
        deleteProduct(code);
    }
}