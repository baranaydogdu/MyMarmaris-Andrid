package com.baranaydogdu.mymarmaris.SearcOperations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import com.baranaydogdu.mymarmaris.AdminPageEnterence;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class SearchMainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    SercahAdapter adapter;
    Activity activity;
    View view;

    AutoCompleteTextView editText;
    ArrayAdapter<String> historyAdapter;
    ArrayList<String> history_list;
    ImageView searcicon;
    public static final int SEARCH_MIN_LENGHT=2;
    int selected_language;
    SharedPreferences sharedPreferences;
    String[] searctext={"Search","Ara","Search","Search","Search",};
    String[] placestext={"Places","İşletmeler","Places","Places","Places"};
    String[] eventstext={"Events","Etkinlikler","Events","Events","Events"};
    String fisrteventname,firstplacename;

    public static SearchMainFragment instance;

    public static SearchMainFragment getInstance() {

        if (instance == null) {

            SearchMainFragment.instance = new SearchMainFragment();
        }
        return instance;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_main, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        this.view=view;
        activity=getActivity();
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language=sharedPreferences.getInt("language",0);

        initviews();
        firstplacename=placestext[selected_language];
        fisrteventname=eventstext[selected_language];
        editText.setHint(searctext[selected_language]);

        adapter=new SercahAdapter(getChildFragmentManager());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        history_list=new ArrayList<>();
        history_list.add("ELLE EKLEME");

        sethistory();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>SEARCH_MIN_LENGHT){

                    search();

                } else {

                    clearsearch();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //ENTER A BASILIR ISE HISTORY EKLE
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) { if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { if (editText.getText().toString().length()>SEARCH_MIN_LENGHT)  addtohistory();return true; }return false;}});

        //ITEM SECILIR ISE HISTOR EKLE
        editText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {@Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (editText.getText().toString().length()>SEARCH_MIN_LENGHT)  addtohistory();
            try{        //KLAVYE GIZLE
                ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(editText.getWindowToken(),0);
            } catch (Exception e){

            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            try{        //KLAVYE GIZLE
                ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(editText.getWindowToken(),0);
            } catch (Exception e){

            }


        }});

        //ARAYA BASILIR ISE HOSTORY EKLE
        searcicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if (editText.getText().toString().length()>SEARCH_MIN_LENGHT)addtohistory(); }});


    }



    private void initviews() {

        tabLayout = view.findViewById(R.id.sliding_tabs);
        viewPager = view.findViewById(R.id.viewpager);
        editText  = view.findViewById(R.id.editText);
        searcicon = view.findViewById(R.id.searchicon);

    }




    public void sethistory() {

        history_list.clear();
        history_list= PreSets.get_search_history(activity);

        historyAdapter=new ArrayAdapter<String> (activity, android.R.layout.select_dialog_item, history_list);
        editText.setThreshold(1); //will start working from first character
        editText.setAdapter(historyAdapter);

        historyAdapter.notifyDataSetChanged();
    }


    public void addtohistory(){

        editText.clearFocus();

        try{        //KLAVYE GIZLE
            ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(editText.getWindowToken(),0);
        } catch (Exception e){

        }

        PreSets.add_item_history(activity,editText.getText().toString());
        sethistory();

    }

    public void search() {

        SerachPlaceFragment.getInstance().setlist(editText.getText().toString());
        SerachEventFragment.getInstance().setlist(editText.getText().toString());

        if (editText.getText().toString().equals("adminozgurmurat")){
            Intent intent=new Intent(activity, AdminPageEnterence.class);
            addtohistory();
            startActivity(intent);
        }
    }

    private void clearsearch() {

        SerachEventFragment.getInstance().clearlist();
        SerachPlaceFragment.getInstance().clearlist();

        firstplacename= placestext[selected_language];
        fisrteventname= eventstext[selected_language];

        adapter.notifyDataSetChanged();
    }






    public class SercahAdapter extends FragmentPagerAdapter {

        public SercahAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

           if (position==0)
               return SerachPlaceFragment.getInstance();
           else  return SerachEventFragment.getInstance();

        }

        @Override
        public int getCount() {

            return 2;

        }


        public CharSequence getPageTitle(int position) {
            // Generate title based on item position

            if (position==0) {
                return firstplacename;

            } else { return fisrteventname; }


        }

    }







}
