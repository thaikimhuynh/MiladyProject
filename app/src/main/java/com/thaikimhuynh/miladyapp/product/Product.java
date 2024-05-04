package com.thaikimhuynh.miladyapp.product;

import java.util.Map;

public class Product {
    private String title;
    private int price;
    private Map<String, String> picUrl;
    private String description;
    private String category_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Map<String, String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(Map<String, String> picUrl) {
        this.picUrl = picUrl;
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

    public Product(String title, int price, Map<String, String> picUrl, String description, String category_id) {
        this.title = title;
        this.price = price;
        this.picUrl = picUrl;
        this.description = description;
        this.category_id = category_id;
    }
}
