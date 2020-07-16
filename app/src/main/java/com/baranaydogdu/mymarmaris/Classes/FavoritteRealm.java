package com.baranaydogdu.mymarmaris.Classes;

import io.realm.Realm;
import io.realm.RealmObject;

public class FavoritteRealm extends RealmObject {

    public String favid = "";

    public FavoritteRealm() {
    }

    public FavoritteRealm(String favid) {
        this.favid = favid;
    }

    public static Boolean addOrDeleteFav(String favid) {

        Realm realm = Realm.getDefaultInstance();
        FavoritteRealm isfav = realm.where(FavoritteRealm.class).equalTo("favid", favid).findFirst();
        Boolean isAdded;
        realm.beginTransaction();

        if (isfav != null) {
            isfav.deleteFromRealm();
            isAdded = false;
        } else {
            realm.copyToRealm(new FavoritteRealm(favid));
            isAdded = true;
        }
        realm.commitTransaction();
        return isAdded;
    }

    public static Boolean isFav(String favid){
        Realm realm = Realm.getDefaultInstance();
        FavoritteRealm isfav = realm.where(FavoritteRealm.class).equalTo("favid", favid).findFirst();

        if (isfav == null) return false;
        else return true;

    }



}
