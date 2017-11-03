package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruhshua.carpool.Presenters.LoginActivityPresenter;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.interfaces.LoginActivityInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruhshua on 11/1/17.
 */

//LoginActivity View class that only Prompts user for email and password. All other processes should be in a separate class.
public class LoginActivity extends Activity implements LoginActivityInterface.View {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    private LoginActivityPresenter presenter;
    private ProgressDialog Dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        presenter = new LoginActivityPresenter(this);
    }

    public void login(View v){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(!email.equals("")||!password.equals("")){
            Dialog = new ProgressDialog(this);
            Dialog.setMessage("Logging in");
            Dialog.show();
            presenter.login(email,password,this);
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

    @Override
    public void attemptLogin(User user) {
        Dialog.dismiss();
        if(user != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("USER", user);
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(),"Error Signing in.",Toast.LENGTH_SHORT).show();
        }

    }
}
