package com.example.notification.notifications;



public class Data {

    private String userId, body, title, sent, notificationType;
    private  Integer icon;


    public Data() {

    }

    public Data(String userId, String body, String title, String sent, String notificationType, Integer icon) {
        this.userId = userId;
        this.body = body;
        this.title = title;
        this.sent = sent;
        this.notificationType = notificationType;
        this.icon = icon;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
