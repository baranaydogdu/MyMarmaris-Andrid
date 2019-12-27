package com.baranaydogdu.mymarmaris.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

public class NavigationDrawerFragment extends Fragment {

    SharedPreferences sharedPreferences;
    int selected_language;


    EditText marmaris_rehberi_edx, diger_apps_edx;
    CardView phone_card, whatsapp_card, mai_card, insta_card, face_card;
    EditText phone_edx, whatsapp_edx, mai_edx, insta_edx, face_edx;
    ImageView mydatca, mymugla;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);

        marmaris_rehberi_edx = view.findViewById(R.id.marmaris_rehberi_edx);
        diger_apps_edx = view.findViewById(R.id.diger_apps_edx);
        phone_card = view.findViewById(R.id.phone_carvdiew);
        whatsapp_card = view.findViewById(R.id.whatsapp_carview);
        mai_card = view.findViewById(R.id.mail_carview);
        insta_card = view.findViewById(R.id.insta_carview);
        face_card = view.findViewById(R.id.facebook_carview);
        phone_edx = view.findViewById(R.id.phone_edx);
        whatsapp_edx = view.findViewById(R.id.whatsapp_edx);
        mai_edx = view.findViewById(R.id.mail_edx);
        insta_edx = view.findViewById(R.id.insta_edx);
        face_edx = view.findViewById(R.id.face_edx);
        mydatca = view.findViewById(R.id.mydatca);
        mymugla = view.findViewById(R.id.mymugla);

        setlanguages();


        phone_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneIntent();
            }
        });

        whatsapp_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whatsappIntent();
            }
        });

        mai_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailIntent();
            }
        });

        insta_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instaIntent();
            }
        });

        face_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                faceIntent();
            }
        });


        mydatca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydatca_Intent();
            }
        });

        mymugla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mymugla_Intent();
            }
        });


    }

    private void setlanguages() {

        String[] phones = {"Phone", "Telefon", "Телефон", "τηλέφωνο", "Telefon"};
        String[] whatsapps = {"Whatsapp", "Whatsapp", "Whatsapp", "Whatsapp", "Whatsapp"};
        String[] faces = {"Facebook", "Facebook", "Facebook", "Facebook", "Facebook"};
        String[] instas = {"Instagram", "Instagram", "Instagram", "Instagram", "Instagram"};
        String[] mails = {"Mail", "Mail", "Mail", "Mail", "Mail"};
        String[] marmarisrehberi = {"Marmaris Guide", "Marnaris Rehberi", "Путеводитель по Мармарису", "Οδηγός Μαρμαρίς", "Marmaris-Führer"};
        String[] digerapps = {"Other Applications", "Diğer Uygulamalarımız", "Другие приложения", "Άλλες εφαρμογές", "Andere Anwendungen"};

        phone_edx.setText(phones[selected_language]);
        whatsapp_edx.setText(whatsapps[selected_language]);
        mai_edx.setText(mails[selected_language]);
        insta_edx.setText(instas[selected_language]);
        face_edx.setText(faces[selected_language]);

        marmaris_rehberi_edx.setText(marmarisrehberi[selected_language]);
        diger_apps_edx.setText(digerapps[selected_language]);


    }

    private void mydatca_Intent() {

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.mobiluygulamamerkezi.mydatca"));
            intent.setPackage("com.android.vending");
            startActivity(intent);


        } catch (Exception e) {

            Toast.makeText(getActivity(), "HATA : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private void mymugla_Intent() {

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.knicode.lokal.mymugla"));
            intent.setPackage("com.android.vending");
            startActivity(intent);


        } catch (Exception e) {

            Toast.makeText(getActivity(), "HATA : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        }

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

        String emailadres = "mymarmaristeam@gmail.com";

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
