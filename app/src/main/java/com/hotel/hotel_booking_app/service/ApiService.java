package com.hotel.hotel_booking_app.service;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.model.Address;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private final Context context;
    private final AllService service;

    public ApiService(Context context) {
        this.context = context;
        String apiEndpoint = this.context.getResources().getString(R.string.api_endpoint);
        Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(apiEndpoint).addConverterFactory(GsonConverterFactory.create(gson)).build();

        service = retrofit.create(AllService.class);

    }

    public static Call<List<Address.Province>> getAllAddress(Context context) {
        String apiEndpoint = context.getResources().getString(R.string.api_address_vietnam);
        Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(apiEndpoint).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit.create(AddressService.class).getAllAddress();
    }

    public Call<ApiResponse<User.SignInOutput>> signIn(User.SignInInput input) {
        return service.signIn(input);
    }

    public Call<ApiResponse<User.SignInOutput>> signUp(User.SignUpInput input) {
        return service.signUp(input);
    }

    public Call<ApiResponse<User>> getProfile() {
        return service.getProfile(getAuthorization());
    }

    public Call<ApiResponse<User>> updateProfile(User input) {
        return service.updateProfile(getAuthorization(), input);
    }

    public Call<ApiResponse<ArrayList<TypeRoom>>> getListTypeRoom() {
        return service.getListTypeRoom();
    }

    private String getAuthorization() {
        return "Bearer " + context.getSharedPreferences("account",
                Context.MODE_PRIVATE).getString("token", "");
    }
}
