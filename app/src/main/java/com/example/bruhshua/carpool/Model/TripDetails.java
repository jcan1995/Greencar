package com.example.bruhshua.carpool.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by bruhshua on 6/12/17.
 */

public class TripDetails implements Serializable {

    private int numOfPeople;
    private float miles;
    private float points;
    private String currentAddress;
    private String destinationAddress;


    private double mCurrentLat;
    private double mCurrentLng;

    private double mDestinationLat;
    private double mDestinationLng;

    public TripDetails(){}

    public TripDetails(int numOfPeople, float miles, float points, String currentAddress, double currentLat, double currentLng, String destinationAddress, double destinationLat, double destinationLng) {
        this.numOfPeople = numOfPeople;
        this.miles = miles;
        this.points = points;
        this.currentAddress = currentAddress;
        this.destinationAddress = destinationAddress;

        this.mCurrentLat = currentLat;
        this.mCurrentLng = currentLng;
        this.mDestinationLat = destinationLat;
        this.mDestinationLng = destinationLng;

    }

    public double getmCurrentLat() {
        return mCurrentLat;
    }

    public void setmCurrentLat(double mCurrentLat) {
        this.mCurrentLat = mCurrentLat;
    }

    public double getmCurrentLng() {
        return mCurrentLng;
    }

    public void setmCurrentLng(double mCurrentLng) {
        this.mCurrentLng = mCurrentLng;
    }

    public double getmDestinationLat() {
        return mDestinationLat;
    }

    public void setmDestinationLat(double mDestinationLat) {
        this.mDestinationLat = mDestinationLat;
    }

    public double getmDestinationLng() {
        return mDestinationLng;
    }

    public void setmDestinationLng(double mDestinationLng) {
        this.mDestinationLng = mDestinationLng;
    }



    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles) {
        this.miles = miles;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }
}
