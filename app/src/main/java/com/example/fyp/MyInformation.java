package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyInformation extends AppCompatActivity {

    Button saveButton, editButton;
    EditText weight,age,allergies,medNotes;
    RadioButton male,female;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;
    boolean gender;
    String weightOfUser;
    String ageOfUser;
    String allergiesOfUser;
    String medNotesOfUser;
    boolean genderOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

       // user = FirebaseAuth.getInstance().getCurrentUser();
        //reference = FirebaseDatabase.getInstance().getReference("Users");
       // userID = user.getUid();

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

        saveButton = (Button)findViewById(R.id.saveButton);
        editButton = (Button) findViewById(R.id.edit);
        weight=(EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
        allergies = (EditText) findViewById(R.id.allergies);
        medNotes = (EditText) findViewById(R.id.medNotes);
        male = (RadioButton) findViewById(R.id.maleButton);
        female = (RadioButton) findViewById(R.id.femaleButton);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(male.isChecked()){
                    gender = false;
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(female.isChecked()){
                    gender= true;
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update();
            }
        });
    }

    public void saveInformation() {
        weightOfUser = weight.getText().toString().trim();
        ageOfUser = age.getText().toString().trim();
        allergiesOfUser = allergies.getText().toString().trim();
        medNotesOfUser = medNotes.getText().toString().trim();
        genderOfUser = gender;


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Your code that uses `user.getUid()` goes here
            PatientInfo patientInfo = new PatientInfo(weightOfUser, ageOfUser, allergiesOfUser, medNotesOfUser, genderOfUser);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("AdditionalInformation").push().setValue(patientInfo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MyInformation.this, "Information has been saved", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MyInformation.this, "Failed to save information", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            //end activity
             finish();

        } else {
            // Handle the case where the user is null (e.g. redirect to login screen)
        }
        //save information to database
    }



  /*  public void update() {
        if(isWeightChanged() || isAgeChanged() || isAllergiesChanged() || isNotesChanged() ||isGenderChanged()){
            Toast.makeText(this,"User Data Updated", Toast.LENGTH_LONG ).show();
        }else{
            Toast.makeText(this,"User Data did not change", Toast.LENGTH_LONG ).show();

        }
    }

    private boolean isWeightChanged() {
        if(!weightOfUser.equals(weight.getText().toString())){
            reference.child(userID).child("weight").setValue(weight.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean isAgeChanged() {
        if(!ageOfUser.equals(age.getText().toString())){
            reference.child(userID).child("age").setValue(age.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean isAllergiesChanged() {
        if(!allergiesOfUser.equals(allergies.getText().toString())){
            reference.child(userID).child("allergies").setValue(allergies.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean isNotesChanged() {
        if(!medNotesOfUser.equals(medNotes.getText().toString())){
            reference.child(userID).child("medicalNote").setValue(medNotes.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean isGenderChanged(){
        if(!genderOfUser==(gender)){
            reference.child(userID).child("gender").setValue(gender);
            return true;
        }else{
            return false;
        }
    }*/

}