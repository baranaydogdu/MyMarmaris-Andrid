package com.baranaydogdu.mymarmaris.TabbarFragments;

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
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class FragmentNotifications extends Fragment {


    View view;
    TextView notification_list_tv;
    RecyclerView notification_recyclerview;
    SharedPreferences sharedPreferences;
    int selected_language = 0;
    Activity activity;
    NotificationAdapter notificationAdapter;
    RealmResults<PlaceCollectionForRealm> notificationList;
    Realm realm = Realm.getDefaultInstance();


    public FragmentNotifications() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);

    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        this.view = view;
        activity = getActivity();
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);
        initviews();

        notificationList = realm.where(PlaceCollectionForRealm.class).equalTo("masterid", "main").findAll().sort("sortnumber");

        notificationAdapter = new NotificationAdapter();
        notification_recyclerview.setAdapter(notificationAdapter);
        notification_recyclerview.setLayoutManager(new LinearLayoutManager(activity));

    }


    @Override
    public void onResume() {
        super.onResume();
        notificationList = realm.where(PlaceCollectionForRealm.class).equalTo("masterid", "main").findAll().sort("sortnumber");
        notificationAdapter.notifyDataSetChanged();
    }

    private void initviews() {

        notification_list_tv = view.findViewById(R.id.notification_list_tv);
        notification_recyclerview = view.findViewById(R.id.notification_recyclerview);
        notification_list_tv.setText(new LanguagePack().notificationsettingstext[selected_language]);

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


            final PlaceCollectionForRealm collection = notificationList.get(position);
            holder.notificationswitch.setText(LanguagePack.getLanguage(collection.name, selected_language));

            if (sharedPreferences.getBoolean("notify" + collection.getId(), true)) {

                holder.notificationswitch.setChecked(true);

            } else {

                holder.notificationswitch.setChecked(false);
            }

            new PreSets().setImage(getContext(), holder.notificationimage, collection.icon);

            holder.notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.notificationswitch.isChecked()) {

                        sharedPreferences.edit().putBoolean("notify" + collection.getId(), true).apply();

                    } else {

                        sharedPreferences.edit().putBoolean("notify" + collection.getId(), false).apply();
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

