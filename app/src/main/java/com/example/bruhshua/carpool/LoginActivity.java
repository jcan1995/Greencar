package com.example.bruhshua.carpool;

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
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Fragments.LoginFragment;
import com.example.bruhshua.carpool.Fragments.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by bruhshua on 3/19/17.
 */

public class LoginActivity extends Activity implements RegisterFragment.AuthCallback, LoginFragment.LoginCallback {

    private TextView tvRegister;
    private int registerFragPressed = 0;

    private ProgressDialog Dialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        Fragment loginFragment = new LoginFragment();
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        ft.add(R.id.frameLayout,loginFragment);

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        AssetManager am = this.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am,  "fonts/Lobster.otf");

        tvRegister = (TextView)findViewById(R.id.tvRegister);
        tvRegister.setTypeface(custom_font);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFragPressed = 1;
                Fragment registerFragment = new RegisterFragment();
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.add(R.id.frameLayout,registerFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });



    }


    @Override
    public void onBackPressed() {
        if(registerFragPressed == 1){
            Fragment loginFragment = new LoginFragment();
            FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.add(R.id.frameLayout,loginFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    @Override
    public void Register(String email, String password) {
        Dialog = new ProgressDialog(this);
        Dialog.setMessage("Registering...");
        if (!email.equals("") || !password.equals("")) {

            Dialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }else{
                        Dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Failed.", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }


    @Override
    public void Login(String email, String password) {

        if (!email.equals("") || !password.equals("")) {

            Dialog = new ProgressDialog(this);
            Dialog.setMessage("Authenticating...");
            Dialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Dialog.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT).show();

                        //Todo: Go to MainActivity in app.
//                        Intent i = new Intent(getActivity(), MainActivity.class);
//                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(i);
                    }
                    else{
                        Dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Login Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
