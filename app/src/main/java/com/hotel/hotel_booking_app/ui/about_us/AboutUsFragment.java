package com.hotel.hotel_booking_app.ui.about_us;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentAboutUsBinding;

public class AboutUsFragment extends Fragment {
    FragmentAboutUsBinding binding;
    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        binding.buttonOurLocation.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.nav_about_us_map);
        });
        return binding.getRoot();
    }
}