# Phân công nhóm 3 người — E-commerce GearHub

> Cập nhật ngày 25/11/2026, phạm vi bản giữa kỳ v2. Ghi họ tên, vai trò và tính năng thực tế; MSSV ghi trong nội dung nộp bài. Quy tắc sửa code/file chung xem [AGENTS.md](../AGENTS.md).

| Thành viên | Issues | Phụ trách | Trạng thái |
| --- | --- | --- | --- |
| **TV1 — Nguyễn Thị Như Ý (Nhóm trưởng)** | [#2 Home/Filter](https://github.com/y0unglewis/e-commerce/issues/2), [#3 Login/Register/Session](https://github.com/y0unglewis/e-commerce/issues/3) | Foundation, Home 2 cột, Search/Advanced Filter, Login/Register SQLite, SessionManager, catalog 24 sản phẩm, tài nguyên dùng chung, tích hợp và kiểm thử | Đã tích hợp vào develop |
| **TV2 — Thái Duy Cường** | [#4 Detail](https://github.com/y0unglewis/e-commerce/issues/4), [#5 Profile](https://github.com/y0unglewis/e-commerce/issues/5) | Detail 3 tab, thông số/mô tả/đánh giá, số lượng và sản phẩm liên quan; Profile, username/logout; đọc và gửi đánh giá SQLite | Đã tích hợp vào develop; còn hoàn thiện đồng bộ rating |
| **TV3 — Phạm Thành Long** | [#6 Cart/Checkout](https://github.com/y0unglewis/e-commerce/issues/6) | Cart, số lượng/xóa/tổng tiền, Checkout demo, Mua ngay, Order Success, ghi chú đơn hàng; lưu giỏ hàng tạm thời bằng SharedPreferences | Đã tích hợp vào develop; còn chuyển giỏ hàng sang SQLite theo tài khoản |

## File sở hữu

- **TV1:** HomeActivity, LoginActivity, RegisterActivity, ProductAdapter, HomeHeaderAdapter và layout tương ứng; SessionManager, AppConstants, Product, database dùng chung, res/values, Manifest/Gradle và các file chung.
- **TV2:** ProductDetailActivity, ProfileActivity, Review và layout/adapter riêng; phối hợp TV1 khi bổ sung bảng reviews.
- **TV3:** CartActivity, CheckoutActivity, OrderSuccessActivity, CartItem, CartAdapter, CartManager và layout riêng.
- **Chức năng giao nhau:** TV2 và TV3 thống nhất cách truyền Product và số lượng vào Cart/Checkout. TV1 phụ trách duyệt thay đổi AppConstants, SessionManager và schema dùng chung.

## Phần đã bàn giao giữa kỳ

Các mục dưới đây xác nhận tính năng đã được tích hợp vào develop, không thay thế kết quả kiểm thử runtime của bản v2.

### TV1 — #2, #3

- [x] Home Grid 2 cột, 24 sản phẩm, tìm kiếm, danh mục và bộ lọc giá/sắp xếp.
- [x] SQLite catalog, Login/Register SQLite, validate input và lưu phiên bằng SessionManager.
- [x] Chuẩn hóa constants, strings và theme tương thích phiên bản Android.
- [x] Tích hợp luồng điều hướng, sửa lỗi UI và phối hợp kiểm thử.

### TV2 — #4, #5

- [x] Detail nhận đối tượng Product qua Intent; ba tab thông số, mô tả và đánh giá.
- [x] Sản phẩm liên quan, tăng/giảm số lượng, kết nối Cart và Mua ngay.
- [x] Profile hiển thị username và đăng xuất.
- [x] Đọc/gửi đánh giá bằng SQLite schema v4. TV1 hỗ trợ sửa số lượt đánh giá động tại PR #20.

### TV3 — #6

- [x] Cart hiển thị sản phẩm/số lượng, thêm/xóa và tính tổng tiền.
- [x] Checkout nhập thông tin nhận hàng, địa chỉ, phương thức thanh toán demo.
- [x] Ghi chú đơn hàng tùy chọn, truyền và hiển thị tại Order Success.
- [x] Order Success hiển thị thông tin đơn hàng demo và điều hướng về Home.
- [x] Lưu giỏ hàng tạm thời bằng SharedPreferences để giữ dữ liệu khi tắt app.

## Công việc phát triển tiếp sau giữa kỳ

- **TV1:** phối hợp tích hợp schema, chuẩn hóa tài nguyên và kiểm thử toàn ứng dụng.
- **TV2:** đồng bộ rating giữa Home, Detail và Related Products; bổ sung ràng buộc bảng reviews, kiểm tra phiên đăng nhập, phân biệt đánh giá mẫu và đánh giá người dùng.
- **TV3:** chuyển Cart sang SQLite theo từng tài khoản, khôi phục sau đăng nhập lại; không xóa giỏ hàng khi đăng xuất; phối hợp TV1 về schema v5 sau reviews v4 và SessionManager.

Bản giữa kỳ vẫn dùng thanh toán mô phỏng; giỏ hàng tạm thời bị xóa khi đăng xuất. Chưa coi các công việc phát triển tiếp là đã hoàn thành.

## Mốc tích hợp / nộp bài

- **23–24/11:** Phân công Issues, xây dựng giao diện và tích hợp từng chức năng.
- **25/11:** Tích hợp các PR [#22](https://github.com/y0unglewis/e-commerce/pull/22), [#20](https://github.com/y0unglewis/e-commerce/pull/20), [#24](https://github.com/y0unglewis/e-commerce/pull/24) vào develop.
- **Release v2:** Kiểm tra CI và runtime bản tích hợp, merge [PR #25](https://github.com/y0unglewis/e-commerce/pull/25) từ develop sang main bằng merge commit.
- **Nộp bài:** File giao diện từ app/src/main/res, đường dẫn source GitHub và phân công đầy đủ họ tên/MSSV trên LMS. Deadline 00:00 ngày 26/11/2026.

> Giữ lịch sử đóng góp của từng thành viên. Không đẩy trực tiếp vào main/develop, không xóa code cũ hoặc sửa ngoài phạm vi Issue. Đọc [AGENTS.md](../AGENTS.md) trước khi sửa.
