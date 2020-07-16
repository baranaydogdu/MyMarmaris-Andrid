package com.baranaydogdu.mymarmaris.Classes;

public class PharmacyClass {

    public String name = "";
    public LocationClass locatin=new LocationClass();
    public String phonenumber="";
    public String adress="";
    public int distance= 0;

    public PharmacyClass() {

    }

    public PharmacyClass(String name, LocationClass locatin, String phonenumber, String adress, int distance) {
        this.name = name;
        this.locatin = locatin;
        this.phonenumber = phonenumber;
        this.adress = adress;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationClass getLocatin() {
        return locatin;
    }

    public void setLocatin(LocationClass locatin) {
        this.locatin = locatin;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
