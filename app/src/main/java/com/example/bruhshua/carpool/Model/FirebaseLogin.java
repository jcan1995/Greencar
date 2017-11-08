package com.example.bruhshua.carpool.Model;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.bruhshua.carpool.Activities.MainActivity;
import com.example.bruhshua.carpool.interfaces.LoginActivityInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by bruhshua on 11/1/17.
 */

public class FirebaseLogin {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference users_ref;

    private LoginActivityInterface.Presenter mPresenter;

    public FirebaseLogin(LoginActivityInterface.Presenter presenter){
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mPresenter = presenter;
    }

    public void login(final String email, String password, Activity activity){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot message: dataSnapshot.getChildren()){
                                User authUser = message.getValue(User.class);
                                if(email.equals(message.getValue(User.class).getEmail())){
                                    mPresenter.loadUser(message.getValue(User.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else
                    mPresenter.loadUser(null);

            }
        });
    }



}
