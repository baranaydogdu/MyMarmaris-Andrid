package com.baranaydogdu.mymarmaris.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baranaydogdu.mymarmaris.R;

public class NavigationDrawerFragment extends Fragment {

    TextView tv;
    LinearLayout linear_phone, linear_whatsapp, linear_mail, linear_insta, linear_face;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv = view.findViewById(R.id.textView23);
        linear_phone = view.findViewById(R.id.calling_button_drawer);
        linear_whatsapp = view.findViewById(R.id.whatsapp_button_drawer);
        linear_mail = view.findViewById(R.id.mail_button_drawer);
        linear_insta = view.findViewById(R.id.insta_button_drawer);
        linear_face = view.findViewById(R.id.face_button_drawer);

        linear_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneIntent();
            }
        });

        linear_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whatsappIntent();
            }
        });

        linear_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mailIntent();
            }
        });

        linear_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instaIntent();
            }
        });

        linear_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                faceIntent();
            }
        });

    }


    private void phoneIntent() {

        String number = "05558106116";
        if (!number.startsWith("+9")) number = "+9" + number;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void whatsappIntent() {

        String number = "05558106116";
        if (!number.startsWith("+9")) number = "+9" + number;

        String contact = number; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getActivity().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Whatsapp not available " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void faceIntent() {

        String url = "www.facebook.com";
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        try {       //FACE YUKLU
            Intent faceintent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url + "/"));
            faceintent.setPackage("com.facebook.katana");
            startActivity(faceintent);

        } catch (Exception e) {     //WEB ILE AC

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            } catch (Exception ex) { //ARTIK NOLUYORSA

                Toast.makeText(getActivity(), "ERROR : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void instaIntent() {

        String url = "www.instagram.com";
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;


        try {        //INSTA YUKLU       //INSTA YUKLU       //INSTA YUKLU       //INSTA YUKLU       //INSTA YUKLU
            Intent insta = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            insta.setPackage("com.instagram.android");
            startActivity(insta);
        } catch (Exception e) {       //INSTA YUKLU DEGIL     //INSTA YUKLU DEGIL     //INSTA YUKLU DEGIL     //INSTA YUKLU DEGIL
            try {   //INSTAYA WEB SAYFASINDAN GIDICEZ       //INSTAYA WEB SAYFASINDAN GIDICEZ
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url)));
            } catch (Exception ex) {  //KULLIYEN HATA     //KULLIYEN HATA     //KULLIYEN HATA     //KULLIYEN HATA
                Toast.makeText(getActivity(), "HATA : " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void mailIntent() {

        String emailadres="mymarmaristeam@gmail.com";

        try {           //SEND EMAIL        //SEND EMAIL        //SEND EMAIL        //SEND EMAIL        //SEND EMAIL
            //Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, place.getContactinfo().getMailadress());
            //startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailadres));

            startActivity(Intent.createChooser(emailIntent, "Chooser Title"));



        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "No available mail account", Toast.LENGTH_SHORT).show();
        }


    }
}
