package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class UserModel {

    public String description;
    public String endTime;
    public Float location;
    public float price;
    public String publicTime;
    public String title;
    public String type;

    public UserModel() {

    }

    public UserModel(String description, String endTime, Float location, Float price, String publicTime, String title, String type) {
        this.description = description;
        this.endTime = endTime;
        this.location = location;
        this.price = price;
        this.publicTime = publicTime;
        this.title = title;
        this.type = type;
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
        return result;
    }

}
