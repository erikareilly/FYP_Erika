package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    String firstname;
    String surname;
    String phone;
    String email;

     EditText displayTextName, displayTextSurname, displayTextNumber, displayTextEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

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
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        return true;
                }
                return false;
            }
        });


        displayTextName = (EditText) findViewById(R.id.name);
        displayTextSurname = (EditText) findViewById(R.id.surname);
        displayTextNumber = (EditText) findViewById(R.id.number);
        displayTextEmail = (EditText) findViewById(R.id.emailText);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    firstname = user.na;
                    surname = user.su;
                     phone = user.ph;
                     email = user.em;

                    displayTextName.setText(firstname);
                    displayTextSurname.setText(surname);
                    displayTextNumber.setText(phone);
                    displayTextEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.edit:
                startActivity(new Intent(this, EditUser.class));
                return(true);
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void update(View view) {
        if(isNameChanged() || isSurnameChanged() || isEmailChanged() || isNumberChanged()){
            Toast.makeText(this,"User Data Updated", Toast.LENGTH_LONG ).show();
       }else{
            Toast.makeText(this,"User Data did not change", Toast.LENGTH_LONG ).show();

        }
    }

    private boolean isSurnameChanged() {
        if(!surname.equals(displayTextSurname.getText().toString())){
            reference.child(userID).child("su").setValue(displayTextSurname.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    public boolean isNumberChanged() {
        if(!phone.equals(displayTextNumber.getText().toString())){
            reference.child(userID).child("ph").setValue(displayTextNumber.getText().toString());
            return true;
        }else{
            return false;
        }
    }


    public boolean isEmailChanged() {
        if(!email.equals(displayTextEmail.getText().toString())){
            reference.child(userID).child("em").setValue(displayTextEmail.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    public boolean isNameChanged() {
        if(!firstname.equals(displayTextName.getText().toString())){
                reference.child(userID).child("na").setValue(displayTextName.getText().toString());
                return true;
        }else{
            return false;
        }
    }
}