package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gearhub.R;
import com.example.gearhub.database.GearHubDatabaseHelper;
import com.example.gearhub.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText inputUsername, inputPassword;
    private TextInputLayout layoutUsername, layoutPassword;
    private Button btnLogin, btnGoRegister;

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
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> login());
        btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
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
        if (password.length() < 6 || password.trim().isEmpty()) {
            layoutPassword.setError("Mật khẩu phải có ít nhất 6 ký tự và không được chỉ có khoảng trắng");
            inputPassword.requestFocus();
            return;
        }
        setBusy(true);
        // SQLite và băm mật khẩu không chạy trên UI thread.
        new Thread(() -> {
            try {
                boolean valid = GearHubDatabaseHelper.getInstance(this)
                        .authenticate(username, password);
                runOnUiThread(() -> completeLogin(username, valid));
            } catch (Exception e) {
                runOnUiThread(this::showDatabaseError);
            }
        }).start();
    }

    private void completeLogin(String username, boolean valid) {
        if (isFinishing() || isDestroyed()) return;
        setBusy(false);
        if (!valid) {
            layoutPassword.setError("Tên đăng nhập hoặc mật khẩu không đúng");
            inputPassword.requestFocus();
            return;
        }
        SessionManager.saveUsername(this, username);
        openHome();
    }

    private void redirectIfLoggedIn() {
        String username = SessionManager.getUsername(this);
        if (username.isEmpty()) return;
        setBusy(true);
        new Thread(() -> {
            try {
                boolean exists = GearHubDatabaseHelper.getInstance(this).userExists(username);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    setBusy(false);
                    if (exists) openHome();
                    else SessionManager.clear(this);
                });
            } catch (Exception e) {
                runOnUiThread(this::showDatabaseError);
            }
        }).start();
    }

    private void showDatabaseError() {
        if (isFinishing() || isDestroyed()) return;
        setBusy(false);
        Toast.makeText(this, "Không thể kiểm tra tài khoản. Hãy thử lại.", Toast.LENGTH_LONG).show();
    }

    private void setBusy(boolean busy) {
        btnLogin.setEnabled(!busy);
        btnGoRegister.setEnabled(!busy);
        inputUsername.setEnabled(!busy);
        inputPassword.setEnabled(!busy);
        btnLogin.setText(busy ? "Đang kiểm tra..." : "Đăng nhập");
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String getValue(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }
}
