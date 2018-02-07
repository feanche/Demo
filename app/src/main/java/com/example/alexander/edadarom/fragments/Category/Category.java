package com.example.alexander.edadarom.fragments.Category;

/**
 * Created by GabdrakhmanovII on 06.02.2018.
 */

public class Category {
    private int categoryId;
    private int categoryPosition;
    private String name;
    private String description;
    private String imgUrl;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(int categoryId, String name, String description, String imgUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public int getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(int categoryPosition) {
        this.categoryPosition = categoryPosition;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
