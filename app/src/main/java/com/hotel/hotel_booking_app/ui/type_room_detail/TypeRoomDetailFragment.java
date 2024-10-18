package com.hotel.hotel_booking_app.ui.type_room_detail;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentTypeRoomDetailBinding;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.util.StringUtil;


public class TypeRoomDetailFragment extends Fragment {
    private FragmentTypeRoomDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTypeRoomDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Gson gson = new Gson();
        String typeRoomJson = getArguments().getString("type_room");
        TypeRoom typeRoom = gson.fromJson(typeRoomJson, TypeRoom.class);

//        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main);
        // Title
        binding.typeRoomTitle.setText(typeRoom.title);

        // View direction
        String viewDirection = String.format("\u25CF %s: %s",
                StringUtil.capitalize(getResources().getString(R.string.view_direction)),
                getResources().getString(typeRoom.viewDirection == 1 ?
                        R.string.view_direction_river : R.string.view_direction_city));
        binding.typeRoomViewDirection.setText(viewDirection);

        // Preferential services
        String preferentialServiceTitle = String.format("\u25CF %s:",
                StringUtil.capitalize(getResources().getString(R.string.preferential_services)));
        binding.typeRoomPreferentialServiceTitle.setText(preferentialServiceTitle);
        Spanned preferentialService = Html.fromHtml(typeRoom.preferentialServices, 0);
        binding.typeRoomPreferentialService.setText(preferentialService);

        // Size
        String size = String.format("\u25CF %s: %s\u33A1",
                StringUtil.capitalize(getResources().getString(R.string.size)), typeRoom.size);
        binding.typeRoomSize.setText(size);
        // Adult capacity
        String adultCapacity = String.format("\u25CF %s: %s",
                StringUtil.capitalize(getResources().getString(R.string.adult_capacity)),
                typeRoom.adultCapacity);
        binding.typeRoomAdultCapacity.setText(adultCapacity);

        // Kid capacity
        String kidCapacity = String.format("\u25CF %s: %s",
                StringUtil.capitalize(getResources().getString(R.string.kid_capacity)),
                typeRoom.kidsCapacity);
        binding.typeRoomKidCapacity.setText(kidCapacity);

        // Base price
        String price = String.format("\u25CF %s: %s",
                StringUtil.capitalize(getResources().getString(R.string.price)),
                typeRoom.basePrice);
        binding.typeRoomBasePrice.setText(price);
        binding.recyclerViewTypeRoomDetailCarousel.setLayoutManager(new CarouselLayoutManager());
        binding.recyclerViewTypeRoomDetailCarousel.setAdapter(new MyTypeRoomCarouselRecyclerViewAdapter(typeRoom.images));


        binding.buttonTypeRoomReservation.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getView());
            if (getContext().getSharedPreferences("account", Context.MODE_PRIVATE).getBoolean("isSignedIn", false)) {
                Bundle bundle = new Bundle();
                bundle.putString("type_room", typeRoomJson);
                navController.navigate(R.id.nav_reservation, bundle);
            } else {
                navController.navigate(R.id.nav_sign_in);
            }

        });
        return root;

    }
}