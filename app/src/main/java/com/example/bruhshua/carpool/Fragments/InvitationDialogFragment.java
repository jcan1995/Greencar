package com.example.bruhshua.carpool.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bruhshua on 8/7/17.
 */
/*This DialogFragment displays when a user has been invited to carpool.
The user may either accept or decline the invitation.
 */
public class InvitationDialogFragment extends DialogFragment {

    private Callback callback;
    private TripDetails tripDetails;
    private User authUser;

    public interface Callback {
        public void declineInvitation();
        public void acceptInvitation(TripDetails tripDetails);
    }


    public static InvitationDialogFragment newInstance(TripDetails tripDetails,User user){

        InvitationDialogFragment invitationDialogFragment = new InvitationDialogFragment();
        Bundle args = new Bundle();
          args.putSerializable("TRIPDETAILS",tripDetails);
        args.putSerializable("USER",user);
        invitationDialogFragment.setArguments(args);
        return invitationDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Invitation", "In onAttach");

        if(context instanceof InvitationDialogFragment.Callback){
            Log.d("Invitation", "In condition");

            callback = (InvitationDialogFragment.Callback) context;
        }else{
            Log.d("Invitation", "In else");
            Log.d("Invitation",context.toString());

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Invitation", "In onAttach");
        if(activity instanceof InvitationDialogFragment.Callback){
            Log.d("Invitation", "In condition");

            callback = (InvitationDialogFragment.Callback) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.invitation_dialog_fragment,null);
        authUser = (User) getArguments().getSerializable("USER");
        tripDetails = (TripDetails) getArguments().getSerializable("TRIPDETAILS");
        if(callback == null){
            Log.d("Invitation", "Callback is null");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("You have been invited for a trip!");
        builder.setView(view);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Takes user to their trip that's in progress.
                tripDetails.setInvitationReceived(true);
                updateTripDetail(tripDetails);
                callback.acceptInvitation(tripDetails);

            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Leaves user on their current page. Deletes POJO from "trip_in_progress" in DB
                callback.declineInvitation();
            }
        });

        return builder.create();
    }

    private void updateTripDetail(final TripDetails tripDetail) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users_ref = database.getReference("users").child(authUser.getKey()).child("trip_in_progress");

        users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users_ref.setValue(tripDetail);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}
