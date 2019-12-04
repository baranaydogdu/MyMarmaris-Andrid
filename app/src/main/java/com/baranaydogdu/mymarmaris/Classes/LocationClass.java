package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;

public class LocationClass implements Serializable {

    private Double lat,log;

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
