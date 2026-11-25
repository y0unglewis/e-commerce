# AGENTS.md — Quy tắc code và phối hợp nhóm GearHub

GearHub là đồ án Android **Java + XML**, nhóm **3 thành viên**, hạn nộp **00:00 ngày 26/11/2026** (phải hoàn tất trong ngày 25/11). Ưu tiên UI/UX hoàn chỉnh, thao tác chạy được, code đúng kiến thức đã học và giải thích được khi giảng viên hỏi.

**Nguồn phân công chính thức:** Issues [#2 Home/Filter](https://github.com/y0unglewis/e-commerce/issues/2), [#3 Login/Register/Session](https://github.com/y0unglewis/e-commerce/issues/3), [#4 Product Detail](https://github.com/y0unglewis/e-commerce/issues/4), [#5 Profile](https://github.com/y0unglewis/e-commerce/issues/5), [#6 Cart/Checkout](https://github.com/y0unglewis/e-commerce/issues/6). Xem thêm [docs/TEAM_TASKS.md](docs/TEAM_TASKS.md), [docs/UI_GUIDELINES.md](docs/UI_GUIDELINES.md), [docs/DATABASE.md](docs/DATABASE.md) và [CONTRIBUTING.md](CONTRIBUTING.md).

## 1. Phân công theo Issues mới nhất

| Vai trò | Issues / nhánh | Công việc phụ trách | File được chủ động sửa |
| --- | --- | --- | --- |
| **TV1 — Nhóm trưởng** | #2 `feature/home-filter`; #3 `feature/auth-session` | Home, Search, Filter Bottom Sheet; UI Login/Register demo; `SessionManager`; SQLite/model/dữ liệu mẫu, design system, tích hợp PR | `HomeActivity.java`, `activity_home.xml`, `ProductAdapter.java`, `item_product.xml`, các file Home/Auth mới, `util/SessionManager.java`, `database/*`, `model/Product.java`, `AppConstants.java`, `res/values/*`, cấu hình/Manifest |
| **TV2** | #4 `feature/product-detail`; #5 `feature/profile` (hoặc cùng nhánh Detail nếu thống nhất) | Product Detail, 3 tab Thông số/Mô tả/Đánh giá, ảnh/giá/số lượng, sản phẩm liên quan; **Profile**, username và nút đăng xuất khi có SessionManager | `ProductDetailActivity.java`, `activity_product_detail.xml`, `ProfileActivity.java`, `activity_profile.xml`, layout/drawable riêng Detail/Profile |
| **TV3** | #6 `feature/cart-checkout` | Cart, tăng/giảm/xóa sản phẩm và tổng tiền, Checkout demo, Order Success, luồng đặt hàng | `CartActivity.java`, `CheckoutActivity.java`, layout tương ứng; Activity/layout Order Success và lớp giỏ hàng **riêng** được thống nhất |

**Lưu ý:** Các màn hiện có trên `develop` là **khung nền**. Thành viên được thiết kế lại UI **trong phạm vi mình phụ trách**, không phải giữ nguyên màn khung; nhưng **không được xóa/ghi đè** phần hoạt động của thành viên khác. **Profile thuộc TV2, không thuộc TV3.**

## 2. Quy tắc giữ an toàn code, tránh mất bài và merge conflict

1. **Chỉ sửa file thuộc Issue/nhánh của mình.** Trước khi sửa, đọc file hiện tại và kiểm tra `git status`, `git diff` để không ghi đè thay đổi sẵn có.
2. **Không xóa file, Activity, layout, drawable, hàm, dữ liệu mẫu hoặc code đang chạy chỉ vì thấy “dư”.** Không thay thế nguyên màn hình của người khác, không dọn dẹp/refactor toàn project trong PR tính năng. Nếu thật sự cần xóa/đổi tên: giải thích ảnh hưởng, báo nhóm trưởng, làm thay đổi nhỏ có review.
3. **File dùng chung do TV1 điều phối:** `AppConstants.java`, `model/Product.java`, `database/GearHubDatabaseHelper.java`, `database/SampleProducts.java`, `AndroidManifest.xml`, `res/values/*`, `res/menu/*` dùng chung, Gradle, `AGENTS.md`, tài liệu chung. TV2/TV3 **không tự sửa** các file này; nêu rõ nhu cầu trong Issue/PR để TV1 thực hiện hoặc duyệt trước.
4. **Không tạo class/resource trùng tên hoặc hệ thống dùng chung thứ hai:** chỉ một `Product`, một `GearHubDatabaseHelper`, một nguồn hằng số `AppConstants`, một `SessionManager` khi được thêm. Không tự tạo database mới, bảng/phiên bản DB khác hoặc SharedPreferences username riêng.
5. **File mới phải có tên riêng và đúng màn phụ trách** (ví dụ `item_related_product.xml`, `activity_order_success.xml`). Nếu cần thêm resource dùng chung, hỏi TV1 trước; **không sửa `item_product.xml` của Home** chỉ để phục vụ Detail/Cart.
6. **Phối hợp chức năng giao nhau trước khi code:** TV2–TV3 thống nhất API “Thêm vào giỏ” / “Mua ngay”, `Product` + số lượng, nơi lưu giỏ; TV1 duyệt khóa Intent dùng chung trong `AppConstants`. TV1 tạo `SessionManager` trước; TV2 đọc username và tích hợp đăng xuất sau khi PR đó merge.
7. **Không thay đổi nghiệp vụ hiện hữu ngoài Issue.** Nếu sửa bug trong file khác, ghi rõ lý do và xin thống nhất; ưu tiên tách PR nhỏ. Không âm thầm xóa code cũ hoặc đổi luồng navigation.
8. Không commit `local.properties`, `*.db`, mật khẩu, API key, cấu hình máy cá nhân hoặc file build. Không lưu password dạng plaintext trong SharedPreferences. Không thêm thanh toán thật, backend/Firebase hay thư viện vượt yêu cầu môn học.

## 3. Quy tắc Java, Activity, Intent và dữ liệu

- `onCreate()` **ngắn**: gọi `setContentView()` rồi tách `initViews()`, `setupListeners()`, `loadData()`, `displayData()`...; không nhét toàn bộ logic vào một hàm. Comment **tiếng Việt** khi cần; Java dễ đọc, đúng phạm vi đã học.
- Dùng `Intent` để chuyển Activity. Khi truyền sản phẩm, truyền **cả đối tượng `Product`** (đã `implements Serializable`), **không** `putExtra()` riêng tên/giá/hãng:

  ```java
  Intent intent = new Intent(this, ProductDetailActivity.class);
  intent.putExtra(AppConstants.EXTRA_PRODUCT, product);
  startActivity(intent);
  ```

- Activity nhận dùng `getSerializableExtra(AppConstants.EXTRA_PRODUCT)` và **kiểm tra null/kiểu** trước khi ép kiểu. Các khóa Intent bổ sung (ví dụ số lượng) phải được TV1 thêm vào `AppConstants`; giá trị đơn giản như `int` có thể truyền riêng bằng khóa chung.
- `SharedPreferences` của phiên đăng nhập chỉ đi qua `SessionManager` do TV1 phụ trách: Login lưu username; Profile đọc username và đăng xuất, **không truyền username qua mọi Activity**. Trong lúc chưa có SessionManager, TV2 có thể dựng UI Profile bằng dữ liệu mẫu, không tự phát minh session API.
- SQLite dùng `GearHubDatabaseHelper` và `SampleProducts` hiện có; không truy vấn tốn thời gian trên UI thread. **Không tự đổi schema, `DB_VERSION` hay xóa dữ liệu người dùng**. Nếu cần migration, TV1 điều phối.
- Số lượng sản phẩm tối thiểu 1; tính tổng theo giá `long`; Back điều hướng đúng, không xóa back stack tùy tiện; nút demo phải có trạng thái/feedback đúng.

## 4. Quy tắc UI chung

- Sử dụng `@color/gear_*`, theme/style và tài liệu `docs/UI_GUIDELINES.md`. Không hardcode hệ màu mới, không tự đổi theme toàn app.
- Có thể dùng Material Components đã có; không tự ý thêm dependency UI mới. Dữ liệu sản phẩm lấy từ `Product`/SQLite thay vì hardcode một catalog thứ hai.
- Các màn riêng có thể khác bố cục nhưng phải thống nhất typography, khoảng cách, bo góc, navigation và hành vi nút.

## 5. Git — quy trình bắt buộc để không xung đột

1. **Không push trực tiếp `main`/`develop`, không force push.** Từ `develop` đã cập nhật → tạo `feature/*` của mình → sửa đúng Issue → PR về `develop` → review/build → merge. Cuối cùng `develop` → PR vào `main`.
2. **Không làm việc trên cùng một nhánh hoặc cùng một file màn hình cùng lúc.** Nếu cần chung file, thỏa thuận ai sửa/merge trước; người còn lại cập nhật `develop` rồi mới tích hợp.
3. **Không dùng bừa** `git reset --hard`, `git clean -fd`, `git restore .`, `git checkout -- .` hoặc xóa thư mục project khi chưa sao lưu và hiểu hậu quả; các lệnh này có thể làm mất thay đổi chưa commit.
4. Trước khi switch/pull/merge: kiểm tra `git status`; commit thay đổi trên nhánh riêng hoặc stash có tên nếu cần. Cập nhật nhánh đang làm bằng `git fetch origin` rồi `git merge origin/develop` khi working tree sạch.
5. **Không `git add .` hoặc stage cả thư mục src theo thói quen.** Dùng `git add <đường-dẫn-file-mình-sửa>`, kiểm tra `git diff --cached --name-status` để chắc chắn không stage code người khác hoặc file bị xóa ngoài ý muốn.
6. Nếu xảy ra conflict: **dừng lại, chụp `git status` và đoạn conflict, trao đổi với người sở hữu file**; không chọn máy móc “Accept Yours/Theirs” vì có thể mất code. Giải quyết từng thay đổi, build/test lại rồi mới commit.
7. PR phải gắn Issue, mô tả file đã sửa, chức năng thực tế, ảnh màn hình, bước test và phần chưa làm. **Không tự merge PR của người khác**; TV1 review và quyết định merge.

## 6. Trước khi báo hoàn thành

- [ ] Đã đọc/tuân thủ đúng Issue và file thuộc quyền phụ trách.
- [ ] `git status`, `git diff`, `git diff --check`; không có xóa/đổi tên file ngoài phạm vi.
- [ ] Gradle Sync/build thành công; thử trên emulator; không crash khi mở/chuyển/quay lại màn.
- [ ] Nút, tab, số lượng, lọc, tổng tiền, session (nếu liên quan) hoạt động phù hợp mô tả.
- [ ] PR đính kèm ảnh, hướng dẫn thử; ghi rõ chức năng demo/placeholder, **không nhận đã PASS khi chưa test**.
