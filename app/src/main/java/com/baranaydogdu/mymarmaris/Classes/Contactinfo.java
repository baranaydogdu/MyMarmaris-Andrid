package com.baranaydogdu.mymarmaris.Classes;

import java.io.Serializable;

public class Contactinfo implements Serializable {

    private String phonenumber,whatsapp,instagram,facebook,website,mailadress,buyticket;

    public Contactinfo() {

    }

    public Contactinfo(String phonenumber, String whatsapp, String instagram, String facebook, String website, String mailadress, String buyticket) {
        this.phonenumber = phonenumber;
        this.whatsapp = whatsapp;
        this.instagram = instagram;
        this.facebook = facebook;
        this.website = website;
        this.mailadress = mailadress;
        this.buyticket = buyticket;
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
}
