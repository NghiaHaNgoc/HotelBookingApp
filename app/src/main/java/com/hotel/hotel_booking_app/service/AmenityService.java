package com.hotel.hotel_booking_app.service;

import com.hotel.hotel_booking_app.model.Amenity;
import com.hotel.hotel_booking_app.model.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AmenityService {
    @GET("public/amenity/list")
    Call<ApiResponse<List<Amenity>>> listAmenity();
}
