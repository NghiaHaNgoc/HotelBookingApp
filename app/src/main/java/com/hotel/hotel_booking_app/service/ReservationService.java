package com.hotel.hotel_booking_app.service;

import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationService {
    @POST("user/reservation/add")
    Call<ApiResponse<Reservation>> addReservation(@Header("Authorization") String authorization,
                                                  @Body Reservation.ReservationInput input);
    @GET("customer/reservation/list")
    Call<ApiResponse<List<Reservation>>> listReservation(@Header("Authorization") String authorization);

    @GET("customer/reservation/{id}")
    Call<ApiResponse<Reservation>> getReservation(@Header("Authorization") String authorization, @Path("id") int reservationId);

    @POST("customer/reservation/{id}/cancel")
    Call<ApiResponse<Reservation>> cancelReservation(@Header("Authorization") String authorization, @Path("id") int reservationId);
}
