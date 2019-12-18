package com.example.notification;

import java.io.Serializable;

public class UserTeacher implements Serializable {
    private String tName;
    private String dept;
    private String designation;
    private String email;
    private String token;
    private String noOfStApll;


    public UserTeacher() {

    }

    public UserTeacher(String name, String dept, String designation, String email, String token) {
        this.tName = name;
        this.dept = dept;
        this.designation = designation;
        this.email = email;
        this.token = token;
    }

    public UserTeacher(String name, String dept, String designation, String email, String token, String noOfStApll) {
        this.tName = name;
        this.dept = dept;
        this.designation = designation;
        this.email = email;
        this.token = token;
        this.noOfStApll = noOfStApll;
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

    public String getNoOfStApll() {
        return noOfStApll;
    }

    public void setNoOfStApll(String noOfStApll) {
        this.noOfStApll = noOfStApll;
    }
}