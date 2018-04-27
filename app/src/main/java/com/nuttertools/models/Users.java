package com.nuttertools.models;

/**
 * Created by GabdrakhmanovII on 22.01.2018.
 */

public class Users {
    String uId;
    String userType;
    String firstName;
    String secondName;
    String email;
    String photo;
    String phoneNumber;
    String pustNotificationToken;
    float rating;


    public Users() {
    }

    public Users(String uId, String firstName, String email, String phoneNumber) {
        this.uId = uId;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Users(String uId, String userType, String firstName, String secondName, String email, String photo, String phoneNumber, float rating) {
        this.uId = uId;
        this.userType = userType;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }

    public Users(String userType, String firstName, String secondName, String email, String photo, float rating) {
        this.userType = userType;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.photo = photo;
        this.rating = rating;
    }

    public String getPustNotificationToken() {
        return pustNotificationToken;
    }

    public void setPustNotificationToken(String pustNotificationToken) {
        this.pustNotificationToken = pustNotificationToken;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
