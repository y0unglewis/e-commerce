package com.example.gearhub.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.google.android.material.button.MaterialButton;

/**
 * Màn hình Đặt hàng thành công (OrderSuccessActivity) - Phụ trách bởi TV3.
 * Hiển thị tóm tắt đơn hàng vừa đặt và cung cấp nút điều hướng về trang chủ (HomeActivity).
 */
public class OrderSuccessActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_CODE = AppConstants.EXTRA_ORDER_CODE;
    public static final String EXTRA_RECIPIENT_NAME = AppConstants.EXTRA_RECIPIENT_NAME;
    public static final String EXTRA_PHONE = AppConstants.EXTRA_PHONE;
    public static final String EXTRA_ADDRESS = AppConstants.EXTRA_ADDRESS;
    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_PAYMENT_METHOD = AppConstants.EXTRA_PAYMENT_METHOD;
    public static final String EXTRA_TOTAL_AMOUNT = AppConstants.EXTRA_TOTAL_AMOUNT;
    public static final String EXTRA_ORDER_TIME = AppConstants.EXTRA_ORDER_TIME;

    private TextView txtOrderCode;
    private TextView txtRecipient;
    private TextView txtPhone;
    private TextView txtOrderTime;
    private TextView txtAddress;
    private TextView txtNote;
    private TextView txtPaymentMethod;
    private TextView txtTotalAmount;
    private View layoutCopyOrderCode;
    private MaterialButton btnBackToHome;
    private MaterialButton btnContinueShopping;

    private String currentOrderCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        initViews();
        displayOrderDetails();
        setupListeners();
        setupBackNavigation();
    }

    private void initViews() {
        txtOrderCode = findViewById(R.id.txtSuccessOrderCode);
        txtRecipient = findViewById(R.id.txtSuccessRecipient);
        txtPhone = findViewById(R.id.txtSuccessPhone);
        txtOrderTime = findViewById(R.id.txtSuccessOrderTime);
        txtAddress = findViewById(R.id.txtSuccessAddress);
        txtNote = findViewById(R.id.txtSuccessNote);
        txtPaymentMethod = findViewById(R.id.txtSuccessPaymentMethod);
        txtTotalAmount = findViewById(R.id.txtSuccessTotalAmount);
        layoutCopyOrderCode = findViewById(R.id.layoutCopyOrderCode);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
    }

    private void displayOrderDetails() {
        Intent intent = getIntent();
        if (intent == null) return;

        currentOrderCode = intent.getStringExtra(EXTRA_ORDER_CODE);
        String recipient = intent.getStringExtra(EXTRA_RECIPIENT_NAME);
        String phone = intent.getStringExtra(EXTRA_PHONE);
        String orderTime = intent.getStringExtra(EXTRA_ORDER_TIME);
        String address = intent.getStringExtra(EXTRA_ADDRESS);
        String note = intent.getStringExtra(EXTRA_NOTE);
        String payment = intent.getStringExtra(EXTRA_PAYMENT_METHOD);
        String total = intent.getStringExtra(EXTRA_TOTAL_AMOUNT);

        if (currentOrderCode != null && !currentOrderCode.isEmpty()) {
            txtOrderCode.setText(currentOrderCode);
        }
        if (recipient != null) txtRecipient.setText(recipient);
        if (phone != null) txtPhone.setText(phone);
        if (orderTime != null) txtOrderTime.setText(orderTime);
        if (address != null) txtAddress.setText(address);
        if (txtNote != null) {
            txtNote.setText(note == null || note.trim().isEmpty() ? "Không có" : note.trim());
        }
        if (payment != null) txtPaymentMethod.setText(payment);
        if (total != null) txtTotalAmount.setText(total);
    }

    private void setupListeners() {
        // Sao chép mã đơn hàng vào Clipboard khi chạm vào mã đơn
        if (layoutCopyOrderCode != null) {
            layoutCopyOrderCode.setOnClickListener(v -> copyOrderCodeToClipboard());
        }

        // Cả 2 nút đều quay về HomeActivity
        btnBackToHome.setOnClickListener(v -> navigateToHome());
        if (btnContinueShopping != null) {
            btnContinueShopping.setOnClickListener(v -> navigateToHome());
        }
    }

    private void copyOrderCodeToClipboard() {
        if (currentOrderCode == null || currentOrderCode.isEmpty()) return;

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Mã đơn hàng", currentOrderCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Đã sao chép mã đơn hàng: " + currentOrderCode, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBackNavigation() {
        // Xử lý điều hướng Back an toàn không quay ngược lại màn thanh toán
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        // Xóa sạch các activity đặt hàng / giỏ hàng trước đó khỏi back stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
