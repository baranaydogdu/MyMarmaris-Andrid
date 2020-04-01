package com.baranaydogdu.mymarmaris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.ImageView;
import com.baranaydogdu.mymarmaris.Classes.Contactinfo;
import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.EventTimes;
import com.baranaydogdu.mymarmaris.Classes.LocationClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceDistanceClass;
import com.baranaydogdu.mymarmaris.Classes.SavedFilesClass;
import com.baranaydogdu.mymarmaris.Fragments.FragmentNotifications;
import com.baranaydogdu.mymarmaris.PlaceActivities.FragmentMainCollections;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import androidx.annotation.NonNull;

public class PreSets {

    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final int UNDOWNLOADED = -1;
    public static final int CONTINUE = -2;
    public static final int EMPTYVIDEO = 0;

    public static final int MAXVIDEOCAOUNT = 50;

    public static final int ICONCOUNT = 285;

    public static final int TODAY_HOURS_MEANS = 1 * DAY_MILLIS;

    //GET FILE VIDEO AND POTOS              //----OK-----
    public static File get_TOP_photo_File(String id, int position) {

        String logoname = "fototop" + id + position + ".jpg";
        final File localfile = new File("/data/user/0/com.baranaydogdu.mymarmaris/cache", logoname);
        return localfile;

    }

    public static File get_DOWN_photo_File(String id, int position) {

        String logoname = "fotodown" + id + position + ".jpg";
        final File localfile = new File("/data/user/0/com.baranaydogdu.mymarmaris/cache", logoname);
        return localfile;

    }

    public static File getvideo_File(String id) {

        String videoname = "video" + id + ".mp4";
        final File localfile = new File("/data/user/0/com.baranaydogdu.mymarmaris/cache", videoname);
        return localfile;

    }

    public static File get_MAP_photo_File(String id) {

        String logoname = "fotomap" + id + ".jpg";
        final File localfile = new File("/data/user/0/com.baranaydogdu.mymarmaris/cache", logoname);
        return localfile;

    }

    //----OK-----
    public static String firebase_TOP_photoname(String id, int position) {

        return "photo_video/" + id + "/phototop" + position + ".jpg";


    }

    public static String firebase_DOWN_photoname(String id, int position) {

        return "photo_video/" + id + "/photodown" + position + ".jpg";


    }

    public static String firebase_videoname(String id) {

        return "photo_video/" + id + "/video.mp4";

    }

    public static String firebase_map_name(String id) {

        return "photo_video/" + id + "/map.jpg";


    }

    //OPEN DATABASE              //----OK-----
    public static SQLiteDatabase open_place_database(Context context) {

        SQLiteDatabase database = context.openOrCreateDatabase("place_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS place_database (mainId VARCHAR, ml1 VARCHAR, ml2 VARCHAR, ml3 VARCHAR, ml4 VARCHAR, ml5 VARCHAR, micon VARCHAR, msort VARCHAR, mtype VARCHAR, mlink VARCHAR, " +
                "subId VARCHAR, sl1 VARCHAR, sl2 VARCHAR, sl3 VARCHAR, sl4 VARCHAR, sl5 VARCHAR, sicon VARCHAR, ssort VARCHAR, " +
                "placeId VARCHAR, pl1 VARCHAR, pl2 VARCHAR, pl3 VARCHAR, pl4 VARCHAR, pl5 VARCHAR, pe1 VARCHAR, pe2 VARCHAR, pe3 VARCHAR, pe4 VARCHAR, pe5 VARCHAR, " +
                "pa1 VARCHAR, pa2 VARCHAR, pa3 VARCHAR, pa4 VARCHAR, pa5 VARCHAR, psort VARCHAR, latitude VARCHAR, longitude VARCHAR, " +
                "phone VARCHAR, insta VARCHAR, face VARCHAR, web VARCHAR, mail VARCHAR, whatsapp VARCHAR, buyticket VARCHAR, " +
                "videoversion VARCHAR, mapversion VARCHAR, topphotocount VARCHAR, " +
                "ptop0 VARCHAR, ptop1 VARCHAR, ptop2 VARCHAR, ptop3 VARCHAR, ptop4 VARCHAR, ptop5 VARCHAR, ptop6 VARCHAR, ptop7 VARCHAR, " +
                "ptop8 VARCHAR, ptop9 VARCHAR, ptop10 VARCHAR, ptop11 VARCHAR, ptop12 VARCHAR, ptop13 VARCHAR, ptop14 VARCHAR, " +
                "pdowncount VARCHAR, pdown0 VARCHAR, pdown1 VARCHAR, pdown2 VARCHAR, pdown3 VARCHAR, pdown4 VARCHAR, pdown5 VARCHAR, pdown6 VARCHAR, pdown7 VARCHAR, " +
                "pdown8 VARCHAR, pdown9 VARCHAR, pdown10 VARCHAR, pdown11 VARCHAR, pdown12 VARCHAR, pdown13 VARCHAR, pdown14 VARCHAR, " +
                "popen0 VARCHAR, popen1 VARCHAR, popen2 VARCHAR, popen3 VARCHAR, popen4 VARCHAR, popen5 VARCHAR, popen6 VARCHAR, " +
                "pclse0 VARCHAR, pclse1 VARCHAR, pclse2 VARCHAR, pclse3 VARCHAR, pclse4 VARCHAR, pclse5 VARCHAR, pclse6 VARCHAR, " +
                "newendtime VARCHAR, isactive VARCHAR)");

        return database;
    }

    public static String get_place_database_insertString() {

        return "INSERT INTO place_database (mainId, ml1, ml2, ml3, ml4, ml5, micon, msort, mtype, mlink, " +                                                                                // 10 ADET
                "subId, sl1, sl2, sl3, sl4, sl5, sicon, ssort, placeId, pl1, pl2, pl3, pl4, pl5, pe1, pe2, pe3, pe4, pe5, " +                                    // 19 ADET
                "pa1, pa2, pa3, pa4, pa5, psort, latitude, longitude, phone, insta, face, web, mail, whatsapp, buyticket, " +                                    // 15
                "videoversion, mapversion, topphotocount, ptop0, ptop1, ptop2, ptop3, ptop4, ptop5, ptop6, ptop7, " +                                            // 11
                "ptop8, ptop9, ptop10, ptop11, ptop12, ptop13, ptop14, " +                                                                                       //  7
                "pdowncount, pdown0, pdown1, pdown2, pdown3, pdown4, pdown5, pdown6, pdown7, pdown8, pdown9, pdown10, pdown11, pdown12, pdown13, pdown14, " +    // 16
                "popen0, popen1, popen2, popen3, popen4, popen5, popen6, pclse0, pclse1, pclse2, pclse3, pclse4, pclse5, pclse6, newendtime, isactive) " +       // 16
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    }

    public static SQLiteDatabase open_events_database(Context context) {

        final SQLiteDatabase database = context.openOrCreateDatabase("events_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS events_database (eventId VARCHAR, pl1 VARCHAR, pl2 VARCHAR, pl3 VARCHAR, pl4 VARCHAR, pl5 VARCHAR, pe1 VARCHAR, pe2 VARCHAR, pe3 VARCHAR, pe4 VARCHAR, pe5 VARCHAR, " +
                "pa1 VARCHAR, pa2 VARCHAR, pa3 VARCHAR, pa4 VARCHAR, pa5 VARCHAR, latitude VARCHAR, longitude VARCHAR, " +
                "phone VARCHAR, insta VARCHAR, face VARCHAR, web VARCHAR, mail VARCHAR, whatsapp VARCHAR, buyticket VARCHAR, " +
                "videoversion VARCHAR, mapversion VARCHAR, topphotocount VARCHAR, " +
                "ptop0 VARCHAR, ptop1 VARCHAR, ptop2 VARCHAR, ptop3 VARCHAR, ptop4 VARCHAR, ptop5 VARCHAR, ptop6 VARCHAR, ptop7 VARCHAR, " +
                "ptop8 VARCHAR, ptop9 VARCHAR, ptop10 VARCHAR, ptop11 VARCHAR, ptop12 VARCHAR, ptop13 VARCHAR, ptop14 VARCHAR, " +
                "pdowncount VARCHAR, pdown0 VARCHAR, pdown1 VARCHAR, pdown2 VARCHAR, pdown3 VARCHAR, pdown4 VARCHAR, pdown5 VARCHAR, pdown6 VARCHAR, pdown7 VARCHAR, " +
                "pdown8 VARCHAR, pdown9 VARCHAR, pdown10 VARCHAR, pdown11 VARCHAR, pdown12 VARCHAR, pdown13 VARCHAR, pdown14 VARCHAR, " +
                "pupl1 VARCHAR, pupl2 VARCHAR, pupl3 VARCHAR, pupl4 VARCHAR, pupl5 VARCHAR, puml1 VARCHAR, puml2 VARCHAR, puml3 VARCHAR, puml4 VARCHAR, puml5 VARCHAR, " +
                "plpId VARCHAR, plmId VARCHAR, ptimecount VARCHAR, " +
                "ptstart0 VARCHAR, ptend0 VARCHAR, ptstart1 VARCHAR, ptend1 VARCHAR, ptstart2 VARCHAR, ptend2 VARCHAR, ptstart3 VARCHAR, ptend3 VARCHAR, " +
                "ptstart4 VARCHAR, ptend4 VARCHAR, ptstart5 VARCHAR, ptend5 VARCHAR, ptstart6 VARCHAR, ptend6 VARCHAR, ptstart7 VARCHAR, ptend7 VARCHAR, " +
                "ptstart8 VARCHAR, ptend8 VARCHAR, ptstart9 VARCHAR, ptend9 VARCHAR, ptstart10 VARCHAR, ptend10 VARCHAR, ptstart11 VARCHAR, ptend11 VARCHAR, " +
                "ptstart12 VARCHAR, ptend12 VARCHAR, ptstart13 VARCHAR, ptend13 VARCHAR, ptstart14 VARCHAR, ptend14 VARCHAR, ptstart15 VARCHAR, ptend15 VARCHAR, " +
                "ptstart16 VARCHAR, ptend16 VARCHAR, ptstart17 VARCHAR, ptend17 VARCHAR, ptstart18 VARCHAR, ptend18 VARCHAR, ptstart19 VARCHAR, ptend19 VARCHAR, " +
                "ptstart20 VARCHAR, ptend20 VARCHAR, ptstart21 VARCHAR, ptend21 VARCHAR, ptstart22 VARCHAR, ptend22 VARCHAR, ptstart23 VARCHAR, ptend23 VARCHAR, " +
                "ptstart24 VARCHAR, ptend24 VARCHAR, ptstart25 VARCHAR, ptend25 VARCHAR, ptstart26 VARCHAR, ptend26 VARCHAR, ptstart27 VARCHAR, ptend27 VARCHAR, " +
                "ptstart28 VARCHAR, ptend28 VARCHAR, ptstart29 VARCHAR, ptend29 VARCHAR, ptstart30 VARCHAR, ptend30 VARCHAR, ptstart31 VARCHAR, ptend31 VARCHAR, " +
                "ptstart32 VARCHAR, ptend32 VARCHAR, ptstart33 VARCHAR, ptend33 VARCHAR, ptstart34 VARCHAR, ptend34 VARCHAR, ptstart35 VARCHAR, ptend35 VARCHAR, " +
                "ptstart36 VARCHAR, ptend36 VARCHAR)");

        return database;
    }

