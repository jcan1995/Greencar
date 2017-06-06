package com.example.bruhshua.carpool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.bruhshua.carpool.R;

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

        final View view = inflater.inflate(R.layout.add_passengers_dialog_fragment,null);

        ListView passengersListView = (ListView) view.findViewById(R.id.listView);


        EditText etPassenger = (EditText) view.findViewById(R.id.etPassenger);
        etPassenger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite Passengers");
        builder.setView(view);

        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
