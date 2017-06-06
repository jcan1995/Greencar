package com.example.bruhshua.carpool;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by bruhshua on 2/18/17.
 */

public class User implements Serializable{

    //THIS IS A TEST FROM BRANCH
    private String number;
    private String email;
    private String password;
    private String userName;
    private String profilePictureUrl;
    private String localProfilePictureUrl;

    public User(){}

    public User(String number, String email, String userName, String profilePictureUrl, String localProfilePictureUrl) {

        this.number = number;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.userName = userName;
        this.localProfilePictureUrl = localProfilePictureUrl;
    }

    public User(String number, String email,String userName) {

        this.number = number;
        this.email = email;

        this.userName = userName;
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
}
