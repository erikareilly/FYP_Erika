package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailLogin, passwordLogin;
    private Button button;
    private TextView register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailLogin=(EditText) findViewById(R.id.emailLog);
        passwordLogin=(EditText) findViewById(R.id.passwordLog);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        button=(Button) findViewById(R.id.button1);
        button.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.button1:
                userLogin();
                break;
        }
    }

    private void userLogin(){
        String email = emailLogin.getText().toString().trim();
        String password=passwordLogin.getText().toString().trim();

        if(email.isEmpty()){
            emailLogin.setError("Input email");
            emailLogin.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLogin.setError("Input valid email");
            emailLogin.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordLogin.setError("Input password");
            passwordLogin.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordLogin.setError("Password must be minimum 6 characters");
            passwordLogin.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to profile
                    startActivity(new Intent(MainActivity.this, Homepage.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to log in", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}