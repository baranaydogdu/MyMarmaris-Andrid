package com.baranaydogdu.mymarmaris.Services;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Presentation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.baranaydogdu.mymarmaris.Appmain;
import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.NotificationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.EventActivity;
import com.baranaydogdu.mymarmaris.EventView;
import com.baranaydogdu.mymarmaris.GirisEkran;
import com.baranaydogdu.mymarmaris.MainPage;
import com.baranaydogdu.mymarmaris.PlaceActivities.InsideLinkCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PharmaciesActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceSubCollectionActivity;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlaceView;
import com.baranaydogdu.mymarmaris.PlaceActivities.PlacesActivity;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.ref.PhantomReference;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences sharedPreferences;
    Context context=this;
    public static int TOPICNUMBER=800;
    int selectedlamguage;
    Boolean isfollow;
    Boolean isexistss;
    int notification_id=0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("MESAJ GELDI ------");

        sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if (remoteMessage.getData().size() > 0) {
            isfollow=false;
            isexistss=false;

            String id= FirebaseDatabase.getInstance().getReference().child("tokens").push().getKey();

            Map<String, String> map= remoteMessage.getData();

            System.out.println("All message : "+remoteMessage.getData());
            selectedlamguage=sharedPreferences.getInt("language",0);

            ArrayList<String> title=new ArrayList<>();
            ArrayList<String> body=new ArrayList<>();
            ArrayList<String> towho=new ArrayList<>();
            String click_action=map.get("click_action");
            String click_id=map.get("click_id");
            int iconnumber=Integer.valueOf(map.get("iconnumber"));

            for (int i=0;i<5;i++){
                title.add(map.get("title"+i));
                body.add(map.get("body"+i));
            }

            int to_who_count=Integer.valueOf(map.get("to_who_count"));
            for (int i=0;i<to_who_count;i++){
                map.get("to_who"+i);
                if (sharedPreferences.getBoolean("notify"+map.get("to_who"+i),true)){
                    isfollow=true;
                    break;
                }
            }

            if (isfollow) {  //SHOW NOTIFICATIONS


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = id;
                    String description = id;
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(id, name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }



                long[] pattern = {500, 500, 500, 500};//Titreşim ayarı
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Intent intent = new Intent(this, GirisEkran.class);
                intent.putExtra("from_click",true);
                intent.putExtra("act",map.get("click_action"));
                intent.putExtra("id",click_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
                        .setSmallIcon(PreSets.getImage(iconnumber))
                        .setContentTitle(PreSets.get_general_language(context,title))
                        .setContentText(PreSets.get_general_language(context,body))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSound(soundUri)
                        .setVibrate(pattern)
                        .setAutoCancel(true);


                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                int m = (int) ((Calendar.getInstance().getTime().getTime() / 1000L) % Integer.MAX_VALUE);

                notificationManager.notify(m, builder.build());

            }

        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        System.out.println("REFRESH TOKEN");
        sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("topicsuccess",false).apply();

    }


}