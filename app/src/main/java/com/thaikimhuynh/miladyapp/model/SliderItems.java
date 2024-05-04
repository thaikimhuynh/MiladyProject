package com.thaikimhuynh.miladyapp.model;

public class SliderItems {
    private String url;

    public SliderItems() {
        // Empty constructor required by Firebase
    }

    public SliderItems(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
