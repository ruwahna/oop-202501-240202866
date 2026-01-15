package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controller untuk Product Management (Admin)
 */
public class ProductManagementController {
    @FXML private TextField searchField;
    @FXML private Button btnCari;
    @FXML private Button btnReset;
    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, String> colKode;
    @FXML private TableColumn<Product, String> colNama;
    @FXML private TableColumn<Product, String> colKategori;
    @FXML private TableColumn<Product, Double> colHarga;
    @FXML private TableColumn<Product, Integer> colStok;
    @FXML private Button btnTambah;
    @FXML private Button btnEdit;
    @FXML private Button btnHapus;

    private ProductService productService;
    private ObservableList<Product> productList;

    @FXML
    public void initialize() {
        productService = new ProductService();
        setupTableColumns();
        setupEventHandlers();
        loadAllProducts();
    }

    /**
     * Setup table columns untuk menampilkan data
     */
    private void setupTableColumns() {
        colKode.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKode()));
        colNama.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama()));
        colKategori.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKategori()));
        
        // Format harga dengan Locale Indonesia (Rp 12.000,00)
        colHarga.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getHarga()).asObject());
        colHarga.setCellFactory(column -> new TableCell<Product, Double>() {
            private final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        
        colStok.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStok()).asObject());
    }

    /**
     * Setup event handlers untuk buttons
     */
    private void setupEventHandlers() {
        btnCari.setOnAction(e -> handleSearch());
        btnReset.setOnAction(e -> loadAllProducts());
        searchField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                handleSearch();
            }
        });
        btnTambah.setOnAction(e -> handleTambahProduk());
        btnEdit.setOnAction(e -> handleEditProduk());
        btnHapus.setOnAction(e -> handleHapusProduk());
    }

    /**
     * Load semua produk dari database
     */
    public void loadAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            productList = FXCollections.observableArrayList(products);
            tableProducts.setItems(productList);
            System.out.println("✓ Loaded " + products.size() + " products");
        } catch (Exception e) {
            showError("Error", "Gagal memuat produk: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle search/filter produk
     */
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadAllProducts();
            return;
        }

        try {
            List<Product> allProducts = productService.getAllProducts();
            ObservableList<Product> filtered = FXCollections.observableArrayList();
            
            for (Product p : allProducts) {
                if (p.getKode().toLowerCase().contains(keyword) ||
                    p.getNama().toLowerCase().contains(keyword) ||
                    p.getKategori().toLowerCase().contains(keyword)) {
                    filtered.add(p);
                }
            }
            
            tableProducts.setItems(filtered);
            System.out.println("✓ Found " + filtered.size() + " products matching '" + keyword + "'");
        } catch (Exception e) {
            showError("Error", "Gagal mencari produk: " + e.getMessage());
        }
    }

    /**
     * Handle tambah produk baru
     */
    private void handleTambahProduk() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Tambah Produk Baru");
        dialog.setHeaderText("Masukkan data produk baru");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tfKode = new TextField();
        tfKode.setPromptText("Contoh: P005");
        TextField tfNama = new TextField();
        tfNama.setPromptText("Contoh: Beras Premium");
        TextField tfKategori = new TextField();
        tfKategori.setPromptText("Contoh: Beras");
        TextField tfHarga = new TextField();
        tfHarga.setPromptText("Contoh: 12000 atau 12.000,00");
        TextField tfStok = new TextField();
        tfStok.setPromptText("Contoh: 100");

        grid.add(new Label("Kode Produk:"), 0, 0);
        grid.add(tfKode, 1, 0);
        grid.add(new Label("Nama Produk:"), 0, 1);
        grid.add(tfNama, 1, 1);
        grid.add(new Label("Kategori:"), 0, 2);
        grid.add(tfKategori, 1, 2);
        grid.add(new Label("Harga:"), 0, 3);
        grid.add(tfHarga, 1, 3);
        grid.add(new Label("Stok:"), 0, 4);
        grid.add(tfStok, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String kode = tfKode.getText().trim();
                    String nama = tfNama.getText().trim();
                    String kategori = tfKategori.getText().trim();
                    
                    // Parse harga dengan flexible format (12000, 12.000, 12,000, 12.000,00, etc)
                    String hargaStr = tfHarga.getText().trim().replaceAll("[^0-9,]", "");
                    hargaStr = hargaStr.replace(".", "").replace(",", ".");
                    double harga = Double.parseDouble(hargaStr);
                    
                    int stok = Integer.parseInt(tfStok.getText().trim());

                    if (kode.isEmpty() || nama.isEmpty()) {
                        showError("Validasi", "Kode dan Nama produk tidak boleh kosong!");
                        return null;
                    }

                    return new Product(kode, nama, kategori, harga, stok);
                } catch (NumberFormatException e) {
                    showError("Validasi", "Harga dan Stok harus berupa angka! (Contoh: 12000 atau 12.000)");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        if (result.isPresent()) {
            Product newProduct = result.get();
            try {
                productService.addProduct(newProduct);
                loadAllProducts();
                showInfo("Sukses", "Produk '" + newProduct.getNama() + "' berhasil ditambahkan!");
                System.out.println("✓ Product added: " + newProduct.getKode());
            } catch (Exception e) {
                showError("Error", "Gagal menambah produk: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle edit produk
     */
    private void handleEditProduk() {
        Product selected = tableProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Perhatian", "Pilih produk yang ingin diedit!");
            return;
        }

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Produk");
        dialog.setHeaderText("Edit data produk: " + selected.getNama());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tfNama = new TextField(selected.getNama());
        TextField tfKategori = new TextField(selected.getKategori());
        TextField tfHarga = new TextField(String.valueOf(selected.getHarga()));
        TextField tfStok = new TextField(String.valueOf(selected.getStok()));

        grid.add(new Label("Nama Produk:"), 0, 0);
        grid.add(tfNama, 1, 0);
        grid.add(new Label("Kategori:"), 0, 1);
        grid.add(tfKategori, 1, 1);
        grid.add(new Label("Harga:"), 0, 2);
        grid.add(tfHarga, 1, 2);
        grid.add(new Label("Stok:"), 0, 3);
        grid.add(tfStok, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    selected.setNama(tfNama.getText().trim());
                    selected.setKategori(tfKategori.getText().trim());
                    
                    // Parse harga dengan flexible format
                    String hargaStr = tfHarga.getText().trim().replaceAll("[^0-9,]", "");
                    hargaStr = hargaStr.replace(".", "").replace(",", ".");
                    double harga = Double.parseDouble(hargaStr);
                    
                    selected.setHarga(harga);
                    selected.setStok(Integer.parseInt(tfStok.getText().trim()));
                    return selected;
                } catch (NumberFormatException e) {
                    showError("Validasi", "Harga dan Stok harus berupa angka! (Contoh: 12000 atau 12.000)");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        if (result.isPresent()) {
            Product updated = result.get();
            try {
                productService.updateProduct(updated);
                loadAllProducts();
                showInfo("Sukses", "Produk berhasil diperbarui!");
                System.out.println("✓ Product updated: " + updated.getKode());
            } catch (Exception e) {
                showError("Error", "Gagal mengupdate produk: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle hapus produk
     */
    private void handleHapusProduk() {
        Product selected = tableProducts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Perhatian", "Pilih produk yang ingin dihapus!");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Konfirmasi Hapus");
        confirmDialog.setHeaderText("Yakin ingin menghapus produk?");
        confirmDialog.setContentText("Produk: " + selected.getNama() + " (" + selected.getKode() + ")");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                productService.deleteProduct(selected.getKode());
                loadAllProducts();
                showInfo("Sukses", "Produk berhasil dihapus!");
                System.out.println("✓ Product deleted: " + selected.getKode());
            } catch (Exception e) {
                showError("Error", "Gagal menghapus produk: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Utility method untuk show info dialog
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Utility method untuk show error dialog
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
