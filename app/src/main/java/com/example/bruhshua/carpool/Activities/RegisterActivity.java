package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruhshua.carpool.R;

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

    FirebaseAuth firebaseAuth;
    EditText email, password;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        firebaseAuth = FirebaseAuth.getInstance(); //create FirebaseAuth object and get the instance

        email = (EditText) findViewById(R.id.tvRegEmail);
        password = (EditText) findViewById(R.id.tvRegPassword);

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            //Todo: send user to the Main Activty
        }
    }




    public void registerUser(View view){

        String parsedEmail = email.getText().toString().trim();
        String parsedPassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(parsedEmail)){
            //if the email text field is empty
            Toast.makeText(getApplicationContext(),"Please Input Your Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(parsedPassword)){
            //if the password text field is empty
            Toast.makeText(getApplicationContext(),"Please Input Your Password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        //Todo: query data into Firebase

    }

    public void backToLogIn(View view){
        //send user back to the log in activity
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }
}
