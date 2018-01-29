package com.example.alexander.edadarom.Notifications;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class Notification {
    public String title;
    public String message;

    public Notification() {
    }

    public Notification(String title, String description) {
        this.title = title;
        this.message = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
