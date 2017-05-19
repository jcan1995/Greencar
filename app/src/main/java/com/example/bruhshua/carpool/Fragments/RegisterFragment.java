package com.example.bruhshua.carpool.Fragments;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bruhshua.carpool.R;

import org.w3c.dom.Text;

import static com.example.bruhshua.carpool.R.id.bLogin;

/**
 * Created by bruhshua on 3/23/17.
 */

public class RegisterFragment extends Fragment {

    private TextView tvLogin;
    private EditText etEmail;
    private EditText etPassword;
    private Button bSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_fragment,container,false);
        AssetManager am = getActivity().getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am,  "fonts/Lobster.otf");

        tvLogin = (TextView)v.findViewById(R.id.tvLogin);
        etEmail = (EditText)v.findViewById(R.id.etEmail);
        etPassword = (EditText)v.findViewById(R.id.etPassword);
        bSubmit = (Button)v.findViewById(R.id.bSubmit);

        tvLogin.setTypeface(custom_font);
        etEmail.setTypeface(custom_font);
        etPassword.setTypeface(custom_font);
        bSubmit.setTypeface(custom_font);


        return v;
    }
}
