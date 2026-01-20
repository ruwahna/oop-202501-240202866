package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.CheckoutSummary;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.DiscountConfigService;
import com.upb.agripos.service.DiscountConfigService.DiscountConfig;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * View untuk Transaksi/Checkout (FR-2, FR-3, FR-4)
 * Tampilan lengkap untuk Kasir Point of Sale
 * Diskon diambil dari DiscountConfigService (dikelola oleh Admin)
 */
public class TransactionView {
    private final PosController controller;
    private final DiscountConfigService discountService;

    private TableView<Product> productTable;
    private ListView<CartItem> cartListView;
    private TextField qtyField, amountPaidField, phoneField, searchField;
    private ComboBox<String> paymentMethodCombo, categoryCombo, providerCombo;
    private ComboBox<String> discountCombo;
    private Label subtotalLabel, discountLabel, taxLabel, totalLabel, changeLabel;
    private Label discountAppliedLabel;
    private TextArea receiptArea;
    private RadioButton cashRadio, ewalletRadio, qrisRadio;
    private ToggleGroup paymentToggle;
    private VBox ewalletOptions;

    public TransactionView(PosController controller) {
        this.controller = controller;
        this.discountService = DiscountConfigService.getInstance();
    }

    public ScrollPane createContent() {
        HBox content = new HBox(12);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e9, #f5f5f5);");

        // Left: Product selection
        content.getChildren().add(createProductPanel());

        // Center: Cart
        content.getChildren().add(createCartPanel());

        // Right: Payment & Receipt
        content.getChildren().add(createPaymentReceiptPanel());

        HBox.setHgrow(content.getChildren().get(0), Priority.ALWAYS);
        
        // Wrap in ScrollPane for scrolling
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        return scrollPane;
    }

    private VBox createProductPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(450);
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("📦");
        icon.setStyle("-fx-font-size: 20px;");
        Label title = new Label("PILIH PRODUK");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");
        header.getChildren().addAll(icon, title);

        // Search
        searchField = new TextField();
        searchField.setPromptText("🔍 Ketik nama atau kode produk...");
        searchField.setStyle("-fx-font-size: 12px; -fx-padding: 10; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #4CAF50;");

        // Category Tabs
        TabPane categoryTabs = new TabPane();
        categoryTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        categoryTabs.setStyle("-fx-background-color: transparent;");
        
        // Tab "Semua"
        Tab allTab = new Tab("📋 Semua");
        allTab.setStyle("-fx-font-size: 11px;");
        
        // Get unique categories
        java.util.List<String> categories = controller.getProductList().stream()
            .map(Product::getCategory)
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        
        categoryTabs.getTabs().add(allTab);
        
        // Add tabs for each category with emoji
        for (String cat : categories) {
            String emoji = getCategoryEmoji(cat);
            Tab catTab = new Tab(emoji + " " + cat);
            catTab.setStyle("-fx-font-size: 11px;");
            categoryTabs.getTabs().add(catTab);
        }

        // Product table
        productTable = new TableView<>();
        productTable.setItems(controller.getProductList());
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productTable.setStyle("-fx-font-size: 12px;");
        productTable.setFixedCellSize(40);
        productTable.setPlaceholder(new Label("Tidak ada produk"));

