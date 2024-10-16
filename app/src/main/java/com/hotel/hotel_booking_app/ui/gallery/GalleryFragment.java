package com.hotel.hotel_booking_app.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hotel.hotel_booking_app.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String a = sharedPref.getString("aaaa", "");
        Log.i("a", a);
//        this.on

        return root;
    }

//    @Override
//    public void onCreate( Bundle savedInstanceState) {
////        ImageView imageView = (ImageView) getView().findViewById(R.id.foo);
//        // or  (ImageView) view.findViewById(R.id.foo);
//    }

    @Override
    public void onDestroyView() {
        Log.i("DESTROY", "DESTROY");
        super.onDestroyView();
        binding = null;
    }
}