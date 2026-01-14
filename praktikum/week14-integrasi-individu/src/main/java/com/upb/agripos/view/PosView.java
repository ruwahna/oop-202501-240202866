package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.ListChangeListener;
import javafx.beans.binding.Bindings;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * JavaFX View untuk aplikasi POS (Bab 12-13)
 */
public class PosView {
    private final PosController controller;
    private TableView<Product> productTable;
    private ListView<CartItem> cartListView;
    private Label totalLabel;
    private Label lastCheckoutLabel;
    private TextField codeField, nameField, priceField, stockField, qtyField;

    // Currency formatter for ID locale (used widely for consistent formatting)
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id","ID"));

    public PosView(PosController controller) {
        this.controller = controller;
        // Ensure two decimal places for currency display (e.g., Rp 10.000,00)
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);
    }

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Form untuk tambah produk
        root.setTop(createProductForm());

        // Center: Tabel produk
        root.setCenter(createProductTable());

        // Right: Keranjang
        root.setRight(createCartPanel());

        Scene scene = new Scene(root, 1000, 600);
        return scene;
    }

    private VBox createProductForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label titleLabel = new Label("Tambah Produk Baru");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        codeField = new TextField();
        nameField = new TextField();
        priceField = new TextField();
        stockField = new TextField();

        grid.add(new Label("Kode:"), 0, 0);
        grid.add(codeField, 1, 0);
        grid.add(new Label("Nama:"), 2, 0);
        grid.add(nameField, 3, 0);
        grid.add(new Label("Harga:"), 4, 0);
        grid.add(priceField, 5, 0);
        grid.add(new Label("Stok:"), 6, 0);
        grid.add(stockField, 7, 0);

        Button addButton = new Button("Tambah Produk");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(e -> handleAddProduct());

        form.getChildren().addAll(titleLabel, grid, addButton);
        return form;
    }

    private VBox createProductTable() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label title = new Label("Daftar Produk");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        productTable = new TableView<>();
        productTable.setItems(controller.getProductList());

        TableColumn<Product, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Nama");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Use consistent currency formatting
        priceCol.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(formatCurrency(value));
                }
            }
        });

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productTable.getColumns().addAll(codeCol, nameCol, priceCol, stockCol);

        HBox buttonBox = new HBox(10);
        Button deleteButton = new Button("Hapus Produk");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> handleDeleteProduct());
        // Disable delete when no selection
        deleteButton.disableProperty().bind(productTable.getSelectionModel().selectedItemProperty().isNull());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> controller.loadProducts());

        buttonBox.getChildren().addAll(deleteButton, refreshButton);

        box.getChildren().addAll(title, productTable, buttonBox);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        return box;
    }

    private VBox createCartPanel() {
        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(10));
        cartBox.setPrefWidth(300);
        cartBox.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffc107;");

        Label cartTitle = new Label("Keranjang Belanja");
        cartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        cartListView = new ListView<>();
        cartListView.setPrefHeight(300);
        // Bind the ListView to the controller's observable cart items
        cartListView.setItems(controller.getCartItems());
        // Custom cell to show helpful text and context menu to remove items
        cartListView.setCellFactory(lv -> {
            ListCell<CartItem> cell = new ListCell<>() {
                @Override
                protected void updateItem(CartItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%s (%s) x%d — %s",
                            item.getProduct().getCode(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            formatCurrency(item.getSubtotal())));
                    }
                }
            };
            MenuItem remove = new MenuItem("Hapus");
            remove.setOnAction(ev -> {
                CartItem item = cell.getItem();
                if (item != null) {
                    try {
                        controller.removeFromCart(item.getProduct().getCode());
                        updateCartDisplay();
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus dari keranjang: " + ex.getMessage());
                    }
                }
            });
            ContextMenu menu = new ContextMenu(remove);
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> cell.setContextMenu(isNowEmpty ? null : menu));
            return cell;
        });
        // Update total when cart changes
        controller.getCartItems().addListener((ListChangeListener<CartItem>) c -> updateCartDisplay());

        HBox qtyBox = new HBox(10);
        qtyField = new TextField("1");
        qtyField.setPrefWidth(60);
        Button addToCartButton = new Button("Tambah ke Keranjang");
        addToCartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        addToCartButton.setOnAction(e -> handleAddToCart());
        // Disable addToCart when no product selected
        addToCartButton.disableProperty().bind(productTable.getSelectionModel().selectedItemProperty().isNull());
        qtyField.disableProperty().bind(productTable.getSelectionModel().selectedItemProperty().isNull());

        qtyBox.getChildren().addAll(new Label("Qty:"), qtyField, addToCartButton);

        totalLabel = new Label("Total: Rp 0.00");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        lastCheckoutLabel = new Label("Terakhir Checkout: -");
        lastCheckoutLabel.setStyle("-fx-font-style: italic;");

        Button clearCartButton = new Button("Kosongkan Keranjang");
        clearCartButton.setOnAction(e -> {
            controller.clearCart();
            updateCartDisplay();
        });

        // Checkout summary area
        VBox checkoutBox = new VBox(6);
        checkoutBox.setPadding(new Insets(6));
        checkoutBox.setStyle("-fx-background-color: #e2f7e2; -fx-border-color: #4caf50;");

        Label checkoutTitle = new Label("Ringkasan Checkout");
        checkoutTitle.setStyle("-fx-font-weight: bold;");

        Label subtotalLabel = new Label("Subtotal: Rp 0.00");
        Label taxLabel = new Label("Pajak (10%): Rp 0.00");
        Label grandTotalLabel = new Label("Total Bayar: Rp 0.00");
        Label summaryItemsLabel = new Label("Item berbeda: 0 | Jumlah total: 0");

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #00796B; -fx-text-fill: white;");
        checkoutButton.setOnAction(e -> handleCheckout(subtotalLabel, taxLabel, grandTotalLabel, summaryItemsLabel));
        // Disable checkout when cart is empty
        checkoutButton.disableProperty().bind(Bindings.isEmpty(controller.getCartItems()));

        Button previewButton = new Button("Preview Struk");
        previewButton.setStyle("-fx-background-color: #795548; -fx-text-fill: white;");
        previewButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Preview Struk");
            dialog.setHeaderText("Cetak struk (preview) ke terminal");
            dialog.setContentText("Nama Kasir:");
            dialog.getEditor().setText("Indah Ruwahna Anugraheni (240202866)");
            dialog.showAndWait().ifPresent(name -> {
                controller.printPreviewReceipt(name);
                showAlert(Alert.AlertType.INFORMATION, "Preview", "Struk preview dicetak ke terminal (console).");
            });
        });
        previewButton.disableProperty().bind(Bindings.isEmpty(controller.getCartItems()));

        // show last checkout label if present
        // (will be updated by handleCheckout)

        checkoutBox.getChildren().add(lastCheckoutLabel);

        checkoutBox.getChildren().addAll(checkoutTitle, subtotalLabel, taxLabel, grandTotalLabel, summaryItemsLabel, previewButton, checkoutButton);

        cartBox.getChildren().addAll(cartTitle, qtyBox, cartListView, totalLabel, clearCartButton, checkoutBox);
        VBox.setVgrow(cartListView, Priority.ALWAYS);
        return cartBox;
    }

    private void handleAddProduct() {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            double price = parseCurrencyToDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            controller.addProduct(code, name, price, stock);
            clearProductForm();
            productTable.getSelectionModel().clearSelection();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk berhasil ditambahkan!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Harga dan Stok harus berupa angka!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambah produk: " + e.getMessage());
        }
    }

    private void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih produk yang akan dihapus!");
            return;
        }

        try {
            controller.deleteProduct(selected.getCode());
            productTable.getSelectionModel().clearSelection();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk berhasil dihapus!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus produk: " + e.getMessage());
        }
    }

    private void handleAddToCart() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih produk untuk ditambahkan ke keranjang!");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyField.getText());
            controller.addToCart(selected, quantity);
            updateCartDisplay();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk ditambahkan ke keranjang!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Jumlah harus berupa angka!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void updateCartDisplay() {
        // ListView is bound to controller's observable cart items; just update the total label
        totalLabel.setText("Total: " + formatCurrency(controller.getCartTotal()));
    }

    private void handleCheckout(Label subtotalLabel, Label taxLabel, Label grandTotalLabel, Label summaryItemsLabel) {
        try {
            com.upb.agripos.model.CheckoutSummary summary = controller.checkout();
            subtotalLabel.setText("Subtotal: " + formatCurrency(summary.getSubtotal()));
            taxLabel.setText("Pajak (10%): " + formatCurrency(summary.getTax()));
            grandTotalLabel.setText("Total Bayar: " + formatCurrency(summary.getTotal()));
            summaryItemsLabel.setText(String.format("Item berbeda: %d | Jumlah total: %d",
                    summary.getDistinctItemCount(), summary.getTotalQuantity()));
            updateCartDisplay();
            lastCheckoutLabel.setText("Terakhir Checkout: " + formatCurrency(summary.getTotal()));
            showAlert(Alert.AlertType.INFORMATION, "Checkout", "Checkout berhasil. Total bayar: " + formatCurrency(summary.getTotal()));

            // Prompt for cashier name and print receipt to terminal
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Cetak Struk");
            dialog.setHeaderText("Cetak struk pada terminal");
            dialog.setContentText("Nama Kasir:");
            dialog.getEditor().setText("Indah Ruwahna Anugraheni (240202866)");
            dialog.showAndWait().ifPresent(name -> {
                controller.printReceipt(name);
                showAlert(Alert.AlertType.INFORMATION, "Struk", "Struk dicetak ke terminal (console).");
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal checkout: " + e.getMessage());
        }
    }

    private void clearProductForm() {
        codeField.clear();
        nameField.clear();
        priceField.clear();
        stockField.clear();
    }

    /**
     * Parse a user-entered price string into a double using Indonesian locale as primary.
     * Accepts formats like "Rp 10.000,00", "10000", "10,000.00" and falls back to a simple numeric parse.
     */
    private double parseCurrencyToDouble(String text) {
        if (text == null || text.isBlank()) throw new NumberFormatException("Harga kosong");
        text = text.replace("Rp", "").trim();
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id","ID"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        try {
            Number n = nf.parse(text);
            return n.doubleValue();
        } catch (ParseException e) {
            // fallback: remove non-numeric characters and parse as double
            String cleaned = text.replaceAll("[^0-9.-]", "");
            try {
                return Double.parseDouble(cleaned);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Tidak bisa parse harga: " + text);
            }
        }
    }

    private String formatCurrency(double value) {
        return currencyFormat.format(value);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}