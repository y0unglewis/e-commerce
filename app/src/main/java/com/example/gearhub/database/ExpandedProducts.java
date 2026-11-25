package com.example.gearhub.database;

import com.example.gearhub.AppConstants;
import com.example.gearhub.model.Product;
import java.util.ArrayList;
import java.util.List;

/** Dữ liệu mẫu bổ sung; giá và rating chỉ phục vụ demo. */
final class ExpandedProducts {
    private ExpandedProducts() {}

    static List<Product> create() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(13, "Tai nghe Sony WH-1000XM5", "Sony", AppConstants.CATEGORY_HEADPHONES,
                6490000, 7990000, 4.9f, "headphones", "Tai nghe chụp tai chống ồn, dành cho nghe nhạc và làm việc.",
                "Kiểu: Over-ear\nKết nối: Bluetooth\nChống ồn: ANC"));
        products.add(new Product(14, "Tai nghe JBL Tune 510BT", "JBL", AppConstants.CATEGORY_HEADPHONES,
                890000, 1190000, 4.5f, "headphones", "Tai nghe không dây nhỏ gọn cho nhu cầu hằng ngày.",
                "Kiểu: On-ear\nKết nối: Bluetooth\nPin: tối đa 40 giờ"));
        products.add(new Product(15, "Tai nghe Soundcore Space One", "Anker", AppConstants.CATEGORY_HEADPHONES,
                2190000, 2690000, 4.7f, "headphones", "Tai nghe chống ồn màu xanh nhạt, phù hợp học tập.",
                "Kiểu: Over-ear\nKết nối: Bluetooth\nChống ồn: ANC"));
        products.add(new Product(16, "Bàn phím cơ Keychron K2 V2", "Keychron", AppConstants.CATEGORY_KEYBOARDS,
                1690000, 2090000, 4.6f, "keyboard", "Bàn phím cơ nhỏ gọn, bố cục tiết kiệm diện tích.",
                "Layout: 75%\nKết nối: Bluetooth / USB-C\nLoại: Cơ"));
        products.add(new Product(17, "Bàn phím Logitech MX Keys Mini", "Logitech", AppConstants.CATEGORY_KEYBOARDS,
                2290000, 2790000, 4.8f, "keyboard", "Bàn phím mỏng nhỏ gọn cho công việc văn phòng.",
                "Kiểu: Low profile\nKết nối: Không dây\nĐèn nền: Có"));
        products.add(new Product(18, "Bàn phím Razer BlackWidow V3", "Razer", AppConstants.CATEGORY_KEYBOARDS,
                2490000, 3190000, 4.7f, "keyboard", "Bàn phím cơ gaming có đèn RGB và kê tay.",
                "Layout: Full size\nKết nối: USB\nĐèn: RGB"));
        products.add(new Product(19, "Chuột Logitech G502 HERO", "Logitech", AppConstants.CATEGORY_MICE,
                990000, 1290000, 4.8f, "mouse", "Chuột gaming có dây với nhiều nút tùy chỉnh.",
                "Kết nối: USB\nCảm biến: HERO\nKiểu: Gaming"));
        products.add(new Product(20, "Chuột Razer DeathAdder Essential", "Razer", AppConstants.CATEGORY_MICE,
                450000, 650000, 4.4f, "mouse", "Chuột gaming có dây với thiết kế công thái học.",
                "Kết nối: USB\nCảm biến: Quang\nDPI: 6400"));
        products.add(new Product(21, "Chuột Logitech M720 Triathlon", "Logitech", AppConstants.CATEGORY_MICE,
                890000, 1190000, 4.6f, "mouse", "Chuột không dây hỗ trợ chuyển đổi giữa các thiết bị.",
                "Kết nối: Bluetooth / USB receiver\nThiết bị: tối đa 3\nKiểu: Thuận tay phải"));
        products.add(new Product(22, "Củ sạc Anker 511 Nano 3 30W", "Anker", AppConstants.CATEGORY_CHARGERS,
                490000, 690000, 4.7f, "charger", "Củ sạc USB-C nhỏ gọn cho thiết bị di động.",
                "Công suất: 30W\nCổng: USB-C\nCông nghệ: GaN"));
        products.add(new Product(23, "Củ sạc UGREEN Nexode 65W", "UGREEN", AppConstants.CATEGORY_CHARGERS,
                890000, 1190000, 4.8f, "charger", "Sạc GaN nhiều cổng cho điện thoại và laptop tương thích.",
                "Công suất: 65W\nCổng: 2 USB-C, 1 USB-A\nCông nghệ: GaN"));
        products.add(new Product(24, "Pin dự phòng Anker 523 10000mAh", "Anker", AppConstants.CATEGORY_CHARGERS,
                690000, 890000, 4.5f, "charger", "Pin dự phòng mỏng gọn, dùng khi di chuyển.",
                "Dung lượng: 10000 mAh\nCổng: USB-C / USB-A\nUSB-C: tối đa 20W"));
        return products;
    }
}
