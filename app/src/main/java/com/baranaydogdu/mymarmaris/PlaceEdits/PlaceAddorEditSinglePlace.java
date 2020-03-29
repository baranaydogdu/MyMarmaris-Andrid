package com.baranaydogdu.mymarmaris.PlaceEdits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baranaydogdu.mymarmaris.Classes.Contactinfo;
import com.baranaydogdu.mymarmaris.Classes.LocationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlaceAddorEditSinglePlace extends AppCompatActivity {

    TextView addorplace,progress_text;
    Button upload_button,deletebutton;
    Intent intent;
    PlaceCollectionClass maincollection,subcollection;
    AlertDialog.Builder builder;
    Switch isfavorite,isactive;
    EditText ll1,ll2,ll3,ll4,ll5;
    EditText ee1,ee2,ee3,ee4,ee5;
    EditText aa1,aa2,aa3,aa4,aa5;
    EditText edittext_phone,edittext_insta,edittext_face,edittext_web,edittext_mail,edittext_whatsapp,edittext_ticket;
    EditText open0,open1,open2,open3,open4,open5,open6;
    EditText close0,close1,close2,close3,close4,close5,close6;
    EditText editText_longitude,editTextlatitude;
    Double lat,log;
    ProgressBar progressBar;
    PlaceClass newplaceClass,oldplace;
    DatabaseReference mref;
    LinearLayout alllinearlayot;
    Activity activity;
    ArrayList<Integer> opentimes,closetimes;
    CalendarView calendarView;
    EditText date,hour,minute;
    public static final int NEW=0;
    public static final int OLD=1;
    public static final int CLON=2;
    int from;
    Boolean issmthwrong=false;
    Calendar calendar;
    Long endtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_addor_edit_single_place);

        calendar=Calendar.getInstance();
        opentimes=new ArrayList<>();
        closetimes=new ArrayList<>();

        initviews();
        activity=this;

        builder = new AlertDialog.Builder(this);
        intent=getIntent();
        maincollection=(PlaceCollectionClass) intent.getSerializableExtra("MainCollection");
        subcollection=(PlaceCollectionClass) intent.getSerializableExtra("SubCollection");

        if (intent.getStringExtra("from").equals("old")) from=OLD;
        else if (intent.getStringExtra("from").equals("new")) from=NEW;
        else from=CLON;

        mref= FirebaseDatabase.getInstance().getReference().child("Places").child(maincollection.getId()).child("subs").child(subcollection.getId()).child("places");

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadtofirebase();

            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteplace(activity,maincollection,subcollection,oldplace);
            }
        });

        if (from>0){   //DUZNELEME YAPILACAK ISE

            fromold_preparation();

        } else if (from==NEW) {

            alllinearlayot.removeView(deletebutton);
            calendar.set(Calendar.AM_PM,Calendar.AM);
            calendar.set(Calendar.HOUR,21);calendar.set(Calendar.MINUTE,0);calendar.set(Calendar.SECOND,0);
            date.setText(PreSets.getlanguages_date(this,calendar.getTime().getTime()));
            hour.setText(PreSets.gethour(calendar.getTime().getTime()));
            minute.setText(""+calendar.get(Calendar.MINUTE));
        }



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.AM_PM,Calendar.AM);
                calendar.set(year, month, dayOfMonth, 21, 0, 0);
                date.setText(PreSets.getlanguages_date(activity,calendar.getTime().getTime()));
                hour.setText(PreSets.gethour(calendar.getTime().getTime()));
                minute.setText(""+calendar.get(Calendar.MINUTE));

            }
        });

    }

    private void deleteplace(final Activity activity, final PlaceCollectionClass maincollection,final PlaceCollectionClass subcollection, final PlaceClass place) {

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

        builder.setIcon(R.drawable.warningicon);
        builder.setTitle("ARE YOU SURE");
        builder.setMessage("You are removing all the content. This progress can not be back.");
        builder.setNegativeButton("CANCEL", null);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startingupload();
                removeplacefilesfromfirebase(place);

                mref.child("Places").child(maincollection.getId()).
                        child("subs").child(subcollection.getId()).child("places").
                        child(place.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

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
                        builder.setCancelable(true).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        finishedupload();
                        Toast.makeText(activity,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setCancelable(false).show();

    }

    public static void removeplacefilesfromfirebase(PlaceClass place) {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        mStorageRef.child(PreSets.firebase_videoname(place.getId())).delete();
        mStorageRef.child(PreSets.firebase_map_name(place.getId())).delete();

        for (int i = 0; i < 15; i++) {
            mStorageRef.child(PreSets.firebase_TOP_photoname(place.getId(), i)).delete();
            mStorageRef.child(PreSets.firebase_DOWN_photoname(place.getId(), i)).delete();
        }



    }

    private void fromold_preparation() {

        oldplace = (PlaceClass) intent.getSerializableExtra("place");

        if (from==OLD){

            addorplace.setText("Edit Place");
            upload_button.setText("Apply Changes");

            final ImageView popupimage=findViewById(R.id.popupimage3);

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

                                intent.putExtra("MainCollection",maincollection);
                                intent.putExtra("SubCollection",subcollection);
                                intent.putExtra("place",oldplace);
                                intent.putExtra("from",EditMediaEdits.FROM_PLACE);
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

        if (oldplace.getSortnumber()==PlaceClass.FAVORIDEGIL) isfavorite.setChecked(false);
        else isfavorite.setChecked(true);

        if (oldplace.getIsactive()) isactive.setChecked(true);
        else isactive.setChecked(false);

        calendar.setTime(new Date(oldplace.getNew_end_time()));
        date.setText(PreSets.getlanguages_date(activity,calendar.getTime().getTime()));
        hour.setText(PreSets.gethour(calendar.getTime().getTime()));
        minute.setText(""+calendar.get(Calendar.MINUTE));

        ll1.setText(oldplace.getName().get(0));
        ll2.setText(oldplace.getName().get(1));
        ll3.setText(oldplace.getName().get(2));
        ll4.setText(oldplace.getName().get(3));
        ll5.setText(oldplace.getName().get(4));

        ee1.setText(oldplace.getExplain().get(0));
        ee2.setText(oldplace.getExplain().get(1));
        ee3.setText(oldplace.getExplain().get(2));
        ee4.setText(oldplace.getExplain().get(3));
        ee5.setText(oldplace.getExplain().get(4));

        aa1.setText(oldplace.getAdres().get(0));
        aa2.setText(oldplace.getAdres().get(1));
        aa3.setText(oldplace.getAdres().get(2));
        aa4.setText(oldplace.getAdres().get(3));
        aa5.setText(oldplace.getAdres().get(4));

        edittext_face.setText(oldplace.getContactinfo().getFacebook());
        edittext_insta.setText(oldplace.getContactinfo().getInstagram());
        edittext_phone.setText(oldplace.getContactinfo().getPhonenumber());
        edittext_whatsapp.setText(oldplace.getContactinfo().getWhatsapp());
        edittext_web.setText(oldplace.getContactinfo().getWebsite());
        edittext_mail.setText(oldplace.getContactinfo().getMailadress());
        edittext_ticket.setText(oldplace.getContactinfo().getBuyticket());

        editTextlatitude.setText(oldplace.getLocation().getLat().toString());
        editText_longitude.setText(oldplace.getLocation().getLog().toString());

        open0.setText(""+oldplace.getOpentime().get(0));
        open1.setText(""+oldplace.getOpentime().get(1));
        open2.setText(""+oldplace.getOpentime().get(2));
        open3.setText(""+oldplace.getOpentime().get(3));
        open4.setText(""+oldplace.getOpentime().get(4));
        open5.setText(""+oldplace.getOpentime().get(5));
        open6.setText(""+oldplace.getOpentime().get(6));

        close0.setText(""+oldplace.getClosetime().get(0));
        close1.setText(""+oldplace.getClosetime().get(1));
        close2.setText(""+oldplace.getClosetime().get(2));
        close3.setText(""+oldplace.getClosetime().get(3));
        close4.setText(""+oldplace.getClosetime().get(4));
        close5.setText(""+oldplace.getClosetime().get(5));
        close6.setText(""+oldplace.getClosetime().get(6));

    }

    private void initviews() {

        isfavorite=findViewById(R.id.isfavoriteswitch);
        isactive  = findViewById(R.id.isactiveswitch);

        ll1=findViewById(R.id.ll1); //NAMES
        ll2=findViewById(R.id.ll2);
        ll3=findViewById(R.id.ll3);
        ll4=findViewById(R.id.ll4);
        ll5=findViewById(R.id.ll5);

        ee1=findViewById(R.id.ee1); //EXPLANATIONS
        ee2=findViewById(R.id.ee2);
        ee3=findViewById(R.id.ee3);
        ee4=findViewById(R.id.ee4);
        ee5=findViewById(R.id.ee5);

        aa1=findViewById(R.id.aa1); //EXPLANATIONS
        aa2=findViewById(R.id.aa2);
        aa3=findViewById(R.id.aa3);
        aa4=findViewById(R.id.aa4);
        aa5=findViewById(R.id.aa5);

        open0  = findViewById(R.id.open0);
        open1  = findViewById(R.id.open1);
        open2  = findViewById(R.id.open2);
        open3  = findViewById(R.id.open3);
        open4  = findViewById(R.id.open4);
        open5  = findViewById(R.id.open5);
        open6  = findViewById(R.id.open6);

        close0 = findViewById(R.id.close0);
        close1 = findViewById(R.id.close1);
        close2 = findViewById(R.id.close2);
        close3 = findViewById(R.id.close3);
        close4 = findViewById(R.id.close4);
        close5 = findViewById(R.id.close5);
        close6 = findViewById(R.id.close6);

        edittext_phone=findViewById(R.id.edittext_phone);
        edittext_insta=findViewById(R.id.edittext_insta);
        edittext_face=findViewById(R.id.edittext_face);
        edittext_web=findViewById(R.id.edittext_web);
        edittext_mail=findViewById(R.id.edittext_mail);
        edittext_whatsapp=findViewById(R.id.edittext_whatsapp);
        edittext_ticket = findViewById(R.id.edittext_ticket);

        editText_longitude=findViewById(R.id.editText_longitude);
        editTextlatitude=findViewById(R.id.editText_latitude);
        progressBar=findViewById(R.id.addplace_progressbar);
        progress_text=findViewById(R.id.progress_text);

        calendarView=findViewById(R.id.calendarView);
        date=findViewById(R.id.date);
        hour=findViewById(R.id.hour);
        minute=findViewById(R.id.minute);

        addorplace=findViewById(R.id.addorplace_textview);
        upload_button=findViewById(R.id.addplace_upload_button);
        deletebutton=findViewById(R.id.addplace_delete_button);
        alllinearlayot=findViewById(R.id.alllinearlayot);

    }

    private void uploadtofirebase() {

        issmthwrong=false;

        newplaceClass = new PlaceClass();

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

        Contactinfo contacts = new Contactinfo();

        contacts.setFacebook(edittext_face.getText().toString());
        contacts.setPhonenumber(edittext_phone.getText().toString());
        contacts.setWhatsapp(edittext_whatsapp.getText().toString());
        contacts.setInstagram(edittext_insta.getText().toString());
        contacts.setWebsite(edittext_web.getText().toString());
        contacts.setMailadress(edittext_mail.getText().toString());
        contacts.setBuyticket(edittext_ticket.getText().toString());

        ArrayList<Integer> openss=new ArrayList<>();
        ArrayList<Integer> closess=new ArrayList<>();

        openss.clear();
        closess.clear();

        LocationClass location= new LocationClass();

        try {

            openss.add(Integer.valueOf(open0.getText().toString()));
            openss.add(Integer.valueOf(open1.getText().toString()));
            openss.add(Integer.valueOf(open2.getText().toString()));
            openss.add(Integer.valueOf(open3.getText().toString()));
            openss.add(Integer.valueOf(open4.getText().toString()));
            openss.add(Integer.valueOf(open5.getText().toString()));
            openss.add(Integer.valueOf(open6.getText().toString()));

            closess.add(Integer.valueOf(close0.getText().toString()));
            closess.add(Integer.valueOf(close1.getText().toString()));
            closess.add(Integer.valueOf(close2.getText().toString()));
            closess.add(Integer.valueOf(close3.getText().toString()));
            closess.add(Integer.valueOf(close4.getText().toString()));
            closess.add(Integer.valueOf(close5.getText().toString()));
            closess.add(Integer.valueOf(close6.getText().toString()));

            calendar.set(Calendar.AM_PM,Calendar.AM);
            calendar.set(Calendar.HOUR,Integer.valueOf(hour.getText().toString()));
            calendar.set(Calendar.MINUTE,Integer.valueOf(minute.getText().toString()));
            endtime=calendar.getTime().getTime();

            if (editText_longitude.getText().toString().equals("") || editTextlatitude.getText().toString().equals("")) {
                log = 0.0;
                lat = 0.0;
            } else {
                log = Double.valueOf(editText_longitude.getText().toString());
                lat = Double.valueOf(editTextlatitude.getText().toString());
            }

            location.setLat(lat);
            location.setLog(log);


        } catch (Exception e){

            issmthwrong=true;

        }

        if (issmthwrong){

            builder.setIcon(R.drawable.error_icon);
            builder.setTitle("WRONG FORMAT");
            builder.setMessage("Something about input does not seem true. Please check your inputs.\nLatitute or/ and longitute format example :\nLat: 36.8531\nLong: 28.2708");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton(null, null);
            builder.setCancelable(true).show();

        } else {

            startingupload();

            if (isactive.isChecked()) newplaceClass.setIsactive(true);
            else newplaceClass.setIsactive(false);

            newplaceClass.setName(names);
            newplaceClass.setExplain(explains);
            newplaceClass.setAdres(adresses);
            newplaceClass.setContactinfo(contacts);
            newplaceClass.setLocation(location);
            newplaceClass.setNew_end_time(endtime);
            newplaceClass.setOpentime(openss);
            newplaceClass.setClosetime(closess);

            if (from==OLD) { //UPTADE

                newplaceClass.setId(oldplace.getId());
                newplaceClass.setVideoversion(oldplace.getVideoversion());
                newplaceClass.setTopphotos(oldplace.getTopphotos());
                newplaceClass.setDownphotos(oldplace.getDownphotos());
                newplaceClass.setMapphotoversion(oldplace.getMapphotoversion());

            } else {
                newplaceClass.setId(mref.push().getKey());
                newplaceClass.setVideoversion("default");
                newplaceClass.setMapphotoversion("default");
            }


            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int newsortnumber = (int) dataSnapshot.getChildrenCount();

                    if (from==OLD) {   //UPDATE

                        if (oldplace.getSortnumber() != PlaceClass.FAVORIDEGIL && isfavorite.isChecked()) {

                            newplaceClass.setSortnumber(oldplace.getSortnumber());  //ESKIDEN DE SIMDI DE FAVORI

                        } else if (isfavorite.isChecked()) { //FAVORI YAPILIYORSA

                            newplaceClass.setSortnumber(newsortnumber);


                        } else { //FAVORIDEN SILINIYOR ISE

                            newplaceClass.setSortnumber(PlaceClass.FAVORIDEGIL);

                        }

                    } else {    //NEW

                        if (isfavorite.isChecked()) newplaceClass.setSortnumber(newsortnumber);
                        else newplaceClass.setSortnumber(PlaceClass.FAVORIDEGIL);

                    }


                    mref.child(newplaceClass.getId()).setValue(newplaceClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) { //DATABASE YAZILDI ISE

                            finishedupload();

                            builder.setIcon(R.drawable.checkicon);
                            builder.setTitle("SUCCESS");
                            builder.setMessage("Upload success");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface g, int which) {
                                    finish();
                                }
                            });
                            builder.setNegativeButton(null, null);
                            builder.setCancelable(true).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { // DATABASE YAZILMADI ISE

                            finishedupload();
                            builder.setIcon(R.drawable.error_icon);
                            builder.setTitle("ERROR");
                            builder.setMessage("Error : " + e.getLocalizedMessage());
                            builder.setPositiveButton("OK", null);
                            builder.setNegativeButton(null, null);
                            builder.setCancelable(true).show();

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

    public void startingupload(){
        progressBar.setVisibility(View.VISIBLE);
        upload_button.setEnabled(false);

    }

    public void finishedupload(){
        progressBar.setVisibility(View.INVISIBLE);
        upload_button.setEnabled(true);

    }



}
