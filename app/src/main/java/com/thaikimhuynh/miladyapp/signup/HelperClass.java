package com.thaikimhuynh.miladyapp.signup;

public class HelperClass {
    String PhoneNumber, Email, Password,Id;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;}

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

    public String getId(){return Id;}
    public void setId(String id){Id=id;}



    public HelperClass(String phoneNumber, String email, String password, String id) {
        PhoneNumber = phoneNumber;
        Email = email;
        Password = password;
        Id = id;
    }
    public HelperClass() {
    }
}
