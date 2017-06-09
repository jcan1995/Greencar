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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Adapters.UsersListViewAdapter;
import com.example.bruhshua.carpool.Manifest;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.ServiceRequests.FetchAddressFromService;
import com.example.bruhshua.carpool.ServiceRequests.FetchLocationFromService;
import com.example.bruhshua.carpool.User;
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

    private EditText etDesinationLocation;
    private Button bSetTrip;
    private ImageView ivInvitePassenger;

    private ArrayList<User> passengers;
    private ListView listview;

    private ArrayList<PolylineOptions> mPolyOptions = new ArrayList<>();
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
        filter.addAction("com.action.getpool");

        manager.registerReceiver(receiver,filter);
    }

    public void updateUI(){

        //Todo: Include step by step polylines to map.
       if(mCurrentLatLng != null && mDestinationLatLng != null) {
       // if(mPolyOptions != null) {
            Log.d("PlanTrip","Inside update uI");
           Log.d("PlanTrip","PolyOptions size: " + mPolyOptions.size());

           for(int i = 0; i < mPolyOptions.size();i++){
               map.addPolyline(mPolyOptions.get(i));
           }
            MarkerOptions destinationMarker = new MarkerOptions();
            destinationMarker.position(mDestinationLatLng);
            map.addMarker(destinationMarker);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(mCurrentLatLng);
                builder.include(mDestinationLatLng);

            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,32);//32
            map.animateCamera(cu);


        }else{
            Log.d("PlanTrip","LatLngs are null bitch");
        }

        mPolyOptions.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.plan_trip_fragment, container, false);
        passengers = new ArrayList<>();

        listview = (ListView) v.findViewById(R.id.lvPassengerPool);

        etDesinationLocation = (EditText) v.findViewById(R.id.etDestionationLocation);
        ivInvitePassenger = (ImageView) v.findViewById(R.id.ivAddPassenger);
        ivInvitePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(passengers != null) {
                    Bundle args = new Bundle();
                    args.putSerializable("POOL", passengers);

                    Intent i = new Intent();
                    i.putExtra("BUNDLE", args);

                    AddPassengersDialogFragment addPassengersDialogFragment = AddPassengersDialogFragment.newInstance(i);
                    addPassengersDialogFragment.show(getActivity().getFragmentManager(), "");
                }

            }
        });

        bSetTrip = (Button) v.findViewById(R.id.bSetTrip);
        bSetTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etDesinationLocation.getText().toString().equals("") && currentAddress != null){
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Please wait...");
                    dialog.show();

                    getDestinationAddress(etDesinationLocation.getText().toString());
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

    private void getDestinationAddress(String destinationAddress){
        try{
            FetchLocationFromService fetchDestinationLatLng = new FetchLocationFromService(destinationAddress,getContext());
            fetchDestinationLatLng.execute();


        }catch (Exception e){
            e.printStackTrace();
        }
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
            FetchRouteStepsFromService fetchRouteStepsFromService = new FetchRouteStepsFromService(currentAddress,destinationAddress.replaceAll("\\s+",""),getContext());
            fetchRouteStepsFromService.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

       // checkPermissions();
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
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.show();

            mCurrentLatLng = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            getCurrentAddress();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    public void checkPermissions() {
//        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            Log.d("onCreate", "ACCESS_FINE_LOCATION INCLUDED");
//            //This method doesn't provide specific data that we need to use. Such as LatLng
//            //Maybe we can use this method just for its UI.
//            //  map.setMyLocationEnabled(true);
//        } else {
//            Log.d("onCreate", "ACCESS_FINE_LOCATION NOT INCLUDED");
//            //Callback onRequestPermissionsResult is called in hosting activity!
//            //Todo: Maybe show dialog that tells users why we need their location.
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getParcelableExtra("BUNDLE");
            switch (intent.getAction()){
                case "com.action.getdestlatlng":

                    bundle = intent.getParcelableExtra("BUNDLE");
                    mDestinationLatLng = bundle.getParcelable("DEST");
                    break;
                case "com.action.getstepslatlng":
                    //Do something
                   // mPolyOptions = bundle.getParcelableArrayList("POLYLINES");
                  //  Log.d("PlanTrip","polyoptionssize:" + mPolyOptions.size());
                   // updateUI();
                    dialog.dismiss();
                    break;

                case "com.action.getdestaddress":
                    currentAddress = bundle.getString("DEST");
                    dialog.dismiss();
                    break;

                //Todo: Make another case where dialogFragment broadcasts a User object.
                case "com.action.getpool":
                   // passengers.add((User) bundle.getSerializable("USER"));

                    passengers = (ArrayList<User>) bundle.getSerializable("POOL");
                    updatePassengersView();
                    break;

                case "":
                    //Do something else, although shouldn't happen.
                    dialog.dismiss();
                    break;
            }
        }
    }

    private void updatePassengersView() {

        UsersListViewAdapter mAdapter = new UsersListViewAdapter(passengers,getContext());
        listview.setAdapter(mAdapter);


    }

    public class FetchRouteStepsFromService extends AsyncTask<Void,Void,StringBuilder> {

        private LocalBroadcastManager manager;
        private String currentAddress;
        private String destinationAddress;


        public FetchRouteStepsFromService(String currentAddress, String destinationAddress, Context context){
            this.currentAddress = currentAddress;
            this.destinationAddress = destinationAddress;
            manager = LocalBroadcastManager.getInstance(context);

        }

        public void longInfo(String str) {
            if(str.length() > 4000) {
                Log.d("PlanTrip", str.substring(0, 4000));
                longInfo(str.substring(4000));
            } else
                Log.i("PlanTrip", str);
        }


        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);
            try{

                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray routesJSONArray = jsonObj.getJSONArray("routes");
                JSONObject beforeLegsJSONObject = routesJSONArray.getJSONObject(0);
                JSONArray legsJSONArray = beforeLegsJSONObject.getJSONArray("legs");

                JSONObject beforeStepsJSONObject = legsJSONArray.getJSONObject(0);
                JSONArray stepsJSONArray = beforeStepsJSONObject.getJSONArray("steps");

                //  longInfo(stepsJSONArray.toString());

                List<LatLng> test = new ArrayList<>();
                map.clear();
                for(int i = 0; i < stepsJSONArray.length(); i++){
                    JSONObject object = stepsJSONArray.getJSONObject(i);
                    JSONObject polyLineObject = object.getJSONObject("polyline");
                    String encodedPoly = polyLineObject.getString("points");//Holds the code for the polyline (String)
                    test = decodePoly(encodedPoly);
                    //Todo: Maybe create a separate asynctask to add latlngs on separate thread?
                    PolylineOptions options1;
                    for(int j = 0; j < test.size();j++){

                        if(j != test.size() -1) {

                            options1 = new PolylineOptions().add(test.get(j), test.get(j + 1)).width(5).color(Color.GREEN).geodesic(true);
                        }else{
                            options1 = new PolylineOptions().add(test.get(j), test.get(j)).width(5).color(Color.GREEN).geodesic(true);
                        }
                        mPolyOptions.add(options1);
                    }
                }

                updateUI();
                dialog.dismiss();

                //Todo: Maybe broadcast data in for loop for each iteration. To relieve huge data.
//                Bundle args = new Bundle();
//                args.putParcelableArrayList("POLYLINES",options);
//
//                Intent intent = new Intent("com.action.getstepslatlng");
//                intent.putExtra("BUNDLE",args);
//                manager.sendBroadcast(intent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            try{

                StringBuilder jsonResults = new StringBuilder();
                String googleMapUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin="+currentAddress+"&" +
                        "destination="+destinationAddress+"&key=AIzaSyB0WPNPyjRxrwB7iyzVDcxwy4W2Gd-KmUA";

                URL url = new URL(googleMapUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                int read;
                char[] buff = new char[3000];

                while((read = in.read(buff,0,3000)) != -1 ){
                    jsonResults.append(buff,0,read);

                }
                return jsonResults;

            }catch (Exception e){

                Log.d("PlanTrip","doInBackgroud exception");
                e.printStackTrace();
            }
            return null;
        }



    }
}
