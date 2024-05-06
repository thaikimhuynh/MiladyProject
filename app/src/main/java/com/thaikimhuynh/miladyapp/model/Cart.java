package com.thaikimhuynh.miladyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Cart implements Parcelable {
    private String productName;
    private double productPrice;
    private String size;
    private int quantity;

    public Cart(String productName, double productPrice, String size, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.size = size;
        this.quantity = quantity;
    }

    public Cart() {

    }

    protected Cart(Parcel in) {
        productName = in.readString();
        productPrice = in.readDouble();
        size = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeDouble(productPrice);
        dest.writeString(size);
        dest.writeInt(quantity);
    }
}
