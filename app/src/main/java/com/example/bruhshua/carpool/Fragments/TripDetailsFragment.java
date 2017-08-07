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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
        setReceiveNotification();
        notifyPassengers();

        Log.d("Info", "Coordinates: " + String.valueOf(tripDetails.getmDestinationLat()) + ", " + tripDetails.getmDestinationLng());
        Log.d("Info","Points: " + tripDetails.getPoints());
       // updatePoints();

        bStart = (Button) v.findViewById(R.id.bStart);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected) {
                    bStart.setAlpha(.5f);
                    bStart.setEnabled(false);
                    bStart.setText("In Progress");
                    requestLocationUpdate();
                }else{
                    Toast.makeText(getActivity(),"Not Connected Yet.",Toast.LENGTH_LONG).show();
                }

            }
        });


        return v;
    }

    //Set listener on "invited_trips" in json.
    private void setReceiveNotification(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users").child(authUser.getKey()).child("invited_trips");
        users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TripDetails","notification received");
                Log.d("TripDetails","data: " + dataSnapshot.toString());

                TripDetails tripDetail = dataSnapshot.getValue(TripDetails.class);
                if(tripDetail != null){
                    Toast.makeText(getContext(),"You've been invited for a trip",Toast.LENGTH_SHORT).show();
                    //Means that another passenger received the tripdetail. Show dialog fragment to accept or decline
                }
              //  Toast.makeText(getContext(),"You've been invited for a trip",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void notifyPassengers() {
        Log.d("TripDetails","passenger size: " + tripDetails.getPassengers().size());
        Toast.makeText(getContext(),"Invitation sent",Toast.LENGTH_SHORT).show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users");
        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot messsageSnapshot : dataSnapshot.getChildren()){

                    User user = messsageSnapshot.getValue(User.class);

                    for(int i = 0; i < tripDetails.getPassengers().size(); i++){
                        if(user.getEmail().equals(tripDetails.getPassengers().get(i).getEmail()) && !user.getEmail().equals(authUser.getEmail())){
                            users_ref.child(user.getKey()).child("invited_trips").push().setValue(tripDetails);
                        }


                    }
                }

                Log.d("TripDetails","inOndataChange");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference users_ref = database.getReference("users");
//
//        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
//
//                    User user = messageSnapshot.getValue(User.class);
//
//                    for(int i = 0; i < tripDetail.getPassengers().size();i++){
//
//                        if(user.getUserName().equals(tripDetail.getPassengers().get(i).getUserName())){
//                            users_ref.child(messageSnapshot.getKey()).child("trips").push().setValue(tripDetail);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

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
                updatePoints();
                showSummary();
                addNewTrip(tripDetails);

            }else{
                Log.d("TripDetails","Exiting Proximity");

            }

        }
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
