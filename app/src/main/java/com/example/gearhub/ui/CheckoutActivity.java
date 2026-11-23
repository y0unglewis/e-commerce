package com.example.gearhub.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gearhub.R;

/** Thanh toán chỉ mô phỏng cho bài giữa kỳ, không xử lý tiền thật. */
public class CheckoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setupActions();
    }

    private void setupActions() {
        findViewById(R.id.btnPlaceOrder).setOnClickListener(v ->
                Toast.makeText(this, "Đặt hàng demo thành công!", Toast.LENGTH_LONG).show());
    }
}
