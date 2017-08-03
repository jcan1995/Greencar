package com.example.bruhshua.carpool.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;

import org.w3c.dom.Text;

/**
 * Created by bruhshua on 8/2/17.
 */

public class TripSummaryFragment extends Fragment {

    private User user;

    private TripDetails tripDetails;
    private TextView tvSourceAddress;
    private TextView tvDestinationAddress;
    private TextView tvPoints;
    private TextView tvNumOfPeople;
    private Button bDone;
    private Callback callback;

    public static TripSummaryFragment newInstance(TripDetails tripDetails, User user) {
        TripSummaryFragment tripSummaryFragment = new TripSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable("DETAILS",tripDetails);
        args.putSerializable("USER", user);
        tripSummaryFragment.setArguments(args);
        return tripSummaryFragment;
    }

    public interface Callback {
        public void Reset(User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PlanTripFragment.Callback){
            callback = (TripSummaryFragment.Callback) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.trip_summary_fragment,container,false);
        tripDetails = (TripDetails) getArguments().getSerializable("DETAILS");
        user = (User) getArguments().getSerializable("USER");
        tvSourceAddress = (TextView) v.findViewById(R.id.tvSourceDestination);
        tvDestinationAddress = (TextView) v.findViewById(R.id.tvDestinationAddress);
        tvPoints = (TextView) v.findViewById(R.id.tvTotalPoints);
        tvNumOfPeople = (TextView) v.findViewById(R.id.tvNumPassengers);
        bDone = (Button) v.findViewById(R.id.bDone);

        if(tripDetails != null){
            tvSourceAddress.setText(tripDetails.getCurrentAddress());
            tvDestinationAddress.setText(tripDetails.getDestinationAddress());
            tvPoints.setText(String.valueOf(tripDetails.getPoints()));
            tvNumOfPeople.setText(String.valueOf(tripDetails.getNumOfPeople()));
            bDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPage();
                }
            });
        }


        return v;


    }

    private void resetPage() {
        callback.Reset(user);
    }
}
