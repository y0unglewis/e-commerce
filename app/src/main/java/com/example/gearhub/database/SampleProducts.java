package com.example.gearhub.database;

import com.example.gearhub.AppConstants;
import com.example.gearhub.model.Product;
import java.util.ArrayList;
import java.util.List;

/** 12 sản phẩm mẫu; chỉ dùng để điền dữ liệu lần đầu khi tạo SQLite. */
final class SampleProducts {
    private SampleProducts() { }

    static List<Product> create() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Tai nghe Soundcore Q30", "Anker", AppConstants.CATEGORY_HEADPHONES,
                1590000, 1990000, 4.8f, "headphones", "Tai nghe chống ồn chủ động, phù hợp làm việc và học tập.",
                "Bluetooth: 5.3\nPin: 40 giờ\nSạc: USB-C\nBảo hành: 12 tháng"));
        products.add(new Product(2, "Bàn phím cơ K8 Pro", "Keychron", AppConstants.CATEGORY_KEYBOARDS,
                1890000, 2190000, 4.7f, "keyboard", "Bàn phím cơ không dây gọn gàng cho góc học tập.",
                "Layout: TKL\nKết nối: Bluetooth / USB-C\nĐèn: RGB\nBảo hành: 12 tháng"));
        products.add(new Product(3, "Chuột không dây M650", "Logitech", AppConstants.CATEGORY_MICE,
                690000, 890000, 4.9f, "mouse", "Chuột êm, thiết kế công thái học dễ sử dụng.",
                "Kết nối: Bluetooth / USB\nDPI: 2000\nPin: AA\nBảo hành: 12 tháng"));
        products.add(new Product(4, "Củ sạc nhanh Nano 65W", "Anker", AppConstants.CATEGORY_CHARGERS,
                790000, 990000, 4.8f, "charger", "Sạc nhanh USB-C nhỏ gọn cho điện thoại và laptop.",
                "Công suất: 65W\nCổng: USB-C\nCông nghệ: GaN\nBảo hành: 12 tháng"));
        products.add(new Product(5, "Tai nghe Bluetooth WH-CH520", "Sony", AppConstants.CATEGORY_HEADPHONES,
                990000, 1190000, 4.6f, "headphones", "Tai nghe không dây nhẹ, pin lâu và âm thanh cân bằng.",
                "Bluetooth: 5.2\nPin: 50 giờ\nSạc: USB-C\nBảo hành: 12 tháng"));
        products.add(new Product(6, "Bàn phím G413 SE", "Logitech", AppConstants.CATEGORY_KEYBOARDS,
                1390000, 1590000, 4.7f, "keyboard", "Bàn phím cơ có dây dành cho học tập và giải trí.",
                "Layout: Full size\nSwitch: Mechanical\nKết nối: USB\nBảo hành: 24 tháng"));
        products.add(new Product(7, "Chuột MX Anywhere 3S", "Logitech", AppConstants.CATEGORY_MICE,
                1690000, 1990000, 4.9f, "mouse", "Chuột nhỏ gọn, cuộn nhanh và đa thiết bị.",
                "Kết nối: Bluetooth\nDPI: 8000\nSạc: USB-C\nBảo hành: 12 tháng"));
        products.add(new Product(8, "Sạc GaN 30W USB-C", "UGREEN", AppConstants.CATEGORY_CHARGERS,
                390000, 490000, 4.6f, "charger", "Củ sạc USB-C tiện lợi khi mang đi học hoặc du lịch.",
                "Công suất: 30W\nCổng: USB-C\nChuẩn: PD\nBảo hành: 12 tháng"));
        products.add(new Product(9, "Tai nghe TWS Liberty 4", "Anker", AppConstants.CATEGORY_HEADPHONES,
                1790000, 2190000, 4.8f, "headphones", "Tai nghe true wireless chống ồn và thiết kế nhỏ gọn.",
                "Bluetooth: 5.3\nPin: 9 giờ + hộp\nChống ồn: ANC\nBảo hành: 12 tháng"));
        products.add(new Product(10, "Bàn phím Bluetooth K380", "Logitech", AppConstants.CATEGORY_KEYBOARDS,
                690000, 790000, 4.7f, "keyboard", "Bàn phím gọn nhẹ hỗ trợ kết nối nhiều thiết bị.",
                "Kết nối: Bluetooth\nThiết bị: 3\nPin: AAA\nBảo hành: 12 tháng"));
        products.add(new Product(11, "Chuột G304 Lightspeed", "Logitech", AppConstants.CATEGORY_MICE,
                590000, 690000, 4.8f, "mouse", "Chuột không dây nhạy, dáng nhỏ và dễ thao tác.",
                "Kết nối: 2.4 GHz\nCảm biến: HERO\nPin: AA\nBảo hành: 24 tháng"));
        products.add(new Product(12, "Sạc dự phòng 10000mAh", "Baseus", AppConstants.CATEGORY_CHARGERS,
                590000, 790000, 4.5f, "charger", "Pin dự phòng dung lượng phù hợp sử dụng hằng ngày.",
                "Dung lượng: 10000 mAh\nCổng: USB-C\nSạc nhanh: 20W\nBảo hành: 12 tháng"));
        return products;
    }
}
