package com.hotel.hotel_booking_app.model;

public class Province {
    private String provinceName;
    private String provinceId;

    public Province(String provinceName, String provinceId) {
        this.provinceName = provinceName;
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return provinceName;
    }
}

