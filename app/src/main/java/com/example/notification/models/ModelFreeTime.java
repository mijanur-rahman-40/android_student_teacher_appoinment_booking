package com.example.notification.models;

import java.io.Serializable;

public class ModelFreeTime implements Serializable {
    private String freeDate;
    private String startTime;
    private String endTime;
    private String owner;
    private String freeSlot;
    private String scheduleID;

    public ModelFreeTime(){
    }

    public ModelFreeTime(String freeDate, String startTime, String endTime, String owner, String freeSlot, String scheduleID) {
        this.freeDate = freeDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.owner = owner;
        this.freeSlot = freeSlot;
        this.scheduleID = scheduleID;
    }

    public String getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(String freeDate) {
        this.freeDate = freeDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String starttime) {
        this.startTime = starttime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFreeSlot() {
        return freeSlot;
    }

    public void setFreeSlot(String freeSlot) {
        this.freeSlot = freeSlot;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

}
