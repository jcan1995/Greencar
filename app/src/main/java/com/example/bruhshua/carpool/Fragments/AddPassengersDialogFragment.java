package com.example.bruhshua.carpool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bruhshua.carpool.Adapters.UsersListViewAdapter;
import com.example.bruhshua.carpool.R;
import com.example.bruhshua.carpool.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bruhshua on 6/5/17.
 */

public class AddPassengersDialogFragment extends DialogFragment {


    private FirebaseDatabase database;
    private DatabaseReference users_ref;
   // private Map<String, User> users = new HashMap<>();
    private ArrayList<User> users = new ArrayList<>();
    private UsersListViewAdapter mAdapter;
    private ListView passengersListView;
    private ImageView ivAddPassenger;

    public static AddPassengersDialogFragment newInstance(){

        AddPassengersDialogFragment addPassengersDialogFragment = new AddPassengersDialogFragment();
        Bundle args = new Bundle();
       // args.putSerializable("DATA",data);
        addPassengersDialogFragment.setArguments(args);
        return addPassengersDialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.add_passengers_dialog_fragment,null);
        passengersListView = (ListView) view.findViewById(R.id.listView);
        final EditText etPassenger = (EditText) view.findViewById(R.id.etPassenger);

        ivAddPassenger = (ImageView) view.findViewById(R.id.ivFindPassenger);
        ivAddPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etPassenger.getText().toString().isEmpty()) {
                    queryFirebase(etPassenger.getText().toString().trim());
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Input Name.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        database = FirebaseDatabase.getInstance();
        users_ref = database.getReference("users");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite Passengers");
        builder.setView(view);

        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    private void queryFirebase(final String userName) {

        users_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        users.clear();

                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){

                            User user = messageSnapshot.getValue(User.class);

                            if(user.getUserName().contains(userName)){
                                Log.d("PlanTrip","username has 's' character");

                                users.add(user);
                                updateUI();

                            }

                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("PlanTrip","onCancelled");

                    }
                });
    }

    private void updateUI() {
        mAdapter = new UsersListViewAdapter(users,getActivity().getApplicationContext());
        passengersListView.setAdapter(mAdapter);

    }
}
