package com.hotel.hotel_booking_app.service;

import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TypeRoomService {
    @GET("public/type-room/list")
    Call<ApiResponse<ArrayList<TypeRoom>>> getListTypeRoom();
}
