package com.hotel.hotel_booking_app.ui.reservation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationBinding;
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

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding binding;
    private TypeRoom typeRoom;
    private TextInputEditText editTextDateFromPicker;
    private TextInputEditText editTextTimeFromPicker;
    private TextInputEditText editTextDateToPicker;
    private TextInputEditText editTextTimeToPicker;
    private TextInputEditText editTextAdultNumber;
    private TextInputEditText editTextKidNumber;
    private ApiService apiService;
    AtomicReference<ZonedDateTime> zonedDateTimeFrom;
    AtomicReference<ZonedDateTime> zonedDateTimeTo;

    public ReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String typeRoomJson = getArguments().getString("type_room");
        typeRoom = new Gson().fromJson(typeRoomJson, TypeRoom.class);
        apiService = new ApiService(getContext());
        zonedDateTimeFrom =
                new AtomicReference<>(Instant.EPOCH.atZone(TimeZone.getDefault().toZoneId()));
        zonedDateTimeTo =
                new AtomicReference<>(Instant.EPOCH.atZone(TimeZone.getDefault().toZoneId()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationBinding.inflate(inflater, container, false);
        editTextDateFromPicker = binding.editTextDateFromPicker;
        editTextTimeFromPicker = binding.editTextTimeFromPicker;
        editTextDateToPicker = binding.editTextDateToPicker;
        editTextTimeToPicker = binding.editTextTimeToPicker;
        editTextAdultNumber = binding.editTextAdultNumber;
        editTextKidNumber = binding.editTextKidNumber;

        editTextDateFromPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.dateFromPickerInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextTimeFromPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.timeFromPickerInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextDateToPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.dateToPickerInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextTimeToPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.timeToPickerInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextAdultNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.inputLayoutAdultNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Calendar calendar = Calendar.getInstance();

        AlertDialog.Builder alertDialogError = new AlertDialog.Builder(getActivity());
        alertDialogError.setPositiveButton(getResources().getString(R.string.ok),
                (dialog, id) -> {
                });
        AlertDialog alertDialogLoading =
                new AlertDialog.Builder(getActivity()).setView(R.layout.loading_progress).setCancelable(false).create();

        // Setup date from picker dialog
        DatePickerDialog dateFromPickerDialog = new DatePickerDialog(getContext(), (datePicker, i
                , i1, i2) -> {
            zonedDateTimeFrom.set(zonedDateTimeFrom.get().withYear(i).withMonth(i1 + 1).withDayOfMonth(i2));

            String dateFormat = String.format("%02d-%02d-%02d", i2, i1 + 1, i);
            editTextDateFromPicker.setText(dateFormat);
            editTextDateFromPicker.setError(null);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dateFromPickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        editTextDateFromPicker.setOnClickListener(view -> {
            dateFromPickerDialog.show();
        });

        // Setup time from picker dialog
        TimePickerDialog timeFromPickerDialog = new TimePickerDialog(getContext(), (timePicker, i
                , i1) -> {
            zonedDateTimeFrom.set(zonedDateTimeFrom.get().withHour(i).withMinute(i1));
            String timeFormat = String.format("%02d:%02d", i, i1);
            editTextTimeFromPicker.setText(timeFormat);
            editTextTimeFromPicker.setError(null);
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        editTextTimeFromPicker.setOnClickListener(view -> {
            timeFromPickerDialog.show();
        });

        // Setup date to picker dialog
        DatePickerDialog dateToPickerDialog = new DatePickerDialog(getContext(), (datePicker, i,
                                                                                  i1, i2) -> {
            zonedDateTimeTo.set(zonedDateTimeTo.get().withYear(i).withMonth(i1 + 1).withDayOfMonth(i2));
            String dateFormat = String.format("%02d-%02d-%02d", i2, i1 + 1, i);
            editTextDateToPicker.setText(dateFormat);
            editTextDateToPicker.setError(null);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dateToPickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        editTextDateToPicker.setOnClickListener(view -> {
            dateToPickerDialog.show();
        });

        // Cập nhật MinDate của dateToPickerDialog mỗi khi mở nó
        editTextDateToPicker.setOnClickListener(view -> {
            // Kiểm tra nếu người dùng đã chọn ngày "From" và cập nhật MinDate của dateToPickerDialog
            if (zonedDateTimeFrom.get() != null) {
                Calendar selectedDateFrom = Calendar.getInstance();
                selectedDateFrom.set(zonedDateTimeFrom.get().getYear(), zonedDateTimeFrom.get().getMonthValue() - 1, zonedDateTimeFrom.get().getDayOfMonth());
                dateToPickerDialog.getDatePicker().setMinDate(selectedDateFrom.getTimeInMillis());
            }
            dateToPickerDialog.show();
        });

        // Setup time to picker dialog
        TimePickerDialog timeToPickerDialog = new TimePickerDialog(getContext(), (timePicker, i,
                                                                                  i1) -> {
            zonedDateTimeTo.set(zonedDateTimeTo.get().withHour(i).withMinute(i1));
            String timeFormat = String.format("%02d:%02d", i, i1);
            editTextTimeToPicker.setText(timeFormat);
            editTextTimeToPicker.setError(null);
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        editTextTimeToPicker.setOnClickListener(view -> {
            timeToPickerDialog.show();
        });

        // Capacity filter
        editTextKidNumber.setFilters(new InputFilter[]{(charSequence, i, i1, spanned, i2, i3) -> {
            try {
                int input = Integer.parseInt(spanned.toString() + charSequence.toString());
                if (input >= 0 && input <= typeRoom.kidsCapacity)
                    return null;
                else
                    return "";

            } catch (NumberFormatException e) {
                Log.i("ERROR", e.toString());
            }
            return null;
        }});
        editTextAdultNumber.setFilters(new InputFilter[]{(charSequence, i, i1, spanned, i2, i3) -> {
            try {
                int input = Integer.parseInt(spanned.toString() + charSequence.toString());
                if (input >= 1 && input <= typeRoom.adultCapacity)
                    return null;
                else
                    return "";

            } catch (NumberFormatException e) {
                Log.i("ERROR", e.toString());
            }
            return null;
        }});

        binding.buttonSubmitReservation.setOnClickListener(view -> {
            String dateFrom = editTextDateFromPicker.getText().toString();
            String timeFrom = editTextTimeFromPicker.getText().toString();
            String dateTo = editTextDateToPicker.getText().toString();
            String timeTo = editTextTimeToPicker.getText().toString();
            String adultNumber = editTextAdultNumber.getText().toString();
            String kidNumber = editTextKidNumber.getText().toString();
            Instant checkinAt = zonedDateTimeFrom.get().toInstant();
            Instant checkoutAt = zonedDateTimeTo.get().toInstant();
            if (!checkAllFields(dateFrom, timeFrom, dateTo, timeTo, adultNumber)) {
                return;
            }
            if (checkinAt.compareTo(checkoutAt) >= 0) {
                alertDialogError.setTitle(getResources().getString(R.string.invalid_input))
                        .setMessage(getResources().getString(R.string.datetime_from_must_less_than_to_message))
                        .show();
                return;
            }
            Duration duration = Duration.between(checkinAt, checkoutAt);
            int totalPrice = (int) Math.ceil(duration.toHours()) * typeRoom.basePrice;

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
                            typeRoom.id);
            alertDialogLoading.show();

            // Handle API request
            apiService.addReservation(input).enqueue(new Callback<ApiResponse<Reservation>>() {
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
                    Bundle bundle = new Bundle();
                    bundle.putString("reservation_result",
                            new Gson().toJson(body.result));
                    navController.navigate(R.id.nav_reservation_success, bundle);
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
        setupFragment();
    }

    private void setupFragment() {
        // Clear on resume
        editTextDateFromPicker.setText("");
        editTextDateToPicker.setText("");
        editTextTimeFromPicker.setText("");
        editTextTimeToPicker.setText("");
        editTextAdultNumber.setText("");
        editTextKidNumber.setText("");

        // Show type room header
        if (!typeRoom.images.isEmpty())
            Glide.with(binding.imageReservation).load(typeRoom.images.get(0)).placeholder(R.drawable.ic_menu_camera).into(binding.imageReservation);

        switch (LanguageUtil.getLanguage()) {
            case LanguageUtil.VIETNAMESE:
                binding.textReservationTypeRoomTitle.setText(typeRoom.title);
                break;
            case LanguageUtil.JAPANESE:
                binding.textReservationTypeRoomTitle.setText(typeRoom.titleJa);
                break;
            default:
                binding.textReservationTypeRoomTitle.setText(typeRoom.titleEn);
        }


        // Show capability
        binding.inputLayoutAdultNumber.setHint(String.format("%s (1 - %s)",
                getContext().getResources().getString(R.string.adult_number),
                typeRoom.adultCapacity));
        binding.inputLayoutKidNumber.setHint(String.format("%s (0 - %s)",
                getContext().getResources().getString(R.string.kid_number), typeRoom.kidsCapacity));

    }


    private boolean checkAllFields(String dateFrom, String timeFrom, String dateTo, String timeTo
            , String adultNumber) {
        String requiredErrorMessage =
                getContext().getResources().getString(R.string.require_input_message);
//        String exceedCapacityErrorMessage =
//                getContext().getResources().getString(R.string.exceed_capacity_limit_message);
        boolean isValid = true;
        if (dateFrom.isEmpty()) {
            binding.dateFromPickerInputLayout.setError(requiredErrorMessage);
            isValid = false;
        }

        if (timeFrom.isEmpty()) {
            binding.timeFromPickerInputLayout.setError(requiredErrorMessage);
            isValid = false;
        }

        if (dateTo.isEmpty()) {
            binding.dateToPickerInputLayout.setError(requiredErrorMessage);
            isValid = false;
        }

        if (timeTo.isEmpty()) {
            binding.timeToPickerInputLayout.setError(requiredErrorMessage);
            isValid = false;
        }

        if (adultNumber.isEmpty()) {
            binding.inputLayoutAdultNumber.setError(requiredErrorMessage);
            isValid = false;
        }
        // after all validation return true.
        return isValid;
    }
}