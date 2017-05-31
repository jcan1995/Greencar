package com.example.bruhshua.carpool.ServiceRequests;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by bruhshua on 5/30/17.
 */

public class FetchRouteStepsFromService extends AsyncTask<Void,Void,StringBuilder> {

    private LocalBroadcastManager manager;
    private String currentAddress;
    private String destinationAddress;


        public FetchRouteStepsFromService(String currentAddress, String destinationAddress, Context context){
            this.currentAddress = currentAddress;
            this.destinationAddress = destinationAddress;
            manager = LocalBroadcastManager.getInstance(context);

        }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.d("PlanTrip", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("PlanTrip", str);
    }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);
            Log.d("PlanTrip","onPostExecute");
            try{

                longInfo(result.toString());

                Bundle args = new Bundle();
                Intent intent = new Intent("com.action.getstepslatlng");
                intent.putExtra("BUNDLE",args);
                manager.sendBroadcast(intent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            try{

                //HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();

          //      Todo: Pass in current and destination locations
//                String googleMapUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
//                        "origin=75+9th+Ave+New+York,+NY&" +
//                        "destination=MetLife+Stadium+1+MetLife+Stadium+Dr+East+Rutherford,+NJ+07073&key=AIzaSyB0WPNPyjRxrwB7iyzVDcxwy4W2Gd-KmUA";

                //currentAddress's spaces may not work.

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