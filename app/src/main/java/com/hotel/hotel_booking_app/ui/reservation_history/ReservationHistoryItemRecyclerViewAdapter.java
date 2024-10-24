package com.hotel.hotel_booking_app.ui.reservation_history;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentReservationHistoryItemBinding;
import com.hotel.hotel_booking_app.model.Reservation;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ReservationHistoryItemRecyclerViewAdapter extends RecyclerView.Adapter<ReservationHistoryItemRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Reservation> reservationList;
    private OnClickListener onClickListener;
    private ZoneId currentZoneId;
    private DateTimeFormatter dateTimeFormatter;

    public ReservationHistoryItemRecyclerViewAdapter(Context context, List<Reservation> list) {
        this.context = context;
        if (list != null) {
            reservationList = list;
        } else {
            reservationList = new ArrayList<>();
        }
        currentZoneId = TimeZone.getDefault().toZoneId();
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentReservationHistoryItemBinding binding =
                FragmentReservationHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.typeRoom.setText(reservation.room.typeRoom.title);

        // Room
        String roomStr = context.getResources().getString(R.string.room) + ": ";
        holder.room.setText(roomStr);
        appendColoredBoldText(holder.room, reservation.room.roomNumber, Color.BLACK);

        // Status
        String statusStr = context.getResources().getString(R.string.status) + ": ";
        holder.status.setText(statusStr);
        int statusColor;
        switch (reservation.status) {
            case 1:
                statusStr = context.getResources().getString(R.string.status_waiting).toUpperCase();
                statusColor = context.getResources().getColor(R.color.teal_700, context.getTheme());
                appendColoredBoldText(holder.status, statusStr, statusColor);
                break;
            case 2:
                statusStr = context.getResources().getString(R.string.status_open).toUpperCase();
                statusColor = context.getResources().getColor(R.color.purple_200, context.getTheme());
                appendColoredBoldText(holder.status, statusStr, statusColor);
                break;
            case 3:
                statusStr = context.getResources().getString(R.string.status_in_progress).toUpperCase();
                statusColor = context.getResources().getColor(R.color.purple_700, context.getTheme());
                appendColoredBoldText(holder.status, statusStr, statusColor);
                break;
            case 4:
                statusStr = context.getResources().getString(R.string.status_end).toUpperCase();
                statusColor = context.getResources().getColor(R.color.black, context.getTheme());
                appendColoredBoldText(holder.status, statusStr, statusColor);
                break;
            case 5:
                statusStr = context.getResources().getString(R.string.status_cancel).toUpperCase();
                appendColoredBoldText(holder.status, statusStr, Color.RED);
                break;
        }


        // Checkin at
        ZonedDateTime checkinAtObj = Instant.parse(reservation.checkinAt).atZone(currentZoneId);
        holder.checkinAt.setText(checkinAtObj.format(dateTimeFormatter));

        // Checkout at
        ZonedDateTime checkoutAtObj = Instant.parse(reservation.checkoutAt).atZone(currentZoneId);
        holder.checkoutAt.setText(checkoutAtObj.format(dateTimeFormatter));

        // Handle onclick listener
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(reservation);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    private void appendColoredBoldText(TextView tv, String text, int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spannableString);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView typeRoom;
        public final TextView checkinAt;
        public final TextView checkoutAt;
        public final TextView room;
        public final TextView status;

        public ViewHolder(@NonNull FragmentReservationHistoryItemBinding binding) {
            super(binding.getRoot());
            typeRoom = binding.reservationHistoryItemTypeRoom;
            checkinAt = binding.reservationHistoryItemCheckinAt;
            checkoutAt = binding.reservationHistoryItemCheckoutAt;
            room = binding.reservationHistoryItemRoom;
            status = binding.reservationHistoryItemStatus;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(Reservation item);
    }
}
