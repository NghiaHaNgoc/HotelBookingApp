package com.hotel.hotel_booking_app.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.databinding.FragmentHomeTypeRoomCardBinding;
import com.hotel.hotel_booking_app.util.LanguageUtil;
import com.hotel.hotel_booking_app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class MyHomeTypeRoomCardRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeTypeRoomCardRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final List<TypeRoom> typeRoomList;
    private OnClickListener onClickListener;

    public MyHomeTypeRoomCardRecyclerViewAdapter(Context context, List<TypeRoom> items) {
        this.context = context;
        if (items != null)
            typeRoomList = items;
        else
            typeRoomList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentHomeTypeRoomCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TypeRoom typeRoom = typeRoomList.get(position);
        switch (LanguageUtil.getLanguage()) {
            case LanguageUtil.VIETNAMESE:
                holder.title.setText(typeRoom.title);
                break;
            case LanguageUtil.JAPANESE:
                holder.title.setText(typeRoom.titleJa);
                break;
            default:
                holder.title.setText(typeRoom.titleEn);
        }

        @SuppressLint("DefaultLocale")
        String capacityFormat = String.format(
                "%d %s %s %d %s",
                typeRoomList.get(position).adultCapacity,
                context.getResources().getString(R.string.adults),
                context.getResources().getString(R.string.and),
                typeRoomList.get(position).kidsCapacity,
                context.getResources().getString(R.string.kids)
        );


        String capacity = String.format("%s: %s",
                StringUtil.capitalize(context.getResources().getString(R.string.capacity)),
                capacityFormat);
        holder.capacity.setText(capacity);

        String price = String.format("%s: %s VND/%s",
                StringUtil.capitalize(context.getResources().getString(R.string.price)),
                typeRoomList.get(position).basePrice,
                context.getResources().getString(R.string.hour));
        holder.price.setText(price);

        if (!typeRoomList.get(position).images.isEmpty())
            Glide.with(holder.image).load(typeRoomList.get(position).images.get(0)).placeholder(R.drawable.ic_menu_gallery).into(holder.image);
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(typeRoomList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeRoomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView title;
        public final TextView capacity;
        public final TextView price;


        public ViewHolder(FragmentHomeTypeRoomCardBinding binding) {
            super(binding.getRoot());
            image = binding.imageHomeTypeRoom;
            title = binding.textHomeTypeRoomTitle;
            capacity = binding.textHomeTypeRoomCapacity;
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