package com.example.bruhshua.carpool.Presenters;

import com.example.bruhshua.carpool.Model.FirebaseMainActivity;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.interfaces.MainActivityInterface;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by bruhshua on 11/5/17.
 */

public class MainActivityPresenter implements MainActivityInterface.Presenter {

    private FirebaseMainActivity firebaseMainActivity;
    private MainActivityInterface.View mView;

    public MainActivityPresenter(MainActivityInterface.View view){
        mView = view;
        firebaseMainActivity = new FirebaseMainActivity(this);

    }

    public FirebaseAuth getAuthentication(){
        return firebaseMainActivity.getFirebaseAuth();
    }

    public void logout(FirebaseAuth auth){
        firebaseMainActivity.logout(auth);
    }

    public void setInviteListener(User user){
        firebaseMainActivity.setListenerForInvitation(user);
    }

    public void getTripInProgressIfAvailable(User user){
        firebaseMainActivity.getTripInProgressIfAvailable(user);
    }

    public void deleteTripInProgress(TripDetails tripDetails){
        firebaseMainActivity.deleteTripInProgress(tripDetails);
    }


    @Override
    public void inviteAcquired(TripDetails tripDetails) {
        mView.toInviteDialog(tripDetails);
    }

    @Override
    public void tripInProgressDeleted() {
        mView.toTripCanceledView();
    }

    @Override
    public void hostTripInProgress(TripDetails tripDetails) {
        mView.toHostTripInProgressView(tripDetails);
    }

    @Override
    public void passengerInviteNotAck(TripDetails tripDetails) {
        mView.toPassengerInviteDialogView(tripDetails);
    }

    @Override
    public void passengerInviteAck(TripDetails tripDetails) {
        mView.toPassengerView(tripDetails);
    }

    @Override
    public void noTripInProgressView(TripDetails tripDetails) {
        mView.toMainView(tripDetails);
    }


}
