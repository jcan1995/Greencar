package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 7/28/17.
 */

public class SplashScreen extends Activity {

    private FirebaseAuth firebaseAuth;
    private static int SPLASH_TIME_OUT = 1000;
    private DatabaseReference users_ref;
    private FirebaseDatabase database;


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("SplashScreen","onStart");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SplashScreen","in run");

                if(firebaseAuth.getCurrentUser() != null){
                    Log.d("SplashScreen","after condition check");

                    users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("SplashScreen","in onDataChange ");

                            for(DataSnapshot message: dataSnapshot.getChildren()){
                                User authUser = message.getValue(User.class);

                                if(firebaseAuth.getCurrentUser().getEmail().equals(authUser.getEmail())){

                                    finish();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("USER",authUser);
                                    startActivity(i);

                                }
                            }

                            Log.d("SplashScreen","in onDataChange after for loop");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("SplashScreen","in onCancelled");
                        }
                    });
                }else{
                    Log.d("SplashScreen","in else ");
                    finish();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }

            }



        },SPLASH_TIME_OUT);
        Log.d("SplashScreen","after timeout");

        finish();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        Log.d("SplashScreen","after timeout");
    }
}
