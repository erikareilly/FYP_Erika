package com.example.fyp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Emergency extends AppCompatActivity implements View.OnClickListener {

    ListView showList;
    private DatabaseReference reference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> phoneList = new ArrayList<>();
    ArrayList<String> keysList = new ArrayList<>();
    ArrayAdapter<String>arrayAdapter;
    FloatingActionButton fab1;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button call, remove;
    String value1;
    String keyNo;
    static int PERMISSION_CODE = 100;
    String phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);


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

        fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(this);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Emergency Contacts");
        showList = (ListView) findViewById(R.id.searchList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        showList.setAdapter(arrayAdapter);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(PhoneNumber.class).toString();
                value1 = snapshot.getValue(PhoneNumber.class).getNumber();
                arrayList.add(value);
                phoneList.add(value1);
                keysList.add(snapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String delete = snapshot.getValue(PhoneNumber.class).toString();
                arrayList.remove(delete);
                keysList.remove(snapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               String key = keysList.get(position);
               keyNo = phoneList.get(position);
               reference.child(key).removeValue();
               return true;
            }
        });

        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog();
            }
        });

    }

    public void createDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View callPopup = getLayoutInflater().inflate(R.layout.popup, null);
        call = (Button) callPopup.findViewById(R.id.call);
        remove = (Button) callPopup.findViewById(R.id.remove);
        dialogBuilder.setView(callPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //after checking permissions complete call of phone number selected
                callContact();
            }

        });



        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissContact();
            }
        });

    }

    //request permissions to call the contact
    public void callContact(){
        if(ContextCompat.checkSelfPermission(Emergency.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Emergency.this, new String[] {Manifest.permission.CALL_PHONE},PERMISSION_CODE);
            //not running
            //phoneList.get(Integer.parseInt(value1));
            //phoneNo = phoneList.get(Integer.parseInt(value1));
            keyNo = keysList.get(showList.getSelectedItemPosition());
            phoneNo = phoneList.get(showList.getSelectedItemPosition());
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse(phoneNo));
           // i.setData(Uri.parse(phoneList.get(showList.getSelectedItemPosition())));
            startActivity(i);
        }

    }


    //dismiss dialog
    public void dismissContact(){
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab1:
                Intent intent = new Intent(Emergency.this, CreateEmergencyContact.class);
                startActivity(intent);
        }
    }
}