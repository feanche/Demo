package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class UserAdsModel {

    private String id;
    private String description;
    private long endTime;
    private int location;
    private int price;
    private long publicTime;
    private String title;
    private String type;
    private String photoUrl;

    public UserAdsModel() {

    }

    public UserAdsModel(String description, long endTime, int location, int price, long publicTime, String title, String type, String photoUrl) {
        this.description = description;
        this.endTime = endTime;
        this.location = location;
        this.price = price;
        this.publicTime = publicTime;
        this.title = title;
        this.type = type;
        this.photoUrl = photoUrl;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description",description);
        result.put("endTime",endTime);
        result.put("location",location);
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
