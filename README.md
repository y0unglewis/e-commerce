# GearHub — Android E-commerce UI (giữa kỳ)

Ứng dụng mua sắm **phụ kiện công nghệ** được viết bằng **Java + XML** cho môn Lập trình di động. Giai đoạn giữa kỳ ưu tiên **giao diện nhất quán và luồng chuyển màn hình rõ ràng**; không có thanh toán thực tế.

> Deadline nhóm: **23:59 ngày 24/11/2026**. Repo dùng cho nhóm 3 thành viên.

## 1. Trạng thái hiện tại

| Màn hình / phần việc | Trạng thái |
| --- | --- |
| Home: sản phẩm 2 cột, tìm kiếm, lọc danh mục, bottom navigation | **Khung chạy được**, tiếp tục hoàn thiện UI |
| SQLite: bảng `products`, 12 sản phẩm mẫu | **Đã có** |
| Product Detail: ảnh, tên, giá, 3 tab, nút +/- | **Khung chạy được**, TV2 hoàn thiện |
| Giỏ hàng, Checkout, Profile | **Màn khung**, TV3 hoàn thiện |
| Thanh toán thật, đăng nhập server, đồng bộ đám mây | **Không nằm trong phạm vi giữa kỳ** |

Ảnh sản phẩm hiện là **Vector Drawable minh họa**. Các thành viên có thể bổ sung ảnh rõ nét theo hướng dẫn chung mà không thay đổi schema SQLite.

## 2. Công nghệ

- Android **Java** + giao diện **XML**.
- Material Components, AppCompat, RecyclerView.
- SQLite có sẵn trong Android; `GearHubDatabaseHelper` tạo database khi chạy lần đầu.
- `Product implements Serializable` để truyền đối tượng bằng `Intent.putExtra(AppConstants.EXTRA_PRODUCT, product)`.
- Gradle plugin 8.7.3 / Gradle 8.9 / JDK 17, compileSdk 35.

## 3. Chạy dự án trong Android Studio

1. Cài Android Studio và SDK Platform 35. Dùng **JDK 17** cho Gradle.
2. Clone repo và mở **thư mục gốc** chứa `settings.gradle` bằng **File → Open**.
3. Chờ **Gradle Sync**; chọn emulator hoặc thiết bị Android API 24 trở lên.
4. Chạy cấu hình **app**. Màn hình mở đầu là `HomeActivity`.
5. Nếu project chưa có bộ Gradle Wrapper (`gradlew`, `gradlew.bat`, `gradle-wrapper.jar`), hãy tạo bằng `gradle wrapper --gradle-version 8.9` từ máy đã cài Gradle, hoặc dùng Android Studio để bổ sung wrapper trước khi chia sẻ. **Không tạo `local.properties` thủ công cho máy của người khác.**

> Ghi chú: bản nền chưa có CI/build Android được xác nhận trong môi trường tự động. Mọi thành viên cần Sync và chạy app trên máy thật/emulator trước khi gửi PR.

## 4. Nguyên tắc làm việc nhóm

- Không push thẳng `main` hoặc `develop`.
- Tạo branch `feature/<ten-man-hinh>` từ `develop`, mở Pull Request vào `develop`.
- **Không sửa file chung** (`AppConstants.java`, `Product.java`, `GearHubDatabaseHelper.java`, `colors.xml`, `themes.xml`, Gradle, Manifest) nếu chưa báo trưởng nhóm.
- `onCreate()` chỉ gọi các hàm nhỏ như `initViews()`, `setupListeners()`, `loadData()`.
- UI lấy màu từ `colors.xml`, không gõ màu hex rải rác trong layout.
- Giữ code đơn giản đúng nội dung đã học; comment tiếng Việt vào những đoạn dễ nhầm.

Hướng dẫn chi tiết: [CONTRIBUTING.md](CONTRIBUTING.md) · [Quy chuẩn UI](docs/UI_GUIDELINES.md) · [Hợp đồng SQLite](docs/DATABASE.md) · [Phân công](docs/TEAM_TASKS.md).

## 5. Luồng thử nghiệm

Home → chọn sản phẩm → Detail → chọn tab Thông số/Mô tả/Đánh giá → bấm `+` / `−` → Mua ngay → Checkout (demo).

Home → Giỏ hàng (khung) → Checkout (demo); Home → Tài khoản (khung).

**Lưu ý:** việc bấm “Thêm vào giỏ” chưa lưu sản phẩm trong bản nền; đây là phần TV3 triển khai tiếp. Không trình bày khung giao diện như chức năng hoàn chỉnh khi nộp.

## 6. Nộp bài

Một bạn đại diện nộp:

1. Đường link repository GitHub đến commit/nhánh ổn định `main`.
2. Hình ảnh hoặc file giao diện các màn hình đã hoàn thiện.
3. Bảng ghi rõ **tên thành viên, MSSV, chức năng đã làm và file đóng góp** (mẫu trong `docs/TEAM_TASKS.md`).

Không gửi mật khẩu, token hay file `local.properties` lên GitHub.
