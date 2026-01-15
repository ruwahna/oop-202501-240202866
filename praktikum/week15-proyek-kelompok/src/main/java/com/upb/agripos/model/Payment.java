package com.upb.agripos.model;

public class Payment {
    private int transactionId;
    private String metode; // "CASH" atau "E-WALLET"
    private double jumlahBayar;
    private double kembalian;

    // Getter dan Setter
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public String getMetode() { return metode; }
    public void setMetode(String metode) { this.metode = metode; }
    public double getJumlahBayar() { return jumlahBayar; }
    public void setJumlahBayar(double jumlahBayar) { this.jumlahBayar = jumlahBayar; }
    public double getKembalian() { return kembalian; }
    public void setKembalian(double kembalian) { this.kembalian = kembalian; }
}