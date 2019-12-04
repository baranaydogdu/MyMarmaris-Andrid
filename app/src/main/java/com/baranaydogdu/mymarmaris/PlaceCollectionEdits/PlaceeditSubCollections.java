package com.baranaydogdu.mymarmaris.PlaceCollectionEdits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PlaceEdits.PlaceEditSinglePlaces;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceeditSubCollections extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView collection_image;
    TextView collection_name;
    Button addsubcollection;
    Intent in;
    PlaceCollectionClass maincollection;
    PlaceeditSubCollectionAdapter myadapter;
    Activity mactivity;
    ArrayList<PlaceCollectionClass> subcollections;
    DatabaseReference mref;
    Query query;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit_sub_collections);

        recyclerView = findViewById(R.id.place_edit_sub_collection_recyclerview);
        in = getIntent();
        maincollection = (PlaceCollectionClass) in.getSerializableExtra("MainCollection");
        mactivity = this;
        mref = FirebaseDatabase.getInstance().getReference();
        query = mref.child("Places").child(maincollection.getId()).child("subs").orderByChild("info/sortnumber");

        collection_image = findViewById(R.id.collection_image);
        collection_name = findViewById(R.id.collection_name);
        addsubcollection = findViewById(R.id.edit_sub_place_add_places_sub_collection_button);
        addsubcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mactivity, PlaceAddorEditSubCollection.class);
                intent.putExtra("from", "new");
                intent.putExtra("MainCollection", maincollection);
                startActivity(intent);
            }
        });
        subcollections = new ArrayList<>();

        PreSets.setImageIcon(collection_image, maincollection.getIconnumber());
        collection_name.setText(maincollection.getName().get(0));

        myadapter = new PlaceeditSubCollectionAdapter(mactivity, subcollections, maincollection);
        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mactivity));

        initvalueeventlistener();
    }

    private void initvalueeventlistener() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                subcollections.clear();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        PlaceCollectionClass scollection = ds.child("info").getValue(PlaceCollectionClass.class);
                        subcollections.add(scollection);

                    }
                    myadapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        query.removeEventListener(valueEventListener);
    }


    public class PlaceeditSubCollectionAdapter extends RecyclerView.Adapter<PlaceeditSubCollections.PlaceeditSubCollectionAdapter.PlaceedirViewHolder> {

        private Activity mActivity;
        private ArrayList<PlaceCollectionClass> subcollectionlist;
        PlaceCollectionClass maincollection;
        DatabaseReference mref;

        public PlaceeditSubCollectionAdapter(Activity mActivity, ArrayList<PlaceCollectionClass> subcollectionlist, PlaceCollectionClass maincollection) {
            this.mActivity = mActivity;
            this.subcollectionlist = subcollectionlist;
            this.maincollection = maincollection;
            mref = FirebaseDatabase.getInstance().getReference().child("Places").child(maincollection.getId()).child("subs");
        }

        @Override
        public PlaceeditSubCollectionAdapter.PlaceedirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_placeedit_layout,
                    parent, false);
            return new PlaceeditSubCollections.PlaceeditSubCollectionAdapter.PlaceedirViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceeditSubCollectionAdapter.PlaceedirViewHolder holder, final int position) {

            final PlaceCollectionClass subcollection = subcollectionlist.get(position);

            if (position == 0) holder.uparrow.setVisibility(View.INVISIBLE);
            else holder.uparrow.setVisibility(View.VISIBLE);

            if (position == (subcollectionlist.size() - 1))  holder.downarrow.setVisibility(View.INVISIBLE);
            else holder.downarrow.setVisibility(View.VISIBLE);



            holder.uparrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child(subcollection.getId()).child("info/sortnumber").setValue(position - 1);
                    mref.child(subcollectionlist.get(position - 1).getId()).child("info/sortnumber").setValue(position);

                }
            });

            holder.downarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child(subcollectionlist.get(position + 1).getId()).child("info/sortnumber").setValue(position);
                    mref.child(subcollectionlist.get(position).getId()).child("info/sortnumber").setValue(position + 1);

                }
            });


            holder.tv.setText(PreSets.setlanguage_name(mActivity, subcollection));

            PreSets.setImageIcon(holder.mPlace, subcollection.getIconnumber());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(mActivity, PlaceEditSinglePlaces.class);
                    in.putExtra("MainCollection", maincollection);
                    in.putExtra("SubCollection", subcollectionlist.get(position));

                    mActivity.startActivity(in);


                }
            });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Intent intent = new Intent(mActivity, PlaceAddorEditSubCollection.class);
                    intent.putExtra("from", "old");
                    intent.putExtra("MainCollection", maincollection);
                    intent.putExtra("SubCollection", subcollectionlist.get(position));
                    mActivity.startActivity(intent);

                    return false;
                }
            });

        }


        @Override
        public int getItemCount() {
            return subcollectionlist.size();
        }

        public class PlaceedirViewHolder extends RecyclerView.ViewHolder {

            ImageView mPlace, uparrow, downarrow;
            TextView tv;

            public PlaceedirViewHolder(View itemView) {
                super(itemView);

                mPlace = itemView.findViewById(R.id.activity_custum_iconsource);
                tv = itemView.findViewById(R.id.custom__activity_textview);
                uparrow = itemView.findViewById(R.id.activity_custum_upicon);
                downarrow = itemView.findViewById(R.id.activity_custum_downicon);
            }
        }
    }


}
