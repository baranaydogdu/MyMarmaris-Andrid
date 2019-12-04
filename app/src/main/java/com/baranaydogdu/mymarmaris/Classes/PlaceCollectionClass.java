package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceCollectionClass implements Serializable {

    String id;
    ArrayList<String> name;
    int iconnumber,sortnumber;
    int type;
    String link;

    public static final int COLLECTION_WITH_SUB=0;
    public static final int COLLECTION_NON_SUB=1;
    public static final int OUTSIDE_LINKKED_COLLECTION=2;
    public static final int INSIDE_LINKKED_COLLECTION=3;
    public static final int ACTIVITIES=4;
    public static final int PHARMACY=5;
    public static final int SUBCOLLECTION=6;

    public PlaceCollectionClass() {
    }

    public PlaceCollectionClass(String id, ArrayList<String> name, int iconnumber, int sortnumber, int type, String link) {
        this.id = id;
        this.name = name;
        this.iconnumber = iconnumber;
        this.sortnumber = sortnumber;
        this.type = type;
        this.link = link;
        if (link==null) this.link="";
    }

    public PlaceCollectionClass(String id, ArrayList<String> name, int iconnumber, int sortnumber, int type) {
        this.id = id;
        this.name = name;
        this.iconnumber = iconnumber;
        this.sortnumber = sortnumber;
        this.type = type;
        this.link="";
    }

    public PlaceCollectionClass(String id, ArrayList<String> name, int iconnumber, int sortnumber) {
        this.id = id;
        this.name = name;
        this.iconnumber = iconnumber;
        this.sortnumber = sortnumber;
        this.type = SUBCOLLECTION;
        this.link="";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public void setIconnumber(int iconnumber) {
        this.iconnumber = iconnumber;
    }

    public void setSortnumber(int sortnumber) {
        this.sortnumber = sortnumber;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLink(String link) {
        if (link==null) link="";
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public int getIconnumber() {
        return iconnumber;
    }

    public int getSortnumber() {
        return sortnumber;
    }

    public int getType() {
        return type;
    }

    public String getLink() {
        if (link==null) link="";
        return link;
    }
}
