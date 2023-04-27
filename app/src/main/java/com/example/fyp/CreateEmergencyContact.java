package com.example.fyp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEmergencyContact extends AppCompatActivity {

    //UI Views
    private ImageView thumbnailIv;
    private TextView contactTv;
    private FloatingActionButton addFab;
    private static final int contact_permission_code=1;
    private static final int contact_pick_code=2;
    private Button saveButton1;
    private FirebaseAuth mAuth;
    String contactName;
    String contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_emergency_contact);
        //init UI views

        thumbnailIv = findViewById(R.id.contactThumbnail);
        contactTv = findViewById(R.id.contactText);
        addFab = findViewById(R.id.addFab);
        saveButton1 = findViewById(R.id.saveButton1);

        //on click to pick contacts
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check read contntact permission
                if(checkContactPermissions()){
                    //permission granted can pick contacts
                    pickContact();
                }else{
                    //permission not granted
                    requestContactPermission();
                }
            }
        });

    }

    private boolean checkContactPermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        //result will be true if permission has been granted, false if not
        return result;
    }

    private void requestContactPermission(){
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, permission, contact_permission_code );
    }

    private void pickContact(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, contact_pick_code);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //handle permission request result
        if(requestCode == contact_permission_code){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                //permission granted
                pickContact();
            }else{
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle intent results
        if(resultCode==RESULT_OK){
           //calls when user clicks a contact from list
            if(requestCode==contact_pick_code){
                contactTv.setText("");

                Cursor cursor, cursor1;

                Uri uri = data.getData();
                cursor = getContentResolver().query(uri,null,null,null,null);
                if(cursor.moveToFirst()){
                    //get contact details
                    @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range") String contactThumb = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    @SuppressLint("Range") String idResults = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);

                    contactTv.append("ID: " + contactId);
                    contactTv.append("\nName: " + contactName);

                    if(idResultHold ==1){
                        cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+ contactId,null,null);

                    //contact might have multiple numbers
                        while(cursor1.moveToNext()){
                            //get phone number
                            contactNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //set details
                            contactTv.append("\nNumber: " + contactNumber);
                            contactTv.append("\n\n ");
                            //before settign image, check if have one or not
                            if(contactThumb!= null){
                               thumbnailIv.setImageURI(Uri.parse(contactThumb));
                            }else{
                                thumbnailIv.setImageResource(R.drawable.ic_person);
                            }
                        }
                        cursor1.close();
                    }
                    cursor.close();
                    saveButton1.setVisibility(View.VISIBLE);
                    saveButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveContacts();
                        }
                    });
                }
            }
        }else{
            //calls when user clicks back button without clicking user
        }
    }

    private void saveContacts() {
        String name = contactName.trim();
        String number = contactNumber.trim();

        PhoneNumber phoneNumber = new PhoneNumber(name,number);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Emergency Contacts").push()
                .setValue(phoneNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(CreateEmergencyContact.this, "Emergency Contact has been saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateEmergencyContact.this, Emergency.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(CreateEmergencyContact.this, "Failed to add Emergency Contact", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}