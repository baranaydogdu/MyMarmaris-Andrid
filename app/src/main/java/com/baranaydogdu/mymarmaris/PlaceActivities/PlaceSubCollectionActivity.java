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
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;

public class PlaceSubCollectionActivity extends AppCompatActivity {

    Intent intent;
    PlaceCollectionClass maincollection;

    ImageView collection_image;
    TextView collection_name;
    RecyclerView recyclerView;
    PlaceSubCollectionAdapter placeSubCollectionAdapter;
    ArrayList<PlaceCollectionClass> scollectionlist;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_sub_collection);

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        scollectionlist=new ArrayList<>();
        intent=getIntent();
        maincollection=PreSets.getmaincollection_from_MainCollectionId(this,intent.getStringExtra("id"));

        collection_name=findViewById(R.id.collection_name);
        collection_image=findViewById(R.id.place_sub_collection_image);
        recyclerView=findViewById(R.id.place_sub_collection_recyclerview);

        PreSets.setImageIcon(collection_image,maincollection.getIconnumber());
        collection_name.setText(PreSets.setlanguage_name(this,maincollection));

        sharedPreferences.getInt("language",0);

        placeSubCollectionAdapter = new PlaceSubCollectionAdapter(this,scollectionlist,maincollection);
        recyclerView.setAdapter(placeSubCollectionAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


    }


    @Override
    protected void onResume() {
        super.onResume();

        scollectionlist.clear();

        for(PlaceCollectionClass subcollection:PreSets.getAll_SubCollectionfrom_from_mainCollection(this,maincollection.getId())){

            scollectionlist.add(subcollection);

        }
        placeSubCollectionAdapter.notifyDataSetChanged();

    }

    public class PlaceSubCollectionAdapter extends RecyclerView.Adapter<PlaceSubCollectionAdapter.PlaceViewHolder>{

        private Activity activity;
        private ArrayList<PlaceCollectionClass> scollectionlist;
        PlaceCollectionClass maincollection;

        public PlaceSubCollectionAdapter(Activity activity, ArrayList<PlaceCollectionClass> scollectionlist, PlaceCollectionClass maincollection) {
            this.activity = activity;
            this.scollectionlist = scollectionlist;
            this.maincollection = maincollection;
        }

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_collection_layout,
                    parent, false);
            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceViewHolder holder, final int position) {

            final PlaceCollectionClass collection=scollectionlist.get(position);

            PreSets.setImageIcon(holder.mPlace, collection.getIconnumber());

            holder.tv.setText(PreSets.setlanguage_name(activity,collection));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(activity, PlacesActivity.class);
                    ArrayList<String> idlist=new ArrayList<>();
                    for (PlaceCollectionClass placeCollectionClass:scollectionlist){
                        idlist.add(placeCollectionClass.getId());
                    }
                    intent.putStringArrayListExtra("id",idlist);
                    intent.putExtra("index",position);
                    activity.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return scollectionlist.size();
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
