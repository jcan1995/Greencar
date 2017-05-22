package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruhshua.carpool.R;

/**
 * Created by bruhshua on 5/22/17.
 */

public class MyTripsFragment extends Fragment {


    public static MyTripsFragment newInstance(){
        MyTripsFragment myTripsFragment = new MyTripsFragment();
//        Bundle args = new Bundle();
//        args.putInt("DATA",data);
//        planTripFragment.setArguments(args);
        return myTripsFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_trips_fragment,container,false);




        return v;


    }
}
