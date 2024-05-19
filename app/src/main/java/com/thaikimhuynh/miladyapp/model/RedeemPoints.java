package com.thaikimhuynh.miladyapp.model;

public class RedeemPoints {
    String title;
    String content;
    String programname;
    String pointRequired;
    String description;
    String voucherCode;

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
    public RedeemPoints(String title, String content, String programname, String pointRequired, String description, String voucherCode) {
        this.title = title;
        this.content = content;
        this.programname = programname;
        this.pointRequired = pointRequired;
        this.description = description;
        this.voucherCode = voucherCode;
    }



    public RedeemPoints() {
    }
}
