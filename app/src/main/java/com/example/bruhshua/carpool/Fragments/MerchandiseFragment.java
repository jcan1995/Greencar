package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruhshua.carpool.Model.User;
import com.example.bruhshua.carpool.R;

/**
 * Created by bruhshua on 8/3/17.
 */

public class MerchandiseFragment extends Fragment {


    private User user;

    public static MerchandiseFragment newInstance(User user){
        MerchandiseFragment merchandiseFragment = new MerchandiseFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER", user);
        merchandiseFragment.setArguments(args);
        return merchandiseFragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.merchandise_fragment,container,false);

        user = (User) getArguments().getSerializable("USER");



        return v;
    }
}
