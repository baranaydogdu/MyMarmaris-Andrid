package com.baranaydogdu.mymarmaris.SearcOperations;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceDistanceClass;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import static com.baranaydogdu.mymarmaris.PlaceActivities.PlacesActivity.LOCATIONMILISECOND;

public class SerachPlaceFragment extends Fragment implements LocationListener{

    private static SerachPlaceFragment instance;

    RecyclerView recyclerView;
    public PlacesActivityAdapter placesActivityAdapter;
    ArrayList<PlaceDistanceClass> placelist, favplacelist, nonfavplacelist, distanceClasses, nondistanceClasses;
    SharedPreferences sharedPreferences;
    int selected_language = 0;
    Activity activity;
    ArrayList<PlaceClass> firstlist;
    LocationManager locationManager;
    Location mylocation;

    private SerachPlaceFragment() {
    }

    public static SerachPlaceFragment getInstance(){
        if (instance==null) instance=new SerachPlaceFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_serach_place, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        activity=getActivity();
        placelist = new ArrayList<>();
        favplacelist = new ArrayList<>();
        nonfavplacelist = new ArrayList<>();
        distanceClasses = new ArrayList<>();
        nondistanceClasses = new ArrayList<>();
        firstlist=new ArrayList<>();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);

        recyclerView = view.findViewById(R.id.places_recyclerview);
        placesActivityAdapter = new PlacesActivityAdapter();
        recyclerView.setAdapter(placesActivityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

    }

    public void setlist(String searctext) {

        ArrayList<PlaceDistanceClass> backlist=new ArrayList<>();
        backlist.clear();
        ArrayList<PlaceClass> firstlist=new ArrayList<>();
        firstlist.clear();

        for (String s : PreSets.get_places_from_contains(activity, searctext)) {

            firstlist.add(PreSets.getPlace(activity, s));
        }
        favplacelist.clear();
        nonfavplacelist.clear();
        distanceClasses.clear();
        nondistanceClasses.clear();

        for (PlaceClass place:firstlist){

            if (place.getIsactive()) {

                Float distance = 0f;

                if (mylocation!=null) {

                    if (place.getLocation().getLat() != 0 && place.getLocation().getLog() != 0 &&
                            mylocation.getLongitude() != 0 && mylocation.getLatitude() != 0) {

                        Location place_location = new Location("place_location");
                        place_location.setLatitude(place.getLocation().getLat());
                        place_location.setLongitude(place.getLocation().getLog());

                        distance = mylocation.distanceTo(place_location);

                    }
                }

                PlaceDistanceClass placeDistanceClass = new PlaceDistanceClass(place, distance);

                backlist.add(placeDistanceClass);
            }
        }

        //FAVORI YADA DEGIL AYIR-----
        for (PlaceDistanceClass singleplace : backlist) {

            if (singleplace.getPlace().getSortnumber() == PlaceClass.FAVORIDEGIL) {//FAVORI DEGIL ISE

                nonfavplacelist.add(singleplace);

            } else { //FAVORI ISE

                favplacelist.add(singleplace);

            }

        }

        //FAVIRILERI SIRALA
        Collections.sort(favplacelist, new Comparator<PlaceDistanceClass>() {
            @Override
            public int compare(PlaceDistanceClass o1, PlaceDistanceClass o2) {
                return o1.getPlace().getSortnumber() - o2.getPlace().getSortnumber();
            }
        });

        //FAVORI OLMAYANLARI DISTANCE YADA NONDISTANCE AYARLA
        for (PlaceDistanceClass singleplace : nonfavplacelist) {

            if (singleplace.getDistance() != 0) {
                distanceClasses.add(singleplace);
            } else {
                nondistanceClasses.add(singleplace);
            }
        }


        //MESAFESI OLANLARI SIRALA
        Collections.sort(distanceClasses, new Comparator<PlaceDistanceClass>() {
            @Override
            public int compare(PlaceDistanceClass o1, PlaceDistanceClass o2) {
                return o1.getDistance() - o2.getDistance();
            }
        });


        backlist.clear();
        for (PlaceDistanceClass placeDistanceClass:favplacelist){

            backlist.add(placeDistanceClass);
        }

        for (PlaceDistanceClass placeDistanceClass:distanceClasses){

            backlist.add(placeDistanceClass);
        }

        for (PlaceDistanceClass placeDistanceClass:nondistanceClasses){

            backlist.add(placeDistanceClass);
        }


        placelist.clear();
        placelist=backlist;
        placesActivityAdapter.notifyDataSetChanged();

        if (placelist.size() == 0)
            SearchMainFragment.getInstance().firstplacename = SearchMainFragment.getInstance().placestext[selected_language];
        else
            SearchMainFragment.getInstance().firstplacename = SearchMainFragment.getInstance().placestext[selected_language] + " (" + SerachPlaceFragment.getInstance().placelist.size() + ")";

        SearchMainFragment.getInstance().adapter.notifyDataSetChanged();


    }

