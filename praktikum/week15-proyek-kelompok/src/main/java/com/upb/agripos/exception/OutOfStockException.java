package com.upb.agripos.exception;

public class OutOfStockException extends Exception {
    private String productName;
    private int availableStock;
    private int requestedStock;

    /**
     * Constructor dengan nama produk dan stok yang tersedia
     */
    public OutOfStockException(String productName, int availableStock) {
        super("Stok produk '" + productName + "' tidak mencukupi. Tersedia: " + availableStock);
        this.productName = productName;
        this.availableStock = availableStock;
        this.requestedStock = -1;
    }

    /**
     * Constructor dengan nama produk, stok tersedia, dan stok yang diminta
     */
    public OutOfStockException(String productName, int availableStock, int requestedStock) {
        super("Stok produk '" + productName + "' tidak mencukupi. Diminta: " + requestedStock + 
              ", Tersedia: " + availableStock);
        this.productName = productName;
        this.availableStock = availableStock;
        this.requestedStock = requestedStock;
    }

    /**
     * Constructor dengan custom message
     */
    public OutOfStockException(String message) {
        super(message);
        this.productName = "Unknown";
        this.availableStock = 0;
        this.requestedStock = -1;
    }

    /**
     * Constructor dengan message dan cause
     */
    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
        this.productName = "Unknown";
        this.availableStock = 0;
        this.requestedStock = -1;
    }

    // Getter methods
    public String getProductName() {
        return productName;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public int getRequestedStock() {
        return requestedStock;
    }

    /**
     * Method untuk mendapatkan informasi detail tentang shortage
     */
    public int getShortage() {
        if (requestedStock > 0) {
            return requestedStock - availableStock;
        }
        return -1;
    }

    /**
     * Method untuk cek apakah ini adalah kasus shortage
     */
    public boolean isShortage() {
        return requestedStock > availableStock && requestedStock > 0;
    }
}
