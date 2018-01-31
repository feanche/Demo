package com.example.alexander.edadarom.models;

/**
 * Created by GabdrakhmanovII on 31.01.2018.
 */

public class ReservationInfo {
    String reservedUser;
    long reservationDate;
    long reservationPeriod;
    boolean delivery;
    String deliveryAddress;

    public ReservationInfo() {
    }

    public ReservationInfo(String reservedUser, long reservationDate, long reservationPeriod, boolean delivery, String deliveryAddress) {
        this.reservedUser = reservedUser;
        this.reservationPeriod = reservationPeriod;
        this.reservationDate = reservationDate;
        this.delivery = delivery;
        this.deliveryAddress = deliveryAddress;
    }

    public String getReservedUser() {
        return reservedUser;
    }

    public void setReservedUser(String reservedUser) {
        this.reservedUser = reservedUser;
    }

    public long getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(long reservationDate) {
        this.reservationDate = reservationDate;
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

    public long getReservationPeriod() {
        return reservationPeriod;
    }

    public void setReservationPeriod(long reservationPeriod) {
        this.reservationPeriod = reservationPeriod;
    }
}
