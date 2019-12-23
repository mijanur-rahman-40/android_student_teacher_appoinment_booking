package com.example.notification.models;


public class ModelMessage {

    private String message, receiver, sender, dateTime;

    private boolean isSeen;


    public ModelMessage(){

    }


    public ModelMessage(String message, String receiver, String sender, String dateTime, boolean isSeen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.dateTime = dateTime;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
