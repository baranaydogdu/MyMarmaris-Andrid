package com.baranaydogdu.mymarmaris.Classes;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EventForRealm extends RealmObject {

    @PrimaryKey
    public String id = "";

    public RealmList<String> name = new RealmList<>();
    public String name0 = "";
    public String name1 = "";
    public String name2 = "";
    public String name3 = "";
    public String name4 = "";

    public RealmList<String> explain = new RealmList<>();
    public String explain0 = "";
    public String explain1 = "";
    public String explain2 = "";
    public String explain3 = "";
    public String explain4 = "";
    public RealmList<String> adres = new RealmList<>();

    public String phonenumber = "";
    public String whatsapp = "";
    public String instagram = "";
    public String facebook = "";
    public String website = "";
    public String mailadress = "";
    public String buyticket = "";

    public Double lat = 0.0;
    public Double log = 0.0;

    public String linked_place = "";
    public String linked_management = "";

    public RealmList<String> unlinked_place = new RealmList<>();
    public RealmList<String> unlinked_management = new RealmList<>();

    public String videoversion = "";
    public String mapphotoversion = "";

    public RealmList<String> topphotos = new RealmList<>();
    public RealmList<String> downphotos = new RealmList<>();

    public RealmList<EventTimes> times = new RealmList<>();

    public EventForRealm() {
    }

    public EventForRealm(EventClass event) {

        this.id = event.id;

        for (String samp : event.name) {
            this.name.add(samp);
        }
        name0 = event.name.get(0);
        name1 = event.name.get(1);
        name2 = event.name.get(2);
        name3 = event.name.get(3);
        name4 = event.name.get(4);

        for (String samp : event.explain) {  this.explain.add(samp);  }
        explain0 = event.explain.get(0);
        explain1 = event.explain.get(1);
        explain2 = event.explain.get(2);
        explain3 = event.explain.get(3);
        explain4 = event.explain.get(4);

        for (String samp : event.adres) {
            this.adres.add(samp);
        }

        this.phonenumber = event.contactinfo.phonenumber;
        this.whatsapp = event.contactinfo.whatsapp;
        this.instagram = event.contactinfo.instagram;
        this.facebook = event.contactinfo.facebook;
        this.website = event.contactinfo.website;
        this.mailadress = event.contactinfo.mailadress;
        this.buyticket = event.contactinfo.buyticket;

        this.lat = event.location.lat;
        this.log = event.location.log;

        this.linked_place = event.linked_place;
        this.linked_management = event.linked_management;
        for (String samp : event.unlinked_place) {
            this.unlinked_place.add(samp);
        }
        for (String samp : event.unlinked_management) {
            this.unlinked_management.add(samp);
        }

        this.videoversion = event.videoversion;
        this.mapphotoversion = event.mapphotoversion;

        for (String samp : event.topphotos) {
            this.topphotos.add(samp);
        }
        for (String samp : event.downphotos) {
            this.downphotos.add(samp);
        }
        for (EventTimes samp : event.times) {
            this.times.add(samp);
        }

    }


    public EventClass toEvent() {
        EventClass event = new EventClass();

        event.id = id;

        for (String samp : name) event.name.add(samp);
        for (String samp : explain) event.explain.add(samp);
        for (String samp : adres) event.adres.add(samp);

        event.contactinfo.phonenumber = phonenumber;
        event.contactinfo.whatsapp = whatsapp;
        event.contactinfo.instagram = instagram;
        event.contactinfo.facebook = facebook;
        event.contactinfo.website = website;
        event.contactinfo.mailadress = mailadress;
        event.contactinfo.buyticket = buyticket;

        event.location.lat = lat;
        event.location.log = log;

        event.linked_place = linked_place;
        event.linked_management = linked_management;
        for (String samp : unlinked_place) {
            event.unlinked_place.add(samp);
        }
        for (String samp : unlinked_management) {
            event.unlinked_management.add(samp);
        }

        event.videoversion = videoversion;
        event.mapphotoversion = mapphotoversion;

        for (String samp : topphotos) event.topphotos.add(samp);
        for (String samp : downphotos) event.downphotos.add(samp);
        for (EventTimes samp : times) event.times.add(samp);

        return event;
    }

    public ArrayList<EventClass> multiple() {
        ArrayList<EventClass> eventList = new ArrayList<>();

        for (EventTimes singleTime : times) {

            if (singleTime.end_time > new Date().getTime()) {

                EventClass singletimeevent = this.toEvent();
                singletimeevent.times.clear();
                singletimeevent.times.add(singleTime);
                eventList.add(singletimeevent);
            }
        }
        return eventList;
    }

    public ArrayList<EventClass> todayMultiple() {
        ArrayList<EventClass> eventList = new ArrayList<>();

        for (EventTimes singleTime : times) {

            if (singleTime.end_time > new Date().getTime() && (new Date().getTime() + 1000 * 60 * 60 * 72 ) > singleTime.start_time) {

                EventClass singletimeevent = this.toEvent();
                singletimeevent.times.clear();
                singletimeevent.times.add(singleTime);
                eventList.add(singletimeevent);
            }

        }


        return eventList;
    }


}
