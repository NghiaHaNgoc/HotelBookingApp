package com.hotel.hotel_booking_app.ui.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.hotel.hotel_booking_app.R;
import com.hotel.hotel_booking_app.databinding.FragmentSettingBinding;
import com.hotel.hotel_booking_app.util.LanguageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    String[] languageList = {
            "Tiếng Việt",
            "English",
            "日本語"
    };

    public SettingFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);


        binding.spinnerSettingLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LocaleListCompat locales;
                switch (i) {
                    case 1:
                        locales = LocaleListCompat.create(Locale.forLanguageTag(LanguageUtil.VIETNAMESE));
                        break;
                    case 2:
                        locales = LocaleListCompat.create(Locale.forLanguageTag(LanguageUtil.ENGLISH));
                        break;
                    case 3:
                        locales = LocaleListCompat.create(Locale.forLanguageTag(LanguageUtil.JAPANESE));
                        break;
                    default:
                        locales = LocaleListCompat.getEmptyLocaleList();
                }
                AppCompatDelegate.setApplicationLocales(locales);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onResume() {
        super.onResume();
        setupFragment();
    }

    private void setupFragment() {
        ArrayList<String> languages = new ArrayList<>();
        languages.add(getResources().getString(R.string.system_language));
        languages.addAll(Arrays.asList(languageList));

        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                languages
        );


        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerSettingLanguage.setAdapter(languageAdapter);

        if (AppCompatDelegate.getApplicationLocales().isEmpty()) {
            binding.spinnerSettingLanguage.setSelection(0);
        } else {
            switch (LanguageUtil.getLanguage()) {
                case LanguageUtil.VIETNAMESE:
                    binding.spinnerSettingLanguage.setSelection(1);
                    break;
                case LanguageUtil.ENGLISH:
                    binding.spinnerSettingLanguage.setSelection(2);
                    break;
                case LanguageUtil.JAPANESE:
                    binding.spinnerSettingLanguage.setSelection(3);
                    break;
                default:
                    binding.spinnerSettingLanguage.setSelection(0);
            }
        }
    }
}