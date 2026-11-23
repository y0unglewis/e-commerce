package com.example.gearhub.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class PriceFormatter {
    private PriceFormatter() { }

    public static String format(long value) {
        // Chuyển 1590000 thành "1.590.000 đ" theo định dạng Việt Nam.
        return NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(value) + " đ";
    }
}
