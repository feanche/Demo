package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 26.01.2018.
 */

public class ReservationQuery {
    String senderId;
    String receiverId;
    String AdId;

    public ReservationQuery(String senderId, String receiverId, String adId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        AdId = adId;
    }
}
