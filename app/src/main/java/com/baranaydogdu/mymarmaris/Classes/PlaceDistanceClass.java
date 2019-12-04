package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;

public class PlaceDistanceClass implements Serializable  {

    PlaceClass place;
    float distance;

    public PlaceDistanceClass() {
    }

    public PlaceDistanceClass(PlaceClass place, float distance) {
        this.place = place;
        this.distance = distance;
    }

    public PlaceClass getPlace() {
        return place;
    }

    public void setPlace(PlaceClass place) {
        this.place = place;
    }

    public int getDistance() {
        return (int)distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }


}
