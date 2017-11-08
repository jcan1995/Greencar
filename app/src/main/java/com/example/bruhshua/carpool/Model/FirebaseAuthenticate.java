package com.example.bruhshua.carpool.Model;

import android.os.Handler;

import com.example.bruhshua.carpool.Presenters.SplashScreenPresenter;
import com.example.bruhshua.carpool.interfaces.SplashScreenInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 11/7/17.
 */

public class FirebaseAuthenticate {

    private SplashScreenInterface.Presenter mPresenter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference users_ref;
    private static int SPLASH_TIME_OUT = 1000;


    public FirebaseAuthenticate(SplashScreenInterface.Presenter presenter){
        mPresenter = presenter;
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");
    }

    public void checkAuth(){
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser() != null){
                    users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot message: dataSnapshot.getChildren()){
                                User authUser = message.getValue(User.class);
                                if(firebaseAuth.getCurrentUser().getEmail().equals(authUser.getEmail())){
                                    mPresenter.authenticated(authUser);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }else{
                    mPresenter.notAuthenticated();
                }
            }
        },SPLASH_TIME_OUT);
    }

}
