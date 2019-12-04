package com.baranaydogdu.mymarmaris.Classes;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NotificationClass {

    public static final String AUTH_KEY="key=AAAAAwlJYZY:APA91bHN3TfaKiI3wlyPb3p7DNQu7EQg2M4YJyvES4r7GEH3XIWMBRMjy2SWTbxtkrXivveGa2YRD6fk6uNs8u13IynqBLV3pkqiU4EYTtliLDmS6qKrNJuA-OHx0JSEatU78qnnvpR0";

    public static final String SENDER_ID="13040705942";

    public static final String TO_MAIN      = "tomain";
    public static final String TO_SUB       = "tosub";
    public static final String TO_EVENT     = "toevent";
    public static final String TO_PLACE     = "toplace";
    public static final String TO_NONE      = "nowhere";

    ArrayList<String> title,body;
    ArrayList<String> to_who;
    String iconnumber;
    String click_action, click_id;
    int topicnumber;

    public NotificationClass(ArrayList<String> title, ArrayList<String> body, ArrayList<String> to_who, String iconnumber, String click_action, String click_id, int topicnumber) {

        this.title = title;
        this.body = body;
        this.to_who = to_who;
        this.iconnumber = iconnumber;
        this.click_action = click_action;
        this.click_id = click_id;
        this.topicnumber = topicnumber;
        if (this.title==null)  title=new ArrayList<>();
        if (this.body==null)   body=new ArrayList<>();
        if (this.to_who==null) to_who=new ArrayList<>();

    }

    public String getnotification_text() {

        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();

        try {

            jsonParam.put("to", "/topics/topic" + topicnumber);
            jsonParam.put("sender", SENDER_ID);

            for (int i = 0; i < 5; i++) {
                data.put("title" + i, title.get(i));
                data.put("body" + i, body.get(i));
            }

            data.put("iconnumber",iconnumber);
            data.put("click_action", click_action);
            data.put("click_id", click_id);
            data.put("to_who_count",to_who.size());
            for (int i=0;i<to_who.size();i++){
                data.put("to_who"+i,to_who.get(i));
            }

            jsonParam.put("data", data);

        } catch (Exception e) {

            e.printStackTrace();
        }


        return jsonParam.toString();

    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }

    public ArrayList<String> getBody() {
        return body;
    }

    public void setBody(ArrayList<String> body) {
        this.body = body;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }

    public String getClick_id() {
        return click_id;
    }

    public void setClick_id(String click_id) {
        this.click_id = click_id;
    }

    public int getTopicnumber() {
        return topicnumber;
    }

    public void setTopicnumber(int topicnumber) {
        this.topicnumber = topicnumber;
    }

    public String getIconnumber() {
        return iconnumber;
    }

    public void setIconnumber(String iconnumber) {
        this.iconnumber = iconnumber;
    }


}
