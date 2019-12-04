package com.baranaydogdu.mymarmaris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class EventActivity extends AppCompatActivity {

    RecyclerView events_recyclerview;
    ArrayList<EventClass> eventsList;
    Activity activity;
    public EventAdapter eventAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<EventClass> extendeteventlist_for_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        sharedPreferences =this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        events_recyclerview=findViewById(R.id.activity_fragment_activity_recyclerview);

        activity=this;
        eventsList=new ArrayList<>();

        eventAdapter= new EventAdapter();
        events_recyclerview.setAdapter(eventAdapter);
        events_recyclerview.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEvents();
    }


    public void setEvents() {

        eventsList.clear();

        for (EventClass single_event:PreSets.getAll_Events_from_database(activity)){

            eventsList.add(single_event);

        }

        extendeteventlist_for_sort=new ArrayList<>();
        extendeteventlist_for_sort.clear();

        for (int i=0;i<eventsList.size();i++){

            EventClass fulldays_event=PreSets.get_Event(activity,eventsList.get(i).getId());

            for (int j=0;j<fulldays_event.seethe_nextdays().size();j++){

                EventTimes singletime=fulldays_event.seethe_nextdays().get(j);

                EventClass extented_sinle_event=PreSets.get_Event(activity,fulldays_event.getId());
                extented_sinle_event.times.clear();
                extented_sinle_event.times.add(singletime);

                extendeteventlist_for_sort.add(extented_sinle_event);

            }

        }


        Collections.sort(extendeteventlist_for_sort, new Comparator<EventClass>() {
            @Override
            public int compare(EventClass o1, EventClass o2) {
                return (new Date(o1.seethe_nextdays().get(0).getStart_time())).
                        compareTo(new Date(o2.seethe_nextdays().get(0).getStart_time()));
            }
        });

        eventAdapter.notifyDataSetChanged();

    }


    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

        public EventAdapter() {

        }

        @Override
        public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_event_item_layout,
                    parent, false);
            return new EventAdapter.EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {

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


                try{


                    holder.link_carvdiew.setEnabled(true);

                    final PlaceClass linkedplace;

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

                            Intent intent=new Intent(activity, PlaceView.class);
                            intent.putExtra("id",linkedplace.getId());
                            startActivity(intent);
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                    holder.link_carvdiew.setVisibility(View.INVISIBLE);
                }

            }
        }

        @Override
        public int getItemCount() {
            return extendeteventlist_for_sort.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {

            TextView date_tv;
            ImageView event_logo;
            EditText time_edx,event_name,event_explain;
            LinearLayout mainlineer;
            ConstraintLayout event_cons;
            CardView date_carvdiew,link_carvdiew;
            EditText linked_edx;

            public EventViewHolder(View itemView) {
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
