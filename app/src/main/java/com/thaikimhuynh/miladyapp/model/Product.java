package com.thaikimhuynh.miladyapp.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private String title;
    private double price;
    private String categoryId;
    private List<String> picUrls;
    private String productId;
    private String userId;
    private String getItemId;

    private String description;
    int numberInCart;
    private String productSize;

    public String getGetItemId() {
        return getItemId;
    }

    public void setGetItemId(String getItemId) {
        this.getItemId = getItemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

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


    public Product(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public Product(String productSize) {
        this.productSize = productSize;
    }

    public Product(String title, double price, String categoryId, List<String> picUrls, String productId, String description) {
        this.title = title;
        this.price = price;
        this.categoryId = categoryId;
        this.picUrls = picUrls;
        this.productId = productId;
        this.description = description;
    }

    public Product(String title, List<String> picUrls, double price) {
        this.title = title;
        this.picUrls = picUrls;
        this.price = price;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
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