package com.example.notification;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String token;
    private String fullName;


    public User(){

    }
    public User(String email, String token, String fullName) {
        this.email = email;
        this.token = token;
        this.fullName = fullName;
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
}
