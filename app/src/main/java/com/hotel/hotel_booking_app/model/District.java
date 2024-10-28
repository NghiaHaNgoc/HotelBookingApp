package com.hotel.hotel_booking_app.model;

public class District {
    private String districtName;
    private String districtId;
    private String provinceId;

    public District(String districtName, String districtId, String provinceId) {
        this.districtName = districtName;
        this.districtId = districtId;
        this.provinceId = provinceId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return districtName;
    }
}

