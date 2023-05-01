package com.example.fyp;

import static com.example.fyp.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogMedicine extends AppCompatActivity implements ItemClickListener {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button nextDay, previousDay, logButton;
    private  TextView monthDayText, dayOfWeekText;
    ArrayAdapter<String> arrayAdapter;
    DatabaseReference mReference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> medsList = new ArrayList<>();
    ArrayList<LocalTime> timeList = new ArrayList<>();
    private RecyclerView recyclerView;
    ListView medListView;
    MedicationAdapter medicationAdapter;
    String medications, timeOf;
    ArrayList<Medication>list;
    LocalTime localTime;


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


        logButton = (Button) findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogMedicine.this, LogMedication.class);
                startActivity(intent);
            }
        });
        nextDay = (Button) findViewById(R.id.rightB);
        previousDay = (Button) findViewById(R.id.leftB);
        monthDayText = (TextView) findViewById(R.id.monthDay);
        dayOfWeekText = (TextView) findViewById(R.id.dayOfWeek);
        medListView = findViewById(R.id.logListView);
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Medication");
        //  recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        medListView.setAdapter(arrayAdapter);

        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // String value = snapshot.getValue(Medication.class).toString();
                medications = snapshot.getValue(Medication.class).getMedicationName()+"      " +snapshot.getValue(Medication.class).getTime();
                timeOf = snapshot.getValue(Medication.class).getTime();

                arrayList.add(medications);
                //timeList.add(localTime);
                medsList.add(medications);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // medicationAdapter = new MedicationAdapter(this,list,this);
        // recyclerView.setAdapter(medicationAdapter);

      /*  mReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                  Medication medication = dataSnapshot.getValue(Medication.class);
                   list.add(medication);
               }
               medicationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


      //  arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
       // medListView.setAdapter(arrayAdapter);
       /* mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(Medication.class).toString();
                medications = snapshot.getValue(Medication.class).getMedicationName();
                arrayList.add(value);
                medsList.add(medications);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        // setMedicationAdapter();

    }

   /* private void setMedicationAdapter(){
        MedicationHourAdapter medicationAdapter = new MedicationHourAdapter(getApplicationContext(), medicationHour());
        logListView.setAdapter(medicationAdapter);

    }*/

   /* private ArrayList<MedicationHour> medicationHour() {
        ArrayList<MedicationHour> list = new ArrayList<>();
        for(int hour = 0; hour<24;hour++){
            LocalTime time = LocalTime.of(hour, 0 );
            ArrayList<Medication>meds = Medication.medsForHour(selectedDate,time);
            MedicationHour medicationHour = new MedicationHour(time,meds);
            list.add(medicationHour);
        }
        return list;

    }*/

    public void previousDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDay();
    }

    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDay();
    }

    @Override
    public void onClick(int position) {

    }
}