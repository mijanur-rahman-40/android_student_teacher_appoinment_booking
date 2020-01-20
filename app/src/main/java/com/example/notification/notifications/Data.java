package com.example.notification.notifications;

import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;

public class Data {

    private ModelStudent userStudent;
    private ModelTeacher userTeacher;
    private String  body, title, sent;
    private  Integer icon;


    public Data() {

    }

    public Data(ModelStudent userStudent, ModelTeacher userTeacher, String body, String title, String sent, Integer icon) {
        this.userStudent = userStudent;
        this.userTeacher = userTeacher;
        this.body = body;
        this.title = title;
        this.sent = sent;
        this.icon = icon;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ModelStudent getUser() {
        return userStudent;
    }

    public void setUser(ModelStudent userStudent) {
        this.userStudent = userStudent;
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

    public ModelTeacher getUserTeacher() {
        return userTeacher;
    }

    public void setUserTeacher(ModelTeacher userTeacher) {
        this.userTeacher = userTeacher;
    }
}
