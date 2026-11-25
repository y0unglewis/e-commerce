package com.example.gearhub.util;

import com.example.gearhub.R;
import com.example.gearhub.model.Product;

/** Minh họa riêng cho catalog mẫu; sản phẩm khác dùng ảnh theo danh mục. */
public final class HomeProductImages {
    private HomeProductImages() { }

    public static int forProduct(Product product) {
        switch (product.getId()) {
            case 1: return R.drawable.product_soundcore_q30;
            case 2: return R.drawable.product_keychron_k8;
            case 3: return R.drawable.product_logitech_m650;
            case 4: return R.drawable.product_anker_nano_65w;
            case 5: return R.drawable.product_sony_ch520;
            case 6: return R.drawable.product_logitech_g413;
            case 7: return R.drawable.product_logitech_mx_anywhere;
            case 8: return R.drawable.product_ugreen_gan_30w;
            case 9: return R.drawable.product_liberty_4;
            case 10: return R.drawable.product_logitech_k380;
            case 11: return R.drawable.product_logitech_g304;
            case 12: return R.drawable.product_baseus_powerbank;
            default: return ProductImages.forKey(product.getImageKey());
        }
    }
}
