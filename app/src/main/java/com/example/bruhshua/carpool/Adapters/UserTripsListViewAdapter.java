package com.example.bruhshua.carpool.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruhshua.carpool.Model.TripDetails;
import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by bruhshua on 6/13/17.
 */

public class UserTripsListViewAdapter extends BaseAdapter {

    public ArrayList<TripDetails> tripDetails;
    public Context context;

    public UserTripsListViewAdapter(ArrayList<TripDetails> tripDetails, Context context) {
        this.tripDetails = tripDetails;
        this.context = context;
    }


    @Override
    public int getCount() {
        return tripDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return tripDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.my_trips_list_view_item, parent, false);

        TripDetails tripDetails = (TripDetails) getItem(position);

        TextView sourceAddress = (TextView) v.findViewById(R.id.tvSourceDestination);
        sourceAddress.setText(tripDetails.getCurrentAddress());

        TextView destinationAddress = (TextView) v.findViewById(R.id.tvDestinationAddress);
        destinationAddress.setText(tripDetails.getDestinationAddress());

        TextView miles = (TextView) v.findViewById(R.id.tvMiles);
        miles.setText(String.valueOf(tripDetails.getMiles()));

        TextView points = (TextView) v.findViewById(R.id.tvPoints);
        points.setText(String.valueOf(tripDetails.getPoints()));

        TextView passengers = (TextView) v.findViewById(R.id.tvPassengers);
        passengers.setText(String.valueOf(tripDetails.getNumOfPeople()));

        return v;
    }
}
