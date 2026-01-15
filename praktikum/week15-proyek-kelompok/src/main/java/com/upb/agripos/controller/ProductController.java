package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

/**
 * Controller for managing products in the inventory.
 */
public class ProductController {
    @FXML private TableView<Product> tableProduct;
    @FXML private TextField txtKode, txtNama, txtHarga, txtStok, txtKategori;
    
    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        loadData();
    }

    /**
     * Loads all products from service and updates the table view.
     */
    private void loadData() {
        if (tableProduct != null) {
            tableProduct.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        }
    }

    /**
     * Handles saving a new product with validation.
     */
    @FXML
    private void handleSave() {
        try {
            if (!validateInput()) {
                return;
            }

            Product p = new Product();
            p.setKode(txtKode.getText());
            p.setNama(txtNama.getText());
            p.setHarga(Double.parseDouble(txtHarga.getText()));
            p.setStok(Integer.parseInt(txtStok.getText()));
            p.setKategori(txtKategori.getText());

            productService.addProduct(p);
            loadData();
            clearForm();
            showAlert("Success", "Product berhasil disimpan!", Alert.AlertType.INFORMATION);
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            showAlert("Error", "Harga dan Stok harus berupa angka!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles deleting a selected product.
     */
    @FXML
    private void handleDelete() {
        Product selected = tableProduct.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi");
            confirmation.setHeaderText("Hapus Produk");
            confirmation.setContentText("Yakin ingin menghapus produk ini?");
            
            if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                productService.deleteProduct(selected.getKode());
                loadData();
                showAlert("Success", "Produk berhasil dihapus!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Warning", "Pilih produk yang ingin dihapus!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Validates user input before saving.
     */
    private boolean validateInput() {
        if (txtKode.getText().isEmpty() || txtNama.getText().isEmpty() || 
            txtHarga.getText().isEmpty() || txtStok.getText().isEmpty() || 
            txtKategori.getText().isEmpty()) {
            showAlert("Warning", "Semua field harus diisi!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    /**
     * Clears all input fields.
     */
    private void clearForm() {
        txtKode.clear();
        txtNama.clear();
        txtHarga.clear();
        txtStok.clear();
        txtKategori.clear();
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