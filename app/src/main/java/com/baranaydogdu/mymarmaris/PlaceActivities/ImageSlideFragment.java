package com.baranaydogdu.mymarmaris.PlaceActivities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;


public class ImageSlideFragment extends Fragment {

    ImageView imageView;
    String id;
    int index;

    public ImageSlideFragment(String id, int index) {
        this.id = id;
        this.index = index;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_imege_slide, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {  //MAGIC IS HERE

        imageView=view.findViewById(R.id.slide_image);
        PreSets.set_TOP_photo(imageView,id,index);

    }


}
