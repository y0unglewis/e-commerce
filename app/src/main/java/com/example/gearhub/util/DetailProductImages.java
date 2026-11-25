package com.example.gearhub.util;

import com.example.gearhub.R;
import com.example.gearhub.model.Product;

/** Ảnh studio của 16 sản phẩm, vị trí trong mảng = id sản phẩm - 1. */
public final class DetailProductImages {
    private static final int[] IMAGES = {
            R.drawable.img_detail_product_01, R.drawable.img_detail_product_02,
            R.drawable.img_detail_product_03, R.drawable.img_detail_product_04,
            R.drawable.img_detail_product_05, R.drawable.img_detail_product_06,
            R.drawable.img_detail_product_07, R.drawable.img_detail_product_08,
            R.drawable.img_detail_product_09, R.drawable.img_detail_product_10,
            R.drawable.img_detail_product_11, R.drawable.img_detail_product_12,
            R.drawable.img_detail_product_13, R.drawable.img_detail_product_14,
            R.drawable.img_detail_product_15, R.drawable.img_detail_product_16
    };

    private DetailProductImages() { }

    public static int forProduct(Product product) {
        if (product == null) return IMAGES[0];
        int index = product.getId() - 1;
        return index >= 0 && index < IMAGES.length ? IMAGES[index] : ProductImages.forKey(product.getImageKey());
    }
}
