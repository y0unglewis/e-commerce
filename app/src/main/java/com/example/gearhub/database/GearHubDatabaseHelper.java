package com.example.gearhub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.gearhub.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Một nơi duy nhất quản lý cấu trúc SQLite của cả nhóm.
 * Thành viên 2/3 gọi hàm đọc dữ liệu; không tự tạo thêm DatabaseHelper.
 */
public class GearHubDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "gearhub.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_PRODUCTS = "products";
    private static GearHubDatabaseHelper instance;

    public static synchronized GearHubDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new GearHubDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private GearHubDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + "id INTEGER PRIMARY KEY, "
                + "name TEXT NOT NULL, brand TEXT NOT NULL, category TEXT NOT NULL, "
                + "price INTEGER NOT NULL, original_price INTEGER NOT NULL, "
                + "rating REAL NOT NULL, image_key TEXT NOT NULL, "
                + "description TEXT NOT NULL, specifications TEXT NOT NULL)");

        // onCreate của SQLite chỉ chạy khi database chưa tồn tại.
        for (Product product : SampleProducts.create()) {
            ContentValues values = toValues(product);
            db.insertOrThrow(TABLE_PRODUCTS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Bản khung v1 chưa có thay đổi schema. Khi thay cấu trúc cần viết migration,
        // không DROP bảng trong ứng dụng thật vì sẽ làm mất dữ liệu người dùng.
        throw new IllegalStateException("Chưa có migration SQLite từ " + oldVersion + " lên " + newVersion);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, "id ASC")) {
            while (cursor.moveToNext()) {
                products.add(fromCursor(cursor));
            }
        }
        return products;
    }

    private static ContentValues toValues(Product p) {
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("name", p.getName());
        values.put("brand", p.getBrand());
        values.put("category", p.getCategory());
        values.put("price", p.getPrice());
        values.put("original_price", p.getOriginalPrice());
        values.put("rating", p.getRating());
        values.put("image_key", p.getImageKey());
        values.put("description", p.getDescription());
        values.put("specifications", p.getSpecifications());
        return values;
    }

    private static Product fromCursor(Cursor c) {
        return new Product(
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getString(c.getColumnIndexOrThrow("brand")),
                c.getString(c.getColumnIndexOrThrow("category")),
                c.getLong(c.getColumnIndexOrThrow("price")),
                c.getLong(c.getColumnIndexOrThrow("original_price")),
                c.getFloat(c.getColumnIndexOrThrow("rating")),
                c.getString(c.getColumnIndexOrThrow("image_key")),
                c.getString(c.getColumnIndexOrThrow("description")),
                c.getString(c.getColumnIndexOrThrow("specifications")));
    }
}
