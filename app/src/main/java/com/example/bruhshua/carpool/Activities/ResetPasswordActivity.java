package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruhshua.carpool.R;

/**
 * Created by bruhshua on 5/19/17.
 */

public class ResetPasswordActivity extends Activity {

    private Button bResetPassword;
    private EditText etEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        etEmail = (EditText) findViewById(R.id.etEmail);
        bResetPassword = (Button) findViewById(R.id.bSendEmail);
        bResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                if(!email.equals("")){
                    Log.d("ResetActivity","Send Email");

                    //Todo: Send email.
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter an Email.",Toast.LENGTH_SHORT).show();
                    Log.d("ResetActivity","Please Enter an Email.");
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this,LoginActivity.class));

    }
}
