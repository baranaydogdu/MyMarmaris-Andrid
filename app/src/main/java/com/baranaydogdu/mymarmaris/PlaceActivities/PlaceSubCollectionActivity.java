package com.baranaydogdu.mymarmaris.PlaceActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.TextView;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PlaceSubCollectionActivity extends AppCompatActivity {

    ImageView collection_image;
    TextView collection_name;
    RecyclerView recyclerView;

    PlaceCollectionForRealm maincollection;
    RealmResults<PlaceCollectionForRealm> subcollectionlist;

    PlaceSubCollectionAdapter placeSubCollectionAdapter;

    SharedPreferences sharedPreferences;
    Realm realm ;
    String mainId;
    int lan =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_sub_collection);

        collection_name=findViewById(R.id.collection_name);
        collection_image=findViewById(R.id.place_sub_collection_image);
        recyclerView=findViewById(R.id.place_sub_collection_recyclerview);

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language",0);

        Intent intent=getIntent();
        realm = Realm.getDefaultInstance();
        mainId = intent.getStringExtra("id");
        maincollection = realm.where(PlaceCollectionForRealm.class).equalTo("id", mainId).findFirst();
        subcollectionlist = realm.where(PlaceCollectionForRealm.class).equalTo("masterid", mainId).findAll().sort("sortnumber");

        new PreSets().setImage(this,collection_image,maincollection.icon);
        collection_name.setText(LanguagePack.getLanguage(maincollection.name,lan));

        placeSubCollectionAdapter = new PlaceSubCollectionAdapter(this);
        recyclerView.setAdapter(placeSubCollectionAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


    }

    public class PlaceSubCollectionAdapter extends RecyclerView.Adapter<PlaceSubCollectionAdapter.PlaceViewHolder>{

        private Activity activity;

        public PlaceSubCollectionAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_collection_layout,
                    parent, false);
            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceViewHolder holder, final int position) {

            final PlaceCollectionForRealm collection=subcollectionlist.get(position);

            new PreSets().setImage(activity,holder.mPlace,collection.icon);
            holder.tv.setText(LanguagePack.getLanguage(collection.name, lan));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(activity, PlacesActivity.class);
                    intent.putExtra("id", mainId);
                    intent.putExtra("index", position);
                    activity.startActivity(intent);
                }
            });



        }

        @Override
        public int getItemCount() {
            return subcollectionlist.size();
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
}
