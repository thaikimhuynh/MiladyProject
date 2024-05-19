package com.thaikimhuynh.miladyapp.model;

import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private String userId;
    private double totalAmount;
    private double shippingFee;
    private double finalAmount;
    private double discountedAmount;
    private String address;
    private String customerName;
    private String phone;
    private List<Product> products;
private String orderDate;
    private String orderStatus;


    public int getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(String customerName, String address, String phone) {
    }

    public Order() {
    }
//    public Order(String userId, double totalAmount, double shippingFee, double finalAmount, String address, String customerName, String phone, List<Product> products) {
//        this.userId = userId;
//        this.totalAmount = totalAmount;
//        this.shippingFee = shippingFee;
//        this.finalAmount = finalAmount;
//        this.address = address;
//        this.customerName = customerName;
//        this.phone = phone;
//        this.products = products;
//    }

    public double getDiscountedAmount() {
        return discountedAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setOrderId(int orderId) {
    }

    public void setDiscountedAmount(double discountedAmount) {
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
