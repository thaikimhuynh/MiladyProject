package com.thaikimhuynh.miladyapp.model;

public class RedeemPoints {
    String title;
    String content;
    String programname;
    String pointRequired;
    String description;
    String voucherCode;
    String expiration;

    public RedeemPoints(String title, String content, String programname, String pointRequired, String description, String voucherCode, String expiration, String discount) {
        this.title = title;
        this.content = content;
        this.programname = programname;
        this.pointRequired = pointRequired;
        this.description = description;
        this.voucherCode = voucherCode;
        this.expiration = expiration;
        this.discount = discount;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    String discount;


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public String getPointRequired() {
        return pointRequired;
    }

    public void setPointRequired(String pointRequired) {
        this.pointRequired = pointRequired;
    }




    public RedeemPoints() {
    }
}
