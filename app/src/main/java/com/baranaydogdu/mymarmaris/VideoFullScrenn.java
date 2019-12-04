package com.baranaydogdu.mymarmaris;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.baranaydogdu.mymarmaris.Classes.MyMediaController;

public class VideoFullScrenn extends AppCompatActivity {

    MyMediaController mediaController;
    VideoView videoView;
    Intent intent;
    String id;
    int seektime;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full_screnn);

        intent=getIntent();
        id=intent.getStringExtra("id");
        seektime=intent.getIntExtra("seektime",1000);

        if (savedInstanceState!=null){
            seektime=savedInstanceState.getInt("seektime",1000);
        }

        videoView=findViewById(R.id.videoView3);
        mediaController=new MyMediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.fromFile(PreSets.getvideo_File(id)));

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                videoView.seekTo(1000);
                videoView.pause();

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(seektime);
        videoView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
        seektime=videoView.getCurrentPosition();

    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("seektime",seektime);
    }




}
