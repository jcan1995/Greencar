package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.R;

import org.w3c.dom.Text;

/**
 * Created by bruhshua on 7/7/17.
 */

public class TripDetailsFragment extends Fragment {


    private TripDetails tripDetails;
    private TextView tvSourceAddress;
    private TextView tvDestinationAddress;
    private TextView tvMiles;
    private TextView tvPoints;
    private TextView tvPassengers;




    public static TripDetailsFragment newInstance(TripDetails tripDetails){

        TripDetailsFragment tripDetailsFragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("TRIPDETAILS",tripDetails);
        tripDetailsFragment.setArguments(args);
        return tripDetailsFragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_trips_list_view_item,container,false);

        tripDetails = (TripDetails) getArguments().getSerializable("TRIPDETAILS");

        tvSourceAddress = (TextView) v.findViewById(R.id.tvSourceDestination);
        tvDestinationAddress = (TextView) v.findViewById(R.id.tvDestinationAddress);
        tvMiles = (TextView) v.findViewById(R.id.tvMiles);
        tvPoints = (TextView) v.findViewById(R.id.tvPoints);
        tvPassengers = (TextView) v.findViewById(R.id.tvPassengers);

        if(tripDetails != null){

            tvSourceAddress.setText(tripDetails.getCurrentAddress());
            tvDestinationAddress.setText(tripDetails.getDestinationAddress());
            tvMiles.setText(String.valueOf(tripDetails.getMiles()));
            tvPoints.setText(String.valueOf(tripDetails.getPoints()));
            tvPassengers.setText(String.valueOf(tripDetails.getNumOfPeople()));
        }




        return v;
    }
}
