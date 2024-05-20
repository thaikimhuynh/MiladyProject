package com.thaikimhuynh.miladyapp.model;

import java.io.Serializable;

public class Feedback implements Serializable {
    private int FeedbackId;
    private String userId;
    private String productId;
    private int RatingStar;
    private String feedbackContent;
    private String userName;

    public Feedback(int feedbackId, String userId, String productId, int ratingStar, String feedbackContent, String userName) {
        FeedbackId = feedbackId;
        this.userId = userId;
        this.productId = productId;
        RatingStar = ratingStar;
        this.feedbackContent = feedbackContent;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Feedback() {
    }

    public int getFeedbackId() {
        return FeedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        FeedbackId = feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = String.valueOf(userId);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getRatingStar() {
        return RatingStar;
    }

    public void setRatingStar(int ratingStar) {
        RatingStar = ratingStar;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
