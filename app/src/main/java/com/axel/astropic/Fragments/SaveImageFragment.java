package com.axel.astropic.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.axel.astropic.Adapter.SavedImagesAdapter;
import com.axel.astropic.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class SaveImageFragment extends Fragment {

    private ListView listViewSavedImages; // ListView to display saved images
    private SavedImagesAdapter adapter;  // Adapter to bind the images to the ListView
    private ArrayList<JSONObject> savedImages;  // List to store the saved images as JSON objects


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_image, container, false);

        listViewSavedImages = view.findViewById(R.id.listViewSavedImages); // ListView for displaying saved images


        // Initialize the list
        savedImages = new ArrayList<>();




        // Retrieve saved images from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SavedImages", getActivity().MODE_PRIVATE);
        String savedImagesJson = sharedPreferences.getString("saved_images", "[]");  // Default to empty array if no saved images

        // Parse the JSON string into a list of JSONObjects (each representing an image)
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<JSONObject>>() {}.getType();
        savedImages = gson.fromJson(savedImagesJson, type);

        // Reverse the list so that the new images appear at the top
        Collections.reverse(savedImages);

        // Set up the adapter with the image URLs
        if (savedImages != null && savedImages.size() > 0) {
            adapter = new SavedImagesAdapter(this, savedImages);

            listViewSavedImages.setAdapter(adapter);
        } else {
            // No images saved, show a toast
            Toast.makeText(getActivity(), "No saved images", Toast.LENGTH_SHORT).show();
        }

        return view;  // Return the fragment view
    }

    // Method to delete the image from SharedPreferences
    public void deleteImage(JSONObject imageToDelete) {
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

        // Iterate over saved images and remove the one that matches the image to delete
        boolean imageFound = false;
        for (int i = 0; i < savedImages.size(); i++) {
            try {
                String savedImageUrl = savedImages.get(i).getString("url");
                String imageToDeleteUrl = imageToDelete.getString("url");

                // Check if the URLs match
                if (savedImageUrl.equals(imageToDeleteUrl)) {
                    savedImages.remove(i);  // Remove the image from the list
                    imageFound = true;
                    break;  // Stop once the image is found and removed
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imageFound) {
            // Save the updated list back to SharedPreferences
            String updatedSavedImagesJson = gson.toJson(savedImages);
            editor.putString("saved_images", updatedSavedImagesJson);
            editor.apply();  // Commit changes

            // Update the savedImages list in the Fragment to reflect changes
            this.savedImages = savedImages; // Update the fragment's list

            // Notify the adapter that the data has changed
            if (adapter != null) {
                adapter.notifyDataSetChanged();  // Refresh the ListView
            }

            // Show a toast confirming the image was deleted
            Toast.makeText(getActivity(), "Image deleted", Toast.LENGTH_SHORT).show();
        } else {
            // If the image was not found, show a message
            Toast.makeText(getActivity(), "Image not found", Toast.LENGTH_SHORT).show();
        }
    }



}
