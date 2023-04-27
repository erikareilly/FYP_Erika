package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextName, editTextSurname,editTextEmail,editTextPhoneNumber,editTextPass;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();

        editTextName=(EditText) findViewById(R.id.name);
        editTextSurname=(EditText) findViewById(R.id.surname);
        editTextEmail=(EditText) findViewById(R.id.email);
        editTextPhoneNumber=(EditText) findViewById(R.id.phoneNumber);
        editTextPass=(EditText) findViewById(R.id.pass);

        button=(Button) findViewById(R.id.button2);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button2:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String na = editTextName.getText().toString().trim();
        String su = editTextSurname.getText().toString().trim();
        String em = editTextEmail.getText().toString().trim();
        String ph = editTextPhoneNumber.getText().toString().trim();
        String pa = editTextPass.getText().toString().trim();

        if(na.isEmpty()){
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        if(su.isEmpty()){
            editTextSurname.setError("Surname required");
            editTextSurname.requestFocus();
            return;
        }
        if(ph.isEmpty()){
            editTextPhoneNumber.setError("Number required");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if(pa.isEmpty()){
            editTextPass.setError("Password required");
            editTextPass.requestFocus();
            return;
        }
        if(pa.length() <6){
            editTextPass.setError("Password must be minimum 6 characters");
            editTextPass.requestFocus();
            return;
        }

        if(em.isEmpty()){
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            editTextEmail.setError("Provide valid email address");
            editTextEmail.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(em,pa)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user= new User(na,su,em,ph);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been registered", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterUser.this, "User has not been registered", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this, "User has not been registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }}