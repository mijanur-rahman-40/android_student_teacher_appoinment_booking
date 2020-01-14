package com.example.notification.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ModelRequest implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderId);
        dest.writeString(receiverId);
        dest.writeString(sendDateTime);
        dest.writeString(requestId);
        dest.writeString(scheduleId);
        dest.writeString(requesterName);
        dest.writeString(receiverName);
        dest.writeString(aptdate);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeBoolean(isAccepted);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public ModelRequest createFromParcel(Parcel source) {
            return new ModelRequest(source);
        }

        public ModelRequest[] newArray(int size){
            return new ModelRequest[size];
        }
    };

    public ModelRequest(Parcel parcel){
        senderId = parcel.readString();
        receiverId = parcel.readString();
        sendDateTime = parcel.readString();
        requestId = parcel.readString();
        scheduleId = parcel.readString();
        requesterName = parcel.readString();
        receiverName = parcel.readString();
        aptdate = parcel.readString();
        startTime = parcel.readString();
        endTime = parcel.readString();
        isAccepted = parcel.readBoolean();
    }
}
