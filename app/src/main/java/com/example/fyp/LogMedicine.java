package com.example.fyp;

import static com.example.fyp.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogMedicine extends AppCompatActivity implements View.OnClickListener {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private ListView logListView;
    private Button nextDay, previousDay, logButton;
    private  TextView monthDayText, dayOfWeekText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_medicine);


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

        logListView = (ListView) findViewById(R.id.logListView);
       logButton = (Button) findViewById(R.id.logButton);
        logButton.setOnClickListener(this);
        nextDay = (Button) findViewById(R.id.rightB);
        previousDay = (Button) findViewById(R.id.leftB);
        monthDayText = (TextView) findViewById(R.id.monthDay);
        dayOfWeekText = (TextView) findViewById(R.id.dayOfWeek);
        //arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medsList);
       // logListView.setAdapter(arrayAdapter);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setDay();
    }


    private void setDay(){
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d");
        date = dateFormat.format(calendar.getTime());
        monthDayText.setText(date);
        LocalDate da = LocalDate.now();
        DayOfWeek dow = da.getDayOfWeek();
        String dayOfWeek = String.valueOf(dow);
        dayOfWeekText.setText(dayOfWeek);
        setMedicationAdapter();

    }

    private void setMedicationAdapter(){
        MedicationHourAdapter medicationAdapter = new MedicationHourAdapter(getApplicationContext(), medicationHour());
        logListView.setAdapter(medicationAdapter);

    }

    private List<MedicationHour> medicationHour() {
        ArrayList<MedicationHour> list = new ArrayList<>();
        for(int hour = 0; hour<24;hour++){
            LocalTime time = LocalTime.of(hour, 0 );
            ArrayList<Medication>meds = Medication.medsForHour(selectedDate,time);
            MedicationHour medicationHour = new MedicationHour(time,meds);
            list.add(medicationHour);
        }
        return list;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.logButton:
                Intent intent = new Intent(LogMedicine.this, LogMedication.class);
                startActivity(intent);

        }

    }

    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDay();
    }

    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDay();
    }
}