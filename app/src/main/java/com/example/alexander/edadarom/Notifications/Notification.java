package com.example.alexander.edadarom.Notifications;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class Notification {
    public String title;
    public String body;
    @ServerTimestamp
    public Date timestamp;

    public Notification() {
    }

    public Notification(String title, String message, Date time) {
        this.title = title;
        this.body = message;
        this.timestamp = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}