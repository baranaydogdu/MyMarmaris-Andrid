package com.baranaydogdu.mymarmaris;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmList;

public class LanguagePack {

    public String[] favlisttext={"Favorite list","Favori listesi","Список избранного","Λίστα αγαπημένων","Liebling"};
    public String[] opentext = {"Open", "Açık", "открытый", "ανοιχτό", "Öffnen"};
    public String[] closetext = {"Close", "Kapalı", "близко", "κλειστό", "Geschlossen"};
    public String[] neww = {"New", "Yeni", "новый", "νέος", "Neu"};
    public String[] adresses = {"Address", "Adres", "адрес", "διεύθυνση", "Address"};
    public String[] seetheroutes = {"Get Directions", "Yol tarifi al", "Проложить маршрут", "Λήψη οδηγιών", "Anweisungen bekommen"};
    public String[] phones = {"Telephone", "Telefon", "телефон", "τηλέφωνο", "Telefon"};
    public String[] whatsapps = {"Whatsapp", "Whatsapp", "Whatsapp", "Whatsapp", "Whatsapp"};
    public String[] webs = {"WEB Page", "İnternet Sitesi", "WEB страница", "Ιστοσελίδα", "Webseite"};
    public String[] faces = {"Facebook", "Facebook", "Facebook", "Facebook", "Facebook"};
    public String[] instas = {"Instagram", "Instagram", "Instagram", "Instagram", "Instagram"};
    public String[] mails = {"E-mail", "E-posta", "Электронная почта", "E-mail", "Email"};
    public String[] buytickets = {"Buy Ticket", "Bilet Al", "Купить билет", "Αγοράστε εισιτήριο", "Kauf ein Ticket"};
    public String[] nextdaystext = {"Planned Dates", "Planlı Tarihler", "Запланированные даты", "Προγραμματισμένες ημερομηνίες", "Geplante Termine"};
    public String[] marmarisrehberi = {"Marmaris Guide", "Marmaris Rehberi", "Путеводитель по Мармарису", "Οδηγός Μαρμαρίς", "Marmaris-Führer"};
    public String[] digerapps = {"Other Applications", "Diğer Uygulamalarımız", "Другие приложения", "Άλλες εφαρμογές", "Andere Anwendungen"};
    public String[] notificationsettingstext={"Notification Settings","Bildirim Ayarları","Настройки уведомлений","Ρυθμίσεις ειδοποιήσεων","Benachrichtigungseinstellungen"};
    public String[] allhistorydeleted={"All History Deleted","Arama Geçmişi Silindi","Вся история удалена","Όλη η ιστορία διαγράφηκε","Suchverlauf gelöscht"};
    public String[] allfavlist_deleted={"Favorite list Deleted","Favori Listesi Silindi","Список избранных удален","Αγαπημένη λίστα διαγράφηκε","Favorit gelöscht"};


    String[] select_language={"Select language","Dil seç","Выберите язык","Επιλέξτε γλώσσα","Sprache auswählen"};
    String[] clearhistory={"Clean Search History","Arama geçmişini temizle","Очистить историю поиска","Καθαρίστε το Ιστορικό αναζήτησης","Suchverlauf löschen"};
    String[] clearfavlisttext={"Clean Favorite List","Favori Listesini Temizle","Чистый список избранного","Καθαρισμός λίστας αγαπημένων","Favorit löschen"};
    String[] autovideotext={"Auto Play Video","Videoyu Otomatik Oynat","Автоматическое воспроизведение видео","Αυτόματη αναπαραγωγή βίντεο","Video automatisch abspielen"};

    String[] autoimagetext={"Auto Slide Images","Fotoğrafları otomatik oynat","Авто слайд изображения","Αυτόματες εικόνες διαφάνειας","Bilder automatisch schieben",};
    String[] settings={"Settings","Ayarlar","настройки","ρυθμίσεις","die Einstellungen"};



    public String[] searctext={"Search","Ara","Поиск","Αναζήτηση","Suche",};
    public String[] placestext={"Places","İşletmeler","места","Μέρη","Orte"};

    public String[] eventstext={"Events","Etkinlikler","события","δραστηριότητες","Aktivitäten"};

    public String[] open ={"Open","Açık","открытый","ανοιχτό","Öffnen"};
    public String[] close ={"Close","Kapalı", "близко","κλειστό","Geschlossen"};
    public String[] worktimes = {"Work Times", "Çalışma Saatleri", "Время работы", "Ώρες εργασίας", "Arbeitszeit"};

    public String[] actualevents={"Current Events","Güncel Etkinlikler","Текущие события","Τρέχοντα γεγονότα","Aktuelle Ereignisse"};

    public String[][] mount = {
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

    public String[][] days = {

            {"Sunday", "Pazar", "воскресенье", "Κυριακή", "Sonntag"},
            {"Monday", "Pazartesi", "понедельник", "Δευτέρα", "Montag"},
            {"Tuesday", "Salı", "вторник", "Τρίτι", "Dienstag"},
            {"Wednesday", "Çarşamba", "среда", "Τετάρτη", "Mittwoch"},
            {"Thursday", "Perşembe", "четверг", "Πέμπτη", "Donnerstag"},
            {"Friday", "Cuma", "пятница", "Παρασκευή", "Freitag"},
            {"Saturday", "Cumartesi", "суббота", "Σάββατο", "Samstag"}

    };

    public String[] monday     ={"Monday" ,  "Pazartesi" ,"понедельник" ,"Δευτέρα" ,    "Montag"   };
    public String[] Tuesday    ={"Tuesday",  "Salı"  ,    "вторник" ,    "Τρίτι",       "Dienstag"  };
    public String[] Wednesday  ={"Wednesday","Çarşamba"  ,"среда" ,      "Τετάρτη"     ,"Mittwoch"     };
    public String[] Thursday   ={"Thursday" ,"Perşembe"  ,"четверг" ,    "Πέμπτη"      ,"Donnerstag"    };
    public String[] Friday     ={"Friday"   ,"Cuma"  ,    "пятница" ,    "Παρασκευή"   ,"Freitag"      };
    public String[] Saturday   ={"Saturday" ,"Cumartesi", "суббота" ,    "Σάββατο"     ,"Samstag" }    ;
    public String[] Sunday     ={"Sunday"   ,"Pazar" ,    "воскресенье" ,"Κυριακή"     ,"Sonntag"     };

    public LanguagePack() {

    }




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
        return new LanguagePack().mount[ay][sharedPreferences.getInt("language", SelectLanguage.ENGLISH)];
    }

    public static String getlanguagesgun(Context context, int dayofthewek) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        dayofthewek = dayofthewek - 1;
        return new LanguagePack().days[dayofthewek][sharedPreferences.getInt("language", SelectLanguage.ENGLISH)];

    }

    public static String getLanguage(ArrayList<String> array, int lan){
        if (!array.get(lan).equals("")) return array.get(lan);
        else return array.get(0);
    }

    public static String getLanguage(RealmList<String> array, int lan){
        if (!array.get(lan).equals("")) return array.get(lan);
        else return array.get(0);
    }


}
