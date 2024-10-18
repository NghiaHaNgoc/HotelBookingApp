package com.hotel.hotel_booking_app.service;

import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.Reservation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReservationService {
    @POST("user/reservation/add")
    Call<ApiResponse<Reservation>> addReservation(@Header("Authorization") String authorization,
                                                  @Body Reservation.ReservationInput input);
}
