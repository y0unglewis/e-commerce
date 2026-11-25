# GearHub — Android E-commerce UI (giữa kỳ)

Ứng dụng mua sắm **phụ kiện công nghệ** được viết bằng **Java + XML** cho môn Lập trình di động. Giai đoạn giữa kỳ ưu tiên **giao diện nhất quán và luồng chuyển màn hình rõ ràng**; không có thanh toán thực tế.

> Bài mở lúc **00:00 ngày 19/11/2026**; hạn nộp **00:00 ngày 26/11/2026** (tức phải hoàn tất trong ngày **25/11/2026**).

## 1. Trạng thái hiện tại

| Màn hình / phần việc | Chức năng đã triển khai |
| --- | --- |
| Login/Register | Xác thực SQLite, PBKDF2, lưu session |
| Home | Grid 2 cột, Search, Advanced Filter, RecyclerView + ConcatAdapter |
| SQLite | Schema v5, 24 sản phẩm (6 sản phẩm/danh mục), bảng tài khoản và đánh giá |
| Product Detail | 3 tab, sản phẩm liên quan, số lượng, thêm vào giỏ và mua ngay |
| Profile | Username từ session, đăng xuất |
| Cart/Checkout/Order Success | Giỏ hàng RAM, tổng tiền, kiểm tra form, đặt hàng demo |

Ảnh sản phẩm dùng chung 24 WebP trong drawable-nodpi. Đánh giá sản phẩm được lưu cục bộ bằng SQLite; thanh toán chỉ minh họa, không có giao dịch thật hay lưu lịch sử đơn hàng. Giỏ hàng không lưu khi tiến trình bị đóng.

## 2. Công nghệ

- Android **Java** + giao diện **XML**.
- Material Components, AppCompat, RecyclerView.
- SQLite có sẵn trong Android; `GearHubDatabaseHelper` tạo database khi chạy lần đầu. Cả nhóm dùng chung **cấu trúc và dữ liệu mẫu trong source**, nhưng mỗi thiết bị có một database cục bộ riêng, **không tự đồng bộ dữ liệu qua mạng**.
- `Product implements Serializable` để truyền đối tượng bằng `Intent.putExtra(AppConstants.EXTRA_PRODUCT, product)`.
- Gradle plugin 8.7.3 / Gradle 8.9 / JDK 17, compileSdk 35.

## 3. Chạy dự án trong Android Studio

1. Cài Android Studio và SDK Platform 35. Dùng **JDK 17** cho Gradle.
2. Clone repo và mở **thư mục gốc** chứa `settings.gradle` bằng **File → Open**.
3. Chờ **Gradle Sync**; chọn emulator hoặc thiết bị Android API 24 trở lên.
4. Chạy cấu hình **app**. Màn hình mở đầu là `LoginActivity` (tự chuyển Home nếu session hợp lệ).
5. Repo có sẵn `gradlew`, `gradlew.bat` và `gradle/wrapper/gradle-wrapper.jar`. Trên Windows có thể chạy `./gradlew.bat :app:assembleDebug` sau khi cài SDK/JDK; không cần cài Gradle toàn máy. **Không tạo `local.properties` cho máy của người khác.**

> GitHub Actions có bước **build debug APK** khi push/PR. Việc build thành công chưa thay cho kiểm tra UI bằng emulator: mỗi thành viên vẫn phải Sync và chạy app trước khi gửi PR.

## 4. Nguyên tắc làm việc nhóm

- Không push thẳng `main` hoặc `develop`.
- Tạo branch `feature/<ten-man-hinh>` từ `develop`, mở Pull Request vào `develop`.
- **Không sửa file chung** (`AppConstants.java`, `Product.java`, `GearHubDatabaseHelper.java`, `colors.xml`, `themes.xml`, Gradle, Manifest) nếu chưa báo trưởng nhóm.
- `onCreate()` chỉ gọi các hàm nhỏ như `initViews()`, `setupListeners()`, `loadData()`.
- UI lấy màu từ `colors.xml`, không gõ màu hex rải rác trong layout.
- Giữ code đơn giản đúng nội dung đã học; comment tiếng Việt vào những đoạn dễ nhầm.

Hướng dẫn chi tiết: [CONTRIBUTING.md](CONTRIBUTING.md) · [Quy chuẩn UI](docs/UI_GUIDELINES.md) · [Hợp đồng SQLite](docs/DATABASE.md) · [Phân công](docs/TEAM_TASKS.md).

## 5. Luồng thử nghiệm

- Đăng ký → đăng nhập → Home → Search/Filter → Detail (3 tab, số lượng, sản phẩm liên quan).
- Detail → Thêm vào giỏ → Cart → Checkout → Order Success → Home.
- Detail → Mua ngay → Checkout → Order Success → Home (giữ nguyên giỏ hàng).
- Profile → Đăng xuất → Login; đăng nhập tài khoản khác không mang giỏ hàng cũ.

Kiểm tra dữ liệu sai, số lượng 1–99, giỏ rỗng, tổng tiền, Back và bàn phím trước khi release.

## 6. Nộp bài

Một bạn đại diện nộp:

1. Đường link repository GitHub đến commit/nhánh ổn định `main`.
2. Hình ảnh hoặc file giao diện các màn hình đã hoàn thiện.
3. Bảng ghi rõ **tên thành viên, MSSV, chức năng đã làm và file đóng góp** khi nộp bài; trong repo chỉ cần lưu **phân công theo vai trò** (xem `docs/TEAM_TASKS.md`).

Không gửi mật khẩu, token hay file `local.properties` lên GitHub.
