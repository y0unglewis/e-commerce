package com.example.gearhub.model;

import java.io.Serializable;

/** Đối tượng sản phẩm dùng chung cho Home, Detail, Giỏ hàng. */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final String brand;
    private final String category;
    private final long price;
    private final long originalPrice;
    private final float rating;
    private final String imageKey;
    private final String description;
    private final String specifications;

    public Product(int id, String name, String brand, String category,
                   long price, long originalPrice, float rating,
                   String imageKey, String description, String specifications) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.rating = rating;
        this.imageKey = imageKey;
        this.description = description;
        this.specifications = specifications;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public long getPrice() { return price; }
    public long getOriginalPrice() { return originalPrice; }
    public float getRating() { return rating; }
    public String getImageKey() { return imageKey; }
    public String getDescription() { return description; }
    public String getSpecifications() { return specifications; }
}
