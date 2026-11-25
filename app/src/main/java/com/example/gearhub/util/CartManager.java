package com.example.gearhub.util;

import com.example.gearhub.model.CartItem;
import com.example.gearhub.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý giỏ hàng toàn ứng dụng theo mẫu Singleton.
 * Lưu trữ danh sách CartItem trong bộ nhớ và cung cấp các hàm thêm, sửa số lượng, xóa, tính tổng tiền.
 */
public final class CartManager {

    private static CartManager instance;
    private final List<CartItem> items;

    private CartManager() {
        items = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    /**
     * Lấy bản sao danh sách sản phẩm hiện tại trong giỏ.
     */
    public synchronized List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng. Nếu đã có thì cộng dồn số lượng (tối đa 99).
     */
    public synchronized void addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) return;

        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                int newQty = Math.min(99, item.getQuantity() + quantity);
                item.setQuantity(newQty);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    /**
     * Cập nhật số lượng của một sản phẩm trong giỏ.
     */
    public synchronized void updateQuantity(int productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(Math.min(99, quantity));
                }
                return;
            }
        }
    }

    /**
     * Tăng/giảm số lượng sản phẩm theo delta (+1 hoặc -1).
     */
    public synchronized void changeQuantity(int productId, int delta) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                int newQty = item.getQuantity() + delta;
                if (newQty <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(Math.min(99, newQty));
                }
                return;
            }
        }
    }

    /**
     * Xóa một sản phẩm khỏi giỏ theo productId.
     */
    public synchronized void removeItem(int productId) {
        items.removeIf(item -> item.getProduct().getId() == productId);
    }

    /**
     * Xóa toàn bộ giỏ hàng (sau khi đặt hàng thành công).
     */
    public synchronized void clearCart() {
        items.clear();
    }

    /**
     * Tính tổng số lượng món hàng trong giỏ.
     */
    public synchronized int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Tính tổng giá tiền các sản phẩm trong giỏ.
     */
    public synchronized long getTotalPrice() {
        long total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    /**
     * Kiểm tra giỏ hàng có rỗng không.
     */
    public synchronized boolean isEmpty() {
        return items.isEmpty();
    }
}