        TableColumn<Product, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setPrefWidth(70);
        codeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Product, String> nameCol = new TableColumn<>("Nama Produk");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, String> catCol = new TableColumn<>("Kategori");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setPrefWidth(90);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);
        priceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(controller.formatCurrency(value));
                    setStyle("-fx-text-fill: #1976D2; -fx-font-weight: bold;");
                }
            }
        });

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
                    if (value < 10) {
                        setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #2E7D32;");
                    }
                }
            }
        });

        productTable.getColumns().addAll(codeCol, nameCol, catCol, priceCol, stockCol);

        // Tab change listener
        categoryTabs.getSelectionModel().selectedItemProperty().addListener((obs, old, newTab) -> {
            if (newTab != null) {
                String tabText = newTab.getText();
                if (tabText.contains("Semua")) {
                    filterByCategory(null);
                } else {
                    // Remove emoji prefix
                    String category = tabText.substring(tabText.indexOf(" ") + 1);
                    filterByCategory(category);
                }
            }
        });

        // Search logic
        searchField.textProperty().addListener((obs, old, newVal) -> {
            Tab selectedTab = categoryTabs.getSelectionModel().getSelectedItem();
            String category = null;
            if (selectedTab != null && !selectedTab.getText().contains("Semua")) {
                category = selectedTab.getText().substring(selectedTab.getText().indexOf(" ") + 1);
            }
            filterProducts(newVal, category);
        });

        // Add to cart section
        VBox addSection = new VBox(8);
        addSection.setPadding(new Insets(10));
        addSection.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");
        
        Label selectedLabel = new Label("Produk terpilih: -");
        selectedLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newProd) -> {
            if (newProd != null) {
                selectedLabel.setText("Produk: " + newProd.getName() + " @ " + controller.formatCurrency(newProd.getPrice()));
            } else {
                selectedLabel.setText("Produk terpilih: -");
            }
        });

        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER);

        Label qtyLbl = new Label("Jumlah:");
        qtyLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        qtyField = new TextField("1");
        qtyField.setPrefWidth(60);
        qtyField.setStyle("-fx-font-size: 14px; -fx-alignment: center; -fx-font-weight: bold;");

        Button addButton = new Button("🛒 TAMBAH KE KERANJANG");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand;");
        addButton.setOnAction(e -> handleAddToCart());
        addButton.disableProperty().bind(productTable.getSelectionModel().selectedItemProperty().isNull());

        addBox.getChildren().addAll(qtyLbl, qtyField, addButton);
        addSection.getChildren().addAll(selectedLabel, addBox);

        VBox.setVgrow(productTable, Priority.ALWAYS);
        panel.getChildren().addAll(header, searchField, categoryTabs, productTable, addSection);
        return panel;
    }
    
    private String getCategoryEmoji(String category) {
        if (category == null) return "📦";
        String lower = category.toLowerCase();
        if (lower.contains("sayur")) return "🥬";
        if (lower.contains("buah")) return "🍎";
        if (lower.contains("beras") || lower.contains("padi")) return "🌾";
        if (lower.contains("pupuk")) return "🧪";
        if (lower.contains("bibit") || lower.contains("benih")) return "🌱";
        if (lower.contains("alat") || lower.contains("pertanian")) return "🔧";
        if (lower.contains("obat") || lower.contains("pestisida")) return "💊";
        return "📦";
    }
    
    private void filterByCategory(String category) {
        if (category == null || category.isEmpty()) {
            productTable.setItems(controller.getProductList());
        } else {
            productTable.setItems(controller.getProductList().filtered(p -> 
                p.getCategory().equals(category)));
        }
    }
    
    private void filterProducts(String search, String category) {
        productTable.setItems(controller.getProductList().filtered(p -> {
            boolean matchSearch = search == null || search.isEmpty() ||
                p.getCode().toLowerCase().contains(search.toLowerCase()) ||
                p.getName().toLowerCase().contains(search.toLowerCase());
            boolean matchCategory = category == null || category.isEmpty() ||
                p.getCategory().equals(category);
            return matchSearch && matchCategory;
        }));
    }

    private void filterProducts(String search) {
        productTable.setItems(controller.getProductList().filtered(p -> {
            boolean matchSearch = search == null || search.isEmpty() ||
                p.getCode().toLowerCase().contains(search.toLowerCase()) ||
                p.getName().toLowerCase().contains(search.toLowerCase());
            return matchSearch;
        }));
    }

    private VBox createCartPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(340);
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("🛒");
        icon.setStyle("-fx-font-size: 20px;");
        Label title = new Label("KERANJANG BELANJA");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FF6F00;");
        header.getChildren().addAll(icon, title);

        cartListView = new ListView<>();
        cartListView.setItems(controller.getCartItems());
        cartListView.setPrefHeight(200);
        cartListView.setStyle("-fx-background-radius: 8;");
        cartListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    HBox row = new HBox(8);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPadding(new Insets(5));
                    row.setStyle("-fx-background-color: #FFF8E1; -fx-background-radius: 8;");

                    VBox info = new VBox(2);
                    Label nameLabel = new Label(item.getProductName());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
                    Label detailLabel = new Label(String.format("%d × %s",
                        item.getQuantity(),
                        controller.formatCurrency(item.getUnitPrice())));
                    detailLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
                    Label subtotalLabel = new Label(controller.formatCurrency(item.getSubtotal()));
                    subtotalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                    info.getChildren().addAll(nameLabel, detailLabel, subtotalLabel);

                    // Quantity controls
                    HBox qtyBox = new HBox(5);
                    qtyBox.setAlignment(Pos.CENTER);
                    Button minusBtn = new Button("−");
                    minusBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 12px; -fx-min-width: 28; -fx-min-height: 28; -fx-background-radius: 14; -fx-cursor: hand;");
                    minusBtn.setOnAction(e -> {
                        try {
                            if (item.getQuantity() > 1) {
                                controller.updateCartQuantity(item.getProductCode(), item.getQuantity() - 1);
                                updateSummary();
                            }
                        } catch (Exception ex) {}
                    });
                    
                    Label qtyLabel = new Label(String.valueOf(item.getQuantity()));
                    qtyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-min-width: 25; -fx-alignment: center;");
                    qtyLabel.setAlignment(Pos.CENTER);
                    
                    Button plusBtn = new Button("+");
                    plusBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-min-width: 28; -fx-min-height: 28; -fx-background-radius: 14; -fx-cursor: hand;");
                    plusBtn.setOnAction(e -> {
                        try {
                            controller.updateCartQuantity(item.getProductCode(), item.getQuantity() + 1);
                            updateSummary();
                        } catch (Exception ex) {
                            showError(ex.getMessage());
                        }
                    });
                    qtyBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

                    Button removeBtn = new Button("🗑");
                    removeBtn.setStyle("-fx-background-color: #EF5350; -fx-text-fill: white; -fx-font-size: 12px; -fx-min-width: 28; -fx-min-height: 28; -fx-background-radius: 14; -fx-cursor: hand;");
                    removeBtn.setOnAction(e -> handleRemoveFromCart(item.getProductCode()));

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    row.getChildren().addAll(info, spacer, qtyBox, removeBtn);
                    setGraphic(row);
                    setText(null);
                }
            }
        });

        // Summary
        VBox summaryBox = new VBox(6);
        summaryBox.setPadding(new Insets(12));
        summaryBox.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 8;");

        subtotalLabel = new Label("Subtotal: Rp 0");
        subtotalLabel.setStyle("-fx-font-size: 12px;");
        
        discountLabel = new Label("Diskon: Rp 0");
        discountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #E91E63;");
        
        taxLabel = new Label("Pajak (10%): Rp 0");
        taxLabel.setStyle("-fx-font-size: 12px;");
        
        Separator sep = new Separator();
        
        totalLabel = new Label("TOTAL: Rp 0");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        summaryBox.getChildren().addAll(subtotalLabel, discountLabel, taxLabel, sep, totalLabel);

        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button clearButton = new Button("🗑️ Kosongkan");
        clearButton.setStyle("-fx-background-color: #78909C; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 8 15; -fx-background-radius: 15; -fx-cursor: hand;");
        clearButton.setOnAction(e -> handleClearCart());

        buttonBox.getChildren().addAll(clearButton);

        // Update summary when cart changes
        controller.getCartItems().addListener((javafx.collections.ListChangeListener<CartItem>) c -> updateSummary());

        VBox.setVgrow(cartListView, Priority.ALWAYS);
        panel.getChildren().addAll(header, cartListView, summaryBox, buttonBox);
        return panel;
    }

    private VBox createPaymentReceiptPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(340);

        // ========== DISCOUNT SECTION ==========
        VBox discountSection = new VBox(10);
        discountSection.setPadding(new Insets(15));
        discountSection.setStyle("-fx-background-color: #FFF3E0; -fx-background-radius: 15; " +
                                "-fx-border-color: #FF9800; -fx-border-width: 2; -fx-border-radius: 15; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox discountHeader = new HBox(10);
        discountHeader.setAlignment(Pos.CENTER);
        Label discountIcon = new Label("🎁");
        discountIcon.setStyle("-fx-font-size: 28px;");
        Label discountTitle = new Label("DISKON & PROMO");
        discountTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E65100;");
        discountHeader.getChildren().addAll(discountIcon, discountTitle);

        // Predefined discounts combo - Load dari DiscountConfigService (dikelola Admin)
        VBox comboBox = new VBox(5);
        Label comboLabel = new Label("Pilih Diskon Standar:");
        comboLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        discountCombo = new ComboBox<>();
        // Load discounts dari shared service
        refreshDiscountCombo();
        discountCombo.setPromptText("Pilih diskon...");
        discountCombo.setPrefWidth(Double.MAX_VALUE);
        discountCombo.setStyle("-fx-font-size: 12px;");
        
        // Refresh button untuk update diskon terbaru dari Admin
        Button refreshDiscountBtn = new Button("🔄");
        refreshDiscountBtn.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 5 10; -fx-background-radius: 5; -fx-cursor: hand;");
        refreshDiscountBtn.setTooltip(new Tooltip("Refresh daftar diskon dari Admin"));
        refreshDiscountBtn.setOnAction(e -> {
            refreshDiscountCombo();
            showInfo("✅ Daftar diskon diperbarui!");
        });
        
        HBox comboRow = new HBox(5);
        comboRow.getChildren().addAll(discountCombo, refreshDiscountBtn);
        HBox.setHgrow(discountCombo, Priority.ALWAYS);
        
        Button applyDiscountBtn = new Button("✅ Terapkan Diskon");
        applyDiscountBtn.setPrefWidth(Double.MAX_VALUE);
        applyDiscountBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold; " +
                                 "-fx-font-size: 12px; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");
        applyDiscountBtn.setOnAction(e -> handleApplyDiscount(discountCombo));
        
        comboBox.getChildren().addAll(comboLabel, comboRow, applyDiscountBtn);

        // Voucher code input
        VBox voucherBox = new VBox(5);
        Label voucherLabel = new Label("Atau Masukkan Kode Voucher:");
        voucherLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        TextField voucherField = new TextField();
        voucherField.setPromptText("Contoh: WELCOME, MEMBER10, PROMO50K, BULKDISKON");
        voucherField.setPrefWidth(Double.MAX_VALUE);
        voucherField.setStyle("-fx-font-size: 11px;");
        
        Button applyVoucherBtn = new Button("🎟️ Terapkan Voucher");
        applyVoucherBtn.setPrefWidth(Double.MAX_VALUE);
        applyVoucherBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; " +
                                "-fx-font-size: 12px; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");
        applyVoucherBtn.setOnAction(e -> handleApplyVoucher(voucherField));
        
        voucherBox.getChildren().addAll(voucherLabel, voucherField, applyVoucherBtn);

        // Applied discount summary
        Label appliedLabel = new Label("Diskon Terapan: Tidak ada");
        appliedLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2E7D32; " +
                             "-fx-wrap-text: true;");
        appliedLabel.setId("appliedDiscountLabel");
        
        // Clear discount button
        Button clearDiscountBtn = new Button("❌ Hapus Semua Diskon");
        clearDiscountBtn.setPrefWidth(Double.MAX_VALUE);
        clearDiscountBtn.setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #C62828; -fx-font-size: 11px; " +
                                "-fx-padding: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        clearDiscountBtn.setOnAction(e -> {
            controller.clearAllDiscounts();
            discountCombo.getSelectionModel().clearSelection();
            voucherField.clear();
            appliedLabel.setText("Diskon Terapan: Tidak ada");
            updateSummary();
        });

        discountSection.getChildren().addAll(
            discountHeader,
            comboBox,
            new Separator(),
            voucherBox,
            appliedLabel,
            clearDiscountBtn
        );

        // Store reference untuk update dari tempat lain
        this.discountAppliedLabel = appliedLabel;

        // ========== PAYMENT SECTION ==========
        VBox paymentSection = new VBox(12);
        paymentSection.setPadding(new Insets(15));
        paymentSection.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header dengan icon besar
        HBox paymentHeader = new HBox(10);
        paymentHeader.setAlignment(Pos.CENTER);
        Label payIcon = new Label("💳");
        payIcon.setStyle("-fx-font-size: 32px;");
        Label paymentTitle = new Label("PEMBAYARAN");
        paymentTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1565C0;");
        paymentHeader.getChildren().addAll(payIcon, paymentTitle);

        // ===== METODE PEMBAYARAN (Sangat Sederhana) =====
        VBox methodBox = new VBox(8);
        methodBox.setPadding(new Insets(10));
        methodBox.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10;");
        
        Label methodLabel = new Label("① Pilih Cara Bayar:");
        methodLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Toggle group untuk metode
        paymentToggle = new ToggleGroup();
        
        // TUNAI - Default dan paling umum
        cashRadio = new RadioButton("💵 TUNAI (Uang Cash)");
        cashRadio.setToggleGroup(paymentToggle);
        cashRadio.setSelected(true);
        cashRadio.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");
        
        // E-Wallet
        ewalletRadio = new RadioButton("📱 E-WALLET (GoPay/OVO/DANA)");
        ewalletRadio.setToggleGroup(paymentToggle);
        ewalletRadio.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1565C0;");
        
        // QRIS
        qrisRadio = new RadioButton("📷 QRIS (Scan Barcode)");
        qrisRadio.setToggleGroup(paymentToggle);
        qrisRadio.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #E65100;");

        methodBox.getChildren().addAll(methodLabel, cashRadio, ewalletRadio, qrisRadio);

        // E-Wallet options (hidden by default)
        ewalletOptions = new VBox(8);
        ewalletOptions.setPadding(new Insets(10));
        ewalletOptions.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 10; -fx-border-color: #2196F3; -fx-border-width: 2; -fx-border-radius: 10;");
        ewalletOptions.setVisible(false);
        ewalletOptions.setManaged(false);

        Label providerLabel = new Label("Pilih Aplikasi:");
        providerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        providerCombo = new ComboBox<>();
        providerCombo.getItems().addAll("GoPay", "OVO", "DANA", "ShopeePay", "LinkAja");
        providerCombo.getSelectionModel().selectFirst();
        providerCombo.setPrefWidth(Double.MAX_VALUE);

        Label phoneLabel = new Label("No. HP Pelanggan:");
        phoneLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        phoneField = new TextField();
        phoneField.setPromptText("Contoh: 081234567890");
        phoneField.setPrefWidth(Double.MAX_VALUE);

        ewalletOptions.getChildren().addAll(providerLabel, providerCombo, phoneLabel, phoneField);

        // Toggle listener untuk e-wallet
        paymentToggle.selectedToggleProperty().addListener((obs, old, newToggle) -> {
            boolean showEwallet = newToggle == ewalletRadio;
            ewalletOptions.setVisible(showEwallet);
            ewalletOptions.setManaged(showEwallet);
        });

        // ===== INPUT UANG YANG DITERIMA =====
        VBox amountBox = new VBox(10);
        amountBox.setPadding(new Insets(10));
        amountBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");
        
        Label amountLabel = new Label("② Uang yang Diterima:");
        amountLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        amountPaidField = new TextField();
        amountPaidField.setPromptText("Ketik jumlah uang...");
        amountPaidField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 15; " +
                                "-fx-background-radius: 10; -fx-border-color: #4CAF50; -fx-border-width: 3; -fx-border-radius: 10; " +
                                "-fx-alignment: center;");
        amountPaidField.setAlignment(Pos.CENTER);

        // Tombol nominal uang cepat
        Label quickLabel = new Label("Klik nominal cepat:");
        quickLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        
        GridPane quickButtons = new GridPane();
        quickButtons.setHgap(8);
        quickButtons.setVgap(8);
        
        String[][] nominals = {
            {"10rb", "10000"}, {"20rb", "20000"}, {"50rb", "50000"},
            {"100rb", "100000"}, {"150rb", "150000"}, {"200rb", "200000"}
        };
        
        int col = 0, row = 0;
        for (String[] nom : nominals) {
            Button btn = new Button(nom[0]);
            btn.setPrefWidth(80);
            btn.setStyle("-fx-background-color: #FFECB3; -fx-text-fill: #E65100; -fx-font-weight: bold; " +
                        "-fx-font-size: 12px; -fx-padding: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            String value = nom[1];
            btn.setOnAction(e -> amountPaidField.setText(value));
            quickButtons.add(btn, col, row);
            col++;
            if (col > 2) { col = 0; row++; }
        }
        
        // Tombol UANG PAS
        Button exactBtn = new Button("💯 UANG PAS");
        exactBtn.setPrefWidth(Double.MAX_VALUE);
        exactBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; " +
                         "-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");
        exactBtn.setOnAction(e -> {
            try {
                if (!controller.isCartEmpty()) {
                    CheckoutSummary preview = controller.previewCheckout();
                    amountPaidField.setText(String.valueOf((int) preview.getTotal()));
                }
            } catch (Exception ex) {}
        });

        amountBox.getChildren().addAll(amountLabel, amountPaidField, quickLabel, quickButtons, exactBtn);

        // ===== KEMBALIAN =====
        VBox changeBox = new VBox(5);
        changeBox.setPadding(new Insets(15));
        changeBox.setAlignment(Pos.CENTER);
        changeBox.setStyle("-fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32); -fx-background-radius: 10;");
        
        Label changeTitle = new Label("💰 KEMBALIAN");
        changeTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        changeLabel = new Label("Rp 0");
        changeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFEB3B;");
        
        changeBox.getChildren().addAll(changeTitle, changeLabel);

        // Update kembalian saat input berubah
        amountPaidField.textProperty().addListener((obs, old, newVal) -> {
            try {
                if (!controller.isCartEmpty() && !newVal.isEmpty()) {
                    double paid = Double.parseDouble(newVal);
                    CheckoutSummary preview = controller.previewCheckout();
                    double change = paid - preview.getTotal();
                    if (change >= 0) {
                        changeLabel.setText(controller.formatCurrency(change));
                        changeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFEB3B;");
                    } else {
                        changeLabel.setText("KURANG " + controller.formatCurrency(Math.abs(change)));
                        changeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FFCDD2;");
                    }
                } else {
                    changeLabel.setText("Rp 0");
                }
            } catch (Exception ex) {
                changeLabel.setText("Rp 0");
            }
        });

        // ===== TOMBOL BAYAR =====
        VBox actionBox = new VBox(10);
        
        Label actionLabel = new Label("③ Selesaikan Pembayaran:");
        actionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button checkoutButton = new Button("✅  PROSES BAYAR");
        checkoutButton.setPrefWidth(Double.MAX_VALUE);
        checkoutButton.setPrefHeight(50);
        checkoutButton.setStyle("-fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32); -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;");
        checkoutButton.setOnAction(e -> handleCheckout());

        Button cancelButton = new Button("❌ Batal / Kosongkan");
        cancelButton.setPrefWidth(Double.MAX_VALUE);
        cancelButton.setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #C62828; -fx-font-size: 12px; " +
                             "-fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> handleClearCart());

        actionBox.getChildren().addAll(actionLabel, checkoutButton, cancelButton);

        paymentSection.getChildren().addAll(
            paymentHeader,
            methodBox,
            ewalletOptions,
            amountBox,
            changeBox,
            actionBox
        );

        // ========== RECEIPT SECTION ==========
        VBox receiptSection = new VBox(10);
        receiptSection.setPadding(new Insets(15));
        receiptSection.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label receiptTitle = new Label("🧾 STRUK PEMBELIAN");
        receiptTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #455A64;");

        receiptArea = new TextArea();
        receiptArea.setEditable(false);
        receiptArea.setPrefHeight(140);
        receiptArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 10px; -fx-background-radius: 8;");

        // Receipt buttons
        HBox receiptButtons = new HBox(10);
        receiptButtons.setAlignment(Pos.CENTER);
        
        Button printBtn = new Button("🖨️ Cetak");
        printBtn.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 8 15; -fx-background-radius: 15; -fx-cursor: hand;");
        printBtn.setOnAction(e -> {
            if (receiptArea.getText().isEmpty()) {
                showError("Tidak ada struk untuk dicetak!");
                return;
            }
            // Buka dialog print
            javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
            if (job != null) {
                boolean showDialog = job.showPrintDialog(null);
                if (showDialog) {
                    // Buat Text node untuk dicetak
                    javafx.scene.text.Text printText = new javafx.scene.text.Text(receiptArea.getText());
                    printText.setFont(javafx.scene.text.Font.font("Courier New", 10));
                    boolean success = job.printPage(printText);
                    if (success) {
                        job.endJob();
                        showInfo("Struk berhasil dicetak!");
                    } else {
                        showError("Gagal mencetak struk!");
                    }
                }
            } else {
                showError("Tidak dapat membuat print job. Pastikan printer tersedia.");
            }
        });
        
        Button saveBtn = new Button("💾 Simpan");
        saveBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 8 15; -fx-background-radius: 15; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            if (receiptArea.getText().isEmpty()) {
                showError("Tidak ada struk untuk disimpan!");
                return;
            }
            // Simpan ke file
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Simpan Struk");
            fileChooser.setInitialFileName("struk_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Text Files", "*.txt"));
            java.io.File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                    writer.print(receiptArea.getText());
                    showInfo("Struk berhasil disimpan ke:\n" + file.getAbsolutePath());
                } catch (Exception ex) {
                    showError("Gagal menyimpan: " + ex.getMessage());
                }
            }
        });
        
        Button emailBtn = new Button("📧 Email");
        emailBtn.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 8 15; -fx-background-radius: 15; -fx-cursor: hand;");
        emailBtn.setOnAction(e -> showInfo("Struk dikirim via email! (simulasi)"));
        
        Button doneBtn = new Button("✅ Selesai");
        doneBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 8 15; -fx-background-radius: 15; -fx-cursor: hand;");
        doneBtn.setOnAction(e -> {
            receiptArea.clear();
            handleClearCart();
        });

        receiptButtons.getChildren().addAll(printBtn, saveBtn, emailBtn, doneBtn);

        receiptSection.getChildren().addAll(receiptTitle, receiptArea, receiptButtons);

        VBox.setVgrow(receiptSection, Priority.ALWAYS);
        panel.getChildren().addAll(discountSection, paymentSection, receiptSection);
        return panel;
    }

    private void handleAddToCart() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            int qty = Integer.parseInt(qtyField.getText().trim());
            controller.addToCart(selected, qty);
            qtyField.setText("1");
            updateSummary();
        } catch (NumberFormatException e) {
            showError("Jumlah harus berupa angka!");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void handleRemoveFromCart(String productCode) {
        try {
            controller.removeFromCart(productCode);
            updateSummary();
        } catch (Exception e) {
            showError("Gagal menghapus: " + e.getMessage());
        }
    }

    private void handleClearCart() {
        controller.clearCart();
        updateSummary();
        receiptArea.clear();
    }

    private void handlePreviewReceipt() {
        try {
            String receipt = controller.generatePreviewReceipt();
            receiptArea.setText(receipt);
        } catch (Exception e) {
            showError("Gagal preview: " + e.getMessage());
        }
    }

    private void handleApplyDiscount(ComboBox<String> discountCombo) {
        if (controller.isCartEmpty()) {
            showError("Keranjang kosong, tambahkan produk terlebih dahulu!");
            return;
        }
        
        String selected = discountCombo.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pilih diskon terlebih dahulu!");
            return;
        }
        
        try {
            // Parse kode diskon dari selection (format: "KODE - Deskripsi")
            String code = selected.split(" - ")[0].trim();
            DiscountConfig config = discountService.findByCode(code);
            
            if (config != null) {
                switch (config.getType()) {
                    case "Persentase":
                        controller.applyPercentageDiscount((int) config.getValue(), config.getName());
                        break;
                    case "Nominal":
                        controller.applyFixedDiscount(config.getValue(), config.getName());
                        break;
                    case "Bulk":
                        controller.applyBulkDiscount(config.getMinQty(), (int) config.getValue(), config.getName());
                        break;
                    case "Voucher":
                        controller.applyFixedDiscount(config.getValue(), config.getName());
                        break;
                }
                showInfo("✅ Diskon '" + config.getName() + "' berhasil diterapkan!");
            } else {
                showError("Diskon tidak ditemukan!");
            }
            
            discountCombo.getSelectionModel().clearSelection();
            updateSummary();
        } catch (Exception e) {
            showError("Gagal terapkan diskon: " + e.getMessage());
        }
    }
    
    /**
     * Refresh daftar diskon dari DiscountConfigService (dikelola Admin)
     */
    private void refreshDiscountCombo() {
        discountCombo.getItems().clear();
        for (DiscountConfig config : discountService.getActiveDiscounts()) {
            String displayText = config.getCode() + " - " + config.getName();
            if (config.getType().equals("Persentase")) {
                displayText += " (" + (int) config.getValue() + "%)";
            } else if (config.getType().equals("Nominal")) {
                displayText += " (Rp " + String.format("%,.0f", config.getValue()) + ")";
            } else if (config.getType().equals("Bulk")) {
                displayText += " (Min " + config.getMinQty() + " item, " + (int) config.getValue() + "%)";
            }
            discountCombo.getItems().add(displayText);
        }
    }

    private void handleApplyVoucher(TextField voucherField) {
        if (controller.isCartEmpty()) {
            showError("Keranjang kosong, tambahkan produk terlebih dahulu!");
            return;
        }
        
        String code = voucherField.getText().trim().toUpperCase();
        if (code.isEmpty()) {
            showError("Masukkan kode voucher!");
            return;
        }
        
        try {
            // Cari voucher dari DiscountConfigService
            DiscountConfig config = discountService.findByCode(code);
            
            if (config != null) {
                // Terapkan diskon berdasarkan tipe
                switch (config.getType()) {
                    case "Persentase":
                        controller.applyPercentageDiscount((int) config.getValue(), config.getName());
                        break;
                    case "Nominal":
                    case "Voucher":
                        controller.applyFixedDiscount(config.getValue(), config.getName());
                        break;
                    case "Bulk":
                        controller.applyBulkDiscount(config.getMinQty(), (int) config.getValue(), config.getName());
                        break;
                }
                showInfo("✅ Voucher " + code + " (" + config.getName() + ") berhasil diterapkan!");
                voucherField.clear();
                updateSummary();
            } else {
                // Fallback ke controller (untuk voucher lama yang masih hardcoded)
                controller.applyVoucherDiscount(code);
                showInfo("✅ Voucher " + code + " berhasil diterapkan!");
                voucherField.clear();
                updateSummary();
            }
        } catch (Exception e) {
            showError("❌ " + e.getMessage());
        }
    }

    private void handleCheckout() {
        if (controller.isCartEmpty()) {
            showError("Keranjang kosong!");
            return;
        }

        // Get payment method from radio buttons
        String paymentMethod;
        if (cashRadio.isSelected()) {
            paymentMethod = "Tunai"; // Harus sama dengan CashPayment.getMethodName()
        } else if (ewalletRadio.isSelected()) {
            // Format: "E-Wallet (Provider)" sesuai dengan EWalletPayment.getMethodName()
            String provider = providerCombo.getValue();
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                showError("Masukkan nomor telepon untuk E-Wallet!");
                return;
            }
            paymentMethod = "E-Wallet (" + provider + ")";
        } else if (qrisRadio.isSelected()) {
            paymentMethod = "QRIS";
        } else {
            showError("Pilih metode pembayaran!");
            return;
        }

        try {
            double amountPaid = Double.parseDouble(amountPaidField.getText().trim());
            
            // Generate receipt SEBELUM checkout (karena cart akan dikosongkan)
            String receipt = controller.generatePreviewReceipt();
            // Ambil items untuk ditampilkan di struk
            java.util.List<CartItem> itemsBeforeCheckout = new java.util.ArrayList<>(controller.getCartItems());
            
            CheckoutSummary summary = controller.processCheckout(paymentMethod, amountPaid);

            // Update change label
            changeLabel.setText("Kembalian: " + controller.formatCurrency(summary.getChangeAmount()));

            // Generate receipt dengan items yang sudah disimpan
            String finalReceipt = generateReceiptWithItems(itemsBeforeCheckout, summary);
            receiptArea.setText(finalReceipt);

            // Show success
            showInfo(String.format("Pembayaran berhasil!\nTotal: %s\nKembalian: %s",
                controller.formatCurrency(summary.getTotal()),
                controller.formatCurrency(summary.getChangeAmount())));

            // Clear fields
            amountPaidField.clear();
            phoneField.clear();
            updateSummary();

        } catch (NumberFormatException e) {
            showError("Jumlah pembayaran harus berupa angka!");
        } catch (Exception e) {
            showError("Checkout gagal: " + e.getMessage());
        }
    }

    private void updateSummary() {
        try {
            if (controller.isCartEmpty()) {
                subtotalLabel.setText("Subtotal: Rp 0");
                discountLabel.setText("Diskon: Rp 0");
                taxLabel.setText("Pajak (10%): Rp 0");
                totalLabel.setText("TOTAL: Rp 0");
                if (discountAppliedLabel != null) {
                    discountAppliedLabel.setText("Diskon Terapan: Tidak ada");
                }
            } else {
                CheckoutSummary preview = controller.previewCheckout();
                subtotalLabel.setText("Subtotal: " + controller.formatCurrency(preview.getSubtotal()));
                discountLabel.setText("Diskon: " + controller.formatCurrency(preview.getDiscount()));
                taxLabel.setText("Pajak (10%): " + controller.formatCurrency(preview.getTax()));
                totalLabel.setText("TOTAL: " + controller.formatCurrency(preview.getTotal()));
                
                // Update discount applied label
                if (discountAppliedLabel != null) {
                    String summary = controller.getDiscountSummary();
                    if (summary == null || summary.isEmpty()) {
                        discountAppliedLabel.setText("Diskon Terapan: Tidak ada");
                    } else {
                        discountAppliedLabel.setText("Diskon Terapan: " + summary);
                    }
                }
            }
        } catch (Exception e) {
            // Ignore preview errors
        }
    }

    private String generateReceiptWithItems(java.util.List<CartItem> items, CheckoutSummary summary) {
        StringBuilder sb = new StringBuilder();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        
        sb.append("================================\n");
        sb.append("          AGRI-POS             \n");
        sb.append("    Jl. Pertanian No. 123      \n");
        sb.append("      Tel: (021) 123-4567      \n");
        sb.append("================================\n\n");
        sb.append("Tanggal : ").append(now.format(dateFormatter)).append("\n");
        sb.append("Waktu   : ").append(now.format(timeFormatter)).append("\n");
        sb.append("Kasir   : ").append(controller.getCurrentUsername()).append("\n");
        sb.append("--------------------------------\n");
        
        // Tampilkan semua items
        for (CartItem item : items) {
            String name = item.getProductName();
            if (name.length() > 20) name = name.substring(0, 17) + "...";
            sb.append(String.format("%-20s\n", name));
            sb.append(String.format("  %d x %s = %s\n",
                item.getQuantity(),
                controller.formatCurrency(item.getUnitPrice()),
                controller.formatCurrency(item.getSubtotal())));
        }
        
        sb.append("--------------------------------\n");
        sb.append(String.format("%-15s %15s\n", "Subtotal:", controller.formatCurrency(summary.getSubtotal())));
        if (summary.getDiscount() > 0) {
            sb.append(String.format("%-15s %15s\n", "Diskon:", "-" + controller.formatCurrency(summary.getDiscount())));
        }
        sb.append(String.format("%-15s %15s\n", "Pajak (10%):", controller.formatCurrency(summary.getTax())));
        sb.append("================================\n");
        sb.append(String.format("%-15s %15s\n", "TOTAL:", controller.formatCurrency(summary.getTotal())));
        sb.append("================================\n");
        sb.append(String.format("%-15s %15s\n", "Metode:", summary.getPaymentMethod()));
        sb.append(String.format("%-15s %15s\n", "Dibayar:", controller.formatCurrency(summary.getAmountPaid())));
        sb.append(String.format("%-15s %15s\n", "Kembalian:", controller.formatCurrency(summary.getChangeAmount())));
        sb.append("\n--------------------------------\n");
        sb.append("    Terima kasih telah\n");
        sb.append("  berbelanja di Agri-POS!\n");
        sb.append("   Barang yang sudah dibeli\n");
        sb.append("   tidak dapat dikembalikan\n");
        sb.append("================================");
        return sb.toString();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
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
