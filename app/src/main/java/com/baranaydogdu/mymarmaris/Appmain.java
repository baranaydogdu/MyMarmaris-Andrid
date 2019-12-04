package com.baranaydogdu.mymarmaris;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import java.util.ArrayList;

public class Appmain extends Application {

    public static int visiblecount = 0;
    public static Activity activity;
    public static Activity mainContext;
    ArrayList<Activity> livingactivity;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        System.out.println("APP CREATED");
        livingactivity=new ArrayList<>();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

    }

    public void checkmemory( String when){

        int available  = (int) (Runtime.getRuntime().maxMemory()/100000);
        int used       = (int) (Runtime.getRuntime().totalMemory()/100000);
        int free       = (int) (Runtime.getRuntime().freeMemory()/100000);


        System.out.println("--------- ******** ------------");
        System.out.println("available : "+available+ " used : "+used+ " free : "+free+"  "+when);

        System.out.println("livingactivitycount : "+livingactivity.size());
        for (Activity activity:livingactivity){
            System.out.println("live : "+activity.getLocalClassName());
        }


    }






    public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            livingactivity.add(activity);
            if (activity.getLocalClassName().equals("MainPage")){
                mainContext=activity;
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

            livingactivity.remove(activity);

            if (activity.getLocalClassName().equals("MainPage")){
                mainContext=null;
            }

        }

        @Override
        public void onActivityResumed(Activity activity) {
            checkmemory("Resume");

            Appmain.visiblecount++;
            //System.out.println("RESUMED ACTIVITY : "+activity.getLocalClassName());
            Appmain.activity=activity;

        }

        @Override
        public void onActivityPaused(Activity activity) {

            Appmain.visiblecount--;

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {


        }
    }

    public static Boolean isforeground() {

        if (Appmain.visiblecount > 0) return true;
        else return false;

    }




}

