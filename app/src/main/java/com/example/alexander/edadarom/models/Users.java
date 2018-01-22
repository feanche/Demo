package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 22.01.2018.
 */

public class Users {
    String userType;
    String firstName;
    String secondName;
    String email;
    String photo;
    float rating;

    public Users() {
    }

    public Users(String userType, String firstName, String secondName, String email, String photo, float rating) {
        this.userType = userType;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.photo = photo;
        this.rating = rating;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
