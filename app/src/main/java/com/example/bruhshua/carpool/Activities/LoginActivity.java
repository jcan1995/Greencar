package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * Created by bruhshua on 3/19/17.
 */

public class LoginActivity extends Activity {

    private EditText etEmail;
    private EditText etPassword;

    private ProgressDialog Dialog;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference users_ref;


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();
    }


    public void login(View v) {

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (!email.equals("") || !password.equals("")) {

            Dialog = new ProgressDialog(this);
            Dialog.setMessage("Authenticating...");
            Dialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                 //   Dialog.dismiss();
                    if (task.isSuccessful()) {
                       // Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT).show();
                        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot message: dataSnapshot.getChildren()){
                                    User authUser = message.getValue(User.class);
                                    if(email.equals(authUser.getEmail())){
                                        finish();
                                        Dialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.putExtra("USER",authUser);
                                        startActivity(i);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Login Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public void register(View v){

        finish();
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);

    }

    public void forgotPassword(View v){

        finish();
        Intent i = new Intent(this,ResetPasswordActivity.class);
        startActivity(i);

    }
}
