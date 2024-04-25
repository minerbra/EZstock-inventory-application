package com.example.androidapplicationbradyminer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.*;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper applicationDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        applicationDatabase = DatabaseHelper.getInstance(getApplicationContext());



    }

    public void logout(View view) {
        // Create AlertDialog to confirm logout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Set positive button and its click listener
        builder.setPositiveButton("Yes", (dialog, which) -> {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            // Finish the current activity to prevent the user from returning
            finish();
        });

        // Set negative button and its click listener
        builder.setNegativeButton("No", (dialog, which) -> {
            // Dismiss the dialog if the user clicks "No"
            dialog.dismiss();
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}