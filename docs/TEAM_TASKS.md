# Phân công nhóm 3 người — E-commerce GearHub

> Cập nhật theo GitHub Issues [#2](https://github.com/y0unglewis/e-commerce/issues/2)–[#6](https://github.com/y0unglewis/e-commerce/issues/6). Chỉ ghi **vai trò và tính năng thực tế**, không đưa thông tin cá nhân vào repo. Quy tắc sửa code/file chung xem [AGENTS.md](../AGENTS.md).

| Thành viên | Issues | Phụ trách | Nhánh | Trạng thái |
| --- | --- | --- | --- | --- |
| **TV1 — Nhóm trưởng** | [#2 Home/Filter](https://github.com/y0unglewis/e-commerce/issues/2), [#3 Login/Register/Session](https://github.com/y0unglewis/e-commerce/issues/3) | Foundation, Home 2 cột, Search/Advanced Filter, Login/Register demo, `SessionManager`, SQLite và file chung, tích hợp/test | `feature/home-filter`, `feature/auth-session` | Foundation đã merge; phần còn lại theo Issue |
| **TV2** | [#4 Detail](https://github.com/y0unglewis/e-commerce/issues/4), [#5 Profile](https://github.com/y0unglewis/e-commerce/issues/5) | Detail 3 tab, thông số/mô tả/đánh giá, +/- và sản phẩm liên quan; **Profile** và kết nối username/logout sau khi có SessionManager | `feature/product-detail`; Profile: `feature/profile` hoặc ghép nhánh Detail nếu đã thống nhất | Chưa bàn giao |
| **TV3** | [#6 Cart/Checkout](https://github.com/y0unglewis/e-commerce/issues/6) | Cart, số lượng/xóa/tổng tiền, Checkout demo, Order Success | `feature/cart-checkout` | Chưa bàn giao |

## File sở hữu

- **TV1:** `ui/HomeActivity.java`, `activity_home.xml`, `adapter/ProductAdapter.java`, `item_product.xml`; Login/Register và `util/SessionManager.java` mới; `AppConstants.java`, `model/Product.java`, `database/*`, `res/values/*`, Manifest/Gradle và các file chung.
- **TV2:** `ui/ProductDetailActivity.java`, `activity_product_detail.xml`, `ui/ProfileActivity.java`, `activity_profile.xml`; drawable, adapter/layout riêng cho Detail/Profile.
- **TV3:** `ui/CartActivity.java`, `activity_cart.xml`, `ui/CheckoutActivity.java`, `activity_checkout.xml`; Order Success và các class/layout riêng của giỏ hàng.
- **Chức năng giao nhau:** TV2 và TV3 thống nhất cách đưa `Product` + số lượng vào Cart/Checkout; TV1 duyệt sửa khóa Intent dùng chung. TV1 tạo `SessionManager`, TV2 tích hợp UI Profile sau khi PR này merge.

## Checklist bàn giao theo người

**TV1 — #2, #3**
- [ ] Home Grid 2 cột, Search, danh mục, Bottom Sheet Filter (danh mục/giá/sắp xếp).
- [ ] SQLite catalog dùng chung; giữ nguyên các chức năng đang hoạt động.
- [ ] Login/Register demo, validate input, `SessionManager` lưu/đọc/xóa username.
- [ ] Tích hợp UI giữa các nhánh, kiểm tra build và điều hướng; chỉ đổi launcher sang Login khi chạy ổn.

**TV2 — #4, #5**
- [ ] Detail nhận đúng đối tượng `Product` qua `AppConstants.EXTRA_PRODUCT`.
- [ ] 3 tab đều có nội dung; sản phẩm liên quan mở đúng Detail; số lượng tối thiểu 1.
- [ ] Profile đồng bộ UI, hiển thị username từ `SessionManager` khi đã có; tích hợp logout với TV1.
- [ ] PR có ảnh Detail các tab và Profile, không sửa file chung khi chưa được duyệt.

**TV3 — #6**
- [ ] Cart hiển thị sản phẩm/số lượng; +/- và xóa tính đúng tổng tiền.
- [ ] Checkout nhập thông tin nhận hàng, địa chỉ, phương thức thanh toán **demo**.
- [ ] Màn Order Success, có đường quay về Home, không crash.
- [ ] Phối hợp TV2 về dữ liệu chuyển vào giỏ hàng và Mua ngay; không tự sửa `Product`/SQLite.

## Mốc tích hợp / nộp bài

- **23/11:** Giao Issues; TV2/TV3 bắt đầu độc lập; TV1 chuẩn bị SessionManager và Home/Filter.
- **24/11:** Bản UI đầu tiên, tích hợp contract dùng chung, mở PR theo từng phần.
- **25/11:** Review, kiểm thử end-to-end, sửa conflict, ảnh chụp Login/Home/Filter/Detail/Cart/Checkout/Success/Profile; merge `develop` → `main` bằng PR.
- **Deadline: 00:00 ngày 26/11/2026** — hoàn tất trước khi kết thúc ngày 25/11.

> Không đánh dấu hoàn thành thay cho người khác. Không xóa file/code cũ hoặc đổi chức năng ngoài Issue chỉ để “dọn source”. Đọc [AGENTS.md](../AGENTS.md) trước khi sửa.
