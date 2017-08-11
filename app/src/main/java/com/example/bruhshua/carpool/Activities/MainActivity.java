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
import com.google.android.gms.maps.CameraUpdate;
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
    private TripDetails tripDetails;
    private NavigationView nvDrawer;

    private User user; //Todo:Get the current user using the application from firebase.
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog dialog;
    private TripDetails tripInProgress;


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

        Log.d("lifecycleCheck", "MainActivity: onPause called");

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycleCheck", "MainActivity: onStart called");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycleCheck", "MainActivity: onRestart called");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycleCheck", "MainActivity: onResume called");


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

        Log.d("lifecycleCheck", "MainActivity: onCreate called");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();

        user = (User) getIntent().getSerializableExtra("USER");

        getCurrentTripIfAvailable();

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
        Log.d("mydebugger","MainActivity updateMap called" );

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
      //  isOnConfirmationFragment = false;
        tripInProgress = null;
        tripDetails = null;
        deleteTripInProgress();


        TripMapFragment tripMapFragment = (TripMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        tripMapFragment.cancelTrip(user);
    }

    private void deleteTripInProgress() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users").child(user.getKey()).child("trip_in_progress");
        users_ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d("deletionTest","onComplete: deleteTripInProgress");

            }
        });


    }


    private void getCurrentTripIfAvailable(){
        Log.d("debugger","getCurrentTripIfAvailable");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users").child(user.getKey()).child("trip_in_progress");

        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tripInProgress = dataSnapshot.getValue(TripDetails.class);

                if(tripInProgress != null){

                    if(tripInProgress.getHost().equals(user.getEmail())){

                        //Current user is hosting the trip
                        TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripInProgress, user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, tripMapFragment)
                                .commit();
                        Log.d("transactions","tripmapfrag attached");

                    }else if(!tripInProgress.getHost().equals(user.getEmail()) && !tripInProgress.isInvitationReceived()){

                        //Current user is a passenger of the trip and hasn't received an invitation
                        TripMapFragment tripMapFragment = TripMapFragment.newInstance(null, user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, tripMapFragment)
                                .commit();

                        toInvitationDialogFragment();

                    }else if(!tripInProgress.getHost().equals(user.getEmail()) && tripInProgress.isInvitationReceived()){
                        //Current user is a passenger of the trip and has already received an invitation
                        TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripInProgress, user);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, tripMapFragment)
                                .commit();
                    }


                }

                else{
                    //No trip in progress
                    TripMapFragment tripMapFragment = TripMapFragment.newInstance(tripInProgress, user);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, tripMapFragment)
                            .commit();
                    Log.d("transactions","tripmapfrag attached from else");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    
    private void toInvitationDialogFragment() {
        InvitationDialogFragment invitationDialogFragment = InvitationDialogFragment.newInstance(tripInProgress,user);
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

