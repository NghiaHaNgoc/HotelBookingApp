package com.hotel.hotel_booking_app.ui.reservation_history;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationUpdateBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.Reservation;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.service.ApiService;
import com.hotel.hotel_booking_app.util.LanguageUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReservationUpdateFragment extends Fragment {
    FragmentReservationUpdateBinding binding;
    Reservation reservation;
    private TextInputEditText editTextUpdateDateFromPicker;
    private TextInputEditText editTextUpdateTimeFromPicker;
    private TextInputEditText editTextUpdateDateToPicker;
    private TextInputEditText editTextUpdateTimeToPicker;
    private TextInputEditText editTextUpdateAdultNumber;
    private TextInputEditText editTextUpdateKidNumber;
    private ApiService apiService;
    AtomicReference<ZonedDateTime> zonedDateTimeFrom;
    AtomicReference<ZonedDateTime> zonedDateTimeTo;

    public ReservationUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservation = new Gson().fromJson(
                    getArguments().getString("reservation"),
                    Reservation.class);
            zonedDateTimeFrom = new AtomicReference<>(
                    Instant.parse(reservation.checkinAt)
                            .atZone(TimeZone.getDefault().toZoneId())
            );
            zonedDateTimeTo = new AtomicReference<>(
                    Instant.parse(reservation.checkoutAt)
                            .atZone(TimeZone.getDefault().toZoneId())
            );
        }
        apiService = new ApiService(getContext());


    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationUpdateBinding.inflate(inflater, container, false);
        editTextUpdateDateFromPicker = binding.editTextUpdateDateFromPicker;
        editTextUpdateTimeFromPicker = binding.editTextUpdateTimeFromPicker;
        editTextUpdateDateToPicker = binding.editTextUpdateDateToPicker;
        editTextUpdateTimeToPicker = binding.editTextUpdateTimeToPicker;
        editTextUpdateAdultNumber = binding.editTextUpdateAdultNumber;
        editTextUpdateKidNumber = binding.editTextUpdateKidNumber;

        AlertDialog.Builder alertDialogError = new AlertDialog.Builder(getActivity())
                .setPositiveButton(getResources().getString(R.string.ok),
                        (dialog, id) -> {
                        });
        AlertDialog alertDialogLoading =
                new AlertDialog.Builder(getActivity()).setView(R.layout.loading_progress).setCancelable(false).create();

        // Setup date from picker dialog
        editTextUpdateDateFromPicker.setText(
                String.format("%02d-%02d-%02d",
                        zonedDateTimeFrom.get().getDayOfMonth(),
                        zonedDateTimeFrom.get().getMonthValue(),
                        zonedDateTimeFrom.get().getYear()));
        DatePickerDialog dateFromPickerDialog = new DatePickerDialog(getContext(), (datePicker, i
                , i1, i2) -> {
            zonedDateTimeFrom.set(zonedDateTimeFrom.get().withYear(i).withMonth(i1 + 1).withDayOfMonth(i2));

            String dateFormat = String.format("%02d-%02d-%02d", i2, i1 + 1, i);
            editTextUpdateDateFromPicker.setText(dateFormat);
            editTextUpdateDateFromPicker.setError(null);

        }, zonedDateTimeFrom.get().getYear(), zonedDateTimeFrom.get().getMonthValue() - 1,
                zonedDateTimeFrom.get().getDayOfMonth());
        dateFromPickerDialog.getDatePicker().setMinDate(Instant.now().toEpochMilli());
        editTextUpdateDateFromPicker.setOnClickListener(view -> {
            dateFromPickerDialog.show();
        });

        // Setup time from picker dialog
        editTextUpdateTimeFromPicker.setText(
                String.format("%02d:%02d",
                        zonedDateTimeFrom.get().getHour(),
                        zonedDateTimeFrom.get().getMinute()
                )
        );
        TimePickerDialog timeFromPickerDialog = new TimePickerDialog(getContext(), (timePicker, i,
                                                                                    i1) -> {
            zonedDateTimeFrom.set(zonedDateTimeFrom.get().withHour(i).withMinute(i1));
            String timeFormat = String.format("%02d:%02d", i, i1);
            editTextUpdateTimeFromPicker.setText(timeFormat);
            editTextUpdateTimeFromPicker.setError(null);
        }, zonedDateTimeFrom.get().getHour(), zonedDateTimeFrom.get().getMinute(), true);
        editTextUpdateTimeFromPicker.setOnClickListener(view -> {
            timeFromPickerDialog.show();
        });

        // Setup date to picker dialog
        editTextUpdateDateToPicker.setText(
                String.format("%02d-%02d-%02d",
                        zonedDateTimeTo.get().getDayOfMonth(),
                        zonedDateTimeTo.get().getMonthValue(),
                        zonedDateTimeTo.get().getYear()));
        DatePickerDialog dateToPickerDialog = new DatePickerDialog(getContext(), (datePicker, i
                , i1, i2) -> {
            zonedDateTimeTo.set(zonedDateTimeTo.get().withYear(i).withMonth(i1 + 1).withDayOfMonth(i2));

            String dateFormat = String.format("%02d-%02d-%02d", i2, i1 + 1, i);
            editTextUpdateDateToPicker.setText(dateFormat);
            editTextUpdateDateToPicker.setError(null);

        }, zonedDateTimeTo.get().getYear(), zonedDateTimeTo.get().getMonthValue() - 1,
                zonedDateTimeTo.get().getDayOfMonth());
        dateToPickerDialog.getDatePicker().setMinDate(Instant.now().toEpochMilli());
        editTextUpdateDateToPicker.setOnClickListener(view -> {
            dateToPickerDialog.show();
        });

        // Setup time from picker dialog
        editTextUpdateTimeToPicker.setText(
                String.format("%02d:%02d",
                        zonedDateTimeTo.get().getHour(),
                        zonedDateTimeTo.get().getMinute()
                )
        );
        TimePickerDialog timeToPickerDialog = new TimePickerDialog(getContext(), (timePicker, i,
                                                                                  i1) -> {
            zonedDateTimeTo.set(zonedDateTimeTo.get().withHour(i).withMinute(i1));
            String timeFormat = String.format("%02d:%02d", i, i1);
            editTextUpdateTimeToPicker.setText(timeFormat);
            editTextUpdateTimeToPicker.setError(null);
        }, zonedDateTimeTo.get().getHour(), zonedDateTimeTo.get().getMinute(), true);
        editTextUpdateTimeToPicker.setOnClickListener(view -> {
            timeToPickerDialog.show();
        });

        // Capacity filter
        editTextUpdateKidNumber.setText(reservation.kidNumber + "");
        editTextUpdateKidNumber.setFilters(new InputFilter[]{(charSequence, i, i1, spanned, i2,
                                                              i3) -> {
            try {
                int input = Integer.parseInt(spanned.toString() + charSequence.toString());
                if (input >= 0 && input <= reservation.room.typeRoom.kidsCapacity)
                    return null;
                else
                    return "";

            } catch (NumberFormatException e) {
                Log.i("ERROR", e.toString());
            }
            return null;
        }});
        editTextUpdateAdultNumber.setText(reservation.adultNumber + "");
        editTextUpdateAdultNumber.setFilters(new InputFilter[]{(charSequence, i, i1, spanned, i2,
                                                                i3) -> {
            try {
                int input = Integer.parseInt(spanned.toString() + charSequence.toString());
                if (input >= 1 && input <= reservation.room.typeRoom.adultCapacity)
                    return null;
                else
                    return "";

            } catch (NumberFormatException e) {
                Log.i("ERROR", e.toString());
            }
            return null;
        }});

        //Handle submit
        binding.buttonSubmitUpdateReservation.setOnClickListener(view -> {
            String dateFrom = editTextUpdateDateFromPicker.getText().toString();
            String timeFrom = editTextUpdateTimeFromPicker.getText().toString();
            String dateTo = editTextUpdateDateToPicker.getText().toString();
            String timeTo = editTextUpdateTimeToPicker.getText().toString();
            String adultNumber = editTextUpdateAdultNumber.getText().toString();
            String kidNumber = editTextUpdateKidNumber.getText().toString();
            Instant checkinAt = zonedDateTimeFrom.get().toInstant();
            Instant checkoutAt = zonedDateTimeTo.get().toInstant();

            if (!checkAllRequiredFields(dateFrom, timeFrom, dateTo, timeTo, adultNumber)) {
                return;
            }
            if (checkinAt.compareTo(checkoutAt) >= 0) {
                alertDialogError.setTitle(getResources().getString(R.string.invalid_input))
                        .setMessage(getResources().getString(R.string.datetime_from_must_less_than_to_message))
                        .show();
                return;
            }

            Duration duration = Duration.between(checkinAt, checkoutAt);
            int totalPrice =
                    (int) Math.ceil(duration.toHours()) * reservation.room.typeRoom.basePrice;

            int adultNumberInt = Integer.parseInt(adultNumber);
            int kidNumberInt = 0;
            try {
                kidNumberInt = Integer.parseInt(kidNumber);
            } catch (NumberFormatException e) {
            }

            Reservation.ReservationInput input =
                    new Reservation.ReservationInput(
                            checkinAt.toString(),
                            checkoutAt.toString(),
                            adultNumberInt,
                            kidNumberInt,
                            totalPrice,
                            reservation.room.typeRoom.id);
            alertDialogLoading.show();

            // Handle API request
            apiService.updateReservation(reservation.id, input).enqueue(new Callback<ApiResponse<Reservation>>() {
                @Override
                public void onResponse(Call<ApiResponse<Reservation>> call,
                                       Response<ApiResponse<Reservation>> response) {
                    alertDialogLoading.dismiss();
                    ApiResponse<Reservation> body = response.body();
                    if (body == null) {
                        alertDialogError.setTitle(getResources().getString(R.string.out_of_room_title));
                        alertDialogError.setMessage(getResources().getString(R.string.out_of_room_message));
                        alertDialogError.show();
                        return;
                    }
                    NavController navController = Navigation.findNavController(getView());
                    navController.popBackStack();
                }

                @Override
                public void onFailure(Call<ApiResponse<Reservation>> call, Throwable throwable) {
                    alertDialogLoading.dismiss();
                }
            });
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupFragment() {
        TypeRoom typeRoom = reservation.room.typeRoom;
        switch (LanguageUtil.getLanguage()) {
            case LanguageUtil.VIETNAMESE:
                binding.textReservationUpdateTypeRoomTitle.setText(typeRoom.title);
                break;
            case LanguageUtil.JAPANESE:
                binding.textReservationUpdateTypeRoomTitle.setText(typeRoom.titleJa);
                break;
            default:
                binding.textReservationUpdateTypeRoomTitle.setText(typeRoom.titleEn);
        }


        // Show capability
        binding.inputLayoutUpdateAdultNumber.setHint(String.format("%s (1 - %s)",
                getContext().getResources().getString(R.string.adult_number),
                typeRoom.adultCapacity));
        binding.inputLayoutUpdateKidNumber.setHint(String.format("%s (0 - %s)",
                getContext().getResources().getString(R.string.kid_number), typeRoom.kidsCapacity));

    }

    private boolean checkAllRequiredFields(String dateFrom, String timeFrom, String dateTo,
                                           String timeTo
            , String adultNumber) {
        String requiredErrorMessage =
                getContext().getResources().getString(R.string.require_input_message);
        String exceedCapacityErrorMessage =
                getContext().getResources().getString(R.string.exceed_capacity_limit_message);
        boolean isValid = true;
        if (dateFrom.isEmpty()) {
            editTextUpdateDateFromPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (timeFrom.isEmpty()) {
            editTextUpdateTimeFromPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (dateTo.isEmpty()) {
            editTextUpdateDateToPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (timeTo.isEmpty()) {
            editTextUpdateTimeToPicker.setError(requiredErrorMessage);
            isValid = false;
        }

        if (adultNumber.isEmpty()) {
            editTextUpdateAdultNumber.setError(requiredErrorMessage);
            isValid = false;
        }
        // after all validation return true.
        return isValid;
    }
}