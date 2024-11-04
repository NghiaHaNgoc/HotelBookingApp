package com.hotel.hotel_booking_app.ui.profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hotel.hotel_booking_app.Enum.Gender;
import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentProfileBinding;
import com.hotel.hotel_booking_app.model.ApiResponse;
import com.hotel.hotel_booking_app.model.District;
import com.hotel.hotel_booking_app.model.Province;
import com.hotel.hotel_booking_app.model.User;
import com.hotel.hotel_booking_app.model.Ward;
import com.hotel.hotel_booking_app.service.ApiService;
import com.hotel.hotel_booking_app.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AlertDialog alertDialogLoading;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private SharedPreferences accountSharedPreferences;
    private final List<Province> provincesList = new ArrayList<>();
    private final List<District> districtsList = new ArrayList<>();
    private final List<Ward> wardsList = new ArrayList<>();
    private User user;
    private boolean isFirstLaunch;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstLaunch = true;
        accountSharedPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);

        String jsonStringProvinces = FileUtil.parseJSON(requireContext(), R.raw.provinces);
        JsonArray jsonArrayProvinces = JsonParser.parseString(jsonStringProvinces).getAsJsonArray();

        for (JsonElement element : jsonArrayProvinces) {
            JsonObject provinceObject = element.getAsJsonObject();

            String provinceName = provinceObject.get("province_name").getAsString();
            String provinceId = provinceObject.get("province_id").getAsString();

            provincesList.add(new Province(provinceName, provinceId));
        }

        String jsonStringDistricts = FileUtil.parseJSON(requireContext(), R.raw.districts);
        JsonArray jsonArrayDistricts = JsonParser.parseString(jsonStringDistricts).getAsJsonArray();

        for (JsonElement element : jsonArrayDistricts) {
            JsonObject districtObject = element.getAsJsonObject();

            String districtName = districtObject.get("district_name").getAsString();
            String districtId = districtObject.get("district_id").getAsString();
            String provinceId = districtObject.get("province_id").getAsString();

            districtsList.add(new District(districtName, districtId, provinceId));
        }

        String jsonStringWards = FileUtil.parseJSON(requireContext(), R.raw.wards);
        JsonArray jsonArrayWards = JsonParser.parseString(jsonStringWards).getAsJsonArray();

        for (JsonElement element : jsonArrayWards) {
            JsonObject wardObject = element.getAsJsonObject();

            String wardName = wardObject.get("ward_name").getAsString();
            String wardId = wardObject.get("ward_id").getAsString();
            String districtId = wardObject.get("district_id").getAsString();

            wardsList.add(new Ward(wardName, wardId, districtId));
        }

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        try {
                            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            byte[] imageBytes = outputStream.toByteArray();
                            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                            // Sử dụng base64Image theo nhu cầu của bạn
                            user.setLinkAvatar(base64Image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Glide.with(this)
                                .load(uri)
                                .apply(RequestOptions.circleCropTransform())
                                .into(binding.avatarProfile);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        if (!accountSharedPreferences.getBoolean("isSignedIn", false)) {
            return binding.getRoot();
        }

        alertDialogLoading = new AlertDialog.Builder(
                getActivity(),
                R.style.TransparentDialog
        ).setView(R.layout.loading_progress).setCancelable(false).create();

        alertDialogLoading.show();
        fetchProfileUser();

        setListAutoComplete(binding.provinceProfile, provincesList);

        binding.provinceProfile.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy đối tượng Province từ vị trí đã chọn
            Province selectedProvince = (Province) parent.getItemAtPosition(position);

            // Lấy ID và tên từ đối tượng Province
            String selectedProvinceId = selectedProvince.getProvinceId();
            user.setCity(selectedProvinceId);

            List<District> filteredDistricts = districtsList.stream()
                    .filter(district -> district.getProvinceId().equals(selectedProvinceId)) // điều kiện lọc
                    .collect(Collectors.toList());

            setListAutoComplete(binding.districtProfile, filteredDistricts);
            setListAutoComplete(binding.wardProfile, new ArrayList<>());
            binding.districtProfile.setText(null);
            binding.wardProfile.setText(null);
        });

        binding.districtProfile.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy đối tượng Province từ vị trí đã chọn
            District selectedDistrict = (District) parent.getItemAtPosition(position);

            // Lấy ID và tên từ đối tượng Province
            String selectedDistrictId = selectedDistrict.getDistrictId();
            user.setDistrict(selectedDistrictId);

            List<Ward> filteredWards = wardsList.stream()
                    .filter(ward -> ward.getDistrictId().equals(selectedDistrictId)) // điều kiện lọc
                    .collect(Collectors.toList());

            setListAutoComplete(binding.wardProfile, filteredWards);
            binding.wardProfile.setText(null);
        });

        binding.wardProfile.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy đối tượng Province từ vị trí đã chọn
            Ward selectedWard = (Ward) parent.getItemAtPosition(position);

            // Lấy ID và tên từ đối tượng Province
            String selectedWardId = selectedWard.getWardId();
            user.setWard(selectedWardId);
        });

        binding.btnUpload.setOnClickListener(v -> {
            pickMedia.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        binding.radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (user != null) {
                if (checkedId == R.id.radio_male) {
                    user.setGender(Gender.MALE.getValue());// Male
                } else if (checkedId == R.id.radio_female) {
                    user.setGender(Gender.FEMALE.getValue()); // Female
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        AtomicReference<ZonedDateTime> zonedBirthDate =
                new AtomicReference<>(Instant.EPOCH.atZone(TimeZone.getDefault().toZoneId()));

        DatePickerDialog birthDatePickerDialog = new DatePickerDialog(getContext(), (datePicker, i,
                                                                                  i1, i2) -> {
            zonedBirthDate.set(zonedBirthDate.get().withYear(i).withMonth(i1 + 1).withDayOfMonth(i2));
            String dateFormat = String.format("%02d-%02d-%02d", i2, i1 + 1, i);
            binding.birthdateProfile.setText(dateFormat);
            user.setBirthDay(dateFormat);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        birthDatePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        binding.birthdateProfile.setOnClickListener(view -> {
            birthDatePickerDialog.show();
        });

        binding.btnUpdateProfile.setOnClickListener(v -> {
            alertDialogLoading.show();
//            Instant birthdate = zonedBirthDate.get().toInstant();
//            user.setBirthDay(birthdate.toString());
            user.setAddress(binding.addressProfile.getText().toString());
            handleSubmitFormProfile();
        });
        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_main);
        binding.btnNavigateHomePage.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_profile_to_nav_home);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!accountSharedPreferences.getBoolean("isSignedIn", false)) {
            if (isFirstLaunch) {
                Navigation.findNavController(getView()).navigate(R.id.nav_sign_in);
            } else {
                Navigation.findNavController(getView()).popBackStack(R.id.nav_home, false);
            }
        }
        isFirstLaunch = false;

    }

    private <T> void setListAutoComplete(AutoCompleteTextView autoCompleteTextView, List<T> listOption) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listOption);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void setGenderRadioButton(Gender gender) {
        if (gender != null) {
            if (gender == Gender.MALE) {
                binding.radioGroupGender.check(R.id.radio_male); // Select radio_pirates for Male
            } else if (gender == Gender.FEMALE) {
                binding.radioGroupGender.check(R.id.radio_female); // Select radio_ninjas for Female
            }
        }
    }

    private void setDefaultValueFormProfile() {
        String link_avatar = Objects.requireNonNullElse(
                user.getLinkAvatar(),
                "https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3407.jpg"
        );
        Glide
            .with(binding.avatarProfile)
            .load(link_avatar)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.avatarProfile);

        binding.fullnameProfile.setText(
                String.format(
                        "%s %s",
                        user.firstname,
                        user.surname
                )
        );
        binding.firstnameProfile.setText(user.firstname);
        binding.surnameProfile.setText(user.surname);
        binding.phoneProfile.setText(user.phone);
        binding.emailProfile.setText(user.email);
        binding.birthdateProfile.setText(user.birthDay);
        if (user.getGender() != null) {
            setGenderRadioButton(Gender.fromValue(user.gender));
        }
        binding.addressProfile.setText(user.address);
        if (user.city != null) {
            // Tìm tên tỉnh/thành phố từ provinceList bằng Stream API
            Optional<Province> matchingProvince = provincesList.stream()
                    .filter(province -> province.getProvinceId().equals(user.city))
                    .findFirst();

            // Nếu tìm thấy tỉnh/thành phố, đặt tên vào AutoCompleteTextView
            matchingProvince.ifPresent(province ->
                    binding.provinceProfile.setText(province.getProvinceName(), false)
            );
        }

        if (user.district != null) {
            // Tìm tên tỉnh/thành phố từ provinceList bằng Stream API
            Optional<District> matchingDistrict = districtsList.stream()
                    .filter(district -> district.getProvinceId().equals(user.district))
                    .findFirst();

            // Nếu tìm thấy tỉnh/thành phố, đặt tên vào AutoCompleteTextView
            matchingDistrict.ifPresent(district ->
                    binding.districtProfile.setText(district.getDistrictName(), false)
            );
        }

        if (user.ward != null) {
            // Tìm tên tỉnh/thành phố từ provinceList bằng Stream API
            Optional<Ward> matchingWard = wardsList.stream()
                    .filter(ward -> ward.getWardId().equals(user.ward))
                    .findFirst();

            // Nếu tìm thấy tỉnh/thành phố, đặt tên vào AutoCompleteTextView
            matchingWard.ifPresent(ward ->
                    binding.wardProfile.setText(ward.getWardName(), false)
            );
        }
    }

    private void fetchProfileUser() {
        // chạy xong onCreateView mới call api nên đặt chỗ nào cx đc
        new ApiService(getContext()).getProfile().enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call,
                                   Response<ApiResponse<User>> response) {
                ApiResponse<User> res = response.body();

                if (res != null && res.status == 200) {
                    user = new User(res.result);
                    // setDefaultValue to form profile
                    setDefaultValueFormProfile();
                } else {
                    assert res != null;
                    Log.e("error 400", res.message);
                }
                alertDialogLoading.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call,
                                  Throwable throwable) {
                Log.e("error 500", throwable.toString());
                alertDialogLoading.dismiss();
            }
        });
    }

    private void handleSubmitFormProfile() {
        user.setFirstname(Objects.requireNonNull(binding.firstnameProfile.getText()).toString());
        user.setSurname(Objects.requireNonNull(binding.surnameProfile.getText()).toString());
        user.setPhone(Objects.requireNonNull(binding.phoneProfile.getText()).toString());
        new ApiService(getContext()).updateProfile(user).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call,
                                   Response<ApiResponse<User>> response) {
                ApiResponse<User> res = response.body();

                if (res != null && res.status == 200) {
                    SharedPreferences.Editor editor =  accountSharedPreferences.edit();
                    editor.putString("email", user.email);
                    editor.putString("firstname", user.firstname);
                    editor.putString("surname", user.surname);
                    editor.putString("linkAvatar", res.result.linkAvatar);
                    editor.apply();
                    binding.fullnameProfile.setText(
                            String.format(
                                    "%s %s",
                                    user.firstname,
                                    user.surname
                            )
                    );
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.update_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    assert res != null;
                    Log.e("error 400", res.message);
                }
                alertDialogLoading.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call,
                                  Throwable throwable) {
                Log.e("error 500", throwable.toString());
                alertDialogLoading.dismiss();
            }
        });
    }
}