package com.thaikimhuynh.miladyapp.model;

public class HelperClass {
    String PhoneNumber, Email, Password;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public HelperClass(String phoneNumber, String email, String password) {
        PhoneNumber = phoneNumber;
        Email = email;
        Password = password;
    }

    public HelperClass() {
    }
}