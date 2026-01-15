package com.upb.agripos.controller;

import com.upb.agripos.model.Transaction;
import com.upb.agripos.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Controller untuk Report/Laporan Penjualan (Admin)
 */
public class ReportController {
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button btnCari;
    @FXML private TableView<Transaction> tableTransactions;
    @FXML private TableColumn<Transaction, Integer> colTransId;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, Double> colTotal;
    @FXML private TableColumn<Transaction, String> colStatus;
    @FXML private Label lblTotalPendapatan;

    private TransactionService transactionService;
    private ObservableList<Transaction> transactionList;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @FXML
    public void initialize() {
        transactionService = new TransactionService();
        setupTableColumns();
        setupEventHandlers();
        loadAllTransactions();
    }

    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        colTransId.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        
        colDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanggal().toString()));
        
        colTotal.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());
        colTotal.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormatter.format(item));
                }
            }
        });
        
        colStatus.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        btnCari.setOnAction(e -> handleSearch());
    }

    /**
     * Load semua transaksi
     */
    private void loadAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            transactionList = FXCollections.observableArrayList(transactions);
            tableTransactions.setItems(transactionList);
            updateTotalPendapatan();
            System.out.println("✓ Loaded " + transactions.size() + " transactions for report");
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            lblTotalPendapatan.setText("Rp 0");
        }
    }

    /**
     * Handle search berdasarkan tanggal
     */
    private void handleSearch() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        if (from == null || to == null) {
            showError("Perhatian", "Pilih tanggal dari dan sampai!");
            return;
        }

        if (from.isAfter(to)) {
            showError("Validasi", "Tanggal 'dari' tidak boleh lebih besar dari tanggal 'sampai'!");
            return;
        }

        try {
            List<Transaction> allTransactions = transactionService.getAllTransactions();
            ObservableList<Transaction> filtered = FXCollections.observableArrayList();

            for (Transaction t : allTransactions) {
                LocalDate transDate = t.getTanggal().toLocalDate();
                if (!transDate.isBefore(from) && !transDate.isAfter(to)) {
                    filtered.add(t);
                }
            }

            tableTransactions.setItems(filtered);
            updateTotalPendapatan(filtered);
            System.out.println("✓ Found " + filtered.size() + " transactions from " + from + " to " + to);
        } catch (Exception e) {
            showError("Error", "Gagal mencari transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update total pendapatan (all transactions)
     */
    private void updateTotalPendapatan() {
        updateTotalPendapatan(transactionList);
    }

    /**
     * Update total pendapatan untuk list tertentu
     */
    private void updateTotalPendapatan(ObservableList<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            lblTotalPendapatan.setText("Rp 0");
            return;
        }

        double total = 0;
        for (Transaction t : transactions) {
            total += t.getTotal();
        }

        lblTotalPendapatan.setText(currencyFormatter.format(total));
    }

    /**
     * Show error dialog
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
