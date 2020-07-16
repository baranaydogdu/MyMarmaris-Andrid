package com.baranaydogdu.mymarmaris.Classes;

import java.util.ArrayList;

public class PlaceCollectionClass {

    public String id = "";
    public ArrayList<String> name = new ArrayList<>();
    public String icon = "";
    public int sortnumber = 0;
    public int type = 0;
    public String link = "";

    public static final int COLLECTION_WITH_SUB=0;
    public static final int COLLECTION_NON_SUB=1;
    public static final int OUTSIDE_LINKKED_COLLECTION=2;
    public static final int INSIDE_LINKKED_COLLECTION=3;
    public static final int ACTIVITIES=4;
    public static final int PHARMACY=5;
    public static final int SUBCOLLECTION=6;

    public PlaceCollectionClass() {

    }

    public PlaceCollectionClass(String id, ArrayList<String> name, String icon, int sortnumber, int type, String link) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.sortnumber = sortnumber;
        this.type = type;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
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
