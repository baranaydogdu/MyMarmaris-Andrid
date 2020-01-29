package com.baranaydogdu.mymarmaris.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.SearcOperations.SearchMainFragment;
import com.baranaydogdu.mymarmaris.SelectLanguage;

public class SettingFragment extends Fragment {

    ImageView language_flag_imageview;
    TextView selectlanguage_tv,settings_tv;
    CardView selectlanguage__carvdiew,clear_carvdiew,clear_favlist_carvdiew;
    SharedPreferences sharedPreferences;
    int selected_language;
    Activity activity;
    EditText clear_tv,clear_fav_tv;
    Switch auto_video_switch,auto_image_switch;
    String[] allhistorydeleted={"All History Deleted","Arama Geçmişi Silindi","Вся история удалена","Όλη η ιστορία διαγράφηκε","Suchverlauf gelöscht"};
    String[] allfavlist_deleted={"Favorite list Deleted","Favori Listesi Silindi","Список избранных удален","Αγαπημένη λίστα διαγράφηκε","Favorit gelöscht"};


    public static SettingFragment instance;

    public static SettingFragment getInstance() {

        if (instance == null) {

            SettingFragment.instance = new SettingFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity=getActivity();
        language_flag_imageview=view.findViewById(R.id.language_flag_imageview);
        selectlanguage_tv=view.findViewById(R.id.selectlanguage_tv);
        clear_carvdiew=view.findViewById(R.id.clear_carvdiew);
        clear_favlist_carvdiew=view.findViewById(R.id.clear_favlist_carvdiew);
        clear_fav_tv=view.findViewById(R.id.clear_favlist_tv);
        clear_tv = view.findViewById(R.id.clear_tv);
        settings_tv = view.findViewById(R.id.settings_tv);

        auto_video_switch=view.findViewById(R.id.auto_video_tv);
        auto_image_switch=view.findViewById(R.id.autoimage_switch);


        sharedPreferences = getActivity().getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        selected_language=sharedPreferences.getInt("language",0);

        if (sharedPreferences.getBoolean("auto_images",true)){
            auto_image_switch.setChecked(true);
        } else auto_image_switch.setChecked(false);

        if (sharedPreferences.getBoolean("auto_video",false)){
            auto_video_switch.setChecked(true);
        } else auto_video_switch.setChecked(false);

        auto_video_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_video_switch.isChecked()){
                    sharedPreferences.edit().putBoolean("auto_video",true).apply();
                } else sharedPreferences.edit().putBoolean("auto_video",false).apply();

            }
        });

        auto_image_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_image_switch.isChecked()){
                    sharedPreferences.edit().putBoolean("auto_images",true).apply();
                } else sharedPreferences.edit().putBoolean("auto_images",false).apply();

            }
        });


        clear_carvdiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreSets.clearhistory(activity);

                Toast.makeText(getActivity(),allhistorydeleted[selected_language],Toast.LENGTH_SHORT).show();
                try{

                    SearchMainFragment.getInstance().sethistory();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        clear_favlist_carvdiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreSets.clearfavlist(activity);
                Toast.makeText(getActivity(),allfavlist_deleted[selected_language],Toast.LENGTH_SHORT).show();

                try {
                    FragmentFavorittes.getInstance().setlist();
                }catch (Exception e){

                }
            }
        });


        selectlanguage__carvdiew=view.findViewById(R.id.selectlanguage__carvdiew);

        selectlanguage__carvdiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SelectLanguage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        setlanguages();

    }


    private void setlanguages() {


        String[] select_language={"Select language","Dil seç","Выберите язык","Επιλέξτε γλώσσα","Sprache auswählen"};
        String[] clearhistory={"Clean Search History","Arama geçmişini temizle","Очистить историю поиска","Καθαρίστε το Ιστορικό αναζήτησης","Suchverlauf löschen"};
        String[] clearfavlisttext={"Clean Favorite List","Favori Listesini Temizle","Чистый список избранного","Καθαρισμός λίστας αγαπημένων","Favorit löschen"};
        String[] autovideotext={"Auto Play Video","Videoyu Otomatik Oynat","Автоматическое воспроизведение видео","Αυτόματη αναπαραγωγή βίντεο","Video automatisch abspielen"};

        String[] autoimagetext={"Auto Slide Images","Fotoğrafları otomatik oynat","Авто слайд изображения","Αυτόματες εικόνες διαφάνειας","Bilder automatisch schieben",};
        String[] settings={"Settings","Ayarlar","настройки","ρυθμίσεις","die Einstellungen"};

        int[] flag={R.drawable.flag_english,R.drawable.flag_turkish,R.drawable.flag_russian,
                        R.drawable.flag_greek,R.drawable.flag_german};

        language_flag_imageview.setImageResource(flag[selected_language]);
        selectlanguage_tv.setText(select_language[selected_language]);
        clear_tv.setText(clearhistory[selected_language]);
        clear_fav_tv.setText(clearfavlisttext[selected_language]);

        auto_video_switch.setText(autovideotext[selected_language]);
        auto_image_switch.setText(autoimagetext[selected_language]);
        settings_tv.setText(settings[selected_language]);

    }


}
