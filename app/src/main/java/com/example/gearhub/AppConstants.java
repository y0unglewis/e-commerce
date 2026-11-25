package com.example.gearhub;

/**
 * Tập trung các khóa Intent và giá trị cố định ở một nơi.
 * Cả nhóm không được tự gõ chuỗi khác khi truyền Product.
 */
public final class AppConstants {
    public static final String PREF_USER_SESSION = "user_session";
    public static final String KEY_USERNAME = "authenticated_username";
    public static final String EXTRA_PRODUCT = "com.example.gearhub.EXTRA_PRODUCT";

    public static final String CATEGORY_ALL = "Tất cả";
    public static final String CATEGORY_HEADPHONES = "Tai nghe";
    public static final String CATEGORY_KEYBOARDS = "Bàn phím";
    public static final String CATEGORY_MICE = "Chuột";
    public static final String CATEGORY_CHARGERS = "Sạc";

    private AppConstants() {
        // Không tạo đối tượng từ lớp chỉ chứa hằng số.
    }
}
