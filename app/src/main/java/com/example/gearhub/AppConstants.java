package com.example.gearhub;

/**
 * Tập trung các khóa Intent và giá trị cố định ở một nơi.
 * Cả nhóm không được tự gõ chuỗi khác khi truyền Product.
 */
public final class AppConstants {
    public static final String PREF_USER_SESSION = "user_session";
    public static final String KEY_USERNAME = "authenticated_username";
    public static final String EXTRA_PRODUCT = "com.example.gearhub.EXTRA_PRODUCT";
    public static final String EXTRA_QUANTITY = "extra_quantity";

    // Khóa Intent dùng cho OrderSuccessActivity
    public static final String EXTRA_ORDER_CODE = "extra_order_code";
    public static final String EXTRA_RECIPIENT_NAME = "extra_recipient_name";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_ADDRESS = "extra_address";
    public static final String EXTRA_PAYMENT_METHOD = "extra_payment_method";
    public static final String EXTRA_TOTAL_AMOUNT = "extra_total_amount";
    public static final String EXTRA_ORDER_TIME = "extra_order_time";

    public static final String CATEGORY_ALL = "Tất cả";
    public static final String CATEGORY_HEADPHONES = "Tai nghe";
    public static final String CATEGORY_KEYBOARDS = "Bàn phím";
    public static final String CATEGORY_MICE = "Chuột";
    public static final String CATEGORY_CHARGERS = "Sạc";

    // Quy tắc kiểm tra tài khoản dùng chung.
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MIN_PASSWORD_LENGTH = 6;

    // Quy ước cho Cart và Detail; dùng khi tích hợp PR của TV2/TV3.
    public static final int MAX_QUANTITY = 99;
    public static final String PHONE_PATTERN = "^0[0-9]{9}$";

    private AppConstants() {
        // Không tạo đối tượng từ lớp chỉ chứa hằng số.
    }
}
