package com.thaikimhuynh.miladyapp.model;

public class Category {

    private String id;
    private String name;
    private int imgUrl;

    // Empty constructor needed for Firebase
    public Category() {}

    // Constructor with both id and name
    public Category(String id, String name, int imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

}

