package com.thaikimhuynh.miladyapp.model;

public class Coupon {
    String title, content, programname,description,voucherCode;

    public Coupon(String title, String content, String programname, String description, String voucherCode) {
        this.title = title;
        this.content = content;
        this.programname = programname;
        this.description = description;
        this.voucherCode = voucherCode;
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



    public Coupon() {
    }
}
