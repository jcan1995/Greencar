package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.User;


/**
 * Created by bruhshua on 5/21/17.
 */

//You got it Danny
public class MyAccountFragment extends Fragment {


    final private static String USER_TAG = "USER";

    private User authUser;

    public static MyAccountFragment newInstance(User user){
        MyAccountFragment myAccountFragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TAG,user);
        myAccountFragment.setArguments(args);
        return myAccountFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authUser = (User) getArguments().getSerializable(USER_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_account_fragment,container,false);

        //Todo: Do whatever you want with the user's data.


        return v;


    }
}
