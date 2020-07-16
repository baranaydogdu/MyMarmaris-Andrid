package com.baranaydogdu.mymarmaris;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import java.util.ArrayList;

import io.realm.Realm;

public class Appmain extends Application {

    //public static int visiblecount = 0;
    public static Activity activity;
    public static Activity mainContext;
    ArrayList<Activity> livingactivity;

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("APP CREATED");
        Realm.init(this);
        livingactivity=new ArrayList<>();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

    }

       /*
        notEqualTo
Compare integer fields: lessThan, biggerThan, etc.
Calculations on integer fields, eg. 2 * field1 > field2
does contains/startsWith/endsWith work on multiple fields?

equalToField(field1, field2);
greaterThanField(field1, field2);
greaterThanOrEqualToField(field1, field2);
lessThanField(field1, field2);
lessThanOrEqualToField(field1, field2);
notEqualToField(field1, field2);
         */
    /*
    public void checkmemory( String when){

        int available  = (int) (Runtime.getRuntime().maxMemory()/100000);
        int used       = (int) (Runtime.getRuntime().totalMemory()/100000);
        int free       = (int) (Runtime.getRuntime().freeMemory()/100000);


        //System.out.println("--------- ******** ------------");
        //System.out.println("available : "+available+ " used : "+used+ " free : "+free+"  "+when);

        //System.out.println("livingactivitycount : "+livingactivity.size());
        for (Activity activity:livingactivity){
            //System.out.println("live : "+activity.getLocalClassName());
        }


    }

     */






    public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            if (activity.getLocalClassName().equals("MainPage")){
                mainContext=activity;
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

            if (activity.getLocalClassName().equals("MainPage")){
                mainContext=null;
            }

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Appmain.activity=activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
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

}

