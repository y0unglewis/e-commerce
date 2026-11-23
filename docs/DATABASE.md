# SQLite — Hợp đồng dữ liệu của cả nhóm

SQLite là **database cục bộ trên từng thiết bị**, không phải database chung qua mạng. Mọi người có thể pull cùng *mã nguồn và dữ liệu mẫu*, nhưng các thao tác thực hiện lúc chạy app **không tự đồng bộ** sang máy bạn khác.

## File sở hữu

- `database/GearHubDatabaseHelper.java`: chủ sở hữu **TV1**; tạo SQLite, đọc sản phẩm.
- `database/SampleProducts.java`: 12 bản ghi khởi tạo lần đầu.
- `model/Product.java`: kiểu dữ liệu dùng chung cho UI.

## Schema v1

Bảng `products`:

| Cột | Kiểu | Ý nghĩa |
| --- | --- | --- |
| `id` | INTEGER PRIMARY KEY | ID cố định |
| `name` | TEXT NOT NULL | Tên hiển thị |
| `brand` | TEXT NOT NULL | Thương hiệu |
| `category` | TEXT NOT NULL | Danh mục |
| `price` | INTEGER NOT NULL | Giá VND, không dùng float |
| `original_price` | INTEGER NOT NULL | Giá gốc VND |
| `rating` | REAL NOT NULL | Điểm sao |
| `image_key` | TEXT NOT NULL | Khóa ảnh, ví dụ `headphones` |
| `description` | TEXT NOT NULL | Mô tả ngắn |
| `specifications` | TEXT NOT NULL | Thông số, ngăn dòng bằng `\n` |

TV2/TV3 nên gọi `GearHubDatabaseHelper.getInstance(context).getAllProducts()` **trong background thread**, sau đó cập nhật UI trên main thread. Không tạo lớp SQLite khác. Trên bản nền, Home đã có ví dụ cụ thể.

## Lưu ý thay đổi schema

- `onCreate(SQLiteDatabase)` chỉ gọi khi database chưa được tạo; **không chạy lại mỗi lần mở app**.
- Nếu chỉ sửa dữ liệu mẫu ở `SampleProducts`, app **đã cài trước đó không tự nhận bản ghi mới**. Cần có luồng cập nhật dữ liệu hoặc gỡ cài đặt/clear storage khi đang thử nghiệm (**sẽ xóa dữ liệu cục bộ**).
- Không tự ý tăng `DB_VERSION`: bản v1 chưa có migration, `onUpgrade` sẽ báo lỗi có chủ ý để tránh âm thầm xóa dữ liệu. Nhóm trưởng phải thêm migration nếu thay schema.
- Đừng commit file `gearhub.db`; `*.db` đã nằm trong `.gitignore`.

## Xem database bằng Android Studio

Chạy app trên emulator → **View → Tool Windows → App Inspection → Database Inspector** → chọn tiến trình `com.example.gearhub` → `gearhub.db` → bảng `products`. **Mở Home ít nhất một lần** để database được tạo.

## Quy ước Intent

```java
Intent intent = new Intent(this, ProductDetailActivity.class);
intent.putExtra(AppConstants.EXTRA_PRODUCT, product);
startActivity(intent);
```

`Product` triển khai `Serializable`; màn nhận đọc đúng khóa `AppConstants.EXTRA_PRODUCT`. Không tạo chuỗi `"product"`, `"PRODUCT"`, ... mới.
