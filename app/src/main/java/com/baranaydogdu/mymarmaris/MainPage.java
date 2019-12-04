package com.baranaydogdu.mymarmaris;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.NotificationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PlaceActivities.InsideLinkCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PharmaciesActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceSubCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlacesActivity;
import com.baranaydogdu.mymarmaris.SearcOperations.SearchMainFragment;
import com.baranaydogdu.mymarmaris.Fragments.FragmentFavorittes;
import com.baranaydogdu.mymarmaris.PlaceActivities.FragmentMainCollections;
import com.baranaydogdu.mymarmaris.Fragments.SettingFragment;
import com.baranaydogdu.mymarmaris.Fragments.FragmentNotifications;
import com.baranaydogdu.mymarmaris.Services.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.HashMap;

public class MainPage extends AppCompatActivity {

    DrawerLayout drawer;
    private BottomNavigationView buttomnavigation;
    private ViewPager viewpager;

    private MenuItem prevMenuItem;

    public ImageView popupimage;

    SharedPreferences sharedPreferences;
    String place_version,events_version;

    private DatabaseReference mref;
    ValueEventListener place_version_listener,events_version_listener;
    DatabaseReference place_version_ref,events_version_ref;
    int selected_language;

    FirebaseAuth mauth;
    Intent source_intent;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mauth=FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        selected_language=sharedPreferences.getInt("language",0);
        activity=this;

        mref= FirebaseDatabase.getInstance().getReference();

        drawer = findViewById(R.id.drawer_layout);

        viewpager=findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(adapter);

        buttomnavigation = findViewById(R.id.buttomnavigation);
        popupimage = findViewById(R.id.popupimage);

        checktopic();
        createbuttomnavigation();
        initvalueventlistener();

        PreSets.checkphotos(this);              //PLACE FOTO
        PreSets.check_eventsphotos(activity);   //EVENT FOTO
        PreSets.clearvideos(activity);

        source_intent=getIntent();