    public void clearlist(){

        placelist.clear();
        placesActivityAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mylocation=location;
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


    public class PlacesActivityAdapter extends RecyclerView.Adapter<PlacesActivityAdapter.PlaceActivityViewHolder> {

        public PlacesActivityAdapter() {

        }

        @Override
        public PlacesActivityAdapter.PlaceActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_places_layout,
                    parent, false);
            return new PlacesActivityAdapter.PlaceActivityViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceActivityViewHolder holder, final int position) {

            final PlaceClass mplace = placelist.get(position).getPlace();

            float distance = placelist.get(position).getDistance();

            if (distance != 0) {

                holder.placerange.setVisibility(View.VISIBLE);

                if (distance < 1000) {

                    holder.placerange.setText(((int) (distance / 10)) + "0 m");

                } else holder.placerange.setText(((float) ((int) (distance / 100))) / 10 + " km");


            } else {

                holder.placerange.setVisibility(View.INVISIBLE);

            }

            holder.isfav_icon.setVisibility(View.INVISIBLE);

            PreSets.set_MAIN_photo(holder.placeimage, mplace.getId());

            holder.placename.setText(PreSets.setlanguage_name(activity, mplace));

            holder.placeexplanation.setText(PreSets.setlanguage_explain(activity, mplace));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, PlaceView.class);
                    intent.putExtra("id", placelist.get(position).getPlace().getId());

                    activity.startActivity(intent);
                }
            });



            int today= Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (today==1) today=6;else today=today-2;

            int opentime=mplace.getOpentime().get(today);
            int openhour=opentime/100;
            int openminute=opentime-(openhour*100);
            int openAM_PM;
            if (openhour>11){
                openhour=openhour-12;
                openAM_PM=Calendar.PM;
            } else {openAM_PM=Calendar.AM;

            }

            Calendar cal_open=Calendar.getInstance();
            cal_open.set(Calendar.HOUR,openhour);cal_open.set(Calendar.MINUTE,openminute);cal_open.set(Calendar.AM_PM,openAM_PM);

            int closetime=mplace.getClosetime().get(today);
            int closehour=closetime/100;
            int closeminute=closetime-(closehour*100);
            int closeAM_PM;
            if (closehour>11) {
                closehour=closehour-12;closeAM_PM=Calendar.PM;
            } else closeAM_PM=Calendar.AM;

            Calendar cal_close=Calendar.getInstance();
            cal_close.set(Calendar.HOUR,closehour);cal_open.set(Calendar.MINUTE,closeminute);cal_close.set(Calendar.AM_PM,closeAM_PM);

            String[] opentext={"OPEN","AÇIK","OPEN","OPEN","OPEN"};
            String[] closetext={"CLOSE","KAPALI", "CLOSE","CLOSE","CLOSE"};

            if (!cal_open.getTime().after(Calendar.getInstance().getTime())) {

                if (!Calendar.getInstance().getTime().after(cal_close.getTime())){

                    holder.close_tv.setText(opentext[selected_language]);
                    holder.close_tv.setBackgroundResource(R.drawable.shape_new_edittext);
                }else {

                    holder.close_tv.setText(closetext[selected_language]);
                    holder.close_tv.setBackgroundResource(R.drawable.shape_close);
                }

            }else {

                holder.close_tv.setText(closetext[selected_language]);
                holder.close_tv.setBackgroundResource(R.drawable.shape_close);
            }


            String[] neww={"NEW","YENİ","NEW","NEW","NEW"};
            holder.new_tv.setText(neww[selected_language]);
            if (mplace.getNew_end_time()< Calendar.getInstance().getTime().getTime()){

                holder.new_tv.setVisibility(View.INVISIBLE);
            } else {
                holder.new_tv.setVisibility(View.VISIBLE);
            }

        }




        @Override
        public int getItemCount() {

            return placelist.size();

        }

        public class PlaceActivityViewHolder extends RecyclerView.ViewHolder {

            ImageView placeimage, isfav_icon;
            TextView placerange;
            EditText placename, placeexplanation;
            TextView new_tv,close_tv;

            public PlaceActivityViewHolder(View itemView) {
                super(itemView);

                placeimage = itemView.findViewById(R.id.custum_place_imageView);
                isfav_icon = itemView.findViewById(R.id.isfav_icon);
                placerange = itemView.findViewById(R.id.custum_place_range);
                placename = itemView.findViewById(R.id.custum_place_name);
                placeexplanation = itemView.findViewById(R.id.custum_place_explain);

                new_tv= itemView.findViewById(R.id.new_tv2);
                close_tv=itemView.findViewById(R.id.close_tv2);
                close_tv.setVisibility(View.VISIBLE);

            }
        }
    }

}
