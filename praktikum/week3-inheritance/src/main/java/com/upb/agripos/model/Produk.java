package com.upb.agripos.model;
// produk.java 

public class Produk {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public void tambahStok(int jumlah) {
        // menambahkan stok produk untuk mengelola stok produk
        if (jumlah > 0) {
            this.stok += jumlah;
            System.out.println(jumlah + " stok berhasil ditambahkan. Total stok sekarang: " + stok);
        } else {
            System.out.println("Jumlah stok sudah lebih dari cukup!");
        }
    }

    public void kurangiStok(int jumlah) {
        // mengurangi stok produk mengelola stok produk
         if (jumlah > 0 && jumlah <= stok) {
            stok -= jumlah;
            System.out.println(jumlah + " stok berhasil dikurangi. Total stok sekarang: " + stok);
        } else {
           System.out.println("Jumlah stok yang dikurangi tidak valid atau melebihi stok yang tersedia.");
        }
    }


}