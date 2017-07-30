package com.example.bruhshua.carpool.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.Model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by bruhshua on 6/5/17.
 */

public class UsersListViewAdapter extends BaseAdapter{

    public ArrayList<User> users;
    public Context context;

    public UsersListViewAdapter(ArrayList<User> users, Context context){
        this.users = users;
        this.context = context;
    }



    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.users_list_view_item, parent, false);

        User user = (User) getItem(position);
        ImageView ivPicture = (ImageView) v.findViewById(R.id.ivUserImage);

        Glide.with(this.context)
                .load(user.getDownloadUrl())
                .into(ivPicture);

        return v;
    }
}
