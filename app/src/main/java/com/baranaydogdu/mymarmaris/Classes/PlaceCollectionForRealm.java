package com.baranaydogdu.mymarmaris.Classes;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlaceCollectionForRealm extends RealmObject {

    //@PrimaryKey
    public String id = "";

    public String masterid = "";//MAIN CLASS
    public RealmList<String> name = new RealmList<>();
    public String icon = "";
    public int sortnumber = 0;
    public int type = 0;
    public String link = "";


    public PlaceCollectionForRealm() {
    }

    public PlaceCollectionForRealm(String masterid, PlaceCollectionClass collection) {

        this.id = collection.id;
        this.masterid = masterid;
        for (String samp : collection.name) {
            this.name.add(samp);
        }

        this.icon = collection.icon;
        this.sortnumber = collection.sortnumber;
        this.type = collection.type;
        this.link = collection.link;

    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

    public RealmList<String> getName() {
        return name;
    }

    public void setName(RealmList<String> name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSortnumber() {
        return sortnumber;
    }

    public void setSortnumber(int sortnumber) {
        this.sortnumber = sortnumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
