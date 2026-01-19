package com.upb.agripos.exception;

/**
 * Exception untuk kondisi stok tidak mencukupi
 * Digunakan saat quantity melebihi stok yang tersedia
 */
public class OutOfStockException extends Exception {
    private final String productCode;
    private final int requestedQuantity;
    private final int availableStock;

    public OutOfStockException(String message) {
        super(message);
        this.productCode = null;
        this.requestedQuantity = 0;
        this.availableStock = 0;
    }

    public OutOfStockException(String productCode, int requestedQuantity, int availableStock) {
        super(String.format("Stok produk %s tidak mencukupi. Diminta: %d, Tersedia: %d",
            productCode, requestedQuantity, availableStock));
        this.productCode = productCode;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
