package com.example.gearhub.model;

/** Đánh giá sản phẩm của người dùng lưu trong bảng SQLite reviews. */
public class Review {
    private final long id;
    private final int productId;
    private final String username;
    private final int rating;
    private final String comment;
    private final String createdAt;

    public Review(long id, int productId, String username, int rating, String comment, String createdAt) {
        this.id = id;
        this.productId = productId;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
