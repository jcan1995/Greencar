package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bruhshua.carpool.Adapters.UserTripsListViewAdapter;
import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by bruhshua on 5/22/17.
 */

//Maybe this class should listen to firebase for a new item?
//This class will listen to the user's "trips" data field.
public class MyTripsFragment extends Fragment {

    private User authUser;
    private FirebaseDatabase database;
    private DatabaseReference users_ref;
    private ArrayList<TripDetails> usersTrips;
    private ListView listView;

    public static MyTripsFragment newInstance(User user){
        MyTripsFragment myTripsFragment = new MyTripsFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER",user);
        myTripsFragment.setArguments(args);
        return myTripsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authUser = (User) getArguments().getSerializable("USER");
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users").child(authUser.getKey()).child("trips");
        usersTrips = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_trips_fragment,container,false);
        listView = (ListView) v.findViewById(R.id.my_trips_list_view);

        users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("MyTripsFrag","child size: " + dataSnapshot.getChildrenCount());

                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
                    TripDetails tripDetails = messageSnapshot.getValue(TripDetails.class);
                    usersTrips.add(tripDetails);
                }
                updateUI();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;


    }

    private void updateUI() {

        if(usersTrips != null && usersTrips.size()!=0){
            UserTripsListViewAdapter adapter = new UserTripsListViewAdapter(usersTrips,getContext());
            listView.setAdapter(adapter);
        }

    }
}
