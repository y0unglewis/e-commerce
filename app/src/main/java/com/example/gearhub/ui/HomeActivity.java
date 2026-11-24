package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.example.gearhub.adapter.ProductAdapter;
import com.example.gearhub.database.GearHubDatabaseHelper;
import com.example.gearhub.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private final List<Product> allProducts = new ArrayList<>();
    private ProductAdapter adapter;
    private TextInputEditText searchInput;
    private TextView emptyText;
    private String selectedCategory = AppConstants.CATEGORY_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        setupSearch();
        setupCategories();
        setupNavigation();
        loadProducts();
    }

    private void initViews() {
        searchInput = findViewById(R.id.inputSearch);
        emptyText = findViewById(R.id.txtEmpty);
        RecyclerView productList = findViewById(R.id.recyclerProducts);
        productList.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this::openProductDetail);
        productList.setAdapter(adapter);
        findViewById(R.id.btnHeaderCart).setOnClickListener(v -> openCart());
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    private void setupCategories() {
        ChipGroup chips = findViewById(R.id.chipCategories);
        chips.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.chipHeadphones) selectedCategory = AppConstants.CATEGORY_HEADPHONES;
            else if (id == R.id.chipKeyboards) selectedCategory = AppConstants.CATEGORY_KEYBOARDS;
            else if (id == R.id.chipMice) selectedCategory = AppConstants.CATEGORY_MICE;
            else if (id == R.id.chipChargers) selectedCategory = AppConstants.CATEGORY_CHARGERS;
            else selectedCategory = AppConstants.CATEGORY_ALL;
            applyFilters();
        });
    }

    private void setupNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cart) {
                openCart();
                return false; // Home vẫn là tab hiện tại khi quay lại.
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return false;
            }
            return id == R.id.nav_home;
        });
    }

    private void loadProducts() {
        // SQLite có thể mất thời gian mở lần đầu nên không đọc trên UI thread.
        new Thread(() -> {
            try {
                List<Product> result = GearHubDatabaseHelper.getInstance(this).getAllProducts();
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    allProducts.clear();
                    allProducts.addAll(result);
                    applyFilters();
                });
            } catch (Exception error) {
                runOnUiThread(() -> Toast.makeText(this,
                        "Không đọc được sản phẩm: " + error.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void applyFilters() {
        if (adapter == null) return;
        String search = searchInput.getText() == null ? "" :
                searchInput.getText().toString().trim().toLowerCase(Locale.ROOT);
        List<Product> visible = new ArrayList<>();
        for (Product product : allProducts) {
            boolean categoryMatches = AppConstants.CATEGORY_ALL.equals(selectedCategory)
                    || selectedCategory.equals(product.getCategory());
            boolean nameMatches = product.getName().toLowerCase(Locale.ROOT).contains(search);
            if (categoryMatches && nameMatches) visible.add(product);
        }
        adapter.setProducts(visible);
        emptyText.setVisibility(visible.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        // Truyền cả Product, không cần putExtra từng thuộc tính.
        intent.putExtra(AppConstants.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    private void openCart() {
        startActivity(new Intent(this, CartActivity.class));
    }
}
