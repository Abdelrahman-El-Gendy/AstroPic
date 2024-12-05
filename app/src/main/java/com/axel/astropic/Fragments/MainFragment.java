package com.axel.astropic.Fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.axel.astropic.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainFragment extends Fragment {

    private Button dateButton, fetchButton, saveButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private String imageUrl, title, explanation, hdUrl;
    private static final String NASA_API_KEY = "DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";

    private int selectedYear, selectedMonth, selectedDay;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        // Initialize views
        dateButton = view.findViewById(R.id.dateButton);
        fetchButton = view.findViewById(R.id.fetchButton);
        saveButton = view.findViewById(R.id.saveButton);
        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);

        // Set up the date picker dialog when the date button is clicked
        dateButton.setOnClickListener(v -> showDatePickerDialog());

        // Fetch image button
        fetchButton.setOnClickListener(v -> fetchNasaImage());

        // Save image button (implement your database logic here)
        saveButton.setOnClickListener(v -> {
            if (imageUrl != null && title != null && explanation != null && hdUrl != null) {
                saveImage(imageUrl, title, explanation, hdUrl);
            } else {
                Toast.makeText(getActivity(), "No image to save", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Show date picker dialog when the button is clicked
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Update the selected date
                    selectedYear = year;
                    selectedMonth = monthOfYear;
                    selectedDay = dayOfMonth;

                    // Update the button text to show the selected date
                    String date = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear);
                    dateButton.setText("Selected Date: " + date);
                },
                selectedYear, selectedMonth, selectedDay);

        datePickerDialog.show();
    }

    // Fetch NASA image based on the selected date
    private void fetchNasaImage() {
        String date = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);

        String url = "https://api.nasa.gov/planetary/apod?api_key=" + NASA_API_KEY + "&date=" + date;

        progressBar.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.INVISIBLE); // Make the save button invisible initially

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        imageUrl = response.getString("url");
                        title = response.getString("title");
                        explanation = response.getString("explanation");
                        hdUrl = response.getString("hdurl");

                        Picasso.get().load(imageUrl).into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                // Image loaded successfully, show the save button
                                saveButton.setVisibility(View.VISIBLE);

                                // Save the image and additional data
                                imageView.setTag(imageUrl);  // Store the image URL in the tag for later use

                                // Hide the progress bar after image is loaded
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                // Handle error if Picasso fails to load image
                                Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();

                                // Hide the progress bar on error
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // Hide progress bar in case of an error
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        Toast.makeText(getActivity(), "No image available for the selected date.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }

                    // Hide the progress bar if an error occurs
                    progressBar.setVisibility(View.GONE);
                });

        queue.add(jsonObjectRequest);
    }

    // Placeholder for saving the image (you should implement database or SharedPreferences logic)
    private void saveImage(String imageUrl, String title, String explanation, String hdUrl) {
        // Get the current timestamp
        long timestamp = System.currentTimeMillis();
        String dateTime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date(timestamp));

        // Create a new entry with the full image data
        JSONObject newEntry = new JSONObject();
        try {
            newEntry.put("url", imageUrl);
            newEntry.put("title", title);
            newEntry.put("explanation", explanation);
            newEntry.put("hdurl", hdUrl);
            newEntry.put("timestamp", dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Save the image data in SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SavedImages", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve the existing saved images list
        String savedImagesJson = sharedPreferences.getString("saved_images", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<JSONObject>>() {}.getType();
        ArrayList<JSONObject> savedImages = gson.fromJson(savedImagesJson, type);

        if (savedImages == null) {
            savedImages = new ArrayList<>();
        }

        // Check if the image URL is already saved
        for (JSONObject savedImage : savedImages) {
            try {
                if (savedImage.getString("url").equals(imageUrl)) {
                    // If the image is already saved, show a message and exit the method
                    Toast.makeText(getActivity(), "Image already saved!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Add the new entry to the list
        savedImages.add(newEntry);

        // Save the updated list back to SharedPreferences
        String updatedSavedImagesJson = gson.toJson(savedImages);
        editor.putString("saved_images", updatedSavedImagesJson);
        editor.apply();

        // Show a message that the image has been saved
        Toast.makeText(getActivity(), "Image saved!", Toast.LENGTH_SHORT).show();
    }
}
