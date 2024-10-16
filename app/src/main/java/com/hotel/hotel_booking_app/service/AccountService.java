package com.hotel.hotel_booking_app.service;

import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AccountService {
    @POST("customer/sign-in")
    Call<ApiResponse<User.SignInOutput>> signIn(@Body User.SignInInput input);
    @POST("customer/sign-up")
    Call<ApiResponse<User.SignInOutput>> signUp(@Body User.SignUpInput input);

    @GET("user/profile")
    Call<ApiResponse<User>> getProfile(@Header("Authorization") String authorization);
    @POST("user/profile")
    Call<ApiResponse<User>> updateProfile(@Header("Authorization") String authorization, @Body User input);
}
