package com.example.alexander.edadarom.fragments.Browse.Models;

/**
 * Created by lAntimat on 15.01.2018.
 */

public class Ad {
    public String description;
    public String endTime;
    public float locationLat;
    public float locationLon;
    public float price;
    public String publicTime;
    public String title;
    public String type;

    public Ad(String description, String endTime, float locationLat, float locationLon, float price, String publicTime, String title, String type) {
        this.description = description;
        this.endTime = endTime;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
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

    public float getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(float locationLat) {
        this.locationLat = locationLat;
    }

    public float getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(float locationLon) {
        this.locationLon = locationLon;
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
