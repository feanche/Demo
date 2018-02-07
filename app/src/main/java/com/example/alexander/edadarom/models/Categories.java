package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 07.02.2018.
 */

public class Categories {
    String description;
    String name;

    public Categories() {

    }

    public Categories(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
