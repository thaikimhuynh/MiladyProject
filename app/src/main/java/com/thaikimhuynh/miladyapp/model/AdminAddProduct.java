package com.thaikimhuynh.miladyapp.model;

public class AdminAddProduct {
    private String id;
    private String title;
    private float price;
    private String description;
    private String category_id;
    private int stock;


    public AdminAddProduct(String productId, String productName, float price, String description, String categoryId, int stock, String imageLink) {
    }

    public void AddminAddProduct(String id, String title, float price, String description, String category_id, int stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category_id = category_id;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
