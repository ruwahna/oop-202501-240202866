package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.TransactionItem;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.service.TransactionService;
import com.upb.agripos.exception.OutOfStockException;
import com.upb.agripos.util.CurrencyFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller for managing transactions and shopping cart.
 */
public class TransactionController {
    @FXML private TextField txtProductCode;
    @FXML private Spinner<Integer> spinQty;
    @FXML private TableView<Product> tableProduk;  // Table untuk daftar produk
    @FXML private TableColumn<Product, String> colProdKode;
    @FXML private TableColumn<Product, String> colProdNama;
    @FXML private TableColumn<Product, Double> colProdHarga;
    @FXML private TableColumn<Product, Integer> colProdStok;
    @FXML private TableView<TransactionItem> tableCart;
    @FXML private TableColumn<TransactionItem, String> colProductName;
    @FXML private TableColumn<TransactionItem, Integer> colQty;
    @FXML private TableColumn<TransactionItem, Double> colSubtotal;
    @FXML private Label lblTotalHarga;
    @FXML private ComboBox<String> comboPaymentMethod;
    @FXML private Button btnCheckout;

    private final CartService cartService = new CartService();
    private final TransactionService transactionService = new TransactionService();
    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        // Setup produk table columns
        colProdKode.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKode()));
        colProdNama.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama()));
        colProdHarga.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getHarga()).asObject());
        colProdHarga.setCellFactory(column -> new TableCell<Product, Double>() {
            private final java.text.NumberFormat formatter = 
                java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
            
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
        colProdStok.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStok()).asObject());
        
        // Initialize table columns for shopping cart with proper property bindings
        colProductName.setCellValueFactory(data -> {
            String productName = data.getValue().getProduct().getNama();
            return javafx.beans.binding.Bindings.createObjectBinding(() -> productName);
        });
        colQty.setCellValueFactory(data -> {
            int qty = data.getValue().getQty();
            return javafx.beans.binding.Bindings.createObjectBinding(() -> qty);
        });
        colSubtotal.setCellValueFactory(data -> {
            double subtotal = data.getValue().getSubtotal();
            return javafx.beans.binding.Bindings.createObjectBinding(() -> subtotal);
        });
        colSubtotal.setCellFactory(column -> new TableCell<TransactionItem, Double>() {
            private final java.text.NumberFormat formatter = 
                java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
            
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
        
        // Setup Spinner qty dengan value factory (1-100, default 1)
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spinQty.setValueFactory(valueFactory);
        spinQty.setEditable(true);
        spinQty.setPrefWidth(100);
        
        // Allow manual text input and validation
        spinQty.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (!newVal.isEmpty()) {
                    int val = Integer.parseInt(newVal);
                    if (val >= 1 && val <= 100) {
                        spinQty.getValueFactory().setValue(val);
                    }
                }
            } catch (NumberFormatException e) {
                spinQty.getEditor().setText(oldVal);
            }
        });
        
        // Setup payment method combo box
        comboPaymentMethod.setItems(FXCollections.observableArrayList(
            "💵 Tunai (Cash)",
            "💳 Transfer Bank",
            "📱 E-Wallet",
            "🔳 QRIS"
        ));
        comboPaymentMethod.setPrefWidth(150);
        comboPaymentMethod.setValue("💵 Tunai (Cash)");  // Default
        
        // Load produk list
        loadProdukList();
        updateUI();
    }
    
    /**
     * Load daftar produk dari database
     */
    private void loadProdukList() {
        try {
            java.util.List<Product> produk = productService.getAllProducts();
            javafx.collections.ObservableList<Product> produkList = FXCollections.observableArrayList(produk);
            tableProduk.setItems(produkList);
            System.out.println("✓ Loaded " + produk.size() + " products");
        } catch (Exception e) {
            System.err.println("Error loading produk: " + e.getMessage());
        }
    }

    /**
     * Handles adding product to shopping cart.
     */
    @FXML
    private void handleAddToOrder() {
        String code = txtProductCode.getText().trim();
        int qty = spinQty.getValue();

        if (code.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Masukkan kode produk!");
            return;
        }

        try {
            Product product = productService.findByCode(code);
            if (product == null) {
                showAlert(Alert.AlertType.WARNING, "Error", "Produk tidak ditemukan!");
                return;
            }
            
            cartService.addItem(product, qty);
            updateUI();
            txtProductCode.clear();
            spinQty.getValueFactory().setValue(1);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Produk ditambahkan ke keranjang!");
        } catch (OutOfStockException e) {
            showAlert(Alert.AlertType.ERROR, "Stok Habis", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambah produk: " + e.getMessage());
        }
    }

    /**
     * Handles checkout and payment processing.
     */
    @FXML
    private void handleCheckout() {
        if (cartService.getCartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Keranjang Kosong", "Tambahkan produk terlebih dahulu.");
            return;
        }

        // Validate payment method selection
        if (comboPaymentMethod.getValue() == null || comboPaymentMethod.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Pilih Metode Pembayaran", 
                "Silakan pilih metode pembayaran terlebih dahulu!");
            return;
        }

        double total = cartService.calculateTotal();
        String selectedMethod = comboPaymentMethod.getValue();
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf((long)total));
        dialog.setTitle("Pembayaran");
        dialog.setHeaderText("Informasi Pembayaran - " + selectedMethod);
        dialog.setContentText("Total Harga: " + CurrencyFormatter.toRupiah(total) + 
                             "\nMetode: " + selectedMethod +
                             "\nMasukkan nominal uang (angka saja, contoh: 100000):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            processPayment(result.get(), total, selectedMethod);
        }
    }

    /**
     * Processes payment and completes the transaction.
     */
    private void processPayment(String amountStr, double total, String paymentMethod) {
        try {
            // Clean input: remove Rp, spaces, and commas
            String cleaned = amountStr.trim()
                .replaceAll("[Rp\\s.]", "")
                .replace(",", ".");
            
            if (cleaned.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Format Error", 
                    "Nominal uang tidak boleh kosong!\nContoh: 100000");
                return;
            }
            
            double payment = Double.parseDouble(cleaned);
            
            if (payment <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", 
                    "Nominal uang harus lebih dari 0");
                return;
            }
            
            if (payment < total) {
                double shortage = total - payment;
                showAlert(Alert.AlertType.ERROR, "Uang Kurang", 
                    "Uang tidak cukup! Kurang Rp " + CurrencyFormatter.toRupiah(shortage));
                return;
            }
            
            Transaction trans = new Transaction();
            trans.setTanggal(LocalDateTime.now());
            trans.setItems(new ArrayList<>(cartService.getCartItems()));
            trans.setTotal(total);
            trans.setPembayaran(payment);
            trans.setKembalian(payment - total);
            trans.setStatus("SELESAI");
            // Store payment method info - remove emoji for clean storage
            trans.setMetodePembayaran(paymentMethod.replaceAll("[💵💳📱🔳]", "").trim());
            
            transactionService.checkout(trans);
            
            double change = payment - total;
            showAlert(Alert.AlertType.INFORMATION, "Transaksi Berhasil!", 
                "Metode: " + paymentMethod + "\n" +
                "Total: " + CurrencyFormatter.toRupiah(total) + "\n" +
                "Pembayaran: " + CurrencyFormatter.toRupiah(payment) + "\n" +
                "Kembalian: " + CurrencyFormatter.toRupiah(change));
            
            cartService.clearCart();
            comboPaymentMethod.setValue("💵 Tunai (Cash)");  // Reset to default
            updateUI();
            
            // Log transaksi untuk debug
            System.out.println("✓ Transaksi tersimpan ke DB dengan metode: " + trans.getMetodePembayaran());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format Error", 
                "Input tidak valid!\nGunakan hanya angka.\nContoh: 100000");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                "Gagal menyimpan transaksi: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Pembayaran gagal: " + e.getMessage());
        }
    }

    /**
     * Updates the UI with current cart data.
     */
    private void updateUI() {
        if (tableCart != null) {
            tableCart.setItems(FXCollections.observableArrayList(cartService.getCartItems()));
        }
        double total = cartService.calculateTotal();
        lblTotalHarga.setText(CurrencyFormatter.toRupiah(total));
    }

    /**
     * Handles removing an item from the cart.
     */
    @FXML
    private void handleRemoveItem() {
        TransactionItem selected = tableCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih item yang ingin dihapus!");
            return;
        }
        
        cartService.removeItem(selected);
        updateUI();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Item berhasil dihapus dari keranjang!");
    }

    /**
     * Shows an alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}