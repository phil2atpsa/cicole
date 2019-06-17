package pro.novatech.solutions.kelasiapi.v1.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/11/2017.
 */

public class EventResponse implements ApiResponse, Serializable {



    private EventResponse(){}

    private EventResponse(JSONObject response){
        for (Field f : EventResponse.class.getDeclaredFields()){
            if("events".equals(f.getName()))
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

    public EventResponse(JSONArray events){
        this.events = events;
    }

    public List<EventResponse> getEventList(){
        List<EventResponse>  eventList  = new ArrayList<EventResponse>();


        for(int i =0; i < events.length(); i++){
            JSONObject response = null;
            try {
                response = events.getJSONObject(i);
                eventList.add( new EventResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return eventList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : EventResponse.class.getDeclaredFields()){
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
        return events.toString();
    }



    private int event_id;
    private String  title;
    private String  description;
    private String date;
    private String start_time;
    private String end_time;
    private JSONArray events;



    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
