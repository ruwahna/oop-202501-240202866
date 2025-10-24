package com.upb.agripos;

import com.upb.agripos.model.*;
import com.upb.agripos.util.*;

public class MainInheritance {
    public static void main(String[] args) {
        Benih b = new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64");
        Pupuk p = new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea");
        AlatPertanian a = new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja");

        System.out.println("------------Deskripsi Produk-------------");
        b.deskripsiBenih();
        p.deskripsiPupuk();
        a.deskripsiAlat();
        System.out.println("-----------------------------------");
        CreditBy.print("240202866", "Indah Ruwahna Anugraheni");
    }
}