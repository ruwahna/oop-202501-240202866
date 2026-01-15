package com.upb.agripos.model;

import java.util.List;

public class Receipt {
    private String noTransaksi;
    private List<TransactionItem> items;
    private double total;
    private double bayar;
    private double kembali;

    // Konstruktor dan Getter (biasanya diisi setelah transaksi sukses)
}