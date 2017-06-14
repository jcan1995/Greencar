package com.example.bruhshua.carpool.Model;

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

    public TripDetails(){}

    public TripDetails(int numOfPeople, float miles, float points, String currentAddress, String destinationAddress) {
        this.numOfPeople = numOfPeople;
        this.miles = miles;
        this.points = points;
        this.currentAddress = currentAddress;
        this.destinationAddress = destinationAddress;

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
