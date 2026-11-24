package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.example.gearhub.model.Product;
import com.example.gearhub.util.PriceFormatter;
import com.example.gearhub.util.ProductImages;
import com.google.android.material.tabs.TabLayout;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;
    private TextView tabContent, quantityText;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        if (!readProduct()) return;
        bindProduct();
        setupTabs();
        setupActions();
    }

    private boolean readProduct() {
        // Serializable giúp truyền một Product qua Intent đơn giản cho bài học.
        Object extra = getIntent().getSerializableExtra(AppConstants.EXTRA_PRODUCT);
        if (!(extra instanceof Product)) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        product = (Product) extra;
        return true;
    }

    private void bindProduct() {
        ImageView image = findViewById(R.id.imgDetailProduct);
        image.setImageResource(ProductImages.forKey(product.getImageKey()));
        ((TextView) findViewById(R.id.txtDetailBrand)).setText(product.getBrand());
        ((TextView) findViewById(R.id.txtDetailName)).setText(product.getName());
        ((TextView) findViewById(R.id.txtDetailPrice)).setText(PriceFormatter.format(product.getPrice()));
        tabContent = findViewById(R.id.txtTabContent);
        quantityText = findViewById(R.id.txtQuantity);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupTabs() {
        TabLayout tabs = findViewById(R.id.detailTabs);
        tabs.addTab(tabs.newTab().setText("Thông số"));
        tabs.addTab(tabs.newTab().setText("Mô tả"));
        tabs.addTab(tabs.newTab().setText("Đánh giá"));
        tabContent.setText(product.getSpecifications());
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) { showTab(tab.getPosition()); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void showTab(int index) {
        if (index == 0) tabContent.setText(product.getSpecifications());
        else if (index == 1) tabContent.setText(product.getDescription());
        else tabContent.setText("★ " + product.getRating()
                + " / 5\n\nĐánh giá thực tế sẽ được thành viên 2 bổ sung.");
    }

    private void setupActions() {
        findViewById(R.id.btnMinus).setOnClickListener(v -> updateQuantity(-1));
        findViewById(R.id.btnPlus).setOnClickListener(v -> updateQuantity(1));
        findViewById(R.id.btnAddToCart).setOnClickListener(v ->
                Toast.makeText(this, "Chờ thành viên 3 tích hợp giỏ hàng", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnBuyNow).setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(AppConstants.EXTRA_PRODUCT, product);
            startActivity(intent);
        });
    }

    private void updateQuantity(int delta) {
        quantity = Math.max(1, Math.min(99, quantity + delta));
        quantityText.setText(String.valueOf(quantity));
    }
}
