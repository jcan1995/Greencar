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
import com.example.bruhshua.carpool.Fragments.AddPassengersDialogFragment;
import com.example.bruhshua.carpool.Fragments.CancelTripDialogFragment;
import com.example.bruhshua.carpool.Fragments.ContactFragment;
import com.example.bruhshua.carpool.Fragments.InvitationDialogFragment;
import com.example.bruhshua.carpool.Fragments.MerchandiseFragment;
import com.example.bruhshua.carpool.Fragments.MyAccountFragment;
import com.example.bruhshua.carpool.Fragments.MyTripsFragment;
import com.example.bruhshua.carpool.Fragments.PlanTripFragment;
import com.example.bruhshua.carpool.Fragments.TripDetailsFragment;
import com.example.bruhshua.carpool.Fragments.TripMapFragment;
import com.example.bruhshua.carpool.Fragments.TripSummaryFragment;
import com.example.bruhshua.carpool.Model.MapUpdatePOJO;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 5/21/17.
 */

public class MainActivity extends AppCompatActivity implements PlanTripFragment.Callback, TripDetailsFragment.Callback, TripSummaryFragment.Callback, CancelTripDialogFragment.Callback, InvitationDialogFragment.Callback{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolBar;
    private TripDetails tripDetail;
    private NavigationView nvDrawer;

    private User user; //Todo:Get the current user using the application from firebase.
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog dialog;

    private boolean isOnConfirmationFragment = false;

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
    protected void onPause() {
        super.onPause();

        Log.d("lifecyle", "onPause called");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecyle", "onResume called");


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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();

        user = (User) getIntent().getSerializableExtra("USER");

        setReceiveNotification();
        TripMapFragment tripMapFragment = TripMapFragment.newInstance(user);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, tripMapFragment)
                .commit();

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

                        // if(!(fragment instanceof TripMapFragment)) {
                        TripMapFragment tripMapFragment = TripMapFragment.newInstance(user);
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

                    case R.id.merch:

                        MerchandiseFragment merchandiseFragment = MerchandiseFragment.newInstance(user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, merchandiseFragment)
                                .commit();

                        break;

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

        firebaseAuth.signOut();

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
            Log.d("MainActivity", "item selected");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void updateMap(MapUpdatePOJO mapUpdatePOJO, TripDetails tripDetails, User user) {

        Log.d("MainActivity", "inside updateMap");
        isOnConfirmationFragment = true;
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.updateMap(mapUpdatePOJO, tripDetails, user);

    }

    @Override
    public void showSummary(TripDetails tripDetails, User user) {
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.showSummary(tripDetails, user);
    }

//    @Override
//    public void notifyPassengers() {
//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference users_ref = database.getReference("users").child(user.getKey()).child("invited_trips");
//
//        users_ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("TripDetails", "notification received");
//                Log.d("TripDetails", "data: " + dataSnapshot.toString());
//
//                TripDetails tripDetail = dataSnapshot.getValue(TripDetails.class);
//                if (tripDetail != null) {
//                    Toast.makeText(getApplicationContext(), "You've been invited for a trip", Toast.LENGTH_SHORT).show();
//                    //  callback.notifyPassengers(tripDetail);
//                    //Means that another passenger received the tripdetail. Show dialog fragment to accept or decline
//                }
//                //  Toast.makeText(getContext(),"You've been invited for a trip",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }


    @Override
    public void Reset(User user) {
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.Reset(user);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed(); <--- is what closes the app

        if (isOnConfirmationFragment) {
            CancelTripDialogFragment cancelTripDialogFragment = CancelTripDialogFragment.newInstance();
            cancelTripDialogFragment.show(this.getFragmentManager(), "CANCELTRIP");

        }

    }

    @Override
    public void cancelTrip() {
        isOnConfirmationFragment = false;
        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.cancelTrip(user);
    }

    //Checks "trip_in_progress" field in JSON to see if the user is currently on a trip. There will always only be 1 trip in that field.
    private void setReceiveNotification() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users").child(user.getKey()).child("trip_in_progress");

        users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TripDetails","notification received");
                Log.d("TripDetails","data: " + dataSnapshot.toString());

                if(tripDetail == null) {
                    tripDetail = dataSnapshot.getValue(TripDetails.class);
//                    if(tripDetail.getPassengers() == null){
//                        Log.d("TripDetails","notification received");
//
//                    }
                }
                if(tripDetail != null && !tripDetail.isAckByPassenger()){
                   // Toast.makeText(getApplicationContext(),"You've been invited for a trip",Toast.LENGTH_LONG).show();

                    tripDetail.setAckByPassenger(true);
                    if(tripDetail.getPassengers() == null){
                        //Todo: Set Arraylist of passengers manually.
                        Log.d("TripDetails","passengers is null right when received");

                    }
                    testMethod();
//                    InvitationDialogFragment invitationDialogFragment = InvitationDialogFragment.newInstance(tripDetail);
//                    invitationDialogFragment.show(getFragmentManager(),"TRIP_INVITATION");


                    //Means that another passenger received the tripdetail. Show dialog fragment to accept or decline
                }
                //  Toast.makeText(getContext(),"You've been invited for a trip",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void testMethod() {
        InvitationDialogFragment invitationDialogFragment = InvitationDialogFragment.newInstance(tripDetail);
        invitationDialogFragment.show(this.getFragmentManager(),"TRIP_INVITATION");
    }

    //Delete POJO from "trip_in_progress"
    @Override
    public void declineInvitation() {



    }

    //Take User to TripDetailsFragment page
    @Override
    public void acceptInvitation(TripDetails tripDetails) {

        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.updateMapForPassengers(tripDetails,user);


    }
}

