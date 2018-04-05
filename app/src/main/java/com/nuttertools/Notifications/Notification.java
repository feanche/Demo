package com.nuttertools.Notifications;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class Notification {
    public String id;
    public String title;
    public String body;
    @ServerTimestamp
    public Date timestamp;

    public Notification() {
    }

    public Notification(String id, String title, String body, Date timestamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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