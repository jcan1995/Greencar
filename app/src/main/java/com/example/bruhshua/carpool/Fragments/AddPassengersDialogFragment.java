package com.example.bruhshua.carpool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Adapters.AddPassengersListViewAdapter;
import com.example.bruhshua.carpool.Adapters.UsersListViewAdapter;
import com.example.bruhshua.carpool.Manifest;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by bruhshua on 6/5/17.
 */

public class AddPassengersDialogFragment extends DialogFragment {


    private FirebaseDatabase database;
    private DatabaseReference users_ref;
   // private Map<String, User> users = new HashMap<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> bufferedPassengers = new ArrayList<>();

    private AddPassengersListViewAdapter mAdapter;

    private ListView passengersListView;
    private ListView bufferedListView;

    private ImageView ivAddPassenger;
    private EditText etPassenger;

    private LocalBroadcastManager manager ;
    private Intent i;

    public static AddPassengersDialogFragment newInstance(Intent i){

        AddPassengersDialogFragment addPassengersDialogFragment = new AddPassengersDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("INTENT",i);
        addPassengersDialogFragment.setArguments(args);
        return addPassengersDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.add_passengers_dialog_fragment,null);

        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.SEND_SMS},1);
        bufferedListView = (ListView) view.findViewById(R.id.lvBufferedPool);

        manager = LocalBroadcastManager.getInstance(getActivity());
        i = getArguments().getParcelable("INTENT");
        Bundle bundle = i.getParcelableExtra("BUNDLE");

        bufferedPassengers = (ArrayList<User>) bundle.getSerializable("POOL");
        Log.d("SizeTest","bufferedPassengers: " + bufferedPassengers.size());
        updatePool();

        passengersListView = (ListView) view.findViewById(R.id.listView);
        passengersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!isInPool(users.get(position))) {
                    users.get(position).setValidated(false);
                    bufferedPassengers.add(users.get(position));
                    updatePool();
                    users.clear();
                    mAdapter = new AddPassengersListViewAdapter(users, getActivity().getApplicationContext());
                    passengersListView.setAdapter(mAdapter);
                }else{
                    Toast.makeText(getActivity(),"Person is already in the pool.",Toast.LENGTH_SHORT).show();

                }

            }
        });



        etPassenger = (EditText) view.findViewById(R.id.etPassenger);

        ivAddPassenger = (ImageView) view.findViewById(R.id.ivFindPassenger);
        ivAddPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etPassenger.getText().toString().isEmpty()) {
                    queryFirebase(etPassenger.getText().toString().trim());
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Input Name.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite Passengers");
        builder.setView(view);

        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Bundle args = new Bundle();
                args.putSerializable("POOL",bufferedPassengers);

                Intent i = new Intent("com.action.getpool");
                i.putExtra("BUNDLE",args);
                manager.sendBroadcast(i);

            }
        });

        return builder.create();
    }


    //Checks if user is already in the pool
    private boolean isInPool(User user) {

        for(int i = 0; i < bufferedPassengers.size();i++){
            if(bufferedPassengers.get(i).getUserName() == user.getUserName()){

                Log.d("SizeTest","true");
                return true;
            }
        }
        Log.d("SizeTest","false");

        return false;

    }

    private void queryFirebase(final String userName) {

        if(userName.equals(PlanTripFragment.getAuthUser().getUserName())){
            Toast.makeText(getActivity(),"Ohhhh weeee that's you dummy!",Toast.LENGTH_SHORT).show();

        }else {
            users_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users.clear();

                    Log.d("PlanTrip", "Autherusername: " + PlanTripFragment.getAuthUser().getUserName());

                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                        User user = messageSnapshot.getValue(User.class);

                        if (user.getUserName().contains(userName) && !PlanTripFragment.getAuthUser().getUserName().equals(user.getUserName())) {
                            Log.d("PlanTrip", "username has 's' character");

                            users.add(user);
                            updateUI();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("PlanTrip", "onCancelled");

                }
            });
        }
    }

    private void updateUI() {
        Log.d("PlanTrip","dialog updateUI");

        //Meaning if the dialog is actually showing. This coincidentally gets fired off from the onDataChanged due to the new trip added.
        if(getActivity() != null) {
            mAdapter = new AddPassengersListViewAdapter(users, getActivity().getApplicationContext());
            passengersListView.setAdapter(mAdapter);
        }

    }

    private void updatePool() {
        if(bufferedPassengers != null) {
            mAdapter = new AddPassengersListViewAdapter(bufferedPassengers, getActivity().getApplicationContext());
            bufferedListView.setAdapter(mAdapter);
        }
    }
}
