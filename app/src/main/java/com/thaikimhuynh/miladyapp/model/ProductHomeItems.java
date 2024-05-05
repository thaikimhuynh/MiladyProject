package com.thaikimhuynh.miladyapp.model;

public class ProductHomeItems {
    private String picUrl;
    private String title;
    private String price;

    public ProductHomeItems() {
        // Empty constructor required for Firebase
    }

    public ProductHomeItems(String picUrl, String title, String price) {
        this.picUrl = picUrl;
        this.title = title;
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
