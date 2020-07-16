package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EventClass {

    public String id = "";      //GENERATED
    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<String> explain = new ArrayList<>();
    public ArrayList<String> adres = new ArrayList<>();

    public Contactinfo contactinfo = new Contactinfo();
    public LocationClass location = new LocationClass();

    public String linked_place = "";
    public String linked_management = "";

    public ArrayList<String> unlinked_place = new ArrayList<>();
    public ArrayList<String> unlinked_management = new ArrayList<>();

    public String videoversion = "";
    public String mapphotoversion = "";


    public ArrayList<String> topphotos = new ArrayList<>();
    public ArrayList<String> downphotos = new ArrayList<>();

    public ArrayList<EventTimes> times = new ArrayList<>();

    public EventClass() {

    }


    public EventClass(String id, ArrayList<String> name, ArrayList<String> explain, ArrayList<String> adres, ArrayList<String> unlinked_place, ArrayList<String> unlinked_management, Contactinfo contactinfo, LocationClass location, ArrayList<String> topphotos, ArrayList<String> downphotos, String videoversion, String mapphotoversion, ArrayList<EventTimes> times, String linked_place, String linked_management) {
        this.id = id;
        this.name = name;
        this.explain = explain;
        this.adres = adres;
        this.unlinked_place = unlinked_place;
        this.unlinked_management = unlinked_management;
        this.contactinfo = contactinfo;
        this.location = location;
        this.topphotos = topphotos;
        this.downphotos = downphotos;
        this.videoversion = videoversion;
        this.mapphotoversion = mapphotoversion;
        this.times = times;
        this.linked_place = linked_place;
        this.linked_management = linked_management;
    }

    public ArrayList<EventTimes> seethe_nextdays() {

        ArrayList<EventTimes> nextTimes = new ArrayList<>();
        for (EventTimes singleTime : times) {

            if (singleTime.end_time > new Date().getTime()) {

                nextTimes.add(singleTime);
            }
        }
        return nextTimes;
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

    public ArrayList<String> getUnlinked_place() {
        return unlinked_place;
    }

    public void setUnlinked_place(ArrayList<String> unlinked_place) {
        this.unlinked_place = unlinked_place;
    }

    public ArrayList<String> getUnlinked_management() {
        return unlinked_management;
    }

    public void setUnlinked_management(ArrayList<String> unlinked_management) {
        this.unlinked_management = unlinked_management;
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

    public ArrayList<EventTimes> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<EventTimes> times) {
        this.times = times;
    }

    public String getLinked_place() {
        return linked_place;
    }

    public void setLinked_place(String linked_place) {
        this.linked_place = linked_place;
    }

    public String getLinked_management() {
        return linked_management;
    }

    public void setLinked_management(String linked_management) {
        this.linked_management = linked_management;
    }
}
