package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    Button notifyButton;
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    List<NotificationClass> list;
    DatabaseReference nReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notifyButton = (Button) findViewById(R.id.notify);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notifications.this, CreateNotification.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.notifList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        nReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Notifications");
        notificationAdapter = new NotificationAdapter(this,list);
        recyclerView.setAdapter(notificationAdapter);
        nReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    NotificationClass notif = dataSnapshot.getValue(NotificationClass.class);
                    list.add(notif);
                }
                notificationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

