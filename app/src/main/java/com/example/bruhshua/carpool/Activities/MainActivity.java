package com.example.bruhshua.carpool.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bruhshua.carpool.Fragments.MyAccountFragment;
import com.example.bruhshua.carpool.Fragments.MyTripsFragment;
import com.example.bruhshua.carpool.Fragments.PlanTripFragment;
import com.example.bruhshua.carpool.R;

/**
 * Created by bruhshua on 5/21/17.
 */

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        nvDrawer = (NavigationView) findViewById(R.id.navigationView);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.my_account:

                        MyAccountFragment myAccountFragment = MyAccountFragment.newInstance();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,myAccountFragment)
                                .commit();
                        break;

                    case R.id.plan_trip:

                        PlanTripFragment planTripFragment = PlanTripFragment.newInstance();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,planTripFragment)
                                .commit();
                        break;

                    case R.id.my_trips:

                        MyTripsFragment myTripsFragment = MyTripsFragment.newInstance();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,myTripsFragment)
                                .commit();
                        break;

                    default:

                        MyAccountFragment fragment1 = MyAccountFragment.newInstance();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,fragment1)
                                .commit();
                        break;

                }
                item.setCheckable(true);
                setTitle(item.getTitle());
                drawerLayout.closeDrawers();

                return true;

            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
