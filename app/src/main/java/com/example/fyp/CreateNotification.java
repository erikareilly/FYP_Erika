package com.example.fyp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalTime;
import java.util.Calendar;

public class CreateNotification extends AppCompatActivity {

    private EditText titleText, descriptionText;
    private TextView datetext, timetext;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    int hour, minute;
    private Button createNotif;
    private LocalTime time;
    private String timeToNotify,date;
    String notifName, notifDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

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

        titleText = (EditText) findViewById(R.id.notifTitle);
        descriptionText = (EditText) findViewById(R.id.notifDescrip);
        datetext = (TextView) findViewById(R.id.date);
        datetext.setText(getTodayDate());
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        timetext = (TextView) findViewById(R.id.time);
        timetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        datePickerUtils();
        createNotif = (Button) findViewById(R.id.create);
        createNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotification();
            }
        });


    }

    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month+1;
        return makeDateString(day,month,year);

    }

    private void datePickerUtils(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                 date = makeDateString(day,month,year);
                datetext.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month)+ " "+ day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month==1){
            return "JAN";
        }
        if(month==2){
            return "FEB";
        }
        if(month==3){
            return "MAR";
        }
        if(month==4){
            return "APR";
        }
        if(month==5){
            return "MAY";
        }
        if(month==6){
            return "JUN";
        }
        if(month==7){
            return "JUL";
        }
        if(month==8){
            return "AUG";
        }
        if(month==9){
            return "SEP";
        }
        if(month==10){
            return "OCT";
        }
        if(month==11){
            return "NOV";
        }
        if(month==12){
            return "DEC";
        }
        return "JAN";
    }

    public void timePicker(){
        System.out.println(" in timePicker method");
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                System.out.println(" in onTimeSet Setting Time " + selectedHour);
                //timeText.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                timetext.setText(hour + ":" + minute);
                time = LocalTime.of(hour, minute);
                timeToNotify = hour + ":" + minute;
            }
        };
        int style= AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener,hour, minute, true);
        timePickerDialog.setTitle("Time");
        timePickerDialog.show();
    }

    public void saveNotification() {
         notifName = titleText.getText().toString().trim();
         notifDesc = descriptionText.getText().toString().trim();

        //validate title data
        if (notifName.isEmpty()) {
            titleText.setError("Title required");
        }
        if (timetext.equals("Time")) {
            timetext.setError("Time for notification required");
        }

        //NotificationClass notificationClass = new NotificationClass(notifName,notifDesc,timeToNotify,date);
       // NotificationClass.notifList.add(notificationClass);
        setNotification();
    }

    public void setNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        //calendar.add(hour,minute);
        Intent intent = new Intent(getApplicationContext(), NotificationBroadcast.class);
        intent.putExtra("title", notifName);
        intent.putExtra("descr", notifDesc);
       // intent.putExtra("time", time);
     //   intent.putExtra("date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        /*if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(CreateNotification.this,"My Notification");
        builder.setContentTitle(notifName);
        builder.setContentText(notifDesc);
        builder.setSmallIcon(R.drawable.ic_baseline_access_alarm_24);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(CreateNotification.this);
        managerCompat.notify(1,builder.build());
        //String dateandtime = date + " " + timeToNotify;
      //  DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
       // try {
            //Date date1 = formatter.parse(dateandtime);

            // } catch (ParseException e) {
            //   e.printStackTrace();
            //}*/

            finish();

    }

    }




