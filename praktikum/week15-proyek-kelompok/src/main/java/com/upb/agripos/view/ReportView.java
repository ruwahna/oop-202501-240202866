package com.upb.agripos.view;

import com.upb.agripos.controller.PosController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * View untuk Report/Laporan (FR-4)
 */
public class ReportView {
    private final PosController controller;

    private ComboBox<String> reportTypeCombo;
    private DatePicker startDatePicker, endDatePicker;
    private TextArea reportArea;

    public ReportView(PosController controller) {
        this.controller = controller;
    }

    public VBox createContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Header
        content.getChildren().add(createHeader());

        // Controls
        content.getChildren().add(createControls());

        // Report display
        content.getChildren().add(createReportDisplay());

        return content;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #9C27B0; -fx-padding: 15;");

        Label title = new Label("📊 Laporan Penjualan");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        header.getChildren().add(title);
        return header;
    }

    private HBox createControls() {
        HBox controls = new HBox(20);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #F3E5F5; -fx-border-color: #9C27B0; -fx-border-radius: 5;");

        // Report type
        VBox typeBox = new VBox(5);
        Label typeLabel = new Label("Jenis Laporan:");
        reportTypeCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Laporan Harian",
            "Laporan Periode"
        ));
        reportTypeCombo.getSelectionModel().selectFirst();
        reportTypeCombo.setOnAction(e -> updateDatePickers());
        typeBox.getChildren().addAll(typeLabel, reportTypeCombo);

        // Start date
        VBox startBox = new VBox(5);
        Label startLabel = new Label("Tanggal Mulai:");
        startDatePicker = new DatePicker(LocalDate.now());
        startDatePicker.setPrefWidth(150);
        startBox.getChildren().addAll(startLabel, startDatePicker);

        // End date
        VBox endBox = new VBox(5);
        Label endLabel = new Label("Tanggal Akhir:");
        endDatePicker = new DatePicker(LocalDate.now());
        endDatePicker.setPrefWidth(150);
        endDatePicker.setDisable(true);
        endBox.getChildren().addAll(endLabel, endDatePicker);

        // Generate button
        Button generateBtn = new Button("📈 Generate Laporan");
        generateBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-weight: bold;");
        generateBtn.setOnAction(e -> generateReport());

        // Export button
        Button exportBtn = new Button("📤 Export");
        exportBtn.setOnAction(e -> exportReport());

        // Clear button
        Button clearBtn = new Button("🗑️ Clear");
        clearBtn.setOnAction(e -> reportArea.clear());

        controls.getChildren().addAll(typeBox, startBox, endBox, generateBtn, exportBtn, clearBtn);
        return controls;
    }

    private VBox createReportDisplay() {
        VBox displayBox = new VBox(10);
        displayBox.setPadding(new Insets(15));
        displayBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");

        Label label = new Label("📄 Hasil Laporan:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(500);
        reportArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 12px;");
        reportArea.setPromptText("Pilih jenis laporan dan klik 'Generate Laporan' untuk melihat hasil...");

        VBox.setVgrow(reportArea, Priority.ALWAYS);
        displayBox.getChildren().addAll(label, reportArea);
        return displayBox;
    }

    private void updateDatePickers() {
        boolean isPeriod = "Laporan Periode".equals(reportTypeCombo.getValue());
        endDatePicker.setDisable(!isPeriod);
        if (!isPeriod) {
            endDatePicker.setValue(startDatePicker.getValue());
        }
    }

    private void generateReport() {
        try {
            String reportType = reportTypeCombo.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null) {
                showError("Pilih tanggal mulai!");
                return;
            }

            String report;
            if ("Laporan Harian".equals(reportType)) {
                report = controller.generateDailyReport(startDate);
            } else {
                if (endDate == null) {
                    showError("Pilih tanggal akhir!");
                    return;
                }
                if (endDate.isBefore(startDate)) {
                    showError("Tanggal akhir tidak boleh sebelum tanggal mulai!");
                    return;
                }
                report = controller.generatePeriodReport(startDate, endDate);
            }

            reportArea.setText(report);

        } catch (Exception e) {
            showError("Gagal generate laporan: " + e.getMessage());
        }
    }

    private void exportReport() {
        if (reportArea.getText().isEmpty()) {
            showError("Tidak ada laporan untuk di-export!");
            return;
        }

        // For now, just copy to clipboard
        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(reportArea.getText());
        clipboard.setContent(content);

        showInfo("Laporan berhasil di-copy ke clipboard!");
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
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
