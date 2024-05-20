package com.thaikimhuynh.miladyapp.model;

public class Yours {
    String orderStatus, orderDate, address;

    public Yours(String orderStatus, String orderDate, String address, int orderId) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.address = address;
        this.orderId = orderId;
    }

    int orderId;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Yours() {
    }
}
