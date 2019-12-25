package com.example.notification.models;

public class ModelFreeTime {
    String freeDate;
    String starttime;
    String endTime;

    public ModelFreeTime(String freeDate, String starttime, String endTime) {
        this.freeDate = freeDate;
        this.starttime = starttime;
        this.endTime = endTime;
    }

    public String getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(String freeDate) {
        this.freeDate = freeDate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
