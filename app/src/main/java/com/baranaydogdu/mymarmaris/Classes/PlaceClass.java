package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceClass {

    public String id = "";
    int sortnumber = 100000;
    public Boolean isactive = true;

    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<String> explain = new ArrayList<>();
    public ArrayList<String> adres = new ArrayList<>();

    public Contactinfo contactinfo = new Contactinfo();
    public LocationClass location = new LocationClass();

    public ArrayList<Integer> opentime = new ArrayList<>();
    public ArrayList<Integer> closetime = new ArrayList<>();

    public String videoversion = "";
    public String mapphotoversion = "";

    public ArrayList<String> topphotos= new ArrayList<>();
    public ArrayList<String> downphotos = new ArrayList<>();

    public Long new_end_time = 0l;

    public float distance = 0f;

    public PlaceClass() { }

    public PlaceClass(String id, int sortnumber, ArrayList<String> name, ArrayList<String> explain, ArrayList<String> adres, Contactinfo contactinfo, LocationClass location, ArrayList<Integer> opentime, ArrayList<Integer> closetime, ArrayList<String> topphotos, ArrayList<String> downphotos, String videoversion, String mapphotoversion, Long new_end_time, Boolean isactive) {
        this.id = id;
        this.sortnumber = sortnumber;
        this.name = name;
        this.explain = explain;
        this.adres = adres;
        this.contactinfo = contactinfo;
        this.location = location;
        this.opentime = opentime;
        this.closetime = closetime;
        this.topphotos = topphotos;
        this.downphotos = downphotos;
        this.videoversion = videoversion;
        this.mapphotoversion = mapphotoversion;
        this.new_end_time = new_end_time;
        this.isactive = isactive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSortnumber() {
        return sortnumber;
    }

    public void setSortnumber(int sortnumber) {
        this.sortnumber = sortnumber;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getExplain() {
        return explain;
    }

    public void setExplain(ArrayList<String> explain) {
        this.explain = explain;
    }

    public ArrayList<String> getAdres() {
        return adres;
    }

    public void setAdres(ArrayList<String> adres) {
        this.adres = adres;
    }

    public Contactinfo getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(Contactinfo contactinfo) {
        this.contactinfo = contactinfo;
    }

    public LocationClass getLocation() {
        return location;
    }

    public void setLocation(LocationClass location) {
        this.location = location;
    }

    public ArrayList<Integer> getOpentime() {
        return opentime;
    }

    public void setOpentime(ArrayList<Integer> opentime) {
        this.opentime = opentime;
    }

    public ArrayList<Integer> getClosetime() {
        return closetime;
    }

    public void setClosetime(ArrayList<Integer> closetime) {
        this.closetime = closetime;
    }

    public ArrayList<String> getTopphotos() {
        return topphotos;
    }

    public void setTopphotos(ArrayList<String> topphotos) {
        this.topphotos = topphotos;
    }

    public ArrayList<String> getDownphotos() {
        return downphotos;
    }

    public void setDownphotos(ArrayList<String> downphotos) {
        this.downphotos = downphotos;
    }

    public String getVideoversion() {
        return videoversion;
    }

    public void setVideoversion(String videoversion) {
        this.videoversion = videoversion;
    }

    public String getMapphotoversion() {
        return mapphotoversion;
    }

    public void setMapphotoversion(String mapphotoversion) {
        this.mapphotoversion = mapphotoversion;
    }

    public Long getNew_end_time() {
        return new_end_time;
    }

    public void setNew_end_time(Long new_end_time) {
        this.new_end_time = new_end_time;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }
}
