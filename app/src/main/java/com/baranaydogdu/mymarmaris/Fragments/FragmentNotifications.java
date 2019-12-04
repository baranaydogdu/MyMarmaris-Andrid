package com.baranaydogdu.mymarmaris.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;


public class FragmentNotifications extends Fragment {

    public static FragmentNotifications instance;

    View view;
    TextView notification_list_tv;
    RecyclerView notification_recyclerview;
    SharedPreferences sharedPreferences;
    int selected_language = 0;
    Activity activity;
    NotificationAdapter notificationAdapter;
    ArrayList<PlaceCollectionClass> notificationList;





    public static FragmentNotifications getInstance() {

        if (instance == null) {

            FragmentNotifications.instance = new FragmentNotifications();
        }
        return instance;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);

    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        this.view=view;
        activity=getActivity();
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);
        notificationList=new ArrayList<>();

        initviews();


        notificationAdapter = new NotificationAdapter();
        notification_recyclerview.setAdapter(notificationAdapter);
        notification_recyclerview.setLayoutManager(new LinearLayoutManager(activity));

        setnotificationList();

    }

    public void setnotificationList() {

        notificationList.clear();

        for (PlaceCollectionClass singlecollection:PreSets.getAll_MainCollections(activity)){

            notificationList.add(singlecollection);

        }

        notificationAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initviews() {

        notification_list_tv = view.findViewById(R.id.notification_list_tv);
        notification_recyclerview = view.findViewById(R.id.notification_recyclerview);

        String[] notificationsettingstext={"Notification Settings","Bildirim ayarları","Notification Settings","Notification Settings","Notification Settings"};

        notification_list_tv.setText(notificationsettingstext[selected_language]);

    }


    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

        public NotificationAdapter() {

        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notification_single_item,
                    parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final NotificationViewHolder holder, final int position) {

            final PlaceCollectionClass collection=notificationList.get(position);
            holder.notificationswitch.setText(PreSets.setlanguage_name(activity,collection));

            if (sharedPreferences.getBoolean("notify"+collection.getId(),true)){

                holder.notificationswitch.setChecked(true);

            }else {

                holder.notificationswitch.setChecked(false);
            }

            PreSets.setImageIcon(holder.notificationimage,collection.getIconnumber());

            holder.notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.notificationswitch.isChecked()){

                        sharedPreferences.edit().putBoolean("notify"+collection.getId(),true).apply();

                    }else {

                        sharedPreferences.edit().putBoolean("notify"+collection.getId(),false).apply();
                    }
                }
            });


        }




        @Override
        public int getItemCount() {

            return notificationList.size();

        }

        public class NotificationViewHolder extends RecyclerView.ViewHolder {

            ImageView notificationimage;//notification_icon notificication_switch
            Switch notificationswitch;

            public NotificationViewHolder(View itemView) {
                super(itemView);

                notificationimage = itemView.findViewById(R.id.notification_icon);
                notificationswitch = itemView.findViewById(R.id.notificication_switch);


            }
        }
    }
}

