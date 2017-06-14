package com.example.bruhshua.carpool.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.Model.User;

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

        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvUserName.setText(user.getUserName());

        ImageView ivPicture = (ImageView) v.findViewById(R.id.ivUserImage);
       // ivPicture.setImageResource();

        return v;
    }
}
