package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruhshua.carpool.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 5/19/17.
 */

public class RegisterActivity extends Activity{

    private FirebaseAuth firebaseAuth;
    private EditText etEmail, etPassword;
    private ProgressDialog dialog;

    private Button bRegisterUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Log.d("LoginActivity","Inside onCreate RegisterActivity");

        firebaseAuth = FirebaseAuth.getInstance(); //create FirebaseAuth object and get the instance

        etEmail = (EditText) findViewById(R.id.tvRegEmail);
        etPassword = (EditText) findViewById(R.id.tvRegPassword);
        bRegisterUser = (Button) findViewById(R.id.bRegister);
        bRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        if (firebaseAuth.getCurrentUser() != null) {
            //Todo: send user to the Main Activty
        }
    }




    public void registerUser(){

        Log.d("RegisterActivity","Inside registerUser");
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //if the email text field is empty
            Toast.makeText(getApplicationContext(),"Please Input Your Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //if the password text field is empty
            Toast.makeText(getApplicationContext(),"Please Input Your Password",Toast.LENGTH_LONG).show();
            return;
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering User...");
        dialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                    }else{
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Failed.", Toast.LENGTH_SHORT).show();

                    }

                }
            });

    }

    public void backToLogIn(View view){
        //send user back to the log in activity
        startActivity(new Intent(this,LoginActivity.class));
    }
}
