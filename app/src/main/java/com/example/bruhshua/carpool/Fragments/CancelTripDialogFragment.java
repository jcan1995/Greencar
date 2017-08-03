package com.example.bruhshua.carpool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by bruhshua on 8/2/17.
 */

public class CancelTripDialogFragment extends DialogFragment {

    private Callback callback;
    private User user;

    public interface Callback {
        public void cancelTrip();
    }

    public static CancelTripDialogFragment newInstance(){

        CancelTripDialogFragment cancelTripDialogFragment = new CancelTripDialogFragment();
        Bundle args = new Bundle();
      //  args.putSerializable("USER",user);
        cancelTripDialogFragment.setArguments(args);
        return cancelTripDialogFragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CancelTripDialogFragment.Callback){
            callback = (CancelTripDialogFragment.Callback) context;
        }

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.cancel_trip_dialog_fragment,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cancel Trip?");
        builder.setView(view);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                callback.cancelTrip();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
