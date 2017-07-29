package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.bruhshua.carpool.Fragments.MyAccountFragment;
import com.example.bruhshua.carpool.Fragments.MyTripsFragment;
import com.example.bruhshua.carpool.Fragments.PlanTripFragment;
import com.example.bruhshua.carpool.Fragments.TripMapFragment;
import com.example.bruhshua.carpool.Model.MapUpdatePOJO;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;

/**
 * Created by bruhshua on 5/21/17.
 */

public class MainActivity extends AppCompatActivity implements PlanTripFragment.Callback {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolBar;

    private NavigationView nvDrawer;

    private User user; //Todo:Get the current user using the application from firebase.
    private ProgressDialog dialog;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("requestCode",""+requestCode);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("lifecyle","onPause called");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecyle","onResume called");


    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolBar = (Toolbar) findViewById(R.id.toolBar); //initialize toolbar
        setSupportActionBar(toolBar); //add toolbar to application

        user = (User) getIntent().getSerializableExtra("USER");

        TripMapFragment tripMapFragment = TripMapFragment.newInstance(user);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,tripMapFragment)
                .commit();


        Log.d("Inside","username: " + user.getUserName());

        nvDrawer = (NavigationView) findViewById(R.id.navigationView);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.my_account:

                        //Pass in user data that is queried in MainActivity.
                        MyAccountFragment myAccountFragment = MyAccountFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,myAccountFragment)
                                .commit();
                        break;

                    case R.id.plan_trip:

                        TripMapFragment tripMapFragment = TripMapFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,tripMapFragment)
                                .commit();
                        break;

                    case R.id.my_trips:

                        MyTripsFragment myTripsFragment = MyTripsFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,myTripsFragment)
                                .commit();
                        break;

                    default:

                        MyAccountFragment fragment1 = MyAccountFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,fragment1)
                                .commit();
                        break;

                }
                item.setCheckable(true);
                setTitle(item.getTitle());
                drawerLayout.closeDrawers();

                return true;

            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateMap(MapUpdatePOJO mapUpdatePOJO, TripDetails tripDetails) {

        Log.d("MainActivity","inside updateMap");
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.updateMap(mapUpdatePOJO,tripDetails);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }
}
