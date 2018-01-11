package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import java.util.HashMap;
import java.util.Map;

public class UserModel {

    public String adTitle;
    //public String key;

    public UserModel() {

    }

    public UserModel(String adTitle) {
        this.adTitle = adTitle;
        //this.key = key;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("adTitle",adTitle);
        //result.put
        return result;
    }

}
