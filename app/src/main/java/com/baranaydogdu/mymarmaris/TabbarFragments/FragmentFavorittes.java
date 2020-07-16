package com.baranaydogdu.mymarmaris.TabbarFragments;

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

import com.baranaydogdu.mymarmaris.Classes.FavoritteRealm;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView.LOCATIONMILISECOND;

public class FragmentFavorittes extends Fragment implements LocationListener {

    RecyclerView recyclerView;
    public PlacesActivityAdapter placesActivityAdapter = new PlacesActivityAdapter();
    SharedPreferences sharedPreferences;
    int lan = 0;
    Activity activity;
    LocationManager locationManager;
    TextView favlist_tv;
    int today;
    RealmResults<FavoritteRealm> idList;
    ArrayList<PlaceClass> placeList = new ArrayList<>();
    Location lastLoc;
    LanguagePack languagePack = new LanguagePack();

    public FragmentFavorittes() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorittes, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (today == 1) today = 6;
        else today = today - 2;

        activity = getActivity();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);

        recyclerView = view.findViewById(R.id.places_recyclerview);
        recyclerView.setAdapter(placesActivityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        favlist_tv = view.findViewById(R.id.favorite_list_tv);

        favlist_tv.setText(new LanguagePack().favlisttext[lan]);
    }


    public void setlist() {

        placeList.clear();
        Realm realm = Realm.getDefaultInstance();

        idList = realm.where(FavoritteRealm.class).findAll();
        for (FavoritteRealm fav : idList) {
            PlaceForRealm favRealm = realm.where(PlaceForRealm.class).equalTo("id", fav.favid).findFirst();
            if (favRealm != null) placeList.add(favRealm.toPlace());
        }
        onLocationChanged(lastLoc);
        placesActivityAdapter.notifyDataSetChanged();
    }

    Boolean isStartted = true;
    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && isStartted) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10 * LOCATIONMILISECOND, 0, this);
            isStartted = false;
        }
        setlist();

    }


    @Override
    public void onLocationChanged(Location location) {
        lastLoc = location;

        if (lastLoc != null) {
            for (PlaceClass place : placeList) {
                if (place.location.lat != 0 && place.location.log != 0) {
                    Location loc = new Location("");
                    loc.setLatitude(place.location.lat);
                    loc.setLongitude(place.location.log);
                    place.distance = loc.distanceTo(lastLoc);
                }
            }
        }
        placesActivityAdapter.notifyDataSetChanged();
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

            final PlaceClass mplace = placeList.get(position);

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

            holder.placename.setText(LanguagePack.getLanguage(mplace.name, lan));
            holder.placeexplanation.setText(LanguagePack.getLanguage(mplace.explain, lan));

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

            return placeList.size();

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
