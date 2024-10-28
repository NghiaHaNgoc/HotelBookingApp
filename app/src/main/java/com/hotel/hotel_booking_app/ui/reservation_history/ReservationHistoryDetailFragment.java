package com.hotel.hotel_booking_app.ui.reservation_history;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationHistoryDetailBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.Reservation;
import com.hotel.hotel_booking_app.service.ApiService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReservationHistoryDetailFragment extends Fragment {
    private FragmentReservationHistoryDetailBinding binding;
    private Reservation reservation;
    private Integer id;
    ZoneId currentZoneId;
    private ApiService apiService;

    public ReservationHistoryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentZoneId = TimeZone.getDefault().toZoneId();
        if (getArguments() != null) {
            id = getArguments().getInt("reservation_id");
        }
        apiService = new ApiService(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReservationHistoryDetailBinding.inflate(inflater, container, false);

        // Action for cancel reservation
        AlertDialog.Builder alertDialogCancelSuccess = new AlertDialog.Builder(getContext());
        alertDialogCancelSuccess.setTitle(R.string.cancel_reservation_success_title);
        alertDialogCancelSuccess.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
        });

        AlertDialog alertDialogLoading = new AlertDialog.Builder(
                getActivity()
        ).setView(R.layout.loading_progress).setCancelable(false).create();

        AlertDialog.Builder alertDialogCancelConfirm = new AlertDialog.Builder(getContext());
        alertDialogCancelConfirm.setTitle(R.string.cancel_reservation_alert_title);
        alertDialogCancelConfirm.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            alertDialogLoading.show();
            apiService.cancelReservation(reservation.id).enqueue(new Callback<ApiResponse<Reservation>>() {
                @Override
                public void onResponse(Call<ApiResponse<Reservation>> call,
                                       Response<ApiResponse<Reservation>> response) {
                    ApiResponse<Reservation> body = response.body();
                    if (body.status == 200) {
                        if (reservation.status == 2) {
                            alertDialogCancelSuccess.setMessage(R.string.cancel_open_reservation_success_message);
                            alertDialogCancelSuccess.setNegativeButton(R.string.contact_us,
                                    (dialogInterface1, i1) -> {
                                    });
                        }
                        reservation = body.result;
                        setupFragment();
                        alertDialogLoading.dismiss();
                        alertDialogCancelSuccess.show();

                    } else {
                        alertDialogLoading.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Reservation>> call, Throwable throwable) {
                    alertDialogLoading.dismiss();
                }
            });
        });

        alertDialogCancelConfirm.setNegativeButton(R.string.dismiss, (dialogInterface, i) -> {

        });


        binding.buttonReservationHistoryDetailCancel.setOnClickListener(view -> {
            String alertCancelMessage = "";
            switch (reservation.status) {
                case 1:
                    alertCancelMessage =
                            getResources().getString(R.string.cancel_waiting_reservation_alert_message);
                    break;
                case 2:
                    alertCancelMessage =
                            getResources().getString(R.string.cancel_open_reservation_alert_message);
                    break;
                case 3:
                    alertCancelMessage =
                            getResources().getString(R.string.cancel_in_progress_reservation_alert_message);
                    break;
            }
            alertDialogCancelConfirm.setMessage(alertCancelMessage).show();
        });

        // Action for payment
        binding.buttonReservationHistoryDetailPayment.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getView());
            Bundle bundle = new Bundle();
            bundle.putString("reservation", new Gson().toJson(reservation));
            navController.navigate(R.id.nav_reservation_payment, bundle);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.reservationHistoryDetailContent.setVisibility(View.GONE);
        binding.progressBarReservationHistoryDetail.setVisibility(View.VISIBLE);
        apiService.getReservation(id).enqueue(new Callback<ApiResponse<Reservation>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reservation>> call,
                                   Response<ApiResponse<Reservation>> response) {
                reservation = response.body().result;
                setupFragment();
                binding.reservationHistoryDetailContent.setVisibility(View.VISIBLE);
                binding.progressBarReservationHistoryDetail.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ApiResponse<Reservation>> call, Throwable throwable) {

            }
        });

    }

    private void setupFragment() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        // Id
        String reservationId = String.format("● %s: ",
                getResources().getString(R.string.reservation_id));
        binding.textReservationHistoryDetailReservationId.setText(reservationId);
        appendBoldText(binding.textReservationHistoryDetailReservationId, reservation.id + "");

        //Check in
        String checkinAt = String.format("● %s: ", getResources().getString(R.string.check_in_at));
        binding.textReservationHistoryDetailCheckinAt.setText(checkinAt);
        ZonedDateTime checkinAtObj = Instant.parse(reservation.checkinAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailCheckinAt,
                checkinAtObj.format(dateTimeFormatter));

        // Checkout
        String checkoutAt = String.format("● %s: ", getResources().getString(R.string.checkout_at));
        binding.textReservationHistoryDetailCheckoutAt.setText(checkoutAt);
        ZonedDateTime checkoutAtObj = Instant.parse(reservation.checkoutAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailCheckoutAt,
                checkoutAtObj.format(dateTimeFormatter));

        // Adult number
        String adultNumber = String.format("● %s: ",
                getResources().getString(R.string.adult_number));
        binding.textReservationHistoryDetailAdultNumber.setText(adultNumber);
        appendBoldText(binding.textReservationHistoryDetailAdultNumber,
                reservation.adultNumber + "");

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
                statusRes = getResources().getString(R.string.status_waiting).toUpperCase();
                binding.buttonReservationHistoryDetailCancel.setEnabled(true);
                binding.buttonReservationHistoryDetailEdit.setEnabled(true);
                binding.buttonReservationHistoryDetailPayment.setVisibility(View.VISIBLE);
                break;
            case 2:
                statusRes = getResources().getString(R.string.status_open).toUpperCase();
                binding.buttonReservationHistoryDetailCancel.setEnabled(true);
                binding.buttonReservationHistoryDetailEdit.setEnabled(false);
                binding.buttonReservationHistoryDetailPayment.setVisibility(View.GONE);
                break;
            case 3:
                statusRes = getResources().getString(R.string.status_in_progress).toUpperCase();
                binding.buttonReservationHistoryDetailCancel.setEnabled(true);
                binding.buttonReservationHistoryDetailEdit.setEnabled(false);
                binding.buttonReservationHistoryDetailPayment.setVisibility(View.GONE);
                break;
            case 4:
                statusRes = getResources().getString(R.string.status_end).toUpperCase();
                binding.buttonReservationHistoryDetailCancel.setEnabled(false);
                binding.buttonReservationHistoryDetailEdit.setEnabled(false);
                binding.buttonReservationHistoryDetailPayment.setVisibility(View.GONE);
                break;
            case 5:
                statusRes = getResources().getString(R.string.status_cancel).toUpperCase();
                binding.buttonReservationHistoryDetailCancel.setEnabled(false);
                binding.buttonReservationHistoryDetailEdit.setEnabled(false);
                binding.buttonReservationHistoryDetailPayment.setVisibility(View.GONE);
                break;
        }
        appendBoldText(binding.textReservationHistoryDetailStatus, statusRes);

        // Total price
        String totalPrice = String.format("● %s: ", getResources().getString(R.string.total_price));
        binding.textReservationHistoryDetailTotalPrice.setText(totalPrice);
        appendBoldText(binding.textReservationHistoryDetailTotalPrice, reservation.totalPrice +
                "VND");

        // updated at
        String updatedAt = String.format("● %s: ", getResources().getString(R.string.updated_at));
        binding.textReservationHistoryDetailUpdatedAt.setText(updatedAt);
        ZonedDateTime updatedAtObj = Instant.parse(reservation.updatedAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailUpdatedAt,
                updatedAtObj.format(dateTimeFormatter));

        // Created at
        String createdAt = String.format("● %s: ", getResources().getString(R.string.created_at));
        binding.textReservationHistoryDetailCreatedAt.setText(createdAt);
        ZonedDateTime createdAtObj = Instant.parse(reservation.createdAt).atZone(currentZoneId);
        appendBoldText(binding.textReservationHistoryDetailCreatedAt,
                createdAtObj.format(dateTimeFormatter));

        // Room number
        String roomNumber = String.format("● %s: ", getResources().getString(R.string.room_number));
        binding.textReservationHistoryDetailRoomNumber.setText(roomNumber);
        appendBoldText(binding.textReservationHistoryDetailRoomNumber, reservation.room.roomNumber);

        // Floor
        String floor = String.format("● %s: ", getResources().getString(R.string.floor));
        binding.textReservationHistoryDetailFloor.setText(floor);
        appendBoldText(binding.textReservationHistoryDetailFloor, reservation.room.floor + "");

        //Type room
        String typeRoomTitle = String.format("● %s: ",
                getResources().getString(R.string.type_room));
        binding.textReservationHistoryDetailTypeRoomTitle.setText(typeRoomTitle);
        appendBoldText(binding.textReservationHistoryDetailTypeRoomTitle,
                reservation.room.typeRoom.title);
    }

    private void appendBoldText(TextView tv, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spannableString);
    }
}