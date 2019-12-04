package com.baranaydogdu.mymarmaris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.NotificationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.EventsEdits.EventsEditAddorEdit;
import com.baranaydogdu.mymarmaris.PlaceEdits.PlaceAddorEditSinglePlace;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SelectClon extends AppCompatActivity {

    Intent intent;
    PlaceCollectionClass maincollection,subcollection;
    ArrayList<PlaceClass> placelist;
    ArrayList<EventClass> eventlist;
    ArrayList<PlaceCollectionClass> collectionlists;


    RecyclerView recyclerView;
    SelectClonAdapter myadapter;
    TextView select_tv;

    DatabaseReference mref;

    Activity activity;

    //CLON REQUESTS
    public static final int CLON_PLACE_REQUEST=0;
    public static final int CLON_EVENT_REQUEST=1;

    //SELECT LINKED PLACE AND MANAGEMENT FOR EVENTS
    public static final int SELECT_PLACE_TYPE=6;
    public static final int SELECT_MANAGEMENT_TYPE=7;

    //SELECT FOR NOTIFICATION
    public static final int SELECT_MAIN_COLLECTION_REQUEST=2;
    public static final int SELECT_SUB_COLLECTION_REQUEST=3;
    public static final int SELECT_EVENT_REQUEST=4;
    public static final int SELECT_PLACE_REQUEST=5;

    int requestcode;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clon);

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        placelist=new ArrayList<>();
        eventlist=new ArrayList<>();
        collectionlists=new ArrayList<>();

        select_tv=findViewById(R.id.select_tv);
        recyclerView=findViewById(R.id.recyclerview);

        mref= FirebaseDatabase.getInstance().getReference();

        activity=this;
        intent=getIntent();
        requestcode=intent.getIntExtra("requestcode",0);

        myadapter= new SelectClonAdapter();
        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        if (requestcode==CLON_PLACE_REQUEST || requestcode==SELECT_PLACE_TYPE ||
                requestcode==SELECT_MANAGEMENT_TYPE|| requestcode==SELECT_PLACE_REQUEST){       //PLACE ISE

            if(requestcode==CLON_PLACE_REQUEST){

                maincollection=(PlaceCollectionClass) intent.getSerializableExtra("MainCollection");
                subcollection=(PlaceCollectionClass) intent.getSerializableExtra("SubCollection");

            }

            placelist.clear();

            mref.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot maincollection_ds : dataSnapshot.getChildren()) {// MAIN LIST BEKLIYORUZ

                            PlaceCollectionClass maincollections=maincollection_ds.child("info").getValue(PlaceCollectionClass.class);
                            if (maincollections.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB ||
                                maincollections.getType()==PlaceCollectionClass.COLLECTION_NON_SUB){

                                for (DataSnapshot subcollection_ds : maincollection_ds.child("subs").getChildren()) {

                                    for (DataSnapshot place_ds : subcollection_ds.child("places").getChildren()) {

                                        PlaceClass singleplace=place_ds.getValue(PlaceClass.class);
                                        placelist.add(singleplace);

                                    }
                                }


                            }


                        }

                        Collections.sort(placelist, new Comparator<PlaceClass>() {
                            @Override
                            public int compare(PlaceClass o1, PlaceClass o2) {
                                return PreSets.setlanguage_name(activity,o1).compareTo(PreSets.setlanguage_name(activity,o2));
                            }
                        });
                        myadapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }



            });

        } else if (requestcode==CLON_EVENT_REQUEST || requestcode==SELECT_EVENT_REQUEST){            //EVENT ISE

            select_tv.setText("Select Event");

            eventlist.clear();
            mref.child("Activities").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        for (DataSnapshot eventds:dataSnapshot.getChildren()){

                            EventClass singleevent=eventds.getValue(EventClass.class);

                            eventlist.add(singleevent);

                        }

                        Collections.sort(eventlist, new Comparator<EventClass>() {
                            @Override
                            public int compare(EventClass o1, EventClass o2) {
                                return PreSets.setlanguage_name(activity,o1).compareTo(PreSets.setlanguage_name(activity,o2));
                            }
                        });
                        myadapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (requestcode==SELECT_MAIN_COLLECTION_REQUEST){

            collectionlists.clear();

            mref.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        for (DataSnapshot mainds:dataSnapshot.getChildren()){

                            collectionlists.add(mainds.child("info").getValue(PlaceCollectionClass.class));

                        }


                        Collections.sort(collectionlists, new Comparator<PlaceCollectionClass>() {
                            @Override
                            public int compare(PlaceCollectionClass o1, PlaceCollectionClass o2) {
                                return PreSets.setlanguage_name(activity,o1).compareTo(PreSets.setlanguage_name(activity,o2));
                            }
                        });

                        myadapter.notifyDataSetChanged();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        } else if (requestcode==SELECT_SUB_COLLECTION_REQUEST){

            collectionlists.clear();

            mref.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        for (DataSnapshot mainds:dataSnapshot.getChildren()){

                            PlaceCollectionClass maincollection=mainds.child("info").getValue(PlaceCollectionClass.class);

                            if (maincollection.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB){

                                for (DataSnapshot subds:mainds.child("subs").getChildren()){

                                    collectionlists.add(subds.child("info").getValue(PlaceCollectionClass.class));

                                }
                            }

                        }

                        Collections.sort(collectionlists, new Comparator<PlaceCollectionClass>() {
                            @Override
                            public int compare(PlaceCollectionClass o1, PlaceCollectionClass o2) {
                                return PreSets.setlanguage_name(activity,o1).compareTo(PreSets.setlanguage_name(activity,o2));
                            }
                        });

                        myadapter.notifyDataSetChanged();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        } else {

            collectionlists.clear();
            mref.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        for(DataSnapshot mainds:dataSnapshot.getChildren()){

                            PlaceCollectionClass singmaincollection=mainds.child("info").getValue(PlaceCollectionClass.class);
                            if (singmaincollection.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB){

                                for (DataSnapshot subds:mainds.child("subs").getChildren()){


                                    collectionlists.add(subds.getValue(PlaceCollectionClass.class));
                                }

                            }


                        }

                        Collections.sort(collectionlists, new Comparator<PlaceCollectionClass>() {
                            @Override
                            public int compare(PlaceCollectionClass o1, PlaceCollectionClass o2) {
                                return PreSets.setlanguage_name(activity,o1).compareTo(PreSets.setlanguage_name(activity,o2));
                            }
                        });

                        myadapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }



    }




    public class SelectClonAdapter extends RecyclerView.Adapter<SelectClonAdapter.SelectClonViewHolder>{


        public SelectClonAdapter() {

        }

        @Override
        public SelectClonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_placeedit_layout,
                    parent, false);
            return new SelectClonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectClonViewHolder holder, final int position) {

            if (requestcode==CLON_PLACE_REQUEST || requestcode==SELECT_PLACE_TYPE ||
                    requestcode==SELECT_MANAGEMENT_TYPE ||requestcode==SELECT_PLACE_REQUEST){

                holder.tv.setText(PreSets.setlanguage_name(activity,placelist.get(position)));
                PreSets.set_MAIN_photo(holder.mPlace,placelist.get(position).getId());

            }else if (requestcode==SELECT_EVENT_REQUEST || requestcode==CLON_EVENT_REQUEST){

                holder.tv.setText(PreSets.setlanguage_name(activity,eventlist.get(position)));
                PreSets.set_MAIN_photo(holder.mPlace,eventlist.get(position).getId());

            }else {     //  requestcode==SELECT_MAIN_COLLECTION_REQUEST || requestcode==SELECT_SUB_COLLECTION_REQUEST ){

                holder.tv.setText(PreSets.setlanguage_name(activity,collectionlists.get(position)));
                PreSets.setImageIcon(holder.mPlace,collectionlists.get(position).getIconnumber());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (requestcode == CLON_EVENT_REQUEST) {

                        Intent intent = new Intent(activity, EventsEditAddorEdit.class);
                        intent.putExtra("from", "clon");
                        intent.putExtra("event", eventlist.get(position));
                        startActivity(intent);
                        activity.finish();

                    } else if (requestcode == CLON_PLACE_REQUEST) {

                        Intent intent = new Intent(activity, PlaceAddorEditSinglePlace.class);
                        intent.putExtra("MainCollection", maincollection);
                        intent.putExtra("SubCollection", subcollection);
                        intent.putExtra("from", "clon");
                        intent.putExtra("place", placelist.get(position));
                        startActivity(intent);
                        activity.finish();

                    } else if (requestcode==SELECT_PLACE_TYPE){

                            sharedPreferences.edit().putString("place_id",placelist.get(position).getId()).apply();;
                            sharedPreferences.edit().putString("place_name",PreSets.setlanguage_name(activity,placelist.get(position))).apply();
                            sharedPreferences.edit().putBoolean("new_place_selected",true).apply();
                            activity.finish();

                    } else if (requestcode==SELECT_MANAGEMENT_TYPE) {

                        sharedPreferences.edit().putString("management_id", placelist.get(position).getId()).apply();
                        sharedPreferences.edit().putString("management_name", PreSets.setlanguage_name(activity, placelist.get(position))).apply();
                        sharedPreferences.edit().putBoolean("new_management_selected", true).apply();
                        activity.finish();


                    } else if (requestcode==SELECT_MAIN_COLLECTION_REQUEST){

                        sharedPreferences.edit().putString("click_action", NotificationClass.TO_MAIN).apply();
                        sharedPreferences.edit().putString("click_id",collectionlists.get(position).getId()).apply();
                        activity.finish();


                    } else if (requestcode==SELECT_SUB_COLLECTION_REQUEST){

                        sharedPreferences.edit().putString("click_action", NotificationClass.TO_SUB).apply();
                        sharedPreferences.edit().putString("click_id",collectionlists.get(position).getId()).apply();
                        activity.finish();


                    } else if (requestcode==SELECT_PLACE_REQUEST){

                        sharedPreferences.edit().putString("click_action", NotificationClass.TO_PLACE).apply();
                        sharedPreferences.edit().putString("click_id",placelist.get(position).getId()).apply();
                        activity.finish();


                    } else if (requestcode==SELECT_EVENT_REQUEST){

                        sharedPreferences.edit().putString("click_action", NotificationClass.TO_EVENT).apply();
                        sharedPreferences.edit().putString("click_id",eventlist.get(position).getId()).apply();
                        activity.finish();

                    }


                }




            });


        }


        @Override
        public int getItemCount() {

            if (requestcode==CLON_PLACE_REQUEST || requestcode==SELECT_PLACE_TYPE ||
                    requestcode==SELECT_MANAGEMENT_TYPE || requestcode==SELECT_PLACE_REQUEST){

                return placelist.size();

            }else if (requestcode==SELECT_EVENT_REQUEST || requestcode==CLON_EVENT_REQUEST){

                return eventlist.size();

            }else {     //  requestcode==SELECT_MAIN_COLLECTION_REQUEST || requestcode==SELECT_SUB_COLLECTION_REQUEST ){

                return collectionlists.size();
            }

        }

        public class SelectClonViewHolder extends RecyclerView.ViewHolder {


            ImageView mPlace,uparrow,downarrow,type_icon;
            TextView tv;

            public SelectClonViewHolder(View itemView) {
                super(itemView);

                mPlace      = itemView.findViewById(R.id.activity_custum_iconsource);
                tv          = itemView.findViewById(R.id.custom__activity_textview);
                uparrow     = itemView.findViewById(R.id.activity_custum_upicon);
                downarrow   = itemView.findViewById(R.id.activity_custum_downicon);
                type_icon   = itemView.findViewById(R.id.collection_type_icon);

                uparrow.setVisibility(View.INVISIBLE);
                downarrow.setVisibility(View.INVISIBLE);
                type_icon.setVisibility(View.INVISIBLE);





            }
        }
    }
}
