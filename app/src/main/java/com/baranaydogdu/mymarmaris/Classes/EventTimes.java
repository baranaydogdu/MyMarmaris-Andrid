package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;

public class EventTimes implements Serializable {

    private Long start_time, end_time;

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
