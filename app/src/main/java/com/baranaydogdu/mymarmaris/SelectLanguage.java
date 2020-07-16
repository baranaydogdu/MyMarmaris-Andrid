package com.baranaydogdu.mymarmaris;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.baranaydogdu.mymarmaris.TabbarFragments.MainPage;

public class SelectLanguage extends AppCompatActivity {

    public static final int NOTSELECTED=6;
    public static final int ENGLISH=0;
    public static final int TURKISH=1;
    public static final int RUSSIAN=2;
    public static final int GREEK=3;
    public static final int GERMAN=4;

    LinearLayout english_button, turkish_button, russian_button, greekbutton, german_button;
    public int selected_language=NOTSELECTED;
    Activity activity;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        activity=this;

        sharedPreferences = this.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        english_button=findViewById(R.id.linearbutton_english);
        turkish_button=findViewById(R.id.linearbutton_turkish);
        russian_button=findViewById(R.id.linearbutton_russian);
        greekbutton=findViewById(R.id.linearbutton_greek);
        german_button=findViewById(R.id.linearbutton_german);

        english_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_language=ENGLISH;
                setlanguage(selected_language);

            }
        });

        turkish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_language=TURKISH;
                setlanguage(selected_language);

            }
        });

        russian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_language=RUSSIAN;
                setlanguage(selected_language);

            }
        });

        greekbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_language=GREEK;
                setlanguage(selected_language);

            }
        });


        german_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_language=GERMAN;
                setlanguage(selected_language);

            }
        });


    }

    private void setlanguage(int selected_language) {

        english_button.setEnabled(false);
        turkish_button.setEnabled(false);
        russian_button.setEnabled(false);
        greekbutton.setEnabled(false);
        german_button.setEnabled(false);

        sharedPreferences.edit().putInt("language", selected_language).apply();

        Intent intent = new Intent(activity, MainPage.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {


    }
}

