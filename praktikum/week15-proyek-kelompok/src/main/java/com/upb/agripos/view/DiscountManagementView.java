package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.service.DiscountConfigService;
import com.upb.agripos.service.DiscountConfigService.DiscountConfig;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * View untuk Manajemen Diskon oleh Admin
 * Admin dapat menambah, mengedit, dan menghapus diskon yang tersedia untuk Kasir
 * Data disimpan di DiscountConfigService yang shared dengan TransactionView (Kasir)
 */
public class DiscountManagementView {
    private final PosController controller;
    private final DiscountConfigService discountService;
    
    private TableView<DiscountConfig> discountTable;
    
    // Form fields
    private TextField nameField;
    private TextField codeField;
    private ComboBox<String> typeCombo;
    private TextField valueField;
    private TextField minPurchaseField;
    private TextField minItemsField;
    private CheckBox activeCheckBox;
    private Label statsLabel;
    private Label activeLabel;
    
    public DiscountManagementView(PosController controller) {
        this.controller = controller;
        this.discountService = DiscountConfigService.getInstance();
    }
    
    public ScrollPane createContent() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #fff3e0, #f5f5f5);");
        
        // Header
        mainContainer.getChildren().add(createHeader());
        
        // Sync info banner
        HBox syncBanner = new HBox(10);
        syncBanner.setAlignment(Pos.CENTER);
        syncBanner.setPadding(new Insets(10, 20, 10, 20));
        syncBanner.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 10;");
        Label syncIcon = new Label("🔄");
        syncIcon.setStyle("-fx-font-size: 18px;");
        Label syncLabel = new Label("Diskon yang ditambahkan/diubah di sini akan langsung tersedia untuk Kasir");
        syncLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #1565C0; -fx-font-weight: bold;");
        syncBanner.getChildren().addAll(syncIcon, syncLabel);
        mainContainer.getChildren().add(syncBanner);
        
        // Content - Split into form and table
        HBox content = new HBox(20);
        content.getChildren().addAll(createFormPanel(), createTablePanel());
        HBox.setHgrow(content.getChildren().get(1), Priority.ALWAYS);
        
        mainContainer.getChildren().add(content);
        
