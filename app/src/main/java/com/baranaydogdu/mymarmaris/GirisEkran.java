package com.baranaydogdu.mymarmaris;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class GirisEkran extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int language;
    Intent intent;
    Activity activity;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_giris_ekrani);
        activity = this;

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        language = sharedPreferences.getInt("language", SelectLanguage.NOTSELECTED);
        System.out.println("secili dil :" + language);

        intent = getIntent();

        new CountDownTimer(500, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (intent.getBooleanExtra("from_click", false)) {
                    System.out.println("FROM CLICK");

                    timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            System.out.println("Appmain.mainContext : "+Appmain.mainContext);

                            if (Appmain.mainContext==null){

                                timer.cancel();
                                System.out.println("MAIN NULL");
                                Intent clikintent = new Intent(activity, MainPage.class);
                                clikintent.putExtra("from_click", true);
                                clikintent.putExtra("act",intent.getStringExtra("act"));
                                clikintent.putExtra("id",intent.getStringExtra("id"));

                                startActivity(clikintent);
                                activity.finish();

                            } else {

                                System.out.println("MAIN NOT NULL");
                            }
                        }
                    },20,200);


                } else {

                    System.out.println("FROM START");
                    //SAYFA YONLENDRIMELERI
                    if (sharedPreferences.getInt("language", SelectLanguage.NOTSELECTED) == SelectLanguage.NOTSELECTED) { //SECILI DIL YOK ISE

                        Intent intent = new Intent(activity, SelectLanguage.class);
                        startActivity(intent);
                        finish();

                    } else { //ONCEDEN DIL SECILMIS ISE

                        Intent intent = new Intent(activity, MainPage.class);
                        startActivity(intent);
                        finish();
                    }

                }


            }
        }.start();


    }

}
