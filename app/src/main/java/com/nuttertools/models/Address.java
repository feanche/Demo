package com.nuttertools.models;

/**
 * Created by Alexander on 31.01.2018.
 */

public class Address {
    double locationLat;
    double locationLon;
    String commentToAddress;
    String locality;
    String id;
    Boolean defaultAddress;

    public Address() {

    }

    public Address(double locationLat, double locationLon, String commentToAddress, String locality) {
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

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(double locationLon) {
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

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
