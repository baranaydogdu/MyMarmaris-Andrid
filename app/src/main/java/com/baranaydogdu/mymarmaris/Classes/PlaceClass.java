package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceClass implements Serializable {

    String id;      //GENERATED
    int sortnumber;

    ArrayList<String> name, explain, adres;
    Contactinfo contactinfo;
    LocationClass location;

    public ArrayList<Integer> opentime,closetime;
    public ArrayList<String> topphotos,downphotos;
    String videoversion,mapphotoversion;
    Long new_end_time;
    Boolean isactive;

    public static final int ACTIVE_PLACE=0;
    public static final int INACTIVE_PLACE=1;


    public static final int FAVORIDEGIL=100000;

    public PlaceClass() {


    }

    public PlaceClass(String id,ArrayList<String> names) {

        this.id = id;
        this.sortnumber = 0;
        ArrayList emty=new ArrayList();
        emty.add("");emty.add("");emty.add("");emty.add("");emty.add("");
        this.name = names;
        this.explain = emty;
        this.adres = emty;
        Contactinfo contactinfo=new Contactinfo("","","","","","","");
        this.contactinfo = contactinfo;
        LocationClass locationemty=new LocationClass(0.0,0.0);
        this.location = locationemty;
        ArrayList<Integer> opentimeemty=new ArrayList<>();
        opentimeemty.add(2000);opentimeemty.add(2000);opentimeemty.add(2000);opentimeemty.add(2000);opentimeemty.add(2000);opentimeemty.add(2000);opentimeemty.add(2000);
        this.opentime = opentimeemty;
        this.closetime = opentimeemty;
        this.topphotos = null;
        this.downphotos = null;
        this.videoversion = "default";
        this.mapphotoversion = "default";
        this.new_end_time = 0l;
        this.isactive = false;

    }

    public PlaceClass(String id,String name0,String adres0,String phonenumer, Double lat, Double log) {

        this.id = id;
        this.sortnumber = FAVORIDEGIL;
        ArrayList emty=new ArrayList();
        emty.add("");emty.add("");emty.add("");emty.add("");emty.add("");
        ArrayList namesss=new ArrayList();
        namesss.add(name0);namesss.add("");namesss.add("");namesss.add("");namesss.add("");
        this.name = namesss;
        ArrayList explainsss=new ArrayList();
        explainsss.add(adres0);explainsss.add("");explainsss.add("");explainsss.add("");explainsss.add("");
        this.explain = explainsss;
        ArrayList<String> adresssss=new ArrayList<>();
        adresssss.add(adres0);adresssss.add("");adresssss.add("");adresssss.add("");adresssss.add("");
        this.adres = adresssss;
        Contactinfo contactinfo=new Contactinfo(phonenumer,"","","","","","");
        this.contactinfo = contactinfo;
        LocationClass locationemty=new LocationClass(lat,log);
        this.location = locationemty;
        ArrayList<Integer> opentimeemty=new ArrayList<>();
        opentimeemty.add(0001);opentimeemty.add(0001);opentimeemty.add(0001);opentimeemty.add(0001);opentimeemty.add(0001);opentimeemty.add(0001);opentimeemty.add(0001);
        ArrayList<Integer> closetimeemty=new ArrayList<>();
        closetimeemty.add(2359);closetimeemty.add(2359);closetimeemty.add(2359);closetimeemty.add(2359);closetimeemty.add(2359);closetimeemty.add(2359);closetimeemty.add(2359);
        this.opentime = opentimeemty;
        this.closetime = closetimeemty;
        this.topphotos = null;
        this.downphotos = null;
        this.videoversion = "default";
        this.mapphotoversion = "default";
        this.new_end_time = 0l;
        this.isactive = true;

    }

    public PlaceClass(String id, int sortnumber, ArrayList<String> name, ArrayList<String> explain,
                      ArrayList<String> adres, Contactinfo contactinfo, LocationClass location,
                      ArrayList<Integer> opentime, ArrayList<Integer> closetime, ArrayList<String> topphotos,
                      ArrayList<String> downphotos, String videoversion, String mapphotoversion,
                      Long new_end_time, Boolean isactive) {

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
        if (topphotos==null) this.topphotos=new ArrayList<>();
        if (downphotos==null) this.downphotos=new ArrayList<>();

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

    public ArrayList<String> getTopphotos() {
        if (topphotos==null) topphotos=new ArrayList<>();
        return topphotos;
    }

    public void setTopphotos(ArrayList<String> topphotos) {
        if (topphotos==null) topphotos=new ArrayList<>();
        this.topphotos = topphotos;
    }

    public ArrayList<String> getDownphotos() {
        if (downphotos==null) downphotos=new ArrayList<>();
        return downphotos;
    }

    public void setDownphotos(ArrayList<String> downphotos) {
        if (downphotos==null) downphotos=new ArrayList<>();
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
}
