package com.example.alexander.edadarom.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by GabdrakhmanovII on 31.01.2018.
 */

public class ReservationInfo {
    String reservedUser;
    @ServerTimestamp
    public Date timestamp;
    @ServerTimestamp
    Date reservationDate;
    @ServerTimestamp
    Date reservationDateEnd;
    boolean delivery;
    String deliveryAddress;

    public ReservationInfo() {
    }

    public ReservationInfo(Date timestamp, Date reservationDate, Date reservationDateEnd, boolean delivery, String deliveryAddress) {
        this.timestamp = timestamp;
        this.reservationDate = reservationDate;
        this.reservationDateEnd = reservationDateEnd;
        this.delivery = delivery;
        this.deliveryAddress = deliveryAddress;
    }

    public String getReservedUser() {
        return reservedUser;
    }

    public void setReservedUser(String reservedUser) {
        this.reservedUser = reservedUser;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getReservationDateEnd() {
        return reservationDateEnd;
    }

    public void setReservationDateEnd(Date reservationDateEnd) {
        this.reservationDateEnd = reservationDateEnd;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
