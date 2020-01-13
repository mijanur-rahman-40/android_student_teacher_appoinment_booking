package com.example.notification.models;

import java.io.Serializable;

public class ModelRequest implements Serializable {
    private String senderId;
    private String receiverId;
    private String sendDateTime;
    private String requestId;
    private String scheduleId;
    private String requesterName;
    private String receiverName;
    private String aptdate;
    private String startTime;
    private String endTime;
    private boolean isAccepted;

    public ModelRequest(){

    }

    public ModelRequest(String senderId, String receiverId, String sendDateTime, String requestId,
                        String scheduleId, String requesterName, String receiverName, String aptdate, String startTime, String endTime, boolean isAccepted) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sendDateTime = sendDateTime;
        this.requestId = requestId;
        this.scheduleId = scheduleId;
        this.requesterName = requesterName;
        this.receiverName = receiverName;
        this.aptdate = aptdate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAccepted = isAccepted;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
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

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAptdate() {
        return aptdate;
    }

    public void setAptdate(String aptdate) {
        this.aptdate = aptdate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
