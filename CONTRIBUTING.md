# Hướng dẫn Git cho nhóm 3 người (Windows PowerShell)

Đây là **lệnh gõ trực tiếp** trong Terminal Android Studio/PowerShell. **Không copy phần `PS C:\\...>`** khi chạy. Trước khi code, đọc [AGENTS.md](AGENTS.md) và [docs/TEAM_TASKS.md](docs/TEAM_TASKS.md). Chỉ sửa file thuộc Issue được giao; **không tự xóa, đổi tên, format lại hoặc thay code của thành viên khác**.

## 1. Chuẩn bị một lần

Nhóm trưởng mời hai bạn làm Collaborator tại GitHub Settings; chấp nhận lời mời trước khi push.

```powershell
git clone https://github.com/y0unglewis/e-commerce.git
cd e-commerce
git fetch origin
git switch develop
git pull --ff-only origin develop
```

Nếu `git switch develop` không tìm thấy nhánh local:

```powershell
git switch -c develop --track origin/develop
```

## 2. Mỗi Issue trên nhánh của mình

Từ `develop` đã cập nhật, khi `git status` sạch:

```powershell
git switch develop
git pull --ff-only origin develop
git switch -c feature/product-detail
```

Tên nhánh theo Issue:
- TV1: `feature/home-filter` (#2), `feature/auth-session` (#3).
- TV2: `feature/product-detail` (#4), `feature/profile` (#5); nếu gộp Profile vào nhánh Detail, phải thống nhất và ghi đủ hai Issue trong PR.
- TV3: `feature/cart-checkout` (#6).

**Không chạy `git switch -c` lại nếu nhánh đã có**; khi đó dùng `git switch <tên-nhánh>`. Không dùng chung nhánh với thành viên khác.

## 3. Commit chính xác các file của mình

```powershell
git status
git diff --check
git add app/src/main/java/com/example/gearhub/ui/ProductDetailActivity.java
git add app/src/main/res/layout/activity_product_detail.xml
git diff --cached --name-status
git diff --cached
git commit -m "feat: improve product detail"
git push -u origin feature/product-detail
```

Các lệnh `git add` ở trên **chỉ là ví dụ cho TV2**. Mỗi người thêm đúng file của mình, gồm file mới nếu có. **Không stage bừa bằng `git add .`, `git add -A` hoặc cả thư mục `app/src/main/java`/`res`**, vì có thể commit file người khác hoặc file bị xóa nhầm. Kiểm tra `git diff --cached --name-status`: nếu thấy file bị xóa/đổi tên ngoài Issue, dừng lại và hỏi nhóm trưởng.

Tạo GitHub PR với **base: `develop`** và **compare: feature branch**. Ghi Issue liên quan, screenshot, danh sách file, chức năng đã test/chưa test. Nhóm trưởng review và merge.

## 4. Cập nhật thay đổi mới của nhóm

Sau khi PR đã merge:

```powershell
git switch develop
git pull --ff-only origin develop
```

Nếu đang làm nhánh riêng và cần cập nhật code chung, **chỉ khi working tree sạch**:

```powershell
git switch feature/product-detail
git fetch origin
git merge origin/develop
```

Nếu có xung đột, đọc đầy đủ cả hai bên, hỏi **người sở hữu file** trước khi quyết định. Không bấm “Accept Yours/Theirs” cho mọi file rồi coi là xong. Sau xử lý phải build/test lại.

## 5. Những điều không được làm khi chưa thống nhất

- Không push trực tiếp `main`/`develop`; không force push; không tự merge PR của bạn khác.
- Không sửa `AppConstants.java`, `model/Product.java`, `database/*`, `AndroidManifest.xml`, `colors.xml`, `themes.xml`, Gradle, menu/resource chung. Nếu cần, mô tả yêu cầu để TV1 cập nhật hoặc duyệt trước.
- Không xóa Activity/layout/drawable, code cũ, dữ liệu mẫu hoặc refactor file nằm ngoài Issue.
- Không tạo thêm `Product`, DatabaseHelper, `SessionManager` hoặc khóa Intent tùy tiện.
- Không dùng `git reset --hard`, `git clean -fd`, `git restore .` hay `git checkout -- .` khi chưa hiểu và sao lưu thay đổi cục bộ.
- Nếu `git switch`/`git pull` báo local changes, **không dùng lệnh hủy thay đổi để ép chạy**; commit đúng nhánh hoặc stash có tên, rồi mới cập nhật.
- Nếu push bị từ chối, không force; dùng `git fetch`, đọc lỗi và báo nhóm trưởng.

## 6. Ruleset bảo vệ nhánh (nhóm trưởng)

GitHub Settings → Rules → Rulesets: đặt luật active cho `main` và `develop`, yêu cầu PR, chặn force push và xóa nhánh. Số approval/status checks điều chỉnh theo khả năng nhóm; kiểm tra thực tế quyền bypass. Tài liệu này không tự bật cấu hình GitHub.

**Trước khi bàn giao:** Gradle Sync/build thành công, thử luồng trên emulator, `git status` sạch sau commit, PR có ảnh + các bước test + ghi rõ phần demo/placeholder. Chi tiết coding rules nằm trong [AGENTS.md](AGENTS.md).
