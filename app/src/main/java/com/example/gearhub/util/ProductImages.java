package com.example.gearhub.util;

import com.example.gearhub.R;

/** Ảnh minh họa tạm bằng Vector Drawable, sau này có thể thay bằng ảnh PNG. */
public final class ProductImages {
    private ProductImages() { }

    public static int forKey(String imageKey) {
        if ("headphones".equals(imageKey)) return R.drawable.ic_headphones;
        if ("keyboard".equals(imageKey)) return R.drawable.ic_keyboard;
        if ("mouse".equals(imageKey)) return R.drawable.ic_mouse;
        return R.drawable.ic_charger;
    }
}
