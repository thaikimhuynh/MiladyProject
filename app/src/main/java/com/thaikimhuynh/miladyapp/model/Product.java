package com.thaikimhuynh.miladyapp.model;

import java.util.List;

public class Product {

    private String title;
    private double price;
    private String categoryId;
    private List<String> picUrls;

    public Product() {}

    public Product(String title, double price, String categoryId, List<String> picUrls) {
        this.title = title;
        this.price = price;
        this.categoryId = categoryId;
        this.picUrls = picUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }

}