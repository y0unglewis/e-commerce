# Hướng dẫn Git cho nhóm 3 người (Windows PowerShell)

Đây là **lệnh gõ trực tiếp** trong Terminal Android Studio/PowerShell. **Không copy phần `PS C:\...>`** khi chạy.

## 1. Chuẩn bị một lần

Nhóm trưởng gửi link `https://github.com/y0unglewis/e-commerce` và mời hai bạn làm **Collaborator** (Settings → Collaborators). Hai bạn chấp nhận lời mời.

Lần đầu tải project về:

```powershell
git clone https://github.com/y0unglewis/e-commerce.git
cd e-commerce
git fetch origin
git switch develop
git pull --ff-only origin develop
```

Nếu `git switch develop` báo không tìm thấy nhánh tại máy, dùng:

```powershell
git switch -c develop --track origin/develop
```

## 2. Mỗi người tạo nhánh riêng (không dùng chung một nhánh)

```powershell
git switch develop
git pull --ff-only origin develop
git switch -c feature/product-detail
```

Đổi tên cuối cho đúng phần việc:

- TV1: `feature/home-catalog`
- TV2: `feature/product-detail`
- TV3: `feature/cart-checkout`

Không chạy lại `git switch -c ...` nếu nhánh đã tồn tại. Khi đó `git switch feature/product-detail` là đủ.

## 3. Sau khi sửa code

```powershell
git status
git diff --check
git add app/src/main/java app/src/main/res
git commit -m "feat: build product detail tabs"
git push -u origin feature/product-detail
```

`git add` theo ví dụ cần **xem kỹ `git status` trước**. Đừng vô tình stage các file người khác đang làm hoặc `local.properties`.

Mở GitHub → **Compare & pull request** → chọn:

- **base:** `develop`
- **compare:** nhánh feature của mình

Ghi rõ nội dung, các file sửa, ảnh giao diện và cách kiểm tra. Nhóm trưởng review và merge.

## 4. Sau khi nhánh đã merge

```powershell
git switch develop
git pull --ff-only origin develop
```

Muốn làm chức năng mới thì tạo **nhánh feature mới** từ `develop` đã cập nhật.

## 5. Tránh xung đột

- Không tự sửa `colors.xml`, `themes.xml`, `Product.java`, `DatabaseHelper`, `AppConstants`, `AndroidManifest.xml`, `build.gradle` khi chưa trao đổi.
- Nếu phát hiện cần sửa file chung: báo nhóm trưởng và ghi yêu cầu cụ thể; ưu tiên nhóm trưởng cập nhật trước cho mọi người pull.
- Mỗi Activity và file XML màn hình chỉ một người chính sửa cùng lúc.
- Nếu push bị từ chối, **không force push**; chụp `git status` và lỗi gửi nhóm trưởng.
- Nếu `git switch`/`git pull` báo local changes, **không chạy `git restore` hay `reset --hard` bừa**. Commit nhánh riêng hoặc `git stash push -m "backup before pull"` rồi mới xử lý.
- Khi giải quyết conflict, dùng chức năng Merge của Android Studio, đọc cả hai phía rồi chạy app lại.

## 6. Cài luật bảo vệ nhánh trên GitHub (nhóm trưởng)

Vào repo → **Settings → Rules → Rulesets → New ruleset → New branch ruleset** (tên mục có thể khác tùy giao diện):

1. Chọn nhánh đích `main` và `develop` (tạo ruleset riêng nếu cần).
2. Bật **Require a pull request before merging**.
3. Bật **Block force pushes** và **Restrict deletions**.
4. Với nhóm 3 người deadline gấp, có thể để số lượt approval bắt buộc = 0 hoặc 1 theo khả năng review thực tế; vẫn phải kiểm tra thủ công.
5. **Không bật required status checks** khi chưa có CI tương ứng (dễ khóa merge vào phút cuối).
6. Kiểm tra trạng thái **Active** và trường hợp tài khoản chủ repo có quyền bypass. Đảm bảo nhóm trưởng cũng tuân thủ quy trình.

> File này là **quy tắc bằng văn bản**, không tự động bật branch protection. Nhóm trưởng phải lưu Ruleset trong GitHub Settings mới có hiệu lực kỹ thuật.
