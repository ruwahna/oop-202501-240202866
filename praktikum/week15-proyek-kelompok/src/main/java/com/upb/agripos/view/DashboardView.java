package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Dashboard View untuk Admin (FR-4 Laporan)
 * Menampilkan ringkasan penjualan dan statistik
 */
public class DashboardView {
    private final PosController controller;
    private final NumberFormat currencyFormat;

    public DashboardView(PosController controller) {
        this.controller = controller;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    }

    public ScrollPane createContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e9, #f5f5f5);");

        // Welcome Section
        VBox welcomeSection = createWelcomeSection();

        // Summary Cards
        HBox summaryCards = createSummaryCards();

        // Quick Actions
        HBox quickActions = createQuickActions();

        // Charts Section
        HBox chartsSection = createChartsSection();

        // Low Stock Alert
        VBox lowStockAlert = createLowStockAlert();

        content.getChildren().addAll(welcomeSection, summaryCards, quickActions, chartsSection, lowStockAlert);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private VBox createWelcomeSection() {
        VBox section = new VBox(5);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);");

        // Current date/time
        LocalDateTime now = LocalDateTime.now();
        String greeting;
        int hour = now.getHour();
        if (hour < 12) greeting = "Selamat Pagi";
        else if (hour < 15) greeting = "Selamat Siang";
        else if (hour < 18) greeting = "Selamat Sore";
        else greeting = "Selamat Malam";

        Label greetingLabel = new Label("👋 " + greeting + ", Admin!");
        greetingLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label dateLabel = new Label("📅 " + now.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"))));
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label subtitleLabel = new Label("Kelola toko Anda dengan mudah melalui dashboard ini");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");

        section.getChildren().addAll(greetingLabel, dateLabel, subtitleLabel);
        return section;
    }

