package com.example.bruhshua.carpool.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bruhshua on 7/24/17.
 */

public class MapUpdatePOJO implements Serializable {

    private ArrayList<PolylineOptions> mPolyOptions;
    private LatLng mCurrentLatLng;
    private LatLng mDestinationLatLng;

    public MapUpdatePOJO(ArrayList<PolylineOptions> mPolyOptions, LatLng mCurrentLatLng, LatLng mDestinationLatLng) {
       // this.mPolyOptions = new ArrayList<>();
        this.mPolyOptions = mPolyOptions;
        this.mCurrentLatLng = mCurrentLatLng;
        this.mDestinationLatLng = mDestinationLatLng;
    }

    public ArrayList<PolylineOptions> getmPolyOptions() {
        return mPolyOptions;
    }

    public void setmPolyOptions(ArrayList<PolylineOptions> mPolyOptions) {
        this.mPolyOptions = mPolyOptions;
    }

    public LatLng getmCurrentLatLng() {
        return mCurrentLatLng;
    }

    public void setmCurrentLatLng(LatLng mCurrentLatLng) {
        this.mCurrentLatLng = mCurrentLatLng;
    }

    public LatLng getmDestinationLatLng() {
        return mDestinationLatLng;
    }

    public void setmDestinationLatLng(LatLng mDestinationLatLng) {
        this.mDestinationLatLng = mDestinationLatLng;
    }
}
