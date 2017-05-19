package com.example.bruhshua.carpool;

import android.location.Location;

/**
 * Created by bruhshua on 2/18/17.
 */

public class User {

    String number;
    String email;
    String firstName;
    String lastName;
    String school;
    Location userLocation;



    User(){}

    User(String n, String e, String f, String l, String s, Location u){
        number = n;
        email = e;
        firstName = f;
        lastName = l;
        school = s;
        userLocation = u;

    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
