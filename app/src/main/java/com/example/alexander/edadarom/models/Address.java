package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 31.01.2018.
 */

public class Address {
    String index;
    String city;
    String address;
    String region;
    String title;

    public Address() {

    }

    public Address(String index, String city, String address, String region, String title) {
        this.index = index;
        this.city = city;
        this.address = address;
        this.region = region;
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
