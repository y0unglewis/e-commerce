package com.example.gearhub.database;

import android.content.ContentValues;
import com.example.gearhub.AppConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Sinh danh sách đánh giá mẫu đa dạng người dùng, điểm sao và ngày cho từng sản phẩm. */
final class SampleReviews {
    private static final String[] REVIEWERS = {
            "Minh Quân", "Thu Hà", "Hoàng Nam", "Ngọc Anh",
            "Gia Bảo", "Khánh Linh", "Đức Thịnh", "Thanh Trúc",
            "Quốc Huy", "Mai Phương", "Tuấn Kiệt", "Bảo Ngọc",
            "Phương Vy", "Nhật Minh", "Thảo Vy", "Trung Kiên"
    };

    private static final String[] DATES = {
            "08/11/2026", "10/11/2026", "12/11/2026", "14/11/2026",
            "16/11/2026", "18/11/2026", "20/11/2026", "22/11/2026",
            "23/11/2026", "24/11/2026"
    };

    private SampleReviews() { }

    static List<ContentValues> forProduct(int productId, String name, String brand, String category) {
        int count = (productId % 2 == 0) ? 4 : 3;
        int startDate = Math.max(0, DATES.length - count - (productId % 3));
        List<ContentValues> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String reviewer = REVIEWERS[(productId * 3 + i * 5) % REVIEWERS.length];
            int rating = ratingFor(productId, i);
            String date = DATES[Math.min(DATES.length - 1, startDate + i)];
            String comment = commentFor(i, rating, name, brand, category);
            list.add(GearHubDatabaseHelper.reviewValues(productId, reviewer, rating, comment, date));
        }
        return list;
    }

    private static int ratingFor(int productId, int index) {
        if ((productId + index) % 3 == 0 && index == 1) return 4;
        if (productId % 4 == 0 && index == 2) return 4;
        return 5;
    }

    private static String commentFor(int index, int rating, String name, String brand, String category) {
        if (rating == 4) {
            return String.format(Locale.getDefault(),
                    "%s của %s hoàn thiện đẹp, dùng thực tế ổn định; mong hộp đóng gói thêm chống sốc dày hơn.",
                    name, brand);
        }
        String[] templates = templatesForCategory(category);
        return String.format(Locale.getDefault(), templates[index % templates.length], name, brand);
    }

    private static String[] templatesForCategory(String category) {
        if (AppConstants.CATEGORY_HEADPHONES.equals(category)) {
            return new String[]{
                    "Âm thanh %1$s chi tiết, đeo êm tai khi học bài lâu.",
                    "Chống ồn và kết nối Bluetooth rất ổn định, pin dùng cả tuần mới sạc.",
                    "Đúng hàng chính hãng %2$s, mic đàm thoại rõ khi họp online.",
                    "Đóng gói %1$s cẩn thận, chuyển thiết bị nhanh và gọn nhẹ khi mang theo."
            };
        }
        if (AppConstants.CATEGORY_KEYBOARDS.equals(category)) {
            return new String[]{
                    "Cảm giác gõ trên %1$s rất đã tay, độ nảy tốt và khung chắc chắn.",
                    "Kết nối chuyển thiết bị mượt, mang đi học hoặc làm việc văn phòng đều gọn.",
                    "Hàng %2$s nguyên seal, keycap đẹp và gõ lâu không mỏi cổ tay.",
                    "Dùng %1$s hằng ngày rất ổn định, độ hoàn thiện xứng đáng trong tầm giá."
            };
        }
        if (AppConstants.CATEGORY_MICE.equals(category)) {
            return new String[]{
                    "Cầm %1$s rất ôm tay, rê chuột mượt và nút bấm êm không gây ồn.",
                    "Con lăn nhạy, dùng cả ngày làm đồ án vẫn rất thoải mái.",
                    "Đúng chuẩn %2$s chính hãng, giao nhanh và nhận tín hiệu ổn định.",
                    "Pin trên %1$s dùng rất lâu, bỏ túi mang theo laptop tiện lợi."
            };
        }
        return new String[]{
                "%1$s sạc nhanh ổn định, chạy mát máy và rất tiện mang theo balo.",
                "Tương thích tốt cho cả laptop lẫn điện thoại, tiết kiệm nhiều thời gian sạc.",
                "Phụ kiện %2$s hoàn thiện gọn đẹp, cắm chắc chắn và bảo hành rõ ràng.",
                "Dùng %1$s hằng ngày rất yên tâm, kích thước nhỏ gọn không chiếm ổ cắm."
        };
    }
}
