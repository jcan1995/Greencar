package com.example.bruhshua.carpool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by bruhshua on 6/5/17.
 */

public class AddPassengersDialogFragment extends DialogFragment {


    public static AddPassengersDialogFragment newInstance(String data){
        AddPassengersDialogFragment addPassengersDialogFragment = new AddPassengersDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("DATA",data);
        addPassengersDialogFragment.setArguments(args);
        return addPassengersDialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
     //   final View view = inflater.inflate(R.)

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite Passengers");
      //  builder.setView();

        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
