package com.example.alexander.edadarom.fragments.Browse.Models;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class Ad {
    public String description;
    public String endTime;
    public Float location;
    public float price;
    public String publicTime;
    public String title;
    public String type;

    public Ad(String description, String endTime, Float location, float price, String publicTime, String title, String type) {
        this.description = description;
        this.endTime = endTime;
        this.location = location;
        this.price = price;
        this.publicTime = publicTime;
        this.title = title;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Float getLocation() {
        return location;
    }

    public void setLocation(Float location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
