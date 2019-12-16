package com.example.notification;

public class DataUser {
    String fullName;
    String email;
    int img;

    public DataUser() {

    }

    public DataUser(String fullName, String email, int img) {
        this.fullName = fullName;
        this.email = email;
        this.img = img;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
