package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 26.01.2018.
 */

public class ReservationQuery {
    String senderId;
    String receiverId;
    String adId;
    String reservationDate;
    String reservationTime;

    //Доставка
    boolean delivery = false;
    String deliveryAddress;


    public ReservationQuery(String senderId, String receiverId, String adId, String reservationDate, String reservationTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public ReservationQuery(String senderId, String receiverId, String adId, String reservationDate, String reservationTime, boolean isDelivery, String deliveryAddress) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.delivery = isDelivery;
        this.deliveryAddress = deliveryAddress;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        delivery = delivery;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
