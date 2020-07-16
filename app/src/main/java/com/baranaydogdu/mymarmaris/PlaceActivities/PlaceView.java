package com.baranaydogdu.mymarmaris.PlaceActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.EventForRealm;
import com.baranaydogdu.mymarmaris.Classes.FavoritteRealm;
import com.baranaydogdu.mymarmaris.Classes.MyMediaController;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.VideoFullScrenn;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class PlaceView extends AppCompatActivity implements LocationListener {

    Intent intent;
    Activity activity;
    LocationManager locationManager;
    public static final int LOCATIONMILISECOND = 1000;
    public PlaceClass place;
    int lan;
    public TextView main_tv, event_range;
    public LinearLayout layout;
    public EditText event_name, event_explain;
    public SharedPreferences sharedPreferences;
    public CardView adress_carview, phone_cardview, whastapp_cardview, web_cardview, insta_cardview, face_cardview, mail_cardview, buyticket;
    public EditText adress;
    public EditText adresse_edx, adres_seeroute_edx, phone_edx, whatsapp_edx, web_edx, face_edx, insta_edx, mail_edx, buyticket_edx;
    public AlertDialog.Builder builder;
    public ImageSlıdeAdapter adapter;
    public ViewPager viewpager;
    public ConstraintLayout viewpager_cons;
    public LinearLayout viewpager_linear, down_photo_linear;
    public ConstraintLayout videocons;
    public VideoView videoView;
    public ImageView videoimage, map_image;
    public ProgressBar videoprogress;
    public Timer timer;
    public ScrollView scrollview;
    public int viewpagerposition = 0;
    public ConstraintLayout eventview_maincons;
    public View view;
    public ArrayList<ImageSlideFragment> fragmentlist;
    String place_id;
    ImageView hearticon;
    TextView close_tv, new_tv;
    Boolean isopen = true;
    EditText work_times_edx;
    TextView tv_open, tv_close, tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday, tv_saturday, tv_sunday;
    EditText edx_open0, edx_open1, edx_open2, edx_open3, edx_open4, edx_open5, edx_open6;
    EditText edx_close0, edx_close1, edx_close2, edx_close3, edx_close4, edx_close5, edx_close6;
    RecyclerView eventrecyclerview;
    EditText events_edx;
    ArrayList<EventClass> eventsList = new ArrayList<>();
    LinearLayout place_events;
    ImageView fullscrenn_icon;
    Boolean isvideo_okey = false;
    MyMediaController mediaController;

    LanguagePack languagePack = new LanguagePack();
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        intent = getIntent();
        place_id = intent.getStringExtra("id");
        initviews();

        eventsList = new ArrayList<>();
        fragmentlist = new ArrayList<>();
        activity = this;
        mediaController = new MyMediaController(activity);

        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);

        adapter = new ImageSlıdeAdapter(getSupportFragmentManager());

        RealmResults<PlaceForRealm> placeRealm = realm.where(PlaceForRealm.class).equalTo("id", place_id).findAll();
        if (placeRealm.size() > 0) {
            place = placeRealm.first().toPlace();

        } else {
            place = new PlaceClass();

        }

        initviews();


        if (place.getTopphotos().size() > 0) {
            for (int i = 0; i < place.getTopphotos().size(); i++) {
                fragmentlist.add(new ImageSlideFragment(this,place.topphotos.get(i)));
            }
            if (place.getTopphotos().size() > 1) {
                createImageSlider();
            } else {
                layout.removeView(viewpager_linear);
            }
        }

        viewpager.setOffscreenPageLimit(fragmentlist.size());
        viewpager.setAdapter(adapter);

        event_range.setVisibility(View.INVISIBLE);

        ArrayList<View> views = new ArrayList<>();
        ArrayList<ImageView> images = new ArrayList<>();
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < place.downphotos.size(); i++) {

            views.add(layoutInflater.inflate(R.layout.image_cons_layout, down_photo_linear, false));
            images.add((ImageView) views.get(i).findViewById(R.id.down_photos_image));
            new PreSets().setImage(this,images.get(i),place.downphotos.get(i));
            down_photo_linear.addView(views.get(i));

        }

        if (place.mapphotoversion == "") {layout.removeView(map_image);
        } else {
            new PreSets().setImage(this, map_image, place.mapphotoversion);
            map_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (place.getLocation().getLat() == 0 || place.getLocation().getLat() == 0.0) {
                    } else {
                        settherouteIntent();
                    }
                }
            });
        }

        mediaController.setAnchorView(videocons);
        videoView.setMediaController(mediaController);

        adress_carview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place.getLocation().getLat() == 0 || place.getLocation().getLat() == 0.0) {
                } else {
                    settherouteIntent();
                }
            }
        });

        edx_open0.setText(setopenclosetime(place.getOpentime().get(0)));
        edx_open1.setText(setopenclosetime(place.getOpentime().get(1)));
        edx_open2.setText(setopenclosetime(place.getOpentime().get(2)));
        edx_open3.setText(setopenclosetime(place.getOpentime().get(3)));
        edx_open4.setText(setopenclosetime(place.getOpentime().get(4)));
        edx_open5.setText(setopenclosetime(place.getOpentime().get(5)));
        edx_open6.setText(setopenclosetime(place.getOpentime().get(6)));

        edx_close0.setText(setopenclosetime(place.getClosetime().get(0)));
        edx_close1.setText(setopenclosetime(place.getClosetime().get(1)));
        edx_close2.setText(setopenclosetime(place.getClosetime().get(2)));
        edx_close3.setText(setopenclosetime(place.getClosetime().get(3)));
        edx_close4.setText(setopenclosetime(place.getClosetime().get(4)));
        edx_close5.setText(setopenclosetime(place.getClosetime().get(5)));
        edx_close6.setText(setopenclosetime(place.getClosetime().get(6)));

        phone_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneIntent();
            }
        });
        whastapp_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsappIntent();
            }
        });
        web_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent();
            }
        });
        face_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceIntent();
            }
        });
        insta_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instaIntent();
            }
        });
        mail_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailIntent();
            }
        });
        buyticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butticketIntent();
            }
        });


        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (today == 1) today = 6;
        else today = today - 2;

        int opentime = place.getOpentime().get(today);
        int closetime = place.getClosetime().get(today);
        int now = (Calendar.getInstance().get(Calendar.HOUR) * 100) + (Calendar.getInstance().get(Calendar.MINUTE));
        if (Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM) now = now + 1200;

        if (opentime < closetime) {

            if (opentime < now && now < closetime) { //ACIK ISE

                isopen = true;

            } else {
                isopen = false;

            }

        } else {

            if (opentime < now || now < closetime) { //ACIK ISE

                isopen = true;

            } else {
                isopen = false;

            }

        }

        if (isopen) {
            close_tv.setBackgroundResource(R.drawable.shape_new_edittext);
        } else {
            close_tv.setBackgroundResource(R.drawable.shape_close);

        }

        if (place.getNew_end_time() < Calendar.getInstance().getTime().getTime()) {
            new_tv.setVisibility(View.INVISIBLE);

        } else {

            new_tv.setVisibility(View.VISIBLE);
        }


        setfavicon();

        hearticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FavoritteRealm.addOrDeleteFav(place.id);

                setfavicon();

            }
        });



        eventsList.clear();

        RealmResults<EventForRealm> eventForRealms = realm.where(EventForRealm.class).equalTo("linked_place",place.id).findAll();
        RealmResults<EventForRealm> eventForRealms2 = realm.where(EventForRealm.class).equalTo("linked_management",place.id).findAll();

        for (EventForRealm eventRealm : eventForRealms) {
            for (EventClass singleTimeEvent : eventRealm.multiple()) {
                eventsList.add(singleTimeEvent);
            }
        }

        for (EventForRealm eventRealm : eventForRealms2) {
            for (EventClass singleTimeEvent : eventRealm.multiple()) {
                eventsList.add(singleTimeEvent);
            }
        }

        Collections.sort(eventsList, new Comparator<EventClass>() {
            @Override
            public int compare(EventClass o1, EventClass o2) {
                return (new Date(o1.times.get(0).getStart_time())).compareTo(new Date(o2.times.get(0).getStart_time()));
            }
        });

        if (!place.getVideoversion().equals("default")) save_video();

        LinkedPlaceAdapter linkedadapter = new LinkedPlaceAdapter();
        eventrecyclerview.setAdapter(linkedadapter);
        eventrecyclerview.setLayoutManager(new LinearLayoutManager(activity));

        if (place.getTopphotos().size() == 0) layout.removeView(viewpager_cons);
        if (LanguagePack.getLanguage(place.name,lan).equals("")) layout.removeView(event_name);
        if (LanguagePack.getLanguage(place.explain,lan).equals("")) layout.removeView(event_explain);
        if (place.getVideoversion().equals("")) layout.removeView(videocons);
        if (LanguagePack.getLanguage(place.adres,lan).equals("")) layout.removeView(adress_carview);
        if (place.getContactinfo().getPhonenumber().equals("")) layout.removeView(phone_cardview);
        if (place.getContactinfo().getWhatsapp().equals("")) layout.removeView(whastapp_cardview);
        if (place.getContactinfo().getWebsite().equals("")) layout.removeView(web_cardview);
        if (place.getContactinfo().getFacebook().equals("")) layout.removeView(face_cardview);
        if (place.getContactinfo().getInstagram().equals("")) layout.removeView(insta_cardview);
        if (place.getContactinfo().getMailadress().equals("")) layout.removeView(mail_cardview);
        if (place.getContactinfo().getBuyticket().equals("")) layout.removeView(buyticket);
        if (place.getDownphotos().size() == 0) layout.removeView(down_photo_linear);
        if (eventsList.size() == 0) layout.removeView(place_events);
        if (place.getMapphotoversion().equals("")) layout.removeView(map_image);

        setlanguage();

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);
        }

    }

    private void initviews() {

        fullscrenn_icon = findViewById(R.id.fullscrenn_icon2);

        place_events = findViewById(R.id.place_events);
        eventrecyclerview = findViewById(R.id.eventrecyclerview);
        events_edx = findViewById(R.id.events_edx);

        tv_open = findViewById(R.id.textView15);
        tv_close = findViewById(R.id.textView16);

        tv_monday = findViewById(R.id.textView17);
        tv_tuesday = findViewById(R.id.textView18);
        tv_wednesday = findViewById(R.id.textView19);
        tv_thursday = findViewById(R.id.textView20);
        tv_friday = findViewById(R.id.textView24);
        tv_saturday = findViewById(R.id.textView25);
        tv_sunday = findViewById(R.id.textView26);

        edx_open0 = findViewById(R.id.open0);
        edx_open1 = findViewById(R.id.open1);
        edx_open2 = findViewById(R.id.open2);
        edx_open3 = findViewById(R.id.open3);
        edx_open4 = findViewById(R.id.open4);
        edx_open5 = findViewById(R.id.open5);
        edx_open6 = findViewById(R.id.open6);
        edx_close0 = findViewById(R.id.close0);
        edx_close1 = findViewById(R.id.close1);
        edx_close2 = findViewById(R.id.close2);
        edx_close3 = findViewById(R.id.close3);
        edx_close4 = findViewById(R.id.close4);
        edx_close5 = findViewById(R.id.close5);
        edx_close6 = findViewById(R.id.close6);

        work_times_edx = findViewById(R.id.work_times_edx);
        hearticon = findViewById(R.id.hearticon);
        new_tv = findViewById(R.id.new_tv);
        close_tv = findViewById(R.id.close_tv);

        eventview_maincons = findViewById(R.id.eventview_maincons);
        scrollview = findViewById(R.id.scrollview);
        down_photo_linear = findViewById(R.id.down_photo_linear);
        map_image = findViewById(R.id.map_image);
        videoView = findViewById(R.id.videoView2);
        videoprogress = findViewById(R.id.videoprogress);
        videocons = findViewById(R.id.video_cons);

        viewpager_linear = findViewById(R.id.viewpager_linear);
        viewpager = findViewById(R.id.viewPager);
        main_tv = findViewById(R.id.main_textView2);
        layout = findViewById(R.id.layout);
        videoimage = findViewById(R.id.videoimage);

        viewpager_cons = findViewById(R.id.viewpager_cons);
        event_range = findViewById(R.id.place_rance_tv2);
        event_name = findViewById(R.id.place_name2);
        event_explain = findViewById(R.id.place_explain2);

        adress_carview = findViewById(R.id.adresscarview);
        phone_cardview = findViewById(R.id.phone_carvdiew);
        whastapp_cardview = findViewById(R.id.whatsapp_carview);
        web_cardview = findViewById(R.id.web_carview);
        insta_cardview = findViewById(R.id.insta_carview);
        face_cardview = findViewById(R.id.facebook_carview);
        mail_cardview = findViewById(R.id.mail_carview);
        buyticket = findViewById(R.id.ticket_carview);

        adress = findViewById(R.id.adress);

        adresse_edx = findViewById(R.id.adresse_edx);
        adres_seeroute_edx = findViewById(R.id.adres_seeroute_edx);

        phone_edx = findViewById(R.id.phone_edx);
        whatsapp_edx = findViewById(R.id.whatsapp_edx);
        web_edx = findViewById(R.id.web_edx);
        face_edx = findViewById(R.id.face_edx);
        insta_edx = findViewById(R.id.insta_edx);
        mail_edx = findViewById(R.id.mail_edx);
        buyticket_edx = findViewById(R.id.ticket_edx);


    }


    public void setfavicon() {
        hearticon.setImageResource(R.drawable.heart_filled);


        if (FavoritteRealm.isFav(place.id)){
            hearticon.setImageResource(R.drawable.heart_filled);
        }else {
            hearticon.setImageResource(R.drawable.heart_emty);
        }


    }

    public String setopenclosetime(int time) {

        String htext, mtext;
        int hour = time / 100;
        int minute = time - (hour * 100);

        if (hour < 10) htext = "0" + hour;
        else htext = "" + hour;
        if (minute < 10) mtext = "0" + (int) minute;
        else mtext = "" + (int) minute;

        return htext + ":" + mtext;

    }

    private void createImageSlider() {

        viewpagerposition = viewpager.getCurrentItem();

        final ArrayList<ImageView> dotimages = new ArrayList<>();

        for (int i = 0; i < place.getTopphotos().size(); i++) {       //DOT ETKLEME

            dotimages.add(new ImageView(activity));

            if (i == viewpager.getCurrentItem()) {

                dotimages.get(i).setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.shape_oval_green));

            } else {

                dotimages.get(i).setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.shape_oval_grey));

            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 0, 4, 0);

            viewpager_linear.addView(dotimages.get(i), layoutParams);

        }


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < place.getTopphotos().size(); i++) {

                    if (position == i) {
                        dotimages.get(i).setImageResource(R.drawable.shape_oval_green);
                    } else {
                        dotimages.get(i).setImageResource(R.drawable.shape_oval_grey);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (sharedPreferences.getBoolean("auto_images", true)) {

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (viewpagerposition == fragmentlist.size())
                        viewpagerposition = 0;
                    viewpager.setCurrentItem(viewpagerposition++, true);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    handler.post(runnable);
                }
            }, 250, 2500);
        }


    }

    private void settherouteIntent() {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getLocation().getLat() + "," + place.getLocation().getLog());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();

        }


        String uri = String.format(Locale.getDefault(), "http://maps.google.com/maps?daddr=%f,%f (%s)",
                place.getLocation().getLat(), place.getLocation().getLog(), LanguagePack.getLanguage(place.name,lan));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(activity, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void phoneIntent() {

        String number = place.getContactinfo().getPhonenumber();
        if (!number.startsWith("+9")) number = "+9" + number;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);

    }

    private void whatsappIntent() {

        String number = place.getContactinfo().getWhatsapp();
        if (!number.startsWith("+9")) number = "+9" + number;

        String contact = number; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = activity.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, "Whatsapp not available " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void webIntent() {

        String url = place.getContactinfo().getWebsite();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri uri = Uri.parse(url);

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void faceIntent() {

        String url = place.getContactinfo().getFacebook();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        try {       //FACE YUKLU
            Intent faceintent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url + "/"));
            faceintent.setPackage("com.facebook.katana");
            startActivity(faceintent);

        } catch (Exception e) {     //WEB ILE AC

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            } catch (Exception ex) { //ARTIK NOLUYORSA

                Toast.makeText(activity, "ERROR : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void instaIntent() {

        String url = place.getContactinfo().getInstagram();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;


        try {        //INSTA YUKLU       //INSTA YUKLU       //INSTA YUKLU       //INSTA YUKLU       //INSTA YUKLU
            Intent insta = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            insta.setPackage("com.instagram.android");
            startActivity(insta);
        } catch (Exception e) {       //INSTA YUKLU DEGIL     //INSTA YUKLU DEGIL     //INSTA YUKLU DEGIL     //INSTA YUKLU DEGIL
            try {   //INSTAYA WEB SAYFASINDAN GIDICEZ       //INSTAYA WEB SAYFASINDAN GIDICEZ
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)));
            } catch (Exception ex) {  //KULLIYEN HATA     //KULLIYEN HATA     //KULLIYEN HATA     //KULLIYEN HATA
                Toast.makeText(activity, "ERRROR : " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void mailIntent() {

        try {           //SEND EMAIL        //SEND EMAIL        //SEND EMAIL        //SEND EMAIL        //SEND EMAIL
            //Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, place.getContactinfo().getMailadress());
            //startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + place.getContactinfo().getMailadress()));

            startActivity(Intent.createChooser(emailIntent, "Chooser Title"));


        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "No available mail account", Toast.LENGTH_SHORT).show();
        }


    }

    private void butticketIntent() {

        String url = place.getContactinfo().getBuyticket();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri uri = Uri.parse(url);

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void setrange(Location location) {

        Double lat = place.getLocation().getLat();
        Double log = place.getLocation().getLog();

        if (location != null) {

            if (lat != 0 && log != 0) {
                Location place_location = new Location("place");
                place_location.setLongitude(log);
                place_location.setLatitude(lat);

                float distance = location.distanceTo(place_location);

                if (distance < 1000) {

                    event_range.setText(((int) (distance / 10)) + "0 m");

                } else {
                    event_range.setText(((float) ((int) (distance / 100))) / 10 + " km");
                }

                event_range.setVisibility(View.VISIBLE);

            } else event_range.setVisibility(View.INVISIBLE);

        } else {

            event_range.setVisibility(View.INVISIBLE);

        }
    }

    public class ImageSlıdeAdapter extends FragmentPagerAdapter {

        public ImageSlıdeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentlist.get(position);

        }

        @Override
        public int getCount() {

            return fragmentlist.size();

        }


    }

    public class LinkedPlaceAdapter extends RecyclerView.Adapter<LinkedPlaceAdapter.LinkedPlaceViewHolder> {

        public LinkedPlaceAdapter() {

        }

        @Override
        public LinkedPlaceAdapter.LinkedPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_event_item_layout,
                    parent, false);
            return new LinkedPlaceAdapter.LinkedPlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LinkedPlaceViewHolder holder, int position) {

            final EventClass event = eventsList.get(position);

            holder.mainlineer.removeView(holder.date_carvdiew);
            holder.mainlineer.removeView(holder.event_cons);

            if (position != 0) {

                if (LanguagePack.getlanguages_date(activity, event.getTimes().get(0).getStart_time())
                        .equals(LanguagePack.getlanguages_date(activity, eventsList.get(position - 1).getTimes().get(0).getStart_time()))) {

                    holder.mainlineer.addView(holder.event_cons);

                } else {
                    holder.mainlineer.addView(holder.date_carvdiew);
                    holder.mainlineer.addView(holder.event_cons);


                }

            } else {
                holder.mainlineer.addView(holder.date_carvdiew);
                holder.mainlineer.addView(holder.event_cons);
            }


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(event.getTimes().get(0).getStart_time()));
            String minute;
            if (calendar.get(Calendar.MINUTE) < 10) {

                minute = "0" + calendar.get(Calendar.MINUTE);
            } else minute = "" + calendar.get(Calendar.MINUTE);

            holder.time_edx.setText(LanguagePack.gethour(event.times.get(0).getStart_time()) + ":" + minute);

            holder.event_name.setText(LanguagePack.getLanguage(event.name,lan));
            holder.event_explain.setText(LanguagePack.getLanguage(event.explain,lan));
            if (event.topphotos.size() > 0) new PreSets().setImage(activity,holder.event_logo,event.topphotos.get(0));
            else new PreSets().setImage(activity,holder.event_logo,"");

            holder.date_tv.setText(LanguagePack.getlanguages_date(activity, event.times.get(0).getStart_time()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, EventView.class);
                    intent.putExtra("id", event.getId());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return eventsList.size();
        }

        public class LinkedPlaceViewHolder extends RecyclerView.ViewHolder {

            TextView date_tv;
            ImageView event_logo;
            EditText time_edx, event_name, event_explain;
            LinearLayout mainlineer;
            ConstraintLayout event_cons;
            CardView date_carvdiew, link_carvdiew;
            EditText linked_edx;

            public LinkedPlaceViewHolder(View itemView) {
                super(itemView);

                event_logo = itemView.findViewById(R.id.custum_place_imageView);
                date_tv = itemView.findViewById(R.id.date_tv);
                time_edx = itemView.findViewById(R.id.custum_event_hour);
                event_name = itemView.findViewById(R.id.custum_place_name);
                event_explain = itemView.findViewById(R.id.custum_place_explain);
                mainlineer = itemView.findViewById(R.id.mainlineer);
                event_cons = itemView.findViewById(R.id.event_cons);
                date_carvdiew = itemView.findViewById(R.id.date_carvdiew);
                link_carvdiew = itemView.findViewById(R.id.link_carvdiew);
                linked_edx = itemView.findViewById(R.id.linked_edx);
                link_carvdiew.setVisibility(View.INVISIBLE);

            }
        }
    }


    private void setlanguage() {


        events_edx.setText(languagePack.eventstext[lan]);
        adresse_edx.setText(languagePack.adresses[lan]);
        adres_seeroute_edx.setText(languagePack.seetheroutes[lan]);
        phone_edx.setText(languagePack.phones[lan]);
        whatsapp_edx.setText(languagePack.whatsapps[lan]);
        web_edx.setText(languagePack.webs[lan]);
        face_edx.setText(languagePack.faces[lan]);
        insta_edx.setText(languagePack.instas[lan]);
        mail_edx.setText(languagePack.mails[lan]);
        buyticket_edx.setText(languagePack.buytickets[lan]);
        new_tv.setText(languagePack.neww[lan]);

        if (isopen) close_tv.setText(languagePack.open[lan]);
        else close_tv.setText(languagePack.close[lan]);
        work_times_edx.setText(languagePack.worktimes[lan]);

        event_name.setText(LanguagePack.getLanguage(place.name,lan));
        event_explain.setText(LanguagePack.getLanguage(place.explain,lan));
        adress.setText(LanguagePack.getLanguage(place.adres,lan));

        tv_open.setText(languagePack.open[lan]);
        tv_close.setText(languagePack.close[lan]);
        tv_monday.setText(languagePack.monday[lan]);
        tv_tuesday.setText(languagePack.Tuesday[lan]);
        tv_wednesday.setText(languagePack.Wednesday[lan]);
        tv_thursday.setText(languagePack.Thursday[lan]);
        tv_friday.setText(languagePack.Friday[lan]);
        tv_saturday.setText(languagePack.Saturday[lan]);
        tv_sunday.setText(languagePack.Sunday[lan]);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isvideo_okey) {
            videoView.seekTo(1000);
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

        }
        setrange(null);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        setrange(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void save_video() {

        final String filename = "/data/user/0/com.baranaydogdu.mymarmaris/cache/" + place.videoversion
                .replaceAll(":", "")
                .replaceAll("/", "") + ".mp4";

        final File file = new File(filename);

        if (file.exists()) {
            playvideo();

        } else {

            new DownloadAsynTask( place.videoversion, file).execute();

        }

    }

    public class DownloadAsynTask extends AsyncTask<String, Void, String> {

        Boolean taskComp = false;
        String url = "";
        File file;

        public DownloadAsynTask( String url, File file) {
            this.url = url;
            this.file = file;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (taskComp) {
                System.out.println("VIDEO DOWNLOADED");
                playvideo();
            } else {

                try {
                    layout.removeView(videocons);
                    if (file.exists()) file.delete();

                } catch (Exception ex) {
                    System.out.println("File Error : " + ex.getLocalizedMessage());
                }

            }

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL u = new URL(url);
                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;

                FileOutputStream fos = new FileOutputStream(file);
                while ((length = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                taskComp = true;

            } catch (Exception ex) {
                taskComp = false;
                System.out.println("Download Error : " + ex.getLocalizedMessage() + "url : "+url);
                try {
                    if (file.exists()) file.delete();

                } catch (Exception ex1) {
                    System.out.println("File Error : " + ex1.getLocalizedMessage());
                }
            }

            return "";
        }
    }


    public void playvideo() {

        final String filename = "/data/user/0/com.baranaydogdu.mymarmaris/cache/" + place.videoversion
                .replaceAll(":", "")
                .replaceAll("/", "") + ".mp4";

        final File file = new File(filename);

       mediaController=new MyMediaController(activity);
       mediaController.setAnchorView(videoView);
       videoView.setMediaController(mediaController);
        videoView.setVideoURI( Uri.fromFile(file) );
        videoprogress.setVisibility(View.INVISIBLE);


        if (sharedPreferences.getBoolean("auto_video",false)){

            videoView.start();
            videoimage.setVisibility(View.INVISIBLE);

        } else{

            videoimage.setVisibility(View.VISIBLE);
            videoView.seekTo(1000); //1.sn ye atla
            videoimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!videoView.isPlaying())videoView.start();
                    videoimage.setVisibility(View.INVISIBLE);
                }
            });

            videocons.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    videoimage.setVisibility(View.INVISIBLE);
                    return false;
                }
            });

        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (videoView.isPlaying()) videoView.stopPlayback();

                videoimage.setVisibility(View.VISIBLE);
                videoView.seekTo(1000);
                videoView.pause();

            }
        });


        isvideo_okey=true;
        fullscrenn_icon.setVisibility(View.VISIBLE);
        fullscrenn_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                Intent intent=new Intent(activity, VideoFullScrenn.class);
                intent.putExtra("filename",filename);
                intent.putExtra("seektime",videoView.getCurrentPosition());
                startActivity(intent);
            }
        });
    }


}
