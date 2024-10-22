package com.hotel.hotel_booking_app.model;

public class Reservation {
    public Integer id;
    public Integer userId;
    public Integer roomId;
    public String checkinAt;
    public String checkoutAt;
    public Integer adultNumber;
    public Integer kidNumber;
    public Integer status;
    public Integer totalPrice;
    public String updatedAt;
    public String createdAt;
    public Room room;

    public static class ReservationInput {
        public String checkinAt;
        public String checkoutAt;
        public Integer adultNumber;
        public Integer kidNumber;
        public Integer totalPrice;
        public Integer typeRoomId;

        public ReservationInput(String checkinAt, String checkoutAt, Integer adultNumber,
                                Integer kidNumber, Integer totalPrice, Integer typeRoomId) {
            this.checkinAt = checkinAt;
            this.checkoutAt = checkoutAt;
            this.adultNumber = adultNumber;
            this.kidNumber = kidNumber;
            this.totalPrice = totalPrice;
            this.typeRoomId = typeRoomId;
        }
    }
}
