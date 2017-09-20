package com.example.bruhshua.carpool.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.Model.User;


/**
 * Created by bruhshua on 5/21/17.
 */

/*
 Background image credit <a href="http://www.freepik.com/free-vector/the-same-landscape-in-different-seasons-banners_844369.htm">Designed by Freepik</a>
 */

public class MyAccountFragment extends Fragment {


    final private static String USER_TAG = "USER";

    private User authUser;
    private TextView tvUserName;
    private TextView tvPoints;
    private TextView tvEmail;
    private ImageView ivUserPhoto;
    private ImageView ivBackGroundImage;

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

        if(authUser != null){

            ivUserPhoto = (ImageView) v.findViewById(R.id.ivUserImage);
            ivBackGroundImage = (ImageView) v.findViewById(R.id.ivBackGroundImage);
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvPoints = (TextView) v.findViewById(R.id.tvPoints);
            // tvEmail = (TextView) v.findViewById(R.id.tvEmail);

            Glide.with(getActivity())
                    .load(authUser.getDownloadUrl())
                    .into(ivUserPhoto);

//            Glide.with(getActivity())
//                    .load(authUser.getDownloadUrl())
//                    .into(ivBackGroundImage);
            ivBackGroundImage.setAlpha(.5f);

            tvUserName.setText(authUser.getUserName());
            tvPoints.setText(" | " + String.valueOf((int)authUser.getPoints()));
           // tvEmail.setText(authUser.getEmail());

        }


        return v;


    }
}
