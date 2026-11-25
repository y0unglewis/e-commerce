package com.example.gearhub.model;

import java.io.Serializable;

/**
 * Model đại diện cho một sản phẩm trong giỏ hàng kèm theo số lượng.
 * Triển khai Serializable để có thể truyền giữa các màn hình nếu cần.
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = Math.max(1, Math.min(99, quantity));
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, Math.min(99, quantity));
    }

    public long getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
