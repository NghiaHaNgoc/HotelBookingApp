package com.hotel.hotel_booking_app.model;

import android.net.Uri;

import java.net.URI;

public class Payment {
    public static final String BANK_ID = "MB";
    public static final String ACCOUNT_NO = "1111122022003";
    private static final String TEMPLATE = "compact";

    public static String getQRPayment(int reservationId, int amount) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("img.vietqr.io")
                .appendPath("image")
                .appendPath(
                        String.format("%s-%s-%s.png", BANK_ID, ACCOUNT_NO, TEMPLATE)
                )
                .appendQueryParameter("amount", String.valueOf(amount))
                .appendQueryParameter("addInfo", "RESERVATION_" + reservationId);
        return builder.build().toString();
    }
}
