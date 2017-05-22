package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruhshua.carpool.R;

/**
 * Created by bruhshua on 5/21/17.
 */

public class PlanTripFragment extends Fragment {

    public static PlanTripFragment newInstance(){
        PlanTripFragment planTripFragment = new PlanTripFragment();
//        Bundle args = new Bundle();
//        args.putInt("DATA",data);
//        planTripFragment.setArguments(args);
        return planTripFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.plan_trip_fragment,container,false);


        return v;
    }
}
