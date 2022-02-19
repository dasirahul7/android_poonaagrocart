package com.poona.agrocart.ui.nav_notification;

public class Notification {
    String id, content, date;

    public Notification(String id, String content, String date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
