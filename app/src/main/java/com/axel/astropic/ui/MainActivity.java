package com.axel.astropic.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import com.axel.astropic.Fragments.MainFragment;
import com.axel.astropic.Fragments.SaveImageFragment;
import com.axel.astropic.Fragments.SettingsFragment;
import com.axel.astropic.R;
import com.axel.astropic.lenguage.LocaleHelper;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isFromFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the language based on the saved preference
        String language = LocaleHelper.getLanguage(this);
        LocaleHelper.setLocale(this, language);

        setContentView(R.layout.activity_main);


        // Initialize DrawerLayout and Toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar);

        // Initialize the NavigationView
        navigationView = findViewById(R.id.nav_view);

        // Set the Home item as checked by default
        navigationView.setCheckedItem(R.id.nav_home);

        // Setup ActionBarDrawerToggle to show/hide drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showMainFragment();
            } else if (id == R.id.nav_settings) {
                showSettingsFragment();
            } else if (id == R.id.nav_saved_images) {
                showShavedImageragment();
            }else if (id == R.id.nav_about) {
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                finish();
            }

            // Close the drawer after selection
            drawerLayout.closeDrawers();
            return true;
        });

        // Show the MainFragment when the activity is created
        if (savedInstanceState == null) {
            showMainFragment();
        }

        // Clear the checked state of the menu item (optional)
        navigationView.setCheckedItem(-1); // This will uncheck any item that is checked
    }



    private void showMainFragment() {
        // Create a fragment transaction to replace the fragment container with MainFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MainFragment());
        transaction.commit();
        isFromFragment = false;  // Track if user is in a fragment
    }
  private void showShavedImageragment() {
        // Create a fragment transaction to replace the fragment container with MainFragment
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.fragment_container, new SaveImageFragment());
      transaction.addToBackStack(null);  // Optional: add fragment to the back stack for navigation
      transaction.commit();
        isFromFragment = false;  // Track if user is in a fragment
    }

    private void showSettingsFragment() {
        // Replace with the settings fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SettingsFragment());
        transaction.commit();
        isFromFragment = true;  // Track if user is in Settings fragment
    }

    @Override
    public void onBackPressed() {
        // If the drawer is open, close it; otherwise, show exit dialog if coming from MainFragment
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (isFromFragment || getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof MainFragment) {
            // Check if the user is on the MainFragment
            showExitDialog();  // Show exit dialog if user is on MainFragment
        } else {
            super.onBackPressed();
        }
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // Close the app
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help")
                .setMessage("Here are the instructions for using this interface:\n\n" +
                        "1. Select a date using the 'Select Date' button.\n" +
                        "2. Fetch an image by clicking the 'Fetch Image' button.\n" +
                        "3. Save the image using the 'Save Image' button once fetched.\n" +
                        "4. Use the list to view or manage images.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }




}
