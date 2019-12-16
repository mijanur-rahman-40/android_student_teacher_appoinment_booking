package com.example.notification;

import java.io.Serializable;

public class UserStudent implements Serializable {
    private String email;
    private String token;
    private String fullName;
    private String regNo;
    private String department;
    private String semester;
    private String session;


    public UserStudent() {

    }

    public UserStudent(String email, String fullName, String token, String regNo, String department, String semester, String session) {
        this.email = email;
        this.fullName = fullName;
        this.token = token;
        this.regNo = regNo;
        this.department = department;
        this.semester = semester;
        this.session = session;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}