package com.baranaydogdu.mymarmaris.Classes;

import io.realm.RealmObject;

public class EventTimes extends RealmObject {

    public Long start_time = 0l;
    public Long end_time = 0l;

    public EventTimes() {
    }

    public EventTimes(Long start_time, Long end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }
}