        if (source_intent.getBooleanExtra("from_click",false)){
            System.out.println("FROM CLICK MAIN PAGE");
            String act      = source_intent.getStringExtra("act");
            String click_id = source_intent.getStringExtra("id");

            System.out.println("MAIN PAGE CLICK ACT act: "+act+" id: "+click_id);

                if (act.equals(NotificationClass.TO_PLACE)){

                    try {
                        PlaceClass notifyplace=PreSets.getPlace(activity,click_id);

                        Intent intent = new Intent(activity, PlaceView.class);
                        intent.putExtra("id", notifyplace.getId());
                        activity.startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if (act.equals(NotificationClass.TO_EVENT)){

                    try {
                        EventClass notifyevent=PreSets.get_Event(activity,click_id);

                        Intent intent = new Intent(activity, EventView.class);
                        intent.putExtra("id", notifyevent.getId());
                        activity.startActivity(intent);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if (act.equals(NotificationClass.TO_SUB)){

                    try {

                        PlaceCollectionClass notifysubcollection=PreSets.getSubcollection_from_SubCollecsitonId(activity,click_id);

                        Intent intent=new Intent(activity, PlacesActivity.class);
                        ArrayList<String> idlist=new ArrayList<>();idlist.add(notifysubcollection.getId());
                        intent.putStringArrayListExtra("id",idlist);
                        intent.putExtra("index",0);
                        activity.startActivity(intent);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else if (act.equals(NotificationClass.TO_MAIN)){

                    try {
                        PlaceCollectionClass noticy_maincollection=PreSets.getmaincollection_from_MainCollectionId(activity,click_id);

                        if (noticy_maincollection.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB) {
                            Intent intent = new Intent(activity, PlaceSubCollectionActivity.class);
                            intent.putExtra("id", noticy_maincollection.getId());
                            activity.startActivity(intent);

                        }else if(noticy_maincollection.getType()==PlaceCollectionClass.COLLECTION_NON_SUB){
                            Intent intent = new Intent(activity, PlacesActivity.class);
                            ArrayList<String> idlist=new ArrayList<>();idlist.add(noticy_maincollection.getId());
                            intent.putStringArrayListExtra("id", idlist);
                            activity.startActivity(intent);

                        }else if(noticy_maincollection.getType()==PlaceCollectionClass.INSIDE_LINKKED_COLLECTION){

                            Intent intent = new Intent(activity, InsideLinkCollectionActivity.class);
                            String url = noticy_maincollection.getLink();
                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://" + url;
                            intent.putExtra("url", url);
                            activity.startActivity(intent);

                        }else if(noticy_maincollection.getType()==PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION) {

                            String url = noticy_maincollection.getLink();
                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://" + url;
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                        }else if (noticy_maincollection.getType()==PlaceCollectionClass.PHARMACY){

                            Intent intent = new Intent(activity, PharmaciesActivity.class);
                            activity.startActivity(intent);


                        } else if (noticy_maincollection.getType()==PlaceCollectionClass.ACTIVITIES){

                            Intent intent = new Intent(activity, EventActivity.class);
                            activity.startActivity(intent);

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
        }

        if (((int) ((Math.random()*10)))==7){

            if (!drawer.isDrawerOpen(GravityCompat.START))
                drawer.openDrawer(GravityCompat.START);
        }

    }

    private void checktopic() {

        Boolean topicsuccess = sharedPreferences.getBoolean("topicsuccess",false);

        if (!topicsuccess){

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(final InstanceIdResult instanceIdResult) {

                    final String token= instanceIdResult.getToken();
                    System.out.println("token : "+token);

                    final DatabaseReference my_token_ref=FirebaseDatabase.getInstance().getReference().
                            child("tokens").child(token);

                    my_token_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists() && dataSnapshot.getValue()!=null) {

                                final int old_index = Integer.valueOf(dataSnapshot.child("sort").getValue().toString());
                                sharedPreferences.edit().putInt("token_sort", old_index).apply();

                                final String topic = "topic" + (int) (old_index / MyFirebaseMessagingService.TOPICNUMBER);

                                System.out.println(topic);
                                FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        sharedPreferences.edit().putBoolean("topicsuccess", true).apply();
                                        System.out.println("TOPIC SUCCESSED FROM OLD topic: "+topic);

                                    }
                                });

                            } else {

                                final Query topic_query=FirebaseDatabase.getInstance().getReference().child("tokens").orderByChild("sort").limitToLast(1);
                                topic_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        int pre_index=-1;

                                        for (DataSnapshot ds:dataSnapshot.getChildren()){

                                            pre_index=Integer.valueOf(ds.child("sort").getValue().toString());

                                        }

                                        final int new_index= pre_index+1;
                                        sharedPreferences.edit().putInt("token_sort",new_index).apply();
                                        HashMap<String ,Object> hashMap=new HashMap<>();
                                        hashMap.put("sort",new_index);
                                        hashMap.put("time", ServerValue.TIMESTAMP);
                                        final String topic = "topic" + (int)(new_index/MyFirebaseMessagingService.TOPICNUMBER);

                                        FirebaseDatabase.getInstance().getReference().child("tokens")
                                                .child(token).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        sharedPreferences.edit().putBoolean("topicsuccess",true).apply();
                                                        System.out.println("TOPIC SUCCESSED FROM NEW topic: "+topic);

                                                    }
                                                });

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }

    }

    private void initvalueventlistener() {

        place_version_listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null){

                    if (!sharedPreferences.getString("place_version","").equals(dataSnapshot.getValue().toString())) {

                        place_version=dataSnapshot.getValue().toString();
                        PreSets.get_place_data_fromfirebase(activity,place_version);

                    } else {        //MEKAN DEGISIKLIGI YOK

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        events_version_listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null){
                    if (!sharedPreferences.getString("events_version","").equals(dataSnapshot.getValue().toString())) {

                        events_version=dataSnapshot.getValue().toString();
                        PreSets.get_events_data_fromfirebase(activity,events_version);
                    } else {        //ETKINLIK DEGISIKLIGI YOK

                    }
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
        place_version_ref.removeEventListener(place_version_listener);
        events_version_ref.removeEventListener(events_version_listener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        place_version_ref = mref.child("Places_version");
        events_version_ref=mref.child("Activityversion");

        if (PreSets.networkConnection(this)){

            place_version_ref.addValueEventListener(place_version_listener);
            events_version_ref.addListenerForSingleValueEvent(events_version_listener);

        }

    }

    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void createbuttomnavigation() {

        buttomnavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.menuitem_home:
                                viewpager.setCurrentItem(0);
                                break;

                            case R.id.menuitem_search:
                                viewpager.setCurrentItem(1);
                                break;

                            case R.id.menuitem_favoritte:
                                viewpager.setCurrentItem(2);
                                break;

                            case R.id.menuitem_notification:
                                viewpager.setCurrentItem(3);
                                break;

                            case R.id.menuitem_setting:
                                viewpager.setCurrentItem(4);
                                break;

                        }
                        return false;
                    }
                });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    buttomnavigation.getMenu().getItem(0).setChecked(false);

                buttomnavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = buttomnavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return FragmentMainCollections.getInstance();

                case 1:
                    return SearchMainFragment.getInstance();

                case 2:
                    return FragmentFavorittes.getInstance();

                case 3:
                    return FragmentNotifications.getInstance();

                case 4:
                    return SettingFragment.getInstance();

                default:
                    return null;

            }

        }

        @Override
        public int getCount() {

            return 5;

        }

    }
}