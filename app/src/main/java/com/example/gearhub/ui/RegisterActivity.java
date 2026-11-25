package com.example.gearhub.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gearhub.AppConstants;
import com.example.gearhub.R;
import com.example.gearhub.database.GearHubDatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText inputUsername, inputPassword, inputConfirm;
    private TextInputLayout layoutUsername, layoutPassword, layoutConfirm;
    private Button btnRegister, btnBackToLogin;

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
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> register());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        String username = getValue(inputUsername).trim();
        String password = getValue(inputPassword);
        String confirm = getValue(inputConfirm);
        layoutUsername.setError(null);
        layoutPassword.setError(null);
        layoutConfirm.setError(null);
        if (username.length() < AppConstants.MIN_USERNAME_LENGTH) {
            layoutUsername.setError(getString(R.string.auth_username_min, AppConstants.MIN_USERNAME_LENGTH));
            inputUsername.requestFocus();
            return;
        }
        if (password.length() < AppConstants.MIN_PASSWORD_LENGTH || password.trim().isEmpty()) {
            layoutPassword.setError(getString(R.string.auth_password_min, AppConstants.MIN_PASSWORD_LENGTH));
            inputPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            layoutConfirm.setError(getString(R.string.auth_password_mismatch));
            inputConfirm.requestFocus();
            return;
        }
        setBusy(true);
        new Thread(() -> {
            try {
                boolean created = GearHubDatabaseHelper.getInstance(this)
                        .registerUser(username, password);
                runOnUiThread(() -> completeRegistration(created));
            } catch (Exception e) {
                runOnUiThread(this::showDatabaseError);
            }
        }).start();
    }

    private void completeRegistration(boolean created) {
        if (isFinishing() || isDestroyed()) return;
        setBusy(false);
        if (!created) {
            layoutUsername.setError(getString(R.string.auth_username_exists));
            inputUsername.requestFocus();
            return;
        }
        Toast.makeText(this, getString(R.string.auth_register_success), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDatabaseError() {
        if (isFinishing() || isDestroyed()) return;
        setBusy(false);
        Toast.makeText(this, getString(R.string.auth_register_error), Toast.LENGTH_LONG).show();
    }

    private void setBusy(boolean busy) {
        btnRegister.setEnabled(!busy);
        btnBackToLogin.setEnabled(!busy);
        inputUsername.setEnabled(!busy);
        inputPassword.setEnabled(!busy);
        inputConfirm.setEnabled(!busy);
        btnRegister.setText(busy ? getString(R.string.auth_register_busy) : getString(R.string.auth_register));
    }

    private String getValue(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }
}
