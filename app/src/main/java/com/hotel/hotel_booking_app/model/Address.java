package com.hotel.hotel_booking_app.model;

import java.util.List;

public class Address {
    public String name;
    public Integer code;
    public String codename;
    public String divisionType;
    public static class Province extends Address {
        public Integer phoneCode;
        public List<District> districts;
    }
    public static class District extends Address {
        public String shortCodename;
        public List<Ward> wards;
    }
    public static class Ward extends Address {
        public String shortCodename;
    }
}
