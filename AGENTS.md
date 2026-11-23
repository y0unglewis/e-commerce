# AGENTS.md — Quy tắc làm việc với source GearHub

Đây là dự án Android **Java + XML** phục vụ bài giữa kỳ, thời hạn **24/11/2026**, nhóm **3 thành viên**, ưu tiên **UI/UX** và **code dễ học, dễ giải thích**.

## Bất biến phải giữ

1. **Không sửa hoặc xóa bài thực hành ở repository khác** (`android-test` hay dự án từ vựng tiếng Nhật). Chỉ làm trong repo `y0unglewis/e-commerce`.
2. Giao diện theo bộ màu trong `app/src/main/res/values/colors.xml` và quy chuẩn `docs/UI_GUIDELINES.md`. Không thêm một hệ màu hay thư viện UI khác nếu chưa thống nhất.
3. `onCreate()` của Activity **ngắn, chỉ gọi các hàm con**. Logic rõ ràng, Java cơ bản, comment tiếng Việt khi cần.
4. Mọi khóa Intent chung đặt trong `AppConstants.java`; truyền **đối tượng `Product`** qua `AppConstants.EXTRA_PRODUCT`, không lặp string và không putExtra từng field.
5. Chỉ có một model `Product` và một lớp `GearHubDatabaseHelper`. Dữ liệu mẫu duy trì ở `SampleProducts`.
6. Không tự đổi schema/DB_VERSION; trao đổi với trưởng nhóm rồi làm migration. Không đưa file `.db`, mật khẩu, khóa API, đường dẫn SDK lên GitHub.
7. Không tự triển khai thanh toán thật, backend, Firebase hoặc chức năng ngoài phạm vi giữa kỳ.
8. Mỗi người chỉ sửa cụm màn hình được giao; nếu cần sửa file chung thì ghi rõ trong PR và báo trưởng nhóm trước.
9. **Không push trực tiếp lên `main` / `develop`, không force push**. Nhánh `feature/*` → PR vào `develop` → kiểm tra → merge, cuối cùng `develop` → PR vào `main` để nộp.
10. Không khẳng định build PASS khi chưa chạy được Gradle + app; ghi chính xác phần đã test và chưa test.

## Quyền sở hữu file (để tránh merge conflict)

| Vai trò | File chính |
| --- | --- |
| TV1 / nhóm trưởng | `HomeActivity`, `activity_home`, `ProductAdapter`, `item_product`, `database/*`, `model/Product`, `AppConstants`, `res/values/*`, Gradle, Manifest |
| TV2 | `ProductDetailActivity`, `activity_product_detail`, drawable hoặc item riêng cho chi tiết/đánh giá |
| TV3 | `CartActivity`, `CheckoutActivity`, `ProfileActivity`, layout tương ứng, thêm lớp giỏ hàng riêng |

## Trước khi hoàn tất công việc

- Build/Sync được trên Android Studio; không còn lỗi `R.id`/resource.
- Kiểm tra chuyển trang, quay lại, tìm kiếm/lọc, nút +/- (nếu liên quan).
- Đọc `git diff --check`, `git status`; chỉ commit file thuộc tính năng.
- PR có ảnh màn hình + mô tả chức năng thực tế + các bước thử.
- Nếu API chưa làm thật, ghi **demo/placeholder**, không che giấu.
