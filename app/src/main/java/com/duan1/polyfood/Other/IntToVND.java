package com.duan1.polyfood.Other;

import java.text.NumberFormat;
import java.util.Locale;

public class IntToVND {

    public String convertToVND(int amount) {

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }
}