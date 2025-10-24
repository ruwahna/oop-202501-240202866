package com.upb.agripos.model;



public class Pupuk extends Produk {
    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    // deskripsiPupuk
    public void deskripsiPupuk() {
        System.out.println("Pupuk :" + getNama() + " jenis pupuk: " + jenis + " harga: " + getHarga() + " kode: " + getKode() + " stok: " + getStok());
    }
}