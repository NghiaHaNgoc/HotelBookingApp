package com.hotel.hotel_booking_app.ui.reservation_history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationHistoryBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.Reservation;
import com.hotel.hotel_booking_app.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationHistoryFragment extends Fragment {
    private FragmentReservationHistoryBinding binding;
    private boolean isFirstLaunch;
    private SharedPreferences accountSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstLaunch = true;
        accountSharedPreferences = getContext().getSharedPreferences("account",
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationHistoryBinding.inflate(inflater, container, false);

        if (!accountSharedPreferences.getBoolean("isSignedIn", false)) {
            return binding.getRoot();
        }

        binding.reservationHistoryItemList.setLayoutManager(new LinearLayoutManager(getContext()));

        new ApiService(getContext()).listReservation().enqueue(new Callback<ApiResponse<List<Reservation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Reservation>>> call,
                                   Response<ApiResponse<List<Reservation>>> response) {
                ReservationHistoryItemRecyclerViewAdapter adapter = new ReservationHistoryItemRecyclerViewAdapter(getContext(), response.body().result);
                adapter.setOnClickListener(item -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("reservation", new Gson().toJson(item));
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.nav_reservation_history_detail, bundle);
                });
                binding.reservationHistoryItemList
                        .setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Reservation>>> call, Throwable throwable) {

            }
        });
//        binding.reservationHistoryItemList.setAdapter();
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_reservation_history, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!accountSharedPreferences.getBoolean("isSignedIn", false)) {
            if (isFirstLaunch) {
                Navigation.findNavController(getView()).navigate(R.id.nav_sign_in);
            } else {
                Navigation.findNavController(getView()).popBackStack(R.id.nav_home, false);
            }
        }
        isFirstLaunch = false;
    }
}