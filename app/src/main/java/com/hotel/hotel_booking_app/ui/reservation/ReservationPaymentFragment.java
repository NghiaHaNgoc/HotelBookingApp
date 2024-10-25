package com.hotel.hotel_booking_app.ui.reservation;

import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationPaymentBinding;
import com.hotel.hotel_booking_app.model.Payment;
import com.hotel.hotel_booking_app.model.Reservation;

public class ReservationPaymentFragment extends Fragment {
    private FragmentReservationPaymentBinding binding;
    private Reservation reservation;

    public ReservationPaymentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservation = new Gson().fromJson(getArguments().getString("reservation"),
                    Reservation.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationPaymentBinding.inflate(inflater, container, false);

        Glide.with(binding.imageReservationPaymentQr)
                .load(Payment.getQRPayment(reservation.id, reservation.totalPrice))
                .into(binding.imageReservationPaymentQr);

        // Bank
        String bank = getResources().getString(R.string.bank) + ": ";
        binding.textReservationPaymentBank.setText(bank);
        appendBoldText(binding.textReservationPaymentBank, "MB Bank");

        // Account number
        String accountNumber = getResources().getString(R.string.account_number) + ": ";
        binding.textReservationPaymentAccountNumber.setText(accountNumber);
        appendBoldText(binding.textReservationPaymentAccountNumber, Payment.ACCOUNT_NO);

        // Total price
        String totalPrice = getResources().getString(R.string.total_price) + ": ";
        binding.textReservationPaymentTotalPrice.setText(totalPrice);
        appendBoldText(binding.textReservationPaymentTotalPrice, reservation.totalPrice + "VND");

        // Warning
        SpannableString warnSpannableString = new SpannableString(
                getResources().getString(R.string.warning).toUpperCase() + ": "
        );

        warnSpannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, warnSpannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        warnSpannableString.setSpan(
                new ForegroundColorSpan(
                        getResources().getColor(R.color.yellow_700, getContext().getTheme())
                ), 0, warnSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);;
        binding.textReservationPaymentWarning.setText(warnSpannableString);
        binding.textReservationPaymentWarning.append(getResources().getString(R.string.warning_payment_message));


        return binding.getRoot();
    }

    private void appendBoldText(TextView tv, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spannableString);
    }
}