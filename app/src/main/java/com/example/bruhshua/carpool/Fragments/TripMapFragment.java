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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bruhshua.carpool.Activities.MainActivity;
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

public class TripMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,TripDetailsFragment.Callback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mSupportMapFragment;
    private User authUser;
    private LocationManager manager;
    private Location location;
    private Criteria mCriteria;
    private String bestProvider;

    private TripDetails tripInProgress;

    public static TripMapFragment newInstance(TripDetails tripInProgress,User user) {
        TripMapFragment tripMapFragment = new TripMapFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER", user);
        args.putSerializable("TRIPINPROGRESS",tripInProgress);
        tripMapFragment.setArguments(args);
        return tripMapFragment;
    }

    /*
        Separate Map Update for passengers who currently will not have PolyLines. To much data to transfer from the host to all his passengers.
        Maybe, in the future, all the passengers can run their own Service request to get their polylines instead
     */

    public void updateMapForPassengers(TripDetails tripDetails, User user){
        Log.d("mydebugger","updateMapForPassengers fired" );

        LatLng mDestinationLatLng = new LatLng(tripDetails.getmDestinationLat(),tripDetails.getmDestinationLng());
        LatLng mCurrentLatLng = new LatLng(tripDetails.getmCurrentLat(),tripDetails.getmCurrentLng());

        MarkerOptions destinationMarker = new MarkerOptions();
        destinationMarker.position(mDestinationLatLng);
        map.addMarker(destinationMarker);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mCurrentLatLng);
        builder.include(mDestinationLatLng);

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 256);

        map.moveCamera(cu);


        TripDetailsFragment tripDetailsFragment = TripDetailsFragment.newInstance(tripDetails, user);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.plan_trip_fragment_container, tripDetailsFragment)
                .commit();


    }

//    public void updateMap(MapUpdatePOJO mapUpdatePOJO, TripDetails tripDetails, User user) {
//        Log.d("mydebugger","TripMapFragment: updateMap called" );
//
//        LatLng mDestinationLatLng = new LatLng(tripDetails.getmDestinationLat(),tripDetails.getmDestinationLng());
//        LatLng mCurrentLatLng = new LatLng(tripDetails.getmCurrentLat(),tripDetails.getmCurrentLng());
//
//        if(mCurrentLatLng != null && mDestinationLatLng != null){
////            for (int i = 0; i < tripDetails.getmPolyOptions().size(); i++) {
////                map.addPolyline(tripDetails.getmPolyOptions().get(i));
////            }
//            MarkerOptions destinationMarker = new MarkerOptions();
//            destinationMarker.position(mDestinationLatLng);
//            map.addMarker(destinationMarker);
//
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(mCurrentLatLng);
//            builder.include(mDestinationLatLng);
//
//            LatLngBounds bounds = builder.build();
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 32);//32
//            map.moveCamera(cu);
//
//            TripDetailsFragment tripDetailsFragment = TripDetailsFragment.newInstance(tripDetails, user);
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.plan_trip_fragment_container, tripDetailsFragment)
//                    .commit();
//
//
//        } else {
//        }
//
////        if (mapUpdatePOJO.getmCurrentLatLng() != null && mapUpdatePOJO.getmDestinationLatLng() != null) {
////
////            for (int i = 0; i < mapUpdatePOJO.getmPolyOptions().size(); i++) {
////                map.addPolyline(mapUpdatePOJO.getmPolyOptions().get(i));
////            }
////
////            MarkerOptions destinationMarker = new MarkerOptions();
////            destinationMarker.position(mapUpdatePOJO.getmDestinationLatLng());
////            map.addMarker(destinationMarker);
////
////            LatLngBounds.Builder builder = new LatLngBounds.Builder();
////            builder.include(mapUpdatePOJO.getmCurrentLatLng());
////            builder.include(mapUpdatePOJO.getmDestinationLatLng());
////
////            LatLngBounds bounds = builder.build();
////            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 32);//32
////            map.moveCamera(cu);
////
////
////            TripDetailsFragment tripDetailsFragment = TripDetailsFragment.newInstance(tripDetails, user);
////            getActivity().getSupportFragmentManager()
////                    .beginTransaction()
////                    .replace(R.id.plan_trip_fragment_container, tripDetailsFragment)
////                    .commit();
////
////
////        } else {
////        }
//
//    }

    public void showSummary(TripDetails tripDetails, User user) {

        TripSummaryFragment tripSummaryFragment = TripSummaryFragment.newInstance(tripDetails, user);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.plan_trip_fragment_container, tripSummaryFragment)
                .commit();


    }

//    @Override
//    public void updateMap(CameraUpdate cu) {
//        map.moveCamera(cu);
//    }

    public void Reset(User user) {
        map.clear();
        PlanTripFragment planTripFragment = PlanTripFragment.newInstance(user);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.plan_trip_fragment_container, planTripFragment)
                .commit();


    }

    public void cancelTrip(User user) {
        map.clear();
        PlanTripFragment planTripFragment = PlanTripFragment.newInstance(user);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.plan_trip_fragment_container, planTripFragment)
                .commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("lifecycleCheck", "TripMapFragment: onAttach called");

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("lifecycleCheck", "TripMapFragment: onStart called");


    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        Log.d("lifecycleCheck", "TripMapFragment: onStop called");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycleCheck", "TripMapFragment: onResume called");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("lifecycleCheck", "TripMapFragment: onPause called");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycleCheck", "TripMapFragment: onCreate called");

        this.authUser = (User) getArguments().getSerializable("USER");
        this.tripInProgress = (TripDetails) getArguments().getSerializable("TRIPINPROGRESS");

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

        View v = inflater.inflate(R.layout.trip_map_fragment, container, false);
        Log.d("lifecycleCheck", "TripMapFragment: onCreateView called");

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getActivity(), "Back pressed", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mCriteria = new Criteria();
        bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = manager.getLastKnownLocation(bestProvider);
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

        }

         if(tripInProgress == null) {
           PlanTripFragment planTripFragment = PlanTripFragment.newInstance(authUser);
           getFragmentManager().beginTransaction()
                 .add(R.id.plan_trip_fragment_container, planTripFragment)
                 .commit();
         }

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
        Log.d("lifecycleCheck", "TripMapFragment: onMapReady called");

        map = googleMap;
        //map.getUiSettings().setScrollGesturesEnabled(false);

        if(tripInProgress != null){
            updateMapForPassengers(tripInProgress,authUser);
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("lifecycleCheck", "TripMapFragment: onConnected called");

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //This method doesn't provide specific data that we need to use. Such as LatLng
            //Maybe we can use this method just for its UI.
            map.setMyLocationEnabled(true);
            manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mCriteria = new Criteria();
            bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));
            location = manager.getLastKnownLocation(bestProvider);
           // map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

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

    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            map.setMyLocationEnabled(true);
            manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mCriteria = new Criteria();
            bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));
            location = manager.getLastKnownLocation(bestProvider);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            return;
        }
//        location = manager.getLastKnownLocation(bestProvider);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
    }
}
