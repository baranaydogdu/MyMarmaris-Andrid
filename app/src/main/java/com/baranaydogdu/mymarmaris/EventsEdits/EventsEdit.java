package com.baranaydogdu.mymarmaris.EventsEdits;

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

import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.SelectClon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsEdit extends AppCompatActivity {

    Button publish_changes;
    Button add_event,add_clone_event;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    AlertDialog.Builder builder;
    DatabaseReference mref;
    Query query;

    public ArrayList<EventClass> eventLists;
    Activity activity;
    EventEditAdapter eventEditAdapter;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_edit);

        eventLists=new ArrayList<>();

        builder = new AlertDialog.Builder(this);
        activity=this;

        mref= FirebaseDatabase.getInstance().getReference().child("Activities");
        query = mref.orderByChild("times/0/start_time");

        publish_changes=findViewById(R.id.eventedit__publish_changes);
        add_event=findViewById(R.id.edit_events_add_event_button);
        add_clone_event=findViewById(R.id.edit_event_add__clone_event);
        recyclerView=findViewById(R.id.eventedit_event_recyclerview);
        progressBar=findViewById(R.id.eventedit_progressBar);

        builder = new AlertDialog.Builder(this);

        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, EventsEditAddorEdit.class);

                intent.putExtra("from","new");
                startActivity(intent);

            }
        });

        add_clone_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, SelectClon.class);

                intent.putExtra("requestcode",SelectClon.CLON_EVENT_REQUEST);
                startActivity(intent);

            }
        });

        publish_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                publish_changes();
            }
        });

        eventEditAdapter= new EventEditAdapter();
        recyclerView.setAdapter(eventEditAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        initeventlistener();

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

                eventLists.clear();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        EventClass singleevent = ds.getValue(EventClass.class);
                        eventLists.add(singleevent);

                    }

                }

                eventEditAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


    }

    private void publish_changes() {

        startsmth();
        DatabaseReference pupref=FirebaseDatabase.getInstance().getReference().child("Activityversion");
        pupref.setValue(pupref.push().getKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showscuccesdialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showserrordialog(e);
            }
        });
    }

    public void startsmth() {

        add_event.setEnabled(false);
        publish_changes.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void finishsmth() {

        add_event.setEnabled(true);
        publish_changes.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void showscuccesdialog(){

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

    public void showserrordialog(Exception e){

        finishsmth();
        builder.setIcon(R.drawable.error_icon);
        builder.setTitle("ERROR");
        builder.setMessage("ERROR : "+e.getLocalizedMessage());
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton(null,null);
        builder.setCancelable(true).show();


    }

    public class EventEditAdapter extends RecyclerView.Adapter<EventEditAdapter.EventEditViewHolder>{


        public EventEditAdapter() {

        }

        @Override
        public EventEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_placeedit_layout,
                    parent, false);
            return new EventEditViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventEditViewHolder holder, final int position) {

            final EventClass event=eventLists.get(position);

            holder.tv.setText(PreSets.setlanguage_name(activity,event));
            PreSets.set_MAIN_photo(holder.mPlace,event.getId());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(activity, EventsEditAddorEdit.class);
                    in.putExtra("from","old");
                    in.putExtra("event",event);
                    startActivity(in);

                }
            });


        }


        @Override
        public int getItemCount() {
            return eventLists.size();
        }

        public class EventEditViewHolder extends RecyclerView.ViewHolder {

            ImageView mPlace,uparrow,downarrow;
            TextView tv;

            public EventEditViewHolder(View itemView) {
                super(itemView);

                mPlace = itemView.findViewById(R.id.activity_custum_iconsource);
                tv = itemView.findViewById(R.id.custom__activity_textview);
                uparrow = itemView.findViewById(R.id.activity_custum_upicon);
                downarrow = itemView.findViewById(R.id.activity_custum_downicon);
                uparrow.setVisibility(View.INVISIBLE);
                downarrow.setVisibility(View.INVISIBLE);
            }
        }
    }




}
