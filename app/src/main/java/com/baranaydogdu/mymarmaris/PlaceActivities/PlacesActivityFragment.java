package com.baranaydogdu.mymarmaris.PlaceActivities;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.baranaydogdu.mymarmaris.Classes.FavoritteRealm;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionForRealm;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.PlaceClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import com.baranaydogdu.mymarmaris.PreSets;

import io.realm.Realm;
import io.realm.RealmResults;


public class PlacesActivityFragment extends Fragment {

    RecyclerView recyclerView;

    public PlacesActivityAdapter placesActivityAdapter;
    PlaceCollectionForRealm subcollection;
    RealmResults<PlaceForRealm> placelist;
    ArrayList<PlaceClass> sortedPlaceList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    int lan = 0;
    int today;
    Realm realm;
    Location lastlocation;
    LanguagePack languagePack = new LanguagePack();

    public PlacesActivityFragment(PlaceCollectionForRealm subcollection, Location lastlocation) {
        this.subcollection = subcollection;
        this.lastlocation = lastlocation;
        realm = Realm.getDefaultInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {  //MAGIC IS HERE

        today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (today == 1) today = 6;
        else today = today - 2;

        placelist = realm.where(PlaceForRealm.class).equalTo("subid", subcollection.id).findAll().sort("sortnumber");

        sharedPreferences = getActivity().getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);

        recyclerView = view.findViewById(R.id.places_recyclerview);
        placesActivityAdapter = new PlacesActivityAdapter();
        recyclerView.setAdapter(placesActivityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setPlaces(lastlocation);
    }

    public void setPlaces(Location location) {

        sortedPlaceList.clear();
        ArrayList<PlaceClass> distanceList = new ArrayList<>();
        ArrayList<PlaceClass> nondistanceList = new ArrayList<>();

        for (PlaceForRealm single : placelist) {
            if (single.isactive) {
                if (single.sortnumber != 100000) {
                    PlaceClass favPlace = single.toPlace();

                    if (location != null && favPlace.location.lat != 0 && favPlace.location.log != 0) {
                        Location placeLoc = new Location("");
                        placeLoc.setLatitude(favPlace.location.lat);
                        placeLoc.setLongitude(favPlace.location.log);
                        favPlace.distance = location.distanceTo(placeLoc);
                    }
                    sortedPlaceList.add(favPlace);
                } else {

                    if (single.lat != 0 && single.log != 0) {
                        PlaceClass disPlace = single.toPlace();
                        if (location != null) {
                            Location place_location = new Location("place_location");
                            place_location.setLatitude(disPlace.getLocation().getLat());
                            place_location.setLongitude(disPlace.getLocation().getLog());
                            disPlace.distance = location.distanceTo(place_location);
                        }
                        distanceList.add(disPlace);
                    } else {
                        nondistanceList.add(single.toPlace());
                    }
                }
            }
        }

        Collections.sort(distanceList, new Comparator<PlaceClass>() {
            @Override
            public int compare(PlaceClass o1, PlaceClass o2) {
                return (int) o1.distance - (int) o2.distance;
            }
        });

        for (PlaceClass place :distanceList) sortedPlaceList.add(place);
        for (PlaceClass place :nondistanceList) sortedPlaceList.add(place);

        placesActivityAdapter.notifyDataSetChanged();

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

            final PlaceClass mplace = sortedPlaceList.get(position);

            float distance = mplace.distance;

            if (distance != 0) {

                holder.placerange.setVisibility(View.VISIBLE);

                if (distance < 1000) {

                    holder.placerange.setText(((int) (distance / 10)) + "0 m");

                } else holder.placerange.setText(((float) ((int) (distance / 100))) / 10 + " km");


            } else {

                holder.placerange.setVisibility(View.INVISIBLE);

            }

            if (mplace.topphotos.size() > 0)
                new PreSets().setImage(getContext(), holder.placeimage, mplace.topphotos.get(0));
            else new PreSets().setImage(getContext(), holder.placeimage, "");

            holder.placename.setText(LanguagePack.getLanguage(mplace.name,lan));
            holder.placeexplanation.setText(LanguagePack.getLanguage(mplace.explain,lan));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), PlaceView.class);
                    intent.putExtra("id", mplace.id);

                    getActivity().startActivity(intent);
                }
            });

            int opentime = mplace.getOpentime().get(today);
            int closetime = mplace.getClosetime().get(today);
            int now = (Calendar.getInstance().get(Calendar.HOUR) * 100) + (Calendar.getInstance().get(Calendar.MINUTE));
            if (Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM) now = now + 1200;

            Boolean isopen;

            if (opentime < closetime) {

                if (opentime < now && now < closetime) { //ACIK ISE

                    isopen = true;

                } else {
                    isopen = false;

                }

            } else {

                if (opentime < now || now < closetime) { //ACIK ISE

                    isopen = true;

                } else {
                    isopen = false;

                }

            }

            if (isopen) {

                holder.close_tv.setText(languagePack.opentext[lan]);
                holder.close_tv.setBackgroundResource(R.drawable.shape_new_edittext);


            } else {
                holder.close_tv.setText(languagePack.closetext[lan]);
                holder.close_tv.setBackgroundResource(R.drawable.shape_close);
            }



            holder.new_tv.setText(languagePack.neww[lan]);
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

            ImageView placeimage;
            TextView placerange;
            EditText placename, placeexplanation;
            TextView new_tv, close_tv;

            public PlaceActivityViewHolder(View itemView) {
                super(itemView);

                placeimage = itemView.findViewById(R.id.custum_place_imageView);
                placerange = itemView.findViewById(R.id.custum_place_range);
                placename = itemView.findViewById(R.id.custum_place_name);
                placeexplanation = itemView.findViewById(R.id.custum_place_explain);

                new_tv = itemView.findViewById(R.id.new_tv2);
                close_tv = itemView.findViewById(R.id.close_tv2);
                close_tv.setVisibility(View.VISIBLE);

            }
        }
    }


}