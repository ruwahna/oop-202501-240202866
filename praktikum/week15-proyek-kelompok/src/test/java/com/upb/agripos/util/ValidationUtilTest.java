package com.upb.agripos.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    @DisplayName("Harus mendeteksi String kosong atau null")
    void testIsEmpty() {
        assertTrue(ValidationUtil.isEmpty(""), "String kosong harus dianggap empty");
        assertTrue(ValidationUtil.isEmpty(null), "Null harus dianggap empty");
        assertTrue(ValidationUtil.isEmpty("   "), "Spasi saja harus dianggap empty");
        assertFalse(ValidationUtil.isEmpty("AgriPOS"), "String berisi tidak boleh dianggap empty");
    }

    @Test
    @DisplayName("Harus memvalidasi format angka dengan benar")
    void testIsNumber() {
        assertTrue(ValidationUtil.isNumber("15000"), "Angka bulat harus valid");
        assertTrue(ValidationUtil.isNumber("12.5"), "Angka desimal harus valid");
        assertFalse(ValidationUtil.isNumber("12.000,00"), "Format dengan titik ribuan (kultur lokal) harus difilter jika bukan angka murni");
        assertFalse(ValidationUtil.isNumber("abc"), "Karakter alfabet harus tidak valid");
    }

    @Test
    @DisplayName("Harus memvalidasi angka positif")
    void testIsPositive() {
        assertTrue(ValidationUtil.isPositive(100.0), "Angka 100 harus dianggap positif");
        assertTrue(ValidationUtil.isPositive(0.0), "Angka 0 harus dianggap positif");
        assertFalse(ValidationUtil.isPositive(-1.0), "Angka negatif tidak boleh dianggap positif");
    }
}