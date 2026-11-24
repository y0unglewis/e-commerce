package com.example.gearhub.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gearhub.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText inputUsername, inputPassword, inputConfirm;
    private TextInputLayout layoutUsername, layoutPassword, layoutConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();
    }

    private void initViews() {
        inputUsername = findViewById(R.id.inputRegisterUsername);
        inputPassword = findViewById(R.id.inputRegisterPassword);
        inputConfirm = findViewById(R.id.inputConfirmPassword);

        layoutUsername = findViewById(R.id.layoutRegisterUsername);
        layoutPassword = findViewById(R.id.layoutRegisterPassword);
        layoutConfirm = findViewById(R.id.layoutConfirmPassword);
    }

    private void setupListeners() {
        findViewById(R.id.btnRegister)
            .setOnClickListener(v -> registerDemo());

        findViewById(R.id.btnBackToLogin)
            .setOnClickListener(v -> finish());
    }

    private void registerDemo() {
        String username = getValue(inputUsername).trim();
        String password = getValue(inputPassword);
        String confirm = getValue(inputConfirm);

        layoutUsername.setError(null);
        layoutPassword.setError(null);
        layoutConfirm.setError(null);

        if (username.length() < 3) {
            layoutUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
            inputUsername.requestFocus();
            return;
        }

        if (password.length() < 6) {
            layoutPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            inputPassword.requestFocus();
            return;
        }

        if (!password.equals(confirm)) {
            layoutConfirm.setError("Mật khẩu xác nhận không khớp");
            inputConfirm.requestFocus();
            return;
        }

        // Chỉ kiểm tra biểu mẫu, chưa tạo tài khoản trong CSDL.
        Toast.makeText(
            this,
            "Biểu mẫu hợp lệ (chế độ demo)",
            Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    private String getValue(TextInputEditText input) {
        return input.getText() == null
            ? ""
            : input.getText().toString();
    }
}
