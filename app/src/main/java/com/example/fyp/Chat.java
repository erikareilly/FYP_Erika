package com.example.fyp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chat extends AppCompatActivity {
    // RecyclerView recyclerView;
    // ImageView profile;
    TextView name,chatText;
    EditText messageChat;
    ImageButton send;
    FirebaseAuth firebaseAuth;
    List<Message> list;
    //MessageAdapter messageAdapter;
    // FirebaseDatabase firebaseDatabase;
    // DatabaseReference reference;
    boolean notify = false;
    String message;
    String uid, myuid;
    AlertDialog alertDialog;
    private String apiEndpoint =  "https://api.fda.gov/drug/label.json?search=drug_interactions:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.nametv);
        messageChat = findViewById(R.id.message);
        send = findViewById(R.id.sendmsg);
        chatText = findViewById(R.id.textView2);
       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = findViewById(R.id.chatrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        uid = getIntent().getStringExtra("uid");

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users");*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                message = messageChat.getText().toString().trim();
                //chatText.setText(message);
                if (TextUtils.isEmpty(message)) {//if empty
                    Toast.makeText(Chat.this, "Write Something Here", Toast.LENGTH_LONG).show();
                } else {
                    sendmessage(message);
                }
                messageChat.setText("");
                sendReply();
            }
        });

        readMessages();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    protected void onStart() {
        super.onStart();
    }

    private void readMessages() {
        // show message after retrieving data
        list = new ArrayList<>();
       /* DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chat");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                        list.add(message); // add the chat in chatlist
                        sendReply();
                   // }
                    messageAdapter = new MessageAdapter(Chat.this, list);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void sendmessage(final String message) {
        // creating a reference to store data in firebase

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myuid);
        hashMap.put("receiver", uid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        //hashMap.put("dilihat", false);
        hashMap.put("type", "text");
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chat").push().setValue(hashMap);
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chat");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    ref1.child("id").setValue(myuid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chat");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    ref2.child("id").setValue(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void sendReply(){
        try {
            String encodedGenericName = URLEncoder.encode(message, "UTF-8");
            String queryUrl = apiEndpoint + encodedGenericName+"&limit=1";
            new Chat.openFDAEndpoint().execute(queryUrl);
            //Intent intent = new Intent(LogMedication.this, LogMedicine.class);
            // startActivity(intent);
        }catch (Exception e) {
            Log.e(TAG, "Error encoding query parameter", e);
        }
    }

    private class openFDAEndpoint extends AsyncTask<String,Void,String> {

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
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("results");
                for(int i =0; i<result.length();i++) {
                    JSONObject resultObject = result.getJSONObject(i);
                    JSONArray precautions = resultObject.getJSONArray("precautions");
                    String warnings = precautions.getString(0);
                    if(precautions.length()>0){
                        //messageChat.setText(warnings);
                        //chatText.setText(warnings);
                        // Toast.makeText(Chat.this, warnings, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                        builder.setTitle("Precautions");
                        builder.setMessage(warnings);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
                        // Assign the dialog to the alertDialog variable
                        alertDialog = builder.create();
                        alertDialog.show();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }





}