    public static String get_events_database_insertString() {

        return "INSERT INTO events_database (eventId, pl1, pl2, pl3, pl4, pl5, pe1, pe2, pe3, pe4, pe5, " +             // 11
                "pa1, pa2, pa3, pa4, pa5, latitude, longitude, " +                                                                  //  7
                "phone, insta, face, web, mail, whatsapp, buyticket, videoversion, mapversion, topphotocount, " +                   // 10
                "ptop0, ptop1, ptop2, ptop3, ptop4, ptop5, ptop6, ptop7, ptop8, ptop9, ptop10, ptop11, ptop12, ptop13, ptop14, " +  // 15
                "pdowncount, pdown0, pdown1, pdown2, pdown3, pdown4, pdown5, pdown6, pdown7, " +                                    //  9
                "pdown8, pdown9, pdown10, pdown11, pdown12, pdown13, pdown14, " +                                                   //  7
                "pupl1, pupl2, pupl3, pupl4, pupl5, puml1, puml2, puml3, puml4, puml5, plpId, plmId, ptimecount, " +                // 13
                "ptstart0, ptend0, ptstart1, ptend1, ptstart2, ptend2, ptstart3, ptend3, " +                                        //  8
                "ptstart4, ptend4, ptstart5, ptend5, ptstart6, ptend6, ptstart7, ptend7, " +                                        //  8
                "ptstart8, ptend8, ptstart9, ptend9, ptstart10, ptend10, ptstart11, ptend11, " +                                    //  8
                "ptstart12, ptend12, ptstart13, ptend13, ptstart14, ptend14, ptstart15, ptend15, " +                                //  8
                "ptstart16, ptend16, ptstart17, ptend17, ptstart18, ptend18, ptstart19, ptend19, " +                                //  8
                "ptstart20, ptend20, ptstart21, ptend21, ptstart22, ptend22, ptstart23, ptend23, " +                                //  8
                "ptstart24, ptend24, ptstart25, ptend25, ptstart26, ptend26, ptstart27, ptend27, " +                                //  8
                "ptstart28, ptend28, ptstart29, ptend29, ptstart30, ptend30, ptstart31, ptend31, " +                                //  8
                "ptstart32, ptend32, ptstart33, ptend33, ptstart34, ptend34, ptstart35, ptend35, " +                                //  8
                "ptstart36, ptend36) VALUES (" +                                                                                    //  2
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public static SQLiteDatabase open_saved_photos_database(Context context) {

        final SQLiteDatabase database = context.openOrCreateDatabase("saved_photos_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS saved_photos_database (file_Id VARCHAR, mapversion VARCHAR, topcount VARCHAR, " +
                "top0 VARCHAR, top1 VARCHAR, top2 VARCHAR, top3 VARCHAR, top4 VARCHAR, top5 VARCHAR, top6 VARCHAR, top7 VARCHAR, top8 VARCHAR, " +
                "top9 VARCHAR, top10 VARCHAR, top11 VARCHAR, top12 VARCHAR, top13 VARCHAR, top14 VARCHAR, downcount VARCHAR, " +
                "down0 VARCHAR, down1 VARCHAR, down2 VARCHAR, down3 VARCHAR, down4 VARCHAR, down5 VARCHAR, down6 VARCHAR, down7 VARCHAR, " +
                "down8 VARCHAR, down9 VARCHAR, down10 VARCHAR, down11 VARCHAR, down12 VARCHAR, down13 VARCHAR, down14 VARCHAR)");

        return database;
    }

    public static String get_saved_photos_database_insertString() {

        return "INSERT INTO saved_photos_database (file_Id, mapversion, topcount, " +
                "top0, top1, top2, top3, top4, top5, top6, top7, top8, top9, top10, top11, top12, top13, top14, downcount, " +
                "down0, down1, down2, down3, down4, down5, down6, down7, down8, down9, down10, down11, down12, down13, down14) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public static SQLiteDatabase open_saved_event_photo_database(Context context) {

        final SQLiteDatabase database = context.openOrCreateDatabase("saved_event_photo_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS saved_event_photo_database (event_Id VARCHAR, mapversion VARCHAR, topcount VARCHAR, top0 VARCHAR, top1 VARCHAR, top2 VARCHAR, top3 VARCHAR, top4 VARCHAR, top5 VARCHAR, top6 VARCHAR, top7 VARCHAR, top8 VARCHAR, top9 VARCHAR, top10 VARCHAR, top11 VARCHAR, top12 VARCHAR, top13 VARCHAR, top14 VARCHAR, downcount VARCHAR, down0 VARCHAR, down1 VARCHAR, down2 VARCHAR, down3 VARCHAR, down4 VARCHAR, down5 VARCHAR, down6 VARCHAR, down7 VARCHAR, down8 VARCHAR, down9 VARCHAR, down10 VARCHAR, down11 VARCHAR, down12 VARCHAR, down13 VARCHAR, down14 VARCHAR)");

        return database;
    }

    public static String get_saved_event_photo_database_insertString() {

        return "INSERT INTO saved_event_photo_database (event_Id, mapversion, topcount, " +
                "top0, top1, top2, top3, top4, top5, top6, top7, top8, top9, top10, top11, top12, top13, top14, downcount, " +
                "down0, down1, down2, down3, down4, down5, down6, down7, down8, down9, down10, down11, down12, down13, down14) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public static SQLiteDatabase open_saved_video_database_database(Context context) {

        SQLiteDatabase database = context.openOrCreateDatabase("saved_video_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS saved_video_database (file_Id VARCHAR, videoversion VARCHAR, vsavedsortnumber VARCHAR)");

        return database;
    }

    public static String get_saved_video_database_insertString() {

        return "INSERT INTO saved_video_database (file_Id, videoversion, vsavedsortnumber) VALUES (?, ?, ?)";
    }

    public static SQLiteDatabase open_search_history_database(Context context) {

        SQLiteDatabase database = context.openOrCreateDatabase("search_history_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS search_history_database (history VARCHAR)");

        return database;
    }

    public static String get_history_database_insertString(Context context) {

        return "INSERT INTO search_history_database (history) VALUES (?)";

    }


    public static SQLiteDatabase open_favlist_database(Context context) {

        SQLiteDatabase database = context.openOrCreateDatabase("favlist_database", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS favlist_database (fav_Id VARCHAR, favsort VARCHAR)");

        return database;
    }

    public static String get_favlist_database_database_insertString(Context context) {

        return "INSERT INTO favlist_database (fav_Id, favsort) VALUES (?, ?)";

    }



    //SILINEN MEKANLARIN FOTO VE VIDEOLARINI SIL- DATABASE GUNCELLE              //----OK-----
    public static void clear_caches(Context context, final ArrayList<PlaceClass> placelist) {

        ArrayList<String> placeidlist = new ArrayList<>();
        ArrayList<String> deleted_id_list = new ArrayList<>();

        for (PlaceClass singleplace : placelist) {
            placeidlist.add(singleplace.getId());

        }

        final SQLiteDatabase database = open_saved_photos_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM saved_photos_database ", null);

        int file_Id_Ix = cursor.getColumnIndex("file_Id");

        cursor.moveToFirst();
        int i = 0;

        while (!cursor.isAfterLast()) {
            i++;

            String id = cursor.getString(file_Id_Ix);

            if (!placeidlist.contains(id)) { //SİLİNEN VAR

                deleted_id_list.add(id);
                System.out.println("------------*********------------");
                System.out.println("silinen mekan var: " + id);
                database.execSQL("DELETE FROM saved_photos_database WHERE file_Id = '" + id + "' ");

                if (getvideo_File(id).exists()) {
                    getvideo_File(id).delete();
                    System.out.println("videosuda varmıs");
                    SQLiteDatabase videodatabes = context.openOrCreateDatabase("saved_video_database", Context.MODE_PRIVATE, null);
                    videodatabes.execSQL("CREATE TABLE IF NOT EXISTS saved_video_database (file_Id VARCHAR, videoversion VARCHAR, vsavedsortnumber VARCHAR)");

                    videodatabes.execSQL("DELETE FROM saved_video_database WHERE file_Id = '" + id + "' ");

                }

                if (get_MAP_photo_File(id).exists()) get_MAP_photo_File(id).delete();

                for (int j = 0; i < 15; i++) {

                    if (get_DOWN_photo_File(id, j).exists()) {
                        get_DOWN_photo_File(id, j).delete();
                    }

                    if (get_TOP_photo_File(id, j).exists()) {
                        get_TOP_photo_File(id, j).delete();
                    }

                }

            }

            cursor.moveToNext();
        }

    }

    public static void clear_evetsphoto_caches(Context context, ArrayList<EventClass> eventlist) {

        ArrayList<String> deleted_event_id_list = new ArrayList<>();
        ArrayList<String> event_idlist = new ArrayList<>();

        for (EventClass event : eventlist) {
            event_idlist.add(event.getId());
        }

        final SQLiteDatabase database = open_saved_event_photo_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM saved_event_photo_database ", null);

        int event_Id_Ix = cursor.getColumnIndex("event_Id");

        cursor.moveToFirst();
        int i = 0;

        while (!cursor.isAfterLast()) {
            i++;

            String id = cursor.getString(event_Id_Ix);

            if (!event_idlist.contains(id)) { //SİLİNEN VAR

                deleted_event_id_list.add(id);
                System.out.println("----------*******--------");
                System.out.println("Silinen event var");
                database.execSQL("DELETE FROM saved_event_photo_database WHERE event_Id = '" + id + "' ");

                if (getvideo_File(id).exists()) {
                    getvideo_File(id).delete();

                    System.out.println("videosu varmıs");
                    SQLiteDatabase videodatabes = context.openOrCreateDatabase("saved_video_database", Context.MODE_PRIVATE, null);
                    videodatabes.execSQL("CREATE TABLE IF NOT EXISTS saved_video_database (file_Id VARCHAR, videoversion VARCHAR, vsavedsortnumber VARCHAR)");

                    videodatabes.execSQL("DELETE FROM saved_video_database WHERE file_Id = '" + id + "' ");

                }

                if (get_MAP_photo_File(id).exists()) get_MAP_photo_File(id);

                for (int j = 0; i < 15; i++) {

                    if (get_DOWN_photo_File(id, j).exists()) {
                        get_DOWN_photo_File(id, j).delete();
                    }

                    if (get_TOP_photo_File(id, j).exists()) {
                        get_TOP_photo_File(id, j).delete();
                    }

                }

            }

            cursor.moveToNext();
        }

    }

    //COK VIDEO OLDU MU - OLDUYSA SIL              //----OK-----
    public static void clearvideos(Context context) {

        final SQLiteDatabase database = open_saved_video_database_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM saved_video_database ", null);

        int file_Id_Ix = cursor.getColumnIndex("file_Id");
        int videoversion_Ix = cursor.getColumnIndex("videoversion");
        int vsavednumber_Ix = cursor.getColumnIndex("vsavedsortnumber");

        cursor.moveToFirst();

        int count = 0;

        ArrayList<SavedFilesClass> savedFiles_list = new ArrayList<>();

        while (!cursor.isAfterLast()) {

            String id = cursor.getString(file_Id_Ix);
            String videoversion = cursor.getString(videoversion_Ix);
            int vsavednumber = Integer.valueOf(cursor.getString(vsavednumber_Ix));

            ArrayList<String> photos = new ArrayList<>();

            SavedFilesClass savedFile = new SavedFilesClass(id, videoversion, vsavednumber);
            savedFiles_list.add(savedFile);

            if (vsavednumber > 0) count++;

            cursor.moveToNext();
        }

        Collections.sort(savedFiles_list, new Comparator<SavedFilesClass>() {
            @Override
            public int compare(SavedFilesClass o1, SavedFilesClass o2) {
                return o1.getVsavednumber() - o2.getVsavednumber();
            }
        });

        if (count > MAXVIDEOCAOUNT) {

            for (SavedFilesClass savedFile : savedFiles_list) {

                if (savedFile.getVsavednumber() == CONTINUE) {

                    if (getvideo_File(savedFile.getId()).exists()) {

                        getvideo_File(savedFile.getId()).delete();
                    }

                    database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + UNDOWNLOADED + "' WHERE file_Id = '" + savedFile.getId() + "' ");

                }

                if (savedFile.getVsavednumber() > 0) {

                    if (getvideo_File(savedFile.getId()).exists()) {

                        getvideo_File(savedFile.getId()).delete();
                        System.out.println("cok oldu bir video sildik");

                        database.execSQL("UPDATE saved_video_database SET vsavedsortnumber = '" + UNDOWNLOADED + "' WHERE file_Id = '" + savedFile.getId() + "' ");
                        count--;
                        if (count == MAXVIDEOCAOUNT) break;
                    }

                }

            }

        }


    }


    //CHECK PHOTOS - KONTROL ET BAKAM VAR MI YOK MU              //----OK-----
    public static void checkphotos(Context context) {

        SQLiteDatabase database = open_place_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM place_database", null);

        int placeId_Ix = cursor.getColumnIndex("placeId");

        ArrayList<String> placeidlist = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            placeidlist.add(cursor.getString(placeId_Ix));
            cursor.moveToNext();
        }

        for (String id : placeidlist) {
            save_photo(context, PreSets.getPlace(context, id));
        }

    }


    //CHECK EVENTS PHOTOS - KONTROL ET BAKAM VAR MI YOK MU              //----OK-----
    public static void check_eventsphotos(Context context) {

        final SQLiteDatabase database = open_saved_event_photo_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM saved_event_photo_database", null);

        int event_Id_Ix = cursor.getColumnIndex("event_Id");

        ArrayList<String> event_idlist = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            event_idlist.add(cursor.getString(event_Id_Ix));
            cursor.moveToNext();
        }

