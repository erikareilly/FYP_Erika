package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homepage extends AppCompatActivity implements View.OnClickListener {

    CardView card1;
    CardView card2;
    CardView card3;
    CardView card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setSelectedItemId(R.id.person);
        bottomNav.setSelectedItemId(R.id.settings);

        card1 = (CardView) findViewById(R.id.cardView);
        card2 = (CardView) findViewById(R.id.cardView2);
        card3 = (CardView) findViewById(R.id.cardView3);
        card4 = (CardView) findViewById(R.id.cardView4);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));

                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cardView:
                Intent intent = new Intent(Homepage.this, CalendarApp.class);
                startActivity(intent);
                break;

            case R.id.cardView2:
                Intent intent2 = new Intent(Homepage.this, Emergency.class);
                startActivity(intent2);
                break;

            case R.id.cardView3:
                Intent intent3 = new Intent(Homepage.this, LocationPage.class);
                startActivity(intent3);
                break;

            case R.id.cardView4:
                Intent intent4 = new Intent(Homepage.this, LogMedicine.class);
                startActivity(intent4);
                break;

            case R.id.cardView5:
                Intent intent5 = new Intent(Homepage.this, Chat.class);
                startActivity(intent5);
                break;

            case R.id.cardView6:
                Intent intent6 = new Intent(Homepage.this, Notifications.class);
                startActivity(intent6);
                break;

    }
}}