    private HBox createQuickActions() {
        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPadding(new Insets(0, 0, 10, 0));

        Label label = new Label("⚡ Aksi Cepat:");
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button refreshBtn = createActionButton("🔄 Refresh Data", "#2196F3");
        refreshBtn.setOnAction(e -> controller.loadProducts());

        Button exportBtn = createActionButton("📊 Export Laporan", "#FF9800");
        exportBtn.setOnAction(e -> showInfo("Fitur export akan segera hadir!"));

        actions.getChildren().addAll(label, refreshBtn, exportBtn);
        return actions;
    }

    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                    "-fx-font-size: 12px; -fx-padding: 8 15; -fx-background-radius: 20; -fx-cursor: hand;");
        return btn;
    }

    private HBox createSummaryCards() {
        HBox cards = new HBox(20);
        cards.setAlignment(Pos.CENTER);

        // Get sales data from controller
        double todaySales = controller.getTodaySales();
        double weekSales = controller.getWeekSales();
        double monthSales = controller.getMonthSales();
        int totalTransactions = controller.getTotalTransactionsToday();

        // Card: Penjualan Hari Ini
        VBox todayCard = createCard("💰", "Penjualan Hari Ini", 
            currencyFormat.format(todaySales), 
            totalTransactions + " transaksi hari ini",
            "#4CAF50", "#E8F5E9");

        // Card: Penjualan Minggu Ini
        VBox weekCard = createCard("📅", "Minggu Ini", 
            currencyFormat.format(weekSales), 
            "7 hari terakhir",
            "#2196F3", "#E3F2FD");

        // Card: Penjualan Bulan Ini
        VBox monthCard = createCard("📆", "Bulan Ini", 
            currencyFormat.format(monthSales), 
            LocalDate.now().getMonth().toString(),
            "#FF9800", "#FFF3E0");

        // Card: Total Produk
        int totalProducts = controller.getProductList().size();
        int lowStockCount = (int) controller.getProductList().stream()
            .filter(p -> p.getStock() < 10).count();
        String stockStatus = lowStockCount > 0 ? "⚠️ " + lowStockCount + " stok menipis" : "✅ Stok aman";
        VBox productCard = createCard("📦", "Total Produk", 
            String.valueOf(totalProducts) + " item", 
            stockStatus,
            "#9C27B0", "#F3E5F5");

        cards.getChildren().addAll(todayCard, weekCard, monthCard, productCard);
        HBox.setHgrow(todayCard, Priority.ALWAYS);
        HBox.setHgrow(weekCard, Priority.ALWAYS);
        HBox.setHgrow(monthCard, Priority.ALWAYS);
        HBox.setHgrow(productCard, Priority.ALWAYS);

        return cards;
    }

    private VBox createCard(String icon, String title, String value, String subtitle, String accentColor, String bgColor) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 15; " +
                     "-fx-border-color: " + accentColor + "; -fx-border-width: 0 0 0 4; -fx-border-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        card.setMinWidth(220);
        card.setMaxWidth(280);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 32px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");

        card.getChildren().addAll(iconLabel, titleLabel, valueLabel, subtitleLabel);
        return card;
    }

    private HBox createChartsSection() {
        HBox section = new HBox(20);

        // Line Chart: Penjualan 7 Hari Terakhir
        VBox lineChartBox = createSalesLineChart();

        // Pie Chart: Penjualan per Kategori
        VBox pieChartBox = createCategoryPieChart();

        section.getChildren().addAll(lineChartBox, pieChartBox);
        HBox.setHgrow(lineChartBox, Priority.ALWAYS);
        HBox.setHgrow(pieChartBox, Priority.ALWAYS);

        return section;
    }

    private VBox createSalesLineChart() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("📈");
        icon.setStyle("-fx-font-size: 24px;");
        Label title = new Label("Grafik Penjualan 7 Hari Terakhir");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        header.getChildren().addAll(icon, title);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tanggal");
        xAxis.setTickLabelFill(Color.valueOf("#666"));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Penjualan (Rp)");
        yAxis.setTickLabelFill(Color.valueOf("#666"));

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(null);
        lineChart.setLegendVisible(false);
        lineChart.setPrefHeight(280);
        lineChart.setCreateSymbols(true);
        lineChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // Get last 7 days sales data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            double sales = controller.getSalesByDate(date);
            series.getData().add(new XYChart.Data<>(date.format(formatter), sales));
        }

        lineChart.getData().add(series);

        box.getChildren().addAll(header, lineChart);
        return box;
    }

    private VBox createCategoryPieChart() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("🥧");
        icon.setStyle("-fx-font-size: 24px;");
        Label title = new Label("Distribusi Stok per Kategori");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        header.getChildren().addAll(icon, title);

        PieChart pieChart = new PieChart();
        pieChart.setPrefHeight(280);
        pieChart.setLegendSide(javafx.geometry.Side.RIGHT);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(90);

        // Group products by category
        controller.getProductList().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Product::getCategory,
                java.util.stream.Collectors.summingInt(Product::getStock)
            ))
            .forEach((category, stock) -> {
                pieChart.getData().add(new PieChart.Data(category + " (" + stock + ")", stock));
            });

        box.getChildren().addAll(header, pieChart);
        return box;
    }

    private VBox createLowStockAlert() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("⚠️");
        icon.setStyle("-fx-font-size: 24px;");
        VBox titleBox = new VBox(2);
        Label title = new Label("Peringatan Stok Menipis");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f44336;");
        Label subtitle = new Label("Produk dengan stok kurang dari 10 unit perlu segera diisi ulang");
        subtitle.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(icon, titleBox);

        // Filter low stock products
        List<Product> lowStockProducts = controller.getProductList().stream()
            .filter(p -> p.getStock() < 10)
            .sorted((a, b) -> Integer.compare(a.getStock(), b.getStock()))
            .toList();

        if (lowStockProducts.isEmpty()) {
            HBox successBox = new HBox(10);
            successBox.setAlignment(Pos.CENTER);
            successBox.setPadding(new Insets(20));
            successBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");
            Label successIcon = new Label("✅");
            successIcon.setStyle("-fx-font-size: 24px;");
            Label noAlert = new Label("Semua produk memiliki stok yang cukup!");
            noAlert.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            successBox.getChildren().addAll(successIcon, noAlert);
            box.getChildren().addAll(header, successBox);
        } else {
            // Table for low stock products
            TableView<Product> lowStockTable = new TableView<>();
            lowStockTable.setPrefHeight(180);
            lowStockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            lowStockTable.setStyle("-fx-background-radius: 10;");

            TableColumn<Product, String> codeCol = new TableColumn<>("Kode");
            codeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
            codeCol.setPrefWidth(80);

            TableColumn<Product, String> nameCol = new TableColumn<>("Nama Produk");
            nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));

            TableColumn<Product, String> categoryCol = new TableColumn<>("Kategori");
            categoryCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));

            TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
            stockCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stock"));
            stockCol.setPrefWidth(70);
            stockCol.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label badge = new Label(item.toString() + " unit");
                        if (item < 5) {
                            badge.setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #f44336; " +
                                          "-fx-padding: 3 8; -fx-background-radius: 10; -fx-font-weight: bold;");
                        } else {
                            badge.setStyle("-fx-background-color: #FFF3E0; -fx-text-fill: #ff9800; " +
                                          "-fx-padding: 3 8; -fx-background-radius: 10; -fx-font-weight: bold;");
                        }
                        setGraphic(badge);
                        setText(null);
                    }
                }
            });

            TableColumn<Product, Void> actionCol = new TableColumn<>("Aksi");
            actionCol.setPrefWidth(100);
            actionCol.setCellFactory(col -> new TableCell<>() {
                private final Button restockBtn = new Button("📦 Restock");
                {
                    restockBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                       "-fx-font-size: 10px; -fx-padding: 5 10; -fx-background-radius: 5;");
                    restockBtn.setOnAction(e -> {
                        Product product = getTableView().getItems().get(getIndex());
                        showInfo("Silakan ke tab Manajemen Produk untuk mengisi stok " + product.getName());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : restockBtn);
                }
            });

            lowStockTable.getColumns().addAll(codeCol, nameCol, categoryCol, stockCol, actionCol);
            lowStockTable.getItems().addAll(lowStockProducts);

            box.getChildren().addAll(header, lowStockTable);
        }

        return box;
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
