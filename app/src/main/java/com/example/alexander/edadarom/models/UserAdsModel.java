package com.example.alexander.edadarom.models;

/**
 * Created by Alexander on 11.01.2018.
 */

import com.google.firebase.firestore.ServerTimestamp;
import java.util.ArrayList;
import java.util.Date;

public class UserAdsModel {

    float locationLat;
    float locationLon;
    String commentToAddress;
    int price;
    int categoryId;
    boolean reserved;
    String title;
    ArrayList<String> photoUrl;
    ReservationInfo reservationInfo;
    String id;
    String userId;
    String description;
    @ServerTimestamp
    public Date timestamp;
    String priceType;

    public UserAdsModel() {

    }

    public UserAdsModel(float locationLat, float locationLon, int price, int categoryId, String title, ArrayList<String> photoUrl, String description, Date timestamp, String commentToAddress, String priceType) {
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.price = price;
        this.categoryId = categoryId;
        this.title = title;
        this.photoUrl = photoUrl;
        this.description = description;
        this.timestamp = timestamp;
        this.commentToAddress = commentToAddress;
        this.priceType = priceType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(ArrayList<String> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ReservationInfo getReservationInfo() {
        return reservationInfo;
    }

    public void setReservationInfo(ReservationInfo reservationInfo) {
        this.reservationInfo = reservationInfo;
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

    public String getCommentToAddress() {
        return commentToAddress;
    }

    public void setCommentToAddress(String commentToAddress) {
        this.commentToAddress = commentToAddress;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
}
