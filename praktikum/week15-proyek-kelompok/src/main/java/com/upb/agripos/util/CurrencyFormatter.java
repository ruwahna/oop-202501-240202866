package com.upb.agripos.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String toRupiah(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }
}