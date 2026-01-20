package com.upb.agripos.view;

import com.upb.agripos.controller.LoginController;
import com.upb.agripos.controller.PosController;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Main View dengan Tab-based interface - Responsive Design
 * Menampilkan menu berbeda untuk Admin vs Kasir (FR-5)
 * 
 * Admin: Dashboard, Manajemen Produk, Manajemen Diskon, Laporan
 * Kasir: Transaksi Baru, Daftar Produk, Riwayat Transaksi
 */
public class MainView {
    private final PosController posController;
    private final LoginController loginController;
    private final Runnable onLogout;
    private final Stage stage;

    private TabPane tabPane;
    private Label userInfoLabel;
    private Label dateTimeLabel;
    private BorderPane root;

    // Sub-views
    private DashboardView dashboardView;
    private ProductManagementView productManagementView;
    private TransactionView transactionView;
    private ReportView reportView;
    private DiscountManagementView discountManagementView;

    public MainView(PosController posController, LoginController loginController, 
                   Runnable onLogout, Stage stage) {
        this.posController = posController;
        this.loginController = loginController;
        this.onLogout = onLogout;
        this.stage = stage;
    }

    public Scene createScene() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        root.setTop(createHeader());

        // Main content dengan tabs
        root.setCenter(createMainContent());

        Scene scene = new Scene(root, 1280, 800);
        stage.setTitle("🌾 Agri-POS - Point of Sale System");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        // Responsive listener
        ChangeListener<Number> responsiveListener = (obs, oldVal, newVal) -> {
            applyResponsiveStyles(scene.getWidth(), scene.getHeight());
        };
        scene.widthProperty().addListener(responsiveListener);
        scene.heightProperty().addListener(responsiveListener);
        
