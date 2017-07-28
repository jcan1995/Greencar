package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
    private static int SPLASH_TIME_OUT = 2500;
    private DatabaseReference users_ref;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(firebaseAuth.getCurrentUser() != null){
                    database = FirebaseDatabase.getInstance();
                    users_ref = database.getReference("users");

                    users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot message: dataSnapshot.getChildren()){
                                User authUser = message.getValue(User.class);
                                if(firebaseAuth.getCurrentUser().getEmail().equals(authUser.getEmail())){

                                    SplashScreen.this.finish();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("USER",authUser);
                                    //i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(i);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    SplashScreen.this.finish();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }

            }
        },SPLASH_TIME_OUT);

    }
}
