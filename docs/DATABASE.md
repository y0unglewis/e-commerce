# SQLite — Hợp đồng dữ liệu của cả nhóm

SQLite là **database cục bộ trên từng thiết bị**, không phải database chung qua mạng. Mọi người có thể pull cùng *mã nguồn và dữ liệu mẫu*, nhưng các thao tác thực hiện lúc chạy app **không tự đồng bộ** sang máy bạn khác.

## File sở hữu

- `database/GearHubDatabaseHelper.java`: chủ sở hữu **TV1**; tạo SQLite, đọc sản phẩm và xác thực tài khoản.
- `database/SampleProducts.java` + `ExpandedProducts.java`: 24 bản ghi khởi tạo lần đầu (6 sản phẩm mỗi danh mục).
- `model/Product.java`: kiểu dữ liệu dùng chung cho UI.

## Schema v4

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

## Bảng `users` (từ v2)

| Cột | Kiểu |
| --- | --- |
| `username` | TEXT NOT NULL PRIMARY KEY |
| `password_hash` | BLOB NOT NULL |
| `salt` | BLOB NOT NULL |
| `algorithm` | TEXT NOT NULL |
| `iterations` | INTEGER NOT NULL |

Mật khẩu lưu dưới dạng PBKDF2 với salt riêng; không lưu mật khẩu gốc. SessionManager chỉ lưu username. Giỏ hàng lưu trong RAM, xóa khi đăng xuất hoặc đổi tài khoản; đơn hàng và thanh toán chỉ mô phỏng, không có bảng lịch sử đơn hàng.

## Bảng `reviews` (từ v4)

| Cột | Kiểu | Ý nghĩa |
| --- | --- | --- |
| `id` | INTEGER PRIMARY KEY AUTOINCREMENT | ID tự tăng |
| `product_id` | INTEGER NOT NULL | ID sản phẩm được đánh giá |
| `username` | TEXT NOT NULL | Tên người đánh giá |
| `rating` | INTEGER NOT NULL | Điểm sao (`1..5`) |
| `comment` | TEXT NOT NULL | Nội dung nhận xét |
| `created_at` | TEXT NOT NULL | Thời gian tạo (`dd/MM/yyyy HH:mm`) |

Khởi tạo sẵn 2 đánh giá mẫu cho mỗi sản phẩm khi tạo database hoặc nâng cấp `v3 → v4`.

## Lưu ý thay đổi schema

- `onCreate(SQLiteDatabase)` chỉ gọi khi database chưa được tạo; **không chạy lại mỗi lần mở app**.
- Nếu chỉ sửa dữ liệu mẫu ở `SampleProducts`, app **đã cài trước đó không tự nhận bản ghi mới**. Cần có luồng cập nhật dữ liệu hoặc gỡ cài đặt/clear storage khi đang thử nghiệm (**sẽ xóa dữ liệu cục bộ**).
- `DB_VERSION` hiện tại là `4`: migration `v1 → v2` thêm bảng `users`; `v2 → v3` bổ sung ID `13–24` bằng `CONFLICT_IGNORE`; `v3 → v4` thêm bảng `reviews` và seed đánh giá mẫu cho từng sản phẩm (bỏ qua nếu bảng đã có dữ liệu). Không xóa database của người dùng để sửa lỗi.
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
