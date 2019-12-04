package com.baranaydogdu.mymarmaris.Classes;

public class SavedFilesClass {

    private String id,videoversion;
    private int vsavednumber;

    public SavedFilesClass() {

    }

    public SavedFilesClass(String id, String videoversion, int vsavednumber) {
        this.id = id;
        this.videoversion = videoversion;
        this.vsavednumber = vsavednumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoversion() {
        return videoversion;
    }

    public void setVideoversion(String videoversion) {
        this.videoversion = videoversion;
    }

    public int getVsavednumber() {
        return vsavednumber;
    }

    public void setVsavednumber(int vsavednumber) {
        this.vsavednumber = vsavednumber;
    }

}
