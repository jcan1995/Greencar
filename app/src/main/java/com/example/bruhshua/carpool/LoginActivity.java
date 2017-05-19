package com.example.bruhshua.carpool;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.bruhshua.carpool.Fragments.LoginFragment;
import com.example.bruhshua.carpool.Fragments.RegisterFragment;


/**
 * Created by bruhshua on 3/19/17.
 */

public class LoginActivity extends Activity {

    private TextView tvRegister;
    private int registerFragPressed = 0;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

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
}
