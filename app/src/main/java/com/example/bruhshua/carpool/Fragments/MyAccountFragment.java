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

public class MyAccountFragment extends Fragment {



    public static MyAccountFragment newInstance(){
        MyAccountFragment myAccountFragment = new MyAccountFragment();
//        Bundle args = new Bundle();
//        args.putInt("DATA",data);
//        myAccountFragment.setArguments(args);
        return myAccountFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_account_fragment,container,false);



        return v;


    }
}
