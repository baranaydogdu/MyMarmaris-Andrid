package com.baranaydogdu.mymarmaris.Classes;

import io.realm.Realm;
import io.realm.RealmObject;

public class HistoryRealm extends RealmObject {

    public String historyText = "";

    public HistoryRealm() {
    }

    public HistoryRealm(String historyText) {
        this.historyText = historyText;
    }

    public static void addtoHistory(String historyText) {

        Realm realm = Realm.getDefaultInstance();
        HistoryRealm historyRealm = realm.where(HistoryRealm.class).equalTo("historyText", historyText).findFirst();

        realm.beginTransaction();

        if (historyRealm == null) {
            realm.copyToRealm(new HistoryRealm(historyText));
        }
        realm.commitTransaction();

    }

}
