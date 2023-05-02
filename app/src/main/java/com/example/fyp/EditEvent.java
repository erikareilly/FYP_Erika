package com.example.fyp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class EditEvent extends AppCompatActivity {

    private EditText nameEdit, descriptionEdit;
    private TextView timeText,dateText;
    int hour, minute;
    private LocalTime time;
    //  private LocalDate dates;
    private LocalDateTime date1;
    String time1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

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
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        descriptionEdit=(EditText) findViewById(R.id.descriptionEdit);
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);

        time = LocalTime.of(hour,minute);
        dateText.setText(/*"Date: " +*/ CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        // timeText.setText("Time: " + CalendarUtils.formattedTime(time));
        // timeText.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));

    }

    public void timePicker(View view) {
        System.out.println(" in timePicker method");
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute=selectedMinute;
                System.out.println(" in onTimeSet Setting Time "+ selectedHour);
                //timeText.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                timeText.setText(hour + ":" + minute);
                time = LocalTime.of(hour,minute);
                time1 = String.valueOf(time);
            }
        };
        int style= AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener,hour, minute, true);
        timePickerDialog.setTitle("Time");
        timePickerDialog.show();
    }

    //create medication class to allow medication to be saved
    public void saveEventAction(View view) {
        String eventName = nameEdit.getText().toString().trim();
        String eventDescription = descriptionEdit.getText().toString().trim();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MMM yyyy").toFormatter(Locale.ENGLISH);
        date1 = LocalDateTime.from(LocalDate.parse(CalendarUtils.formattedDate(CalendarUtils.selectedDate),formatter).atStartOfDay());
        String date = String.valueOf(date1);
        //create new event
        Event newEvent = new Event(eventName, eventDescription, date, time1);
        //add event to list
        Event.eventsList.add(newEvent);
        //add event to firebase
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Event").push().setValue(newEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditEvent.this, "Event has been created", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(EditEvent.this, "Failed to create event", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        //end activity
        finish();
    }

}
