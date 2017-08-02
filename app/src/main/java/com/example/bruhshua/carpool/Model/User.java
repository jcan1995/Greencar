package com.example.bruhshua.carpool.Model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by bruhshua on 2/18/17.
 */

public class User implements Serializable{

    private String number;
    private String email;
    private String password;
    private String userName;
    private String profilePictureUrl;
    private double points;
    private String localProfilePictureUrl;
    private HashMap<String,TripDetails> trips;
    private String downloadUrl;
    private String key;

    public User(){}

    public User(String number, String email, String userName, String profilePictureUrl, String localProfilePictureUrl, HashMap<String,TripDetails> tripDetails) {

        this.number = number;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.userName = userName;
        this.localProfilePictureUrl = localProfilePictureUrl;
        this.trips = tripDetails;
    }

    public User(String email, String displayName, String downloadUrl) {
        this.email = email;
        this.userName = displayName;
        this.downloadUrl = downloadUrl;
    }

    public User(String email, String displayName, String downloadUrl, String phoneNumber, String key) {
        this.email = email;
        this.userName = displayName;
        this.downloadUrl = downloadUrl;
        this.number = phoneNumber;
        this.key = key;

    }
    public User(String email, String displayName) {
        this.email = email;
        this.userName = displayName;
    }


    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getLocalProfilePictureUrl() {
        return localProfilePictureUrl;
    }

    public void setLocalProfilePictureUrl(String localProfilePictureUrl) {
        this.localProfilePictureUrl = localProfilePictureUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
