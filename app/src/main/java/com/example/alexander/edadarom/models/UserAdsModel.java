package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdsModel {

    String id;
    String userId;
    public String description;
    public long endTime;
    public float locationLat;
    public float locationLon;
    public int price;
    public long publicTime;
    public boolean reserved;
    public String title;
    public String type;
    public ArrayList<String> photoUrl;

    public UserAdsModel() {

    }

    public UserAdsModel(String description, long endTime, float locationLat, float locationLon, int price, long publicTime, String title, String type, ArrayList<String> photoUrl) {
        this.description = description;
        this.endTime = endTime;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.price = price;
        this.publicTime = publicTime;
        this.title = title;
        this.type = type;
        this.photoUrl = photoUrl;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message",description);
        result.put("endTime",endTime);
        result.put("locationLon",locationLon);
        result.put("locationLat",locationLat);
        result.put("price",price);
        result.put("publicTime",publicTime);
        result.put("title",title);
        result.put("type",type);
        result.put("photoUrl",photoUrl);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(long publicTime) {
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

    public ArrayList<String> getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(ArrayList<String> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
