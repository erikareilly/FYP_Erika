package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    private Button info,notifications,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setSelectedItemId(R.id.person);
        bottomNav.setSelectedItemId(R.id.settings);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        return true;
                    case R.id.settings:
                        return true;
                }
                return false;
            }
        });

        info = (Button) findViewById(R.id.myInfo);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MyInformation.class);
                startActivity(intent);
            }
        });
        notifications = (Button) findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Settings.this, Notifications.class);
                startActivity(intent1);
            }
        });
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.person:
                startActivity(new Intent(this, UserProfile.class));
                overridePendingTransition(0,0);
                break;

            case R.id.home:
                startActivity(new Intent(this, Homepage.class));
                overridePendingTransition(0,0);
                break;*/
        }
    }
}