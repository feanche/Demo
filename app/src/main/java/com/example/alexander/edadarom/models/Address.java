package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 31.01.2018.
 */

public class Address {
    float locationLat;
    float locationLon;
    String commentToAddress;
    String locality;
    String id;

    public Address() {

    }

    public Address(float locationLat, float locationLon, String commentToAddress, String locality) {
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.commentToAddress = commentToAddress;
        this.locality = locality;
    }

    public Address(float locationLat, float locationLon, String commentToAddress, String locality, String id) {
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.commentToAddress = commentToAddress;
        this.locality = locality;
        this.id = id;
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

    public String getCommentToAddress() {
        return commentToAddress;
    }

    public void setCommentToAddress(String commentToAddress) {
        this.commentToAddress = commentToAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
