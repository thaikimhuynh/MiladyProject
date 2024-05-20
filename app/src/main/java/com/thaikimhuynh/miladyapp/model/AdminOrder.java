package com.thaikimhuynh.miladyapp.model;

public class AdminOrder {
    String address, customerName, orderStatus;
    double finalAmount, shippingFee, discountedAmount, totalAmount;

    public AdminOrder(String address, String customerName, String orderStatus, double finalAmount, double shippingFee, double discountedAmount, double totalAmount, int orderId) {
        this.address = address;
        this.customerName = customerName;
        this.orderStatus = orderStatus;
        this.finalAmount = finalAmount;
        this.shippingFee = shippingFee;
        this.discountedAmount = discountedAmount;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
    }

    int orderId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(double discountedAmount) {
        this.discountedAmount = discountedAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public AdminOrder() {
    }
}

