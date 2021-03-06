package com.baranaydogdu.mymarmaris.Classes;


import org.json.JSONObject;
import java.util.ArrayList;

public class NotificationClass {

    public static final String AUTH_KEY="key=AAAAAwlJYZY:APA91bHN3TfaKiI3wlyPb3p7DNQu7EQg2M4YJyvES4r7GEH3XIWMBRMjy2SWTbxtkrXivveGa2YRD6fk6uNs8u13IynqBLV3pkqiU4EYTtliLDmS6qKrNJuA-OHx0JSEatU78qnnvpR0";

    public static final String SENDER_ID="13040705942";

    public static final String TO_MAIN      = "tomain";
    public static final String TO_SUB       = "tosub";
    public static final String TO_EVENT     = "toevent";
    public static final String TO_PLACE     = "toplace";
    public static final String TO_NONE      = "nowhere";

    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> body = new ArrayList<>();

    public ArrayList<String> to_who = new ArrayList<>();
    public String click_action = "";
    public String click_id = "";

    int topicnumber = 0;

    public NotificationClass() {
    }


    public NotificationClass(ArrayList<String> title, ArrayList<String> body, ArrayList<String> to_who, String click_action, String click_id, int topicnumber) {
        this.title = title;
        this.body = body;
        this.to_who = to_who;
        this.click_action = click_action;
        this.click_id = click_id;
        this.topicnumber = topicnumber;
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

    public ArrayList<String> getTo_who() {
        return to_who;
    }

    public void setTo_who(ArrayList<String> to_who) {
        this.to_who = to_who;
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

    public String getnotification_text() {

        JSONObject jsonParam = new JSONObject();
        JSONObject data = new JSONObject();

        try {

            jsonParam.put("to", "/topics/topic" + topicnumber);
            jsonParam.put("sender", SENDER_ID);
            jsonParam.put("priority", "high");

            for (int i = 0; i < 5; i++) {
                data.put("title" + i, title.get(i));
                data.put("body" + i, body.get(i));
            }

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

    public ArrayList<String> getIosNotificationText(){

        ArrayList<String> singlenotifications = new ArrayList<>();
        singlenotifications.clear();

        for (int i = 0; i<5;i++){
            JSONObject jsonParam = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            try {

                String towhotext = "";
                for (int j=0;j<to_who.size();j++){

                    if (j != to_who.size() -1 ){
                        towhotext = towhotext + "'"+to_who.get(j) + i + "' in topics || ";
                    } else {
                        towhotext = towhotext + "'"+to_who.get(j) + i + "' in topics";

                    }

                }

                jsonParam.put("condition", towhotext);
                jsonParam.put("sender", SENDER_ID);
                jsonParam.put("content_available", true);
                jsonParam.put("priority", "high");

                if (title.get(i).equals("")) {
                    notification.put("title", title.get(0));

                } else {
                    notification.put("title", title.get(i));

                }

                if (body.get(i).equals("")){
                    notification.put("body" , body.get(0));

                }else {
                    notification.put("body" , body.get(i));

                }
                notification.put("sound" , "default");

                data.put("click_action", click_action);
                data.put("click_id", click_id);

                jsonParam.put("data", data);
                jsonParam.put("notification", notification);

            } catch (Exception e) {

                e.printStackTrace();
            }

            singlenotifications.add(jsonParam.toString());
        }

        return singlenotifications;

    }



}
