package com.hotel.hotel_booking_app.ui.type_room_detail;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentTypeRoomDetailBinding;
import com.hotel.hotel_booking_app.model.Amenity;
import com.hotel.hotel_booking_app.model.TypeRoom;
import com.hotel.hotel_booking_app.util.LanguageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TypeRoomDetailFragment extends Fragment {
    private FragmentTypeRoomDetailBinding binding;
    private TypeRoom typeRoom;
    private List<Amenity> amenityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTypeRoomDetailBinding.inflate(inflater, container, false);

        String typeRoomJson = getArguments().getString("type_room");
        typeRoom = new Gson().fromJson(typeRoomJson, TypeRoom.class);
        String amenityJson = getArguments().getString("amenity");
        amenityList = new Gson().fromJson(amenityJson,
                new TypeToken<List<Amenity>>() {
                }.getType());


        binding.buttonTypeRoomReservation.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getView());

            if (getContext().getSharedPreferences("account", Context.MODE_PRIVATE).getBoolean(
                    "isSignedIn", false)) {
                Bundle bundle = new Bundle();
                bundle.putString("type_room", typeRoomJson);
                navController.navigate(R.id.nav_reservation, bundle);
            } else {
                navController.navigate(R.id.nav_sign_in);
            }

        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupFragment();
    }

    private void setupFragment() {
        // Title
        binding.typeRoomTitle.setText(typeRoom.title);
        switch (LanguageUtil.getLanguage()) {
            case LanguageUtil.VIETNAMESE:
                binding.typeRoomTitle.setText(typeRoom.title);
                break;
            case LanguageUtil.JAPANESE:
                binding.typeRoomTitle.setText(typeRoom.titleJa);
                break;
            default:
                binding.typeRoomTitle.setText(typeRoom.titleEn);
        }

        // View direction
        String viewDirection = String.format("● %s: %s",
                getResources().getString(R.string.view_direction),
                getResources().getString(typeRoom.viewDirection == 1 ?
                        R.string.view_direction_river : R.string.view_direction_city));
        binding.typeRoomViewDirection.setText(viewDirection);

        // Preferential services
        String preferentialServiceTitle = String.format("● %s:",
                getResources().getString(R.string.preferential_services));
        binding.typeRoomPreferentialServiceTitle.setText(preferentialServiceTitle);
        Spanned preferentialService;
        switch (LanguageUtil.getLanguage()) {
            case LanguageUtil.VIETNAMESE:
                preferentialService = Html.fromHtml(typeRoom.preferentialServices,
                        Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
                break;
            case LanguageUtil.JAPANESE:
                preferentialService = Html.fromHtml(typeRoom.preferentialServicesJa,
                        Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
                break;
            default:
                preferentialService = Html.fromHtml(typeRoom.preferentialServicesEn,
                        Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
        }

        binding.typeRoomPreferentialService.setText(preferentialService);

        // Size
        String size = String.format("● %s: %s㎡",
                getResources().getString(R.string.size), typeRoom.size);
        binding.typeRoomSize.setText(size);
        // Adult capacity
        String adultCapacity = String.format("● %s: %s",
                getResources().getString(R.string.adult_capacity),
                typeRoom.adultCapacity);
        binding.typeRoomAdultCapacity.setText(adultCapacity);

        // Kid capacity
        String kidCapacity = String.format("● %s: %s",
                getResources().getString(R.string.kid_capacity),
                typeRoom.kidsCapacity);
        binding.typeRoomKidCapacity.setText(kidCapacity);

        // Base price
        String price = String.format("● %s: %sVND/%s",
                getResources().getString(R.string.price),
                typeRoom.basePrice,
                getResources().getString(R.string.hour));
        binding.typeRoomBasePrice.setText(price);

        // Amenity
        binding.typeRoomAmenities.setText(String.format("● %s:",
                getResources().getString(R.string.amenities)));
        ArrayList<Amenity> generalAmenities = new ArrayList<>();
        ArrayList<Amenity> bathroomAmenities = new ArrayList<>();
        ArrayList<Amenity> otherAmenities = new ArrayList<>();
        amenityList.forEach(amenity -> {
            switch (amenity.type) {
                case Amenity.GENERAL_TYPE:
                    generalAmenities.add(amenity);
                    break;
                case Amenity.BATHROOM_TYPE:
                    bathroomAmenities.add(amenity);
                    break;
                case Amenity.OTHER_TYPE:
                    otherAmenities.add(amenity);
            }
        });

        Function<Amenity, String> mapAmenity = amenity -> {
            switch (LanguageUtil.getLanguage()) {
                case LanguageUtil.VIETNAMESE:
                    return amenity.name;
                case LanguageUtil.JAPANESE:
                    return amenity.nameJa;
                default:
                    return amenity.nameEn;
            }
        };

        String generalAmenitiesStr = String.format("• %s: %s",
                getResources().getString(R.string.general_amenities),
                generalAmenities.stream().map(mapAmenity).collect(Collectors.joining(", "))
        );
        binding.typeRoomAmenitiesGeneral.setText(generalAmenitiesStr);

        String bathroomAmenitiesStr = String.format("• %s: %s",
                getResources().getString(R.string.bathroom_amenities),
                bathroomAmenities.stream().map(mapAmenity).collect(Collectors.joining(", "))
        );
        binding.typeRoomAmenitiesBathroom.setText(bathroomAmenitiesStr);

        String otherAmenitiesStr = String.format("• %s: %s",
                getResources().getString(R.string.bathroom_amenities),
                otherAmenities.stream().map(mapAmenity).collect(Collectors.joining(", "))
        );
        binding.typeRoomAmenitiesOther.setText(otherAmenitiesStr);


        // Image
        binding.recyclerViewTypeRoomDetailCarousel.setLayoutManager(new CarouselLayoutManager());
        binding.recyclerViewTypeRoomDetailCarousel.setAdapter(new MyTypeRoomCarouselRecyclerViewAdapter(typeRoom.images));
    }
}