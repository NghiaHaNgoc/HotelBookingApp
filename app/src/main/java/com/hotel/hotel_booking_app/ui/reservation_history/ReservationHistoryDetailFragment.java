package com.hotel.hotel_booking_app.ui.reservation_history;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationHistoryDetailBinding;
import com.hotel.hotel_booking_app.model.Reservation;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


public class ReservationHistoryDetailFragment extends Fragment {
    private FragmentReservationHistoryDetailBinding binding;
    private Reservation reservation;
    ZoneId currentZoneId;

    public ReservationHistoryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentZoneId = TimeZone.getDefault().toZoneId();
        if (getArguments() != null) {
            reservation = new Gson().fromJson(
                    getArguments().getString("reservation"),
                    Reservation.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReservationHistoryDetailBinding.inflate(inflater, container, false);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        // Id
        String reservationId = String.format("● %s: ", getResources().getString(R.string.reservation_id));
        binding.textReservationHistoryDetailReservationId.setText(reservationId);
        appendBoldText(binding.textReservationHistoryDetailReservationId, reservation.id + "");

        //Check in
        String checkinAt = String.format("● %s: ", getResources().getString(R.string.check_in_at));
        binding.textReservationHistoryDetailCheckinAt.setText(checkinAt);
        ZonedDateTime checkinAtObj = Instant.parse(reservation.checkinAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailCheckinAt, checkinAtObj.format(dateTimeFormatter));

        // Checkout
        String checkoutAt = String.format("● %s: ", getResources().getString(R.string.checkout_at));
        binding.textReservationHistoryDetailCheckoutAt.setText(checkoutAt);
        ZonedDateTime checkoutAtObj = Instant.parse(reservation.checkoutAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailCheckoutAt, checkoutAtObj.format(dateTimeFormatter));

        // Adult number
        String adultNumber = String.format("● %s: ", getResources().getString(R.string.adult_number));
        binding.textReservationHistoryDetailAdultNumber.setText(adultNumber);
        appendBoldText(binding.textReservationHistoryDetailAdultNumber, reservation.adultNumber + "");

        // Kid number
        String kidNumber = String.format("● %s: ", getResources().getString(R.string.kid_number));
        binding.textReservationHistoryDetailKidNumber.setText(kidNumber);
        appendBoldText(binding.textReservationHistoryDetailKidNumber, reservation.kidNumber + "");

        // Status
        String status = String.format("● %s: ", getResources().getString(R.string.status));
        binding.textReservationHistoryDetailStatus.setText(status);
        String statusRes = "";
        switch (reservation.status) {
            case 1:
                statusRes = getResources().getString(R.string.status_open).toUpperCase();
                appendBoldText(binding.textReservationHistoryDetailStatus, statusRes);
                break;
            case 2:
                statusRes = getResources().getString(R.string.status_in_progress).toUpperCase();
                appendBoldText(binding.textReservationHistoryDetailStatus, statusRes);
                break;
            case 3:
                statusRes = getResources().getString(R.string.status_end).toUpperCase();
                appendBoldText(binding.textReservationHistoryDetailStatus, statusRes);
                break;
            case 4:
                statusRes = getResources().getString(R.string.status_cancel).toUpperCase();
                appendBoldText(binding.textReservationHistoryDetailStatus, statusRes);
                break;
        }

        // Total price
        String totalPrice = String.format("● %s: ", getResources().getString(R.string.total_price));
        binding.textReservationHistoryDetailTotalPrice.setText(totalPrice);
        appendBoldText(binding.textReservationHistoryDetailTotalPrice, reservation.totalPrice + "VND");

        // updated at
        String updatedAt = String.format("● %s: ", getResources().getString(R.string.updated_at));
        binding.textReservationHistoryDetailUpdatedAt.setText(updatedAt);
        ZonedDateTime updatedAtObj = Instant.parse(reservation.updatedAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailUpdatedAt, updatedAtObj.format(dateTimeFormatter));

        // Created at
        String createdAt = String.format("● %s: ", getResources().getString(R.string.created_at));
        binding.textReservationHistoryDetailCreatedAt.setText(createdAt);
        ZonedDateTime createdAtObj = Instant.parse(reservation.createdAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailCreatedAt, createdAtObj.format(dateTimeFormatter));

        // Room number
        String roomNumber = String.format("● %s: ", getResources().getString(R.string.room_number));
        binding.textReservationHistoryDetailRoomNumber.setText(roomNumber);
        appendBoldText(binding.textReservationHistoryDetailRoomNumber, reservation.room.roomNumber);

        // Floor
        String floor = String.format("● %s: ", getResources().getString(R.string.floor));
        binding.textReservationHistoryDetailFloor.setText(floor);
        appendBoldText(binding.textReservationHistoryDetailFloor, reservation.room.floor + "");

        //Type room
        String typeRoomTitle = String.format("● %s: ", getResources().getString(R.string.type_room));
        binding.textReservationHistoryDetailTypeRoomTitle.setText(typeRoomTitle);
        appendBoldText(binding.textReservationHistoryDetailTypeRoomTitle, reservation.room.typeRoom.title);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void appendBoldText(TextView tv, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spannableString);
    }
}