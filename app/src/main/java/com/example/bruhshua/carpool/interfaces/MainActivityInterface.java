package com.example.bruhshua.carpool.interfaces;

import com.example.bruhshua.carpool.Model.TripDetails;
/**
 * Created by bruhshua on 11/5/17.
 */

public interface MainActivityInterface {

    interface View{

        void toInviteDialog(TripDetails tripDetails);
        void toTripCanceledView();

        //Following 3 views are the views that show the current trip.
        void toHostTripInProgressView(TripDetails tripDetails);
        void toPassengerInviteDialogView(TripDetails tripDetails);
        void toPassengerView(TripDetails tripDetails);

        //This is the first view where a user can invite passengers and set a location.(MainView)
        void toMainView(TripDetails tripDetails);
    }

    interface Presenter{

        void tripInProgressDeleted();
        void inviteAcquired(TripDetails tripDetails);
        void hostTripInProgress(TripDetails tripDetails);
        void passengerInviteNotAck(TripDetails tripDetails);
        void passengerInviteAck(TripDetails tripDetails);
        void noTripInProgressView(TripDetails tripDetails);
    }

}
