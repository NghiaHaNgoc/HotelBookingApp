package com.hotel.hotel_booking_app.ui.reservation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationSuccessBinding;
import com.hotel.hotel_booking_app.model.Reservation;
import com.hotel.hotel_booking_app.model.TypeRoom;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class ReservationSuccessFragment extends Fragment {
    private Reservation reservation;
    private TypeRoom typeRoom;
    private FragmentReservationSuccessBinding binding;

    public ReservationSuccessFragment() {
        // Required empty public constructor
    }

    public static ReservationSuccessFragment newInstance(String param1, String param2) {
        ReservationSuccessFragment fragment = new ReservationSuccessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservation = new Gson().fromJson(getArguments().getString("reservation_result"),
                    Reservation.class);
            typeRoom = new Gson().fromJson(getArguments().getString("type_room"), TypeRoom.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationSuccessBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ZoneId currentZoneId = TimeZone.getDefault().toZoneId();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        // Room number
        String roomNumber = String.format("%s: %s",
                getResources().getString(R.string.room_number), reservation.room.roomNumber);
        binding.textReservationSuccessRoomNumber.setText(roomNumber);

        // Floor
        String floor = String.format("%s: %s", getResources().getString(R.string.floor),
                reservation.room.floor);
        binding.textReservationSuccessFloor.setText(floor);

        // Reservation id
        String reservationId = String.format("%s: %s",
                getResources().getString(R.string.reservation_id), reservation.id);
        binding.textReservationSuccessId.setText(reservationId);

        // Type room
        String typeRoomStr = String.format("%s: %s", getResources().getString(R.string.type_room), typeRoom.title);
        binding.textReservationSuccessTypeRoom.setText(typeRoomStr);

        // Datetime from
        ZonedDateTime datetimeFromObj = Instant.parse(reservation.checkinAt).atZone(currentZoneId);
        String datetimeFrom = String.format("%s: %s",
                getResources().getString(R.string.datetime_from),
                datetimeFromObj.format(dateTimeFormatter));
        binding.textReservationSuccessDatetimeFrom.setText(datetimeFrom);

        // Datetime to
        ZonedDateTime dateTimeToObj = Instant.parse(reservation.checkoutAt).atZone(currentZoneId);
        String datetimeTo = String.format("%s: %s",
                getResources().getString(R.string.datetime_to),
                dateTimeToObj.format(dateTimeFormatter));
        binding.textReservationSuccessDatetimeTo.setText(datetimeTo);

        // Adult number
        String adultNumber = String.format("%s: %s",
                getResources().getString(R.string.adult_number), reservation.adultNumber);
        binding.textReservationSuccessAdultNumber.setText(adultNumber);

        // Kid number
        String kidNumber = String.format("%s: %s", getResources().getString(R.string.kid_number),
                reservation.kidNumber);
        binding.textReservationSuccessKidNumber.setText(kidNumber);

        // Total price
        String totalPrice = String.format("%s: %sVND",
                getResources().getString(R.string.total_price), reservation.totalPrice);
        binding.textReservationSuccessTotalPrice.setText(totalPrice);

        // Created at
        ZonedDateTime createdAtObj = Instant.parse(reservation.updatedAt).atZone(currentZoneId);
        String createdAt = String.format("%s: %s", getResources().getString(R.string.created_at),
                createdAtObj.format(dateTimeFormatter));
        binding.textReservationSuccessCreatedAt.setText(createdAt);

        // Action payment
        binding.buttonReservationSuccessPayment.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getView());
            Bundle bundle = new Bundle();
            bundle.putString("reservation", new Gson().toJson(reservation));
            navController.navigate(R.id.nav_reservation_payment, bundle);
        });
        // Action go home
        binding.buttonReservationSuccessHome.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getView());
            navController.popBackStack(R.id.nav_home, false);

        });

        return root;
    }
}