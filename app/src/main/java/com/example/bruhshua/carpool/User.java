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
//    private String firstName;
//    private String lastName;
    private String userName;

    public User(){}

    public User(String number, String email, String password,String userName) {

        this.number = number;
        this.email = email;
        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
        this.userName = userName;
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
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
}
