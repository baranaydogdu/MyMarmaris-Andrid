package com.baranaydogdu.mymarmaris.Classes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

import com.baranaydogdu.mymarmaris.R;

import androidx.appcompat.view.ContextThemeWrapper;

public class MyMediaController extends MediaController {
    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public MyMediaController(Context context) {
        super(new ContextThemeWrapper(context, R.style.Theme_MusicPlayer));

    }
}
