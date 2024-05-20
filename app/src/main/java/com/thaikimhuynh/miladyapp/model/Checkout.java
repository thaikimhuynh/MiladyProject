package com.thaikimhuynh.miladyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Checkout implements Parcelable {
    private String title;
    private double price;
    private String productSize;
    private int numberInCart;

    // Constructor, getters, and setters

    public Checkout(String title, double price, String productSize, int numberInCart) {
        this.title = title;
        this.price = price;
        this.productSize = productSize;
        this.numberInCart = numberInCart;
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

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    // Parcelable implementation
    protected Checkout(Parcel in) {
        title = in.readString();
        price = in.readDouble();
        productSize = in.readString();
        numberInCart = in.readInt();
    }

    public static final Creator<Checkout> CREATOR = new Creator<Checkout>() {
        @Override
        public Checkout createFromParcel(Parcel in) {
            return new Checkout(in);
        }

        @Override
        public Checkout[] newArray(int size) {
            return new Checkout[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeDouble(price);
        parcel.writeString(productSize);
        parcel.writeInt(numberInCart);
    }
}

