package com.example.bruhshua.carpool.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bruhshua.carpool.R;

import java.io.File;

/**
 * Created by bruhshua on 3/19/17.
 */

public class LoginFragment extends Fragment {

    private TextView tvLogin;
    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private LoginCallback callback;

    public interface LoginCallback{
        public void Login(String email, String password);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragment.LoginCallback){
            callback = (LoginCallback) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment,container,false);
        AssetManager am = getActivity().getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am,  "fonts/Lobster.otf");

        tvLogin = (TextView) v.findViewById(R.id.tvLogin);
        tvLogin.setTypeface(custom_font);

        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etEmail.setTypeface(custom_font);

        etPassword = (EditText) v.findViewById(R.id.etPassword);
        etPassword.setTypeface(custom_font);

        bLogin = (Button) v.findViewById(R.id.bLogin);
        bLogin.setTypeface(custom_font);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Callback to LoginActivity where user logs in.
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                callback.Login(email,password);

                Log.d("LoginFragment","After callback");
            }
        });



        return v;
    }
}
