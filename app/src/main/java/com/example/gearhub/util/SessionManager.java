package com.example.gearhub.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gearhub.AppConstants;

public final class SessionManager {

    private SessionManager() {
        // Không cần tạo đối tượng SessionManager.
    }

    // Lưu username sau khi đăng nhập thành công.
    public static void saveUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(
            AppConstants.PREF_USER_SESSION,
            Context.MODE_PRIVATE
        );

        prefs.edit()
            .putString(AppConstants.KEY_USERNAME, username)
            .apply();
    }

    // Lấy username của người dùng đang đăng nhập.
    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
            AppConstants.PREF_USER_SESSION,
            Context.MODE_PRIVATE
        );

        return prefs.getString(AppConstants.KEY_USERNAME, "");
    }

    // Xóa phiên đăng nhập khi người dùng đăng xuất.
    public static void clear(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
            AppConstants.PREF_USER_SESSION,
            Context.MODE_PRIVATE
        );

        prefs.edit()
            .remove(AppConstants.KEY_USERNAME)
            .apply();
    }
}
