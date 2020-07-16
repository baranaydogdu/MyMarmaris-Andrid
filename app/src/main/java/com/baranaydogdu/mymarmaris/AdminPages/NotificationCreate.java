package com.baranaydogdu.mymarmaris.AdminPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.EventForRealm;
import com.baranaydogdu.mymarmaris.Classes.NotificationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionForRealm;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.Services.MyFirebaseMessagingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationCreate extends AppCompatActivity {


    Activity activity;

    TextView usercount_tv, click_action_tv, progress_text;
    EditText ll1, ll2, ll3, ll4, ll5, ee1, ee2, ee3, ee4, ee5;
    Button click_button_place, click_button_event, click_button_main;
    Button click_button_sub, click_button_none, send_notification_button, send_notification_ios_button;
    LinearLayout to_cons;
    ProgressBar progressBar;

    AlertDialog.Builder builder;
    Query query;
    SharedPreferences sharedPreferences;
    ArrayList<NotificationClass> notificationClassArrayList;
    NotificationClass ios_Notifications;
    int usercount = 0;
    String progress = "";
    int topicindex = 0;

    RealmResults<PlaceCollectionForRealm> maincollections;
    ArrayList<View> views;
    ArrayList<Switch> switches;
    ArrayList<ImageView> icons;

    ArrayList<String> sendingcollection;

    public static final int SELECT_MAIN_COLLECTION_REQUEST = 2;
    public static final int SELECT_SUB_COLLECTION_REQUEST = 3;
    public static final int SELECT_EVENT_REQUEST = 4;
    public static final int SELECT_PLACE_REQUEST = 5;
    Realm realm = Realm.getDefaultInstance();

    int lan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_create);


        activity = this;
        notificationClassArrayList = new ArrayList<>();
        sendingcollection = new ArrayList<>();
        maincollections = realm.where(PlaceCollectionForRealm.class).equalTo("masterid", "main").findAll().sort("sortnumber");
        views = new ArrayList<>();
        switches = new ArrayList<>();
        icons = new ArrayList<>();

        builder = new AlertDialog.Builder(activity);
        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("click_action", NotificationClass.TO_NONE).apply();
        sharedPreferences.edit().putString("click_id", "default").apply();
        lan = sharedPreferences.getInt("language", 0);


        initviews();

        query = FirebaseDatabase.getInstance().getReference().child("tokens").orderByChild("sort").limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        usercount = Integer.valueOf(ds.child("sort").getValue().toString());
                    }

                    usercount_tv.setText("User Count : " + usercount);
                    topicindex = usercount / MyFirebaseMessagingService.TOPICNUMBER;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        click_button_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SelectClon.class);
                intent.putExtra("requestcode",5);
                startActivity(intent);
            }
        });

        click_button_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SelectClon.class);
                intent.putExtra("requestcode",4);
                startActivity(intent);
            }
        });

        click_button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SelectClon.class);
                intent.putExtra("requestcode",2);
                startActivity(intent);
            }
        });

        click_button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SelectClon.class);
                intent.putExtra("requestcode",3);
                startActivity(intent);
            }
        });




        click_button_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences.edit().putString("click_action", NotificationClass.TO_NONE).apply();
                sharedPreferences.edit().putString("click_id", "default").apply();

            }
        });

        send_notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendnotification();
            }
        });

        send_notification_ios_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_Ios_notification();
            }
        });


        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < maincollections.size(); i++) {

            final int positition = i;
            views.add(layoutInflater.inflate(R.layout.recyclerview_notification_single_item, to_cons, false));
            switches.add((Switch) views.get(i).findViewById(R.id.notificication_switch));
            switches.get(i).setText(LanguagePack.getLanguage(maincollections.get(i).name, lan));
            switches.get(i).setChecked(false);
            switches.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (switches.get(positition).isChecked()) {
                        sendingcollection.add(maincollections.get(positition).getId());
                    } else {
                        sendingcollection.remove(maincollections.get(positition).getId());
                    }
                }
            });
           icons.add((ImageView) views.get(i).findViewById(R.id.notification_icon));
            new PreSets().setImage(activity, icons.get(i), maincollections.get(i).icon);
            to_cons.addView(views.get(i));

        }

    }

    private void sendnotification() {


        if ((ee1.getText().toString().equals("")) || sendingcollection.size() == 0) {

            setfailuremessage("Notification body can not be emty. At least one category must be chosen");

        } else {

            ArrayList<String> title = new ArrayList<>();

            title.add(ll1.getText().toString());
            title.add(ll2.getText().toString());
            title.add(ll3.getText().toString());
            title.add(ll4.getText().toString());
            title.add(ll5.getText().toString());

            ArrayList<String> bodyy = new ArrayList<>();

            bodyy.add(ee1.getText().toString());
            bodyy.add(ee2.getText().toString());
            bodyy.add(ee3.getText().toString());
            bodyy.add(ee4.getText().toString());
            bodyy.add(ee5.getText().toString());

            String click_action = sharedPreferences.getString("click_action", NotificationClass.TO_NONE);
            String click_id = sharedPreferences.getString("click_id", "default");

            notificationClassArrayList.clear();
            for (int i = 0; i <= topicindex; i++) {

                notificationClassArrayList.add(new NotificationClass(title, bodyy, sendingcollection, click_action, click_id, i));
                System.out.println(notificationClassArrayList.get(i).getnotification_text());

            }

            addprogress("Creating Notification...");
            startsmth();

            new AsynTask().execute("https://fcm.googleapis.com/fcm/send");

        }

    }


    private void send_Ios_notification() {

        if (( ee1.getText().toString().equals(""))  || sendingcollection.size() == 0) {

            setfailuremessage("Notification body can not be emty. At least one category must be chosen");

        } else {

            ArrayList<String> title = new ArrayList<>();

            title.add(ll1.getText().toString());
            title.add(ll2.getText().toString());
            title.add(ll3.getText().toString());
            title.add(ll4.getText().toString());
            title.add(ll5.getText().toString());

            ArrayList<String> bodyy = new ArrayList<>();

            bodyy.add(ee1.getText().toString());
            bodyy.add(ee2.getText().toString());
            bodyy.add(ee3.getText().toString());
            bodyy.add(ee4.getText().toString());
            bodyy.add(ee5.getText().toString());

            String click_action = sharedPreferences.getString("click_action", NotificationClass.TO_NONE);
            String click_id = sharedPreferences.getString("click_id", "default");

            notificationClassArrayList.clear();

            ioscount = 4;
               ios_Notifications = new NotificationClass(title,bodyy,sendingcollection,
                      click_action,click_id,0);

            if (ios_Notifications.to_who.size() > 5) {
                setfailuremessage("You can not send more than five category.");

            } else {

                addprogress("Creating Ios Notification...");
                startsmth();
                new IosAsynTask().execute("https://fcm.googleapis.com/fcm/send");

            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();



            setClickAction();


    }


        private void setClickAction() {

            String click_action=sharedPreferences.getString("click_action", NotificationClass.TO_NONE);
            String click_id=sharedPreferences.getString("click_id","default");

            if (click_action.equals(NotificationClass.TO_MAIN) || click_action.equals(NotificationClass.TO_SUB)  ) {

                PlaceCollectionForRealm placeCollectionForRealm = realm.where(PlaceCollectionForRealm.class).equalTo("id",click_id).findFirst();
                click_action_tv.setText(LanguagePack.getLanguage(placeCollectionForRealm.name,lan));

            }else if (click_action.equals(NotificationClass.TO_EVENT)){

                EventForRealm eventForRealm = realm.where(EventForRealm.class).equalTo("id",click_id).findFirst();
                click_action_tv.setText(LanguagePack.getLanguage(eventForRealm.name,lan));

            }else if (click_action.equals(NotificationClass.TO_PLACE)){
                PlaceForRealm placeForRealm = realm.where(PlaceForRealm.class).equalTo("id",click_id).findFirst();
                click_action_tv.setText(LanguagePack.getLanguage(placeForRealm.name,lan));

            }else {

                click_action_tv.setText("NONE");
            }

        }



    private void initviews() {

        usercount_tv = findViewById(R.id.usercount_tv);
        click_action_tv = findViewById(R.id.click_action_tv);
        progress_text = findViewById(R.id.progress_text);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ee1 = findViewById(R.id.ee1);
        ee2 = findViewById(R.id.ee2);
        ee3 = findViewById(R.id.ee3);
        ee4 = findViewById(R.id.ee4);
        ee5 = findViewById(R.id.ee5);
        click_button_place = findViewById(R.id.click_button_place);
        click_button_event = findViewById(R.id.click_button_event);
        click_button_main = findViewById(R.id.click_button_main);
        click_button_sub = findViewById(R.id.click_button_sub);
        click_button_none = findViewById(R.id.click_button_none);
        send_notification_button = findViewById(R.id.send_notification_button);
        send_notification_ios_button = findViewById(R.id.send_notification_ios_button);
        to_cons = findViewById(R.id.to_cons);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setfailuremessage(String error) {

        finishsmth();
        builder.setIcon(R.drawable.error_icon);
        builder.setTitle("ERROR");
        builder.setMessage("ERROR : " + error);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton(null, null);
        builder.setCancelable(true).show();
    }

    private void setsuccessmessage(String msg) {

        builder.setIcon(R.drawable.checkicon);
        builder.setTitle("SUCCESS");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton(null, null);
        builder.setCancelable(true).show();
    }

    private void startsmth() {

        ll1.setSaveEnabled(false);
        ll2.setSaveEnabled(false);
        ll3.setSaveEnabled(false);
        ll4.setSaveEnabled(false);
        ll5.setSaveEnabled(false);
        ee1.setSaveEnabled(false);
        ee2.setSaveEnabled(false);
        ee3.setSaveEnabled(false);
        ee4.setSaveEnabled(false);
        ee5.setSaveEnabled(false);
        click_button_place.setSaveEnabled(false);
        click_button_event.setSaveEnabled(false);
        click_button_main.setSaveEnabled(false);
        click_button_sub.setSaveEnabled(false);
        click_button_none.setSaveEnabled(false);
        send_notification_button.setSaveEnabled(false);
        progressBar.setVisibility(View.VISIBLE);


    }

    private void finishsmth() {

        ll1.setSaveEnabled(true);
        ll2.setSaveEnabled(true);
        ll3.setSaveEnabled(true);
        ll4.setSaveEnabled(true);
        ll5.setSaveEnabled(true);
        ee1.setSaveEnabled(true);
        ee2.setSaveEnabled(true);
        ee3.setSaveEnabled(true);
        ee4.setSaveEnabled(true);
        ee5.setSaveEnabled(true);
        click_button_place.setSaveEnabled(true);
        click_button_event.setSaveEnabled(true);
        click_button_main.setSaveEnabled(true);
        click_button_sub.setSaveEnabled(true);
        click_button_none.setSaveEnabled(true);
        send_notification_button.setSaveEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);


    }

    public void addprogress(String s) {

        progress = progress + "\n" + s;
        progress_text.setText(progress);
    }

    public class AsynTask extends AsyncTask<String, Void, String> {
        String server_response;
        String responseMessage;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            addprogress("Server: " + responseMessage + "\nResponse: " + server_response);
            responseMessage = "";
            server_response = "";

            if (topicindex == 0) {
                addprogress("Notification sended.\n");
                setsuccessmessage("Notification sended");
                finishsmth();
                topicindex = usercount / MyFirebaseMessagingService.TOPICNUMBER;
            } else {
                topicindex--;
                addprogress(" ");
                new AsynTask().execute("https://fcm.googleapis.com/fcm/send");
            }
        }


        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("Authorization", NotificationClass.AUTH_KEY);
                urlConnection.addRequestProperty("Content-Type", "application/json");

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(notificationClassArrayList.get(topicindex).getnotification_text());
                wr.flush();
                wr.close();


                int responseCode = urlConnection.getResponseCode();

                responseMessage = urlConnection.getResponseMessage();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    server_response = readStream(urlConnection.getInputStream());

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return server_response;
        }


        // Converting InputStream to String
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }


    int ioscount = 4;

    public class IosAsynTask extends AsyncTask<String, Void, String> {
        String server_response;
        String responseMessage;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            addprogress("IosServer: " + responseMessage + "\nResponse: " + server_response);
            responseMessage = "";
            server_response = "";

            if (ioscount == 0) {
                addprogress("Notification sended.\n");
                setsuccessmessage("Notification sended");
                finishsmth();
                ioscount = 4;

            } else {
                ioscount--;
                new IosAsynTask().execute("https://fcm.googleapis.com/fcm/send");
            }
        }


        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("Authorization", NotificationClass.AUTH_KEY);
                urlConnection.addRequestProperty("Content-Type", "application/json");

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(ios_Notifications.getIosNotificationText().get(ioscount));
                wr.flush();
                wr.close();


                int responseCode = urlConnection.getResponseCode();

                responseMessage = urlConnection.getResponseMessage();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    server_response = readStream(urlConnection.getInputStream());

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return server_response;
        }


        // Converting InputStream to String
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }


}
