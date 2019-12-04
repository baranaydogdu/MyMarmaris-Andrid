package com.baranaydogdu.mymarmaris.PlaceActivities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity implements LocationListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    PlaceActivityAdapter adapter;
    LinearLayout mainlineer;
    Intent intent;
    Activity activity;
    ArrayList<PlacesActivityFragment> fragmentlist = new ArrayList<>();
    ArrayList<String> idlist;
    int currentposition=0;
    LocationManager locationManager;
    public static final int LOCATIONMILISECOND = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        activity = this;
        intent = getIntent();
        initviews();

        idlist = new ArrayList<>();
        idlist = intent.getStringArrayListExtra("id");
        currentposition = intent.getIntExtra("index",0);

        adapter = new PlaceActivityAdapter(getSupportFragmentManager());

        for (int i = 0; i < idlist.size(); i++) {

            fragmentlist.add(new PlacesActivityFragment(idlist.get(i)));

        }

        viewPager.setOffscreenPageLimit(idlist.size());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(currentposition);

        if (idlist.size() == 1) {
            mainlineer.removeView(tabLayout);
        }


        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentposition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

                }
            }
        }
    }

    private void initviews() {

        mainlineer = findViewById(R.id.mainlineer);
        tabLayout = findViewById(R.id.sliding_tabs);
        viewPager = findViewById(R.id.viewpager);

    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            fragmentlist.get(currentposition).setPlaces(location);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class PlaceActivityAdapter extends FragmentPagerAdapter {

        public PlaceActivityAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentlist.get(position);

        }

        @Override
        public int getCount() {

            return fragmentlist.size();

        }


        public CharSequence getPageTitle(int position) {
            // Generate title based on item position

            return PreSets.setlanguage_name(activity, PreSets.getSubcollection_from_SubCollecsitonId(activity, idlist.get(position)));
        }

    }


}
