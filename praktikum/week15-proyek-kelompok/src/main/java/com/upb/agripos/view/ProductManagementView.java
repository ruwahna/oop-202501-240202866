package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * View untuk Manajemen Produk (FR-1) - Admin only
 * UI modern dengan section yang jelas
 */
public class ProductManagementView {
    private final PosController controller;
    
    private TableView<Product> productTable;
    private TextField codeField, nameField, categoryField, priceField, stockField, searchField;
    private ComboBox<String> categoryFilterCombo;
    private Button saveButton, clearButton, deleteButton;
    private Label formTitleLabel;
    private boolean isEditMode = false;

    public ProductManagementView(PosController controller) {
        this.controller = controller;
    }

    public VBox createContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #f5f5f5;");

        // Header Section - lebih kompak
        HBox headerSection = createHeaderSection();

        // Main content: Form + Table
        HBox mainContent = new HBox(15);
        VBox formPanel = createForm();
        VBox tablePanel = createTable();
        mainContent.getChildren().addAll(formPanel, tablePanel);
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        content.getChildren().addAll(headerSection, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        return content;
    }

    private HBox createHeaderSection() {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 15, 10, 15));
        header.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");

        Label icon = new Label("📦");
        icon.setStyle("-fx-font-size: 24px;");
        
        Label title = new Label("Manajemen Produk");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1976D2;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Stats kompak
        int totalProducts = controller.getProductList().size();
        int lowStock = (int) controller.getProductList().stream().filter(p -> p.getStock() < 10).count();
        
        Label statsLabel = new Label("📊 " + totalProducts + " produk | " + 
            (lowStock > 0 ? "⚠️ " + lowStock + " stok rendah" : "✅ Stok aman"));
        statsLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

        header.getChildren().addAll(icon, title, spacer, statsLabel);
        return header;
    }

    private VBox createForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setPrefWidth(280);
        form.setMinWidth(280);
        form.setMaxWidth(280);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        // Form Header
        HBox formHeader = new HBox(8);
        formHeader.setAlignment(Pos.CENTER_LEFT);
        Label formIcon = new Label("✏️");
        formIcon.setStyle("-fx-font-size: 16px;");
        formTitleLabel = new Label("Tambah Produk");
        formTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");
        formHeader.getChildren().addAll(formIcon, formTitleLabel);

        Separator sep = new Separator();

        // Fields - lebih kompak
        codeField = createCompactTextField("Kode (PRD001)");
        nameField = createCompactTextField("Nama produk");
        categoryField = createCompactTextField("Kategori");
        priceField = createCompactTextField("Harga (Rp)");
        stockField = createCompactTextField("Stok");

        // Grid untuk fields agar lebih kompak
        GridPane fieldsGrid = new GridPane();
        fieldsGrid.setHgap(8);
        fieldsGrid.setVgap(8);
        
        fieldsGrid.add(new Label("Kode:"), 0, 0);
        fieldsGrid.add(codeField, 1, 0);
        fieldsGrid.add(new Label("Nama:"), 0, 1);
        fieldsGrid.add(nameField, 1, 1);
        fieldsGrid.add(new Label("Kategori:"), 0, 2);
        fieldsGrid.add(categoryField, 1, 2);
        fieldsGrid.add(new Label("Harga:"), 0, 3);
        fieldsGrid.add(priceField, 1, 3);
        fieldsGrid.add(new Label("Stok:"), 0, 4);
        fieldsGrid.add(stockField, 1, 4);
        
        // Style labels
        for (var node : fieldsGrid.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");
            }
        }
        
        GridPane.setHgrow(codeField, Priority.ALWAYS);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(categoryField, Priority.ALWAYS);
        GridPane.setHgrow(priceField, Priority.ALWAYS);
        GridPane.setHgrow(stockField, Priority.ALWAYS);

        // Buttons - kompak
        VBox buttonBox = new VBox(8);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        saveButton = new Button("💾 Simpan");
        saveButton.setPrefWidth(Double.MAX_VALUE);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                           "-fx-font-size: 12px; -fx-padding: 8; -fx-background-radius: 5;");
        saveButton.setOnAction(e -> handleSave());

        HBox actionButtons = new HBox(8);
        clearButton = new Button("🗑️ Bersih");
        clearButton.setPrefWidth(100);
        clearButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-padding: 6; -fx-background-radius: 5; -fx-font-size: 11px;");
        clearButton.setOnAction(e -> handleClear());

        deleteButton = new Button("❌ Hapus");
        deleteButton.setPrefWidth(100);
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 6; -fx-background-radius: 5; -fx-font-size: 11px;");
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e -> handleDelete());

        actionButtons.getChildren().addAll(clearButton, deleteButton);
        HBox.setHgrow(clearButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);

        buttonBox.getChildren().addAll(saveButton, actionButtons);

        form.getChildren().addAll(formHeader, sep, fieldsGrid, buttonBox);
        return form;
    }

    private TextField createCompactTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-padding: 6; -fx-background-radius: 5; -fx-border-color: #ddd; " +
                      "-fx-border-radius: 5; -fx-font-size: 11px;");
        field.setPrefWidth(150);
        return field;
    }

    private VBox createTable() {
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(15));
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");

        // Table Header with Search - kompak
        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label tableTitle = new Label("📋 Daftar Produk");
        tableTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Search
        searchField = new TextField();
        searchField.setPromptText("🔍 Cari...");
        searchField.setPrefWidth(150);
        searchField.setStyle("-fx-padding: 6; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #ddd; -fx-font-size: 11px;");

        // Category filter
        categoryFilterCombo = new ComboBox<>();
        categoryFilterCombo.getItems().add("Semua");
        controller.getProductList().stream()
            .map(Product::getCategory)
            .distinct()
            .forEach(cat -> categoryFilterCombo.getItems().add(cat));
        categoryFilterCombo.getSelectionModel().selectFirst();
        categoryFilterCombo.setStyle("-fx-background-radius: 15; -fx-font-size: 11px;");
        categoryFilterCombo.setPrefWidth(100);

        tableHeader.getChildren().addAll(tableTitle, spacer, searchField, categoryFilterCombo);

        // Product Table
        productTable = new TableView<>();
        productTable.setItems(controller.getProductList());
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setPrefWidth(70);

        TableColumn<Product, String> nameCol = new TableColumn<>("Nama");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> categoryCol = new TableColumn<>("Kategori");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(80);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(controller.formatCurrency(value));
                    setStyle("-fx-text-fill: #2E7D32;");
                }
            }
        });
        priceCol.setPrefWidth(100);

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(60);
        stockCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(value.toString());
                    if (value < 5) {
                        setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
                    } else if (value < 10) {
                        setStyle("-fx-text-fill: #ff9800; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #4CAF50;");
                    }
                }
            }
        });

        productTable.getColumns().addAll(codeCol, nameCol, categoryCol, priceCol, stockCol);

        // Selection listener for edit
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                populateForm(selected);
                isEditMode = true;
                formTitleLabel.setText("✏️ Edit Produk");
                saveButton.setText("💾 Update");
                deleteButton.setDisable(false);
                codeField.setDisable(true);
            }
        });

        // Filter logic
        searchField.textProperty().addListener((obs, old, newVal) -> filterTable());
        categoryFilterCombo.setOnAction(e -> filterTable());

        // Bottom actions
        HBox bottomActions = new HBox(10);
        bottomActions.setAlignment(Pos.CENTER_LEFT);
        
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 11px;");
        refreshButton.setOnAction(e -> {
            controller.loadProducts();
            filterTable();
        });

        Label countLabel = new Label(controller.getProductList().size() + " produk");
        countLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        bottomActions.getChildren().addAll(countLabel, bottomSpacer, refreshButton);

        VBox.setVgrow(productTable, Priority.ALWAYS);
        tableBox.getChildren().addAll(tableHeader, productTable, bottomActions);
        return tableBox;
    }

    private void filterTable() {
        String search = searchField.getText();
        String category = categoryFilterCombo.getValue();
        productTable.setItems(controller.getProductList().filtered(p -> {
            boolean matchSearch = search == null || search.isEmpty() ||
                p.getCode().toLowerCase().contains(search.toLowerCase()) ||
                p.getName().toLowerCase().contains(search.toLowerCase());
            boolean matchCategory = category == null || category.equals("Semua") ||
                p.getCategory().equals(category);
            return matchSearch && matchCategory;
        }));
    }

    private void populateForm(Product product) {
        codeField.setText(product.getCode());
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
    }

    private void handleSave() {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            if (code.isEmpty() || name.isEmpty()) {
                showError("Kode dan Nama produk wajib diisi!");
                return;
            }

            if (category.isEmpty()) category = "Umum";

            if (isEditMode) {
                controller.updateProduct(code, name, category, price, stock);
                showSuccess("✅ Produk berhasil diupdate!");
            } else {
                controller.addProduct(code, name, category, price, stock);
                showSuccess("✅ Produk berhasil ditambahkan!");
            }

            handleClear();
            filterTable();
        } catch (NumberFormatException e) {
            showError("Harga dan Stok harus berupa angka!");
        } catch (Exception e) {
            showError("Gagal menyimpan: " + e.getMessage());
        }
    }

    private void handleClear() {
        codeField.clear();
        nameField.clear();
        categoryField.clear();
        priceField.clear();
        stockField.clear();
        codeField.setDisable(false);
        isEditMode = false;
        formTitleLabel.setText("Tambah Produk");
        saveButton.setText("💾 Simpan");
        deleteButton.setDisable(true);
        productTable.getSelectionModel().clearSelection();
    }

    private void handleDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText("Hapus Produk?");
        confirm.setContentText("Anda akan menghapus produk:\n\n" + 
                              "🏷️ " + selected.getCode() + "\n" +
                              "📝 " + selected.getName() + "\n\n" +
                              "Aksi ini tidak dapat dibatalkan.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deleteProduct(selected.getCode());
                    handleClear();
                    showSuccess("✅ Produk berhasil dihapus!");
                } catch (Exception e) {
                    showError("Gagal menghapus: " + e.getMessage());
                }
            }
        });
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
