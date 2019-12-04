package com.baranaydogdu.mymarmaris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SelectIcon extends AppCompatActivity {

    RecyclerView selecticonrecyclerView;
    Activity activity;
    public SharedPreferences sharedPreferences;
    public String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon);

        activity=this;
        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        from=getIntent().getStringExtra("main/sub");

        selecticonrecyclerView=findViewById(R.id.selecticon_recyclerview);
        SelectIconAdapter selectIconAdapter = new SelectIconAdapter();
        selecticonrecyclerView.setAdapter(selectIconAdapter);
        selecticonrecyclerView.setLayoutManager(new GridLayoutManager(this,3));

    }


    public class SelectIconAdapter extends RecyclerView.Adapter<SelectIconAdapter.SelectIconViewHolder>{


        public SelectIconAdapter() {

        }

        @Override
        public SelectIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom_selecticon_layout,
                    parent, false);
            return new SelectIconViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectIconViewHolder holder, final int position) {

            PreSets.setImageIcon(holder.mPlace,position);

            holder.mPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (from.equals("sub")) sharedPreferences.edit().putInt("selected_subiconnumber",position).apply();
                    if (from.equals("main")) sharedPreferences.edit().putInt("selectediconnumber",position).apply();
                    if (from.equals("notification")) sharedPreferences.edit().putInt("notificationiconnumber",position).apply();


                    activity.finish();
                }
            });
        }


        @Override
        public int getItemCount() {
            return PreSets.ICONCOUNT;
        }

        public class SelectIconViewHolder extends RecyclerView.ViewHolder {

            ImageView mPlace;

            public SelectIconViewHolder(View itemView) {
                super(itemView);
                mPlace = itemView.findViewById(R.id.selecticon_custum_iconsource);


            }
        }
    }

}
