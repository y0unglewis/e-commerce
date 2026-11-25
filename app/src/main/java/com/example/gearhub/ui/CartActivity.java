package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearhub.R;
import com.example.gearhub.adapter.CartAdapter;
import com.example.gearhub.database.GearHubDatabaseHelper;
import com.example.gearhub.model.CartItem;
import com.example.gearhub.model.Product;
import com.example.gearhub.util.CartManager;
import com.example.gearhub.util.PriceFormatter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Màn hình Giỏ hàng (CartActivity) - Phụ trách bởi TV3.
 * Quản lý danh sách sản phẩm trong giỏ, tăng/giảm/xóa và tính tổng tiền realtime.
 */
public class CartActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtHeaderCount;
    private RecyclerView recyclerCart;
    private View layoutEmpty;
    private View layoutBottomSummary;
    private TextView txtTotalPrice;
    private MaterialButton btnCheckout;
    private MaterialButton btnContinueShopping;
    private MaterialButton btnAddSampleToCart;

    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        CartManager.getInstance(this);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại dữ liệu mỗi khi quay trở lại màn hình giỏ hàng
        loadCartData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnCartBack);
        txtHeaderCount = findViewById(R.id.txtCartItemCountHeader);
        recyclerCart = findViewById(R.id.recyclerCart);
        layoutEmpty = findViewById(R.id.layoutCartEmpty);
        layoutBottomSummary = findViewById(R.id.layoutCartBottomSummary);
        txtTotalPrice = findViewById(R.id.txtCartTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
        btnAddSampleToCart = findViewById(R.id.btnAddSampleToCart);
    }

    private void setupRecyclerView() {
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                CartManager.getInstance().updateQuantity(item.getProduct().getId(), newQuantity);
                loadCartData();
            }

            @Override
            public void onItemRemoved(CartItem item) {
                showConfirmDeleteDialog(item);
            }
        });
        recyclerCart.setAdapter(cartAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnContinueShopping.setOnClickListener(v -> finish());

        btnAddSampleToCart.setOnClickListener(v -> {
            new Thread(() -> {
                List<Product> products = GearHubDatabaseHelper.getInstance(this).getAllProducts();
                if (!products.isEmpty()) {
                    CartManager.getInstance().addToCart(products.get(0), 1);
                    if (products.size() > 1) {
                        CartManager.getInstance().addToCart(products.get(1), 2);
                    }
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đã thêm sản phẩm mẫu vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        loadCartData();
                    });
                }
            }).start();
        });

        btnCheckout.setOnClickListener(v -> {
            if (CartManager.getInstance().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Chuyển sang màn hình thanh toán
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    private void loadCartData() {
        List<CartItem> items = CartManager.getInstance().getItems();
        boolean isEmpty = items.isEmpty();

        if (isEmpty) {
            recyclerCart.setVisibility(View.GONE);
            layoutBottomSummary.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            txtHeaderCount.setText("(0 món)");
        } else {
            recyclerCart.setVisibility(View.VISIBLE);
            layoutBottomSummary.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            int totalQty = CartManager.getInstance().getTotalQuantity();
            txtHeaderCount.setText("(" + totalQty + " món)");
            txtTotalPrice.setText(PriceFormatter.format(CartManager.getInstance().getTotalPrice()));

            cartAdapter.submitList(items);
        }
    }

    private void showConfirmDeleteDialog(CartItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa khỏi giỏ hàng")
                .setMessage("Bạn có chắc chắn muốn bỏ \"" + item.getProduct().getName() + "\" khỏi giỏ?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    CartManager.getInstance().removeItem(item.getProduct().getId());
                    loadCartData();
                    Toast.makeText(CartActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
