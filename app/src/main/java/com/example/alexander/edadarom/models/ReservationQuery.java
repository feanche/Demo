package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 26.01.2018.
 */

public class ReservationQuery {
    String senderId;
    String receiverId;
    String adId;
    long reservationDate;

    //Доставка
    boolean delivery = false;
    String deliveryAddress;


    public ReservationQuery(String senderId, String receiverId, String adId, long reservationDate) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.reservationDate = reservationDate;
    }

    public ReservationQuery(String senderId, String receiverId, String adId, long reservationDate, boolean isDelivery, String deliveryAddress) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.reservationDate = reservationDate;
        this.delivery = isDelivery;
        this.deliveryAddress = deliveryAddress;
    }
}
