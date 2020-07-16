package com.baranaydogdu.mymarmaris.SearcOperations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class SerachEventFragment extends Fragment {

    RecyclerView events_recyclerview;
    ArrayList<EventClass> eventsList = new ArrayList<>();
    public EventAdapter eventAdapter= new EventAdapter();
    SharedPreferences sharedPreferences;
    Realm realm = Realm.getDefaultInstance();
    int lan;

    public SerachEventFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serach_single, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);
        events_recyclerview = view.findViewById(R.id.recyclerview);
        events_recyclerview.setAdapter(eventAdapter);
        events_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    public void setEvents(ArrayList<EventClass> searchedEventList) {

        eventsList = searchedEventList;

        eventAdapter.notifyDataSetChanged();

    }


    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        public EventAdapter() {

        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_event_item_layout,
                    parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

            final EventClass event = eventsList.get(position);

            holder.mainlineer.removeView(holder.date_carvdiew);
            holder.mainlineer.removeView(holder.event_cons);

            if (position != 0) {

                if (LanguagePack.getlanguages_date(getActivity(), event.getTimes().get(0).getStart_time())
                        .equals(LanguagePack.getlanguages_date(getActivity(), eventsList.get(position - 1).getTimes().get(0).getStart_time()))) {

                    holder.mainlineer.addView(holder.event_cons);

                } else {
                    holder.mainlineer.addView(holder.date_carvdiew);
                    holder.mainlineer.addView(holder.event_cons);
                }

            } else {
                holder.mainlineer.addView(holder.date_carvdiew);
                holder.mainlineer.addView(holder.event_cons);
            }


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(event.getTimes().get(0).getStart_time()));
            String minute = "" + calendar.get(Calendar.MINUTE);
            if (calendar.get(Calendar.MINUTE) < 10) minute = "0" + minute;

            holder.time_edx.setText(LanguagePack.gethour(event.times.get(0).getStart_time()) + ":" + minute);

            holder.event_name.setText(LanguagePack.getLanguage(event.name, lan));
            holder.event_explain.setText(LanguagePack.getLanguage(event.explain, lan));

            if (event.topphotos.size() > 0)
                new PreSets().setImage(getActivity(), holder.event_logo, event.topphotos.get(0));
            else new PreSets().setImage(getActivity(), holder.event_logo, "");

            holder.date_tv.setText(LanguagePack.getlanguages_date(getActivity(), event.times.get(0).getStart_time()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventView.class);
                    intent.putExtra("id", event.getId());
                    startActivity(intent);
                }
            });

            holder.link_carvdiew.setVisibility(View.VISIBLE);

            if (event.getLinked_management().equals("") && event.getLinked_place().equals("")) {

                holder.link_carvdiew.setEnabled(false);

                if (!LanguagePack.getLanguage(event.unlinked_place, lan).equals("")) {

                    holder.linked_edx.setText(LanguagePack.getLanguage(event.unlinked_place, lan));

                } else if (!LanguagePack.getLanguage(event.unlinked_management, lan).equals("")) {

                    holder.linked_edx.setText(LanguagePack.getLanguage(event.unlinked_management, lan));

                } else holder.link_carvdiew.setVisibility(View.INVISIBLE);

            } else {

                holder.link_carvdiew.setEnabled(true);

                try {

                    if (!event.getLinked_place().equals("")) {

                        final PlaceForRealm linkedplace = realm.where(PlaceForRealm.class).equalTo("id", event.linked_place).findFirst();

                        holder.linked_edx.setText(LanguagePack.getLanguage(linkedplace.name, lan));
                        holder.link_carvdiew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Intent intent = new Intent(getActivity(), PlaceView.class);
                                    intent.putExtra("id", linkedplace.getId());
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    System.out.println("Exception ERROR : " + ex.getLocalizedMessage());
                                }

                            }
                        });
                    } else {
                        final PlaceForRealm linked_management = realm.where(PlaceForRealm.class).equalTo("id", event.linked_management).findFirst();
                        holder.linked_edx.setText(LanguagePack.getLanguage(linked_management.name, lan));
                        holder.link_carvdiew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Intent intent = new Intent(getActivity(), PlaceView.class);
                                    intent.putExtra("id", linked_management.getId());
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    System.out.println("Exception ERROR2 : " + ex.getLocalizedMessage());
                                }

                            }
                        });
                    }


                } catch (Exception e) {
                    holder.link_carvdiew.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

            }
        }

        @Override
        public int getItemCount() {
            return eventsList.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {

            TextView date_tv;
            ImageView event_logo;
            EditText time_edx, event_name, event_explain;
            LinearLayout mainlineer;
            ConstraintLayout event_cons;
            CardView date_carvdiew, link_carvdiew;
            EditText linked_edx;

            public EventViewHolder(View itemView) {
                super(itemView);

                event_logo = itemView.findViewById(R.id.custum_place_imageView);
                date_tv = itemView.findViewById(R.id.date_tv);
                time_edx = itemView.findViewById(R.id.custum_event_hour);
                event_name = itemView.findViewById(R.id.custum_place_name);
                event_explain = itemView.findViewById(R.id.custum_place_explain);
                mainlineer = itemView.findViewById(R.id.mainlineer);
                event_cons = itemView.findViewById(R.id.event_cons);
                date_carvdiew = itemView.findViewById(R.id.date_carvdiew);
                link_carvdiew = itemView.findViewById(R.id.link_carvdiew);
                linked_edx = itemView.findViewById(R.id.linked_edx);

            }
        }
    }


}
