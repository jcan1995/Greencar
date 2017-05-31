package com.example.bruhshua.carpool.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruhshua.carpool.Manifest;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.ServiceRequests.FetchAddressFromService;
import com.example.bruhshua.carpool.ServiceRequests.FetchLocationFromService;
import com.example.bruhshua.carpool.ServiceRequests.FetchRouteStepsFromService;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruhshua on 5/21/17.
 */


    /*
         Directions APIKEY = AIzaSyB0WPNPyjRxrwB7iyzVDcxwy4W2Gd-KmUA
     */

//Todo: PRIORITY, obtain users current address from Location object.

public class PlanTripFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private LocalBroadcastManager manager;

    private ProgressDialog dialog;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    private Location mCurrentLocation;
    private Location mDestinationLocation;

    private LatLng mCurrentLatLng;
    private String currentAddress;
    private String destinationAddress;
    private LatLng mDestinationLatLng;

    private SupportMapFragment mSupportMapFragment;
    private EditText etNumberOfPassengers;
    private EditText etDesinationLocation;
    private Button bSetTrip;

    //Todo: http request to get step by step latlngs to create route between points.
    public static PlanTripFragment newInstance() {
        PlanTripFragment planTripFragment = new PlanTripFragment();
        Bundle args = new Bundle();
        // args.put("DATA",DATA);
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
        initBroadcastReceiver();

    }

    private void initBroadcastReceiver(){
        manager = LocalBroadcastManager.getInstance(getContext());
        MyBroadCastReceiver receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.action.getdestlatlng");
        filter.addAction("com.action.getdestaddress");
        filter.addAction("com.action.getstepslatlng");
        manager.registerReceiver(receiver,filter);
    }

    public void updateUI(){

        //Todo: Include step by step polylines to map.
        if(mCurrentLatLng != null && mDestinationLatLng != null) {

            PolylineOptions options = new PolylineOptions().add(mCurrentLatLng, mDestinationLatLng)
                    .width(5).color(Color.GREEN);

            MarkerOptions destinationMarker = new MarkerOptions();
            destinationMarker.position(mDestinationLatLng);
            map.addMarker(destinationMarker);

            Polyline line = map.addPolyline(options);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(mCurrentLatLng);
            builder.include(mDestinationLatLng);
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,32);//32
            map.animateCamera(cu);


        }else{
            Log.d("PlanTrip","LatLngs are null bitch");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.plan_trip_fragment, container, false);

        etDesinationLocation = (EditText) v.findViewById(R.id.etDestionationLocation);
        etNumberOfPassengers = (EditText) v.findViewById(R.id.etNumPassengers);

        bSetTrip = (Button) v.findViewById(R.id.bSetTrip);
        bSetTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etDesinationLocation.getText().toString().equals("") && currentAddress != null){
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Please wait...");
                    dialog.show();

                    getDirections(etDesinationLocation.getText().toString());// Just pass in the destination string since current address is initialized in onConnect interface method.

                }else{
                    Toast.makeText(getActivity(), "Please Enter an Address.", Toast.LENGTH_SHORT).show();
                }
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

    private void getCurrentAddress() {

        try{

            FetchAddressFromService fectchLocationFromService = new FetchAddressFromService(getContext(),mCurrentLocation);
            fectchLocationFromService.execute();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getDirections(String destinationAddress) {
        //Todo: Use entered address and current address to get direction path JSON

        try{
            Log.d("PlanTrip","current:"+currentAddress);
            Log.d("PlanTrip","destination:"+destinationAddress);

            FetchRouteStepsFromService fetchRouteStepsFromService = new FetchRouteStepsFromService(currentAddress,destinationAddress.replaceAll("\\s+",""),getContext());
            fetchRouteStepsFromService.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("PlanTrip", "onMapReady fired");
        map = googleMap;

       // checkPermissions();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("PlanTrip", "onConnected fired");

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("PlanTrip", "ACCESS_FINE_LOCATION INCLUDED");
            //This method doesn't provide specific data that we need to use. Such as LatLng
            //Maybe we can use this method just for its UI.

            map.setMyLocationEnabled(true);
        } else {
            Log.d("PlanTrip", "ACCESS_FINE_LOCATION NOT INCLUDED");
            //Todo: Maybe show dialog that tells users why we need their location.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mCurrentLocation != null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.show();

            mCurrentLatLng = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            //Todo: Call getCurrentAddress here.
            getCurrentAddress();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

//
//    public void getDestinationLatLngFromAddress(String place){
//        try{
//            Geocoder placeGeocoder = new Geocoder(getContext());
//            List<Address> address;
//            address = placeGeocoder.getFromLocationName(place,5);
//            if(address == null){
//                //Do something
//            }else{
//                Address location = address.get(0);
//                Log.d("PlanTrip","(get..FromAddress)Lat: "+location.getLatitude());
//                Log.d("PlanTrip","(get..FromAddress)Long: "+location.getLongitude());
//
////                FetchLocationFromService fectchLocationFromService = new FetchLocationFromService(place.replaceAll("\\s+",""),getContext());
////                fectchLocationFromService.execute();
//
////                FetchRouteStepsFromService fetchRouteStepsFromService = new FetchRouteStepsFromService(place.replaceAll("\\s+",""));//Todo: pass in current and destination addressses, not LatLngs
////                fetchRouteStepsFromService.execute();
//            }
//
//        }catch (Exception e){
//            //This method is used when above method returns null...
//            e.printStackTrace();
//        }
//    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("PlanTrip","intent:" + intent.getAction());
            Bundle bundle = intent.getParcelableExtra("BUNDLE");;
            switch (intent.getAction()){
                case "com.action.getdestlatlng":

                    //bundle = intent.getParcelableExtra("BUNDLE");
                   // mDestinationLatLng = bundle.getParcelable("DEST");
                    // Log.d("PlanTrip","Coordinates:"+mDestinationLatLng.toString());
                  //  updateUI();

                    break;
                case "com.action.getstepslatlng":
                    //Do something
                    dialog.dismiss();
                    break;

                case "com.action.getdestaddress":

                    currentAddress = bundle.getString("DEST");
                    Log.d("PlanTrip","currentAddress:" + currentAddress);
                    dialog.dismiss();
                    break;

                case "":
                    //Do something else, although shouldn't happen.
                    dialog.dismiss();
                    break;
            }
        }
    }
}
