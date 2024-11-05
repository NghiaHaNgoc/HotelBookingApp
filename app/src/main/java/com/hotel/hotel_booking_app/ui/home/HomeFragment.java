package com.hotel.hotel_booking_app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentHomeBinding;
import com.hotel.hotel_booking_app.model.Amenity;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Amenity> amenities;
    ApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = new ApiService(getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        apiService.listAmenity().enqueue(new Callback<ApiResponse<List<Amenity>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Amenity>>> call, Response<ApiResponse<List<Amenity>>> response) {
                amenities = response.body().result;
                Log.e("AMENITY", amenities.size() + "");
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Amenity>>> call, Throwable throwable) {

            }
        });
        View root = binding.getRoot();


        binding.recyclerViewListTypeRoom.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        apiService.getListTypeRoom()
                .enqueue(
                        new Callback<ApiResponse<ArrayList<TypeRoom>>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<ArrayList<TypeRoom>>> call,
                                                   Response<ApiResponse<ArrayList<TypeRoom>>> response) {
                                MyHomeTypeRoomCardRecyclerViewAdapter a = new MyHomeTypeRoomCardRecyclerViewAdapter(getContext(), response.body().result);
                                a.setOnClickListener(item -> {
                                    NavController navController = Navigation.findNavController(getView());
                                    Gson gson = new Gson();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("type_room", gson.toJson(item));
                                    bundle.putString("amenity", gson.toJson(amenities));
                                    navController.navigate(R.id.nav_type_room_detail, bundle);
                                });

                                binding.recyclerViewListTypeRoom.setAdapter(a);
                                binding.recyclerViewListTypeRoom.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(
                                    Call<ApiResponse<ArrayList<TypeRoom>>> call,
                                    Throwable throwable) {
                            }
                        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
