package com.example.bruhshua.carpool.Activities;

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
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bruhshua.carpool.Fragments.AboutFragment;
import com.example.bruhshua.carpool.Fragments.CancelTripDialogFragment;
import com.example.bruhshua.carpool.Fragments.ContactFragment;
import com.example.bruhshua.carpool.Fragments.InvitationDialogFragment;
import com.example.bruhshua.carpool.Fragments.MyAccountFragment;
import com.example.bruhshua.carpool.Fragments.MyTripsFragment;
import com.example.bruhshua.carpool.Fragments.PlanTripFragment;
import com.example.bruhshua.carpool.Fragments.TripDetailsFragment;
import com.example.bruhshua.carpool.Fragments.TripMapFragment;
import com.example.bruhshua.carpool.Fragments.TripSummaryFragment;
import com.example.bruhshua.carpool.Model.MapUpdatePOJO;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.Presenters.MainActivityPresenter;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.interfaces.MainActivityInterface;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by bruhshua on 5/21/17.
 */

public class MainActivity extends AppCompatActivity implements MainActivityInterface.View,PlanTripFragment.Callback, TripDetailsFragment.Callback, TripSummaryFragment.Callback, CancelTripDialogFragment.Callback, InvitationDialogFragment.Callback{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolBar;
    private NavigationView nvDrawer;

    private User user;
    private FirebaseAuth firebaseAuth;

    private TripDetails tripInProgress;
    private MainActivityPresenter mainActivityPresenter;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("requestCode", "" + requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                tripMapFragment.updateLocation();
                break;
        }
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
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        mainActivityPresenter = new MainActivityPresenter(this);
        firebaseAuth = mainActivityPresenter.getAuthentication();
        user = (User) getIntent().getSerializableExtra("USER");

        getTripInProgressIfAvailable();
        setupNavigationDrawer();

    }

    private void getTripInProgressIfAvailable(){
        mainActivityPresenter.getTripInProgressIfAvailable(user);
    }

    private void registerInviteListener() {
        mainActivityPresenter.setInviteListener(user);
    }

    private void setupNavigationDrawer() {

        nvDrawer = (NavigationView) findViewById(R.id.navigationView);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.my_account:

                        //Pass in user data that is queried in MainActivity.
                        MyAccountFragment myAccountFragment = MyAccountFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, myAccountFragment)
                                .commit();
                        break;

                    case R.id.plan_trip:

                        TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripInProgress, user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, tripMapFragment)
                                .commit();

                        break;

                    case R.id.my_trips:

                        MyTripsFragment myTripsFragment = MyTripsFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, myTripsFragment)
                                .commit();
                        break;

                    case R.id.log_out:
                        logout();
                        break;

//                    case R.id.merch:
//
//                        MerchandiseFragment merchandiseFragment = MerchandiseFragment.newInstance(user);
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.fragment_container, merchandiseFragment)
//                                .commit();
//
//                        break;

                    case R.id.contact:
                        ContactFragment contactFragment = ContactFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, contactFragment)
                                .commit();
                        break;

                    case R.id.about:
                        AboutFragment aboutFragment = AboutFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, aboutFragment)
                                .commit();
                        break;

                    default:

                        MyAccountFragment fragment1 = MyAccountFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment1)
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
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void logout() {
        mainActivityPresenter.logout(firebaseAuth);
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Signed Out Successfully",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Could not sign out, please try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void updateMap(MapUpdatePOJO mapUpdatePOJO, TripDetails tripDetails, User user) {
        Log.d("mydebugger","MainActivity updateMap called" );

        tripInProgress = tripDetails;
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.updateMapForPassengers(tripDetails, user);

    }

    @Override
    public void showSummary(TripDetails tripDetails, User user) {
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.showSummary(tripDetails, user);
    }


    @Override
    public void Reset(User user) {
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.Reset(user);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed(); <--- is what closes the app

        Log.d("onBackPressed","onBackPressed");

        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Fragment hostedFragment = tripMapFragment.getFragmentManager().findFragmentById(R.id.plan_trip_fragment_container);

        if(hostedFragment instanceof TripDetailsFragment){
            Log.d("onBackPressed","instaceof TripDetails");
            CancelTripDialogFragment cancelTripDialogFragment = CancelTripDialogFragment.newInstance();
            cancelTripDialogFragment.show(this.getFragmentManager(), "CANCELTRIP");
        }else{
            Log.d("onBackPressed","else");

        }

    }

    @Override
    public void cancelTrip() {
        mainActivityPresenter.deleteTripInProgress(tripInProgress);
    }

    //Delete POJO from "trip_in_progress"
    @Override
    public void declineInvitation() {
        mainActivityPresenter.deleteTripInProgress(tripInProgress);
    }

    //Take User to TripDetailsFragment page
    @Override
    public void acceptInvitation(TripDetails tripDetails) {
        tripInProgress = tripDetails;
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.updateMapForPassengers(tripDetails,user);


    }

    @Override
    public void toInviteDialog(TripDetails tripDetails) {

        tripInProgress = tripDetails;
        InvitationDialogFragment invitationDialogFragment = InvitationDialogFragment.newInstance(tripDetails,user);
        invitationDialogFragment.show(this.getFragmentManager(),"TRIP_INVITATION");
    }

    @Override
    public void toTripCanceledView() {
        tripInProgress = null;
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.cancelTrip(user);
    }

    @Override
    public void toHostTripInProgressView(TripDetails tripDetails) {
        tripInProgress = tripDetails;
        TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripDetails, user);
              getSupportFragmentManager()
                      .beginTransaction()
                      .replace(R.id.fragment_container, tripMapFragment)
                      .commit();
    }

    @Override
    public void toPassengerInviteDialogView(TripDetails tripDetails) {
        tripInProgress = tripDetails;
        TripMapFragment tripMapFragment = TripMapFragment.newInstance(null, user);
              getSupportFragmentManager()
                      .beginTransaction()
                      .replace(R.id.fragment_container, tripMapFragment)
                      .commit();

        InvitationDialogFragment invitationDialogFragment = InvitationDialogFragment.newInstance(tripDetails,user);
        invitationDialogFragment.show(this.getFragmentManager(),"TRIP_INVITATION");
    }

    @Override
    public void toPassengerView(TripDetails tripDetails) {
        tripInProgress = tripDetails;
        TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripDetails, user);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, tripMapFragment)
                .commit();
    }

    @Override
    public void toMainView(TripDetails tripDetails) {
        tripInProgress = tripDetails;
        registerInviteListener();
        TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripDetails, user);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, tripMapFragment)
                .commit();
    }


}

