package com.thaikimhuynh.miladyapp.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private String title;
    private double price;
    private String categoryId;
    private List<String> picUrls;
private String productId;
private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product() {}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public Product(String title, double price, String categoryId, List<String> picUrls, String productId, String description) {
        this.title = title;
        this.price = price;
        this.categoryId = categoryId;
        this.picUrls = picUrls;
        this.productId = productId;
        this.description = description;
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