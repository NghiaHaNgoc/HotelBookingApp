package com.hotel.hotel_booking_app.service;

import com.hotel.hotel_booking_app.model.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AddressService {
    @GET("?depth=3")
    Call<List<Address.Province>> getAllAddress();
}
