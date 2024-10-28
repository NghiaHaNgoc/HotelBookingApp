package com.hotel.hotel_booking_app.model;

public class Ward {
    private String wardName;
    private String wardId;
    private String districtId;

    public Ward(String wardName, String wardId, String districtId) {
        this.wardName = wardName;
        this.wardId = wardId;
        this.districtId = districtId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return wardName;
    }
}

