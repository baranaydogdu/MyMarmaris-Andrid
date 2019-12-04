package com.baranaydogdu.mymarmaris.Classes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;
import android.widget.ProgressBar;

import com.baranaydogdu.mymarmaris.R;

import androidx.appcompat.view.ContextThemeWrapper;

public class MyProgressBar extends ProgressBar {
    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgressBar(Context context) {
        super(new ContextThemeWrapper(context, R.style.Theme_MusicPlayer));

    }
}
