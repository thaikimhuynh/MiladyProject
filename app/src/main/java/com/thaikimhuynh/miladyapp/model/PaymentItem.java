package com.thaikimhuynh.miladyapp.model;

import android.widget.ImageView;

import java.io.Serializable;

public class PaymentItem implements Serializable {
    private String wallet_name, user_name, phoneNumber, layout;
    private String img_logo;
    private boolean isSelected;


    public PaymentItem(String wallet_name, String user_name, String phoneNumber, String img_logo, String layout) {
        this.wallet_name = wallet_name;
        this.user_name = user_name;
        this.phoneNumber = phoneNumber;
        this.layout = layout;
        this.img_logo = img_logo;
        isSelected = false;
    }

    public String getWallet_name() {
        return wallet_name;
    }

    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getImg_logo() {
        return img_logo;
    }

    public void setImg_logo(String img_logo) {
        this.img_logo = img_logo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
