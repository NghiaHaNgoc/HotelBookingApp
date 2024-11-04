package com.hotel.hotel_booking_app.model;

public class Amenity {
    public Integer id;
    public String name;
    public String nameEn;
    public String nameJa;
    public int type;
    public int status;
    public String createdAt;

    public static final int GENERAL_TYPE = 1;
    public static final int BATHROOM_TYPE = 2;
    public static final int OTHER_TYPE = 3;
}
