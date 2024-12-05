package com.axel.astropic.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.axel.astropic.R;
import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView imageView, backBtnImageView;
    private TextView imageName, imageInfo;
    private Button hdImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail); // Your layout file for image details

        // Initialize UI elements
        imageView = findViewById(R.id.imageView3);
        backBtnImageView = findViewById(R.id.backBtnImageView);
        imageName = findViewById(R.id.imageName);
        imageInfo = findViewById(R.id.imageInfo);
        hdImageButton = findViewById(R.id.hdImageButton);

        // Get data from Intent
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String explanation = intent.getStringExtra("explanation");
        String hdUrl = intent.getStringExtra("hdUrl");

        // Set data to UI elements
        imageName.setText(title);
        imageInfo.setText(explanation);
        Picasso.get().load(imageUrl).into(imageView);

        // Set action for the HD Image button
        hdImageButton.setOnClickListener(v -> {
            // Open HD image in browser or another activity (optional)
            if (hdUrl != null && !hdUrl.isEmpty()) {
                Intent hdIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hdUrl));
                startActivity(hdIntent);
            } else {
                Toast.makeText(ImageDetailActivity.this, "HD Image not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Set action for back button
        backBtnImageView.setOnClickListener(v -> finish());  // Close the activity when back button is pressed
    }
}
