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

import com.example.bruhshua.carpool.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bruhshua on 5/19/17.
 */

public class RegisterActivity extends Activity{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference users_ref;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etPhoneNumber;
    private EditText etUserName;
    private Button bRegisterUser;

    private String key;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Log.d("LoginActivity","Inside onCreate RegisterActivity");

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance(); //create FirebaseAuth object and get the instance
        users_ref = database.getReference("users");

        etEmail = (EditText) findViewById(R.id.tvRegEmail);
        etPassword = (EditText) findViewById(R.id.tvRegPassword);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etUserName = (EditText) findViewById(R.id.etUserName);
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

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();


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
                        final String userName = etUserName.getText().toString();
                        String phoneNumber = etPhoneNumber.getText().toString();

                        User newUser = new User(phoneNumber,email,userName);

                        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                users_ref.child(key).child(userName).push().child("password").setValue(password);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Map<String, User> users = new HashMap<String, User>();
                        users.put(userName,newUser);

                        key = users_ref.push().getKey();
                        users_ref.child(key).setValue(users);

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        //Todo: add user to database

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
