package com.example.notification.models;

import java.io.Serializable;

public class ModelRequest implements Serializable {
    private String senderId;
    private String recieverId;
    private String sendDateTime;
    private String requestId;
    private String scheduleId;

    public ModelRequest(){

    }

    public ModelRequest(String senderId, String recieverId, String sendDateTime, String requestId, String scheduleId) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.sendDateTime = sendDateTime;
        this.requestId = requestId;
        this.scheduleId = scheduleId;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}
