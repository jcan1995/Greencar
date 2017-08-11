package com.example.bruhshua.carpool.Fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Model.MapUpdatePOJO;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by bruhshua on 7/7/17.
 */

public class TripDetailsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private User authUser;
    private TripDetails tripDetails;
    private Button bStart;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location previousLocation;

    private GoogleApiClient mGoogleApiClient;
    private Callback callback;
    private PendingIntent proximityIntent;
    private boolean isConnected;
    private ProximityIntentReceiver proximityIntentReceiver;
    private final static String PROX_ALERT_INTENT = "com.action.proxalert";

    public interface Callback {
        public void showSummary(TripDetails tripDetails, User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PlanTripFragment.Callback){
            callback = (TripDetailsFragment.Callback) context;
        }
        Log.d("lifecycleCheck", "TripDetailsFragment: onAttach called");


    }
    public static TripDetailsFragment newInstance(TripDetails tripDetails, User user) {

        TripDetailsFragment tripDetailsFragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("TRIPDETAILS", tripDetails);
        args.putSerializable("USER", user);
        tripDetailsFragment.setArguments(args);
        return tripDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Log.d("lifecycleCheck", "TripDetailsFragment: onCreate called");

    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("lifecycleCheck", "TripDetailsFragment: onStart called");


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycleCheck", "TripDetailsFragment: onResume called");

    }

    @Override
    public void onStop() {
        super.onStop();
       // updateTripProgress();
        Log.d("lifecycleCheck", "TripDetailsFragment: onStop called");

    }

    private void updateTripProgress() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users");
        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot messsageSnapshot : dataSnapshot.getChildren()){

                    User user = messsageSnapshot.getValue(User.class);
                    users_ref.child(user.getKey()).child("trip_in_progress").setValue(tripDetails);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
       // updateTripProgress();
        Log.d("lifecycleCheck", "TripDetailsFragment: onPause called");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        Log.d("lifecycleCheck", "TripDetailsFragment: onActivityCreated called");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("lifecycleCheck", "TripDetailsFragment: onCreateView called");

        View v = inflater.inflate(R.layout.confirm_trip_fragment, container, false);
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getActivity(),"Back pressed",Toast.LENGTH_SHORT).show();
//                    return true;
                }
                return true;
               // return false;
            }
        });

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        authUser = (User) getArguments().getSerializable("USER");
        tripDetails = (TripDetails) getArguments().getSerializable("TRIPDETAILS");
        bStart = (Button) v.findViewById(R.id.bStart);

        if(!tripDetails.isInvitationSent() && tripDetails.getHost().equals(authUser.getEmail())){
            sendInvitations();
        }

       // If the current user is the host
        if (tripDetails.getHost().equals(authUser.getEmail())) {
            if(!tripDetails.isInProgress()) {
                bStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isConnected) {
                            tripDetails.setInProgress(true);//Update db when stop or pause lifecycle methods are called
                            bStart.setAlpha(.5f);
                            bStart.setEnabled(false);
                            bStart.setText("In Progress");
                            updateTripProgress();
                            requestLocationUpdate();
                        } else {
                            bStart.setAlpha(.5f);
                            bStart.setEnabled(false);
                            bStart.setText("In Progress");
                        }

                    }
                });
            } else if(tripDetails.isInProgress()) {
                bStart.setAlpha(.5f);
                bStart.setEnabled(false);
                bStart.setText("In Progress");
            }
        }
        //If the current user is not the host. (A passenger)
        else if(!tripDetails.getHost().equals(authUser.getEmail())){
            if(!tripDetails.isInProgress()) {
                if (isConnected) {
                   // tripDetails.setInProgress(true);//Update db when stop or pause lifecycle methods are called
                    bStart.setAlpha(.5f);
                    bStart.setEnabled(false);
                    bStart.setText("Please wait...");
                    //requestLocationUpdate();
                } else {
                    bStart.setAlpha(.5f);
                    bStart.setEnabled(false);
                    bStart.setText("Please wait...");
                }
            }else if(tripDetails.isInProgress()){
              //  tripDetails.setInProgress(true);//Update db when stop or pause lifecycle methods are called
                bStart.setAlpha(.5f);
                bStart.setEnabled(false);
                bStart.setText("In Progress");
             //   requestLocationUpdate();
            }
        }

        return v;
    }

    private void sendInvitations() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users");
        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(int i = 0; i< tripDetails.getPassengers().size();i++){
                    if(tripDetails.getHost().equals(tripDetails.getPassengers().get(i).getEmail())){
                        tripDetails.getPassengers().get(i).setAckCurrentTrip(true);
                    }
                }

                for(DataSnapshot messsageSnapshot : dataSnapshot.getChildren()){

                    User user = messsageSnapshot.getValue(User.class);
                    tripDetails.setInvitationSent(true);

                          if(tripDetails.getHost().equals(user.getEmail())){

                              TripDetails tripDetailsForHost = tripDetails;
                              tripDetailsForHost.setInvitationReceived(true);

                              users_ref.child(user.getKey()).child("trip_in_progress").setValue(tripDetailsForHost);


                          }else {
                              tripDetails.setInvitationReceived(false);
                              users_ref.child(user.getKey()).child("trip_in_progress").setValue(tripDetails);
                          }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Updates","onConnected");
        isConnected = true;
    }

    private void requestLocationUpdate() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        Intent intent = new Intent(PROX_ALERT_INTENT);
        proximityIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.addProximityAlert(tripDetails.getmDestinationLat(),tripDetails.getmDestinationLng(),20,-1,proximityIntent);

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);

        proximityIntentReceiver = new ProximityIntentReceiver();

        getActivity().registerReceiver(proximityIntentReceiver,filter);
        //Toast.makeText(getActivity(),"Proximity Alert Set",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        //Log.d("Updates","Location: " + location.toString());
    }

    class ProximityIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String key = LocationManager.KEY_PROXIMITY_ENTERING;
            Boolean entering = intent.getBooleanExtra(key, false);
            if(entering){
                Log.d("TripDetails","onReceive called");
                Log.d("TripDetails","Entering Proximity");
                locationManager.removeProximityAlert(proximityIntent);
                getActivity().unregisterReceiver(proximityIntentReceiver);
                deleteInProgressNode();
                updatePoints();
                showSummary();
                addNewTrip(tripDetails);

            }else{
                Log.d("TripDetails","Exiting Proximity");

            }

        }
    }

    //Will delete "trip_in_progress" POJO from Firebase.
    private void deleteInProgressNode() {






    }

    private void showSummary() {

        callback.showSummary(tripDetails,authUser);

    }

    private void updatePoints() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(int i = 0; i < tripDetails.getPassengers().size();i++){

            final DatabaseReference users_ref = database.getReference("users/" + tripDetails.getPassengers().get(i).getKey() + "/points");
            users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double points = dataSnapshot.getValue(double.class);
                    points += tripDetails.getPoints();
                    users_ref.setValue(points);
                    Log.d("Points: ","" + points);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }
    private void addNewTrip(final TripDetails tripDetail) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users");

        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){

                    User user = messageSnapshot.getValue(User.class);

                    for(int i = 0; i < tripDetail.getPassengers().size();i++){

                        if(user.getUserName().equals(tripDetail.getPassengers().get(i).getUserName())){
                            users_ref.child(messageSnapshot.getKey()).child("trips").push().setValue(tripDetail);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
