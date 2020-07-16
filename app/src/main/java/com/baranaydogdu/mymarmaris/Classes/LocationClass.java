package com.baranaydogdu.mymarmaris.Classes;

public class LocationClass  {

    public Double lat = 0.0;
    public Double log = 0.0;


    public LocationClass() {
    }

    public LocationClass(Double lat, Double log) {
        this.lat = lat;
        this.log = log;
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
}
