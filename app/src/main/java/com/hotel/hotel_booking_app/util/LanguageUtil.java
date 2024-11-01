package com.hotel.hotel_booking_app.util;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.Arrays;
import java.util.Locale;

public class LanguageUtil {
    public static final String ENGLISH = "en";
    public static final String VIETNAMESE = "vi";
    public static final String JAPANESE = "ja";
    public static String getLanguage() {
        Locale appLocale = AppCompatDelegate.getApplicationLocales().get(0);
        if (appLocale == null) {
            Locale sysLocale = Locale.getDefault();
            return sysLocale.toString().split("_")[0];
        } else {
            return appLocale.toString();
        }
    }
}
