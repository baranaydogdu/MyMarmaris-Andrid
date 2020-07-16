package com.baranaydogdu.mymarmaris.AdminPages;

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
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
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

    //SELECT FOR NOTIFICATION
    public static final int SELECT_MAIN_COLLECTION_REQUEST=2;
    public static final int SELECT_SUB_COLLECTION_REQUEST=3;
    public static final int SELECT_EVENT_REQUEST=4;
    public static final int SELECT_PLACE_REQUEST=5;

    int requestcode;
    SharedPreferences sharedPreferences;
    int lan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clon);

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);


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

        if (requestcode==SELECT_PLACE_REQUEST){       //PLACE ISE

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
                                return LanguagePack.getLanguage(o1.name,lan).compareTo(LanguagePack.getLanguage(o2.name,lan));
                            }
                        });
                        myadapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }



            });

        } else if (requestcode==SELECT_EVENT_REQUEST){            //EVENT ISE

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
                                return LanguagePack.getLanguage(o1.name,lan).compareTo(LanguagePack.getLanguage(o2.name,lan));
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
                                return LanguagePack.getLanguage(o1.name,lan).compareTo(LanguagePack.getLanguage(o2.name,lan));
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
                                return LanguagePack.getLanguage(o1.name,lan).compareTo(LanguagePack.getLanguage(o2.name,lan));
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
                                return LanguagePack.getLanguage(o1.name,lan).compareTo(LanguagePack.getLanguage(o2.name,lan));
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

            if (requestcode==SELECT_PLACE_REQUEST){

                holder.tv.setText(LanguagePack.getLanguage(placelist.get(position).name,lan));
                if (placelist.get(position).topphotos.size() > 0)
                new PreSets().setImage(activity,holder.mPlace,placelist.get(position).topphotos.get(0));
                else  new PreSets().setImage(activity,holder.mPlace,"");

            }else if (requestcode==SELECT_EVENT_REQUEST){

                holder.tv.setText(LanguagePack.getLanguage(eventlist.get(position).name,lan));

                if (eventlist.get(position).topphotos.size() > 0)
                    new PreSets().setImage(activity,holder.mPlace,eventlist.get(position).topphotos.get(0));
                else  new PreSets().setImage(activity,holder.mPlace,"");


            }else {     //  requestcode==SELECT_MAIN_COLLECTION_REQUEST || requestcode==SELECT_SUB_COLLECTION_REQUEST ){

                holder.tv.setText(LanguagePack.getLanguage(collectionlists.get(position).name,lan));
                new PreSets().setImage(activity,holder.mPlace,collectionlists.get(position).icon);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     if (requestcode==SELECT_MAIN_COLLECTION_REQUEST){

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

            if (requestcode==SELECT_PLACE_REQUEST){

                return placelist.size();

            }else if (requestcode==SELECT_EVENT_REQUEST ){

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