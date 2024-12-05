package com.axel.astropic.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.axel.astropic.ui.MainActivity;
import com.axel.astropic.R;
import com.axel.astropic.lenguage.LocaleHelper;
public class SettingsFragment extends Fragment {

    private RadioGroup radioGroup;
    private Button saveButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize views from the inflated layout
        radioGroup = view.findViewById(R.id.radio_group);
        saveButton = view.findViewById(R.id.save_button);

        // Get the saved language from SharedPreferences
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String savedLanguage = preferences.getString("language", "en");  // Default to English

        // Set the selected radio button based on the saved language
        if ("es".equals(savedLanguage)) {
            radioGroup.check(R.id.radio_spanish);
        } else if ("en".equals(savedLanguage)) {
            radioGroup.check(R.id.radio_english);
        } else if ("hi".equals(savedLanguage)) {  // Hindi
            radioGroup.check(R.id.radio_hindi);
        } else {
            radioGroup.check(R.id.radio_english);  // Default to English
        }

        // Handle Save button click
        saveButton.setOnClickListener(v -> {
            int selectedRadioId = radioGroup.getCheckedRadioButtonId();
            String selectedLanguage = "en"; // Default to English

            if (selectedRadioId == R.id.radio_english) {
                selectedLanguage = "en";
            } else if (selectedRadioId == R.id.radio_spanish) {
                selectedLanguage = "es";
            } else if (selectedRadioId == R.id.radio_hindi) {  // Hindi selection
                selectedLanguage = "hi";
            }

            // Save the selected language using SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", selectedLanguage);
            editor.apply();

            // Change the locale to the selected language
            LocaleHelper.setLocale(getContext(), selectedLanguage);

            // Optionally, restart the activity to apply the new language
            Toast.makeText(getContext(), "Language changed to: " + selectedLanguage, Toast.LENGTH_SHORT).show();

            // Restart the activity to apply the new language
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view; // Return the inflated view
    }

}
