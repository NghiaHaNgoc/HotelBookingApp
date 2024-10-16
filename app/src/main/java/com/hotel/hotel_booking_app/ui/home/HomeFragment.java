package com.hotel.hotel_booking_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentHomeBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        NavHostFragment navHostFragment = (NavHostFragment)
//                getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
//        NavController navController = Navigation.findNavController(getView());
        binding.recyclerViewListTypeRoom.setLayoutManager(new LinearLayoutManager(getContext()));

        new ApiService(getContext())
                .getListTypeRoom()
                .enqueue(
                        new Callback<ApiResponse<ArrayList<TypeRoom>>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<ArrayList<TypeRoom>>> call,
                                                   Response<ApiResponse<ArrayList<TypeRoom>>> response) {
                                MyHomeTypeRoomCardRecyclerViewAdapter a = new MyHomeTypeRoomCardRecyclerViewAdapter(getContext(), response.body().result);
                                a.setOnClickListener(item -> {
                                    NavController navController = Navigation.findNavController(getView());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("type_room", new Gson().toJson(item));
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
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
