package com.example.bruhshua.carpool.Fragments;

import android.Manifest;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

/**
 * Created by bruhshua on 7/7/17.
 */

public class TripDetailsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private TripDetails tripDetails;
    private TextView tvSourceAddress;
    private TextView tvDestinationAddress;
    private TextView tvMiles;
    private TextView tvPoints;
    private TextView tvPassengers;
    private Button bStart;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location previousLocation;

    private GoogleApiClient mGoogleApiClient;

    private boolean isConnected;
    public static TripDetailsFragment newInstance(TripDetails tripDetails) {

        TripDetailsFragment tripDetailsFragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("TRIPDETAILS", tripDetails);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.confirm_trip_fragment, container, false);
        tripDetails = (TripDetails) getArguments().getSerializable("TRIPDETAILS");

        tvSourceAddress = (TextView) v.findViewById(R.id.tvSourceDestination);
        tvDestinationAddress = (TextView) v.findViewById(R.id.tvDestinationAddress);
        tvMiles = (TextView) v.findViewById(R.id.tvMiles);
        tvPoints = (TextView) v.findViewById(R.id.tvPoints);
        tvPassengers = (TextView) v.findViewById(R.id.tvPassengers);

        if (tripDetails != null) {

            tvSourceAddress.setText(tripDetails.getCurrentAddress());
            tvDestinationAddress.setText(tripDetails.getDestinationAddress());
            tvMiles.setText(String.valueOf(tripDetails.getMiles()));
            tvPoints.setText(String.valueOf(tripDetails.getPoints()));
            tvPassengers.setText(String.valueOf(tripDetails.getNumOfPeople()));
        }
        bStart = (Button) v.findViewById(R.id.bStart);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected) {
                    requestLocationUpdate();
                }else{
                    Toast.makeText(getActivity(),"Not Connected Yet.",Toast.LENGTH_LONG).show();
                }

            }
        });


        return v;
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("Updates","Location: " + location.toString());
    }
}
