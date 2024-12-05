package com.axel.astropic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.axel.astropic.Fragments.SaveImageFragment;
import com.axel.astropic.R;
import com.axel.astropic.ui.ImageDetailActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class SavedImagesAdapter extends BaseAdapter {

    private SaveImageFragment fragment;  // SaveImageFragment reference instead of generic Context
    private ArrayList<JSONObject> savedImages;  // List of JSON objects

    // Constructor now takes SaveImageFragment instead of Context
    public SavedImagesAdapter(SaveImageFragment fragment, ArrayList<JSONObject> savedImages) {
        this.fragment = fragment;
        this.savedImages = savedImages;
    }

    @Override
    public int getCount() {
        return savedImages.size();
    }

    @Override
    public Object getItem(int position) {
        return savedImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.saved_image_item, parent, false);
        }

        try {
            // Get the saved image data at this position
            JSONObject savedImage = savedImages.get(position);
            String imageUrl = savedImage.getString("url");
            String timestamp = savedImage.getString("timestamp");
            String title = savedImage.getString("title");
            String explanation = savedImage.getString("explanation");
            String hdUrl = savedImage.getString("hdurl");

            // Set the image
            ImageView imageView = convertView.findViewById(R.id.savedImageView);
            Picasso.get().load(imageUrl).into(imageView);

            // Set the timestamp
            TextView timestampTextView = convertView.findViewById(R.id.timestampTextView);
            timestampTextView.setText(timestamp);

            // Set the delete button action
            View deleteButton = convertView.findViewById(R.id.dlt_btn);
            deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(fragment.getActivity())
                        .setTitle("Delete Image")
                        .setMessage("Are you sure you want to delete this image?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Call the fragment's deleteImage method to handle deletion
                                fragment.deleteImage(savedImage);

                                // Remove image from the adapter's list
                                savedImages.remove(position);
                                notifyDataSetChanged();  // Refresh the list view

                                // Show confirmation toast
//                                Toast.makeText(fragment.getActivity(), "Image deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            // Set the "View" button action to open ImageDetailActivity
            View viewButton = convertView.findViewById(R.id.view_btn);
            viewButton.setOnClickListener(v -> {
                // Start ImageDetailActivity and pass the relevant data
                Intent intent = new Intent(fragment.getActivity(), ImageDetailActivity.class);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("title", title);
                intent.putExtra("explanation", explanation);
                intent.putExtra("hdUrl", hdUrl);
                fragment.getActivity().startActivity(intent);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
