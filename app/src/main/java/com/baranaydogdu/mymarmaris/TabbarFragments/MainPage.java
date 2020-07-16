package com.baranaydogdu.mymarmaris.TabbarFragments;
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
import com.baranaydogdu.mymarmaris.EventActivity;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.PlaceActivities.InsideLinkCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PharmaciesActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceSubCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlacesActivity;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.Services.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import io.realm.Realm;

public class MainPage extends AppCompatActivity {

    DrawerLayout drawer ;
    ConstraintLayout progress_cons;
    ConstraintLayout progressevent_cons;
    private BottomNavigationView buttomnavigation;
    private ViewPager viewpager;
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    private MenuItem prevMenuItem;
    SharedPreferences sharedPreferences;
    Intent source_intent;
    Activity activity;
    ArrayList<Fragment> viewPagerFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        progress_cons = findViewById(R.id.progress_cons);
        progressevent_cons = findViewById(R.id.progressevent_cons);

        FragmentMainCollections fragmentMainCollections = new FragmentMainCollections(progress_cons,progressevent_cons);
        SearchMainFragment searchMainFragment = new SearchMainFragment();
        FragmentFavorittes fragmentFavorittes = new FragmentFavorittes();
        FragmentNotifications fragmentNotifications = new FragmentNotifications();
        SettingFragment settingFragment = new SettingFragment(searchMainFragment,fragmentFavorittes);

        viewPagerFragments.add(fragmentMainCollections);
        viewPagerFragments.add(searchMainFragment);
        viewPagerFragments.add(fragmentFavorittes);
        viewPagerFragments.add(fragmentNotifications);
        viewPagerFragments.add(settingFragment);

        drawer = findViewById(R.id.drawer_layout);
        buttomnavigation = findViewById(R.id.buttomnavigation);
        viewpager=findViewById(R.id.viewpager);
        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        activity=this;

        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(adapter);
        source_intent=getIntent();

        createbuttomnavigation();
        checkSourceIntent();
        checktopic();
    }

    private void checkSourceIntent(){
        if (source_intent.getBooleanExtra("from_click",false)){
            System.out.println("FROM CLICK MAIN PAGE");
            String act      = source_intent.getStringExtra("act");
            String click_id = source_intent.getStringExtra("id");

            System.out.println("MAIN PAGE CLICK ACT act: "+act+" id: "+click_id);

            if (act.equals(NotificationClass.TO_PLACE)){

                Intent intent = new Intent(activity, PlaceView.class);
                intent.putExtra("id", click_id);
                activity.startActivity(intent);

            } else if (act.equals(NotificationClass.TO_EVENT)){

                Intent intent = new Intent(activity, EventView.class);
                intent.putExtra("id", click_id);
                activity.startActivity(intent);

            } else if (act.equals(NotificationClass.TO_SUB)){

                Intent intent=new Intent(activity, PlacesActivity.class);
                intent.putExtra("id",click_id);
                intent.putExtra("index",0);
                activity.startActivity(intent);

            }else if (act.equals(NotificationClass.TO_MAIN)){

                PlaceCollectionClass noticy_maincollection= new PlaceCollectionClass();//PreSets.getmaincollection_from_MainCollectionId(activity,click_id);

                if (noticy_maincollection.getType()==PlaceCollectionClass.COLLECTION_WITH_SUB) {
                    Intent intent = new Intent(activity, PlaceSubCollectionActivity.class);
                    intent.putExtra("id", noticy_maincollection.getId());
                    activity.startActivity(intent);

                }else if(noticy_maincollection.getType()==PlaceCollectionClass.COLLECTION_NON_SUB){
                    Intent intent = new Intent(activity, PlacesActivity.class);
                    intent.putExtra("id", noticy_maincollection.getId());
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


            }
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

    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
           return viewPagerFragments.get(position);

        }

        @Override
        public int getCount() {

            return 5;

        }

    }
}