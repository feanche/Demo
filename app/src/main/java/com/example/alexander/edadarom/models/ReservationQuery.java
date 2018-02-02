package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 26.01.2018.
 */

public class ReservationQuery {
    //1 - забронировал
    //2 - отмена бронирования
    int type;
    String senderId;
    String receiverId;
    String adId;
    long reservationDate;

    //Доставка
    boolean delivery = false;
    String deliveryAddress;


    public ReservationQuery() {
    }

    public ReservationQuery(int type, String senderId, String receiverId, String adId) {
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
    }

    public ReservationQuery(int type, String senderId, String receiverId, String adId, long reservationDate) {
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.reservationDate = reservationDate;
    }

    public ReservationQuery(int type, String senderId, String receiverId, String adId, long reservationDate, boolean isDelivery, String deliveryAddress) {
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.reservationDate = reservationDate;
        this.delivery = isDelivery;
        this.deliveryAddress = deliveryAddress;
    }
}
