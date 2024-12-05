package com.axel.astropic.lenguage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.Locale;

public class LocaleHelper {

    // Method to change language
    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        // Save the selected language to SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    // Method to load language from preferences
    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.getString("language", "en"); // default to English if not set
    }
}
