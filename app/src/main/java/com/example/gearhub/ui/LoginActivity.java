package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gearhub.R;
import com.example.gearhub.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputUsername, inputPassword;
    private TextInputLayout layoutUsername, layoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
        redirectIfLoggedIn();
    }

    private void initViews() {
        inputUsername = findViewById(R.id.inputLoginUsername);
        inputPassword = findViewById(R.id.inputLoginPassword);
        layoutUsername = findViewById(R.id.layoutLoginUsername);
        layoutPassword = findViewById(R.id.layoutLoginPassword);
    }

    private void setupListeners() {
        findViewById(R.id.btnLogin).setOnClickListener(v -> login());

        findViewById(R.id.btnGoRegister).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String username = getValue(inputUsername).trim();
        String password = getValue(inputPassword);

        layoutUsername.setError(null);
        layoutPassword.setError(null);

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

        // Bản demo: kiểm tra dữ liệu nhập, chưa xác thực mật khẩu.
        SessionManager.saveUsername(this, username);
        openHome();
    }

    private void redirectIfLoggedIn() {
        String username = SessionManager.getUsername(this);

        if (!username.isEmpty()) {
            openHome();
        }
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);

        // Xóa màn Login khỏi back stack sau khi đăng nhập.
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
    }

    private String getValue(TextInputEditText input) {
        return input.getText() == null
            ? ""
            : input.getText().toString();
    }
}