        for (String id : event_idlist) {
            save_photo(context, PreSets.get_Event(context, id));

        }
        database.close();

    }

    //FOTO KONTROL ET VAR MI BAKAM              //----OK-----
    public static void save_photo(Context context, PlaceClass place) {

        SQLiteDatabase database = open_saved_photos_database(context);

        String selectmainQuery = "SELECT * FROM saved_photos_database WHERE file_Id= '" + place.getId() + "'";
        Cursor cursor = database.rawQuery(selectmainQuery, null);

        //top0  down0
        int file_Id_Ix = cursor.getColumnIndex("file_Id");
        int topcount_Ix = cursor.getColumnIndex("topcount");
        int downcount_Ix = cursor.getColumnIndex("downcount");
        int mapversion_Ix = cursor.getColumnIndex("mapversion");

        cursor.moveToFirst();

        if (cursor.getCount() <= 0) { //DAHA ONCE KAYIT YOK ISE

            System.out.println("Yeni Place");
            String sqlstring = get_saved_photos_database_insertString();
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);
            sqLiteStatement.bindString(1, place.getId());
            sqLiteStatement.bindString(2, "default");
            sqLiteStatement.bindString(3, "0");

            for (int i = 4; i < 19; i++) { // MAX 18
                sqLiteStatement.bindString(i, "default");

            }
            sqLiteStatement.bindString(19, "0");

            for (int i = 20; i < 35; i++) { // MAX 18
                sqLiteStatement.bindString(i, "default");
            }

            sqLiteStatement.execute();

            if (!place.getMapphotoversion().equals("default"))
                download_photo(context, place, -1, -1);
            for (int i = 0; i < place.getTopphotos().size(); i++)
                download_photo(context, place, i, -1);
            for (int i = 0; i < place.getDownphotos().size(); i++)
                download_photo(context, place, -1, i);


        } else { //DATABASE DE KAYITLI

            String mapversion = cursor.getString(mapversion_Ix);

            if (mapversion.equals(place.getMapphotoversion())) {

                if (!get_MAP_photo_File(place.getId()).exists() && !place.getMapphotoversion().equals("default")) {
                    System.out.println("MAP OLMAYAN PLACE " + PreSets.setlanguage_name(context, place));
                    download_photo(context, place, -1, -1);
                }


            } else {

                if (place.getMapphotoversion().equals("default")) {  //MAP FOTO SILINMIS ISE
                    System.out.println("MAP SILINMIS " + PreSets.setlanguage_name(context, place));

                    if (get_MAP_photo_File(place.getId()).exists())
                        get_MAP_photo_File(place.getId()).delete();

                    database.execSQL("UPDATE saved_photos_database SET mapversion = '" + "default" + "' WHERE file_Id = '" + place.getId() + "' ");


                } else {

                    download_photo(context, place, -1, -1);

                }


            }

            int topcount = Integer.valueOf(cursor.getString(topcount_Ix));
            ArrayList<String> topphotos = new ArrayList<>();

            for (int i = 0; i < topcount; i++) {
                topphotos.add(cursor.getString(cursor.getColumnIndex("top" + i)));
            }

            if (topphotos.size() > place.getTopphotos().size()) { //CIKARMA OLMUSSA

                for (int i = 0; i < place.topphotos.size(); i++) {

                    System.out.println("TOP EKSILME VAR " + PreSets.setlanguage_name(context, place));

                    if (!place.getTopphotos().get(i).equals(topphotos.get(i))) {
                        System.out.println("TOP DEGISME VAR " + PreSets.setlanguage_name(context, place));

                        download_photo(context, place, i, -1);

                    } else {

                        if (!get_TOP_photo_File(place.getId(), i).exists()) {

                            download_photo(context, place, i, -1);

                        }


                    }

                }
                for (int i = place.getTopphotos().size(); i < topphotos.size(); i++) {

                    if (get_TOP_photo_File(place.getId(), i).exists()) {
                        System.out.println("TOP CIKARMA OLMUS BIZDE VAR SILIYORUZ" + PreSets.setlanguage_name(context, place));

                        get_TOP_photo_File(place.getId(), i).delete();

                    }

                    database.execSQL("UPDATE saved_photos_database SET top" + i + " = '" + "default" + "' WHERE file_Id = '" + place.getId() + "' ");
                }

                database.execSQL("UPDATE saved_photos_database SET topcount = '" + place.getTopphotos().size() + "' WHERE file_Id = '" + place.getId() + "' ");

            } else {        //FOTOLAR EKSILMEDIYSE

                for (int i = 0; i < topphotos.size(); i++) {

                    if (!place.getTopphotos().get(i).equals(topphotos.get(i))) {

                        System.out.println("TOP DEGISME VAR " + PreSets.setlanguage_name(context, place));
                        download_photo(context, place, i, -1);

                    } else {

                        if (!get_TOP_photo_File(place.getId(), i).exists()) {

                            download_photo(context, place, i, -1);
                        }

                    }
                }

                for (int i = topphotos.size(); i < place.getTopphotos().size(); i++) {
                    System.out.println("TOP EKLEME VAR" + PreSets.setlanguage_name(context, place));

                    download_photo(context, place, i, -1);

                }
            }


            //DOWN PHOTOS

            //------


            int downcount = Integer.valueOf(cursor.getString(downcount_Ix));
            ArrayList<String> downphotos = new ArrayList<>();

            for (int i = 0; i < downcount; i++) {
                downphotos.add(cursor.getString(cursor.getColumnIndex("down" + i)));
            }

            if (downphotos.size() > place.getDownphotos().size()) { //CIKARMA OLMUSSA

                for (int i = 0; i < place.downphotos.size(); i++) {

                    System.out.println("down EKSILME VAR " + PreSets.setlanguage_name(context, place));

                    if (!place.getDownphotos().get(i).equals(downphotos.get(i))) {
                        System.out.println("down DEGISME VAR " + PreSets.setlanguage_name(context, place));

                        download_photo(context, place, -1, i);

                    } else {

                        if (!get_DOWN_photo_File(place.getId(), i).exists()) {

                            download_photo(context, place, -1, i);

                        }


                    }

                }
                for (int i = place.getDownphotos().size(); i < downphotos.size(); i++) {

                    if (get_DOWN_photo_File(place.getId(), i).exists()) {
                        System.out.println("down CIKARMA OLMUS BIZDE VAR SILIYORUZ" + PreSets.setlanguage_name(context, place));

                        get_DOWN_photo_File(place.getId(), i).delete();

                    }

                    database.execSQL("UPDATE saved_photos_database SET down" + i + " = '" + "default" + "' WHERE file_Id = '" + place.getId() + "' ");
                }

                database.execSQL("UPDATE saved_photos_database SET downcount = '" + place.getDownphotos().size() + "' WHERE file_Id = '" + place.getId() + "' ");

            } else {        //FOTOLAR EKSILMEDIYSE

                for (int i = 0; i < downphotos.size(); i++) {

                    if (!place.getDownphotos().get(i).equals(downphotos.get(i))) {

                        System.out.println("down DEGISME VAR " + PreSets.setlanguage_name(context, place));
                        download_photo(context, place, -1, i);

                    } else {

                        if (!get_DOWN_photo_File(place.getId(), i).exists()) {

                            download_photo(context, place, -1, i);
                        }

                    }
                }

                for (int i = downphotos.size(); i < place.getDownphotos().size(); i++) {
                    System.out.println("down EKLEME VAR" + PreSets.setlanguage_name(context, place));

                    download_photo(context, place, -1, i);

                }
            }


        }
        database.close();
    }

    public static void save_photo(Context context, EventClass event) {

        SQLiteDatabase database = open_saved_event_photo_database(context);

        String selectmainQuery = "SELECT * FROM saved_event_photo_database WHERE event_Id= '" + event.getId() + "'";
        Cursor cursor = database.rawQuery(selectmainQuery, null);

        //top0  down0
        int event_id_Ix = cursor.getColumnIndex("event_Id");
        int topcount_Ix = cursor.getColumnIndex("topcount");
        int downcount_Ix = cursor.getColumnIndex("downcount");
        int mapversion_Ix = cursor.getColumnIndex("mapversion");

        cursor.moveToFirst();

        if (cursor.getCount() <= 0) { //DAHA ONCE KAYIT YOK ISE

            System.out.println("YENI EVENT :" + PreSets.setlanguage_name(context, event));

            String sqlstring = get_saved_event_photo_database_insertString();
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);
            sqLiteStatement.bindString(1, event.getId());
            sqLiteStatement.bindString(2, "default");
            sqLiteStatement.bindString(3, "0");

            for (int i = 4; i < 19; i++) { // MAX 18
                sqLiteStatement.bindString(i, "default");

            }
            sqLiteStatement.bindString(19, "0");

            for (int i = 20; i < 35; i++) { // MAX 18
                sqLiteStatement.bindString(i, "default");
            }

            sqLiteStatement.execute();


            if (!event.getMapphotoversion().equals("default"))
                download_photo(context, event, -1, -1);
            for (int i = 0; i < event.getTopphotos().size(); i++)
                download_photo(context, event, i, -1);
            for (int i = 0; i < event.getDownphotos().size(); i++)
                download_photo(context, event, -1, i);

        } else { //DATABASE DE KAYITLI

            String mapversion = cursor.getString(mapversion_Ix);

            if (mapversion.equals(event.getMapphotoversion())) {

                if (!get_MAP_photo_File(event.getId()).exists() && !event.getMapphotoversion().equals("default")) {

                    System.out.println("EVENT MAP INDIR:" + PreSets.setlanguage_name(context, event));

                    download_photo(context, event, -1, -1);
                }


            } else {

                if (event.getMapphotoversion().equals("default")) {  //MAP FOTO SILINMIS ISE

                    if (get_MAP_photo_File(event.getId()).exists())
                        get_MAP_photo_File(event.getId()).delete();
                    System.out.println("EVENT MAP SILINMIS:" + PreSets.setlanguage_name(context, event));

                    database.execSQL("UPDATE saved_event_photo_database SET mapversion = '" + "default" + "' WHERE event_Id = '" + event.getId() + "' ");


                } else {
                    System.out.println("EVENT MAP EKLENMIS:" + PreSets.setlanguage_name(context, event));

                    download_photo(context, event, -1, -1);

                }


            }

            int topcount = Integer.valueOf(cursor.getString(topcount_Ix));
            ArrayList<String> topphotos = new ArrayList<>();

            for (int i = 0; i < topcount; i++) {
                topphotos.add(cursor.getString(cursor.getColumnIndex("top" + i)));
            }

            if (topphotos.size() > event.getTopphotos().size()) { //CIKARMA OLMUSSA

                for (int i = 0; i < event.topphotos.size(); i++) {

                    if (!event.getTopphotos().get(i).equals(topphotos.get(i))) {

                        System.out.println("EVENT TOP DEGISMIS:" + PreSets.setlanguage_name(context, event));

                        download_photo(context, event, i, -1);

                    } else {

                        if (!get_TOP_photo_File(event.getId(), i).exists()) {

                            download_photo(context, event, i, -1);

                        }


                    }

                }

                for (int i = event.getTopphotos().size(); i < topphotos.size(); i++) {

                    if (get_TOP_photo_File(event.getId(), i).exists()) {
                        get_TOP_photo_File(event.getId(), i).delete();
                        System.out.println("EVENT TOP SILINMIS:" + PreSets.setlanguage_name(context, event));


                    }

                    database.execSQL("UPDATE saved_event_photo_database SET top" + i + " = '" + "default" + "' WHERE event_Id = '" + event.getId() + "' ");
                }

                database.execSQL("UPDATE saved_event_photo_database SET topcount = '" + event.getTopphotos().size() + "' WHERE event_Id = '" + event.getId() + "' ");

            } else {        //FOTOLAR EKSILMEDIYSE

                for (int i = 0; i < topphotos.size(); i++) {

                    if (!event.getTopphotos().get(i).equals(topphotos.get(i))) {
                        System.out.println("EVENT TOP DEGISMIS:" + PreSets.setlanguage_name(context, event));

                        download_photo(context, event, i, -1);

                    } else {

                        if (!get_TOP_photo_File(event.getId(), i).exists()) {

                            download_photo(context, event, i, -1);
                        }

                    }
                }

                for (int i = topphotos.size(); i < event.getTopphotos().size(); i++) {
                    System.out.println("EVENT TOP EKLENMIS:" + PreSets.setlanguage_name(context, event));

                    download_photo(context, event, i, -1);

                }
            }


            //DOWN PHOTOS


            int downcount = Integer.valueOf(cursor.getString(downcount_Ix));
            ArrayList<String> downphotos = new ArrayList<>();

            for (int i = 0; i < downcount; i++) {
                downphotos.add(cursor.getString(cursor.getColumnIndex("down" + i)));
            }

            if (downphotos.size() > event.getDownphotos().size()) { //CIKARMA OLMUSSA

                for (int i = 0; i < event.downphotos.size(); i++) {

                    if (!event.getDownphotos().get(i).equals(downphotos.get(i))) {

                        System.out.println("EVENT DOWN DEGISMIS:" + PreSets.setlanguage_name(context, event));

                        download_photo(context, event, -1, i);

                    } else {

                        if (!get_DOWN_photo_File(event.getId(), i).exists()) {

                            download_photo(context, event, -1, i);
                        }
                    }

                }
                for (int i = event.getDownphotos().size(); i < downphotos.size(); i++) {

                    if (get_DOWN_photo_File(event.getId(), i).exists()) {
                        get_DOWN_photo_File(event.getId(), i).delete();
                        System.out.println("EVENT DOWN EKSILMIS:" + PreSets.setlanguage_name(context, event));


                    }

                    database.execSQL("UPDATE saved_event_photo_database SET down" + i + " = '" + "default" + "' WHERE event_Id = '" + event.getId() + "' ");
                }

                database.execSQL("UPDATE saved_event_photo_database SET downcount = '" + event.getDownphotos().size() + "' WHERE event_Id = '" + event.getId() + "' ");

            } else {        //FOTOLAR EKSILMEDIYSE

                for (int i = 0; i < downphotos.size(); i++) {

                    if (!event.getDownphotos().get(i).equals(downphotos.get(i))) {
                        download_photo(context, event, -1, i);
                        System.out.println("EVENT DOWN DEGISMIS:" + PreSets.setlanguage_name(context, event));

                    } else {

                        if (!get_DOWN_photo_File(event.getId(), i).exists()) {

                            download_photo(context, event, -1, i);
                        }

                    }
                }

                for (int i = downphotos.size(); i < event.getDownphotos().size(); i++) {
                    System.out.println("EVENT DOWN EKLENMIS:" + PreSets.setlanguage_name(context, event));

                    download_photo(context, event, -1, i);

                }
            }


        }
        database.close();
    }


    //DOWNLOAD FOTO              //----OK-----
    public static void download_photo(final Context context, final PlaceClass place, final int topposition, final int downposition) {

        System.out.println("------- **** -------");
        System.out.println(PreSets.setlanguage_name(context, place) + " id: " + place.getId());
        String fotoname = "";
        File file = get_MAP_photo_File(place.getId());

        if (topposition == -1 && downposition == -1) {           //MAP INDIR

            System.out.println("MAP");
            fotoname = firebase_map_name(place.getId());
            file = get_MAP_photo_File(place.getId());

        } else if (topposition != -1) {    //TOP INDIR

            System.out.println("TOP " + topposition);
            fotoname = firebase_TOP_photoname(place.getId(), topposition);
            file = get_TOP_photo_File(place.getId(), topposition);

        } else {                        //DOWN INDIR
            System.out.println("DOWN " + downposition);
            fotoname = firebase_DOWN_photoname(place.getId(), downposition);
            file = get_DOWN_photo_File(place.getId(), downposition);

        }

        final SQLiteDatabase database = open_saved_photos_database(context);

        StorageReference newstorageReference = FirebaseStorage.getInstance().getReference().child(fotoname);

        newstorageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                if (topposition == -1 && downposition == -1) {//MAP INDI ISE

                    database.execSQL("UPDATE saved_photos_database SET mapversion = '" + place.getMapphotoversion() + "' WHERE file_Id = '" + place.getId() + "' ");

                } else if (topposition != -1) {//TOP INDI ISE
                    database.execSQL("UPDATE saved_photos_database SET top" + topposition + " = '" + place.getTopphotos().get(topposition) + "' WHERE file_Id = '" + place.getId() + "' ");
                    database.execSQL("UPDATE saved_photos_database SET topcount = '" + place.getTopphotos().size() + "' WHERE file_Id = '" + place.getId() + "' ");

                } else {    //DOWN INDI ISE
                    database.execSQL("UPDATE saved_photos_database SET down" + downposition + " = '" + place.getDownphotos().get(downposition) + "' WHERE file_Id = '" + place.getId() + "' ");
                    database.execSQL("UPDATE saved_photos_database SET downcount = '" + place.getDownphotos().size() + "' WHERE file_Id = '" + place.getId() + "' ");
                }

                try {

                    if (Appmain.activity.getLocalClassName().equals("PlaceView")) {
                        //((PlaceView)Appmain.activity).adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }

                database.close();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    public static void download_photo(final Context context, final EventClass event, final int topposition, final int downposition) {

        System.out.println("------- **** EVENT PHOTO -------");
        System.out.println(PreSets.setlanguage_name(context, event) + " id: " + event.getId());
        String fotoname = "";
        File file = get_MAP_photo_File(event.getId());

        if (topposition == -1 && downposition == -1) {           //MAP INDIR

            System.out.println("MAP");
            fotoname = firebase_map_name(event.getId());
            file = get_MAP_photo_File(event.getId());

        } else if (topposition != -1) {    //TOP INDIR

            System.out.println("TOP " + topposition);
            fotoname = firebase_TOP_photoname(event.getId(), topposition);
            file = get_TOP_photo_File(event.getId(), topposition);

        } else {                        //DOWN INDIR
            System.out.println("DOWN " + downposition);
            fotoname = firebase_DOWN_photoname(event.getId(), downposition);
            file = get_DOWN_photo_File(event.getId(), downposition);

        }

        final SQLiteDatabase database = open_saved_event_photo_database(context);

        StorageReference newstorageReference = FirebaseStorage.getInstance().getReference().child(fotoname);

        newstorageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                if (topposition == -1 && downposition == -1) {//MAP INDI ISE        saved_event_photo_database (event_Id,
                    System.out.println("map indi amk");
                    database.execSQL("UPDATE saved_event_photo_database SET mapversion = '" + event.getMapphotoversion() + "' WHERE event_Id = '" + event.getId() + "' ");

                } else if (topposition != -1) {//TOP INDI ISE
                    database.execSQL("UPDATE saved_event_photo_database SET top" + topposition + " = '" + event.getTopphotos().get(topposition) + "' WHERE event_Id = '" + event.getId() + "' ");
                    database.execSQL("UPDATE saved_event_photo_database SET topcount = '" + event.getTopphotos().size() + "' WHERE event_Id = '" + event.getId() + "' ");

                } else {    //DOWN INDI ISE
                    database.execSQL("UPDATE saved_event_photo_database SET down" + downposition + " = '" + event.getDownphotos().get(downposition) + "' WHERE event_Id = '" + event.getId() + "' ");
                    database.execSQL("UPDATE saved_event_photo_database SET downcount = '" + event.getDownphotos().size() + "' WHERE event_Id = '" + event.getId() + "' ");
                }

                try {

                    if (Appmain.activity.getLocalClassName().equals("EventView")) {
                        ((EventView) Appmain.activity).adapter.notifyDataSetChanged();
                    } else if (Appmain.activity.getLocalClassName().equals("EventActivity")) {
                        ((EventActivity) Appmain.activity).eventAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {

                }
                try {
                    FragmentMainCollections.getInstance().todayeventAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

                database.close();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }


    //GET DATA FROM FIREBASE
    public static void get_place_data_fromfirebase(final Context context, final String place_version) {

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        final SQLiteDatabase database = open_place_database(context);
        database.execSQL("DELETE FROM place_database");

        mref.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<PlaceClass> placelist_forcache = new ArrayList<>();

                // START CREATE DATABASE

                for (DataSnapshot collection_ds : dataSnapshot.getChildren()) {  //KOLEKSIYONLAR

                    PlaceCollectionClass maincollection = collection_ds.child("info").getValue(PlaceCollectionClass.class);

                    for (DataSnapshot subcollection_ds : collection_ds.child("subs").getChildren()) { //SUB KOLEKSIYONLAR

                        PlaceCollectionClass subcollection = subcollection_ds.child("info").getValue(PlaceCollectionClass.class);

                        for (DataSnapshot placesds : subcollection_ds.child("places").getChildren()) { //PLACES

                            System.out.println("placesds.getValue : "+ placesds.getValue());

                            PlaceClass place = placesds.getValue(PlaceClass.class);
                            placelist_forcache.add(place);

                            String sqlstring = get_place_database_insertString();
                            SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);

                            sqLiteStatement.bindString(1, maincollection.getId());
                            sqLiteStatement.bindString(2, maincollection.getName().get(0));
                            sqLiteStatement.bindString(3, maincollection.getName().get(1));
                            sqLiteStatement.bindString(4, maincollection.getName().get(2));
                            sqLiteStatement.bindString(5, maincollection.getName().get(3));
                            sqLiteStatement.bindString(6, maincollection.getName().get(4));
                            sqLiteStatement.bindString(7, String.valueOf(maincollection.getIconnumber()));
                            sqLiteStatement.bindString(8, String.valueOf(maincollection.getSortnumber()));
                            sqLiteStatement.bindString(9, String.valueOf(maincollection.getType()));
                            sqLiteStatement.bindString(10, maincollection.getLink());

                            sqLiteStatement.bindString(11, subcollection.getId());
                            sqLiteStatement.bindString(12, subcollection.getName().get(0));
                            sqLiteStatement.bindString(13, subcollection.getName().get(1));
                            sqLiteStatement.bindString(14, subcollection.getName().get(2));
                            sqLiteStatement.bindString(15, subcollection.getName().get(3));
                            sqLiteStatement.bindString(16, subcollection.getName().get(4));
                            sqLiteStatement.bindString(17, String.valueOf(subcollection.getIconnumber()));
                            sqLiteStatement.bindString(18, String.valueOf(subcollection.getSortnumber()));

                            sqLiteStatement.bindString(19, place.getId());
                            sqLiteStatement.bindString(20, place.getName().get(0));
                            sqLiteStatement.bindString(21, place.getName().get(1));
                            sqLiteStatement.bindString(22, place.getName().get(2));
                            sqLiteStatement.bindString(23, place.getName().get(3));
                            sqLiteStatement.bindString(24, place.getName().get(4));

                            sqLiteStatement.bindString(25, place.getExplain().get(0));
                            sqLiteStatement.bindString(26, place.getExplain().get(1));
                            sqLiteStatement.bindString(27, place.getExplain().get(2));
                            sqLiteStatement.bindString(28, place.getExplain().get(3));
                            sqLiteStatement.bindString(29, place.getExplain().get(4));

                            sqLiteStatement.bindString(30, place.getAdres().get(0));
                            sqLiteStatement.bindString(31, place.getAdres().get(1));
                            sqLiteStatement.bindString(32, place.getAdres().get(2));
                            sqLiteStatement.bindString(33, place.getAdres().get(3));
                            sqLiteStatement.bindString(34, place.getAdres().get(4));

                            sqLiteStatement.bindString(35, String.valueOf(place.getSortnumber()));
                            sqLiteStatement.bindString(36, String.valueOf(place.getLocation().getLat()));
                            sqLiteStatement.bindString(37, String.valueOf(place.getLocation().getLog()));

                            sqLiteStatement.bindString(38, place.getContactinfo().getPhonenumber());
                            sqLiteStatement.bindString(39, place.getContactinfo().getInstagram());
                            sqLiteStatement.bindString(40, place.getContactinfo().getFacebook());
                            sqLiteStatement.bindString(41, place.getContactinfo().getWebsite());
                            sqLiteStatement.bindString(42, place.getContactinfo().getMailadress());
                            sqLiteStatement.bindString(43, place.getContactinfo().getWhatsapp());
                            sqLiteStatement.bindString(44, place.getContactinfo().getBuyticket());

                            sqLiteStatement.bindString(45, place.getVideoversion());
                            sqLiteStatement.bindString(46, place.getMapphotoversion());

                            int topcount;
                            topcount = place.getTopphotos().size();

                            sqLiteStatement.bindString(47, String.valueOf(topcount));

                            for (int i = 48; i < 63; i++) {//MAX 62

                                if (topcount > 0) {

                                    sqLiteStatement.bindString(i, place.getTopphotos().get(i - 48));
                                    topcount--;

                                } else sqLiteStatement.bindString(i, "default");

                            }

                            int downcount;
                            downcount = place.getDownphotos().size();

                            sqLiteStatement.bindString(63, String.valueOf(downcount));

                            for (int i = 64; i < 79; i++) {//MAX 78

                                if (downcount > 0) {

                                    sqLiteStatement.bindString(i, place.getDownphotos().get(i - 64));
                                    downcount--;

                                } else sqLiteStatement.bindString(i, "default");

                            }

                            for (int i = 79; i < 86; i++) {    //MAX 85

                                sqLiteStatement.bindString(i, String.valueOf(place.getOpentime().get(i - 79)));


                            }

                            for (int i = 86; i < 93; i++) {    //MAX 91

                                sqLiteStatement.bindString(i, String.valueOf(place.getClosetime().get(i - 86)));

                            }

                            sqLiteStatement.bindString(93, String.valueOf(place.getNew_end_time()));

                            if (place.getIsactive()) {
                                sqLiteStatement.bindString(94, String.valueOf(PlaceClass.ACTIVE_PLACE));

                            } else
                                sqLiteStatement.bindString(94, String.valueOf(PlaceClass.INACTIVE_PLACE));

                            sqLiteStatement.execute();

                            PreSets.save_photo(context, place);

                        }

                    }

                }

                //END OF CREATE DATABASE

                sharedPreferences.edit().putString("place_version", place_version).apply();

                try {
                    FragmentMainCollections.setmainCollections(context);
                } catch (Exception e) {

                }

                try {
                    FragmentNotifications.getInstance().setnotificationList();
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    FragmentMainCollections.getInstance().settodayevent();
                }catch (Exception e){}

                database.close();
                PreSets.clear_caches(context, placelist_forcache);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static void get_events_data_fromfirebase(final Context context, final String events_version) {

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        final SQLiteDatabase database = open_events_database(context);
        database.execSQL("DELETE FROM events_database");

        mref.child("Activities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<EventClass> evensLists = new ArrayList<>();

                // START CREATE DATABASE

                for (DataSnapshot singleevent_ds : dataSnapshot.getChildren()) { //EVENTS

                    EventClass event = singleevent_ds.getValue(EventClass.class);
                    evensLists.add(event);

                    String sqlstring = get_events_database_insertString();

                    SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);

                    sqLiteStatement.bindString(1, event.getId());

                    sqLiteStatement.bindString(2, event.getName().get(0));
                    sqLiteStatement.bindString(3, event.getName().get(1));
                    sqLiteStatement.bindString(4, event.getName().get(2));
                    sqLiteStatement.bindString(5, event.getName().get(3));
                    sqLiteStatement.bindString(6, event.getName().get(4));

                    sqLiteStatement.bindString(7, event.getExplain().get(0));
                    sqLiteStatement.bindString(8, event.getExplain().get(1));
                    sqLiteStatement.bindString(9, event.getExplain().get(2));
                    sqLiteStatement.bindString(10, event.getExplain().get(3));
                    sqLiteStatement.bindString(11, event.getExplain().get(4));

                    sqLiteStatement.bindString(12, event.getAdres().get(0));
                    sqLiteStatement.bindString(13, event.getAdres().get(1));
                    sqLiteStatement.bindString(14, event.getAdres().get(2));
                    sqLiteStatement.bindString(15, event.getAdres().get(3));
                    sqLiteStatement.bindString(16, event.getAdres().get(4));

                    sqLiteStatement.bindString(17, String.valueOf(event.getLocation().getLat()));
                    sqLiteStatement.bindString(18, String.valueOf(event.getLocation().getLog()));

                    sqLiteStatement.bindString(19, event.getContactinfo().getPhonenumber());
                    sqLiteStatement.bindString(20, event.getContactinfo().getInstagram());
                    sqLiteStatement.bindString(21, event.getContactinfo().getFacebook());
                    sqLiteStatement.bindString(22, event.getContactinfo().getWebsite());
                    sqLiteStatement.bindString(23, event.getContactinfo().getMailadress());
                    sqLiteStatement.bindString(24, event.getContactinfo().getWhatsapp());
                    sqLiteStatement.bindString(25, event.getContactinfo().getBuyticket());

                    sqLiteStatement.bindString(26, event.getVideoversion());
                    sqLiteStatement.bindString(27, event.getMapphotoversion());

                    int topcount;
                    topcount = event.getTopphotos().size();

                    sqLiteStatement.bindString(28, String.valueOf(topcount));

                    for (int i = 29; i < 44; i++) {//MAX 43

                        if (topcount > 0) {

                            sqLiteStatement.bindString(i, event.getTopphotos().get(i - 29));
                            topcount--;

                        } else sqLiteStatement.bindString(i, "default");

                    }


                    int downcount;
                    downcount = event.getDownphotos().size();

                    sqLiteStatement.bindString(44, String.valueOf(downcount));

                    for (int i = 45; i < 60; i++) {//MAX 59

                        if (downcount > 0) {

                            sqLiteStatement.bindString(i, event.getDownphotos().get(i - 45));
                            downcount--;

                        } else sqLiteStatement.bindString(i, "default");

                    }

                    sqLiteStatement.bindString(60, event.getUnlinked_place().get(0));
                    sqLiteStatement.bindString(61, event.getUnlinked_place().get(1));
                    sqLiteStatement.bindString(62, event.getUnlinked_place().get(2));
                    sqLiteStatement.bindString(63, event.getUnlinked_place().get(3));
                    sqLiteStatement.bindString(64, event.getUnlinked_place().get(4));

                    sqLiteStatement.bindString(65, event.getUnlinked_management().get(0));
                    sqLiteStatement.bindString(66, event.getUnlinked_management().get(1));
                    sqLiteStatement.bindString(67, event.getUnlinked_management().get(2));
                    sqLiteStatement.bindString(68, event.getUnlinked_management().get(3));
                    sqLiteStatement.bindString(69, event.getUnlinked_management().get(4));

                    sqLiteStatement.bindString(70, event.getLinked_place());
                    sqLiteStatement.bindString(71, event.getLinked_management());

                    int timescount;
                    timescount = event.getTimes().size();

                    sqLiteStatement.bindString(72, String.valueOf(timescount));

                    for (int i = 0; i < 37; i++) {//MAX 146

                        int indexstart = (i * 2) + 73;
                        int index__end = (i * 2) + 74;

                        if (timescount > 0) {

                            sqLiteStatement.bindString(indexstart, event.getTimes().get(i).getStart_time().toString());
                            sqLiteStatement.bindString(index__end, event.getTimes().get(i).getEnd_time().toString());

                        } else {

                            sqLiteStatement.bindString(indexstart, "default");
                            sqLiteStatement.bindString(index__end, "default");


                        }

                        timescount--;


                    }

                    sqLiteStatement.execute();

                    PreSets.save_photo(context, event);

                }

                //END OF CREATE DATABASE

                database.close();
                sharedPreferences.edit().putString("events_version", events_version).apply();

                PreSets.clear_evetsphoto_caches(context, evensLists);


                if (Appmain.activity.getLocalClassName().equals("EventActivity")) {
                    ((EventActivity) Appmain.activity).setEvents();


                } else {

                    FragmentMainCollections.getInstance().settodayevent();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static int getImage(int iconnumber){
        int[] imageid = {

                R.drawable.z000,
                R.drawable.z001,
                R.drawable.z002,
                R.drawable.z003,
                R.drawable.z004,
                R.drawable.z005,
                R.drawable.z006,
                R.drawable.z007,
                R.drawable.z008,
                R.drawable.z009,

                R.drawable.z010,
                R.drawable.z011,
                R.drawable.z012,
                R.drawable.z013,
                R.drawable.z014,
                R.drawable.z015,
                R.drawable.z016,
                R.drawable.z017,
                R.drawable.z018,
                R.drawable.z019,

                R.drawable.z020,
                R.drawable.z021,
                R.drawable.z022,
                R.drawable.z023,
                R.drawable.z024,
                R.drawable.z025,
                R.drawable.z026,
                R.drawable.z027,
                R.drawable.z028,
                R.drawable.z029,

                R.drawable.z030,
                R.drawable.z031,
                R.drawable.z032,
                R.drawable.z033,
                R.drawable.z034,
                R.drawable.z035,
                R.drawable.z036,
                R.drawable.z037,
                R.drawable.z038,
                R.drawable.z039,

                R.drawable.z040,
                R.drawable.z041,
                R.drawable.z042,
                R.drawable.z043,
                R.drawable.z044,
                R.drawable.z045,
                R.drawable.z046,
                R.drawable.z047,
                R.drawable.z048,
                R.drawable.z049,

                R.drawable.z050,
                R.drawable.z051,
                R.drawable.z052,
                R.drawable.z053,
                R.drawable.z054,
                R.drawable.z055,
                R.drawable.z056,
                R.drawable.z057,
                R.drawable.z058,
                R.drawable.z059,

                R.drawable.z060,
                R.drawable.z061,
                R.drawable.z062,
                R.drawable.z063,
                R.drawable.z064,
                R.drawable.z065,
                R.drawable.z066,
                R.drawable.z067,
                R.drawable.z068,
                R.drawable.z069,

                R.drawable.z070,
                R.drawable.z071,
                R.drawable.z072,
                R.drawable.z073,
                R.drawable.z074,
                R.drawable.z075,
                R.drawable.z076,
                R.drawable.z077,
                R.drawable.z078,
                R.drawable.z079,

                R.drawable.z080,
                R.drawable.z081,
                R.drawable.z082,
                R.drawable.z083,
                R.drawable.z084,
                R.drawable.z085,
                R.drawable.z086,
                R.drawable.z087,
                R.drawable.z088,
                R.drawable.z089,

                R.drawable.z090,
                R.drawable.z091,
                R.drawable.z092,
                R.drawable.z093,
                R.drawable.z094,
                R.drawable.z095,
                R.drawable.z096,
                R.drawable.z097,
                R.drawable.z098,
                R.drawable.z099,


                R.drawable.z100,
                R.drawable.z101,
                R.drawable.z102,
                R.drawable.z103,
                R.drawable.z104,
                R.drawable.z105,
                R.drawable.z106,
                R.drawable.z107,
                R.drawable.z108,
                R.drawable.z109,

                R.drawable.z110,
                R.drawable.z111,
                R.drawable.z112,
                R.drawable.z113,
                R.drawable.z114,
                R.drawable.z115,
                R.drawable.z116,
                R.drawable.z117,
                R.drawable.z118,
                R.drawable.z119, //

                R.drawable.z120,
                R.drawable.z121,
                R.drawable.z122,
                R.drawable.z123,
                R.drawable.z124,
                R.drawable.z125,
                R.drawable.z126,
                R.drawable.z127,
                R.drawable.z128,
                R.drawable.z129,

                R.drawable.z130,
                R.drawable.z131,
                R.drawable.z132,
                R.drawable.z133,
                R.drawable.z134,
                R.drawable.z135,
                R.drawable.z136,
                R.drawable.z137,
                R.drawable.z138,
                R.drawable.z139,

                R.drawable.z140,
                R.drawable.z141,
                R.drawable.z142,
                R.drawable.z143,
                R.drawable.z144,
                R.drawable.z145,
                R.drawable.z146,
                R.drawable.z147,
                R.drawable.z148,
                R.drawable.z149,

                R.drawable.z150,
                R.drawable.z151,
                R.drawable.z152,
                R.drawable.z153,
                R.drawable.z154,
                R.drawable.z155,
                R.drawable.z156,
                R.drawable.z157,
                R.drawable.z158,
                R.drawable.z159,

                R.drawable.z160,
                R.drawable.z161,
                R.drawable.z162,
                R.drawable.z163,
                R.drawable.z164,
                R.drawable.z165,
                R.drawable.z166,
                R.drawable.z167,
                R.drawable.z168,
                R.drawable.z169,

                R.drawable.z170,
                R.drawable.z171,
                R.drawable.z172,
                R.drawable.z173,
                R.drawable.z174,
                R.drawable.z175,
                R.drawable.z176,
                R.drawable.z177,
                R.drawable.z178,
                R.drawable.z179,

                R.drawable.z180,
                R.drawable.z181,
                R.drawable.z182,
                R.drawable.z183,
                R.drawable.z184,
                R.drawable.z185,
                R.drawable.z186,
                R.drawable.z187,
                R.drawable.z188,
                R.drawable.z189,

                R.drawable.z190,
                R.drawable.z191,
                R.drawable.z192,
                R.drawable.z193,
                R.drawable.z194,
                R.drawable.z195,
                R.drawable.z196,
                R.drawable.z197,
                R.drawable.z198,
                R.drawable.z199,

                R.drawable.z200,
                R.drawable.z201,
                R.drawable.z202,
                R.drawable.z203,
                R.drawable.z204,
                R.drawable.z205,
                R.drawable.z206,
                R.drawable.z207,
                R.drawable.z208,
                R.drawable.z209,

                R.drawable.z210,
                R.drawable.z211,
                R.drawable.z212,
                R.drawable.z213,
                R.drawable.z214,
                R.drawable.z215,
                R.drawable.z216,
                R.drawable.z217,
                R.drawable.z218,
                R.drawable.z219,

                R.drawable.z220,
                R.drawable.z221,
                R.drawable.z222,
                R.drawable.z223,
                R.drawable.z224,
                R.drawable.z225,
                R.drawable.z226,
                R.drawable.z227,
                R.drawable.z228,
                R.drawable.z229,

                R.drawable.z230,
                R.drawable.z231,
                R.drawable.z232,
                R.drawable.z233,
                R.drawable.z234,
                R.drawable.z235,
                R.drawable.z236,
                R.drawable.z237,
                R.drawable.z238,
                R.drawable.z239,

                R.drawable.z240,
                R.drawable.z241,
                R.drawable.z242,
                R.drawable.z243,
                R.drawable.z244,
                R.drawable.z245,
                R.drawable.z246,
                R.drawable.z247,
                R.drawable.z248,

                R.drawable.z249,

                R.drawable.z250,
                R.drawable.z251,
                R.drawable.z252,
                R.drawable.z253,
                R.drawable.z254,
                R.drawable.z255,
                R.drawable.z256,
                R.drawable.z257,
                R.drawable.z258,
                R.drawable.z259,

                R.drawable.z260,
                R.drawable.z261,
                R.drawable.z262,
                R.drawable.z263,
                R.drawable.z264,
                R.drawable.z265,
                R.drawable.z266,
                R.drawable.z267,
                R.drawable.z268,
                R.drawable.z269,

                R.drawable.z270,
                R.drawable.z271,
                R.drawable.z272,
                R.drawable.z273,
                R.drawable.z274,
                R.drawable.z275,
                R.drawable.z276,
                R.drawable.z277,
                R.drawable.z278,
                R.drawable.z279,

                R.drawable.z280,
                R.drawable.z281,
                R.drawable.z282,
                R.drawable.z283,
                R.drawable.z284
        };

        return imageid[iconnumber];
    }

    //ICON FOTO SETTERS
    public static void setImageIcon(ImageView image, int position) {

        if (position < ICONCOUNT && position >= 0)
            image.setImageResource(getImage(position));
        else image.setImageResource(getImage(0));

    }

    public static void set_TOP_photo(ImageView imagephoto, String id, int position) {

        if (get_TOP_photo_File(id, position).exists()) {

            imagephoto.setImageResource(R.drawable.app_logo);
            imagephoto.setImageURI(Uri.fromFile(get_TOP_photo_File(id, position)));

        } else {

            imagephoto.setImageResource(R.drawable.app_logo);
        }


    }

    public static void set_DOWN_photo(ImageView imagephoto, String id, int position) {

        if (get_DOWN_photo_File(id, position).exists()) {
            imagephoto.setImageResource(R.drawable.app_logo);
            imagephoto.setImageURI(Uri.fromFile(get_DOWN_photo_File(id, position)));

        } else {

            imagephoto.setImageResource(R.drawable.app_logo);
        }


    }

    public static void set_MAIN_photo(ImageView imagephoto, String id) {

        imagephoto.setImageResource(R.drawable.app_logo);
        set_TOP_photo(imagephoto, id, 0);

    }

    public static void set_MAP_photo(ImageView imagephoto, String id) {

        if (get_MAP_photo_File(id).exists()) {

            imagephoto.setImageResource(R.drawable.app_logo);
            imagephoto.setImageURI(Uri.fromFile(get_MAP_photo_File(id)));

        } else {

            imagephoto.setImageResource(R.drawable.app_logo);
        }


    }


    //SET LANGUAGES
    public static String get_general_language(Context context, ArrayList<String> arrayList) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((arrayList.get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return arrayList.get(SelectLanguage.ENGLISH);

        } else
            return arrayList.get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }


    public static String setlanguage_name(Context context, PlaceClass place) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((place.getName().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return place.getName().get(SelectLanguage.ENGLISH);

        } else
            return place.getName().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_name(Context context, EventClass event) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((event.getName().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return event.getName().get(SelectLanguage.ENGLISH);

        } else
            return event.getName().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_explain(Context context, PlaceClass place) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((place.getExplain().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return place.getExplain().get(SelectLanguage.ENGLISH);

        } else
            return place.getExplain().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_explain(Context context, EventClass event) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((event.getExplain().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return event.getExplain().get(SelectLanguage.ENGLISH);

        } else
            return event.getExplain().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_adress(Context context, PlaceClass place) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((place.getAdres().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return place.getAdres().get(SelectLanguage.ENGLISH);

        } else
            return place.getAdres().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_adress(Context context, EventClass event) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((event.getAdres().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return event.getAdres().get(SelectLanguage.ENGLISH);

        } else
            return event.getAdres().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_name(Context context, PlaceCollectionClass collection) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((collection.getName().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return collection.getName().get(SelectLanguage.ENGLISH);

        } else
            return collection.getName().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));


    }

    public static String setlanguage_unlinked_place(Context context, EventClass event) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((event.getUnlinked_place().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return event.getUnlinked_place().get(SelectLanguage.ENGLISH);

        } else
            return event.getUnlinked_place().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }

    public static String setlanguage_unlinked_management(Context context, EventClass event) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        if ((event.getUnlinked_management().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH)).equals(""))) {

            return event.getUnlinked_management().get(SelectLanguage.ENGLISH);

        } else
            return event.getUnlinked_management().get(sharedPreferences.getInt("language", SelectLanguage.ENGLISH));

    }


    //TIME OPERATIONS
    public static String getTimeAgo(Context context, long time) {

        long now = System.currentTimeMillis();

        long diff = now - time;

        if (time <= 0 || diff < 0) return "";

        else if (diff < MINUTE_MILLIS) {
            return "biraz önce";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 dk";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " dk";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 saat";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " saat";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "dün";
        } else if (diff / DAY_MILLIS < 30) {
            return diff / DAY_MILLIS + " gün";
        } else return getCal(context, time);
    }

    public static String getCal(Context context, Object time_val) {

        long time = Long.parseLong(String.valueOf(time_val));
        long now = System.currentTimeMillis();

        if (time < 0) return "";

        Calendar cal_time = Calendar.getInstance(Locale.ENGLISH);
        cal_time.setTimeInMillis(time);

        Calendar now_time = Calendar.getInstance(Locale.ENGLISH);
        now_time.setTimeInMillis(now);

        if (DateFormat.format("dd-MM-yyyy", cal_time).toString().
                equals(DateFormat.format("dd-MM-yyyy", now_time).toString())) { // AYNI GUN İSE

            return DateFormat.format("hh:mm a", cal_time).toString();

        } else if (DateFormat.format("yyyy", cal_time).toString().
                equals(DateFormat.format("yyyy", now_time).toString())) { // AYNI YIL İSE

            String kacıncı_ay = DateFormat.format("MM", cal_time).toString();

            return DateFormat.format("dd", cal_time).toString() + getlanguagesAy(context, Integer.parseInt(kacıncı_ay)) +
                    DateFormat.format(" hh:mm a", cal_time).toString();
        } else {

            String kacıncı_ay = DateFormat.format("MM", cal_time).toString();
            return DateFormat.format("dd", cal_time).toString() + getlanguagesAy(context, Integer.parseInt(kacıncı_ay)) +
                    DateFormat.format(" yyyy hh:mm a", cal_time).toString();
        }

    }

    //NETWORK CONNECTIONS
    public static boolean networkConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isConnected() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    //LANGUGES TIMES OPERATIONS
    public static String getlanguages_date(Context context, Long time) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

        int ay = calendar.get(Calendar.MONTH);

        return calendar.get(Calendar.DAY_OF_MONTH) + " " +
                getlanguagesAy(context, ay) + " " +
                calendar.get(Calendar.YEAR) + " " + getlanguagesgun(context, calendar.get(Calendar.DAY_OF_WEEK));


    }

    public static String gethour(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

        int hour = calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {

            hour = hour + 12;
        }


        return "" + hour;

    }

    public static String getmimute(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

        int minute = calendar.get(Calendar.MINUTE);

        if (minute < 10) return "0" + minute;
        else return "" + minute;

    }

    public static String getlanguagesAy(Context context, int ay) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        String[][] mount = {
                {"January", "Ocak", "январь", "Ιανουάριος", "Januar"},
                {"February", "Şubat", "февраль", "Φεβρουάριος", "Februar"},
                {"March", "Mart", "март", "Μάρτιος", "März"},
                {"April", "Nisan", "апрель", "Απρίλιος", "April"},
                {"May", "Mayıs", "май", "Μάιος", "Mai"},
                {"June", "Haziran", "июнь", "Ιούνιος", "Juni"},
                {"July", "Temmuz", "июль", "Ιούλιος", "Juli"},
                {"August", "Ağustos", "август", "Αύγουστος", "August"},
                {"Septembe", "Eylül", "сентябрь", "Σεπτέμβριος", "September"},
                {"October", "Ekim", "октябрь", "Οκτώβριος", "Oktober"},
                {"November", "Kasım", "ноябрь", "Νοέμβριος", "November"},
                {"December", "Aralık", "декабрь", "Δεκέμβριος", "Dezember"}};

        return mount[ay][sharedPreferences.getInt("language", SelectLanguage.ENGLISH)];
    }

    public static String getlanguagesgun(Context context, int dayofthewek) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        dayofthewek = dayofthewek - 1;

        String[][] days = {

                {"Sunday", "Pazar", "воскресенье", "Κυριακή", "Sonntag"},
                {"Monday", "Pazartesi", "понедельник", "Δευτέρα", "Montag"},
                {"Tuesday", "Salı", "вторник", "Τρίτι", "Dienstag"},
                {"Wednesday", "Çarşamba", "среда", "Τετάρτη", "Mittwoch"},
                {"Thursday", "Perşembe", "четверг", "Πέμπτη", "Donnerstag"},
                {"Friday", "Cuma", "пятница", "Παρασκευή", "Freitag"},
                {"Saturday", "Cumartesi", "суббота", "Σάββατο", "Samstag"}

        };


        return days[dayofthewek][sharedPreferences.getInt("language", SelectLanguage.ENGLISH)];

    }

    //DATABASE SHORTCUTS
    public static PlaceCollectionClass getmaincollection_from_placeId(Context context, String placeid) {

        SQLiteDatabase database = open_place_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM place_database WHERE placeId= '" + placeid + "'", null);
        // Cursor cursor =database.rawQuery("SELECT * FROM lessons "WHERE age < 30 AND NAME = 'ali'",null);     yası 30dan buyuk olan
        //WHERE name LIKE '%a%'    içinde a gecen
        //WHERE name LIKE 'R%'     R ile baslayan

        // database.execSQL("UPDATE lessons SET age = 23 WHERE name = 'ali' ");

        int mainId_Ix = cursor.getColumnIndex("mainId");

        int l1_Ix = cursor.getColumnIndex("ml1");
        int l2_Ix = cursor.getColumnIndex("ml2");
        int l3_Ix = cursor.getColumnIndex("ml3");
        int l4_Ix = cursor.getColumnIndex("ml4");
        int l5_Ix = cursor.getColumnIndex("ml5");
        int micon_Ix = cursor.getColumnIndex("micon");
        int msort_Ix = cursor.getColumnIndex("msort");
        int mtype_Ix = cursor.getColumnIndex("mtype");
        int mlink_Ix = cursor.getColumnIndex("mlink");

        cursor.moveToFirst();

        String mainid, l1, l2, l3, l4, l5;
        int iconnumber, sortnumber, mtype;
        String link;

        mainid = cursor.getString(mainId_Ix);
        l1 = cursor.getString(l1_Ix);
        l2 = cursor.getString(l2_Ix);
        l3 = cursor.getString(l3_Ix);
        l4 = cursor.getString(l4_Ix);
        l5 = cursor.getString(l5_Ix);
        iconnumber = Integer.parseInt(cursor.getString(micon_Ix));
        sortnumber = Integer.parseInt(cursor.getString(msort_Ix));
        mtype = Integer.parseInt(cursor.getString(mtype_Ix));
        link = cursor.getString(mlink_Ix);

        ArrayList<String> names = new ArrayList<>();
        names.add(l1);
        names.add(l2);
        names.add(l3);
        names.add(l4);
        names.add(l5);

        database.close();
        return new PlaceCollectionClass(mainid, names, iconnumber, sortnumber, mtype, link);

    }

    public static PlaceCollectionClass getmaincollection_from_MainCollectionId(Context context, String mainId) {

        SQLiteDatabase database = open_place_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM place_database WHERE mainId= '" + mainId + "'", null);
        // Cursor cursor =database.rawQuery("SELECT * FROM lessons "WHERE age < 30 AND NAME = 'ali'",null);     yası 30dan buyuk olan
        //WHERE name LIKE '%a%'    içinde a gecen
        //WHERE name LIKE 'R%'     R ile baslayan

        // database.execSQL("UPDATE lessons SET age = 23 WHERE name = 'ali' ");

        int mainId_Ix = cursor.getColumnIndex("mainId");

        int l1_Ix = cursor.getColumnIndex("ml1");
        int l2_Ix = cursor.getColumnIndex("ml2");
        int l3_Ix = cursor.getColumnIndex("ml3");
        int l4_Ix = cursor.getColumnIndex("ml4");
        int l5_Ix = cursor.getColumnIndex("ml5");
        int micon_Ix = cursor.getColumnIndex("micon");
        int msort_Ix = cursor.getColumnIndex("msort");
        int mtype_Ix = cursor.getColumnIndex("mtype");
        int mlink_Ix = cursor.getColumnIndex("mlink");

        cursor.moveToFirst();

        String mainid, l1, l2, l3, l4, l5;
        int iconnumber, sortnumber, mtype;
        String link;

        mainid = cursor.getString(mainId_Ix);
        l1 = cursor.getString(l1_Ix);
        l2 = cursor.getString(l2_Ix);
        l3 = cursor.getString(l3_Ix);
        l4 = cursor.getString(l4_Ix);
        l5 = cursor.getString(l5_Ix);
        iconnumber = Integer.parseInt(cursor.getString(micon_Ix));
        sortnumber = Integer.parseInt(cursor.getString(msort_Ix));
        mtype = Integer.parseInt(cursor.getString(mtype_Ix));
        link = cursor.getString(mlink_Ix);

        ArrayList<String> names = new ArrayList<>();
        names.add(l1);
        names.add(l2);
        names.add(l3);
        names.add(l4);
        names.add(l5);

        database.close();
        return new PlaceCollectionClass(mainid, names, iconnumber, sortnumber, mtype, link);

    }

    public static ArrayList<PlaceCollectionClass> getAll_MainCollections(Context context) {

        ArrayList<PlaceCollectionClass> mcollectionlist = new ArrayList<>();

        SQLiteDatabase database = open_place_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM place_database ", null);
        // Cursor cursor =database.rawQuery("SELECT * FROM lessons "WHERE age < 30 AND NAME = 'ali'",null);     yası 30dan buyuk olan
        //WHERE name LIKE '%a%'    içinde a gecen
        //WHERE name LIKE 'R%'     R ile baslayan

        // database.execSQL("UPDATE lessons SET age = 23 WHERE name = 'ali' ");

        int mainId_Ix = cursor.getColumnIndex("mainId");

        HashSet<String> mainIdhashset = new HashSet<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            mainIdhashset.add(cursor.getString(mainId_Ix));
            cursor.moveToNext();
        }

        for (String mainid : mainIdhashset) {

            String selectQuery = "SELECT * FROM place_database WHERE mainId= '" + mainid + "'";
            cursor = database.rawQuery(selectQuery, null);

            cursor.moveToFirst();

            String placeid = cursor.getString(cursor.getColumnIndex("placeId"));

            mcollectionlist.add(getmaincollection_from_placeId(context, placeid));

        }


        Collections.sort(mcollectionlist, new Comparator<PlaceCollectionClass>() {
            @Override
            public int compare(PlaceCollectionClass o1, PlaceCollectionClass o2) {
                return o1.getSortnumber() - o2.getSortnumber();
            }
        });

        database.close();
        return mcollectionlist;

    }

    public static PlaceCollectionClass getSubcollection_from_placeId(Context context, String placeid) {

        SQLiteDatabase database = open_place_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM place_database WHERE placeId= '" + placeid + "'", null);
        // Cursor cursor =database.rawQuery("SELECT * FROM lessons "WHERE age < 30 AND NAME = 'ali'",null);     yası 30dan buyuk olan
        //WHERE name LIKE '%a%'    içinde a gecen
        //WHERE name LIKE 'R%'     R ile baslayan

        // database.execSQL("UPDATE lessons SET age = 23 WHERE name = 'ali' ");

        int subId_Ix = cursor.getColumnIndex("subId");
        int l1_Ix = cursor.getColumnIndex("sl1");
        int l2_Ix = cursor.getColumnIndex("sl2");
        int l3_Ix = cursor.getColumnIndex("sl3");
        int l4_Ix = cursor.getColumnIndex("sl4");
        int l5_Ix = cursor.getColumnIndex("sl5");
        int micon_Ix = cursor.getColumnIndex("sicon");
        int msort_Ix = cursor.getColumnIndex("ssort");

        cursor.moveToFirst();
        String subid, l1, l2, l3, l4, l5;
        int iconnumber, sortnumber;

        subid = cursor.getString(subId_Ix);
        l1 = cursor.getString(l1_Ix);
        l2 = cursor.getString(l2_Ix);
        l3 = cursor.getString(l3_Ix);
        l4 = cursor.getString(l4_Ix);
        l5 = cursor.getString(l5_Ix);
        iconnumber = Integer.parseInt(cursor.getString(micon_Ix));
        sortnumber = Integer.parseInt(cursor.getString(msort_Ix));

        ArrayList<String> names = new ArrayList<>();
        names.add(l1);
        names.add(l2);
        names.add(l3);
        names.add(l4);
        names.add(l5);

        database.close();
        return new PlaceCollectionClass(subid, names, iconnumber, sortnumber);

    }

    public static PlaceCollectionClass getSubcollection_from_SubCollecsitonId(Context context, String subId) {

        SQLiteDatabase database = open_place_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM place_database WHERE subId= '" + subId + "'", null);
        // Cursor cursor =database.rawQuery("SELECT * FROM lessons "WHERE age < 30 AND NAME = 'ali'",null);     yası 30dan buyuk olan
        //WHERE name LIKE '%a%'    içinde a gecen
        //WHERE name LIKE 'R%'     R ile baslayan

        // database.execSQL("UPDATE lessons SET age = 23 WHERE name = 'ali' ");

        int subId_Ix = cursor.getColumnIndex("subId");
        int l1_Ix = cursor.getColumnIndex("sl1");
        int l2_Ix = cursor.getColumnIndex("sl2");
        int l3_Ix = cursor.getColumnIndex("sl3");
        int l4_Ix = cursor.getColumnIndex("sl4");
        int l5_Ix = cursor.getColumnIndex("sl5");
        int micon_Ix = cursor.getColumnIndex("sicon");
        int msort_Ix = cursor.getColumnIndex("ssort");

        cursor.moveToFirst();
        String subid, l1, l2, l3, l4, l5;
        int iconnumber, sortnumber;

        subid = cursor.getString(subId_Ix);
        l1 = cursor.getString(l1_Ix);
        l2 = cursor.getString(l2_Ix);
        l3 = cursor.getString(l3_Ix);
        l4 = cursor.getString(l4_Ix);
        l5 = cursor.getString(l5_Ix);
        iconnumber = Integer.parseInt(cursor.getString(micon_Ix));
        sortnumber = Integer.parseInt(cursor.getString(msort_Ix));

        ArrayList<String> names = new ArrayList<>();
        names.add(l1);
        names.add(l2);
        names.add(l3);
        names.add(l4);
        names.add(l5);

        database.close();
        return new PlaceCollectionClass(subid, names, iconnumber, sortnumber);

    }

    public static ArrayList<PlaceCollectionClass> getAll_SubCollectionfrom_from_mainCollection(Context context, String maincollectionId) {

        ArrayList<PlaceCollectionClass> scollectionlist = new ArrayList<>();

        SQLiteDatabase database = open_place_database(context);

        String selectmainQuery = "SELECT * FROM place_database WHERE mainId= '" + maincollectionId + "'";
        Cursor cursor = database.rawQuery(selectmainQuery, null);

        HashSet<String> scollectionhashset = new HashSet<String>();

        int subId_Ix = cursor.getColumnIndex("subId");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            scollectionhashset.add(cursor.getString(subId_Ix));
            cursor.moveToNext();
        }


        for (String subid : scollectionhashset) {

            String selectsubQuery = "SELECT * FROM place_database WHERE subId= '" + subid + "'";
            cursor = database.rawQuery(selectsubQuery, null);

            cursor.moveToFirst();

            String placeid = cursor.getString(cursor.getColumnIndex("placeId"));

            scollectionlist.add(getSubcollection_from_placeId(context, placeid));

        }


        Collections.sort(scollectionlist, new Comparator<PlaceCollectionClass>() {
            @Override
            public int compare(PlaceCollectionClass o1, PlaceCollectionClass o2) {
                return o1.getSortnumber() - o2.getSortnumber();
            }
        });

        database.close();
        return scollectionlist;

    }

    public static PlaceClass getPlace(Context context, String placeid) {

        SQLiteDatabase database = open_place_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM place_database WHERE placeId= '" + placeid + "'", null);

        int placeid_Ix = cursor.getColumnIndex("placeId");

        int l1_Ix = cursor.getColumnIndex("pl1");
        int l2_Ix = cursor.getColumnIndex("pl2");
        int l3_Ix = cursor.getColumnIndex("pl3");
        int l4_Ix = cursor.getColumnIndex("pl4");
        int l5_Ix = cursor.getColumnIndex("pl5");

        int e1_Ix = cursor.getColumnIndex("pe1");
        int e2_Ix = cursor.getColumnIndex("pe2");
        int e3_Ix = cursor.getColumnIndex("pe3");
        int e4_Ix = cursor.getColumnIndex("pe4");
        int e5_Ix = cursor.getColumnIndex("pe5");

        int a1_Ix = cursor.getColumnIndex("pa1");
        int a2_Ix = cursor.getColumnIndex("pa2");
        int a3_Ix = cursor.getColumnIndex("pa3");
        int a4_Ix = cursor.getColumnIndex("pa4");
        int a5_Ix = cursor.getColumnIndex("pa5");

        int psort_Ix = cursor.getColumnIndex("psort"); //TO INT

        int latitude_Ix = cursor.getColumnIndex("latitude");
        int longitude_Ix = cursor.getColumnIndex("longitude");

        int phone_Ix = cursor.getColumnIndex("phone");
        int insta_Ix = cursor.getColumnIndex("insta");
        int face_Ix = cursor.getColumnIndex("face");
        int web_Ix = cursor.getColumnIndex("web");
        int mail_Ix = cursor.getColumnIndex("mail");
        int whatsapp_Ix = cursor.getColumnIndex("whatsapp");
        int buyticket_Ix = cursor.getColumnIndex("buyticket");

        int videoversion_Ix = cursor.getColumnIndex("videoversion");
        int mapversion_Ix = cursor.getColumnIndex("mapversion");
        int topphotocount_Ix = cursor.getColumnIndex("topphotocount");
        int pdowncount_Ix = cursor.getColumnIndex("pdowncount");
        int newendtime_Ix = cursor.getColumnIndex("newendtime");
        int isactive_Ix = cursor.getColumnIndex("isactive");

        cursor.moveToFirst();

        String id;
        String l1, l2, l3, l4, l5;
        String e1, e2, e3, e4, e5;
        String a1, a2, a3, a4, a5;
        int sortnumber;
        Double latitude, longitude;
        String videoversion, mapversion;
        String phone, whatsapp, insta, face, web, mail, buyticket;
        Boolean isactive;
        Long newendtime;

        id = cursor.getString(placeid_Ix);

        l1 = cursor.getString(l1_Ix);
        l2 = cursor.getString(l2_Ix);
        l3 = cursor.getString(l3_Ix);
        l4 = cursor.getString(l4_Ix);
        l5 = cursor.getString(l5_Ix);

        ArrayList<String> names = new ArrayList<>();
        names.add(l1);
        names.add(l2);
        names.add(l3);
        names.add(l4);
        names.add(l5);

        e1 = cursor.getString(e1_Ix);
        e2 = cursor.getString(e2_Ix);
        e3 = cursor.getString(e3_Ix);
        e4 = cursor.getString(e4_Ix);
        e5 = cursor.getString(e5_Ix);

        ArrayList<String> explains = new ArrayList<>();
        explains.add(e1);
        explains.add(e2);
        explains.add(e3);
        explains.add(e4);
        explains.add(e5);

        a1 = cursor.getString(a1_Ix);
        a2 = cursor.getString(a2_Ix);
        a3 = cursor.getString(a3_Ix);
        a4 = cursor.getString(a4_Ix);
        a5 = cursor.getString(a5_Ix);

        ArrayList<String> adresses = new ArrayList<>();
        adresses.add(a1);
        adresses.add(a2);
        adresses.add(a3);
        adresses.add(a4);
        adresses.add(a5);

        sortnumber = Integer.valueOf(cursor.getString(psort_Ix));

        latitude = Double.valueOf(cursor.getString(latitude_Ix));
        longitude = Double.valueOf(cursor.getString(longitude_Ix));
        LocationClass location = new LocationClass(latitude, longitude);

        phone = cursor.getString(phone_Ix);
        insta = cursor.getString(insta_Ix);
        whatsapp = cursor.getString(whatsapp_Ix);
        face = cursor.getString(face_Ix);
        web = cursor.getString(web_Ix);
        mail = cursor.getString(mail_Ix);
        buyticket = cursor.getString(buyticket_Ix);

        Contactinfo contactinfo = new Contactinfo();
        contactinfo.setPhonenumber(phone);
        contactinfo.setInstagram(insta);
        contactinfo.setWhatsapp(whatsapp);
        contactinfo.setFacebook(face);
        contactinfo.setWebsite(web);
        contactinfo.setMailadress(mail);
        contactinfo.setBuyticket(buyticket);

        int topcount = Integer.valueOf(cursor.getString(topphotocount_Ix));

        ArrayList<String> topphotos = new ArrayList<>();
        ArrayList<String> downphotos = new ArrayList<>();


        for (int i = 0; i < topcount; i++) {

            topphotos.add(cursor.getString(cursor.getColumnIndex("ptop" + i)));

        }

        int downcount = Integer.valueOf(cursor.getString(pdowncount_Ix));

        for (int i = 0; i < downcount; i++) {

            downphotos.add(cursor.getString(cursor.getColumnIndex("pdown" + i)));

        }

        ArrayList<Integer> opentimes = new ArrayList<>();
        ArrayList<Integer> closetimes = new ArrayList<>();


        for (int i = 0; i < 7; i++) {

            opentimes.add(Integer.valueOf(cursor.getString(cursor.getColumnIndex("popen" + i))));
            closetimes.add(Integer.valueOf(cursor.getString(cursor.getColumnIndex("pclse" + i))));


        }

        newendtime = Long.valueOf(cursor.getString(newendtime_Ix));

        videoversion = cursor.getString(videoversion_Ix);
        mapversion = cursor.getString(mapversion_Ix);


        if (Integer.valueOf(cursor.getString(isactive_Ix)) == PlaceClass.ACTIVE_PLACE) {

            isactive = true;
        } else isactive = false;


        database.close();
        return new PlaceClass(id, sortnumber, names, explains, adresses, contactinfo, location,
                opentimes, closetimes, topphotos, downphotos, videoversion, mapversion, newendtime, isactive);


    }

    public static ArrayList<PlaceClass> getAllPlaces_from_subcollection(Context context, String subcollectionId) {

        ArrayList<PlaceClass> placelist = new ArrayList<>();

        SQLiteDatabase database = open_place_database(context);
        String selectQuery = "SELECT * FROM place_database WHERE subId= '" + subcollectionId + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);

        int placeid_Ix = cursor.getColumnIndex("placeId");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String placeid = cursor.getString(cursor.getColumnIndex("placeId"));

            placelist.add(getPlace(context, placeid));

            cursor.moveToNext();
        }


        database.close();
        return placelist;
    }

    public static EventClass get_Event(Context context, String event_id) {

        final SQLiteDatabase database = open_events_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM events_database WHERE eventId= '" + event_id + "'", null);

        int event_Id_Ix = cursor.getColumnIndex("eventId");

        int l1_Ix = cursor.getColumnIndex("pl1");
        int l2_Ix = cursor.getColumnIndex("pl2");
        int l3_Ix = cursor.getColumnIndex("pl3");
        int l4_Ix = cursor.getColumnIndex("pl4");
        int l5_Ix = cursor.getColumnIndex("pl5");

        int e1_Ix = cursor.getColumnIndex("pe1");
        int e2_Ix = cursor.getColumnIndex("pe2");
        int e3_Ix = cursor.getColumnIndex("pe3");
        int e4_Ix = cursor.getColumnIndex("pe4");
        int e5_Ix = cursor.getColumnIndex("pe5");

        int a1_Ix = cursor.getColumnIndex("pa1");
        int a2_Ix = cursor.getColumnIndex("pa2");
        int a3_Ix = cursor.getColumnIndex("pa3");
        int a4_Ix = cursor.getColumnIndex("pa4");
        int a5_Ix = cursor.getColumnIndex("pa5");

        int latitude_Ix = cursor.getColumnIndex("latitude");
        int longitude_Ix = cursor.getColumnIndex("longitude");

        int phone_Ix = cursor.getColumnIndex("phone");
        int insta_Ix = cursor.getColumnIndex("insta");
        int face_Ix = cursor.getColumnIndex("face");
        int web_Ix = cursor.getColumnIndex("web");
        int mail_Ix = cursor.getColumnIndex("mail");
        int whatsapp_Ix = cursor.getColumnIndex("whatsapp");
        int buyticket_Ix = cursor.getColumnIndex("buyticket");

        int videoversion_Ix = cursor.getColumnIndex("videoversion");
        int mapversion_Ix = cursor.getColumnIndex("mapversion");
        int topphotocount_Ix = cursor.getColumnIndex("topphotocount");
        int pdowncount_Ix = cursor.getColumnIndex("pdowncount");

        int pupl1_Ix = cursor.getColumnIndex("pupl1");
        int pupl2_Ix = cursor.getColumnIndex("pupl2");
        int pupl3_Ix = cursor.getColumnIndex("pupl3");
        int pupl4_Ix = cursor.getColumnIndex("pupl4");
        int pupl5_Ix = cursor.getColumnIndex("pupl5");

        int puml1_Ix = cursor.getColumnIndex("puml1");
        int puml2_Ix = cursor.getColumnIndex("puml2");
        int puml3_Ix = cursor.getColumnIndex("puml3");
        int puml4_Ix = cursor.getColumnIndex("puml4");
        int puml5_Ix = cursor.getColumnIndex("puml5");

        int linledplace_Ix = cursor.getColumnIndex("plpId");
        int linkedmanagement_Ix = cursor.getColumnIndex("plmId");

        int ptimecount_Ix = cursor.getColumnIndex("ptimecount");

        cursor.moveToFirst();

        String id;
        String l1, l2, l3, l4, l5;
        String e1, e2, e3, e4, e5;
        String a1, a2, a3, a4, a5;
        String pupl1, pupl2, pupl3, pupl4, pupl5;
        String puml1, puml2, puml3, puml4, puml5;
        String linkedplaceId, linkedmanagementId;

        Double latitude, longitude;
        String mapversion, videoversion;
        String phone, whatsapp, insta, face, web, mail, buyticket;

        id = cursor.getString(event_Id_Ix);

        l1 = cursor.getString(l1_Ix);
        l2 = cursor.getString(l2_Ix);
        l3 = cursor.getString(l3_Ix);
        l4 = cursor.getString(l4_Ix);
        l5 = cursor.getString(l5_Ix);

        ArrayList<String> names = new ArrayList<>();
        names.add(l1);
        names.add(l2);
        names.add(l3);
        names.add(l4);
        names.add(l5);

        e1 = cursor.getString(e1_Ix);
        e2 = cursor.getString(e2_Ix);
        e3 = cursor.getString(e3_Ix);
        e4 = cursor.getString(e4_Ix);
        e5 = cursor.getString(e5_Ix);

        ArrayList<String> explains = new ArrayList<>();
        explains.add(e1);
        explains.add(e2);
        explains.add(e3);
        explains.add(e4);
        explains.add(e5);

        pupl1 = cursor.getString(pupl1_Ix);
        pupl2 = cursor.getString(pupl2_Ix);
        pupl3 = cursor.getString(pupl3_Ix);
        pupl4 = cursor.getString(pupl4_Ix);
        pupl5 = cursor.getString(pupl5_Ix);

        ArrayList<String> unlinledplacenames = new ArrayList<>();
        unlinledplacenames.add(pupl1);
        unlinledplacenames.add(pupl2);
        unlinledplacenames.add(pupl3);
        unlinledplacenames.add(pupl4);
        unlinledplacenames.add(pupl5);

        puml1 = cursor.getString(puml1_Ix);
        puml2 = cursor.getString(puml2_Ix);
        puml3 = cursor.getString(puml3_Ix);
        puml4 = cursor.getString(puml4_Ix);
        puml5 = cursor.getString(puml5_Ix);

        ArrayList<String> unlinledmanagementnames = new ArrayList<>();
        unlinledmanagementnames.add(puml1);
        unlinledmanagementnames.add(puml2);
        unlinledmanagementnames.add(puml3);
        unlinledmanagementnames.add(puml4);
        unlinledmanagementnames.add(puml5);

        linkedmanagementId = cursor.getString(linkedmanagement_Ix);
        linkedplaceId = cursor.getString(linledplace_Ix);

        a1 = cursor.getString(a1_Ix);
        a2 = cursor.getString(a2_Ix);
        a3 = cursor.getString(a3_Ix);
        a4 = cursor.getString(a4_Ix);
        a5 = cursor.getString(a5_Ix);

        ArrayList<String> adresses = new ArrayList<>();
        adresses.add(a1);
        adresses.add(a2);
        adresses.add(a3);
        adresses.add(a4);
        adresses.add(a5);

        latitude = Double.valueOf(cursor.getString(latitude_Ix));
        longitude = Double.valueOf(cursor.getString(longitude_Ix));
        LocationClass location = new LocationClass(latitude, longitude);

        phone = cursor.getString(phone_Ix);
        insta = cursor.getString(insta_Ix);
        whatsapp = cursor.getString(whatsapp_Ix);
        face = cursor.getString(face_Ix);
        web = cursor.getString(web_Ix);
        mail = cursor.getString(mail_Ix);
        buyticket = cursor.getString(buyticket_Ix);

        Contactinfo contactinfo = new Contactinfo();
        contactinfo.setPhonenumber(phone);
        contactinfo.setInstagram(insta);
        contactinfo.setWhatsapp(whatsapp);
        contactinfo.setFacebook(face);
        contactinfo.setWebsite(web);
        contactinfo.setMailadress(mail);
        contactinfo.setBuyticket(buyticket);

        mapversion = cursor.getString(mapversion_Ix);
        videoversion = cursor.getString(videoversion_Ix);

        ArrayList<EventTimes> eventTimes = new ArrayList<>();
        int timescount = Integer.valueOf(cursor.getString(ptimecount_Ix));

        for (int i = 0; i < timescount; i++) {

            eventTimes.add(new EventTimes(Long.valueOf(cursor.getString(cursor.getColumnIndex("ptstart" + i))),
                    Long.valueOf(cursor.getString(cursor.getColumnIndex("ptend" + i)))));

        }

        int topphotoscount = Integer.valueOf(cursor.getString(topphotocount_Ix));
        ArrayList<String> topphotos = new ArrayList<>();
        for (int i = 0; i < topphotoscount; i++) {
            topphotos.add(cursor.getString(cursor.getColumnIndex("ptop" + i)));

        }

        int downphotoscount = Integer.valueOf(cursor.getString(pdowncount_Ix));
        ArrayList downphotos = new ArrayList();
        for (int i = 0; i < downphotoscount; i++) {
            downphotos.add(cursor.getString(cursor.getColumnIndex("pdown" + i)));

        }

        EventClass event = new EventClass(id, names, explains, adresses, unlinledplacenames, unlinledmanagementnames,
                contactinfo, location, topphotos, downphotos, videoversion, mapversion, eventTimes, linkedplaceId, linkedmanagementId);

        database.close();
        return event;


    }

    public static ArrayList<EventClass> getAll_Events_from_database(Context context) {

        ArrayList<EventClass> eventsList = new ArrayList<>();

        final SQLiteDatabase database = open_events_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM events_database ", null);

        int event_Id_Ix = cursor.getColumnIndex("eventId");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            eventsList.add(get_Event(context, cursor.getString(event_Id_Ix)));

            cursor.moveToNext();
        }

        database.close();
        return eventsList;

    }

    public static ArrayList<String> getEventIdlist_from_placeId(Context context, String placeId) {

        final SQLiteDatabase database = open_events_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM events_database WHERE plpId = '" + placeId + "'", null);

        int event_Id_Ix = cursor.getColumnIndex("eventId");
        int linledplace_Ix = cursor.getColumnIndex("plpId");
        int linkedmanagement_Ix = cursor.getColumnIndex("plmId");

        cursor.moveToFirst();
        HashSet<String> linkedplaceevents_Idlist = new HashSet<>();

        if (cursor.getCount() > 0) { //LINKED PLACE EVENT VAR ISE

            while (!cursor.isAfterLast()) {

                if (cursor.getString(linledplace_Ix).equals(placeId)) {

                    linkedplaceevents_Idlist.add(cursor.getString(event_Id_Ix));

                }
                cursor.moveToNext();
            }

        }

        cursor = database.rawQuery("SELECT * FROM events_database WHERE plmId = '" + placeId + "'", null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) { //LINKED PLACE EVENT VAR ISE

            while (!cursor.isAfterLast()) {

                if (cursor.getString(linkedmanagement_Ix).equals(placeId)) {

                    linkedplaceevents_Idlist.add(cursor.getString(event_Id_Ix));

                }

                cursor.moveToNext();
            }

        }

        database.close();

        ArrayList<String> idlist = new ArrayList<>();

        for (String id : linkedplaceevents_Idlist) {
            idlist.add(id);
        }

        return idlist;

    }

    public static ArrayList<String> get_search_history(Context context) {

        ArrayList<String> history_list = new ArrayList<>();

        final SQLiteDatabase database = open_search_history_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM search_history_database ", null);

        int history_Ix = cursor.getColumnIndex("history");

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {

                history_list.add(cursor.getString(history_Ix));
                cursor.moveToNext();

            }
        }

        database.close();
        return history_list;


    }

    public static void add_item_history(Context context, String s) {

        final SQLiteDatabase database = open_search_history_database(context);

        if (!get_search_history(context).contains(s)) {

            String sqlstring = get_history_database_insertString(context);
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);

            sqLiteStatement.bindString(1, s);
            sqLiteStatement.execute();

        }

        database.close();

    }

    public static void clearhistory(Context context) {

        final SQLiteDatabase database = open_search_history_database(context);
        database.execSQL("DELETE FROM search_history_database");
        database.close();

    }

    public static ArrayList<String> get_places_from_contains(Context context, String text) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        int language= sharedPreferences.getInt("language", SelectLanguage.ENGLISH)+1;

        HashSet<String> hashSet_idlist = new HashSet<>();

        final SQLiteDatabase database = open_place_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM place_database WHERE pl"+language+" like '%" + text + "%'", null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {

                if (Integer.valueOf(cursor.getString(cursor.getColumnIndex("mtype"))) < 2){

                    hashSet_idlist.add(cursor.getString(cursor.getColumnIndex("placeId")));

                }

                cursor.moveToNext();

            }
        }


        ArrayList<String> idlist = new ArrayList<>();
        for (String s : hashSet_idlist) {
            idlist.add(s);

        }


        return idlist;

    }

    public static ArrayList<String> get_events_from_contains(Context context, String text) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        int language= sharedPreferences.getInt("language", SelectLanguage.ENGLISH)+1;

            HashSet<String> hashSet_idlist = new HashSet<>();

        final SQLiteDatabase database = open_events_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM events_database WHERE pl"+language+" like '%" + text + "%'", null);

        int eventId_Ix = cursor.getColumnIndex("eventId");

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {

                hashSet_idlist.add(cursor.getString(eventId_Ix));

                cursor.moveToNext();
            }

        }


        ArrayList<String> idlist = new ArrayList<>();
        for (String s : hashSet_idlist) {
            idlist.add(s);

        }

        return idlist;
    }

    public static ArrayList<PlaceClass> get_favlist(Context context) {

        ArrayList<PlaceClass> favlist_list = new ArrayList<>();
        final SQLiteDatabase database=open_favlist_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM favlist_database ", null);

        int fav_Id_Ix = cursor.getColumnIndex("fav_Id");

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {

                PlaceClass favplace=getPlace(context,cursor.getString(fav_Id_Ix));
                favplace.setSortnumber(Integer.valueOf(cursor.getString(cursor.getColumnIndex("favsort"))));

                favlist_list.add(favplace);

                cursor.moveToNext();

            }
        }

        database.close();
        return favlist_list;

    }

    public static void add_item_to_favlist (Context context, String id) {

        final SQLiteDatabase database = open_favlist_database(context);

        Cursor cursor = database.rawQuery("SELECT * FROM favlist_database ", null);
        int favsort_Ix = cursor.getColumnIndex("favsort");
        cursor.moveToFirst();

        int sort=0;

        while (!cursor.isAfterLast()) {

            int databasesort=Integer.valueOf(cursor.getString(favsort_Ix));

            if (databasesort < sort)   sort=databasesort;

            cursor.moveToNext();
        }

        sort--;


        String sqlstring = get_favlist_database_database_insertString(context);
        SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);

        sqLiteStatement.bindString(1, id);
        sqLiteStatement.bindString(2, String.valueOf(sort));
        sqLiteStatement.execute();


        database.close();
    }

    public static void delete_item_from_favlist (Context context, String id) {

        final SQLiteDatabase database = open_favlist_database(context);

        try{

            database.execSQL("DELETE FROM favlist_database WHERE fav_Id = '" + id + "' ");

            try {

                //FAV
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (Exception e){
            System.out.println("OLMADI AMK");
            e.printStackTrace();
        }


        database.close();
    }

    public static Boolean isfav(Context context,String id){

        final SQLiteDatabase database = open_favlist_database(context);
        Cursor cursor = database.rawQuery("SELECT * FROM favlist_database ", null);

        cursor.moveToFirst();

        if (cursor.getCount()>0){

            while (!cursor.isAfterLast()){

                if (cursor.getString(cursor.getColumnIndex("fav_Id")).equals(id)){
                    database.close();
                    return true;
                }

                cursor.moveToNext();
            }

        }
        database.close();
        return false;


    }

    public static void clearfavlist(Context context) {

        final SQLiteDatabase database = open_favlist_database(context);
        database.execSQL("DELETE FROM favlist_database");
        database.close();

    }



}
