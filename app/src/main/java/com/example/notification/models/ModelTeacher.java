package com.example.notification.models;

import java.io.Serializable;

public class ModelTeacher implements Serializable {
    private String fullName;
    private String department;
    private String designation;
    private String email;
    private String token;
    private String userType;
    private String imageLink;


    public ModelTeacher() {

    }

    public ModelTeacher(String fullName, String department, String designation, String email, String token, String userType, String imageLink) {
        this.fullName = fullName;
        this.department = department;
        this.designation = designation;
        this.email = email;
        this.token = token;
        this.userType = userType;
        this.imageLink = imageLink;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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