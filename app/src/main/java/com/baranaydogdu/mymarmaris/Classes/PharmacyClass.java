package com.baranaydogdu.mymarmaris.Classes;

public class PharmacyClass {

    private String name;
    private LocationClass locatin;
    private String phonenumber;
    private String adress;
    private int distance;

    public PharmacyClass() {

    }

    public PharmacyClass(String name, LocationClass locatin, String phonenumber, String adress) {
        this.name = name;
        this.locatin = locatin;
        this.phonenumber = phonenumber;
        this.adress = adress;
        distance=0;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDistance(float distance) {
        this.distance = (int)distance;
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
}
