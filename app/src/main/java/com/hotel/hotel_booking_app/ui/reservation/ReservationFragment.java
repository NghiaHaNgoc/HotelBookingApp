package com.hotel.hotel_booking_app.ui.reservation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationBinding;
import com.hotel.hotel_booking_app.databinding.FragmentTypeRoomDetailBinding;
import com.hotel.hotel_booking_app.model.TypeRoom;

public class ReservationFragment extends Fragment {

   private FragmentReservationBinding binding;
   private TypeRoom typeRoom;

    public ReservationFragment() {
        // Required empty public constructor
    }

    public static ReservationFragment newInstance(String param1, String param2) {
        ReservationFragment fragment = new ReservationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Gson gson = new Gson();
        String typeRoomJson = getArguments().getString("type_room");
        typeRoom = gson.fromJson(typeRoomJson, TypeRoom.class);

        TypeRoom.ImageTypeRoom imageLink = typeRoom.images.get(0);
        Glide.with(binding.imageReservation).load(imageLink.link).placeholder(R.drawable.ic_menu_camera).into(binding.imageReservation);
        binding.textReservationTypeRoomTitle.setText(typeRoom.title);

        binding.buttonSubmitReservation.setOnClickListener(view -> {
            String dateFrom = binding.editTextDateFromPicker.getText().toString();
            String timeFrom = binding.editTextTimeFromPicker.getText().toString();
            String dateTo = binding.editTextDateToPicker.getText().toString();
            String timeTo = binding.editTextTimeToPicker.getText().toString();
            String adultNumber = binding.editTextAdultNumber.getText().toString();
            String kidNumber = binding.editTextKidNumber.getText().toString();
            if (checkAllFields(dateFrom, timeFrom, dateTo, timeTo, adultNumber, kidNumber)) {

            }
        });
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_reservation, container, false);
        return root;
    }
    private boolean checkAllFields(String dateFrom, String timeFrom, String dateTo, String timeTo, String adultNumber, String kidNumber) {
        String requiredErrorMessage = getContext().getResources().getString(R.string.require_input_message);
        String exceedCapacityErrorMessage = getContext().getResources().getString(R.string.exceed_capacity_limit_message);
        boolean isValid = true;
        if (dateFrom.isEmpty()) {
            binding.editTextDateFromPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (timeFrom.isEmpty()) {
            binding.editTextTimeFromPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (dateTo.isEmpty()) {
            binding.editTextDateToPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (timeTo.isEmpty()) {
            binding.editTextTimeToPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (adultNumber.isEmpty()) {
            binding.editTextAdultNumber.setError(requiredErrorMessage);
            isValid = false;
        } else if (Integer.parseInt(adultNumber) > typeRoom.adultCapacity) {
            binding.editTextAdultNumber.setError(exceedCapacityErrorMessage);
            isValid = false;
        }

        if (kidNumber.isEmpty()) {
            binding.editTextKidNumber.setError(requiredErrorMessage);
            isValid = false;
        } else if (Integer.parseInt(kidNumber) > typeRoom.kidsCapacity) {
            binding.editTextKidNumber.setError(exceedCapacityErrorMessage);
            isValid = false;
        }



        // after all validation return true.
        return isValid;
    }
}