package com.example.bruhshua.carpool.Model;

import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bruhshua on 6/12/17.
 */

public class TripDetails implements Serializable {

    private int numOfPeople;
    private float miles;
    private double points;
    private String currentAddress;
    private String destinationAddress;
    private String host;//By user's emails

    private double mCurrentLat;
    private double mCurrentLng;

    private double mDestinationLat;
    private double mDestinationLng;

    private ArrayList<User> passengers;
    private ArrayList<PolylineOptions> mPolyOptions;

    private boolean invitationSent;
    private boolean invitationReceived;
    private boolean isInProgress;

    public TripDetails(){}

    public TripDetails(ArrayList<User> passengers,int numOfPeople, float miles, float points, String currentAddress, double currentLat, double currentLng, String destinationAddress, double destinationLat, double destinationLng) {
        this.passengers = passengers;
        this.numOfPeople = numOfPeople;
        this.miles = miles;
        this.points = points;
        this.currentAddress = currentAddress;
        this.destinationAddress = destinationAddress;
        this.mCurrentLat = currentLat;
        this.mCurrentLng = currentLng;
        this.mDestinationLat = destinationLat;
        this.mDestinationLng = destinationLng;
        this.invitationSent = false;
        this.invitationReceived = false;
        this.isInProgress = false;


    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public boolean isInvitationReceived() {
        return invitationReceived;
    }

    public void setInvitationReceived(boolean invitationReceived) {
        this.invitationReceived = invitationReceived;
    }

    public boolean isInvitationSent() {
        return invitationSent;
    }

    public void setInvitationSent(boolean invitationSent) {
        this.invitationSent = invitationSent;
    }

    public ArrayList<PolylineOptions> getmPolyOptions() {
        return mPolyOptions;
    }

    public void setmPolyOptions(ArrayList<PolylineOptions> mPolyOptions) {
        this.mPolyOptions = mPolyOptions;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ArrayList<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<User> passengers) {
        this.passengers = passengers;
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

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
