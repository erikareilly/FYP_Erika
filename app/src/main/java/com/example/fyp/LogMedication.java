package com.example.fyp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;

public class LogMedication extends AppCompatActivity implements View.OnClickListener {

    private EditText medicationText;
    private TextView timeText;
    int hour, minute;
    LocalTime localTime;
    private Button logButton;
    AlertDialog alertDialog;
    String medicationName;
    String timeString, dateString;
    private String apiEndpoint =  "https://api.fda.gov/drug/label.json?search=openfda.generic_name:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_medication);

        medicationText = (EditText) findViewById(R.id.medicationText);
        timeText = (TextView) findViewById(R.id.timeText);
        //ImageView time = (ImageView) findViewById(R.id.timeClick);
        logButton = (Button) findViewById(R.id.saveButton);
        logButton.setOnClickListener(this);
        /*logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedication(v);
            }
        });*/
        localTime = LocalTime.of(hour,minute);

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


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.saveButton:
                saveMedication();

        }

    }

    public void timePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute=selectedMinute;
                System.out.println(" in onTimeSet Setting Time "+ selectedHour);
                timeText.setText(hour + ":" + minute);
                localTime = LocalTime.of(hour,minute);
                timeString = timeText.getText().toString();
            }
        };
        int style= android.app.AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener,hour, minute, true);
        timePickerDialog.setTitle("Time");
        timePickerDialog.show();
    }


    public void saveMedication() {
        medicationName = medicationText.getText().toString().trim();

        String urlSearch = apiEndpoint + medicationName;
        try {
            String encodedGenericName = URLEncoder.encode(medicationName, "UTF-8");
            String queryUrl = apiEndpoint + encodedGenericName;
            new openFDAEndpoint().execute(queryUrl);
            //Intent intent = new Intent(LogMedication.this, LogMedicine.class);
           // startActivity(intent);
        }catch (Exception e) {
            Log.e(TAG, "Error encoding query parameter", e);
        }


}

private class openFDAEndpoint extends AsyncTask<String,Void,String>{


    @Override
    protected String doInBackground(String... params) {

        String queryUrl = params[0];
        try {
            URL url = new URL(queryUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {

        LocalDate date = LocalDate.now();
        dateString = String.valueOf(date);


        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("results");
            for(int i =0; i<result.length();i++){
                JSONObject resultObject = result.getJSONObject(i);
                JSONObject openfda = resultObject.getJSONObject("openfda");
                JSONArray warnings = resultObject.getJSONArray("do_not_use");
                String donot = warnings.getString(0);
                if(openfda.has("generic_name")){
                    String genericName = openfda.getJSONArray("generic_name").getString(0);
                    Log.d(TAG, "Generic Name" + genericName);
                    if(warnings.length()>0) {
                        //removed toast message to display dialog instead
                        // Toast.makeText(LogMedication.this,donot,Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LogMedication.this);
                        builder.setTitle("Warning");
                        builder.setMessage(donot);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        // Assign the dialog to the alertDialog variable
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                    Medication newLog = new Medication(medicationName,timeString,dateString);
                    Medication.medsList.add(newLog);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Medication").push().setValue(newLog).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LogMedication.this, "Medication has been logged", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(LogMedication.this, "Failed to log medication", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                    //end activity
                    //  finish();


                }else{
                    Log.d(TAG, "No name found");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }

}

    }



