package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class UserAdsModel {

    public String description;
    public long endTime;
    public float locationLat;
    public float locationLon;
    public int price;
    public long publicTime;
    public String title;
    public String type;
    public String photoUrl;

    public UserAdsModel() {

    }

    public UserAdsModel(String description, long endTime, float locationLat, float locationLon, int price, long publicTime, String title, String type, String photoUrl) {
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
        result.put("description",description);
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

}
