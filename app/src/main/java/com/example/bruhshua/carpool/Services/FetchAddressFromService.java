package com.example.bruhshua.carpool.Services;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bruhshua on 5/30/17.
 */

public class FetchAddressFromService extends AsyncTask<Void,Void,StringBuilder> {

    private LocalBroadcastManager manager;
    private Location currentLocation;

    public FetchAddressFromService(Context context, Location currentLocation) {
        super();
        this.currentLocation = currentLocation;
        manager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.cancel(true);
    }

    @Override
    protected StringBuilder doInBackground(Void... params) {

        //Todo: Make http requests to get JSON data for steps and JSON data for LatLng from address strings
        try{

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();

            String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + currentLocation.getLatitude() + ","+ currentLocation.getLongitude() + "&sensor=true";

            URL url = new URL(googleMapUrl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while((read = in.read(buff)) != -1){
                jsonResults.append(buff,0,read);
            }
            String a = "";
            return jsonResults;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(StringBuilder result) {
        super.onPostExecute(result);
        try{

            //Todo: extract current address from JSON data, send string through intent.

            JSONObject jsonObject = new JSONObject(result.toString());
            JSONArray resultJSONArray = jsonObject.getJSONArray("results");
            JSONObject beforeAddress = resultJSONArray.getJSONObject(0);

            String address = beforeAddress.getString("formatted_address");
            //Log.d("PlanTrip","onPost, formatted_adress: " + address);//Display current address, should be up2 yous address.

            Bundle args = new Bundle();
            args.putString("DEST",address);

            Intent intent = new Intent("com.action.getdestaddress");
            intent.putExtra("BUNDLE",args);
            manager.sendBroadcast(intent);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}