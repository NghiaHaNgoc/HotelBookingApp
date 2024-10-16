package com.hotel.hotel_booking_app.ui.type_room_detail;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.databinding.FragmentTypeRoomCarouselImageBinding;

import java.util.List;

public class MyTypeRoomCarouselRecyclerViewAdapter extends RecyclerView.Adapter<MyTypeRoomCarouselRecyclerViewAdapter.ViewHolder> {

    private final List<TypeRoom.ImageTypeRoom> mValues;

    public MyTypeRoomCarouselRecyclerViewAdapter(List<TypeRoom.ImageTypeRoom> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentTypeRoomCarouselImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Glide.with(holder.image).load(mValues.get(position).link).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public TypeRoom.ImageTypeRoom mItem;

        public ViewHolder(FragmentTypeRoomCarouselImageBinding binding) {
            super(binding.getRoot());
            image = binding.typeRoomCarouselImage;
        }

    }
}