        // Wrap in ScrollPane
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        return scrollPane;
    }
    
    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Label icon = new Label("🎁");
        icon.setStyle("-fx-font-size: 36px;");
        
        VBox titleBox = new VBox(3);
        Label title = new Label("Manajemen Diskon & Promo");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E65100;");
        Label subtitle = new Label("Kelola diskon dan voucher yang tersedia untuk kasir (Sinkron Real-time)");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
        titleBox.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Stats
        VBox statsBox = new VBox(3);
        statsBox.setAlignment(Pos.CENTER_RIGHT);
        ObservableList<DiscountConfig> discounts = discountService.getDiscountConfigs();
        statsLabel = new Label("Total Diskon: " + discounts.size());
        statsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #FF9800;");
        long activeCount = discounts.stream().filter(DiscountConfig::isActive).count();
        activeLabel = new Label("Aktif: " + activeCount);
        activeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4CAF50;");
        statsBox.getChildren().addAll(statsLabel, activeLabel);
        
        header.getChildren().addAll(icon, titleBox, spacer, statsBox);
        return header;
    }
    
    private void updateStats() {
        ObservableList<DiscountConfig> discounts = discountService.getDiscountConfigs();
        statsLabel.setText("Total Diskon: " + discounts.size());
        long activeCount = discounts.stream().filter(DiscountConfig::isActive).count();
        activeLabel.setText("Aktif: " + activeCount);
    }
    
    private VBox createFormPanel() {
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(20));
        formPanel.setPrefWidth(350);
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        
        // Form Header
        Label formTitle = new Label("➕ Tambah/Edit Diskon");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        // Nama Diskon
        VBox nameBox = createFormField("Nama Diskon", "Contoh: Diskon Hari Raya");
        nameField = (TextField) nameBox.getChildren().get(1);
        
        // Kode Voucher
        VBox codeBox = createFormField("Kode Voucher", "Contoh: RAYA25");
        codeField = (TextField) codeBox.getChildren().get(1);
        
        // Tipe Diskon
        VBox typeBox = new VBox(5);
        Label typeLabel = new Label("Tipe Diskon");
        typeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Persentase", "Nominal");
        typeCombo.setValue("Persentase");
        typeCombo.setPrefWidth(Double.MAX_VALUE);
        typeCombo.setStyle("-fx-font-size: 13px;");
        typeBox.getChildren().addAll(typeLabel, typeCombo);
        
        // Nilai Diskon
        VBox valueBox = createFormField("Nilai Diskon", "Persentase (%) atau Nominal (Rp)");
        valueField = (TextField) valueBox.getChildren().get(1);
        
        // Minimal Pembelian
        VBox minPurchaseBox = createFormField("Minimal Pembelian (Rp)", "0 = tidak ada minimal");
        minPurchaseField = (TextField) minPurchaseBox.getChildren().get(1);
        minPurchaseField.setText("0");
        
        // Minimal Item
        VBox minItemsBox = createFormField("Minimal Jumlah Item", "0 = tidak ada minimal");
        minItemsField = (TextField) minItemsBox.getChildren().get(1);
        minItemsField.setText("0");
        
        // Status Aktif
        activeCheckBox = new CheckBox("Diskon Aktif");
        activeCheckBox.setSelected(true);
        activeCheckBox.setStyle("-fx-font-size: 13px;");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button addButton = new Button("💾 Simpan Diskon");
        addButton.setPrefWidth(150);
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                          "-fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        addButton.setOnAction(e -> handleSaveDiscount());
        
        Button clearButton = new Button("🔄 Reset Form");
        clearButton.setPrefWidth(120);
        clearButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        clearButton.setOnAction(e -> clearForm());
        
        buttonBox.getChildren().addAll(addButton, clearButton);
        
        formPanel.getChildren().addAll(
            formTitle,
            new Separator(),
            nameBox, codeBox, typeBox, valueBox,
            minPurchaseBox, minItemsBox,
            activeCheckBox,
            new Separator(),
            buttonBox
        );
        
        return formPanel;
    }
    
    private VBox createFormField(String labelText, String placeholder) {
        VBox box = new VBox(5);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ddd;");
        box.getChildren().addAll(label, field);
        return box;
    }
    
    private VBox createTablePanel() {
        VBox tablePanel = new VBox(15);
        tablePanel.setPadding(new Insets(20));
        tablePanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        
        // Table Header
        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label tableTitle = new Label("📋 Daftar Diskon (Tersedia untuk Kasir)");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari diskon...");
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-padding: 8; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #ddd;");
        
        tableHeader.getChildren().addAll(tableTitle, spacer, searchField);
        
        // Table
        discountTable = new TableView<>();
        discountTable.setItems(discountService.getDiscountConfigs());
        discountTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        discountTable.setStyle("-fx-font-size: 12px;");
        VBox.setVgrow(discountTable, Priority.ALWAYS);
        
        // Columns
        TableColumn<DiscountConfig, String> nameCol = new TableColumn<>("Nama Diskon");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);
        
        TableColumn<DiscountConfig, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setPrefWidth(100);
        codeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(value);
                    badge.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 3 8; " +
                                  "-fx-background-radius: 5; -fx-font-family: monospace; -fx-font-weight: bold;");
                    setGraphic(badge);
                }
            }
        });
        
        TableColumn<DiscountConfig, String> typeCol = new TableColumn<>("Tipe");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(90);
        
        TableColumn<DiscountConfig, Double> valueCol = new TableColumn<>("Nilai");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(80);
        valueCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    DiscountConfig item = getTableView().getItems().get(getIndex());
                    if (item.getType().equals("Persentase")) {
                        setText(value.intValue() + "%");
                    } else {
                        setText("Rp " + String.format("%,.0f", value));
                    }
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #E65100;");
                }
            }
        });
        
        TableColumn<DiscountConfig, Double> minPurchaseCol = new TableColumn<>("Min. Beli");
        minPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("minPurchase"));
        minPurchaseCol.setPrefWidth(100);
        minPurchaseCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null || value == 0) {
                    setText("-");
                } else {
                    setText("Rp " + String.format("%,.0f", value));
                }
            }
        });
        
        TableColumn<DiscountConfig, Integer> minItemsCol = new TableColumn<>("Min. Item");
        minItemsCol.setCellValueFactory(new PropertyValueFactory<>("minItems"));
        minItemsCol.setPrefWidth(80);
        minItemsCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null || value == 0) {
                    setText("-");
                } else {
                    setText(value + " item");
                }
            }
        });
        
        TableColumn<DiscountConfig, Boolean> activeCol = new TableColumn<>("Status");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeCol.setPrefWidth(80);
        activeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(value ? "✅ Aktif" : "❌ Nonaktif");
                    badge.setStyle("-fx-background-color: " + (value ? "#E8F5E9" : "#FFEBEE") + "; " +
                                  "-fx-padding: 3 8; -fx-background-radius: 10; -fx-font-size: 10px;");
                    setGraphic(badge);
                }
            }
        });
        
        // Action column
        TableColumn<DiscountConfig, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox actionBox = new HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                                "-fx-padding: 5 10; -fx-background-radius: 5; -fx-cursor: hand;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; " +
                                  "-fx-padding: 5 10; -fx-background-radius: 5; -fx-cursor: hand;");
                actionBox.setAlignment(Pos.CENTER);
                
                editBtn.setOnAction(e -> {
                    DiscountConfig item = getTableView().getItems().get(getIndex());
                    loadToForm(item);
                });
                
                deleteBtn.setOnAction(e -> {
                    DiscountConfig item = getTableView().getItems().get(getIndex());
                    handleDeleteDiscount(item);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
        
        discountTable.getColumns().addAll(nameCol, codeCol, typeCol, valueCol, minPurchaseCol, minItemsCol, activeCol, actionCol);
        
        // Select listener
        discountTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadToForm(newVal);
            }
        });
        
        // Search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                discountTable.setItems(discountService.getDiscountConfigs());
            } else {
                FilteredList<DiscountConfig> filtered = new FilteredList<>(discountService.getDiscountConfigs(), 
                    item -> item.getName().toLowerCase().contains(newVal.toLowerCase()) ||
                           item.getCode().toLowerCase().contains(newVal.toLowerCase())
                );
                discountTable.setItems(filtered);
            }
        });
        
        tablePanel.getChildren().addAll(tableHeader, discountTable);
        return tablePanel;
    }
    
    private void handleSaveDiscount() {
        try {
            String name = nameField.getText().trim();
            String code = codeField.getText().trim().toUpperCase();
            String type = typeCombo.getValue();
            double value = Double.parseDouble(valueField.getText().trim());
            double minPurchase = Double.parseDouble(minPurchaseField.getText().trim());
            int minItems = Integer.parseInt(minItemsField.getText().trim());
            boolean active = activeCheckBox.isSelected();
            
            if (name.isEmpty() || code.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validasi", "Nama dan Kode diskon harus diisi!");
                return;
            }
            
            // Check if editing existing
            DiscountConfig existing = discountService.findByCode(code);
            
            if (existing != null) {
                // Update existing
                existing.setName(name);
                existing.setType(type);
                existing.setValue(value);
                existing.setMinPurchase(minPurchase);
                existing.setMinItems(minItems);
                existing.setActive(active);
                discountTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", 
                    "Diskon '" + name + "' berhasil diperbarui!\n\n" +
                    "✅ Perubahan langsung tersedia untuk Kasir.");
            } else {
                // Add new
                DiscountConfig newItem = new DiscountConfig(name, code, type, value, minPurchase, minItems, active);
                discountService.addDiscount(newItem);
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", 
                    "Diskon baru '" + name + "' berhasil ditambahkan!\n\n" +
                    "✅ Diskon langsung tersedia untuk Kasir dengan kode: " + code);
            }
            
            clearForm();
            updateStats();
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Format angka tidak valid!");
        }
    }
    
    private void handleDeleteDiscount(DiscountConfig item) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText("Hapus Diskon: " + item.getName());
        confirm.setContentText("Yakin ingin menghapus diskon ini?\n\nDiskon tidak akan tersedia lagi untuk Kasir.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                discountService.removeDiscount(item.getCode());
                clearForm();
                updateStats();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Diskon berhasil dihapus!");
            }
        });
    }
    
    private void loadToForm(DiscountConfig item) {
        nameField.setText(item.getName());
        codeField.setText(item.getCode());
        typeCombo.setValue(item.getType());
        valueField.setText(String.valueOf((int)item.getValue()));
        minPurchaseField.setText(String.valueOf((int)item.getMinPurchase()));
        minItemsField.setText(String.valueOf(item.getMinItems()));
        activeCheckBox.setSelected(item.isActive());
    }
    
    private void clearForm() {
        nameField.clear();
        codeField.clear();
        typeCombo.setValue("Persentase");
        valueField.clear();
        minPurchaseField.setText("0");
        minItemsField.setText("0");
        activeCheckBox.setSelected(true);
        discountTable.getSelectionModel().clearSelection();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
