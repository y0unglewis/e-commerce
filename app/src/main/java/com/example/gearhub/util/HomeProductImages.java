package com.example.gearhub.util;

import com.example.gearhub.R;
import com.example.gearhub.model.Product;

public final class HomeProductImages {
    private HomeProductImages() {}
    public static int forProduct(Product product) {
        switch (product.getId()) {
            case 1: return R.drawable.product_soundcore_q30_photo;
            case 2: return R.drawable.product_keychron_k8_photo;
            case 3: return R.drawable.product_logitech_m650_photo;
            case 4: return R.drawable.product_anker_nano_65w_photo;
            case 5: return R.drawable.product_sony_ch520_photo;
            case 6: return R.drawable.product_logitech_g413_photo;
            case 7: return R.drawable.product_logitech_mx_anywhere_photo;
            case 8: return R.drawable.product_ugreen_gan_30w_photo;
            case 9: return R.drawable.product_liberty_4_photo;
            case 10: return R.drawable.product_logitech_k380_photo;
            case 11: return R.drawable.product_logitech_g304_photo;
            case 12: return R.drawable.product_baseus_powerbank_photo;
            case 13: return R.drawable.product_sony_xm5_photo;
            case 14: return R.drawable.product_jbl_tune510bt_photo;
            case 15: return R.drawable.product_soundcore_space_one_photo;
            case 16: return R.drawable.product_keychron_k2_v2_photo;
            case 17: return R.drawable.product_logitech_mx_keys_mini_photo;
            case 18: return R.drawable.product_razer_blackwidow_v3_photo;
            case 19: return R.drawable.product_logitech_g502_photo;
            case 20: return R.drawable.product_razer_deathadder_photo;
            case 21: return R.drawable.product_logitech_m720_photo;
            case 22: return R.drawable.product_anker_511_photo;
            case 23: return R.drawable.product_ugreen_nexode65_photo;
            case 24: return R.drawable.product_anker_523_photo;
            default: return ProductImages.forKey(product.getImageKey());
        }
    }
}
