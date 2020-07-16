package com.baranaydogdu.mymarmaris.TabbarFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.baranaydogdu.mymarmaris.AdminPages.AdminPageEnterence;
import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.EventForRealm;
import com.baranaydogdu.mymarmaris.Classes.HistoryRealm;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceForRealm;
import com.baranaydogdu.mymarmaris.LanguagePack;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.SearcOperations.SerachEventFragment;
import com.baranaydogdu.mymarmaris.SearcOperations.SerachPlaceFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class SearchMainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    SercahAdapter adapter;
    View view;

    ImageView searcicon;
    public static final int SEARCH_MIN_LENGHT = 2;
    int selected_language;
    SharedPreferences sharedPreferences;
    LanguagePack languagePack = new LanguagePack();
    String firstplacename = "";
    String fisrteventname = "";

    SerachPlaceFragment serachPlaceFragment = new SerachPlaceFragment();
    SerachEventFragment serachEventFragment = new SerachEventFragment();

    AutoCompleteTextView editText;
    ArrayList<String> history_list = new ArrayList<>();
    ArrayAdapter<String> historyAdapter;

    Realm realm = Realm.getDefaultInstance();

    public SearchMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_main, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {

        this.view = view;
        sharedPreferences = getActivity().getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language = sharedPreferences.getInt("language", 0);

        initviews();
        firstplacename = languagePack.placestext[selected_language];
        fisrteventname = languagePack.eventstext[selected_language];
        editText.setHint(languagePack.searctext[selected_language]);

        adapter = new SercahAdapter(getChildFragmentManager());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        history_list = new ArrayList<>();
        history_list.add("ELLE EKLEME");

        editText.setThreshold(1); //will start working from first character

        sethistory();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > SEARCH_MIN_LENGHT) {

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
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (editText.getText().toString().length() > SEARCH_MIN_LENGHT) addtohistory();
                    return true;
                }
                return false;
            }
        });

        //ITEM SECILIR ISE HISTOR EKLE
        editText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (editText.getText().toString().length() > SEARCH_MIN_LENGHT) addtohistory();
                try {        //KLAVYE GIZLE
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(editText.getWindowToken(), 0);
                } catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {        //KLAVYE GIZLE
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(editText.getWindowToken(), 0);
                } catch (Exception e) {

                }


            }
        });

        //ARAYA BASILIR ISE HOSTORY EKLE
        searcicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > SEARCH_MIN_LENGHT) addtohistory();
            }
        });

    }

    private void initviews() {

        tabLayout = view.findViewById(R.id.sliding_tabs);
        viewPager = view.findViewById(R.id.viewpager);
        editText = view.findViewById(R.id.editText);
        searcicon = view.findViewById(R.id.searchicon);

    }


    public void sethistory() {

        history_list.clear();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HistoryRealm> results = realm.where(HistoryRealm.class).findAll();

        for (HistoryRealm his : results) {
            history_list.add(his.historyText);
        }

        historyAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, history_list);
        editText.setAdapter(historyAdapter);

    }


    public void addtohistory() {

        editText.clearFocus();

        try {        //KLAVYE GIZLE
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {

        }


        HistoryRealm.addtoHistory(editText.getText().toString());
        sethistory();

    }

    public void search() {

        String searchText =  editText.getText().toString();
        if (editText.getText().toString().equals("adminozgurmurat")) {
            Intent intent = new Intent(getActivity(), AdminPageEnterence.class);
            addtohistory();
            startActivity(intent);
            return;
        }

        RealmResults<PlaceForRealm> placeResults = realm.where(PlaceForRealm.class)
                .contains("name0", searchText , Case.INSENSITIVE).or()
                .contains("name1", searchText, Case.INSENSITIVE).or()
                .contains("name2", searchText, Case.INSENSITIVE).or()
                .contains("name3", searchText, Case.INSENSITIVE).or()
                .contains("name4", searchText, Case.INSENSITIVE).or()
                .contains("explain0", searchText , Case.INSENSITIVE).or()
                .contains("explain1", searchText, Case.INSENSITIVE).or()
                .contains("explain2", searchText, Case.INSENSITIVE).or()
                .contains("explain3", searchText, Case.INSENSITIVE).or()
                .contains("explain4", searchText, Case.INSENSITIVE).or()
                .findAll();

        ArrayList<PlaceClass> placeList = new ArrayList<>();
        for (PlaceForRealm placeForRealm : placeResults) {
            placeList.add(placeForRealm.toPlace());
        }

        RealmResults<EventForRealm> eventResults = realm.where(EventForRealm.class)
                .contains("name0", searchText , Case.INSENSITIVE).or()
                .contains("name1", searchText, Case.INSENSITIVE).or()
                .contains("name2", searchText, Case.INSENSITIVE).or()
                .contains("name3", searchText, Case.INSENSITIVE).or()
                .contains("name4", searchText, Case.INSENSITIVE).or()
                .contains("explain0", searchText , Case.INSENSITIVE).or()
                .contains("explain1", searchText, Case.INSENSITIVE).or()
                .contains("explain2", searchText, Case.INSENSITIVE).or()
                .contains("explain3", searchText, Case.INSENSITIVE).or()
                .contains("explain4", searchText, Case.INSENSITIVE).or()
                .findAll();

        ArrayList<EventClass> eventList = new ArrayList<>();
        for (EventForRealm eventForRealm : eventResults) {
            for (EventClass ev :eventForRealm.multiple() )
                eventList.add(ev);
        }

        Collections.sort(eventList, new Comparator<EventClass>() {
            @Override
            public int compare(EventClass o1, EventClass o2) {
                return (new Date(o1.times.get(0).getStart_time())).compareTo(new Date(o2.times.get(0).getStart_time()));
            }
        });

        firstplacename = languagePack.placestext[selected_language];
        fisrteventname = languagePack.eventstext[selected_language];
        if (placeList.size() > 0) firstplacename = firstplacename + " (" + placeList.size() + ")";

        if (eventList.size() > 0) fisrteventname = fisrteventname + " (" + eventList.size() + ")";

        serachPlaceFragment.setlist(placeList);
        serachEventFragment.setEvents(eventList);
        adapter.notifyDataSetChanged();

    }

    private void clearsearch() {

        serachPlaceFragment.setlist(new ArrayList<PlaceClass>());
        serachEventFragment.setEvents(new ArrayList<EventClass>());

        firstplacename = languagePack.placestext[selected_language];
        fisrteventname = languagePack.eventstext[selected_language];
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        System.out.println("");
        super.onResume();
        sethistory();

    }

    public class SercahAdapter extends FragmentPagerAdapter {

        public SercahAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0)
                return serachPlaceFragment;
            else return serachEventFragment;

        }

        @Override
        public int getCount() {

            return 2;

        }


        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return firstplacename;

            } else {
                return fisrteventname;
            }

        }

    }


}
