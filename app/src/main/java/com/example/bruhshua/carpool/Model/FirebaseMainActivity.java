package com.example.bruhshua.carpool.Model;

import android.util.Log;

import com.example.bruhshua.carpool.Fragments.TripMapFragment;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.interfaces.MainActivityInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 11/5/17.
 */

public class FirebaseMainActivity {

  private TripDetails tripInProgress;

  private FirebaseDatabase database;
  private MainActivityInterface.Presenter mPresenter;
  private DatabaseReference users_ref;
  private ValueEventListener valueEventListener;

    public FirebaseMainActivity(MainActivityInterface.Presenter presenter){
        mPresenter = presenter;
        database = FirebaseDatabase.getInstance();

    }
    public FirebaseAuth getFirebaseAuth(){
      return FirebaseAuth.getInstance();
    }

    public void logout(FirebaseAuth auth){
         auth.signOut();
    }

    public void setListenerForInvitation(final User user){

      users_ref = database.getReference("users").child(user.getKey()).child("trip_in_progress");
      valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          tripInProgress = dataSnapshot.getValue(TripDetails.class);
          if(tripInProgress != null && !tripInProgress.getHost().equals(user.getEmail())){
            users_ref.removeEventListener(valueEventListener);
            mPresenter.inviteAcquired(tripInProgress);
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
      };
      users_ref.addValueEventListener(valueEventListener);
    }

    public void getTripInProgressIfAvailable(final User user){

      users_ref = database.getReference("users").child(user.getKey()).child("trip_in_progress");
      users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

          tripInProgress = dataSnapshot.getValue(TripDetails.class);
          if(tripInProgress != null){
            if(tripInProgress.getHost().equals(user.getEmail())){

              //Current user is hosting the trip
              mPresenter.hostTripInProgress(tripInProgress);

            }else if(!tripInProgress.getHost().equals(user.getEmail()) && !tripInProgress.isInvitationReceived()){

              //Current user is a passenger of the trip and hasn't received an invitation
              mPresenter.passengerInviteNotAck(tripInProgress);

            }else if(!tripInProgress.getHost().equals(user.getEmail()) && tripInProgress.isInvitationReceived()){

              //Current user is a passenger of the trip and has already received an invitation
              mPresenter.passengerInviteAck(tripInProgress);

            }
          }

          else{
            //No trip in progress. Go to main view of home screen.
            mPresenter.noTripInProgressView(tripInProgress);
          }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
      });
    }

  public void deleteTripInProgress(final TripDetails tripDetails) {
    users_ref = database.getReference("users/");
   // tripInProgress = tripDetails;
    users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        for(DataSnapshot messsageSnapshot : dataSnapshot.getChildren()){

          User user = messsageSnapshot.getValue(User.class);
          for(int i = 0; i < tripDetails.getPassengers().size();i++){
            if(user.getEmail().equals(tripDetails.getPassengers().get(i).getEmail())){
              users_ref.child(tripDetails.getPassengers().get(i).getKey()).child("trip_in_progress").removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    //tripDetails = null;
                  mPresenter.tripInProgressDeleted();
                }
              });
            }
          }


        }
      }
      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

  }
}
