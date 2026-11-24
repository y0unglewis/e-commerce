package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gearhub.R;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setupActions();
    }

    private void setupActions() {
        findViewById(R.id.btnCheckout).setOnClickListener(v ->
                startActivity(new Intent(this, CheckoutActivity.class)));
    }
}
