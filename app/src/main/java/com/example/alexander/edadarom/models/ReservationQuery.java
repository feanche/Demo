package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 26.01.2018.
 */

public class ReservationQuery {
    String senderId;
    String receiverId;
    String adId;
    String senderPushToken;

    public ReservationQuery(String senderId, String receiverId, String adId, String senderPushToken) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.adId = adId;
        this.senderPushToken = senderPushToken;
    }
}
