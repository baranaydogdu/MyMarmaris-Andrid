package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EventClass implements Serializable {

    String id;      //GENERATED

    ArrayList<String> name, explain, adres,unlinked_place,unlinked_management;
    Contactinfo contactinfo;
    LocationClass location;
    public ArrayList<String> topphotos,downphotos;
    String videoversion,mapphotoversion;
    public ArrayList<EventTimes> times;
    String linked_place,linked_management;

    public EventClass() {

    }

    public String getMapphotoversion() {
        return mapphotoversion;
    }

    public void setMapphotoversion(String mapphotoversion) {
        this.mapphotoversion = mapphotoversion;
    }

    public EventClass(String id, ArrayList<String> name, ArrayList<String> explain,
                      ArrayList<String> adres, ArrayList<String> unlinked_place,
                      ArrayList<String> unlinked_management, Contactinfo contactinfo,
                      LocationClass location, ArrayList<String> topphotos, ArrayList<String> downphotos,
                      String videoversion, String mapphotoversion, ArrayList<EventTimes> times,
                      String linked_place, String linked_management) {
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
        this.times=times;
        this.linked_place = linked_place;
        this.linked_management = linked_management;
        if (topphotos==null) this.topphotos=new ArrayList<>();
        if (downphotos==null) this.downphotos=new ArrayList<>();

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
        if (topphotos==null) this.topphotos=new ArrayList<>();
        return topphotos;
    }

    public void setTopphotos(ArrayList<String> topphotos) {
        if (topphotos==null) this.topphotos=new ArrayList<>();
        this.topphotos = topphotos;
    }

    public ArrayList<String> getDownphotos() {
        if (downphotos==null) this.downphotos=new ArrayList<>();
        return downphotos;
    }

    public void setDownphotos(ArrayList<String> downphotos) {
        if (downphotos==null) this.downphotos=new ArrayList<>();
        this.downphotos = downphotos;
    }

    public String getVideoversion() {
        return videoversion;
    }

    public void setVideoversion(String videoversion) {
        this.videoversion = videoversion;
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

    public ArrayList<EventTimes> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<EventTimes> times) {
        this.times = times;
    }

    public ArrayList<EventTimes> seethe_nextdays(){

        ArrayList<EventTimes> nextdays=new ArrayList<>();

        Calendar calendar=Calendar.getInstance();
        Date nowdate=calendar.getTime();

        for (EventTimes singletime:times){

            if (new Date(singletime.getEnd_time()).after(nowdate)){

                nextdays.add(singletime);

            }
        }

        return nextdays;

    }







}
