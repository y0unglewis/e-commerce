# Phân công nhóm 3 người — E-commerce GearHub

> File này chỉ theo dõi **vai trò và chức năng** trong source. Thông tin cá nhân của thành viên có thể điền riêng vào bài nộp theo yêu cầu của cô. Chỉ ghi chức năng **thực sự hoàn thành**.

| Thành viên | Chức năng | File chính | Trạng thái |
| --- | --- | --- | --- |
| **TV1 — Nhóm trưởng** | Khởi tạo project; UI chung; SQLite; Home; sản phẩm 2 cột; tìm kiếm/lọc; kiểm tra/ghép code | `HomeActivity`, `ProductAdapter`, `model/*`, `database/*`, `res/values/*` | Khung ban đầu |
| **TV2** | Product Detail; 3 tab Thông số/Mô tả/Đánh giá; nút số lượng; gợi ý sản phẩm | `ProductDetailActivity`, `activity_product_detail.xml`, layout phụ | Chưa bàn giao |
| **TV3** | Giỏ hàng; tổng tiền; Checkout mô phỏng; xác nhận đơn; Profile | `CartActivity`, `CheckoutActivity`, `ProfileActivity`, layout tương ứng | Chưa bàn giao |

## Yêu cầu giao sản phẩm

**TV1**
- [ ] Home có ít nhất 8–12 sản phẩm, 2 cột đẹp và cuộn mượt.
- [ ] Tìm kiếm + lọc hoạt động.
- [ ] SQLite lưu/đọc sản phẩm mẫu.
- [ ] Thống nhất theme/style; merge các PR; chạy app end-to-end.

**TV2**
- [ ] Detail hiển thị đúng sản phẩm chọn từ Home.
- [ ] Ba tab đều có nội dung riêng (không chỉ tab trống).
- [ ] Thay ảnh minh họa nếu có thời gian.
- [ ] Nút +/- không cho số lượng bằng 0.
- [ ] Gợi ý sản phẩm liên quan, thêm giỏ hàng/mua ngay gọi sang luồng TV3.

**TV3**
- [ ] Cart hiển thị sản phẩm và số lượng; xóa/tính lại tổng tiền.
- [ ] Checkout có địa chỉ và phương thức thanh toán *demo*.
- [ ] Có trạng thái đặt hàng thành công.
- [ ] Profile cùng style, có thông tin tài khoản mẫu.
- [ ] Chuyển trang/quay lại không crash.

## Trước hạn nộp 00:00 ngày 26/11/2026 (hoàn tất trong ngày 25/11)

- [ ] Cả 3 PR đã merge `develop`.
- [ ] Mở app được, không crash khi đi từ Home → Detail → Cart → Checkout.
- [ ] Không còn text TODO/"đang phát triển" trong ảnh giao diện nộp.
- [ ] Chụp ảnh: Home, Search/Filter, Detail (3 tab), Cart, Checkout, Profile.
- [ ] Merge bản ổn vào `main` theo PR.
- [ ] Một đại diện nộp **link GitHub**, **các file/ảnh giao diện**, **bảng phân công** trên.
