package com.hotel.hotel_booking_app.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.databinding.FragmentHomeTypeRoomCardBinding;
import com.hotel.hotel_booking_app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyHomeTypeRoomCardRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeTypeRoomCardRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final List<TypeRoom> mValues;
    private OnClickListener onClickListener;

    public MyHomeTypeRoomCardRecyclerViewAdapter(Context context, List<TypeRoom> items) {
        this.context = context;
        if (items != null)
          mValues = items;
        else
            mValues = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentHomeTypeRoomCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).title);
        String price = String.format("%s: %sVND",
                StringUtil.capitalize(context.getResources().getString(R.string.price)),
                mValues.get(position).basePrice);
        holder.price.setText(price);

        TypeRoom.ImageTypeRoom imageLink = mValues.get(position).images.get(0);
        if (imageLink != null)
            Glide.with(holder.image).load(imageLink.link).placeholder(R.drawable.ic_menu_gallery).into(holder.image);
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(mValues.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView title;
        public final TextView price;
        public TypeRoom mItem;


        public ViewHolder(FragmentHomeTypeRoomCardBinding binding) {
            super(binding.getRoot());
            image = binding.imageHomeTypeRoom;
            title = binding.textHomeTypeRoomTitle;
            price = binding.textHomeTypeRoomPrice;
        }

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(TypeRoom item);
    }
}