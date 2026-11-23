# GearHub UI Guidelines — đọc trước khi làm XML

## 1. Bảng màu (đã chốt)

| Mục đích | Resource | HEX |
| --- | --- | --- |
| Nút chính, giá, tab đang chọn | `@color/gear_primary` | `#2563EB` |
| Chữ tiêu đề | `@color/gear_text` | `#0F172A` |
| Chữ phụ | `@color/gear_muted` | `#64748B` |
| Nền app | `@color/gear_background` | `#F8FAFC` |
| Nền card | `@color/gear_surface` | `#FFFFFF` |
| Đường viền | `@color/gear_border` | `#E2E8F0` |
| Sao đánh giá | `@color/gear_accent` | `#F59E0B` |

**Không viết màu `#...` trực tiếp trong từng layout.** Nếu thiếu màu, báo nhóm trưởng thêm vào `colors.xml`.

## 2. Kích thước thống nhất

- Khoảng cách: **8dp / 16dp / 24dp**. Mép màn hình: **16dp**.
- Card: bo góc **16dp**, nền trắng, viền `gear_border` 1dp.
- Nút chính: **52dp** cao, bo góc **14dp**, xanh nền + chữ trắng.
- Tiêu đề trang: **22–24sp**, tên card: **14sp**, body: **14–16sp**, chú thích **12sp**.
- Danh sách sản phẩm: **RecyclerView GridLayoutManager(2)**, card cùng chiều ngang.
- Giá: xanh đậm, giá gốc gạch ngang; sao đánh giá màu vàng cam.
- Dùng `MaterialButton`, `MaterialCardView`, `Chip`, `TabLayout` nếu phù hợp với kiến thức đã học.

## 3. Quy ước trải nghiệm

- Back phải quay về màn trước; không dùng NEW_TASK/CLEAR_TASK khi chỉ xem sản phẩm.
- Một màn có **một hành động chính** màu xanh; hành động phụ dạng outlined.
- Nút +/- luôn giữ số lượng ít nhất 1; chặn tăng quá mức đã quy định.
- Không dùng text quá dài để giả giao diện; dữ liệu nên đọc từ `Product` / SQLite.
- Màn Detail phải có **Thông số – Mô tả – Đánh giá**, dễ bấm, trạng thái tab rõ.
- Chụp ảnh màn hình trên cùng một kích thước emulator (gợi ý Pixel 6 / API 35) để bộ ảnh nộp đồng nhất.
- Ảnh sản phẩm đang dùng vector mẫu: khi thay bằng PNG, ưu tiên nền sáng và kích thước ảnh đồng đều.

## 4. Các thành phần đã có

- `Widget.GearHub.PrimaryButton`
- `Widget.GearHub.OutlinedButton`
- `bottom_navigation.xml`
- `item_product.xml`
- `bg_product_icon.xml`, `bg_promo.xml`

Thành viên không cần định nghĩa lại style cho mỗi màn. Nếu muốn thay giao diện chung, trao đổi trước khi sửa.
