package com.example.bruhshua.carpool.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import android.view.WindowManager;

import com.example.bruhshua.carpool.Model.MapUpdatePOJO;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by bruhshua on 7/24/17.
 */

public class TripMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mSupportMapFragment;
    private User authUser;
    private LocationManager manager;
    private Location location;
    private Criteria mCriteria;
    private String bestProvider;

    public static TripMapFragment newInstance(User user) {
        TripMapFragment tripMapFragment = new TripMapFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER", user);
        tripMapFragment.setArguments(args);
        return tripMapFragment;
    }

    public void updateMap(MapUpdatePOJO mapUpdatePOJO, TripDetails tripDetails, User user) {
        Log.d("TripMapFragment", "inside updateMap");

        if (mapUpdatePOJO.getmCurrentLatLng() != null && mapUpdatePOJO.getmDestinationLatLng() != null) {
            Log.d("TripMapFragment", "inside condition");

            for (int i = 0; i < mapUpdatePOJO.getmPolyOptions().size(); i++) {
                map.addPolyline(mapUpdatePOJO.getmPolyOptions().get(i));
            }

            MarkerOptions destinationMarker = new MarkerOptions();
            destinationMarker.position(mapUpdatePOJO.getmDestinationLatLng());
            map.addMarker(destinationMarker);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(mapUpdatePOJO.getmCurrentLatLng());
            builder.include(mapUpdatePOJO.getmDestinationLatLng());

            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 32);//32
            map.moveCamera(cu);

//            map.getUiSettings().setScrollGesturesEnabled(false);

            TripDetailsFragment tripDetailsFragment = TripDetailsFragment.newInstance(tripDetails, user);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.plan_trip_fragment_container, tripDetailsFragment)
                    .commit();


        } else {
            Log.d("TripMapFragment", "latlngs are null");
        }

    }

    public void showSummary(TripDetails tripDetails, User user) {

        TripSummaryFragment tripSummaryFragment = TripSummaryFragment.newInstance(tripDetails, user);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.plan_trip_fragment_container, tripSummaryFragment)
                .commit();


    }

    public void Reset(User user) {
        map.clear();
        PlanTripFragment planTripFragment = PlanTripFragment.newInstance(user);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.plan_trip_fragment_container, planTripFragment)
                .commit();


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

        this.authUser = (User) getArguments().getSerializable("USER");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.trip_map_fragment,container,false);
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mCriteria = new Criteria();
        bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));
        location = manager.getLastKnownLocation(bestProvider);
        Log.d("PlanTrip","location" + location.toString());
        PlanTripFragment planTripFragment = PlanTripFragment.newInstance(authUser);
        getFragmentManager().beginTransaction()
                .add(R.id.plan_trip_fragment_container,planTripFragment)
                .commit();


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

        Log.d("TripMapFragment","onMapReady called");
        map = googleMap;
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

       // map.moveCamera();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //This method doesn't provide specific data that we need to use. Such as LatLng
            //Maybe we can use this method just for its UI.

            map.setMyLocationEnabled(true);
            Log.d("PlanTrip","setMyLocationEnabled");
        } else {
            //Todo: Maybe show dialog that tells users why we need their location.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
