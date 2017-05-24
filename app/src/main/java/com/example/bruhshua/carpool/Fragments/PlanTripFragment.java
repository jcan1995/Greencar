package com.example.bruhshua.carpool.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bruhshua.carpool.Manifest;
import com.example.bruhshua.carpool.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by bruhshua on 5/21/17.
 */

public class PlanTripFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private SupportMapFragment mSupportMapFragment;
    private EditText etNumberOfPassengers;
    private Button bSetTrip;


    public static PlanTripFragment newInstance() {
        PlanTripFragment planTripFragment = new PlanTripFragment();
        Bundle args = new Bundle();
        // args.put("DIALOF",dialog);
        planTripFragment.setArguments(args);
        return planTripFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.plan_trip_fragment, container, false);


        etNumberOfPassengers = (EditText) v.findViewById(R.id.etNumPassengers);
        bSetTrip = (Button) v.findViewById(R.id.bSetTrip);
        bSetTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get data, set trip
            }
        });

        mSupportMapFragment = SupportMapFragment.newInstance();
        FragmentManager fm = getFragmentManager();
        mSupportMapFragment.getMapAsync(this);
        if (!mSupportMapFragment.isAdded())
            fm.beginTransaction().add(R.id.map_fragment, mSupportMapFragment).commit();

        else if (mSupportMapFragment.isAdded())
            fm.beginTransaction().hide(mSupportMapFragment).commit();
        else
            fm.beginTransaction().show(mSupportMapFragment).commit();

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onCreate", "onMapReady fired");
        map = googleMap;

       // checkPermissions();
    }


    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("onCreate", "ACCESS_FINE_LOCATION INCLUDED");
            //This method doesn't provide specific data that we need to use. Such as LatLng
            //Maybe we can use this method just for its UI.
            //  map.setMyLocationEnabled(true);
        } else {
            Log.d("onCreate", "ACCESS_FINE_LOCATION NOT INCLUDED");
            //Callback onRequestPermissionsResult is called in hosting activity!
            //Todo: Maybe show dialog that tells users why we need their location.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onCreate", "onConnected fired");

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("onCreate", "ACCESS_FINE_LOCATION INCLUDED");
            //This method doesn't provide specific data that we need to use. Such as LatLng
            //Maybe we can use this method just for its UI.

            map.setMyLocationEnabled(true);
        } else {
            Log.d("onCreate", "ACCESS_FINE_LOCATION NOT INCLUDED");
            //Callback onRequestPermissionsResult is called in hosting activity!
            //Todo: Maybe show dialog that tells users why we need their location.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //Todo: Set destination
        if (mCurrentLocation != null) {

            Log.d("onCreate","Lat: "+mCurrentLocation.getLatitude());
            Log.d("onCreate","Long: "+mCurrentLocation.getLongitude());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
