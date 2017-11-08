package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.Presenters.SplashScreenPresenter;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.interfaces.SplashScreenInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 7/28/17.
 */

public class SplashScreen extends Activity implements SplashScreenInterface.View{

    private SplashScreenPresenter splashScreenPresenter;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SplashScreen","onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashScreenPresenter = new SplashScreenPresenter(this);
        splashScreenPresenter.attemptLogin();
    }

    @Override
    public void toLoginPage() {
        finish();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void toHomePage(User authUser) {
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("USER",authUser);
        startActivity(i);
    }
}
