package com.baranaydogdu.mymarmaris.Fragments;
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

import androidx.annotation.NonNull;
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

public class FragmentFavorittes extends Fragment implements LocationListener {

    public static FragmentFavorittes instance;

    RecyclerView recyclerView;
    public PlacesActivityAdapter placesActivityAdapter;
    ArrayList<PlaceDistanceClass> placelist, favplacelist, nonfavplacelist, distanceClasses, nondistanceClasses;
    SharedPreferences sharedPreferences;
    int selected_language = 0;
    Activity activity;
    ArrayList<PlaceClass> firstlist;
    LocationManager locationManager;
    TextView favlist_tv;
    int today;

    public static FragmentFavorittes getInstance() {

        if (instance == null) {

            FragmentFavorittes.instance = new FragmentFavorittes();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorittes, container, false);


    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        today= Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (today==1) today=6;else today=today-2;

        activity = getActivity();
        placelist = new ArrayList<>();
        favplacelist = new ArrayList<>();
        nonfavplacelist = new ArrayList<>();
        distanceClasses = new ArrayList<>();
        nondistanceClasses = new ArrayList<>();
        firstlist = new ArrayList<>();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);

        recyclerView = view.findViewById(R.id.places_recyclerview);
        placesActivityAdapter = new PlacesActivityAdapter();
        recyclerView.setAdapter(placesActivityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        favlist_tv=view.findViewById(R.id.favorite_list_tv);

        String[] favlisttext={"Favorite list","Favori listesi","Список избранного","Λίστα αγαπημένων","Liebling"};
        favlist_tv.setText(favlisttext[selected_language]);



    }


    public void setlist() {

        firstlist.clear();

        firstlist=PreSets.get_favlist(getContext());

        Collections.sort(firstlist, new Comparator<PlaceClass>() {
            @Override
            public int compare(PlaceClass o1, PlaceClass o2) {
                return o1.getSortnumber() - o2.getSortnumber();
            }
        });

        setPlaces(null);

    }



    public void setPlaces(Location location) {

        placelist.clear();
        favplacelist.clear();
        nonfavplacelist.clear();
        distanceClasses.clear();
        nondistanceClasses.clear();

        for (PlaceClass place:firstlist){

            if (place.getIsactive()) {

                Float distance = 0f;

                if (location!=null) {

                    if (place.getLocation().getLat() != 0 && place.getLocation().getLog() != 0 &&
                            location.getLongitude() != 0 && location.getLatitude() != 0) {

                        Location place_location = new Location("place_location");
                        place_location.setLatitude(place.getLocation().getLat());
                        place_location.setLongitude(place.getLocation().getLog());

                        distance = location.distanceTo(place_location);

                    }
                }

                PlaceDistanceClass placeDistanceClass = new PlaceDistanceClass(place, distance);

                placelist.add(placeDistanceClass);
            }
        }

        placesActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        setlist();

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

        }
    }


    @Override
    public void onLocationChanged(Location location) {

        setPlaces(location);
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
        public PlaceActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_places_layout,
                    parent, false);
            return new PlaceActivityViewHolder(view);
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


            int opentime = mplace.getOpentime().get(today);
            int closetime = mplace.getClosetime().get(today);
            int now = (Calendar.getInstance().get(Calendar.HOUR) * 100) + (Calendar.getInstance().get(Calendar.MINUTE));
            if (Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM) now = now + 1200;

            String[] opentext = {"Open", "Açık", "открытый", "ανοιχτό", "Öffnen"};
            String[] closetext = {"Close", "Kapalı", "близко", "κλειστό", "Geschlossen"};

            Boolean isopen;


            if (opentime < closetime){

                if (opentime<now && now<closetime){ //ACIK ISE

                    isopen = true;

                } else {
                    isopen = false;

                }

            } else {

                if (opentime<now || now<closetime){ //ACIK ISE

                    isopen = true;

                } else {
                    isopen = false;

                }

            }

            if (isopen) {


                holder.close_tv.setText(opentext[selected_language]);
                holder.close_tv.setBackgroundResource(R.drawable.shape_new_edittext);

            } else {
                holder.close_tv.setText(closetext[selected_language]);
                holder.close_tv.setBackgroundResource(R.drawable.shape_close);

            }

            System.out.println("TAAAAAAAAAAAAAAA");




            String[] neww = {"New", "Yeni", "новый", "νέος", "Neu"};
            holder.new_tv.setText(neww[selected_language]);
            if (mplace.getNew_end_time() < Calendar.getInstance().getTime().getTime()) {

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
