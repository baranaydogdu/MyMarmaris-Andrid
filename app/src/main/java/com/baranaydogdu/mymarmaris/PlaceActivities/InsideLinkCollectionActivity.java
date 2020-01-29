package com.baranaydogdu.mymarmaris.PlaceActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

        /*
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.setWebViewClient(new HelloWebViewClient());
*/
        webview.loadUrl(url);
    }

    private class HelloWebViewClient extends WebViewClient {

        public void onPageFinished(WebView view, String url) {

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

    }







}
