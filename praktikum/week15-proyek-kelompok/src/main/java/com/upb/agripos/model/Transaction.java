package com.upb.agripos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int id;
    private LocalDateTime tanggal;
    private double total;
    private double pembayaran;
    private double kembalian;
    private String status;
    private String metodePembayaran;  // Payment method
    private List<TransactionItem> items = new ArrayList<>();

    public Transaction() {
        this.tanggal = LocalDateTime.now();
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public double getPembayaran() { return pembayaran; }
    public void setPembayaran(double pembayaran) { this.pembayaran = pembayaran; }
    
    public double getKembalian() { return kembalian; }
    public void setKembalian(double kembalian) { this.kembalian = kembalian; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    
    public List<TransactionItem> getItems() { return items; }
    public void setItems(List<TransactionItem> items) { this.items = items; }
}