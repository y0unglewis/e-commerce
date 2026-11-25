package com.example.gearhub.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gearhub.model.CartItem;
import com.example.gearhub.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý giỏ hàng toàn ứng dụng theo mẫu Singleton.
 * Lưu trữ danh sách CartItem trong bộ nhớ và đồng bộ bền vững xuống SharedPreferences.
 */
public final class CartManager {

    private static final String PREF_CART = "gearhub_cart_prefs";
    private static final String KEY_CART_ITEMS = "cart_items";

    private static CartManager instance;
    private final List<CartItem> items;
    private Context appContext;
    private boolean loadedFromPrefs = false;

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
     * Lấy CartManager đồng thời khởi tạo Context để đọc/ghi SharedPreferences.
     */
    public static synchronized CartManager getInstance(Context context) {
        CartManager manager = getInstance();
        manager.init(context);
        return manager;
    }

    /**
     * Khởi tạo ApplicationContext và khôi phục giỏ hàng từ SharedPreferences nếu chưa nạp.
     */
    public synchronized void init(Context context) {
        if (context == null) return;
        if (appContext == null) {
            appContext = context.getApplicationContext();
        }
        if (!loadedFromPrefs) {
            loadedFromPrefs = true;
            List<CartItem> savedItems = loadFromPreferences();
            if (items.isEmpty()) {
                items.addAll(savedItems);
            } else if (!savedItems.isEmpty()) {
                List<CartItem> pendingItems = new ArrayList<>(items);
                items.clear();
                items.addAll(savedItems);
                for (CartItem pending : pendingItems) {
                    addOrMergeItem(pending.getProduct(), pending.getQuantity());
                }
                saveToPreferences();
            } else {
                saveToPreferences();
            }
        }
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

        addOrMergeItem(product, quantity);
        saveToPreferences();
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
                saveToPreferences();
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
                saveToPreferences();
                return;
            }
        }
    }

    /**
     * Xóa một sản phẩm khỏi giỏ theo productId.
     */
    public synchronized void removeItem(int productId) {
        boolean removed = items.removeIf(item -> item.getProduct().getId() == productId);
        if (removed) {
            saveToPreferences();
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng (sau khi đặt hàng thành công hoặc đăng xuất).
     */
    public synchronized void clearCart() {
        items.clear();
        loadedFromPrefs = true;
        clearPreferences();
    }

    /**
     * Xóa toàn bộ giỏ hàng kèm Context để đảm bảo dọn sạch cả SharedPreferences.
     */
    public synchronized void clearCart(Context context) {
        if (context != null && appContext == null) {
            appContext = context.getApplicationContext();
        }
        clearCart();
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

    private void addOrMergeItem(Product product, int quantity) {
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

    private void saveToPreferences() {
        if (appContext == null) return;

        JSONArray array = new JSONArray();
        try {
            for (CartItem item : items) {
                Product product = item.getProduct();
                JSONObject obj = new JSONObject();
                obj.put("id", product.getId());
                obj.put("name", safeString(product.getName()));
                obj.put("brand", safeString(product.getBrand()));
                obj.put("category", safeString(product.getCategory()));
                obj.put("price", product.getPrice());
                obj.put("originalPrice", product.getOriginalPrice());
                obj.put("rating", (double) product.getRating());
                obj.put("imageKey", safeString(product.getImageKey()));
                obj.put("description", safeString(product.getDescription()));
                obj.put("specifications", safeString(product.getSpecifications()));
                obj.put("quantity", item.getQuantity());
                array.put(obj);
            }
            SharedPreferences prefs = appContext.getSharedPreferences(PREF_CART, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_CART_ITEMS, array.toString()).apply();
        } catch (JSONException ignored) {
            // Bỏ qua nếu dữ liệu JSON không hợp lệ
        }
    }

    private List<CartItem> loadFromPreferences() {
        List<CartItem> restored = new ArrayList<>();
        if (appContext == null) return restored;

        SharedPreferences prefs = appContext.getSharedPreferences(PREF_CART, Context.MODE_PRIVATE);
        String raw = prefs.getString(KEY_CART_ITEMS, "");
        if (raw.isEmpty()) return restored;

        try {
            JSONArray array = new JSONArray(raw);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                if (obj == null) continue;

                long price = obj.getLong("price");
                Product product = new Product(
                        obj.getInt("id"),
                        obj.optString("name", ""),
                        obj.optString("brand", ""),
                        obj.optString("category", ""),
                        price,
                        obj.optLong("originalPrice", price),
                        (float) obj.optDouble("rating", 0.0),
                        obj.optString("imageKey", ""),
                        obj.optString("description", ""),
                        obj.optString("specifications", "")
                );
                int quantity = obj.optInt("quantity", 1);
                restored.add(new CartItem(product, quantity));
            }
        } catch (JSONException ignored) {
            // Nếu dữ liệu cũ bị lỗi định dạng thì trả về danh sách rỗng
        }
        return restored;
    }

    private void clearPreferences() {
        if (appContext == null) return;
        SharedPreferences prefs = appContext.getSharedPreferences(PREF_CART, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_CART_ITEMS).apply();
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }
}
