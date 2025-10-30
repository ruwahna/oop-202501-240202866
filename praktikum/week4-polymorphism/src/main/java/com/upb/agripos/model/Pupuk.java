
package com.upb.agripos.model;

public class Pupuk extends  Product{

    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", jenis: " + jenis;
    }
}
