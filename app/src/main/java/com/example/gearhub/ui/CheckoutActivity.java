package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.example.gearhub.model.CartItem;
import com.example.gearhub.model.Product;
import com.example.gearhub.util.CartManager;
import com.example.gearhub.util.PriceFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Màn hình Thanh toán demo (CheckoutActivity) - Phụ trách bởi TV3.
 * Hỗ trợ 2 luồng: Mua ngay từ ProductDetailActivity hoặc Thanh toán từ CartActivity.
 * Validate form giao hàng và điều hướng sang OrderSuccessActivity.
 */
public class CheckoutActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextInputLayout layoutName, layoutPhone, layoutAddress;
    private TextInputEditText inputName, inputPhone, inputAddress, inputNote;
    private RadioGroup radioGroupPayment;
    private LinearLayout layoutProductList;
    private TextView txtSubtotal, txtShippingFee, txtTotal;
    private MaterialButton btnPlaceOrder;

    // Danh sách các mặt hàng sẽ thanh toán trong phiên này
    private final List<CartItem> checkoutItems = new ArrayList<>();
    private boolean isBuyNowFlow = false;
    private long totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        resolveCheckoutItems();
        displayOrderSummary();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnCheckoutBack);

        layoutName = findViewById(R.id.layoutCheckoutName);
        layoutPhone = findViewById(R.id.layoutCheckoutPhone);
        layoutAddress = findViewById(R.id.layoutCheckoutAddress);

        inputName = findViewById(R.id.inputCheckoutName);
        inputPhone = findViewById(R.id.inputCheckoutPhone);
        inputAddress = findViewById(R.id.inputCheckoutAddress);
        inputNote = findViewById(R.id.inputCheckoutNote);

        radioGroupPayment = findViewById(R.id.radioGroupPayment);

        layoutProductList = findViewById(R.id.layoutCheckoutProductList);
        txtSubtotal = findViewById(R.id.txtCheckoutSubtotal);
        txtShippingFee = findViewById(R.id.txtCheckoutShippingFee);
        txtTotal = findViewById(R.id.txtCheckoutTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }

    /**
     * Xác định nguồn sản phẩm: Nếu truyền Product qua Intent (luồng Mua ngay), tạo item riêng.
     * Nếu không có Intent Product, lấy toàn bộ danh sách trong CartManager.
     */
    private void resolveCheckoutItems() {
        checkoutItems.clear();

        Serializable extra = getIntent().getSerializableExtra(AppConstants.EXTRA_PRODUCT);
        if (extra instanceof Product) {
            // Luồng 1: Mua ngay từ ProductDetailActivity
            Product product = (Product) extra;
            int quantity = getIntent().getIntExtra("extra_quantity", 1);
            if (quantity <= 0) quantity = 1;

            checkoutItems.add(new CartItem(product, quantity));
            isBuyNowFlow = true;
        } else {
            // Luồng 2: Thanh toán từ CartActivity
            List<CartItem> cartList = CartManager.getInstance().getItems();
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Không có sản phẩm nào để thanh toán!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            checkoutItems.addAll(cartList);
            isBuyNowFlow = false;
        }
    }

    /**
     * Hiển thị danh sách sản phẩm và tổng tiền trong form checkout.
     */
    private void displayOrderSummary() {
        layoutProductList.removeAllViews();
        totalAmount = 0;

        LayoutInflater inflater = LayoutInflater.from(this);

        for (CartItem item : checkoutItems) {
            View itemView = inflater.inflate(R.layout.item_checkout_product, layoutProductList, false);

            TextView txtName = itemView.findViewById(R.id.txtCheckoutProductName);
            TextView txtQty = itemView.findViewById(R.id.txtCheckoutProductQty);
            TextView txtPrice = itemView.findViewById(R.id.txtCheckoutProductPrice);

            txtName.setText(item.getProduct().getName());
            txtQty.setText("x" + item.getQuantity());
            txtPrice.setText(PriceFormatter.format(item.getTotalPrice()));

            layoutProductList.addView(itemView);
            totalAmount += item.getTotalPrice();
        }

        txtSubtotal.setText(PriceFormatter.format(totalAmount));
        txtShippingFee.setText("Miễn phí");
        txtTotal.setText(PriceFormatter.format(totalAmount));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPlaceOrder.setOnClickListener(v -> handlePlaceOrder());
    }

    /**
     * Xử lý xác nhận đặt hàng demo.
     */
    private void handlePlaceOrder() {
        String name = getTextValue(inputName).trim();
        String phone = getTextValue(inputPhone).trim();
        String address = getTextValue(inputAddress).trim();

        // Xóa thông báo lỗi cũ
        layoutName.setError(null);
        layoutPhone.setError(null);
        layoutAddress.setError(null);

        // Validate tên người nhận
        if (name.isEmpty()) {
            layoutName.setError("Vui lòng nhập họ và tên người nhận");
            inputName.requestFocus();
            return;
        }

        // Validate số điện thoại (tối thiểu 10 chữ số)
        if (phone.isEmpty()) {
            layoutPhone.setError("Vui lòng nhập số điện thoại");
            inputPhone.requestFocus();
            return;
        }
        if (!phone.matches("^0[0-9]{9}$")) {
            layoutPhone.setError("Số điện thoại không hợp lệ (cần 10 chữ số, bắt đầu bằng 0)");
            inputPhone.requestFocus();
            return;
        }

        // Validate địa chỉ giao hàng
        if (address.isEmpty()) {
            layoutAddress.setError("Vui lòng nhập địa chỉ nhận hàng chi tiết");
            inputAddress.requestFocus();
            return;
        }

        // Xác định phương thức thanh toán
        String paymentMethod = "Thanh toán khi nhận hàng (COD)";
        int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedPaymentId == R.id.radioBankTransfer) {
            paymentMethod = "Chuyển khoản ngân hàng (Demo)";
        } else if (selectedPaymentId == R.id.radioEWallet) {
            paymentMethod = "Ví điện tử MoMo / ZaloPay (Demo)";
        }

        // Nếu là luồng thanh toán từ giỏ hàng thì xóa giỏ hàng
        if (!isBuyNowFlow) {
            CartManager.getInstance().clearCart();
        }

        // Tạo mã đơn hàng demo ngẫu nhiên và thời gian đặt hàng
        String timestamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        int randomSuffix = 1000 + new Random().nextInt(9000);
        String orderCode = "#GH" + timestamp + "-" + randomSuffix;
        String orderTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // Chuyển sang màn hình OrderSuccessActivity
        Intent successIntent = new Intent(this, OrderSuccessActivity.class);
        successIntent.putExtra(OrderSuccessActivity.EXTRA_ORDER_CODE, orderCode);
        successIntent.putExtra(OrderSuccessActivity.EXTRA_RECIPIENT_NAME, name);
        successIntent.putExtra(OrderSuccessActivity.EXTRA_PHONE, phone);
        successIntent.putExtra(OrderSuccessActivity.EXTRA_ADDRESS, address);
        successIntent.putExtra(OrderSuccessActivity.EXTRA_PAYMENT_METHOD, paymentMethod);
        successIntent.putExtra(OrderSuccessActivity.EXTRA_TOTAL_AMOUNT, PriceFormatter.format(totalAmount));
        successIntent.putExtra(OrderSuccessActivity.EXTRA_ORDER_TIME, orderTime);

        startActivity(successIntent);
        finish();
    }

    private String getTextValue(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }
}
