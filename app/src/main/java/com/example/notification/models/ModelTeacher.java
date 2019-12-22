package com.example.notification.models;

import java.io.Serializable;

public class ModelTeacher implements Serializable {
    private String tName;
    private String dept;
    private String designation;
    private String email;
    private String token;
    private String userType;


    public ModelTeacher() {

    }

    public ModelTeacher(String name, String dept, String designation, String email, String token, String userType) {
        this.tName = name;
        this.dept = dept;
        this.designation = designation;
        this.email = email;
        this.token = token;
        this.userType = userType;
    }

    public String getName() {
        return tName;
    }

    public void setName(String name) {
        this.tName = name;
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
}