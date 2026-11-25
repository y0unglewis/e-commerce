package com.example.gearhub.ui;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.example.gearhub.adapter.RelatedProductAdapter;
import com.example.gearhub.database.GearHubDatabaseHelper;
import com.example.gearhub.model.Product;
import com.example.gearhub.model.Review;
import com.example.gearhub.util.CartManager;
import com.example.gearhub.util.HomeProductImages;
import com.example.gearhub.util.PriceFormatter;
import com.example.gearhub.util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String[] TAB_TITLES = {"Thông số", "Mô tả", "Đánh giá"};
    private static final int RELATED_MIN = 4;

    private Product product;
    private TextView quantityText, subtotalText, emptyRelatedText;
    private TextView emptyReviewsText, reviewCountText;
    private TextInputLayout layoutReviewComment;
    private TextInputEditText inputReviewComment;
    private MaterialButton btnSubmitReview;
    private TextView[] starButtons;
    private View[] tabPages;
    private RelatedProductAdapter relatedAdapter;
    private int quantity = 1;
    private int selectedRating = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        product = readProduct();
        if (product == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initViews();
        bindProduct();
        setupTabs();
        setupActions();
        setupReviewForm();
        loadReviews();
        loadRelatedProducts();
    }

    @SuppressWarnings("deprecation")
    private Product readProduct() {
        Object extra = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? getIntent().getSerializableExtra(AppConstants.EXTRA_PRODUCT, Product.class)
                : getIntent().getSerializableExtra(AppConstants.EXTRA_PRODUCT);
        return extra instanceof Product ? (Product) extra : null;
    }

    private void initViews() {
        quantityText = findViewById(R.id.txtQuantity);
        subtotalText = findViewById(R.id.txtSubtotalPrice);
        emptyRelatedText = findViewById(R.id.txtEmptyRelated);
        emptyReviewsText = findViewById(R.id.txtEmptyReviews);
        reviewCountText = findViewById(R.id.txtReviewCount);
        layoutReviewComment = findViewById(R.id.layoutReviewComment);
        inputReviewComment = findViewById(R.id.inputReviewComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        starButtons = new TextView[]{
                findViewById(R.id.star1), findViewById(R.id.star2), findViewById(R.id.star3),
                findViewById(R.id.star4), findViewById(R.id.star5)
        };
        tabPages = new View[]{findViewById(R.id.layoutTabSpecs),
                findViewById(R.id.layoutTabDescription), findViewById(R.id.layoutTabReviews)};

        RecyclerView recycler = findViewById(R.id.recyclerRelatedProducts);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedAdapter = new RelatedProductAdapter(item -> startActivity(productIntent(ProductDetailActivity.class, item)));
        recycler.setAdapter(relatedAdapter);
    }

    private void bindProduct() {
        ImageView image = findViewById(R.id.imgDetailProduct);
        image.setImageResource(HomeProductImages.forProduct(product));
        image.setContentDescription(product.getName());
        setText(R.id.txtDetailBrand, product.getBrand());
        setText(R.id.txtDetailCategory, product.getCategory());
        setText(R.id.txtDetailName, product.getName());
        setText(R.id.txtDetailPrice, PriceFormatter.format(product.getPrice()));
        setText(R.id.txtDetailRatingBadge, String.format(Locale.getDefault(), "★ %.1f / 5.0", product.getRating()));
        setText(R.id.txtReviewScoreBig, String.format(Locale.getDefault(), "%.1f", product.getRating()));

        TextView original = findViewById(R.id.txtDetailOriginalPrice);
        TextView discount = findViewById(R.id.txtDetailDiscountBadge);
        boolean onSale = product.getOriginalPrice() > product.getPrice();
        original.setVisibility(onSale ? View.VISIBLE : View.GONE);
        discount.setVisibility(onSale ? View.VISIBLE : View.GONE);
        if (onSale) {
            original.setText(PriceFormatter.format(product.getOriginalPrice()));
            original.setPaintFlags(original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setText(String.format(Locale.getDefault(), "-%d%%",
                    Math.round((1.0 - (double) product.getPrice() / product.getOriginalPrice()) * 100)));
        }

        populateSpecs();
        populateDescription();
        updateQuantity(0);
    }

    private void populateSpecs() {
        LinearLayout container = findViewById(R.id.containerSpecRows);
        addSpecRow(container, "Thương hiệu", product.getBrand(), true);
        addSpecRow(container, "Danh mục", product.getCategory(), false);
        String specs = product.getSpecifications() == null ? "" : product.getSpecifications();
        int index = 0;
        for (String line : specs.split("\\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            int colon = line.indexOf(':');
            addSpecRow(container,
                    colon > 0 ? line.substring(0, colon).trim() : "Thông số " + (index + 1),
                    colon > 0 ? line.substring(colon + 1).trim() : line,
                    index++ % 2 == 0);
        }
    }

    private void addSpecRow(LinearLayout container, String label, String value, boolean highlight) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(12), dp(10), dp(12), dp(10));
        if (highlight) row.setBackgroundResource(R.drawable.bg_product_icon);
        row.addView(createText(label, R.color.gear_muted, 14, false), new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f));
        row.addView(createText(value, R.color.gear_text, 14, true), new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.3f));
        container.addView(row);
    }

    private void populateDescription() {
        setText(R.id.txtDescriptionBody, product.getDescription() + "\n\nSản phẩm " + product.getName()
                + " chính hãng từ " + product.getBrand()
                + " được thiết kế tối ưu cho nhu cầu học tập, làm việc và giải trí hằng ngày với độ bền cao.");
        setText(R.id.txtDescriptionHighlights, "• Sản phẩm mới 100%, nguyên hộp chính hãng " + product.getBrand()
                + "\n• Hỗ trợ đổi trả trong 7 ngày nếu phát sinh lỗi từ nhà sản xuất"
                + "\n• Tư vấn kỹ thuật và bảo hành nhanh chóng tại hệ thống GearHub");
    }

    private void setupReviewForm() {
        for (int i = 0; i < starButtons.length; i++) {
            final int score = i + 1;
            starButtons[i].setOnClickListener(v -> {
                selectedRating = score;
                updateStarPicker();
            });
        }
        updateStarPicker();
        btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void updateStarPicker() {
        for (int i = 0; i < starButtons.length; i++) {
            int colorRes = i < selectedRating ? R.color.gear_accent : R.color.gear_border;
            starButtons[i].setTextColor(ContextCompat.getColor(this, colorRes));
        }
    }

    private void submitReview() {
        String comment = inputReviewComment.getText() == null ? "" : inputReviewComment.getText().toString().trim();
        layoutReviewComment.setError(null);
        if (comment.isEmpty()) {
            layoutReviewComment.setError("Vui lòng nhập nội dung đánh giá");
            inputReviewComment.requestFocus();
            return;
        }
        String savedUser = SessionManager.getUsername(this);
        String username = savedUser.trim().isEmpty() ? "Thành viên GearHub" : savedUser.trim();
        String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        int ratingToSave = selectedRating;

        btnSubmitReview.setEnabled(false);
        new Thread(() -> {
            try {
                long rowId = GearHubDatabaseHelper.getInstance(this)
                        .addReview(product.getId(), username, ratingToSave, comment, createdAt);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    btnSubmitReview.setEnabled(true);
                    if (rowId == -1) {
                        Toast.makeText(this, "Không thể lưu đánh giá", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    inputReviewComment.setText("");
                    selectedRating = 5;
                    updateStarPicker();
                    Toast.makeText(this, "Đã gửi đánh giá", Toast.LENGTH_SHORT).show();
                    loadReviews();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    btnSubmitReview.setEnabled(true);
                    Toast.makeText(this, "Lỗi lưu đánh giá", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void loadReviews() {
        new Thread(() -> {
            try {
                List<Review> reviews = GearHubDatabaseHelper.getInstance(this).getReviewsByProduct(product.getId());
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    renderReviews(reviews);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    emptyReviewsText.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private void renderReviews(List<Review> reviews) {
        LinearLayout container = findViewById(R.id.containerReviews);
        container.removeAllViews();
        emptyReviewsText.setVisibility(reviews.isEmpty() ? View.VISIBLE : View.GONE);

        double average = product.getRating();
        if (!reviews.isEmpty()) {
            int sum = 0;
            for (Review review : reviews) sum += review.getRating();
            average = (double) sum / reviews.size();
        }
        setText(R.id.txtReviewScoreBig, String.format(Locale.getDefault(), "%.1f", average));
        setText(R.id.txtDetailRatingBadge, String.format(Locale.getDefault(), "★ %.1f / 5.0", average));
        reviewCountText.setText(reviews.size() + " đánh giá");

        for (Review review : reviews) {
            LinearLayout header = new LinearLayout(this);
            header.setGravity(Gravity.CENTER_VERTICAL);
            header.addView(createText(review.getUsername(), R.color.gear_text, 14, true),
                    new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f));
            header.addView(createText(starsText(review.getRating()) + "  •  " + review.getCreatedAt(),
                    R.color.gear_accent, 12, false));

            TextView comment = createText(review.getComment(), R.color.gear_muted, 14, false);
            comment.setPadding(0, dp(4), 0, 0);

            LinearLayout box = new LinearLayout(this);
            box.setOrientation(LinearLayout.VERTICAL);
            box.setPadding(0, dp(10), 0, dp(10));
            box.addView(header);
            box.addView(comment);
            container.addView(box);
        }
    }

    private String starsText(int rating) {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) sb.append(i < rating ? '★' : '☆');
        return sb.toString();
    }

    private void setupTabs() {
        TabLayout tabs = findViewById(R.id.detailTabs);
        for (String title : TAB_TITLES) tabs.addTab(tabs.newTab().setText(title));
        showTab(0);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) { showTab(tab.getPosition()); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void showTab(int index) {
        for (int i = 0; i < tabPages.length; i++) tabPages[i].setVisibility(i == index ? View.VISIBLE : View.GONE);
    }

    private void setupActions() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnDetailCart).setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        findViewById(R.id.btnMinus).setOnClickListener(v -> updateQuantity(-1));
        findViewById(R.id.btnPlus).setOnClickListener(v -> updateQuantity(1));
        findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product, quantity);
            Snackbar.make(v,
                            "Đã thêm " + quantity + " × " + product.getName() + " vào giỏ hàng",
                            Snackbar.LENGTH_SHORT)
                    .setAction("Xem giỏ", view -> startActivity(new Intent(this, CartActivity.class)))
                    .show();
        });
        findViewById(R.id.btnBuyNow).setOnClickListener(v -> {
            Intent intent = productIntent(CheckoutActivity.class, product);
            intent.putExtra(AppConstants.EXTRA_QUANTITY, quantity);
            startActivity(intent);
        });
    }

    private void updateQuantity(int delta) {
        quantity = Math.max(1, Math.min(99, quantity + delta));
        quantityText.setText(String.valueOf(quantity));
        subtotalText.setText(PriceFormatter.format(product.getPrice() * quantity));
    }

    private void loadRelatedProducts() {
        new Thread(() -> {
            try {
                List<Product> related = new ArrayList<>();
                List<Product> sameBrand = new ArrayList<>();
                List<Product> others = new ArrayList<>();
                for (Product item : GearHubDatabaseHelper.getInstance(this).getAllProducts()) {
                    if (item.getId() == product.getId()) continue;
                    if (item.getCategory().equals(product.getCategory())) related.add(item);
                    else if (item.getBrand().equalsIgnoreCase(product.getBrand())) sameBrand.add(item);
                    else others.add(item);
                }
                related.addAll(sameBrand);
                for (int i = 0; related.size() < RELATED_MIN && i < others.size(); i++) related.add(others.get(i));
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    relatedAdapter.setProducts(related);
                    emptyRelatedText.setVisibility(related.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    emptyRelatedText.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    private Intent productIntent(Class<?> target, Product item) {
        return new Intent(this, target).putExtra(AppConstants.EXTRA_PRODUCT, item);
    }

    private TextView createText(String text, int colorRes, float sizeSp, boolean bold) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(ContextCompat.getColor(this, colorRes));
        view.setTextSize(sizeSp);
        if (bold) view.setTypeface(null, Typeface.BOLD);
        return view;
    }

    private void setText(int viewId, CharSequence text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
