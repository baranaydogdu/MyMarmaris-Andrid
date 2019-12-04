package com.baranaydogdu.mymarmaris.PlaceCollectionEdits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PlaceEdits.PlaceEditSinglePlaces;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlacesEditCollections extends AppCompatActivity {

    Button addmaincollectionbutton,addtinycollectionbutton,addlinkedcollectionbutton,edit_place_publish_changes;
    RecyclerView placeedit_recyclerview;
    static ArrayList<PlaceCollectionClass> mcollectionlist;
    Activity mactivity;
    DatabaseReference mRef;
    Query query;
    AlertDialog.Builder builder;
    PlaceeditCollectionAdapter placeeditAdapter;
    ValueEventListener valueEventListener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_edit_collections);

        builder = new AlertDialog.Builder(this);
        mactivity=this;
        mRef= FirebaseDatabase.getInstance().getReference();
        mcollectionlist=new ArrayList<>();

        query = mRef.child("Places").orderByChild("info/sortnumber");

        addmaincollectionbutton  =findViewById(R.id.edit_place_add_maincollection);
        addtinycollectionbutton  =findViewById(R.id.edit_place_add_tinycollection);
        addlinkedcollectionbutton=findViewById(R.id.edit_place_add_linkedcollection);

        edit_place_publish_changes=findViewById(R.id.edit_place_publish_changes);
        placeedit_recyclerview=findViewById(R.id.edit_place_collection_recyclerview);
        progressBar=findViewById(R.id.progressBar);

        addmaincollectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mactivity, PlaceAddorEditCollection.class);
                intent.putExtra("from", "new");
                intent.putExtra("type", PlaceCollectionClass.COLLECTION_WITH_SUB);
                startActivity(intent);
            }
        });

        addtinycollectionbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mactivity, PlaceAddorEditCollection.class);
                        intent.putExtra("from", "new");
                        intent.putExtra("type", PlaceCollectionClass.COLLECTION_NON_SUB);
                        startActivity(intent);
                    }
                });

        addlinkedcollectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mactivity, PlaceAddorEditCollection.class);
                intent.putExtra("from", "new");
                intent.putExtra("type", PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION);
                startActivity(intent);
            }
        });

        placeeditAdapter= new PlaceeditCollectionAdapter(mactivity,mcollectionlist);
        placeedit_recyclerview.setAdapter(placeeditAdapter);
        placeedit_recyclerview.setLayoutManager(new LinearLayoutManager(mactivity));

        initeventlistener();

        edit_place_publish_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishchanges();
            }
        });

    }

    public void startsmth(){

        addmaincollectionbutton.setEnabled(false);
        addtinycollectionbutton.setEnabled(false);
        addlinkedcollectionbutton.setEnabled(false);
        edit_place_publish_changes.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void finishsmth(){

        addmaincollectionbutton.setEnabled(true);
        addtinycollectionbutton.setEnabled(true);
        addlinkedcollectionbutton.setEnabled(true);
        edit_place_publish_changes.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void publishchanges() {

        startsmth();

        mRef.child("Places_version").setValue(mRef.push().getKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                finishsmth();

                builder.setIcon(R.drawable.checkicon);
                builder.setTitle("SUCCESS");
                builder.setMessage("Changes Published successfuly.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(null,null);
                builder.setCancelable(false).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                finishsmth();

                builder.setIcon(R.drawable.error_icon);
                builder.setTitle("ERROR");
                builder.setMessage("ERROR : "+e.getLocalizedMessage());
                builder.setPositiveButton("OK", null);
                builder.setNegativeButton(null,null);
                builder.setCancelable(true).show();

            }
        });

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

    private void initeventlistener() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mcollectionlist.clear();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        PlaceCollectionClass maincollection = ds.child("info").getValue(PlaceCollectionClass.class);
                        mcollectionlist.add(maincollection);

                    }
                    placeeditAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }


    public class PlaceeditCollectionAdapter extends RecyclerView.Adapter<PlaceeditCollectionAdapter.PlaceedirViewHolder>{

        private Activity mActivity;
        private ArrayList<PlaceCollectionClass> mcollectionList;
        DatabaseReference mref;

        public PlaceeditCollectionAdapter(Activity mActivity, ArrayList<PlaceCollectionClass> mcollectionList) {
            this.mActivity = mActivity;
            this.mcollectionList = mcollectionList;
            mref= FirebaseDatabase.getInstance().getReference();
        }

        @Override
        public PlaceedirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_placeedit_layout,
                    parent, false);
            return new PlaceedirViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceedirViewHolder holder, final int position) {

            final PlaceCollectionClass maincollection = mcollectionList.get(position);

            holder.type_icon.setVisibility(View.VISIBLE);

            if (maincollection.getType() == PlaceCollectionClass.COLLECTION_WITH_SUB) {
                holder.type_icon.setImageResource(R.drawable.maincollection);

            } else if (maincollection.getType() == PlaceCollectionClass.COLLECTION_NON_SUB) {
                holder.type_icon.setImageResource(R.drawable.tinycollection);

            } else if (maincollection.getType() == PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION ||
                        maincollection.getType()== PlaceCollectionClass.INSIDE_LINKKED_COLLECTION) {
                holder.type_icon.setImageResource(R.drawable.linked);

            } else holder.type_icon.setVisibility(View.INVISIBLE);



            if (position==0) holder.uparrow.setVisibility(View.INVISIBLE); else holder.uparrow.setVisibility(View.VISIBLE);
            if (position==(mcollectionList.size()-1)) holder.downarrow.setVisibility(View.INVISIBLE); else holder.downarrow.setVisibility(View.VISIBLE);

            holder.uparrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child("Places").child(mcollectionList.get(position).getId()).child("info/sortnumber").setValue(position-1);
                    mref.child("Places").child(mcollectionList.get(position-1).getId()).child("info/sortnumber").setValue(position);

                }
            });

            holder.downarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child("Places").child(mcollectionList.get(position+1).getId()).child("info/sortnumber").setValue(position);
                    mref.child("Places").child(mcollectionList.get(position).getId()).child("info/sortnumber").setValue(position+1);

                }
            });


            holder.tv.setText(PreSets.setlanguage_name(mActivity,maincollection));

            PreSets.setImageIcon(holder.mPlace,maincollection.getIconnumber());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (maincollection.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB){

                        Intent in = new Intent(mActivity, PlaceeditSubCollections.class);
                        in.putExtra("MainCollection",maincollection);
                        mActivity.startActivity(in);

                    } else if (maincollection.getType()==PlaceCollectionClass.COLLECTION_NON_SUB){

                        Intent in = new Intent(mActivity, PlaceEditSinglePlaces.class);
                        in.putExtra("MainCollection",maincollection);
                        in.putExtra("SubCollection",maincollection);
                        mActivity.startActivity(in);


                    }

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Intent in = new Intent(mActivity, PlaceAddorEditCollection.class);
                    in.putExtra("MainCollection",maincollection);
                    in.putExtra("from", "old");

                    mActivity.startActivity(in);


                    return false;
                }
            });



        }


        @Override
        public int getItemCount() {
            return mcollectionList.size();
        }

        public class PlaceedirViewHolder extends RecyclerView.ViewHolder {

            ImageView mPlace,uparrow,downarrow,type_icon;
            TextView tv;

            public PlaceedirViewHolder(View itemView) {
                super(itemView);

                mPlace = itemView.findViewById(R.id.activity_custum_iconsource);
                tv = itemView.findViewById(R.id.custom__activity_textview);
                uparrow = itemView.findViewById(R.id.activity_custum_upicon);
                downarrow = itemView.findViewById(R.id.activity_custum_downicon);
                type_icon= itemView.findViewById(R.id.collection_type_icon);
            }
        }
    }

}
