package com.example.bruhshua.carpool.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Adapters.UsersListViewAdapter;
import com.example.bruhshua.carpool.Model.MapUpdatePOJO;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.Services.FetchAddressFromService;
import com.example.bruhshua.carpool.Services.FetchLocationFromService;
import com.example.bruhshua.carpool.Model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

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

//Todo: Switch add passengers view with details fragment.
public class PlanTripFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private TripDetails tripDetail;

    private static User authUser;

    private LocalBroadcastManager manager;

    private ProgressDialog dialog;
    private GoogleApiClient mGoogleApiClient;

    private Location mCurrentLocation;
    private Location mDestinationLocation;
    private Location  mValidationLocation;

    private LatLng mCurrentLatLng;
    private LatLng mDestinationLatLng;

    private String currentAddress;
    private String destinationAddress;

    private EditText etDesinationLocation;
    private ImageButton bSetTrip;
    private ImageView ivInvitePassenger;

    private ArrayList<User> passengers;
    private ListView listview;
    private Callback callback;
    private ArrayList<PolylineOptions> mPolyOptions = new ArrayList<>();

    public static PlanTripFragment newInstance(User user) {
        PlanTripFragment planTripFragment = new PlanTripFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER", user);
        planTripFragment.setArguments(args);
        return planTripFragment;
    }

    public static User getAuthUser() {
        return authUser;
    }

    public interface Callback {
        public void updateMap(MapUpdatePOJO mapUpdatePOJO, TripDetails tripDetails, User user);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        }
        Log.d("lifecycleCheck", "PlanTripFragment: onAttach called");

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("lifecycleCheck", "PlanTripFragment: onStart called");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycleCheck", "PlanTripFragment: onResume called");

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        Log.d("lifecycleCheck", "PlanTripFragment: onStop called");


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("lifecycleCheck", "PlanTripFragment: onPause called");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycleCheck", "PlanTripFragment: onCreate called");

        this.authUser = (User) getArguments().getSerializable("USER");
        this.authUser.setHost(true);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        initBroadcastReceiver();

    }

    private void initBroadcastReceiver() {


        manager = LocalBroadcastManager.getInstance(getContext());
        MyBroadCastReceiver receiver = new MyBroadCastReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction("com.action.getdestlatlng");
        filter.addAction("com.action.getdestaddress");
        filter.addAction("com.action.getstepslatlng");
        filter.addAction("com.action.getpool");

        manager.registerReceiver(receiver, filter);
    }


    public void updateUI() {

        //Todo: Construct MapUIUpdate POJO send through callback to TripMapFragment.
        if (mCurrentLatLng != null && mDestinationLatLng != null) {

            MapUpdatePOJO mapUpdatePOJO = new MapUpdatePOJO(mPolyOptions, mCurrentLatLng, mDestinationLatLng);

            if (callback != null) {
                callback.updateMap(mapUpdatePOJO, tripDetail, authUser);
            } else {
            }

        } else {
        }

        mPolyOptions.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.set_destination_add_passengers_layout, container, false);
        Log.d("lifecycleCheck", "PlanTripFragment: onCreateView called");

        dialog = new ProgressDialog(getActivity());
        passengers = new ArrayList<>();
        passengers.add(authUser);

        listview = (ListView) v.findViewById(R.id.lvPassengerPool);
        updatePassengersView();
        etDesinationLocation = (EditText) v.findViewById(R.id.etDestinationLocation);
        ivInvitePassenger = (ImageView) v.findViewById(R.id.ivAddPassenger);

        ivInvitePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passengers != null) {

                    Bundle args = new Bundle();
                    args.putSerializable("POOL", passengers);

                    Intent i = new Intent();
                    i.putExtra("BUNDLE", args);

                    AddPassengersDialogFragment addPassengersDialogFragment = AddPassengersDialogFragment.newInstance(i);
                    addPassengersDialogFragment.show(getActivity().getFragmentManager(), "");
                }

            }
        });

        bSetTrip = (ImageButton) v.findViewById(R.id.bSetTrip);
        bSetTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etDesinationLocation.getText().toString().equals("") && currentAddress != null) {
                    passengers.get(0).setHost(true);
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Please wait...");
                    dialog.show();

                    // validatePassengers();
                    destinationAddress = etDesinationLocation.getText().toString();
                    getDestinationAddress(etDesinationLocation.getText().toString());
                    getDirections(etDesinationLocation.getText().toString());// Just pass in the destination string since current address is initialized in onConnect interface method.

                } else {
                    Toast.makeText(getActivity(), "Please Enter an Address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

            FetchAddressFromService fetchLocationFromService = new FetchAddressFromService(getContext(),mCurrentLocation);
            fetchLocationFromService.execute();

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
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            //Todo: Maybe show dialog that tells users why we need their location.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        //mCurrentLocation.get
        if (mCurrentLocation != null) {


//            dialog = new ProgressDialog(getActivity());
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

                    dialog.dismiss();
                    break;

                case "com.action.getdestaddress":
                    currentAddress = bundle.getString("DEST");
                    dialog.dismiss();
                    break;

                case "com.action.getpool":

                    passengers = (ArrayList<User>) bundle.getSerializable("POOL");

                    //notifyPassengers();
                  //  validatePassengers();//First through texts
                    updatePassengersView();
                    break;

                case SMS_SENT:

                    break;

                case SMS_DELIVERED:
                    break;

                case "":
                    //Do something else, although shouldn't happen.
                    dialog.dismiss();
                    break;
            }
        }
    }

    //Upon inviting passengers, update all user's json with a new trip. Set a listener,
    // when fired show a dialogfragment to the user to either accept or decline the trip. If accepted, keep the data in json
    // else delete it.



    private void updatePassengersView() {

        if(passengers != null && passengers.size() != 0) {
            UsersListViewAdapter mAdapter = new UsersListViewAdapter(passengers, getContext());
            listview.setAdapter(mAdapter);
        }

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

                float distance;

                //Todo: Get Distance////////////////

                JSONObject jsonObj2 = new JSONObject(result.toString());
                JSONArray routesJSONArray2 = jsonObj2.getJSONArray("routes");
                JSONObject beforeLegsJSONObject2 = routesJSONArray2.getJSONObject(0);
                JSONArray legsJSONArray2 = beforeLegsJSONObject2.getJSONArray("legs");
                JSONObject legsObject = legsJSONArray2.getJSONObject(0);

                JSONObject distanceObject = legsObject.getJSONObject("distance");

                String milesString = distanceObject.getString("text");
                String milestrimmed = null;
                float miles = 0;
                if(milesString.contains("mi")){

                    milestrimmed = milesString.replaceAll(" mi","");

                }else if(milesString.contains("ft")){
                    //todo: convert from ft to miles
                    milestrimmed = milesString.replaceAll(" ft","");

                }

                if(milestrimmed != null) {
                    miles = Float.parseFloat(milestrimmed);

                }



                longInfo(result.toString());
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray routesJSONArray = jsonObj.getJSONArray("routes");
                JSONObject beforeLegsJSONObject = routesJSONArray.getJSONObject(0);
                JSONArray legsJSONArray = beforeLegsJSONObject.getJSONArray("legs");

                JSONObject beforeStepsJSONObject = legsJSONArray.getJSONObject(0);
                JSONArray stepsJSONArray = beforeStepsJSONObject.getJSONArray("steps");

                //longInfo(stepsJSONArray.toString());

                List<LatLng> test = new ArrayList<>();

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

                int numPeople = passengers.size(); //Passengers should also include the driver.
                float points = miles;
                tripDetail = new TripDetails(passengers,numPeople,miles,points,currentAddress, mCurrentLatLng.latitude,mCurrentLatLng.longitude,destinationAddress,mDestinationLatLng.latitude,mDestinationLatLng.longitude);
                updateUI();

                dialog.dismiss();

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

                e.printStackTrace();
            }
            return null;
        }



    }
}