        return scene;
    }
    
    /**
     * Apply responsive styles berdasarkan ukuran window
     */
    private void applyResponsiveStyles(double width, double height) {
        boolean isCompact = width < 1000;
        boolean isMobile = width < 800;
        
        // Adjust tab font size
        if (isMobile) {
            tabPane.setStyle("-fx-font-size: 11px; -fx-tab-min-height: 35;");
        } else if (isCompact) {
            tabPane.setStyle("-fx-font-size: 12px; -fx-tab-min-height: 38;");
        } else {
            tabPane.setStyle("-fx-font-size: 14px; -fx-tab-min-height: 40;");
        }
    }

    private VBox createHeader() {
        VBox headerContainer = new VBox(0);
        
        // Main Header
        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);
        header.setStyle("-fx-background-color: linear-gradient(to right, #1B5E20, #2E7D32);");

        // Logo & Title
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        Label logo = new Label("🌾");
        logo.setStyle("-fx-font-size: 32px;");
        VBox titleBox = new VBox(0);
        Label appName = new Label("AGRI-POS");
        appName.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label tagline = new Label("Point of Sale System");
        tagline.setStyle("-fx-font-size: 11px; -fx-text-fill: #C8E6C9;");
        titleBox.getChildren().addAll(appName, tagline);
        logoBox.getChildren().addAll(logo, titleBox);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // DateTime
        VBox dateTimeBox = new VBox(2);
        dateTimeBox.setAlignment(Pos.CENTER_RIGHT);
        LocalDateTime now = LocalDateTime.now();
        Label dateLabel = new Label("📅 " + now.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"))));
        dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #C8E6C9;");
        dateTimeLabel = new Label("🕐 " + now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        dateTimeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #C8E6C9;");
        dateTimeBox.getChildren().addAll(dateLabel, dateTimeLabel);

        // User info card
        HBox userCard = new HBox(12);
        userCard.setAlignment(Pos.CENTER);
        userCard.setPadding(new Insets(10, 15, 10, 15));
        userCard.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-background-radius: 25;");
        
        String role = loginController.getRoleDisplayName();
        String username = loginController.getCurrentUser().getUsername();
        boolean isAdmin = loginController.isAdmin();
        
        Label roleIcon = new Label(isAdmin ? "👔" : "🏪");
        roleIcon.setStyle("-fx-font-size: 24px;");
        
        VBox userInfo = new VBox(0);
        userInfo.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label(username);
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label roleBadge = new Label(role);
        roleBadge.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-background-color: " + 
                         (isAdmin ? "#FF9800" : "#2196F3") + "; -fx-padding: 2 8; -fx-background-radius: 10;");
        userInfo.getChildren().addAll(usernameLabel, roleBadge);
        
        userCard.getChildren().addAll(roleIcon, userInfo);

        // Logout button
        Button logoutButton = new Button("🚪 Logout");
        logoutButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; " +
                             "-fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand;");
        logoutButton.setOnAction(e -> handleLogout());

        header.getChildren().addAll(logoBox, spacer, dateTimeBox, userCard, logoutButton);
        
        // Role indicator bar
        HBox roleBar = new HBox();
        roleBar.setPadding(new Insets(8, 25, 8, 25));
        roleBar.setAlignment(Pos.CENTER_LEFT);
        String barColor = isAdmin ? "#FF9800" : "#2196F3";
        roleBar.setStyle("-fx-background-color: " + barColor + ";");
        
        Label roleInfoLabel = new Label(isAdmin ? 
            "👔 Mode Administrator - Kelola produk, lihat laporan, dan pantau stok" :
            "🏪 Mode Kasir - Proses transaksi dan layani pelanggan");
        roleInfoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
        
        roleBar.getChildren().add(roleInfoLabel);
        
        headerContainer.getChildren().addAll(header, roleBar);
        return headerContainer;
    }

    private TabPane createMainContent() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 14px; -fx-tab-min-height: 40;");

        if (loginController.isAdmin()) {
            // === ADMIN VIEW ===
            createAdminTabs();
        } else {
            // === KASIR VIEW ===
            createKasirTabs();
        }

        // Refresh data when tab changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().contains("Dashboard")) {
                if (dashboardView != null) {
                    dashboardView = new DashboardView(posController);
                    newTab.setContent(dashboardView.createContent());
                }
            }
        });

        return tabPane;
    }

    private void createAdminTabs() {
        // Tab 1: Dashboard
        Tab dashboardTab = new Tab("  📊 Dashboard  ");
        dashboardTab.setStyle("-fx-font-weight: bold;");
        dashboardView = new DashboardView(posController);
        dashboardTab.setContent(dashboardView.createContent());
        tabPane.getTabs().add(dashboardTab);

        // Tab 2: Manajemen Produk
        Tab productTab = new Tab("  📦 Manajemen Produk  ");
        productManagementView = new ProductManagementView(posController);
        productTab.setContent(productManagementView.createContent());
        tabPane.getTabs().add(productTab);
        
        // Tab 3: Manajemen Diskon
        Tab discountTab = new Tab("  🎁 Manajemen Diskon  ");
        discountManagementView = new DiscountManagementView(posController);
        discountTab.setContent(discountManagementView.createContent());
        tabPane.getTabs().add(discountTab);

        // Tab 4: Laporan
        Tab reportTab = new Tab("  📈 Laporan Penjualan  ");
        reportView = new ReportView(posController);
        reportTab.setContent(reportView.createContent());
        tabPane.getTabs().add(reportTab);
    }

    private void createKasirTabs() {
        // Tab 1: Transaksi Baru
        Tab transactionTab = new Tab("  🛒 Transaksi Baru  ");
        transactionTab.setStyle("-fx-font-weight: bold;");
        transactionView = new TransactionView(posController);
        transactionTab.setContent(transactionView.createContent());
        tabPane.getTabs().add(transactionTab);

        // Tab 2: Daftar Produk (Read-Only)
        Tab productListTab = new Tab("  📦 Daftar Produk  ");
        productListTab.setContent(createReadOnlyProductList());
        tabPane.getTabs().add(productListTab);

        // Tab 3: Riwayat Transaksi Saya
        Tab historyTab = new Tab("  📋 Riwayat Transaksi  ");
        historyTab.setContent(createTransactionHistoryView());
        tabPane.getTabs().add(historyTab);
    }

    /**
     * Create Read-Only Product List for Kasir
     * Tampilan modern dengan informasi stok yang jelas
     */
    private VBox createReadOnlyProductList() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(25));
        panel.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #f5f5f5);");

        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        
        Label icon = new Label("📦");
        icon.setStyle("-fx-font-size: 28px;");
        
        VBox titleBox = new VBox(2);
        Label title = new Label("Daftar Produk");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1976D2;");
        Label subtitle = new Label("Lihat informasi produk dan ketersediaan stok");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Product count
        int totalProducts = posController.getProductList().size();
        Label countLabel = new Label("📊 " + totalProducts + " produk tersedia");
        countLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        header.getChildren().addAll(icon, titleBox, spacer, countLabel);

        // Search & Filter Section
        HBox filterSection = new HBox(15);
        filterSection.setAlignment(Pos.CENTER_LEFT);
        filterSection.setPadding(new Insets(15));
        filterSection.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari nama atau kode produk...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-padding: 10; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #ddd;");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().add("📁 Semua Kategori");
        posController.getProductList().stream()
            .map(p -> p.getCategory())
            .distinct()
            .sorted()
            .forEach(cat -> categoryCombo.getItems().add(cat));
        categoryCombo.getSelectionModel().selectFirst();
        categoryCombo.setStyle("-fx-background-radius: 20; -fx-padding: 5;");

        filterSection.getChildren().addAll(new Label("Filter:"), searchField, categoryCombo);

        // Product table with enhanced styling
        TableView<com.upb.agripos.model.Product> productTable = new TableView<>();
        productTable.setItems(posController.getProductList());
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productTable.setStyle("-fx-background-radius: 15;");

        TableColumn<com.upb.agripos.model.Product, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        codeCol.setPrefWidth(80);
        codeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(value);
                    badge.setStyle("-fx-background-color: #ECEFF1; -fx-padding: 3 8; -fx-background-radius: 5; -fx-font-family: monospace;");
                    setGraphic(badge);
                }
            }
        });

        TableColumn<com.upb.agripos.model.Product, String> nameCol = new TableColumn<>("Nama Produk");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        nameCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(value);
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });

        TableColumn<com.upb.agripos.model.Product, String> categoryCol = new TableColumn<>("Kategori");
        categoryCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        categoryCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(value);
                    badge.setStyle("-fx-background-color: #E3F2FD; -fx-text-fill: #1976D2; " +
                                  "-fx-padding: 3 10; -fx-background-radius: 15; -fx-font-size: 11px;");
                    setGraphic(badge);
                }
            }
        });

        TableColumn<com.upb.agripos.model.Product, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(posController.formatCurrency(value));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #2E7D32;");
                }
            }
        });

        TableColumn<com.upb.agripos.model.Product, Integer> stockCol = new TableColumn<>("Status Stok");
        stockCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(130);
        stockCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5);
                    box.setAlignment(Pos.CENTER_LEFT);
                    
                    String emoji, bgColor, textColor, status;
                    if (value < 5) {
                        emoji = "🔴"; bgColor = "#FFEBEE"; textColor = "#f44336"; status = "Hampir Habis";
                    } else if (value < 10) {
                        emoji = "🟡"; bgColor = "#FFF3E0"; textColor = "#ff9800"; status = "Stok Rendah";
                    } else if (value < 20) {
                        emoji = "🟢"; bgColor = "#E8F5E9"; textColor = "#4CAF50"; status = "Tersedia";
                    } else {
                        emoji = "✅"; bgColor = "#E8F5E9"; textColor = "#2E7D32"; status = "Stok Banyak";
                    }
                    
                    Label statusLabel = new Label(emoji + " " + value + " - " + status);
                    statusLabel.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; " +
                                        "-fx-padding: 4 10; -fx-background-radius: 15; -fx-font-size: 11px; -fx-font-weight: bold;");
                    box.getChildren().add(statusLabel);
                    setGraphic(box);
                }
            }
        });

        productTable.getColumns().addAll(codeCol, nameCol, categoryCol, priceCol, stockCol);

        // Filter logic
        searchField.textProperty().addListener((obs, old, newVal) -> {
            filterProducts(productTable, searchField.getText(), categoryCombo.getValue());
        });
        categoryCombo.setOnAction(e -> {
            filterProducts(productTable, searchField.getText(), categoryCombo.getValue());
        });

        // Bottom info
        HBox bottomInfo = new HBox(15);
        bottomInfo.setAlignment(Pos.CENTER_LEFT);
        bottomInfo.setPadding(new Insets(10));
        bottomInfo.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");
        Label infoLabel = new Label("💡 Tip: Klik pada tab 'Transaksi Baru' untuk memulai penjualan");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2E7D32;");
        bottomInfo.getChildren().add(infoLabel);

        VBox.setVgrow(productTable, Priority.ALWAYS);
        panel.getChildren().addAll(header, filterSection, productTable, bottomInfo);
        return panel;
    }

    private void filterProducts(TableView<com.upb.agripos.model.Product> table, String search, String category) {
        String cleanCategory = category != null && category.startsWith("📁 ") ? category.substring(3) : category;
        table.setItems(posController.getProductList().filtered(p -> {
            boolean matchSearch = search == null || search.isEmpty() ||
                p.getCode().toLowerCase().contains(search.toLowerCase()) ||
                p.getName().toLowerCase().contains(search.toLowerCase());
            boolean matchCategory = cleanCategory == null || cleanCategory.equals("Semua Kategori") ||
                p.getCategory().equals(cleanCategory);
            return matchSearch && matchCategory;
        }));
    }

    /**
     * Create Transaction History View for Kasir
     * Tampilan riwayat transaksi dengan statistik
     */
    private VBox createTransactionHistoryView() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(25));
        panel.setStyle("-fx-background-color: linear-gradient(to bottom, #fff3e0, #f5f5f5);");

        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        
        Label icon = new Label("📋");
        icon.setStyle("-fx-font-size: 28px;");
        
        VBox titleBox = new VBox(2);
        Label title = new Label("Riwayat Transaksi Hari Ini");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #E65100;");
        Label subtitle = new Label("Lihat semua transaksi yang telah Anda proses");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        titleBox.getChildren().addAll(title, subtitle);

        header.getChildren().addAll(icon, titleBox);

        // Summary Cards
        HBox summaryCards = new HBox(20);
        summaryCards.setAlignment(Pos.CENTER);

        // Load today's transactions
        java.util.List<com.upb.agripos.model.Transaction> todayTransactions = new java.util.ArrayList<>();
        try {
            todayTransactions = posController.getTransactionsByDate(java.time.LocalDate.now());
            System.out.println("Loaded " + todayTransactions.size() + " transactions for today");
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }

        int totalTx = todayTransactions.size();
        double totalSales = todayTransactions.stream().mapToDouble(t -> t.getTotal()).sum();
        double avgTransaction = totalTx > 0 ? totalSales / totalTx : 0;

        VBox txCountCard = createSummaryCard("🧾", "Jumlah Transaksi", String.valueOf(totalTx) + " transaksi", "#2196F3", "#E3F2FD");
        VBox salesCard = createSummaryCard("💰", "Total Penjualan", posController.formatCurrency(totalSales), "#4CAF50", "#E8F5E9");
        VBox avgCard = createSummaryCard("📊", "Rata-rata/Transaksi", posController.formatCurrency(avgTransaction), "#FF9800", "#FFF3E0");

        summaryCards.getChildren().addAll(txCountCard, salesCard, avgCard);
        HBox.setHgrow(txCountCard, Priority.ALWAYS);
        HBox.setHgrow(salesCard, Priority.ALWAYS);
        HBox.setHgrow(avgCard, Priority.ALWAYS);

        // Transaction table
        TableView<com.upb.agripos.model.Transaction> transactionTable = new TableView<>();
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        transactionTable.setStyle("-fx-background-radius: 15;");
        transactionTable.setPlaceholder(new Label("📋 Belum ada transaksi hari ini.\nLakukan transaksi baru untuk melihat riwayat."));

        TableColumn<com.upb.agripos.model.Transaction, String> codeCol = new TableColumn<>("Kode Transaksi");
        codeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("transactionCode"));
        codeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(value);
                    badge.setStyle("-fx-background-color: #ECEFF1; -fx-padding: 3 8; -fx-background-radius: 5; -fx-font-family: monospace; -fx-font-size: 11px;");
                    setGraphic(badge);
                }
            }
        });

        TableColumn<com.upb.agripos.model.Transaction, String> timeCol = new TableColumn<>("Waktu");
        timeCol.setCellValueFactory(data -> {
            java.time.LocalDateTime dt = data.getValue().getTransactionDate();
            return new javafx.beans.property.SimpleStringProperty(
                dt.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
            );
        });
        timeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText("🕐 " + value);
                }
            }
        });

        TableColumn<com.upb.agripos.model.Transaction, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("total"));
        totalCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(posController.formatCurrency(value));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #2E7D32;");
                }
            }
        });

        TableColumn<com.upb.agripos.model.Transaction, String> methodCol = new TableColumn<>("Metode Bayar");
        methodCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("paymentMethod"));
        methodCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    String emoji = value.contains("TUNAI") ? "💵" : (value.contains("QRIS") ? "📷" : "📱");
                    Label badge = new Label(emoji + " " + value);
                    badge.setStyle("-fx-background-color: #E3F2FD; -fx-text-fill: #1976D2; " +
                                  "-fx-padding: 3 8; -fx-background-radius: 10; -fx-font-size: 11px;");
                    setGraphic(badge);
                }
            }
        });

        TableColumn<com.upb.agripos.model.Transaction, com.upb.agripos.model.Transaction.TransactionStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(com.upb.agripos.model.Transaction.TransactionStatus value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label("✅ " + value.getDisplayName());
                    badge.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #4CAF50; " +
                                  "-fx-padding: 3 8; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 11px;");
                    setGraphic(badge);
                }
            }
        });

        transactionTable.getColumns().addAll(codeCol, timeCol, totalCol, methodCol, statusCol);
        
        // Tambahkan kolom aksi untuk cetak struk
        TableColumn<com.upb.agripos.model.Transaction, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(100);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button printBtn = new Button("🖨️ Struk");
            {
                printBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 5 10; -fx-background-radius: 10; -fx-cursor: hand;");
                printBtn.setOnAction(e -> {
                    com.upb.agripos.model.Transaction tx = getTableView().getItems().get(getIndex());
                    showTransactionReceipt(tx);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(printBtn);
                }
            }
        });
        transactionTable.getColumns().add(actionCol);
        
        transactionTable.getItems().addAll(todayTransactions);

        // Refresh button
        HBox bottomActions = new HBox(15);
        bottomActions.setAlignment(Pos.CENTER_RIGHT);
        
        Button refreshBtn = new Button("🔄 Refresh Data");
        refreshBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20;");
        refreshBtn.setOnAction(e -> {
            try {
                transactionTable.getItems().clear();
                java.util.List<com.upb.agripos.model.Transaction> transactions = 
                    posController.getTransactionsByDate(java.time.LocalDate.now());
                transactionTable.getItems().addAll(transactions);
                System.out.println("Refreshed: " + transactions.size() + " transactions loaded");
                
                // Update summary cards
                int totalTxUpdated = transactions.size();
                double totalSalesUpdated = transactions.stream().mapToDouble(t -> t.getTotal()).sum();
                double avgTransactionUpdated = totalTxUpdated > 0 ? totalSalesUpdated / totalTxUpdated : 0;
                
                // Show info
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Data Diperbarui");
                info.setHeaderText(null);
                info.setContentText("Berhasil memuat " + totalTxUpdated + " transaksi hari ini.");
                info.showAndWait();
            } catch (Exception ex) {
                System.err.println("Error refreshing: " + ex.getMessage());
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText(null);
                error.setContentText("Gagal memuat data: " + ex.getMessage());
                error.showAndWait();
            }
        });

        bottomActions.getChildren().add(refreshBtn);

        VBox.setVgrow(transactionTable, Priority.ALWAYS);
        panel.getChildren().addAll(header, summaryCards, transactionTable, bottomActions);
        return panel;
    }

    private VBox createSummaryCard(String emoji, String title, String value, String accentColor, String bgColor) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 15; " +
                     "-fx-border-color: " + accentColor + "; -fx-border-width: 0 0 3 0; -fx-border-radius: 15;");
        card.setMinWidth(180);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 28px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");

        card.getChildren().addAll(emojiLabel, titleLabel, valueLabel);
        return card;
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin logout?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loginController.logout();
                onLogout.run();
            }
        });
    }

    /**
     * Tampilkan struk transaksi dari riwayat
     */
    private void showTransactionReceipt(com.upb.agripos.model.Transaction transaction) {
        String receipt = posController.generateReceipt(transaction);
        
        Alert receiptDialog = new Alert(Alert.AlertType.INFORMATION);
        receiptDialog.setTitle("Struk Transaksi");
        receiptDialog.setHeaderText("Transaksi: " + transaction.getTransactionCode());
        
        TextArea receiptArea = new TextArea(receipt);
        receiptArea.setEditable(false);
        receiptArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 12px;");
        receiptArea.setPrefWidth(400);
        receiptArea.setPrefHeight(400);
        
        receiptDialog.getDialogPane().setContent(receiptArea);
        receiptDialog.getDialogPane().setPrefWidth(450);
        receiptDialog.showAndWait();
    }

    public void refresh() {
        posController.loadProducts();
    }
}
