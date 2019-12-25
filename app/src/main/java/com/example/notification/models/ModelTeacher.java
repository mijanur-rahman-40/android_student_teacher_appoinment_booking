package com.example.notification.models;

import java.io.Serializable;

public class ModelTeacher implements Serializable {
    private String fullName;
    private String dept;
    private String designation;
    private String email;
    private String token;
    private String userType;
    private String imageLink;


    public ModelTeacher() {

    }

    public ModelTeacher(String fullName, String dept, String designation, String email, String token, String userType, String imageLink) {
        this.fullName = fullName;
        this.dept = dept;
        this.designation = designation;
        this.email = email;
        this.token = token;
        this.userType = userType;
        this.imageLink = imageLink;
    }

    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}