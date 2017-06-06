package com.example.bruhshua.carpool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bruhshua on 6/5/17.
 */

public class AddPassengersDialogFragment extends DialogFragment {


    private FirebaseDatabase database;
    private DatabaseReference users_ref;
    private Map<String, User> users = new HashMap<>();

    public static AddPassengersDialogFragment newInstance(){

        AddPassengersDialogFragment addPassengersDialogFragment = new AddPassengersDialogFragment();
        Bundle args = new Bundle();
       // args.putSerializable("DATA",data);
        addPassengersDialogFragment.setArguments(args);
        return addPassengersDialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.add_passengers_dialog_fragment,null);

        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");
//        users_ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("PlanTrip","onDataChange");
//
//                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
//                    Log.d("PlanTrip","data: " + messageSnapshot.getValue());
//
//                    User user = messageSnapshot.getValue(User.class);
//
//                    Log.d("PlanTrip","username: " + user.getUserName());
//
//
//                }
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //Log.d("PlanTrip","onDataChange");
//
//            }
//        });

        ListView passengersListView = (ListView) view.findViewById(R.id.listView);
        EditText etPassenger = (EditText) view.findViewById(R.id.etPassenger);
        etPassenger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

//                Log.d("PlanTrip","beforeTextChanged");
//                Log.d("PlanTrip","" + s );


            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
//                Log.d("PlanTrip","onTextChanged");
//                Log.d("PlanTrip","" + s );
                users_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("PlanTrip","onDataChange");

                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
                            Log.d("PlanTrip","data: " + messageSnapshot.getValue());

                            User user = messageSnapshot.getValue(User.class);

                            if(user.getUserName().contains(s)){

                            }
                            Log.d("PlanTrip","username: " + user.getUserName());


                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.d("PlanTrip","onDataChange");

                    }
                });



            }
            @Override
            public void afterTextChanged(Editable s) {
//                Log.d("PlanTrip","afterTextChanged");
//                Log.d("PlanTrip","" + s );

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
