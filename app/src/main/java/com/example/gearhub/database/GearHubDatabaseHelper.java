package com.example.gearhub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.example.gearhub.model.Product;
import com.example.gearhub.model.Review;
import com.example.gearhub.util.PasswordHasher;
import java.util.ArrayList;
import java.util.List;

public class GearHubDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "gearhub.db";
    private static final int DB_VERSION = 4;
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_REVIEWS = "reviews";
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

        for (Product product : SampleProducts.create()) {
            ContentValues values = toValues(product);
            db.insertOrThrow(TABLE_PRODUCTS, null, values);
        }
        createUsersTable(db);
        createReviewsTable(db);
        seedSampleReviews(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        if (oldVersion < 2) {
            createUsersTable(db);
        }

        if (oldVersion < 3) {
            for (Product product : ExpandedProducts.create()) {
                db.insertWithOnConflict(
                    TABLE_PRODUCTS,
                    null,
                    toValues(product),
                    SQLiteDatabase.CONFLICT_IGNORE
                );
            }
        }

        if (oldVersion < 4) {
            createReviewsTable(db);
            seedSampleReviews(db);
        }
    }

    private void createUsersTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + "username TEXT NOT NULL PRIMARY KEY, "
                + "password_hash BLOB NOT NULL, salt BLOB NOT NULL, "
                + "algorithm TEXT NOT NULL, iterations INTEGER NOT NULL)");
    }

    private void createReviewsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REVIEWS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "product_id INTEGER NOT NULL, "
                + "username TEXT NOT NULL, "
                + "rating INTEGER NOT NULL, "
                + "comment TEXT NOT NULL, "
                + "created_at TEXT NOT NULL)");
    }

    private void seedSampleReviews(SQLiteDatabase db) {
        try (Cursor countCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REVIEWS, null)) {
            if (countCursor.moveToFirst() && countCursor.getInt(0) > 0) return;
        }
        db.beginTransaction();
        try (Cursor products = db.query(TABLE_PRODUCTS, new String[]{"id", "name", "brand"},
                null, null, null, null, "id ASC")) {
            while (products.moveToNext()) {
                int productId = products.getInt(0);
                String name = products.getString(1);
                String brand = products.getString(2);
                db.insert(TABLE_REVIEWS, null, reviewValues(productId, "Thu Hà", 5,
                        "Đúng hàng chính hãng " + brand + ", giao hàng nhanh, chất lượng vượt mong đợi trong tầm giá.",
                        "20/11/2026"));
                db.insert(TABLE_REVIEWS, null, reviewValues(productId, "Minh Quân", 5,
                        "Sản phẩm " + name + " đóng gói rất kỹ, hoàn thiện chắc chắn và dùng cực kỳ ổn định.",
                        "23/11/2026"));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Review> getReviewsByProduct(int productId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_REVIEWS, null, "product_id = ?",
                new String[]{String.valueOf(productId)}, null, null, "id DESC")) {
            int idIdx = cursor.getColumnIndexOrThrow("id");
            int productIdx = cursor.getColumnIndexOrThrow("product_id");
            int userIdx = cursor.getColumnIndexOrThrow("username");
            int ratingIdx = cursor.getColumnIndexOrThrow("rating");
            int commentIdx = cursor.getColumnIndexOrThrow("comment");
            int timeIdx = cursor.getColumnIndexOrThrow("created_at");
            while (cursor.moveToNext()) {
                reviews.add(new Review(
                        cursor.getLong(idIdx),
                        cursor.getInt(productIdx),
                        cursor.getString(userIdx),
                        cursor.getInt(ratingIdx),
                        cursor.getString(commentIdx),
                        cursor.getString(timeIdx)));
            }
        }
        return reviews;
    }

    public long addReview(int productId, String username, int rating, String comment, String createdAt) {
        int clampedRating = Math.max(1, Math.min(5, rating));
        return getWritableDatabase().insert(TABLE_REVIEWS, null,
                reviewValues(productId, username, clampedRating, comment, createdAt));
    }

    private static ContentValues reviewValues(int productId, String username, int rating, String comment, String createdAt) {
        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("username", username);
        values.put("rating", rating);
        values.put("comment", comment);
        values.put("created_at", createdAt);
        return values;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_USERS, new String[]{"username"},
                "username = ?", new String[]{username}, null, null, null)) {
            return cursor.moveToFirst();
        }
    }

    public boolean registerUser(String username, String password) {
        if (userExists(username)) return false;
        // Android 8+ dùng SHA-256; API 24-25 dùng phương án tương thích cũ.
        boolean modern = Build.VERSION.SDK_INT >= 26;
        String algorithm = modern ? PasswordHasher.SHA256 : PasswordHasher.SHA1;
        int iterations = modern ? PasswordHasher.SHA256_ITERATIONS : PasswordHasher.SHA1_ITERATIONS;
        byte[] salt = PasswordHasher.newSalt();
        byte[] hash = PasswordHasher.hash(password, salt, algorithm, iterations);
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password_hash", hash);
        values.put("salt", salt);
        values.put("algorithm", algorithm);
        values.put("iterations", iterations);
        try {
            getWritableDatabase().insertOrThrow(TABLE_USERS, null, values);
            return true;
        } catch (SQLiteConstraintException e) {
            // PRIMARY KEY chặn trùng kể cả khi hai yêu cầu đến cùng lúc.
            return false;
        }
    }

    public boolean authenticate(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        byte[] salt, hash;
        String algorithm;
        int iterations;
        try (Cursor cursor = db.query(TABLE_USERS,
                new String[]{"salt", "password_hash", "algorithm", "iterations"},
                "username = ?", new String[]{username}, null, null, null)) {
            if (!cursor.moveToFirst()) return false;
            salt = cursor.getBlob(0);
            hash = cursor.getBlob(1);
            algorithm = cursor.getString(2);
            iterations = cursor.getInt(3);
        }
        return PasswordHasher.matches(password, salt, hash, algorithm, iterations);
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
