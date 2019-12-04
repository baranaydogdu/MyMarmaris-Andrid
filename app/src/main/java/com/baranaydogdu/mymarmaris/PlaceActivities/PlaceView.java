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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.MyMediaController;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.VideoFullScrenn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PlaceView extends AppCompatActivity implements LocationListener {

    Intent intent;
    Activity activity;
    LocationManager locationManager;
    public static final int LOCATIONMILISECOND = 1000;
    public PlaceClass place;
    int selected_language;
    public TextView main_tv, event_range;
    public LinearLayout layout;
    public EditText event_name, event_explain;
    public SharedPreferences sharedPreferences;
    public CardView adress_carview, phone_cardview, whastapp_cardview, web_cardview, insta_cardview, face_cardview, mail_cardview, buyticket;
    public EditText adress;
    public EditText adresse_edx, adres_seeroute_edx, phone_edx, whatsapp_edx, web_edx, face_edx, insta_edx, mail_edx,buyticket_edx;
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
    public MyMediaController mediaController;
    public ConstraintLayout eventview_maincons;
    public View view;
    public ArrayList<ImageSlideFragment> fragmentlist;
    String place_id;
    ImageView hearticon;
    TextView close_tv,new_tv;
    Boolean isopen=true;
    EditText work_times_edx;
    TextView tv_open,tv_close,tv_monday,tv_tuesday,tv_wednesday,tv_thursday,tv_friday,tv_saturday,tv_sunday;
    EditText edx_open0,edx_open1,edx_open2,edx_open3,edx_open4,edx_open5,edx_open6;
    EditText edx_close0,edx_close1,edx_close2,edx_close3,edx_close4,edx_close5,edx_close6;
    RecyclerView eventrecyclerview;
    EditText events_edx;
    ArrayList<String> eventsList;
    LinearLayout place_events;
    ArrayList<EventClass> extendeteventlist_for_sort;
    ImageView fullscrenn_icon;
    Boolean isvideo_okey=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        intent=getIntent();
        place_id=intent.getStringExtra("id");
        initviews();

        eventsList=new ArrayList<>();
        fragmentlist = new ArrayList<>();
        activity = this;
        mediaController = new MyMediaController(activity);

        place = PreSets.getPlace(activity, place_id);

        initviews();

        builder = new AlertDialog.Builder(activity);

        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);

        adapter = new ImageSlıdeAdapter(getSupportFragmentManager());

        if (place.getTopphotos().size() > 0) {

            for (int i = 0; i < place.getTopphotos().size(); i++) {

                fragmentlist.add(new ImageSlideFragment( place.getId(), i));

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
            PreSets.set_DOWN_photo(images.get(i), place.getId(), i);
            down_photo_linear.addView(views.get(i));

        }


        PreSets.set_MAP_photo(map_image, place.getId());
        map_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place.getLocation().getLat() == 0 || place.getLocation().getLat() == 0.0) {
                } else {
                    settherouteIntent();
                }
            }
        });

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



        int today= Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (today==1) today=6;else today=today-2;

        int opentime=place.getOpentime().get(today);
        int openhour=opentime/100;
        int openminute=opentime-(openhour*100);
        int openAM_PM;
        if (openhour>11){
            openhour=openhour-12;
            openAM_PM=Calendar.PM;
        } else {openAM_PM=Calendar.AM;

        }

        Calendar cal_open=Calendar.getInstance();
        cal_open.set(Calendar.HOUR,openhour);cal_open.set(Calendar.MINUTE,openminute);cal_open.set(Calendar.AM_PM,openAM_PM);

        int closetime=place.getClosetime().get(today);
        int closehour=closetime/100;
        int closeminute=closetime-(closehour*100);
        int closeAM_PM;
        if (closehour>11) {
            closehour=closehour-12;closeAM_PM=Calendar.PM;
        } else closeAM_PM=Calendar.AM;

        Calendar cal_close=Calendar.getInstance();
        cal_close.set(Calendar.HOUR,closehour);cal_open.set(Calendar.MINUTE,closeminute);cal_close.set(Calendar.AM_PM,closeAM_PM);

        if (!cal_open.getTime().after(Calendar.getInstance().getTime())) {

            if (!Calendar.getInstance().getTime().after(cal_close.getTime())){

                isopen=true;
                close_tv.setBackgroundResource(R.drawable.shape_new_edittext);
            }else {

                isopen=false;
                close_tv.setBackgroundResource(R.drawable.shape_close);
            }

        }else {

            isopen=false;
            close_tv.setBackgroundResource(R.drawable.shape_close);
        }


        if (place.getNew_end_time()< Calendar.getInstance().getTime().getTime()){
            new_tv.setVisibility(View.INVISIBLE);

        } else {

            new_tv.setVisibility(View.VISIBLE);
        }



        setfavicon();

        hearticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreSets.isfav(activity,place_id)){

                    PreSets.delete_item_from_favlist(activity,place.getId());

                }else {

                    PreSets.add_item_to_favlist(activity,place.getId());
                }

                setfavicon();

            }
        });



        eventsList=PreSets.getEventIdlist_from_placeId(activity,place.getId());

        System.out.println("list size: "+eventsList.size());
        extendeteventlist_for_sort=new ArrayList<>();
        extendeteventlist_for_sort.clear();

        for (int i=0;i<eventsList.size();i++){

            EventClass fulldays_event=PreSets.get_Event(activity,eventsList.get(i));

            for (int j=0;j<fulldays_event.seethe_nextdays().size();j++){

                EventTimes singletime=fulldays_event.seethe_nextdays().get(j);

                EventClass extented_sinle_event=PreSets.get_Event(activity,fulldays_event.getId());
                extented_sinle_event.times.clear();
                extented_sinle_event.times.add(singletime);

                extendeteventlist_for_sort.add(extented_sinle_event);

            }

        }

        System.out.println("extendeteventlist_for_sort.size() : "+extendeteventlist_for_sort.size());

        Collections.sort(extendeteventlist_for_sort, new Comparator<EventClass>() {
            @Override
            public int compare(EventClass o1, EventClass o2) {
                return (new Date(o1.seethe_nextdays().get(0).getStart_time())).
                        compareTo(new Date(o2.seethe_nextdays().get(0).getStart_time()));
            }
        });


        save_video();

        LinkedPlaceAdapter linkedadapter =new LinkedPlaceAdapter();
        eventrecyclerview.setAdapter(linkedadapter);
        eventrecyclerview.setLayoutManager(new LinearLayoutManager(activity));

        if (place.getTopphotos().size() == 0) layout.removeView(viewpager_cons);
        if (PreSets.setlanguage_name(activity, place).equals("")) layout.removeView(event_name);
        if (PreSets.setlanguage_explain(activity, place).equals(""))  layout.removeView(event_explain);
        if (place.getVideoversion().equals("default")) layout.removeView(videocons);
        if (PreSets.setlanguage_adress(activity, place).equals(""))  layout.removeView(adress_carview);
        if (place.getContactinfo().getPhonenumber().equals("")) layout.removeView(phone_cardview);
        if (place.getContactinfo().getWhatsapp().equals("")) layout.removeView(whastapp_cardview);
        if (place.getContactinfo().getWebsite().equals("")) layout.removeView(web_cardview);
        if (place.getContactinfo().getFacebook().equals("")) layout.removeView(face_cardview);
        if (place.getContactinfo().getInstagram().equals("")) layout.removeView(insta_cardview);
        if (place.getContactinfo().getMailadress().equals("")) layout.removeView(mail_cardview);
        if (place.getContactinfo().getBuyticket().equals("")) layout.removeView(buyticket);
        if (place.getDownphotos().size() == 0) layout.removeView(down_photo_linear);
        if (extendeteventlist_for_sort.size()==0) layout.removeView(place_events);
        if (place.getMapphotoversion().equals("default")) layout.removeView(map_image);

        setlanguage();

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATIONMILISECOND, 0, this);
        }

    }

    private void initviews() {

        fullscrenn_icon     = findViewById(R.id.fullscrenn_icon2);

        place_events        = findViewById(R.id.place_events);
        eventrecyclerview   = findViewById(R.id.eventrecyclerview);
        events_edx          = findViewById(R.id.events_edx);

        tv_open             = findViewById(R.id.textView15);
        tv_close            = findViewById(R.id.textView16);

        tv_monday           = findViewById(R.id.textView17);
        tv_tuesday          = findViewById(R.id.textView18);
        tv_wednesday        = findViewById(R.id.textView19);
        tv_thursday         = findViewById(R.id.textView20);
        tv_friday           = findViewById(R.id.textView24);
        tv_saturday         = findViewById(R.id.textView25);
        tv_sunday           = findViewById(R.id.textView26);

        edx_open0           = findViewById(R.id.open0);
        edx_open1           = findViewById(R.id.open1);
        edx_open2           = findViewById(R.id.open2);
        edx_open3           = findViewById(R.id.open3);
        edx_open4           = findViewById(R.id.open4);
        edx_open5           = findViewById(R.id.open5);
        edx_open6           = findViewById(R.id.open6);
        edx_close0          = findViewById(R.id.close0);
        edx_close1          = findViewById(R.id.close1);
        edx_close2          = findViewById(R.id.close2);
        edx_close3          = findViewById(R.id.close3);
        edx_close4          = findViewById(R.id.close4);
        edx_close5          = findViewById(R.id.close5);
        edx_close6          = findViewById(R.id.close6);

        work_times_edx  = findViewById(R.id.work_times_edx);
        hearticon       = findViewById(R.id.hearticon);
        new_tv          = findViewById(R.id.new_tv);
        close_tv        = findViewById(R.id.close_tv);

        eventview_maincons  = findViewById(R.id.eventview_maincons);
        scrollview          = findViewById(R.id.scrollview);
        down_photo_linear   = findViewById(R.id.down_photo_linear);
        map_image           = findViewById(R.id.map_image);
        videoView           = findViewById(R.id.videoView2);
        videoprogress       = findViewById(R.id.videoprogress);
        videocons           = findViewById(R.id.video_cons);

        viewpager_linear    = findViewById(R.id.viewpager_linear);
        viewpager           = findViewById(R.id.viewPager);
        main_tv             = findViewById(R.id.main_textView2);
        layout              = findViewById(R.id.layout);
        videoimage          = findViewById(R.id.videoimage);

        viewpager_cons      = findViewById(R.id.viewpager_cons);
        event_range         = findViewById(R.id.place_rance_tv2);
        event_name          = findViewById(R.id.place_name2);
        event_explain       = findViewById(R.id.place_explain2);

        adress_carview      = findViewById(R.id.adresscarview);
        phone_cardview      = findViewById(R.id.phone_carvdiew);
        whastapp_cardview   = findViewById(R.id.whatsapp_carview);
        web_cardview        = findViewById(R.id.web_carview);
        insta_cardview      = findViewById(R.id.insta_carview);
        face_cardview       = findViewById(R.id.facebook_carview);
        mail_cardview       = findViewById(R.id.mail_carview);
        buyticket           = findViewById(R.id.ticket_carview);

        adress              = findViewById(R.id.adress);

        adresse_edx         = findViewById(R.id.adresse_edx);
        adres_seeroute_edx  = findViewById(R.id.adres_seeroute_edx);

        phone_edx           = findViewById(R.id.phone_edx);
        whatsapp_edx        = findViewById(R.id.whatsapp_edx);
        web_edx             = findViewById(R.id.web_edx);
        face_edx            = findViewById(R.id.face_edx);
        insta_edx           = findViewById(R.id.insta_edx);
        mail_edx            = findViewById(R.id.mail_edx);
        buyticket_edx       = findViewById(R.id.ticket_edx);


    }


    public void setfavicon(){
        if (PreSets.isfav(activity,place_id)){
            hearticon.setImageResource(R.drawable.heart_filled);
        }else {
            hearticon.setImageResource(R.drawable.heart_emty);
        }
    }

    public String setopenclosetime(int time){

        String htext,mtext;
        int hour=time/100;
        int minute= time - (hour*100);

        if (hour<10) htext="0"+hour; else htext=""+hour;
        if (minute<10) mtext="0"+(int)minute; else mtext=""+(int)minute;

        return htext+":"+mtext;

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


        if (sharedPreferences.getBoolean("auto_images",true)){

            final Handler handler=new Handler();
            final Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    if (viewpagerposition==fragmentlist.size())
                        viewpagerposition=0;
                    viewpager.setCurrentItem(viewpagerposition++,true);
                }
            };

            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    handler.post(runnable);
                }
            },250,2500);
        }



    }

    private void settherouteIntent() {

        String uri = String.format(Locale.getDefault(), "http://maps.google.com/maps?daddr=%f,%f (%s)",
                place.getLocation().getLat(), place.getLocation().getLog(), PreSets.setlanguage_name(activity, place));
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

    public void setrange(Location location){

        Double lat = place.getLocation().getLat();
        Double log = place.getLocation().getLog();

        if (location!=null){

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

    public class LinkedPlaceAdapter extends RecyclerView.Adapter<LinkedPlaceAdapter.LinkedPlaceViewHolder>{

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

            final EventClass event=extendeteventlist_for_sort.get(position);

            holder.mainlineer.removeView(holder.date_carvdiew);
            holder.mainlineer.removeView(holder.event_cons);

            if (position!=0){

                if (PreSets.getlanguages_date(activity,event.getTimes().get(0).getStart_time())
                        .equals(PreSets.getlanguages_date(activity,extendeteventlist_for_sort.get(position-1).getTimes().get(0).getStart_time()))){

                    holder.mainlineer.addView(holder.event_cons);

                } else {
                    holder.mainlineer.addView(holder.date_carvdiew);
                    holder.mainlineer.addView(holder.event_cons);


                }

            } else {
                holder.mainlineer.addView(holder.date_carvdiew);
                holder.mainlineer.addView(holder.event_cons);
            }


            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date(event.getTimes().get(0).getStart_time()));
            String minute;
            if (calendar.get(Calendar.MINUTE)<10){

                minute="0"+calendar.get(Calendar.MINUTE);
            }else minute=""+calendar.get(Calendar.MINUTE);

            holder.time_edx.setText(PreSets.gethour(event.seethe_nextdays().get(0).getStart_time())+":"+ minute);

            holder.event_name.setText(PreSets.setlanguage_name(activity,event));
            holder.event_explain.setText(PreSets.setlanguage_explain(activity,event));
            PreSets.set_MAIN_photo(holder.event_logo,event.getId());
            holder.date_tv.setText(PreSets.getlanguages_date(activity,event.seethe_nextdays().get(0).getStart_time()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, EventView.class);
                    intent.putExtra("id",event.getId());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return extendeteventlist_for_sort.size();
        }

        public class LinkedPlaceViewHolder extends RecyclerView.ViewHolder {

            TextView date_tv;
            ImageView event_logo;
            EditText time_edx,event_name,event_explain;
            LinearLayout mainlineer;
            ConstraintLayout event_cons;
            CardView date_carvdiew,link_carvdiew;
            EditText linked_edx;

            public LinkedPlaceViewHolder(View itemView) {
                super(itemView);

                event_logo = itemView.findViewById(R.id.custum_place_imageView);
                date_tv    = itemView.findViewById(R.id.date_tv);
                time_edx   = itemView.findViewById(R.id.custum_event_hour);
                event_name = itemView.findViewById(R.id.custum_place_name);
                event_explain = itemView.findViewById(R.id.custum_place_explain);
                mainlineer = itemView.findViewById(R.id.mainlineer);
                event_cons = itemView.findViewById(R.id.event_cons);
                date_carvdiew=itemView.findViewById(R.id.date_carvdiew);
                link_carvdiew=itemView.findViewById(R.id.link_carvdiew);
                linked_edx=itemView.findViewById(R.id.linked_edx);
                link_carvdiew.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();


    }


    private void setlanguage() {

        String[] adresses = {"Address", "Adres", "адрес", "διεύθυνση", "Address"};
        String[] seetheroutes = {"See the route", "Yol tarifi al", "посмотреть маршрут", "δείτε τη διαδρομή", "siehe die Route"};
        String[] phones = {"Phone", "Telefon", "Телефон", "τηλέφωνο", "Telefon"};
        String[] whatsapps = {"Whatsapp", "Whatsapp", "Whatsapp", "Whatsapp", "Whatsapp"};
        String[] webs = {"WEB", "İnternet Sitesi", "Веб-сайт", "δικτυακός τόπος", "Webseite"};
        String[] faces = {"Facebook", "Facebook", "Facebook", "Facebook", "Facebook"};
        String[] instas = {"Instagram", "Instagram", "Instagram", "Instagram", "Instagram"};
        String[] mails = {"Mail", "Mail", "Mail", "Mail", "Mail"};
        String[] buytickets = {"Buy Ticket", "Bilet Al", "Buy Ticket", "Buy Ticket", "Buy Ticket"};

        String[] neww = {"NEW", "YENİ", "NEW", "NEW", "NEW"};
        String[] open = {"OPEN", "AÇIK", "OPEN", "OPEN", "OPEN"};
        String[] close = {"CLOSE", "KAPALI", "CLOSE", "CLOSE", "CLOSE"};
        String[] worktimes = {"Work Times", "Çalışma Saatleri", "Work Times", "Work Times", "Work Times"};
        String[] evenst     ={"Events","Etkinlikler","Events","Events","Events"};

        String[] monday     ={"Monday" ,  "Pazartesi" ,"понедельник" ,"Δευτέρα" ,    "Montag"   };
        String[] Tuesday    ={"Tuesday",  "Salı"  ,    "вторник" ,    "Τρίτι",       "Dienstag"  };
        String[] Wednesday  ={"Wednesday","Çarşamba"  ,"среда" ,      "Τετάρτη"     ,"Mittwoch"     };
        String[] Thursday   ={"Thursday" ,"Perşembe"  ,"четверг" ,    "Πέμπτη"      ,"Donnerstag"    };
        String[] Friday     ={"Friday"   ,"Cuma"  ,    "пятница" ,    "Παρασκευή"   ,"Freitag"      };
        String[] Saturday   ={"Saturday" ,"Cumartesi", "суббота" ,    "Σάββατο"     ,"Samstag" }    ;
        String[] Sunday     ={"Sunday"   ,"Pazar" ,    "воскресенье" ,"Κυριακή"     ,"Sonntag"     };

        events_edx.setText(evenst[selected_language]);
        adresse_edx.setText(adresses[selected_language]);
        adres_seeroute_edx.setText(seetheroutes[selected_language]);
        phone_edx.setText(phones[selected_language]);
        whatsapp_edx.setText(whatsapps[selected_language]);
        web_edx.setText(webs[selected_language]);
        face_edx.setText(faces[selected_language]);
        insta_edx.setText(instas[selected_language]);
        mail_edx.setText(mails[selected_language]);
        buyticket_edx.setText(buytickets[selected_language]);

        new_tv.setText(neww[selected_language]);
        if (isopen) close_tv.setText(open[selected_language]);
        else close_tv.setText(close[selected_language]);
        work_times_edx.setText(worktimes[selected_language]);

        event_name.setText(PreSets.setlanguage_name(activity, place));
        event_explain.setText(PreSets.setlanguage_explain(activity, place));
        adress.setText(PreSets.setlanguage_adress(activity, place));

        tv_open     .setText(open     [selected_language]);
        tv_close    .setText(close    [selected_language]);
        tv_monday   .setText(monday   [selected_language]);
        tv_tuesday  .setText(Tuesday  [selected_language]);
        tv_wednesday.setText(Wednesday[selected_language]);
        tv_thursday .setText(Thursday [selected_language]);
        tv_friday   .setText(Friday   [selected_language]);
        tv_saturday .setText(Saturday [selected_language]);
        tv_sunday   .setText(Sunday   [selected_language]);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isvideo_okey){
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

        final SQLiteDatabase database = PreSets.open_saved_video_database_database(activity);

        String selectmainQuery = "SELECT * FROM saved_video_database WHERE file_Id= '" + place.getId() + "'";
        Cursor cursor = database.rawQuery(selectmainQuery, null);

        int videoversion_Ix = cursor.getColumnIndex("videoversion");
        int vsavedsortnumber_Ix = cursor.getColumnIndex("vsavedsortnumber");

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {    //DATABASE DE VAR ISE

            String videoversion = cursor.getString(videoversion_Ix);
            int vsavedsortnumber = Integer.parseInt(cursor.getString(vsavedsortnumber_Ix));

            if ((videoversion.equals(place.getVideoversion()))) {    //VIDEO GUNCEL ISE


                if (vsavedsortnumber > 0) {    //DAHA ONCE INDIRMIS ISEK
                    if (PreSets.getvideo_File(place.getId()).exists()) {    //VIDEO DA VAR ISE

                        playvideo();

                    } else {    //VIDEO DOSYASI YOK ISE

                        database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + (PreSets.UNDOWNLOADED) + "' WHERE file_Id = '" + place.getId() + "' ");
                        download_video();

                    }

                } else {        //VIDEO DOWNLOAD EDILMEMIS ISE

                    if (vsavedsortnumber == PreSets.EMPTYVIDEO) {// VIDEO FIREBASE DE YOK ISE
                        if (PreSets.getvideo_File(place.getId()).exists()) {     //BIZDE VAR ISE DE SIL
                            PreSets.getvideo_File(place.getId()).delete();

                        }
                        //HIC BISEY YAPMA

                    } else {    //VIDEO VAR ISE

                        database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + (PreSets.UNDOWNLOADED) + "' WHERE file_Id = '" + place.getId() + "' ");
                        download_video();
                    }
                }

            } else {     //VIDEO GUNCELLENMIS ISE

                if (place.getVideoversion().equals("default")) { //VIDEO SILINMIS ISE

                    database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + String.valueOf(PreSets.EMPTYVIDEO) + "' WHERE file_Id = '" + place.getId() + "' ");
                    database.execSQL("UPDATE saved_video_database SET videoversion = '" + place.getVideoversion() + "' WHERE file_Id = '" + place.getId() + "' ");


                    if (PreSets.getvideo_File(place.getId()).exists()) {     //BIZDE VAR ISE DE SIL
                        PreSets.getvideo_File(place.getId()).delete();
                    }

                } else {    //VIDEO DEGISMIS ISE

                    database.execSQL("UPDATE saved_video_database SET videoversion = '" + place.getVideoversion() + "' WHERE file_Id = '" + place.getId() + "' ");
                    database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + String.valueOf(PreSets.UNDOWNLOADED) + "' WHERE file_Id = '" + place.getId() + "' ");
                    download_video();


                }

            }

        } else {        //VIDEO YENI ISE


            if (place.getVideoversion().equals("default")) { //VIDEO YOK ISE

                String sqlstring = PreSets.get_saved_video_database_insertString();
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);

                sqLiteStatement.bindString(1, place.getId());
                sqLiteStatement.bindString(2, place.getVideoversion());
                sqLiteStatement.bindString(3, String.valueOf(PreSets.EMPTYVIDEO));
                sqLiteStatement.execute();

            } else {

                String sqlstring = PreSets.get_saved_video_database_insertString();
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);

                sqLiteStatement.bindString(1, place.getId());
                sqLiteStatement.bindString(2, place.getVideoversion());
                sqLiteStatement.bindString(3, String.valueOf(PreSets.UNDOWNLOADED));
                sqLiteStatement.execute();

                download_video();
            }
        }
        database.close();
    }

    private void download_video() {

        System.out.println("download video ---- ");



        if (PreSets.networkConnection(activity)) {

            final SQLiteDatabase database = PreSets.open_saved_video_database_database(activity);

            database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + String.valueOf(PreSets.CONTINUE) + "' WHERE file_Id = '" + place.getId() + "' ");

                final String videoFirebaseposition = PreSets.firebase_videoname(place.getId());
                StorageReference newstorageReference = FirebaseStorage.getInstance().getReference().child(videoFirebaseposition);
                newstorageReference.getFile(PreSets.getvideo_File(place.getId())).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                        Cursor cursor = database.rawQuery("SELECT * FROM saved_video_database ", null);
                        int vsavednumber_Ix = cursor.getColumnIndex("vsavedsortnumber");
                        cursor.moveToFirst();

                        int sort = 1;
                        while (!cursor.isAfterLast()) {

                            int databasesort = Integer.valueOf(cursor.getString(vsavednumber_Ix));

                            if (databasesort > sort) sort = databasesort;

                            cursor.moveToNext();
                        }

                        database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + (sort + 1) + "' WHERE file_Id = '" + place.getId() + "' ");
                        database.execSQL("UPDATE saved_video_database SET videoversion = '" + place.getVideoversion() + "' WHERE file_Id = '" + place.getId() + "' ");

                        playvideo();
                        database.close();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        layout.addView(videocons);

                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {

                        double progress = (double) 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();

                    }
                });





        } else layout.removeView(videocons);


    }

    public void playvideo() {

        mediaController=new MyMediaController(activity);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.fromFile(PreSets.getvideo_File(place.getId())));
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
                intent.putExtra("id",place.getId());
                intent.putExtra("seektime",videoView.getCurrentPosition());
                startActivity(intent);
            }
        });



    }


}
