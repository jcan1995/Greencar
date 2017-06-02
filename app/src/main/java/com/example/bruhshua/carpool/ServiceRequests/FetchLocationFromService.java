package com.example.bruhshua.carpool.ServiceRequests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bruhshua on 5/24/17.
 */

public class FetchLocationFromService extends AsyncTask<Void,Void,StringBuilder> {

    private String destinationAddress;
    private LocalBroadcastManager manager ;

    public FetchLocationFromService(String destinationAddress, Context context) {
        super();
        this.destinationAddress = destinationAddress;
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

            String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="
                    + this.destinationAddress + "&sensor=false";

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
            JSONObject jsonObj = new JSONObject(result.toString());
            JSONArray resultJsonArray = jsonObj.getJSONArray("results");

            JSONObject beforeGeometryObj = resultJsonArray.getJSONObject(0);
            JSONObject geometryObj = beforeGeometryObj.getJSONObject("geometry");
            JSONObject locationObj = geometryObj.getJSONObject("location");

            String lat = locationObj.getString("lat");
            String lon = locationObj.getString("lng");
            Double latitude = Double.valueOf(lat);
            Double longitude = Double.valueOf(lon);

            LatLng latLng = new LatLng(latitude,longitude);

            Bundle args = new Bundle();
            args.putParcelable("DEST",latLng);
            //Use another intent for route data extraction

            Intent intent = new Intent("com.action.getdestlatlng");
            intent.putExtra("BUNDLE",args);
            manager.sendBroadcast(intent);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}