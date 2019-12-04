package com.baranaydogdu.mymarmaris.PlaceActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.baranaydogdu.mymarmaris.R;

public class InsideLinkCollectionActivity extends AppCompatActivity {

    WebView webview;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_link_collection);

        intent=getIntent();
        String url=intent.getStringExtra("url");
        webview=findViewById(R.id.webview);
        webview.loadUrl(url);
    }
}
