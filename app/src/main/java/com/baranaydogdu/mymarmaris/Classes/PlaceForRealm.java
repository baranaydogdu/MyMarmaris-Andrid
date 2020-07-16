package com.baranaydogdu.mymarmaris.Classes;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class PlaceForRealm extends RealmObject {

    @PrimaryKey
    public String id = "";

    public String mainid;
    public String subid;

    public int sortnumber = 100000;
    public Boolean isactive = true;

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

    public RealmList<Integer> opentime = new RealmList<>();
    public RealmList<Integer> closetime = new RealmList<>();

    public String videoversion = "";
    public String mapphotoversion = "";

    public RealmList<String> topphotos = new RealmList<>();
    public RealmList<String> downphotos = new RealmList<>();

    public Long new_end_time = 0l;

    public PlaceForRealm() {

    }

    public PlaceForRealm(String mainid, String subid, PlaceClass place) {

        this.id = place.id;
        this.mainid = mainid;
        this.subid = subid;

        this.sortnumber = place.sortnumber;
        this.isactive = place.isactive;

        for (String samp : place.name) {  this.name.add(samp);  }
        name0 = place.name.get(0);
        name1 = place.name.get(1);
        name2 = place.name.get(2);
        name3 = place.name.get(3);
        name4 = place.name.get(4);

        for (String samp : place.explain) {  this.explain.add(samp);  }
        explain0 = place.explain.get(0);
        explain1 = place.explain.get(1);
        explain2 = place.explain.get(2);
        explain3 = place.explain.get(3);
        explain4 = place.explain.get(4);

        for (String samp : place.adres) {  this.adres.add(samp);  }

        this.phonenumber = place.contactinfo.phonenumber;
        this.whatsapp = place.contactinfo.whatsapp;
        this.instagram = place.contactinfo.instagram;
        this.facebook = place.contactinfo.facebook;
        this.website = place.contactinfo.website;
        this.mailadress = place.contactinfo.mailadress;
        this.buyticket = place.contactinfo.buyticket;

        this.lat = place.location.lat;
        this.log = place.location.log;

        for (int samp : place.opentime) {  this.opentime.add(samp);  }
        for (int samp : place.closetime) {  this.closetime.add(samp);  }

        this.videoversion = place.videoversion;
        this.mapphotoversion = place.mapphotoversion;

        for (String samp : place.topphotos) {  this.topphotos.add(samp);  }
        for (String samp : place.downphotos) {  this.downphotos.add(samp);  }

        this.new_end_time = place.new_end_time;

    }

    public PlaceClass toPlace(){
        PlaceClass place = new PlaceClass();

        place.id = id ;
        place.sortnumber = sortnumber ;
        place.isactive = isactive ;

        for (String samp : name) place.name.add(samp);
        for (String samp : explain) place.explain.add(samp);
        for (String samp : adres) place.adres.add(samp);

        place.contactinfo.phonenumber = phonenumber;
        place.contactinfo.whatsapp = whatsapp;
        place.contactinfo.instagram = instagram;
        place.contactinfo.facebook = facebook;
        place.contactinfo.website = website;
        place.contactinfo.mailadress = mailadress;
        place.contactinfo.buyticket = buyticket;

        place.location.lat = lat;
        place.location.log = log;

        for (int samp : opentime) place.opentime.add(samp);
        for (int samp : closetime) place.closetime.add(samp);

        place.videoversion = videoversion;
        place.mapphotoversion = mapphotoversion;

        for (String samp : topphotos) place.topphotos.add(samp);
        for (String samp : downphotos) place.downphotos.add(samp);

        place.new_end_time = new_end_time;
        return place;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainid() {
        return mainid;
    }

    public void setMainid(String mainid) {
        this.mainid = mainid;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public int getSortnumber() {
        return sortnumber;
    }

    public void setSortnumber(int sortnumber) {
        this.sortnumber = sortnumber;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public RealmList<String> getName() {
        return name;
    }

    public void setName(RealmList<String> name) {
        this.name = name;
    }

    public RealmList<String> getExplain() {
        return explain;
    }

    public void setExplain(RealmList<String> explain) {
        this.explain = explain;
    }

    public RealmList<String> getAdres() {
        return adres;
    }

    public void setAdres(RealmList<String> adres) {
        this.adres = adres;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMailadress() {
        return mailadress;
    }

    public void setMailadress(String mailadress) {
        this.mailadress = mailadress;
    }

    public String getBuyticket() {
        return buyticket;
    }

    public void setBuyticket(String buyticket) {
        this.buyticket = buyticket;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public RealmList<Integer> getOpentime() {
        return opentime;
    }

    public void setOpentime(RealmList<Integer> opentime) {
        this.opentime = opentime;
    }

    public RealmList<Integer> getClosetime() {
        return closetime;
    }

    public void setClosetime(RealmList<Integer> closetime) {
        this.closetime = closetime;
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

    public RealmList<String> getTopphotos() {
        return topphotos;
    }

    public void setTopphotos(RealmList<String> topphotos) {
        this.topphotos = topphotos;
    }

    public RealmList<String> getDownphotos() {
        return downphotos;
    }

    public void setDownphotos(RealmList<String> downphotos) {
        this.downphotos = downphotos;
    }

    public Long getNew_end_time() {
        return new_end_time;
    }

    public void setNew_end_time(Long new_end_time) {
        this.new_end_time = new_end_time;
    }
}
