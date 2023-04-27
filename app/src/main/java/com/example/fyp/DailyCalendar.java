package com.example.fyp;

import static com.example.fyp.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DailyCalendar extends AppCompatActivity {

    private TextView monthDayText;
    private TextView dayOfWeekText;
    private ListView hourListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar);

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
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        return true;
                }
                return false;
            }
        });

        //initialise variables
        monthDayText = (TextView) findViewById(R.id.monthDay);
        dayOfWeekText = (TextView) findViewById(R.id.dayOfWeek);
        hourListView = (ListView) findViewById(R.id.hourListView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setDayView();
    }

    private void setDayView() {
        //set text to day format from calendar utils
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekText.setText(dayOfWeek);
        //method to call functions in hour adapter
        setHourAdapter();
    }

    private void setHourAdapter() {
        //create hour adapter object
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourList());
        hourListView.setAdapter(hourAdapter);
        //setting item listener to bring user to edit event page when time is picked
        hourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), EditEvent.class));
            }
        });

    }
//adding to set time
    private ArrayList<HourEvent> hourList() {
        ArrayList<HourEvent> list=new ArrayList<>();
        for(int hour = 0; hour<24;hour++){
            LocalTime time = LocalTime.of(hour, 0 );
            ArrayList<Event>events = Event.eventsForHour(selectedDate,time);
            HourEvent hourEvent = new HourEvent(time,events);
            list.add(hourEvent);
       }
        return list;
    }

    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EditEvent.class));

    }

}