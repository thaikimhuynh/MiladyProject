package com.thaikimhuynh.miladyapp.model;
import java.io.Serializable;

public class Cart implements Serializable {
    String productID;
    String productName;
    double productPrice;
    int quantity;

    public Cart () {

    }

    public Cart(String productID, String productName, double productPrice, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}