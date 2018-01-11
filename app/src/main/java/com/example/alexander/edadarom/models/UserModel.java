package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import java.util.HashMap;
import java.util.Map;

public class UserModel {

    public String adTitle;

    public UserModel() {

    }

    public UserModel(String adTitle) {
        this.adTitle = adTitle;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result",result);
        return result;
    }

}
