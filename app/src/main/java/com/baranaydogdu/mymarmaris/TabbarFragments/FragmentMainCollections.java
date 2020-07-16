package com.baranaydogdu.mymarmaris.TabbarFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.baranaydogdu.mymarmaris.Classes.EventForRealm;
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionForRealm;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.EventActivity;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PlaceActivities.InsideLinkCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PharmaciesActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceSubCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlacesActivity;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class FragmentMainCollections extends Fragment {

    LinearLayout mainlinear;
    RecyclerView place_recyclerview, event_recyclerview;
    TextView today_events_edx;

    PlaceCollectionAdapter placeCollectionAdapter = new PlaceCollectionAdapter();
    RealmResults<PlaceCollectionForRealm> mcollectionlist;

    TodayEventAdapter todayeventAdapter = new TodayEventAdapter();
    ArrayList<EventClass> eventsList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    int lan;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
    ConstraintLayout progress_cons, progressevent_cons;
    Realm realm= Realm.getDefaultInstance();

    public FragmentMainCollections(ConstraintLayout progress_cons, ConstraintLayout progressevent_cons) {
        this.progress_cons = progress_cons;
        this.progressevent_cons = progressevent_cons;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maincollections, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {  //MAGIC IS HERE

        sharedPreferences = getActivity().getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);

        mainlinear = view.findViewById(R.id.mainlinear);
        place_recyclerview = view.findViewById(R.id.homefragment_places_recyclerview);
        event_recyclerview = view.findViewById(R.id.homefragment_event_recyclerview);
        today_events_edx = view.findViewById(R.id.today_events_edx);

        place_recyclerview.setAdapter(placeCollectionAdapter);
        place_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        event_recyclerview.setAdapter(todayeventAdapter);
        event_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        today_events_edx.setText(new LanguagePack().actualevents[lan]);
        setCollectionList();
        settodayevent();
        checkChanges();

    }


    public void checkChanges() {

        mref.child("Places_version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    if (!sharedPreferences.getString("place_version", "").equals(dataSnapshot.getValue().toString())) {
                        System.out.println("İşletme Değişiklik var");
                        getFirebasePlaceData(dataSnapshot.getValue().toString());
                        progress_cons.setVisibility(View.VISIBLE);
                    } else {
                        progress_cons.setVisibility(View.INVISIBLE);
                        System.out.println("Places_version : " + dataSnapshot.getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mref.child("Activityversion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    if (!sharedPreferences.getString("Activityversion", "").equals(dataSnapshot.getValue().toString())) {
                        System.out.println("Events Değişiklik var");
                        getFirebaseEventData(dataSnapshot.getValue().toString());
                        progressevent_cons.setVisibility(View.VISIBLE);
                    } else {
                        System.out.println("Activityversion : " + dataSnapshot.getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getFirebasePlaceData(final String version) {

        mref.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    final RealmResults<PlaceForRealm> allplaces = realm.where(PlaceForRealm.class).findAll();
                    final RealmResults<PlaceCollectionForRealm> allcollections = realm.where(PlaceCollectionForRealm.class).findAll();

                    realm.beginTransaction();
                    allplaces.deleteAllFromRealm();
                    allcollections.deleteAllFromRealm();

                    for (DataSnapshot mainds : dataSnapshot.getChildren()) {
                        PlaceCollectionClass maincollection = mainds.child("info").getValue(PlaceCollectionClass.class);
                        Boolean isMain = true;

                        for (DataSnapshot subds : mainds.child("subs").getChildren()) {
                            PlaceCollectionClass subcollection = subds.child("info").getValue(PlaceCollectionClass.class);
                            Boolean isSub = true;

                            for (DataSnapshot placesds : subds.child("places").getChildren()) {

                                PlaceClass place = placesds.getValue(PlaceClass.class);
                                if (isMain) {
                                    realm.copyToRealm(new PlaceCollectionForRealm("main", maincollection));
                                    isMain = false;
                                }
                                if (isSub && maincollection.type == 0) {
                                    realm.copyToRealm(new PlaceCollectionForRealm(maincollection.id, subcollection));
                                    isSub = false;
                                }

                                if (maincollection.type < 2) {
                                    realm.copyToRealm(new PlaceForRealm(maincollection.id, subcollection.id, place));
                                }
                            }

                        }

                    }
                    realm.commitTransaction();
                    sharedPreferences.edit().putString("place_version", version).commit();
                    setCollectionList();
                } else {
                    System.out.println("Place Data Null");
                }
                progress_cons.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress_cons.setVisibility(View.INVISIBLE);

            }
        });

    }

    public void getFirebaseEventData(final String version) {

        mref.child("Activities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    final RealmResults<EventTimes> allTtimes = realm.where(EventTimes.class).findAll();
                    final RealmResults<EventForRealm> allEvent = realm.where(EventForRealm.class).findAll();

                    realm.beginTransaction();
                    allTtimes.deleteAllFromRealm();
                    allEvent.deleteAllFromRealm();

                    for (DataSnapshot mainds : dataSnapshot.getChildren()) {
                        EventClass event = mainds.getValue(EventClass.class);
                        realm.copyToRealm(new EventForRealm(event));

                    }
                    realm.commitTransaction();
                    sharedPreferences.edit().putString("Activityversion", version).commit();
                    settodayevent();
                } else {
                    System.out.println("Event Data Null");
                }
                progressevent_cons.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressevent_cons.setVisibility(View.INVISIBLE);

            }
        });

    }

    public void setCollectionList() {

        mcollectionlist = realm.where(PlaceCollectionForRealm.class).equalTo("masterid", "main").findAll().sort("sortnumber");
        placeCollectionAdapter.notifyDataSetChanged();

        int linecount = mcollectionlist.size() / 3;
        if (mcollectionlist.size() % 3 != 0) linecount++;
        place_recyclerview.getLayoutParams().height = PreSets.getDp(getContext(), linecount * 150);

    }

    public void settodayevent() {

        eventsList.clear();

        RealmResults<EventForRealm> eventForRealms = realm.where(EventForRealm.class).findAll();
        for (EventForRealm eventRealm : eventForRealms) {
            for (EventClass singleTimeEvent : eventRealm.todayMultiple()) {
                eventsList.add(singleTimeEvent);
            }
        }

        Collections.sort(eventsList, new Comparator<EventClass>() {
            @Override
            public int compare(EventClass o1, EventClass o2) {
                return (new Date(o1.times.get(0).getStart_time())).compareTo(new Date(o2.times.get(0).getStart_time()));
            }
        });

        if ( eventsList.size() == 0)  {
            today_events_edx.setVisibility(View.INVISIBLE);
            event_recyclerview.setVisibility(View.INVISIBLE);
        } else {
            today_events_edx.setVisibility(View.VISIBLE);
            event_recyclerview.setVisibility(View.VISIBLE);
        }

        event_recyclerview.getLayoutParams().height = PreSets.getDp(getContext(), eventsList.size() * 290);
        todayeventAdapter.notifyDataSetChanged();

    }



    public class PlaceCollectionAdapter extends RecyclerView.Adapter<PlaceCollectionAdapter.PlaceViewHolder> {


        public PlaceCollectionAdapter() {
        }

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_collection_layout,
                    parent, false);
            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceViewHolder holder, final int position) {

            final PlaceCollectionForRealm collection = mcollectionlist.get(position);

            new PreSets().setImage(getContext(), holder.mPlace, collection.icon);

            holder.tv.setText(LanguagePack.getLanguage(collection.name, lan));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (collection.getType() == PlaceCollectionClass.COLLECTION_WITH_SUB) {
                        Intent intent = new Intent(getActivity(), PlaceSubCollectionActivity.class);
                        intent.putExtra("id", collection.getId());
                        getActivity().startActivity(intent);

                    } else if (collection.getType() == PlaceCollectionClass.COLLECTION_NON_SUB) {
                        Intent intent = new Intent(getActivity(), PlacesActivity.class);
                        intent.putExtra("id", collection.getId());
                        getActivity().startActivity(intent);

                    } else if (collection.getType() == PlaceCollectionClass.INSIDE_LINKKED_COLLECTION) {

                        Intent intent = new Intent(getActivity(), InsideLinkCollectionActivity.class);
                        intent.putExtra("url", collection.getLink());
                        getActivity().startActivity(intent);

                    } else if (collection.getType() == PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(collection.getLink())));

                    } else if (collection.getType() == PlaceCollectionClass.PHARMACY) {

                        getActivity().startActivity(new Intent(getActivity(), PharmaciesActivity.class));

                    } else if (collection.getType() == PlaceCollectionClass.ACTIVITIES) {
                        getActivity().startActivity(new Intent(getActivity(), EventActivity.class));
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

    public class TodayEventAdapter extends RecyclerView.Adapter<TodayEventAdapter.TodayEventViewHolder> {

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
            String minute =  "" + calendar.get(Calendar.MINUTE);
            if (calendar.get(Calendar.MINUTE) < 10)   minute = "0" + minute;

            holder.time_edx.setText(LanguagePack.gethour(event.times.get(0).getStart_time())+":"+ minute);

            holder.event_name.setText(LanguagePack.getLanguage(event.name, lan));
            holder.event_explain.setText(LanguagePack.getLanguage(event.explain, lan));

            if ( event.topphotos.size() > 0)
            new PreSets().setImage(getActivity(),holder.event_logo,event.topphotos.get(0));
            else new PreSets().setImage(getActivity(),holder.event_logo,"");

            holder.date_tv.setText(LanguagePack.getlanguages_date(getActivity(),event.times.get(0).getStart_time()));

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

                        final PlaceForRealm linkedplace = realm.where(PlaceForRealm.class).equalTo("id",event.linked_place).findFirst();

                        holder.linked_edx.setText(LanguagePack.getLanguage(linkedplace.name, lan));
                        holder.link_carvdiew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Intent intent = new Intent(getActivity(), PlaceView.class);
                                    intent.putExtra("id", linkedplace.getId());
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    System.out.println("Exception ERROR : "+ ex.getLocalizedMessage());
                                }

                            }
                        });
                    } else {
                        final PlaceForRealm linked_management = realm.where(PlaceForRealm.class).equalTo("id",event.linked_management).findFirst();
                        holder.linked_edx.setText(LanguagePack.getLanguage(linked_management.name, lan));
                        holder.link_carvdiew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Intent intent = new Intent(getActivity(), PlaceView.class);
                                    intent.putExtra("id", linked_management.getId());
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    System.out.println("Exception ERROR2 : "+ ex.getLocalizedMessage());
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

        public class TodayEventViewHolder extends RecyclerView.ViewHolder {

            TextView date_tv;
            ImageView event_logo;
            EditText time_edx, event_name, event_explain;
            LinearLayout mainlineer;
            ConstraintLayout event_cons;
            CardView date_carvdiew, link_carvdiew;
            EditText linked_edx;

            public TodayEventViewHolder(View itemView) {
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
