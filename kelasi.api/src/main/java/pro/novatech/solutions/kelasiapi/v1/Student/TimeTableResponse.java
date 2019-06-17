package pro.novatech.solutions.kelasiapi.v1.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/10/2017.
 */

public class TimeTableResponse   implements ApiResponse, Serializable {

    private String weekday;
    private String subject;
    private String classtiming;
    private String start_time;
    private String end_time;
    private String room;
    private JSONArray entries;


    private TimeTableResponse(){}

    private TimeTableResponse(JSONObject response){
        for (Field f : TimeTableResponse.class.getDeclaredFields()){
            if("entries".equals(f.getName()))
                continue;

            try{
                if(f.getType() == String.class){
                    f.set(this, response.optString(f.getName()));
                }
                if(f.getType() == int.class){
                    f.set(this, response.optInt(f.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public TimeTableResponse(JSONArray entries){
        this.entries = entries;
    }

    public List<TimeTableResponse> getEntries(){
        List<TimeTableResponse>  entry  = new ArrayList<TimeTableResponse>();


        for(int i =0; i < entries.length(); i++){
            JSONObject response = null;
            try {
                response = entries.getJSONObject(i);
                entry.add( new TimeTableResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return entry;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : TimeTableResponse.class.getDeclaredFields()){
            try {
                object.put(f.getName(), f.get(this));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }

    @Override
    public String ToJSONArrayString() {
        return entries.toString();
    }



    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClasstiming() {
        return classtiming;
    }

    public void setClasstiming(String classtiming) {
        this.classtiming = classtiming;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
