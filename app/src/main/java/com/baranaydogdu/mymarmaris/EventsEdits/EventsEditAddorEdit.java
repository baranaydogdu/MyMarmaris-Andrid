package com.baranaydogdu.mymarmaris.EventsEdits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baranaydogdu.mymarmaris.Classes.Contactinfo;
import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.LocationClass;
import com.baranaydogdu.mymarmaris.PlaceEdits.EditMediaEdits;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.SelectClon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class EventsEditAddorEdit extends AppCompatActivity {

    TextView addorplace, progress_text;
    Button upload_button, deletebutton;
    Intent intent;
    AlertDialog.Builder builder;
    EditText ll1, ll2, ll3, ll4, ll5;
    EditText ee1, ee2, ee3, ee4, ee5;
    EditText aa1, aa2, aa3, aa4, aa5;
    EditText edittext_phone, edittext_insta, edittext_face, edittext_web, edittext_mail, edittext_whatsapp, edittext_sell_ticket;
    EditText apl1, apl2, apl3, apl4, apl5, aml1, aml2, aml3, aml4, aml5;
    Button addlinked_place, addlinked_management;
    EditText linked_place_edx, linked_management_edx;
    EditText editText_longitude, editTextlatitude;
    Double lat, log;
    ProgressBar progressBar;
    EventClass old_event,new_event;
    String videoversion,map_photoversion;
    ImageView popupimage;

    DatabaseReference mref;
    StorageReference mStorageRef;

    LinearLayout alllinearlayot;
    Activity activity;

    String progress;

    CalendarView calendarView;
    RecyclerView recyclerView;
    public ArrayList<TimesClass> times;
    public TimeAdapter timeAdapter;
    ScrollView scrollView;
    public static final int MAXDAYCOUNT = 37;

    int from;
    public static final int NEW = 0;
    public static final int OLD = 1;
    public static final int CLON = 2;

    SharedPreferences sharedPreferences;
    String event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_edit_addor_edit);

        initviews();

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("place_id", "default").apply();
        sharedPreferences.edit().putString("place_name", "default").apply();
        sharedPreferences.edit().putBoolean("new_place_selected", false).apply();

        sharedPreferences.edit().putString("management_id", "default").apply();
        sharedPreferences.edit().putString("management_name", "default").apply();
        sharedPreferences.edit().putBoolean("new_management_selected", false).apply();

        videoversion="default";
        map_photoversion="default";

        addlinked_management.setText("Add management");
        addlinked_place.setText("Add Place");

        linked_place_edx.setText("default");
        linked_management_edx.setText("default");

        activity = this;

        builder = new AlertDialog.Builder(this);
        mref = FirebaseDatabase.getInstance().getReference().child("Activities");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        times = new ArrayList<>();
        progress = "";

        timeAdapter = new TimeAdapter();
        recyclerView.setAdapter(timeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtofirebase();
            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_event();
            }
        });

        intent = getIntent();

        if (intent.getStringExtra("from").equals("old")) from = OLD;
        else if (intent.getStringExtra("from").equals("new")) from = NEW;
        else if (intent.getStringExtra("from").equals("clon")) from = CLON;

        if (from > 0) {

            fromold_preparation();

        } else {

            event_id = mref.push().getKey();
            alllinearlayot.removeView(deletebutton);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if (times.size() < MAXDAYCOUNT) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.AM_PM,Calendar.AM);
                    calendar.set(year, month, dayOfMonth, 20, 0, 0);

                    times.add(new TimesClass(calendar.getTime().getTime()));

                    Collections.sort(times, new Comparator<TimesClass>() {
                        @Override
                        public int compare(TimesClass o1, TimesClass o2) {
                            return o1.startdate.compareTo(o2.startdate);
                        }
                    });

                    timeAdapter.notifyDataSetChanged();

                } else {

                    builder.setIcon(R.drawable.error_icon);
                    builder.setTitle("ERROR");
                    builder.setMessage("You have reached to max date number.");
                    builder.setPositiveButton("OK", null);
                    builder.setNegativeButton(null, null);
                    builder.setCancelable(true).show();

                }

            }
        });

        addlinked_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPreferences.getString("place_id","default").equals("default")) {      //SECILI LINK YOK ISE

                    Intent intent = new Intent(activity, SelectClon.class);
                    intent.putExtra("requestcode", SelectClon.SELECT_PLACE_TYPE);
                    startActivity(intent);

                } else {        //SECILI VAR ISE SILCEZ


                    sharedPreferences.edit().putString("place_id","default").apply();
                    linked_place_edx.setText("default");
                    addlinked_place.setText("Add Place");

                }

            }
        });

        addlinked_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPreferences.getString("management_id","default").equals("default")) {      //SECILI LINK YOK ISE

                    Intent intent = new Intent(activity, SelectClon.class);
                    intent.putExtra("requestcode", SelectClon.SELECT_MANAGEMENT_TYPE);
                    startActivity(intent);

                } else {        //SECILI VAR ISE SILCEZ

                    sharedPreferences.edit().putString("management_id","default").apply();
                    linked_management_edx.setText("default");
                    addlinked_management.setText("Add Management");

                }

            }
        });

        scrollView.scrollTo(0, 0);
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (sharedPreferences.getBoolean("new_place_selected", false)) {
            linked_place_edx.setText(sharedPreferences.getString("place_name", "default"));
            sharedPreferences.edit().putBoolean("new_place_selected", false).apply();
            addlinked_place.setText("Remove Place");

        }

        if (sharedPreferences.getBoolean("new_management_selected", false)) {
            linked_management_edx.setText(sharedPreferences.getString("management_name", "default"));
            sharedPreferences.edit().putBoolean("new_management_selected", false).apply();
            addlinked_management.setText("Remove Management");

        }


    }

    private void initviews() {

        popupimage=findViewById(R.id.popupimage3);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerview);
        scrollView = findViewById(R.id.scrollView2);

        ll1 = findViewById(R.id.ll1); //NAMES
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);

        ee1 = findViewById(R.id.ee1); //EXPLANATIONS
        ee2 = findViewById(R.id.ee2);
        ee3 = findViewById(R.id.ee3);
        ee4 = findViewById(R.id.ee4);
        ee5 = findViewById(R.id.ee5);

        aa1 = findViewById(R.id.aa1); //EXPLANATIONS
        aa2 = findViewById(R.id.aa2);
        aa3 = findViewById(R.id.aa3);
        aa4 = findViewById(R.id.aa4);
        aa5 = findViewById(R.id.aa5);

        edittext_phone = findViewById(R.id.edittext_phone);
        edittext_insta = findViewById(R.id.edittext_insta);
        edittext_face = findViewById(R.id.edittext_face);
        edittext_web = findViewById(R.id.edittext_web);
        edittext_mail = findViewById(R.id.edittext_mail);
        edittext_whatsapp = findViewById(R.id.edittext_whatsapp);
        edittext_sell_ticket = findViewById(R.id.edittext_sell_ticket);

        editText_longitude = findViewById(R.id.editText_longitude);
        editTextlatitude = findViewById(R.id.editText_latitude);
        progressBar = findViewById(R.id.addplace_progressbar);
        progress_text = findViewById(R.id.progress_text);

        addorplace = findViewById(R.id.addorplace_textview);
        upload_button = findViewById(R.id.addplace_upload_button);
        deletebutton = findViewById(R.id.addplace_delete_button);
        alllinearlayot = findViewById(R.id.alllinearlayot);

        edittext_sell_ticket = findViewById(R.id.edittext_sell_ticket);

        apl1 = findViewById(R.id.add_place_ll1);
        apl2 = findViewById(R.id.add_place_ll2);
        apl3 = findViewById(R.id.add_place_ll3);
        apl4 = findViewById(R.id.add_place_ll4);
        apl5 = findViewById(R.id.add_place_ll5);

        aml1 = findViewById(R.id.add_management_ll1);
        aml2 = findViewById(R.id.add_management_ll2);
        aml3 = findViewById(R.id.add_management_ll3);
        aml4 = findViewById(R.id.add_management_ll4);
        aml5 = findViewById(R.id.add_management_ll5);

        addlinked_place = findViewById(R.id.avent_add_link_place_button);
        addlinked_management = findViewById(R.id.avent_add_link_management_button);

        linked_place_edx = findViewById(R.id.add_linked_place_edx);
        linked_management_edx = findViewById(R.id.add_linked_management_edx);

    }

    private void fromold_preparation() {

        old_event = (EventClass) intent.getSerializableExtra("event");

        if (from == CLON) {

            event_id = mref.push().getKey();
            alllinearlayot.removeView(deletebutton);
            addorplace.setText("Add Activity");
            upload_button.setText("Add Activity");

        } else {

            event_id = old_event.getId();
            addorplace.setText("Edit Activity");
            upload_button.setText("Apply Changes");
            map_photoversion=old_event.getMapphotoversion();
            videoversion=old_event.getVideoversion();

            popupimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popupMenu_allplaces = new PopupMenu(activity, popupimage);
                    popupMenu_allplaces.getMenuInflater().inflate(R.menu.popup_menu_media_edit, popupMenu_allplaces.getMenu());

                    popupMenu_allplaces.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.media_edit) {

                                Intent intent=new Intent(activity, EditMediaEdits.class);

                                intent.putExtra("event",old_event);
                                intent.putExtra("from",EditMediaEdits.FROM_EVENT);
                                finish();

                                startActivity(intent);

                            }

                            return true;
                        }
                    });

                    popupMenu_allplaces.show();

                }
            });

        }


        for (int i = 0; i < old_event.getTimes().size(); i++) {

            TimesClass singletime = new TimesClass(old_event.getTimes().get(i).getStart_time(), old_event.getTimes().get(i).getEnd_time());
            times.add(singletime);

        }

        timeAdapter.notifyDataSetChanged();


        if (old_event.getLinked_management().equals("default")) {


        } else {

            addlinked_management.setText("Remove Management");
            sharedPreferences.edit().putString("management_id",old_event.getLinked_management()).apply();
            linked_management_edx.setText("SELECTED");

        }

        if (old_event.getLinked_place().equals("default")) {

        } else {

            addlinked_place.setText("Remove Place");
            sharedPreferences.edit().putString("place_id",old_event.getLinked_place()).apply();
            linked_place_edx.setText("SELECTED");

        }


        ll1.setText(old_event.getName().get(0));
        ll2.setText(old_event.getName().get(1));
        ll3.setText(old_event.getName().get(2));
        ll4.setText(old_event.getName().get(3));
        ll5.setText(old_event.getName().get(4));

        ee1.setText(old_event.getExplain().get(0));
        ee2.setText(old_event.getExplain().get(1));
        ee3.setText(old_event.getExplain().get(2));
        ee4.setText(old_event.getExplain().get(3));
        ee5.setText(old_event.getExplain().get(4));

        aa1.setText(old_event.getAdres().get(0));
        aa2.setText(old_event.getAdres().get(1));
        aa3.setText(old_event.getAdres().get(2));
        aa4.setText(old_event.getAdres().get(3));
        aa5.setText(old_event.getAdres().get(4));

        apl1.setText(old_event.getUnlinked_place().get(0));
        apl2.setText(old_event.getUnlinked_place().get(1));
        apl3.setText(old_event.getUnlinked_place().get(2));
        apl4.setText(old_event.getUnlinked_place().get(3));
        apl5.setText(old_event.getUnlinked_place().get(4));

        aml1.setText(old_event.getUnlinked_management().get(0));
        aml2.setText(old_event.getUnlinked_management().get(1));
        aml3.setText(old_event.getUnlinked_management().get(2));
        aml4.setText(old_event.getUnlinked_management().get(3));
        aml5.setText(old_event.getUnlinked_management().get(4));

        edittext_face.setText(old_event.getContactinfo().getFacebook());
        edittext_insta.setText(old_event.getContactinfo().getInstagram());
        edittext_phone.setText(old_event.getContactinfo().getPhonenumber());
        edittext_whatsapp.setText(old_event.getContactinfo().getWhatsapp());
        edittext_web.setText(old_event.getContactinfo().getWebsite());
        edittext_mail.setText(old_event.getContactinfo().getMailadress());
        edittext_sell_ticket.setText(old_event.getContactinfo().getBuyticket());

        editTextlatitude.setText(old_event.getLocation().getLat().toString());
        editText_longitude.setText(old_event.getLocation().getLog().toString());

    }

    private void uploadtofirebase() {

        if (times.size() != 0) {

            try {

                if (editText_longitude.getText().toString().equals("")) log = 0.0;
                else log = Double.valueOf(editText_longitude.getText().toString());

                if (editTextlatitude.getText().toString().equals("")) lat = 0.0;
                else lat = Double.valueOf(editTextlatitude.getText().toString());

            } catch (Exception e) {

                lat = null;
                log = null;

            }

            if (lat == null || log == null) {

                builder.setIcon(R.drawable.error_icon);
                builder.setTitle("WRONG FORMAT");
                builder.setMessage("Latitute or/ and longitute format example :\nLat: 36.8531\nLong: 28.2708");
                builder.setPositiveButton("OK", null);
                builder.setNegativeButton(null, null);
                builder.setCancelable(true).show();

            } else {

                startingupload();

                new_event = new EventClass();

                ArrayList<String> names = new ArrayList<>();

                names.add(ll1.getText().toString());
                names.add(ll2.getText().toString());
                names.add(ll3.getText().toString());
                names.add(ll4.getText().toString());
                names.add(ll5.getText().toString());

                ArrayList<String> explains = new ArrayList<>();

                explains.add(ee1.getText().toString());
                explains.add(ee2.getText().toString());
                explains.add(ee3.getText().toString());
                explains.add(ee4.getText().toString());
                explains.add(ee5.getText().toString());

                ArrayList<String> adresses = new ArrayList<>();

                adresses.add(aa1.getText().toString());
                adresses.add(aa2.getText().toString());
                adresses.add(aa3.getText().toString());
                adresses.add(aa4.getText().toString());
                adresses.add(aa5.getText().toString());

                ArrayList<String> unlinked_place_names=new ArrayList<>();

                unlinked_place_names.add(apl1.getText().toString());
                unlinked_place_names.add(apl2.getText().toString());
                unlinked_place_names.add(apl3.getText().toString());
                unlinked_place_names.add(apl4.getText().toString());
                unlinked_place_names.add(apl5.getText().toString());

                ArrayList<String> unlinked_management_names=new ArrayList<>();

                unlinked_management_names.add(aml1.getText().toString());
                unlinked_management_names.add(aml2.getText().toString());
                unlinked_management_names.add(aml3.getText().toString());
                unlinked_management_names.add(aml4.getText().toString());
                unlinked_management_names.add(aml5.getText().toString());

                Contactinfo contacts = new Contactinfo();

                contacts.setFacebook(edittext_face.getText().toString());
                contacts.setPhonenumber(edittext_phone.getText().toString());
                contacts.setWhatsapp(edittext_whatsapp.getText().toString());
                contacts.setInstagram(edittext_insta.getText().toString());
                contacts.setWebsite(edittext_web.getText().toString());
                contacts.setMailadress(edittext_mail.getText().toString());
                contacts.setBuyticket(edittext_sell_ticket.getText().toString());

                LocationClass location = new LocationClass();
                location.setLat(lat);
                location.setLog(log);

                new_event.setId(event_id);
                new_event.setName(names);
                new_event.setExplain(explains);
                new_event.setAdres(adresses);
                new_event.setContactinfo(contacts);
                new_event.setLocation(location);
                new_event.setVideoversion(videoversion);
                new_event.setMapphotoversion(map_photoversion);
                new_event.setLinked_place(sharedPreferences.getString("place_id","default"));
                new_event.setLinked_management(sharedPreferences.getString("management_id","default"));
                new_event.setUnlinked_management(unlinked_management_names);
                new_event.setUnlinked_place(unlinked_place_names);

                if (from==OLD){

                    new_event.setVideoversion(old_event.getVideoversion());
                    new_event.setMapphotoversion(old_event.getMapphotoversion());
                    new_event.setTopphotos(old_event.getTopphotos());
                    new_event.setDownphotos(old_event.getDownphotos());

                }

                ArrayList<EventTimes> eventTimes=new ArrayList<>();
                for (TimesClass singletimes:times){

                    EventTimes singleeventTime=new EventTimes(singletimes.getStarttime(),singletimes.getEndtime());
                    eventTimes.add(singleeventTime);

                }

                new_event.setTimes(eventTimes);


                mref.child(new_event.getId()).setValue(new_event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        finishedupload();
                        textedit("Finished Upload...");
                        builder.setIcon(R.drawable.checkicon);
                        builder.setTitle("SUCCESS");
                        builder.setMessage("Upload success");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();

                            }
                        });
                        builder.setNegativeButton(null, null);
                        builder.setCancelable(false).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setfailuremessage(e);
                    }
                });


            }


        } else {

            builder.setIcon(R.drawable.error_icon);
            builder.setTitle("ERROR");
            builder.setMessage("You have not select any activity time.");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton(null, null);
            builder.setCancelable(true).show();

        }
    }



    private void delete_event() {

        builder.setIcon(R.drawable.warningicon);
        builder.setTitle("ARE YOU SURE");
        builder.setMessage("You are removing all the content. This progress can not be back.");
        builder.setNegativeButton("CANCEL", null);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startingupload();

                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

                mStorageRef.child(PreSets.firebase_videoname(event_id)).delete();
                mStorageRef.child(PreSets.firebase_map_name(event_id)).delete();

                for (int i = 0; i < 15; i++) {
                    mStorageRef.child(PreSets.firebase_TOP_photoname(event_id, i)).delete();
                    mStorageRef.child(PreSets.firebase_DOWN_photoname(event_id, i)).delete();
                }

                mref.child(event_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override

                    public void onSuccess(Void aVoid) {

                        finishedupload();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setIcon(R.drawable.checkicon);
                        builder.setTitle("SUCCESS");
                        builder.setMessage("Deleting success");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        });
                        builder.setNegativeButton(null, null);
                        builder.setCancelable(false).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        finishedupload();
                        setfailuremessage(e);
                    }
                });
            }
        }).setCancelable(false).show();

    }


    public void startingupload(){

        progressBar.setVisibility(View.VISIBLE);
        upload_button.setEnabled(false);
        deletebutton.setEnabled(false);
        addlinked_place.setEnabled(false);
        addlinked_management.setEnabled(false);
        calendarView.setEnabled(false);
        recyclerView.setEnabled(false);

    }

    public void finishedupload(){

        progressBar.setVisibility(View.INVISIBLE);
        upload_button.setEnabled(       true);
        deletebutton.setEnabled(        true);
        addlinked_place.setEnabled(     true);
        addlinked_management.setEnabled(true);
        calendarView.setEnabled(        true);
        recyclerView.setEnabled(        true);

    }

    public void textedit(String add){

        progress=progress+add+"\n";
        progress_text.setText(progress);

    }

    public void setfailuremessage(Exception e){

        finishedupload();
        builder.setIcon(R.drawable.error_icon);
        builder.setTitle("ERROR");
        builder.setMessage("Error : " +e.getLocalizedMessage());
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton(null,null);
        builder.setCancelable(true).show();

    }

    class TimeAdapter extends RecyclerView.Adapter <TimeAdapter.TimeViewHolder>{

        public TimeAdapter() {


        }

        @NonNull
        @Override
        public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerwiew_time_cons_single_item,
                    parent, false);
            return new TimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final TimeViewHolder holder, final int position) {

            if (times.get(position).getIs_changed()){
                holder.checkbutton.setVisibility(View.VISIBLE);
                holder.delbutton.setVisibility(View.INVISIBLE);

            } else {

                holder.checkbutton.setVisibility(View.INVISIBLE);
                holder.delbutton.setVisibility(View.VISIBLE);

            }

            final Calendar startcalendar=Calendar.getInstance();
            startcalendar.setTime(new Date(times.get(position).getStarttime()));

            final Calendar endcalendar=Calendar.getInstance();
            endcalendar.setTime(new Date(times.get(position).getEndtime()));

            holder.startdate_edx.setText(PreSets.getlanguages_date(activity,startcalendar.getTime().getTime()));
            holder.startminute_edx.setText(""+startcalendar.get(Calendar.MINUTE));
            holder.starthour_edx.setText(PreSets.gethour(startcalendar.getTime().getTime()));

            holder.enddate_edx.setText(PreSets.getlanguages_date(activity,endcalendar.getTime().getTime()));
            holder.endminute_edx.setText(""+endcalendar.get(Calendar.MINUTE));
            holder.endhour_edx.setText(PreSets.gethour(endcalendar.getTime().getTime()));

            holder.delbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    times.remove(position);
                    timeAdapter.notifyDataSetChanged();
                }
            });

            holder.checkbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int single_start_hour,single_end_hour,single_start_minute,single_end_minute;

                    try {

                        single_start_hour   = Integer.valueOf(holder.starthour_edx.getText().toString());
                        single_start_minute = Integer.valueOf(holder.startminute_edx.getText().toString());

                        single_end_hour   = Integer.valueOf(holder.endhour_edx.getText().toString());
                        single_end_minute = Integer.valueOf(holder.endminute_edx.getText().toString());


                    } catch (Exception e) {

                        single_start_hour   = 20;
                        single_start_minute = 0;

                        single_end_hour     = 23;
                        single_end_minute   = 0;

                        Toast.makeText(activity,"Error writing times",Toast.LENGTH_SHORT).show();

                    }

                    if (single_start_hour>12){
                        single_start_hour=single_start_hour-12;
                        startcalendar.set(Calendar.HOUR,single_start_hour);
                        startcalendar.set(Calendar.AM_PM,Calendar.PM);

                    } else {

                        startcalendar.set(Calendar.HOUR,single_start_hour);
                        startcalendar.set(Calendar.AM_PM,Calendar.AM);

                    }

                    startcalendar.set(Calendar.MINUTE,single_start_minute);


                    if (single_end_hour>12){
                        single_end_hour=single_end_hour-12;
                        endcalendar.set(Calendar.HOUR,single_end_hour);
                        endcalendar.set(Calendar.AM_PM,Calendar.PM);

                    } else {

                        endcalendar.set(Calendar.HOUR,single_end_hour);
                        endcalendar.set(Calendar.AM_PM,Calendar.AM);

                    }

                    endcalendar.set(Calendar.MINUTE,single_end_minute);

                    TimesClass timesClass=new TimesClass(startcalendar.getTime().getTime(),endcalendar.getTime().getTime(),false);


                    times.set(position,timesClass);

                    Collections.sort(times, new Comparator<TimesClass>() {
                        @Override
                        public int compare(TimesClass o1, TimesClass o2) {
                            return o1.startdate.compareTo(o2.startdate);
                        }
                    });
                    timeAdapter.notifyDataSetChanged();

                }
            });

            View.OnTouchListener touchListener=new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (!times.get(position).getIs_changed()){

                        times.get(position).setIs_changed(true);
                        timeAdapter.notifyDataSetChanged();
                    }

                    return false;
                }
            };

            holder.starthour_edx.setOnTouchListener(touchListener);
            holder.startminute_edx.setOnTouchListener(touchListener);

            holder.endhour_edx.setOnTouchListener(touchListener);
            holder.endminute_edx.setOnTouchListener(touchListener);

            holder.plusbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endcalendar.set(Calendar.DAY_OF_MONTH,endcalendar.get(Calendar.DAY_OF_MONTH)+1);

                    times.get(position).plus_one_day_to_endtime();
                    timeAdapter.notifyDataSetChanged();
                }
            });

        }


        @Override
        public int getItemCount() {
            return times.size();
        }

        class TimeViewHolder extends RecyclerView.ViewHolder {

            ImageView delbutton,checkbutton,plusbutton;
            EditText startdate_edx,starthour_edx,startminute_edx;
            EditText enddate_edx,endhour_edx,endminute_edx;


            public TimeViewHolder(@NonNull View itemView) {
                super(itemView);

                startdate_edx   =itemView.findViewById(R.id.date);
                starthour_edx   =itemView.findViewById(R.id.hour);
                startminute_edx =itemView.findViewById(R.id.minute);

                enddate_edx   =itemView.findViewById(R.id.date2);
                endhour_edx   =itemView.findViewById(R.id.hour2);
                endminute_edx =itemView.findViewById(R.id.minute2);

                delbutton=itemView.findViewById(R.id.del_icon);
                checkbutton=itemView.findViewById(R.id.check_icon);
                plusbutton=itemView.findViewById(R.id.imageView5);

            }
        }
    }

    class TimesClass {

        Long starttime,endtime;
        Boolean is_changed;
        public Date startdate,enddate;

        public TimesClass(Long starttime,Long endtime, Boolean is_changed) {
            this.starttime = starttime;
            this.endtime = endtime;
            this.is_changed = is_changed;
            this.startdate=new Date(starttime);
            this.enddate=new Date(endtime);

        }

        public TimesClass(Long starttime,Long endtime) {
            this.starttime = starttime;
            this.endtime = endtime;
            this.is_changed = false;
            this.startdate=new Date(starttime);
            this.enddate=new Date(endtime);

        }



        public TimesClass(Long starttime) {

            this.is_changed=false;
            this.starttime = starttime;
            this.startdate=new Date(starttime);

            Calendar calendar=Calendar.getInstance();
            calendar.setTime(startdate);

            calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)+1);

            this.enddate=calendar.getTime();
            this.endtime=calendar.getTime().getTime();
            System.out.println("start : "+starttime);
            System.out.println("end   : "+endtime);

        }

        public Long getStarttime() {
            return starttime;
        }

        public void setStarttime(Long starttime) {
            startdate.setTime(starttime);
            this.starttime = starttime;
        }

        public Long getEndtime() {
            return endtime;
        }

        public void setEndtime(Long endtime) {
            enddate.setTime(endtime);
            this.endtime = endtime;
        }

        public Boolean getIs_changed() {
            return is_changed;
        }

        public void setIs_changed(Boolean is_changed) {
            this.is_changed = is_changed;
        }

        public Date getStartdate() {
            return startdate;
        }

        public void setStartdate(Date startdate) {
            starttime=startdate.getTime();
            this.startdate = startdate;
        }

        public Date getEnddate() {
            return enddate;
        }

        public void setEnddate(Date enddate) {
            endtime=enddate.getTime();
            this.enddate = enddate;
        }

        public void plus_one_day_to_endtime(){

            Calendar cal=Calendar.getInstance();
            cal.setTime(enddate);cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
            enddate=cal.getTime();
            endtime=enddate.getTime();


        }
    }




}
