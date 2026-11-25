package com.example.gearhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gearhub.R;
import com.example.gearhub.util.CartManager;
import com.example.gearhub.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupMenuItems();
        setupBottomNavigation();
        findViewById(R.id.btnProfileBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnLogout).setOnClickListener(v -> confirmLogout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = username();
        if (username.isEmpty()) {
            SessionManager.clear(this);
            openLogin();
            return;
        }
        setText(R.id.txtProfileFullName, displayName(username));
        setText(R.id.txtProfileUsername, "@" + username);
        setText(findViewById(R.id.itemPersonalInfo), R.id.txtMenuSubtitle,
                "Tài khoản: @" + username + " • Đang hoạt động");
    }

    private void setupMenuItems() {
        bindMenu(R.id.itemPersonalInfo, R.drawable.ic_profile, "Thông tin cá nhân",
                "Xem hồ sơ và phiên đăng nhập hiện tại", v -> {
                    String username = username();
                    showDialog("Thông tin cá nhân", "Họ tên hiển thị: " + displayName(username)
                            + "\nUsername: @" + username + "\nHạng thành viên: Khách hàng thân thiết GearHub");
                });
        bindMenu(R.id.itemOrders, R.drawable.ic_profile_orders, "Giỏ hàng",
                "Xem sản phẩm đã chọn", v -> startActivity(new Intent(this, CartActivity.class)));
        bindMenu(R.id.itemSettings, R.drawable.ic_profile_settings, "Cài đặt",
                "Thông tin ứng dụng", v -> showDialog("Cài đặt ứng dụng",
                        "• Phiên bản: GearHub 1.0\n• Ứng dụng mua sắm mô phỏng"));
    }

    private void bindMenu(int itemId, int iconRes, String title, String subtitle, View.OnClickListener action) {
        View item = findViewById(itemId);
        ImageView icon = item.findViewById(R.id.imgMenuIcon);
        icon.setImageResource(iconRes);
        icon.setContentDescription(title);
        setText(item, R.id.txtMenuTitle, title);
        setText(item, R.id.txtMenuSubtitle, subtitle);
        item.setOnClickListener(action);
    }

    private String username() {
        return SessionManager.getUsername(this);
    }

    private String displayName(String username) {
        StringBuilder name = new StringBuilder();
        for (String part : username.replace('_', ' ').replace('.', ' ').trim().split("\\s+")) {
            if (part.isEmpty()) continue;
            if (name.length() > 0) name.append(' ');
            name.append(part.substring(0, 1).toUpperCase(Locale.getDefault())).append(part.substring(1));
        }
        return name.length() > 0 ? name.toString() : "Thành viên GearHub";
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("Đóng", null).show();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản hiện tại?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Đăng xuất", (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        SessionManager.clear(this);
        CartManager.getInstance().clearCart(this);
        Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        openLogin();
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_profile);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) finish();
            else if (id == R.id.nav_cart) startActivity(new Intent(this, CartActivity.class));
            return id == R.id.nav_home || id == R.id.nav_profile;
        });
    }

    private void setText(int viewId, CharSequence text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    private void setText(View parent, int viewId, CharSequence text) {
        ((TextView) parent.findViewById(viewId)).setText(text);
    }
}
