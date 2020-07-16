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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PlacesActivity extends AppCompatActivity implements LocationListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    PlaceActivityAdapter adapter;
    LinearLayout mainlineer;

    ArrayList<PlacesActivityFragment> fragmentlist = new ArrayList<>();
    RealmResults<PlaceCollectionForRealm> subcollectionlist;

    SharedPreferences sharedPreferences;
    Realm realm;

    int lan = 0;
    int currentposition;

    LocationManager locationManager;
    public static final int LOCATIONMILISECOND = 1000;
    Location lastlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mainlineer = findViewById(R.id.mainlineer);
        tabLayout = findViewById(R.id.sliding_tabs);
        viewPager = findViewById(R.id.viewpager);

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);

        realm = Realm.getDefaultInstance();
        String mainId = getIntent().getStringExtra("id");
        System.out.println("mainId : "+mainId );
        currentposition = getIntent().getIntExtra("index", 0);

        subcollectionlist = realm.where(PlaceCollectionForRealm.class).equalTo("masterid", mainId).findAll().sort("sortnumber");
        System.out.println("subcollectionlist.size() "+subcollectionlist.size());

        if (subcollectionlist.size() == 0 )
            subcollectionlist = realm.where(PlaceCollectionForRealm.class).equalTo("id", mainId).findAll().sort("sortnumber");

        System.out.println("subcollectionlist.size() "+subcollectionlist.size());

        adapter = new PlaceActivityAdapter(getSupportFragmentManager());

        for (int i = 0; i < subcollectionlist.size(); i++) {

            fragmentlist.add(new PlacesActivityFragment(subcollectionlist.get(i),lastlocation));

        }

        viewPager.setOffscreenPageLimit(subcollectionlist.size());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(currentposition);

        if (subcollectionlist.size() == 1) {
            mainlineer.removeView(tabLayout);
        }


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentposition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
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

            return LanguagePack.getLanguage(subcollectionlist.get(position).name, lan);
        }

    }


}
