package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.example.gearhub.adapter.HomeHeaderAdapter;
import com.example.gearhub.adapter.ProductAdapter;
import com.example.gearhub.database.GearHubDatabaseHelper;
import com.example.gearhub.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final int SORT_DEFAULT = 0;
    private static final int SORT_PRICE_ASC = 1;
    private static final int SORT_PRICE_DESC = 2;
    private static final int SORT_RATING = 3;

    private final List<Product> allProducts = new ArrayList<>();

    private ProductAdapter adapter;
    private TextInputEditText searchInput;
    private HomeHeaderAdapter headerAdapter;

    private String selectedCategory = AppConstants.CATEGORY_ALL;

    private long minPrice = 0;
    private long maxPrice = Long.MAX_VALUE;
    private int selectedSort = SORT_DEFAULT;

    private boolean productsLoaded = false;

    private final String[] categories = {
        AppConstants.CATEGORY_ALL,
        AppConstants.CATEGORY_HEADPHONES,
        AppConstants.CATEGORY_KEYBOARDS,
        AppConstants.CATEGORY_MICE,
        AppConstants.CATEGORY_CHARGERS
    };

    private final String[] sortOptions = {
        "Mặc định",
        "Giá thấp đến cao",
        "Giá cao đến thấp",
        "Đánh giá cao nhất"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupSearch();
        setupCategories();
        setupNavigation();
        setupFilter();
        loadProducts();
    }

    private void initViews() {
        searchInput = findViewById(R.id.inputSearch);
        setupProductList();

        findViewById(R.id.btnHeaderCart)
            .setOnClickListener(v -> openCart());
    }

    private void setupProductList() {
        RecyclerView productList = findViewById(R.id.recyclerProducts);
        headerAdapter = new HomeHeaderAdapter();
        adapter = new ProductAdapter(this::openProductDetail);

        // Header chiếm cả hai cột; mỗi sản phẩm chiếm một cột.
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < headerAdapter.getItemCount() ? 2 : 1;
            }
        });

        productList.setLayoutManager(manager);
        productList.setAdapter(new ConcatAdapter(headerAdapter, adapter));
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                CharSequence s, int start,
                int count, int after) {
            }

            @Override
            public void onTextChanged(
                CharSequence s, int start,
                int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupCategories() {
        ChipGroup chips = findViewById(R.id.chipCategories);

        chips.setOnCheckedStateChangeListener(
            (group, checkedIds) -> {

                if (checkedIds.isEmpty()) return;

                int id = checkedIds.get(0);

                if (id == R.id.chipHeadphones) {
                    selectedCategory =
                        AppConstants.CATEGORY_HEADPHONES;

                } else if (id == R.id.chipKeyboards) {
                    selectedCategory =
                        AppConstants.CATEGORY_KEYBOARDS;

                } else if (id == R.id.chipMice) {
                    selectedCategory =
                        AppConstants.CATEGORY_MICE;

                } else if (id == R.id.chipChargers) {
                    selectedCategory =
                        AppConstants.CATEGORY_CHARGERS;

                } else {
                    selectedCategory =
                        AppConstants.CATEGORY_ALL;
                }

                applyFilters();
            });
    }

    private void setupNavigation() {
        BottomNavigationView nav =
            findViewById(R.id.bottomNavigation);

        nav.setSelectedItemId(R.id.nav_home);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_cart) {
                openCart();
                return false;
            }

            if (id == R.id.nav_profile) {
                startActivity(new Intent(
                    this, ProfileActivity.class
                ));

                return false;
            }

            return id == R.id.nav_home;
        });
    }

    private void setupFilter() {
        findViewById(R.id.btnFilter)
            .setOnClickListener(v -> showFilterDialog());
    }

    private void loadProducts() {
        // Đọc SQLite trên luồng riêng.
        new Thread(() -> {
            try {
                List<Product> result =
                    GearHubDatabaseHelper
                        .getInstance(this)
                        .getAllProducts();

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;

                    allProducts.clear();
                    allProducts.addAll(result);

                    productsLoaded = true;

                    applyFilters();
                });

            } catch (Exception error) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;

                    Toast.makeText(
                        this,
                        "Không đọc được sản phẩm: "
                            + error.getMessage(),
                        Toast.LENGTH_LONG
                    ).show();
                });
            }
        }).start();
    }

    private void showFilterDialog() {
        BottomSheetDialog dialog =
            new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(
            R.layout.dialog_product_filter, null
        );

        dialog.setContentView(view);

        initFilterViews(view);
        setupFilterActions(view, dialog);

        dialog.show();

        dialog.getBehavior().setState(
            BottomSheetBehavior.STATE_EXPANDED
        );

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams
                    .SOFT_INPUT_ADJUST_RESIZE
                    | WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
        }
    }

    private void initFilterViews(View view) {
        Spinner categorySpinner =
            view.findViewById(R.id.spinnerFilterCategory);

        Spinner sortSpinner =
            view.findViewById(R.id.spinnerFilterSort);

        TextInputEditText inputMin =
            view.findViewById(R.id.inputMinPrice);

        TextInputEditText inputMax =
            view.findViewById(R.id.inputMaxPrice);

        setupSpinner(categorySpinner, categories);
        setupSpinner(sortSpinner, sortOptions);

        int categoryIndex = Arrays.asList(categories)
            .indexOf(selectedCategory);

        categorySpinner.setSelection(
            Math.max(0, categoryIndex)
        );

        sortSpinner.setSelection(selectedSort);

        inputMin.setText(
            minPrice == 0
                ? "" : String.valueOf(minPrice)
        );

        inputMax.setText(
            maxPrice == Long.MAX_VALUE
                ? "" : String.valueOf(maxPrice)
        );
    }

    private void setupSpinner(
        Spinner spinner, String[] values) {

        ArrayAdapter<String> spinnerAdapter =
            new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                values
            );

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(spinnerAdapter);
    }

    private void setupFilterActions(
        View view, BottomSheetDialog dialog) {

        view.findViewById(R.id.btnResetFilter)
            .setOnClickListener(
                v -> resetFilterViews(view)
            );

        view.findViewById(R.id.btnApplyFilter)
            .setOnClickListener(v -> {
                if (readFilterValues(view)) {
                    syncCategoryChip();
                    applyFilters();
                    dialog.dismiss();
                }
            });
    }

    private void resetFilterViews(View view) {
        Spinner categorySpinner =
            view.findViewById(R.id.spinnerFilterCategory);

        Spinner sortSpinner =
            view.findViewById(R.id.spinnerFilterSort);

        TextInputEditText inputMin =
            view.findViewById(R.id.inputMinPrice);

        TextInputEditText inputMax =
            view.findViewById(R.id.inputMaxPrice);

        TextInputLayout layoutMin =
            view.findViewById(R.id.layoutMinPrice);

        TextInputLayout layoutMax =
            view.findViewById(R.id.layoutMaxPrice);

        categorySpinner.setSelection(0);
        sortSpinner.setSelection(SORT_DEFAULT);

        inputMin.setText("");
        inputMax.setText("");

        layoutMin.setError(null);
        layoutMax.setError(null);
    }

    private boolean readFilterValues(View view) {
        Spinner categorySpinner =
            view.findViewById(R.id.spinnerFilterCategory);

        Spinner sortSpinner =
            view.findViewById(R.id.spinnerFilterSort);

        TextInputLayout layoutMin =
            view.findViewById(R.id.layoutMinPrice);

        TextInputLayout layoutMax =
            view.findViewById(R.id.layoutMaxPrice);

        TextInputEditText inputMin =
            view.findViewById(R.id.inputMinPrice);

        TextInputEditText inputMax =
            view.findViewById(R.id.inputMaxPrice);

        String min = getText(inputMin);
        String max = getText(inputMax);

        layoutMin.setError(null);
        layoutMax.setError(null);

        long newMin;
        long newMax;

        try {
            newMin = min.isEmpty()
                ? 0 : Long.parseLong(min);

        } catch (NumberFormatException e) {
            layoutMin.setError("Giá không hợp lệ");
            return false;
        }

        try {
            newMax = max.isEmpty()
                ? Long.MAX_VALUE : Long.parseLong(max);

        } catch (NumberFormatException e) {
            layoutMax.setError("Giá không hợp lệ");
            return false;
        }

        if (newMin < 0) {
            layoutMin.setError("Giá không được âm");
            return false;
        }

        if (newMax < 0) {
            layoutMax.setError("Giá không được âm");
            return false;
        }

        if (newMax < newMin) {
            layoutMax.setError("Giá tối đa nhỏ hơn giá tối thiểu");
            return false;
        }

        // Chỉ cập nhật khi toàn bộ dữ liệu hợp lệ.
        minPrice = newMin;
        maxPrice = newMax;

        selectedCategory =
            categorySpinner.getSelectedItem().toString();

        selectedSort =
            sortSpinner.getSelectedItemPosition();

        return true;
    }

    private void syncCategoryChip() {
        ChipGroup chips = findViewById(R.id.chipCategories);

        if (AppConstants.CATEGORY_HEADPHONES
            .equals(selectedCategory)) {

            chips.check(R.id.chipHeadphones);

        } else if (AppConstants.CATEGORY_KEYBOARDS
            .equals(selectedCategory)) {

            chips.check(R.id.chipKeyboards);

        } else if (AppConstants.CATEGORY_MICE
            .equals(selectedCategory)) {

            chips.check(R.id.chipMice);

        } else if (AppConstants.CATEGORY_CHARGERS
            .equals(selectedCategory)) {

            chips.check(R.id.chipChargers);

        } else {
            chips.check(R.id.chipAll);
        }
    }

    private void applyFilters() {
        if (adapter == null || !productsLoaded) return;

        String search = getText(searchInput)
            .toLowerCase(Locale.ROOT);

        List<Product> visible = new ArrayList<>();

        for (Product product : allProducts) {

            boolean categoryMatches =
                AppConstants.CATEGORY_ALL
                    .equals(selectedCategory)
                    || selectedCategory
                    .equals(product.getCategory());

            boolean nameMatches = product.getName()
                .toLowerCase(Locale.ROOT)
                .contains(search);

            boolean priceMatches =
                product.getPrice() >= minPrice
                    && product.getPrice() <= maxPrice;

            if (categoryMatches && nameMatches && priceMatches) {
                visible.add(product);
            }
        }

        sortProducts(visible);

        adapter.setProducts(visible);
        headerAdapter.setEmpty(visible.isEmpty());

        updateFilterButton();
    }

    private void sortProducts(List<Product> products) {
        if (selectedSort == SORT_PRICE_ASC) {

            products.sort(
                Comparator.comparingLong(Product::getPrice)
            );

        } else if (selectedSort == SORT_PRICE_DESC) {

            products.sort(
                Comparator.comparingLong(Product::getPrice)
                    .reversed()
            );

        } else if (selectedSort == SORT_RATING) {

            products.sort(
                Comparator.comparingDouble(Product::getRating)
                    .reversed()
            );
        }
    }

    private void updateFilterButton() {
        TextView button = findViewById(R.id.btnFilter);

        boolean hasFilter =
            minPrice != 0
                || maxPrice != Long.MAX_VALUE
                || selectedSort != SORT_DEFAULT
                || !AppConstants.CATEGORY_ALL.equals(selectedCategory);

        button.setText(hasFilter ? "Lọc ✓" : "Lọc");
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null
            ? ""
            : input.getText().toString().trim();
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(
            this, ProductDetailActivity.class
        );

        // Truyền toàn bộ đối tượng Product.
        intent.putExtra(AppConstants.EXTRA_PRODUCT, product);

        startActivity(intent);
    }

    private void openCart() {
        startActivity(new Intent(this, CartActivity.class));
    }
}
