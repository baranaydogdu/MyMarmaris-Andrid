package com.baranaydogdu.mymarmaris.PlaceActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.NotificationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.EventActivity;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.NotificationCreate;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.Services.MyFirebaseMessagingService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class FragmentMainCollections extends Fragment {

    public static FragmentMainCollections instance;
    RecyclerView place_recyclerview,event_recyclerview;
    LinearLayout mainlinear;

    public static PlaceCollectionAdapter placeCollectionAdapter;
    public static ArrayList<PlaceCollectionClass> mcollectionlist;

    public TodayEventAdapter todayeventAdapter;
    public ArrayList<EventClass> eventsList;
    public ArrayList<EventClass> extendeteventlist_for_sort;
    Activity activity;
    TextView today_events_edx;
    SharedPreferences sharedPreferences;
    int selected_language;

    public static FragmentMainCollections getInstance() {

        if (instance == null) {

            FragmentMainCollections.instance = new FragmentMainCollections();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maincollections, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {  //MAGIC IS HERE

        activity=getActivity();
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);

        mainlinear=view.findViewById(R.id.mainlinear);
        place_recyclerview=view.findViewById(R.id.homefragment_places_recyclerview);
        event_recyclerview=view.findViewById(R.id.homefragment_event_recyclerview);
        today_events_edx=view.findViewById(R.id.today_events_edx);


        mcollectionlist=new ArrayList<>();

        placeCollectionAdapter = new PlaceCollectionAdapter(getActivity(),mcollectionlist);
        place_recyclerview.setAdapter(placeCollectionAdapter);
        place_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),3));


        eventsList=new ArrayList<>();
        extendeteventlist_for_sort=new ArrayList<>();

        todayeventAdapter = new TodayEventAdapter();
        event_recyclerview.setAdapter(todayeventAdapter);
        event_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        String[] actualevents={"Actual Events","Güncel Etkinlikler","Actual Events","Actual Events","Actual Events"};
        today_events_edx.setText(actualevents[selected_language]);

    }



    @Override
    public void onResume() {
        super.onResume();
        setmainCollections(getActivity());
        settodayevent();

    }

    public void settodayevent() {

        eventsList.clear();
        extendeteventlist_for_sort.clear();

        for (EventClass single_event:PreSets.getAll_Events_from_database(activity)){

            eventsList.add(single_event);

        }

        for (int i=0;i<eventsList.size();i++){

            EventClass fulldays_event=PreSets.get_Event(activity,eventsList.get(i).getId());

            for (int j=0;j<fulldays_event.seethe_nextdays().size();j++){

                EventTimes singletime=fulldays_event.seethe_nextdays().get(j);

                if (!(new Date(singletime.getStart_time())).
                        after(new Date(Calendar.getInstance().getTime().getTime()+PreSets.TODAY_HOURS_MEANS))){

                    EventClass extented_sinle_event=PreSets.get_Event(activity,fulldays_event.getId());
                    extented_sinle_event.times.clear();
                    extented_sinle_event.times.add(singletime);

                    extendeteventlist_for_sort.add(extented_sinle_event);
                }

            }

        }


        Collections.sort(extendeteventlist_for_sort, new Comparator<EventClass>() {
            @Override
            public int compare(EventClass o1, EventClass o2) {
                return (new Date(o1.seethe_nextdays().get(0).getStart_time())).
                        compareTo(new Date(o2.seethe_nextdays().get(0).getStart_time()));
            }
        });





        if (extendeteventlist_for_sort.size()==0) today_events_edx.setVisibility(View.INVISIBLE);
        else today_events_edx.setVisibility(View.VISIBLE);


        todayeventAdapter.notifyDataSetChanged();
    }

    public static void setmainCollections(Context activity){

        mcollectionlist.clear();
        for (PlaceCollectionClass maincollection:PreSets.getAll_MainCollections(activity)){

            mcollectionlist.add(maincollection);

        }

        placeCollectionAdapter.notifyDataSetChanged();

    }


    public class PlaceCollectionAdapter extends RecyclerView.Adapter<PlaceCollectionAdapter.PlaceViewHolder> {

        private Activity activity;
        private ArrayList<PlaceCollectionClass> mcollectionlist;

        public PlaceCollectionAdapter(Activity activity, ArrayList<PlaceCollectionClass> mcollectionlist) {
            this.activity = activity;
            this.mcollectionlist = mcollectionlist;
        }

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_collection_layout,
                    parent, false);
            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceViewHolder holder, final int position) {

            final PlaceCollectionClass collection = mcollectionlist.get(position);

            PreSets.setImageIcon(holder.mPlace, collection.getIconnumber());

            holder.tv.setText(PreSets.setlanguage_name(activity, collection));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (collection.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB) {
                        Intent intent = new Intent(activity, PlaceSubCollectionActivity.class);
                        intent.putExtra("id", collection.getId());
                        activity.startActivity(intent);

                    }else if(collection.getType()==PlaceCollectionClass.COLLECTION_NON_SUB){
                        Intent intent = new Intent(activity, PlacesActivity.class);
                        ArrayList<String> idlist=new ArrayList<>();idlist.add(collection.getId());
                        intent.putStringArrayListExtra("id", idlist);
                        activity.startActivity(intent);

                    }else if(collection.getType()==PlaceCollectionClass.INSIDE_LINKKED_COLLECTION){

                        Intent intent = new Intent(activity, InsideLinkCollectionActivity.class);
                        String url = collection.getLink();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        intent.putExtra("url", url);
                        activity.startActivity(intent);

                    }else if(collection.getType()==PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION) {

                        String url = collection.getLink();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    }else if (collection.getType()==PlaceCollectionClass.PHARMACY){

                        Intent intent = new Intent(activity, PharmaciesActivity.class);
                        activity.startActivity(intent);


                    } else if (collection.getType()==PlaceCollectionClass.ACTIVITIES){

                        Intent intent = new Intent(activity, EventActivity.class);
                        activity.startActivity(intent);

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return mcollectionlist.size();
        }

        public class PlaceViewHolder extends RecyclerView.ViewHolder {

            ImageView mPlace;
            EditText tv;

            public PlaceViewHolder(View itemView) {
                super(itemView);

                mPlace = itemView.findViewById(R.id.place_custum_iconsource);
                tv = itemView.findViewById(R.id.custom_place_edittext);
            }
        }
    }


    public class TodayEventAdapter extends RecyclerView.Adapter<TodayEventAdapter.TodayEventViewHolder>{

        public TodayEventAdapter() {

        }

        @Override
        public TodayEventAdapter.TodayEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_event_item_layout,
                    parent, false);
            return new TodayEventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TodayEventViewHolder holder, int position) {

            final EventClass event=extendeteventlist_for_sort.get(position);

            holder.mainlineer.removeView(holder.date_carvdiew);
            holder.mainlineer.removeView(holder.event_cons);

            if (position!=0){

                if (PreSets.getlanguages_date(activity,event.getTimes().get(0).getStart_time())
                        .equals(PreSets.getlanguages_date(activity,extendeteventlist_for_sort.get(position-1).getTimes().get(0).getStart_time()))){

                    holder.mainlineer.addView(holder.event_cons);

                } else {
                    holder.mainlineer.addView(holder.date_carvdiew);
                    holder.mainlineer.addView(holder.event_cons);
                }

            } else {
                holder.mainlineer.addView(holder.date_carvdiew);
                holder.mainlineer.addView(holder.event_cons);
            }


            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date(event.getTimes().get(0).getStart_time()));
            String minute;
            if (calendar.get(Calendar.MINUTE)<10){

                minute="0"+calendar.get(Calendar.MINUTE);
            }else minute=""+calendar.get(Calendar.MINUTE);

            holder.time_edx.setText(PreSets.gethour(event.seethe_nextdays().get(0).getStart_time())+":"+ minute);

            holder.event_name.setText(PreSets.setlanguage_name(activity,event));
            holder.event_explain.setText(PreSets.setlanguage_explain(activity,event));
            PreSets.set_MAIN_photo(holder.event_logo,event.getId());
            holder.date_tv.setText(PreSets.getlanguages_date(activity,event.seethe_nextdays().get(0).getStart_time()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, EventView.class);
                    intent.putExtra("id",event.getId());
                    startActivity(intent);
                }
            });


            holder.link_carvdiew.setVisibility(View.VISIBLE);

            if (event.getLinked_management().equals("default") && event.getLinked_place().equals("default")){

                holder.link_carvdiew.setEnabled(false);

                if (!PreSets.setlanguage_unlinked_place(activity,event).equals("")){

                    holder.linked_edx.setText(PreSets.setlanguage_unlinked_place(activity,event));

                } else if (!PreSets.setlanguage_unlinked_management(activity,event).equals("")){

                    holder.linked_edx.setText(PreSets.setlanguage_unlinked_management(activity,event));

                } else holder.link_carvdiew.setVisibility(View.INVISIBLE);

            } else {

                holder.link_carvdiew.setEnabled(true);

                final PlaceClass linkedplace;

                try{

                    if (!event.getLinked_place().equals("default")) {

                        linkedplace=PreSets.getPlace(activity,event.getLinked_place());
                        holder.linked_edx.setText(PreSets.setlanguage_name(activity,linkedplace));

                    } else {
                        linkedplace=PreSets.getPlace(activity,event.getLinked_management());
                        holder.linked_edx.setText(PreSets.setlanguage_name(activity,linkedplace));
                    }

                    holder.link_carvdiew.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ArrayList<String> idlist=new ArrayList<>();
                            Intent intent=new Intent(activity, PlaceView.class);
                            intent.putExtra("id",linkedplace.getId());
                            startActivity(intent);
                        }
                    });
                } catch (Exception e){
                    holder.link_carvdiew.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

            }
        }

        @Override
        public int getItemCount() {
            return extendeteventlist_for_sort.size();
        }

        public class TodayEventViewHolder extends RecyclerView.ViewHolder {

            TextView date_tv;
            ImageView event_logo;
            EditText time_edx,event_name,event_explain;
            LinearLayout mainlineer;
            ConstraintLayout event_cons;
            CardView date_carvdiew,link_carvdiew;
            EditText linked_edx;

            public TodayEventViewHolder(View itemView) {
                super(itemView);

                event_logo = itemView.findViewById(R.id.custum_place_imageView);
                date_tv    = itemView.findViewById(R.id.date_tv);
                time_edx   = itemView.findViewById(R.id.custum_event_hour);
                event_name = itemView.findViewById(R.id.custum_place_name);
                event_explain = itemView.findViewById(R.id.custum_place_explain);
                mainlineer = itemView.findViewById(R.id.mainlineer);
                event_cons = itemView.findViewById(R.id.event_cons);
                date_carvdiew=itemView.findViewById(R.id.date_carvdiew);
                link_carvdiew=itemView.findViewById(R.id.link_carvdiew);
                linked_edx=itemView.findViewById(R.id.linked_edx);

            }
        }
    }




}
