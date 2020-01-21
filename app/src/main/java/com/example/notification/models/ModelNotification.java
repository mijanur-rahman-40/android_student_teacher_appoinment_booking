package com.example.notification.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ModelNotification implements Parcelable {


    private String notifierId;
    private String sendDateTime;
    private String scheduleId;
    private String notifierName;

    public ModelNotification(){

    }

    public ModelNotification(String notifierId, String sendDateTime, String scheduleId, String notifierName) {
        this.notifierId = notifierId;
        this.sendDateTime = sendDateTime;
        this.scheduleId = scheduleId;
        this.notifierName = notifierName;
    }

    public String getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(String notifierId) {
        this.notifierId = notifierId;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notifierId);
        dest.writeString(sendDateTime);
        dest.writeString(scheduleId);
        dest.writeString(notifierName);

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

    public ModelNotification(Parcel parcel){
        notifierId = parcel.readString();
        sendDateTime = parcel.readString();
        scheduleId = parcel.readString();
        notifierName = parcel.readString();
    }
}
