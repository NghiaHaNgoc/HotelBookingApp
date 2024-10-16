package com.hotel.hotel_booking_app.model;

import java.util.List;

public class TypeRoom {
    public Integer id;
    public String title;
    public Integer viewDirection;
    public String preferentialServices;
    public Integer size;
    public Integer adultCapacity;
    public Integer kidsCapacity;
    public Integer basePrice;
    public Integer status;
    public String createdAt;
    public String updatedAt;
    public List<Integer> amenities;
    public List<ImageTypeRoom> images;

    public static class ImageTypeRoom {
        public Integer id;
        public String link;
    }
}
