package com.baranaydogdu.mymarmaris.PlaceEdits;

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

import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.SelectClon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceEditSinglePlaces extends AppCompatActivity {

    ImageView collection_image,subcollection_imagee;
    TextView collection_namee,subcollection_name;
    Button add_clone_places,add_new_places ;
    RecyclerView recyclerview;
    PlaceCollectionClass maincollection,subcollection;
    Intent in;
    ArrayList<PlaceClass> placelist;
    ArrayList<String> placeidlist;
    PlaceeditSinglePlaceAdapter myadapter;
    Activity mactivity;
    ValueEventListener valueEventListener;
    int favcount;
    Activity activity;

    DatabaseReference mref;
    Query query;
    public static final int FAVORIDEGIL=100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit_single_place);

        activity=this;
        placelist=new ArrayList<>();
        placeidlist=new ArrayList<>();
        favcount=0;

        mactivity=this;
        in=getIntent();
        maincollection=(PlaceCollectionClass) in.getSerializableExtra("MainCollection");
        subcollection=(PlaceCollectionClass) in.getSerializableExtra("SubCollection");

        recyclerview=findViewById(R.id.edit_place_single_recyclerview);
        add_new_places=findViewById(R.id.edit_sing_place_add_places_button);
        add_clone_places=findViewById(R.id.edit_sing_place_add__clone_places);
        collection_namee=findViewById(R.id.collection_namee);
        subcollection_name=findViewById(R.id.subcollection_name);
        collection_image=findViewById(R.id.collection_image);
        subcollection_imagee=findViewById(R.id.subcollection_imagee);

        collection_namee.setText(PreSets.setlanguage_name(this,maincollection));
        PreSets.setImageIcon(collection_image,maincollection.getIconnumber());
        subcollection_name.setText(PreSets.setlanguage_name(this,subcollection));
        PreSets.setImageIcon(subcollection_imagee,subcollection.getIconnumber());

        if (maincollection.getId().equals(subcollection.getId())){
            subcollection_name.setVisibility(View.INVISIBLE);
            subcollection_imagee.setVisibility(View.INVISIBLE);
        }

        add_new_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, PlaceAddorEditSinglePlace.class);

                intent.putExtra("MainCollection",maincollection);
                intent.putExtra("SubCollection",subcollection);
                intent.putExtra("from","new");

                startActivity(intent);

            }
        });

        add_clone_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, SelectClon.class);

                intent.putExtra("MainCollection",maincollection);
                intent.putExtra("SubCollection",subcollection);
                intent.putExtra("requestcode",SelectClon.CLON_PLACE_REQUEST);

                startActivity(intent);

            }
        });

        myadapter= new PlaceeditSinglePlaceAdapter(mactivity,placelist,maincollection,subcollection,favcount);
        recyclerview.setAdapter(myadapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(activity));

        mref= FirebaseDatabase.getInstance().getReference();
        query=mref.child("Places").child(maincollection.getId()).child("subs").child(subcollection.getId()).child("places").orderByChild("sortnumber");
        initvalueeventlistener();

    }

    private void initvalueeventlistener() {

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                placelist.clear();
                placeidlist.clear();
                favcount=0;

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        PlaceClass singleplace = ds.getValue(PlaceClass.class);
                        placelist.add(singleplace);
                        placeidlist.add(ds.getKey());
                        if (singleplace.getSortnumber()!=PlaceClass.FAVORIDEGIL) favcount++;

                    }
                    myadapter.setFavcount(favcount);
                    myadapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


    }


    @Override
    protected void onPause() {
        super.onPause();
        query.removeEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        query.addValueEventListener(valueEventListener);
    }


    public class PlaceeditSinglePlaceAdapter extends RecyclerView.Adapter<PlaceeditSinglePlaceAdapter.PlaceeditSingleViewHolder>{

        private Activity mActivity;
        private ArrayList<PlaceClass> mPlaceList;
        PlaceCollectionClass maincollection,subcollection;
        DatabaseReference mref;
        int favcount;

        public void setFavcount(int favcount) {
            this.favcount = favcount;
        }

        public PlaceeditSinglePlaceAdapter(Activity mActivity, ArrayList<PlaceClass> mPlaceList, PlaceCollectionClass maincollection, PlaceCollectionClass subcollection, int favcount) {
            this.mActivity = mActivity;
            this.mPlaceList = mPlaceList;
            this.maincollection=maincollection;
            this.subcollection=subcollection;
            this.favcount=favcount;
            mref= FirebaseDatabase.getInstance().getReference().child("Places").child(maincollection.getId()).child("subs").child(subcollection.getId()).child("places");

        }

        @Override
        public PlaceeditSingleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_placeedit_layout,
                    parent, false);
            return new PlaceeditSingleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceeditSingleViewHolder holder, final int position) {

            final PlaceClass place=mPlaceList.get(position);

            if (place.getSortnumber()==PlaceClass.FAVORIDEGIL){ //FAVORI DEGIL ISE
                holder.uparrow.setVisibility(View.INVISIBLE);
                holder.downarrow.setVisibility(View.INVISIBLE);

                holder.favorite_icon.setVisibility(View.INVISIBLE);

            } else { //FAVORI ISE

                holder.favorite_icon.setVisibility(View.VISIBLE);

                if (position==0) {
                    holder.uparrow.setVisibility(View.INVISIBLE);
                } else holder.uparrow.setVisibility(View.VISIBLE);

                if (position==(favcount-1)) {
                    holder.downarrow.setVisibility(View.INVISIBLE);

                } else {
                    holder.downarrow.setVisibility(View.VISIBLE);
                }

            }

            holder.uparrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child(mPlaceList.get(position).getId()).child("sortnumber").setValue(position-1);
                    mref.child(mPlaceList.get(position-1).getId()).child("sortnumber").setValue(position);

                }
            });

            holder.downarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child(mPlaceList.get(position).getId()).child("sortnumber").setValue(position+1);
                    mref.child(mPlaceList.get(position+1).getId()).child("sortnumber").setValue(position);

                }
            });

            holder.tv.setText(PreSets.setlanguage_name(mActivity,place));

            PreSets.set_MAIN_photo(holder.place_photo,place.getId());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mActivity, PlaceAddorEditSinglePlace.class);

                    intent.putExtra("from", "old");
                    intent.putExtra("MainCollection",maincollection);
                    intent.putExtra("SubCollection",subcollection);
                    intent.putExtra("place",place);

                    mActivity.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return mPlaceList.size();
        }

        public class PlaceeditSingleViewHolder extends RecyclerView.ViewHolder {

            ImageView place_photo,favorite_icon,uparrow,downarrow;
            TextView tv;

            public PlaceeditSingleViewHolder(View itemView) {
                super(itemView);

                place_photo = itemView.findViewById(R.id.activity_custum_iconsource);
                tv = itemView.findViewById(R.id.custom__activity_textview);
                uparrow = itemView.findViewById(R.id.activity_custum_upicon);
                downarrow = itemView.findViewById(R.id.activity_custum_downicon);
                favorite_icon=itemView.findViewById(R.id.favorite_imageview);

            }
        }
    }
}
