package com.example.bruhshua.carpool.ServiceRequests;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bruhshua on 5/30/17.
 */

//public class FetchRouteStepsFromService extends AsyncTask<Void,Void,StringBuilder> {
//
//    private LocalBroadcastManager manager;
//    private String currentAddress;
//    private String destinationAddress;
//
//
//        public FetchRouteStepsFromService(String currentAddress, String destinationAddress, Context context){
//            this.currentAddress = currentAddress;
//            this.destinationAddress = destinationAddress;
//            manager = LocalBroadcastManager.getInstance(context);
//
//        }
//
//    public static void longInfo(String str) {
//        if(str.length() > 4000) {
//            Log.d("PlanTrip", str.substring(0, 4000));
//            longInfo(str.substring(4000));
//        } else
//            Log.i("PlanTrip", str);
//    }
//
//
//    private List<LatLng> decodePoly(String encoded) {
//
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }
//
//        @Override
//        protected void onPostExecute(StringBuilder result) {
//            super.onPostExecute(result);
//            try{
//
//               // Log.d("PlanTrip",result.toString());
//                JSONObject jsonObj = new JSONObject(result.toString());
//                JSONArray routesJSONArray = jsonObj.getJSONArray("routes");
//                JSONObject beforeLegsJSONObject = routesJSONArray.getJSONObject(0);
//                JSONArray legsJSONArray = beforeLegsJSONObject.getJSONArray("legs");
//
//                JSONObject beforeStepsJSONObject = legsJSONArray.getJSONObject(0);
//                JSONArray stepsJSONArray = beforeStepsJSONObject.getJSONArray("steps");
//
//              //  longInfo(stepsJSONArray.toString());
//
//                List<LatLng> test = new ArrayList<>();
//                ArrayList<PolylineOptions> options = new ArrayList<>();
//
//                for(int i = 0; i < stepsJSONArray.length(); i++){
//                    //Todo: Instead of using each start and end lats and lngs
//                    //ArrayList<PolylineOptions> options = new ArrayList<>();
//                    options.clear();
//                    JSONObject object = stepsJSONArray.getJSONObject(i);
//                    JSONObject polyLineObject = object.getJSONObject("polyline");
//                    String encodedPoly = polyLineObject.getString("points");//Holds the code for the polyline (String)
//                    test = decodePoly(encodedPoly);
//                    for(int j = 0; j < test.size();j++){
//
//
//                        if(j != test.size() -1) {
//                            LatLng startLocation = test.get(j);
//                            LatLng nextLocation = test.get(j + 1);
//
//                            PolylineOptions options1 = new PolylineOptions().add(startLocation, nextLocation).width(5).color(Color.GREEN).geodesic(true);
//                            options.add(options1);
//                        }else{
//                            LatLng startLocation = test.get(j);
//                            LatLng nextLocation = test.get(j);
//
//                            PolylineOptions options1 = new PolylineOptions().add(startLocation, nextLocation).width(5).color(Color.GREEN).geodesic(true);
//                            options.add(options1);
//                        }
//
//                    }
//                    Log.d("PlanTrip","options size before:" + options.size());
//
//                    Bundle args = new Bundle();
//                    args.putParcelableArrayList("POLYLINES",options);
//
//                    Intent intent = new Intent("com.action.getstepslatlng");
//                    intent.putExtra("BUNDLE",args);
//                    manager.sendBroadcast(intent);
//                    //options.clear();
//                    Log.d("PlanTrip","options size after:" + options.size());
//
////                    if(options.size() == 500){
////                        Bundle args = new Bundle();
////                        args.putParcelableArrayList("POLYLINES",options);
////
////                        Intent intent = new Intent("com.action.getstepslatlng");
////                        intent.putExtra("BUNDLE",args);
////                        manager.sendBroadcast(intent);
////                        options.clear();
////                    }
////                    Bundle args = new Bundle();
////                    args.putParcelableArrayList("POLYLINES",options);
////
////                    Intent intent = new Intent("com.action.getstepslatlng");
////                    intent.putExtra("BUNDLE",args);
////                    manager.sendBroadcast(intent);
//                   // Log.d("PlanTrip","options size:" + options.size());
////                    JSONObject start_location = object.getJSONObject("start_location");
////                    JSONObject end_location = object.getJSONObject("end_location");
////
////                    String startLat = start_location.getString("lat");
////                    String startLng = start_location.getString("lng");
////
////                    String endLat = end_location.getString("lat");
////                    String endLng = end_location.getString("lng");
////
////                    Double startLatFinal = Double.valueOf(startLat);
////                    Double startLngFinal = Double.valueOf(startLng);
////
////                    Double endLatFinal = Double.valueOf(endLat);
////                    Double endLngFinal = Double.valueOf(endLng);
////
////                    LatLng startLocation = new LatLng(startLatFinal,startLngFinal);
////                    LatLng endLocation = new LatLng(endLatFinal,endLngFinal);
////
////                    PolylineOptions options1 = new PolylineOptions().add(startLocation,endLocation).width(5).color(Color.GREEN).geodesic(true);
////                    options.add(options1);
//                }
//
////                for(int i = 0; i < test.size();i++){
////                    Log.d("PlanTrip","lat:"+test.get(i).latitude);
////                    Log.d("PlanTrip","lng:"+test.get(i).longitude);
////
////                }
//
//                //Todo: Maybe broadcast data in for loop for each iteration. To relieve huge data.
////                Bundle args = new Bundle();
////                args.putParcelableArrayList("POLYLINES",options);
////
////                Intent intent = new Intent("com.action.getstepslatlng");
////                intent.putExtra("BUNDLE",args);
////                manager.sendBroadcast(intent);
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected StringBuilder doInBackground(Void... params) {
//            try{
//
//                StringBuilder jsonResults = new StringBuilder();
//                String googleMapUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
//                        "origin="+currentAddress+"&" +
//                        "destination="+destinationAddress+"&key=AIzaSyB0WPNPyjRxrwB7iyzVDcxwy4W2Gd-KmUA";
//
//                URL url = new URL(googleMapUrl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                InputStreamReader in = new InputStreamReader(conn.getInputStream());
//                int read;
//                char[] buff = new char[3000];
//
//                while((read = in.read(buff,0,3000)) != -1 ){
//                    jsonResults.append(buff,0,read);
//
//                }
//                return jsonResults;
//
//            }catch (Exception e){
//
//                Log.d("PlanTrip","doInBackgroud exception");
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//
//
//    }