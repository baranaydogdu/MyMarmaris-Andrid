package com.baranaydogdu.mymarmaris;

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
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.MyMediaController;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.PlaceActivities.ImageSlideFragment;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlacesActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class EventView extends AppCompatActivity implements LocationListener {

    Intent intent;
    EventClass event;
    int lan;
    TextView main_tv, event_range;
    LinearLayout layout;
    EditText event_name, event_explain;
    SharedPreferences sharedPreferences;
    CardView adress_carview, phone_cardview, whastapp_cardview, web_cardview, insta_cardview, face_cardview, mail_cardview, buyticket;
    CardView linked_place_carvdiew, linked_management_carvdiew, unlinked_place_carvdiew, unlinked_management_carvdiew;
    EditText linked_place_edx, linked_managemet_edx, unlinked_place_edx, unlinked_management_edx;
    EditText adress, nextdays_edx;
    EditText adresse_edx, adres_seeroute_edx, phone_edx, whatsapp_edx, web_edx, face_edx, insta_edx, mail_edx, buyticket_edx;
    AlertDialog.Builder builder;
    LocationManager locationManager;
    public static final int LOCATIONMILISECOND = PlacesActivity.LOCATIONMILISECOND;
    public ImageSlıdeAdapter adapter;
    ViewPager viewpager;
    ConstraintLayout viewpager_cons;
    CardView date_carvdiew;
    TextView date_tv;
    LinearLayout viewpager_linear, down_photo_linear, next_days_linear;
    ConstraintLayout videocons;
    VideoView videoView;
    ImageView videoimage, map_image;
    ProgressBar videoprogress;
    Timer timer;
    ScrollView scrollview;
    int viewpagerposition = 0;
    Activity activity;
    MyMediaController mediaController;
    ConstraintLayout eventview_maincons;
    ArrayList<ImageSlideFragment> fragmentlist;
    ImageView fullscrenn_icon;
    Boolean isvideo_okey = false;
    Realm realm = Realm.getDefaultInstance();
    LanguagePack languagePack = new LanguagePack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        lan = sharedPreferences.getInt("language", 0);

        fragmentlist = new ArrayList<>();
        activity = this;
        intent = getIntent();

        EventForRealm eventForRealms = realm.where(EventForRealm.class).equalTo("id", intent.getStringExtra("id")).findFirst();

        if (eventForRealms == null) {

        } else {
            event = eventForRealms.toEvent();
        }

        initviews();
        setlanguage();

        builder = new AlertDialog.Builder(this);

        adapter = new ImageSlıdeAdapter(getSupportFragmentManager());

        if (event.getTopphotos().size() > 0) {

            for (int i = 0; i < event.getTopphotos().size(); i++) {

                fragmentlist.add(new ImageSlideFragment(activity, event.topphotos.get(i)));

            }

            if (event.getTopphotos().size() > 1) {
                createImageSlider();

            } else {
                layout.removeView(viewpager_linear);
            }
        }
        viewpager.setAdapter(adapter);
        viewpager.setAdapter(adapter);

        event_range.setVisibility(View.INVISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);
        }


        if (!event.getLinked_place().equals("")) {

            try {
                final PlaceClass linkedplace = realm.where(PlaceForRealm.class).equalTo("id", event.linked_place).findFirst().toPlace();
                linked_place_edx.setText(LanguagePack.getLanguage(linkedplace.name, lan));

                linked_place_carvdiew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, PlaceView.class);
                        intent.putExtra("id", linkedplace.getId());
                        startActivity(intent);

                    }
                });
            } catch (Exception e) {
                layout.removeView(linked_place_carvdiew);

            }


        }

        if (!event.getLinked_management().equals("")) {

            try {
                final PlaceClass linkedmanagement = realm.where(PlaceForRealm.class).equalTo("id", event.linked_management).findFirst().toPlace();
                linked_managemet_edx.setText(LanguagePack.getLanguage(linkedmanagement.name, lan));

                linked_management_carvdiew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<String> idlist = new ArrayList<>();
                        idlist.add(linkedmanagement.getId());

                        Intent intent = new Intent(activity, PlaceView.class);
                        intent.putExtra("id", linkedmanagement.getId());
                        startActivity(intent);

                    }
                });
            } catch (Exception e) {
                layout.removeView(linked_management_carvdiew);
                e.printStackTrace();
            }


        }

        ArrayList<View> views = new ArrayList<>();
        ArrayList<ImageView> images = new ArrayList<>();
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < event.downphotos.size(); i++) {

            views.add(layoutInflater.inflate(R.layout.image_cons_layout, down_photo_linear, false));
            images.add((ImageView) views.get(i).findViewById(R.id.down_photos_image));
            new PreSets().setImage(this, images.get(i), event.downphotos.get(i));
            down_photo_linear.addView(views.get(i));

        }

        ArrayList<View> timesviews = new ArrayList<>();
        ArrayList<TextView> times_edx_s = new ArrayList<>();
        ArrayList<EventTimes> nexttimes = event.seethe_nextdays();

        for (int i = 0; i < nexttimes.size(); i++) {

            EventTimes singletime = event.seethe_nextdays().get(i);

            String datetext;
            Long diff = singletime.getEnd_time() - singletime.getStart_time();

            if (diff < (12 * 1000 * 60 * 60)) {

                datetext = LanguagePack.getlanguages_date(activity, singletime.getStart_time()) + " " +
                        LanguagePack.gethour(singletime.getStart_time()) + ":" + LanguagePack.getmimute(singletime.getStart_time()) +
                        "-" + LanguagePack.gethour(singletime.getEnd_time()) + ":" + LanguagePack.getmimute(singletime.getEnd_time());
            } else {

                datetext = LanguagePack.getlanguages_date(activity, singletime.getStart_time()) + " " +
                        LanguagePack.gethour(singletime.getStart_time()) + ":" + LanguagePack.getmimute(singletime.getStart_time()) + "\n" +
                        LanguagePack.getlanguages_date(activity, singletime.getEnd_time()) + " " +
                        LanguagePack.gethour(singletime.getEnd_time()) + ":" + LanguagePack.getmimute(singletime.getEnd_time());
            }

            timesviews.add(layoutInflater.inflate(R.layout.nextdays_cons, next_days_linear, false));
            times_edx_s.add((TextView) timesviews.get(i).findViewById(R.id.date_tv));
            times_edx_s.get(i).setText(datetext);
            next_days_linear.addView(timesviews.get(i));

        }


        if (event.mapphotoversion == "") {
            layout.removeView(map_image);
        } else {
            new PreSets().setImage(this, map_image, event.mapphotoversion);
            map_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.getLocation().getLat() == 0 || event.getLocation().getLat() == 0.0) {
                    } else {
                        settherouteIntent();
                    }
                }
            });
        }


        if (!event.getVideoversion().equals("")) save_video();

        unlinked_place_edx.setText(LanguagePack.getLanguage(event.unlinked_place, lan));
        unlinked_management_edx.setText(LanguagePack.getLanguage(event.unlinked_management, lan));
        adress_carview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getLocation().getLat() == 0 || event.getLocation().getLat() == 0.0) {
                } else {
                    settherouteIntent();
                }
            }
        });

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

        if (event.getTopphotos().size() == 0) layout.removeView(viewpager_cons);
        if (LanguagePack.getLanguage(event.name, lan).equals("")) layout.removeView(event_name);
        if (LanguagePack.getLanguage(event.explain, lan).equals(""))
            layout.removeView(event_explain);
        if (event.getVideoversion().equals("")) layout.removeView(videocons);
        if (LanguagePack.getLanguage(event.adres, lan).equals(""))
            layout.removeView(adress_carview);
        if (event.getContactinfo().getPhonenumber().equals("")) layout.removeView(phone_cardview);
        if (event.getContactinfo().getWhatsapp().equals("")) layout.removeView(whastapp_cardview);
        if (event.getContactinfo().getWebsite().equals("")) layout.removeView(web_cardview);
        if (event.getContactinfo().getFacebook().equals("")) layout.removeView(face_cardview);
        if (event.getContactinfo().getInstagram().equals("")) layout.removeView(insta_cardview);
        if (event.getContactinfo().getMailadress().equals("")) layout.removeView(mail_cardview);
        if (event.getContactinfo().getBuyticket().equals("")) layout.removeView(buyticket);
        if (event.getDownphotos().size() == 0) layout.removeView(down_photo_linear);
        if (event.getMapphotoversion().equals("")) layout.removeView(map_image);
        if (event.getLinked_place().equals("")) layout.removeView(linked_place_carvdiew);
        if (event.getLinked_management().equals("")) layout.removeView(linked_management_carvdiew);
        if (LanguagePack.getLanguage(event.unlinked_place, lan).equals("")) layout.removeView(unlinked_place_carvdiew);
        if (LanguagePack.getLanguage(event.unlinked_management, lan).equals("")) layout.removeView(unlinked_management_carvdiew);


        String datetext;
        Long diff = event.seethe_nextdays().get(0).getEnd_time() - event.seethe_nextdays().get(0).getStart_time();

        if (diff < (12 * 1000 * 60 * 60)) {

            datetext = LanguagePack.getlanguages_date(activity, event.seethe_nextdays().get(0).getStart_time()) + " " +
                    LanguagePack.gethour(event.seethe_nextdays().get(0).getStart_time()) + ":" + LanguagePack.getmimute(event.seethe_nextdays().get(0).getStart_time()) +
                    "-" + LanguagePack.gethour(event.seethe_nextdays().get(0).getEnd_time()) + ":" + LanguagePack.getmimute(event.seethe_nextdays().get(0).getEnd_time());
        } else {

            datetext = LanguagePack.getlanguages_date(activity, event.seethe_nextdays().get(0).getStart_time()) + " " +
                    LanguagePack.gethour(event.seethe_nextdays().get(0).getStart_time()) + ":" + LanguagePack.getmimute(event.seethe_nextdays().get(0).getStart_time()) + "\n" +
                    LanguagePack.getlanguages_date(activity, event.seethe_nextdays().get(0).getEnd_time()) + " " +
                    LanguagePack.gethour(event.seethe_nextdays().get(0).getEnd_time()) + ":" + LanguagePack.getmimute(event.seethe_nextdays().get(0).getEnd_time());
        }

        date_tv.setText(datetext);


    }


    private void initviews() {


        nextdays_edx = findViewById(R.id.nextdays_edx);
        fullscrenn_icon = findViewById(R.id.fullscrenn_icon);
        eventview_maincons = findViewById(R.id.eventview_maincons);
        scrollview = findViewById(R.id.scrollview);
        next_days_linear = findViewById(R.id.next_days_linear);
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

        date_carvdiew = findViewById(R.id.date_carvdiew);
        date_tv = findViewById(R.id.date_tv);

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


        linked_place_carvdiew = findViewById(R.id.linked_place_carvdiew);
        linked_management_carvdiew = findViewById(R.id.linked_management_carvdiew);
        unlinked_place_carvdiew = findViewById(R.id.unlinked_place_carvdiew);
        unlinked_management_carvdiew = findViewById(R.id.unlinked_management_carvdiew);
        linked_place_edx = findViewById(R.id.linked_place_edx);
        linked_managemet_edx = findViewById(R.id.linked_managemet_edx);
        unlinked_place_edx = findViewById(R.id.unlinked_place_edx);
        unlinked_management_edx = findViewById(R.id.unlinked_management_edx);

    }


    private void createImageSlider() {

        viewpagerposition = viewpager.getCurrentItem();

        final ArrayList<ImageView> dotimages = new ArrayList<>();

        for (int i = 0; i < event.getTopphotos().size(); i++) {       //DOT ETKLEME

            dotimages.add(new ImageView(this));

            if (i == viewpager.getCurrentItem()) {

                dotimages.get(i).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shape_oval_green));

            } else {

                dotimages.get(i).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shape_oval_grey));

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

                for (int i = 0; i < event.getTopphotos().size(); i++) {

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


    private void setlanguage() {


        nextdays_edx.setText(languagePack.nextdaystext[lan]);
        adresse_edx.setText(languagePack.adresses[lan]);
        adres_seeroute_edx.setText(languagePack.seetheroutes[lan]);
        phone_edx.setText(languagePack.phones[lan]);
        whatsapp_edx.setText(languagePack.whatsapps[lan]);
        web_edx.setText(languagePack.webs[lan]);
        face_edx.setText(languagePack.faces[lan]);
        insta_edx.setText(languagePack.instas[lan]);
        mail_edx.setText(languagePack.mails[lan]);
        buyticket_edx.setText(languagePack.buytickets[lan]);
        event_name.setText(LanguagePack.getLanguage(event.name,lan));
        event_explain.setText(LanguagePack.getLanguage(event.explain,lan));
        adress.setText(LanguagePack.getLanguage(event.adres,lan));

    }


    private void settherouteIntent() {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + event.getLocation().getLat() + "," + event.getLocation().getLog());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();

        }


        String uri = String.format(Locale.getDefault(), "http://maps.google.com/maps?daddr=%f,%f (%s)",
                event.getLocation().getLat(), event.getLocation().getLog(), LanguagePack.getLanguage(event.name,lan));
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

        String number = event.getContactinfo().getPhonenumber();
        if (!number.startsWith("+9")) number = "+9" + number;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void whatsappIntent() {

        String number = event.getContactinfo().getWhatsapp();
        if (!number.startsWith("+9")) number = "+9" + number;

        String contact = number; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = this.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "Whatsapp not available " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void webIntent() {

        String url = event.getContactinfo().getWebsite();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri uri = Uri.parse(url);

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void faceIntent() {

        String url = event.getContactinfo().getFacebook();
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

                Toast.makeText(this, "ERROR : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void instaIntent() {

        String url = event.getContactinfo().getInstagram();
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
                Toast.makeText(this, "HATA : " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void mailIntent() {

        try {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + event.getContactinfo().getMailadress()));
            startActivity(Intent.createChooser(emailIntent, "Chooser Title"));


        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "No available mail account", Toast.LENGTH_SHORT).show();
        }


    }

    private void butticketIntent() {

        String url = event.getContactinfo().getBuyticket();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri uri = Uri.parse(url);

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    protected void onResume() {

        if (isvideo_okey) {
            videoView.seekTo(1000);

        }
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Double lat = event.getLocation().getLat();
        Double log = event.getLocation().getLog();

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

        } else {

            event_range.setVisibility(View.INVISIBLE);

        }
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

        final String filename = "/data/user/0/com.baranaydogdu.mymarmaris/cache/" + event.videoversion
                .replaceAll(":", "")
                .replaceAll("/", "") + ".mp4";

        final File file = new File(filename);

        if (file.exists()) {
            playvideo();

        } else {

            new DownloadAsynTask( event.videoversion, file).execute();

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



    private void playvideo() {


        final String filename = "/data/user/0/com.baranaydogdu.mymarmaris/cache/" + event.videoversion
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


}
