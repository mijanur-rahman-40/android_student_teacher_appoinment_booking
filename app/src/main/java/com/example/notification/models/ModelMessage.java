package com.example.notification.models;

import java.util.Date;


public class ModelMessage {

    private String message;

    private Date date;

    private String userID;


    public ModelMessage(String message, Date date, String userID) {
        this.message = message;
        this.date = date;
        this.userID = userID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }


}
