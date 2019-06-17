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

public class AttendanceResponse implements ApiResponse, Serializable {

    private String date;
    private String reason;
    private JSONArray attendances;
    private String message;


    private AttendanceResponse(){}

    private AttendanceResponse(JSONObject response){
        for (Field f : AttendanceResponse.class.getDeclaredFields()){
            if("attendances".equals(f.getName()))
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

    public AttendanceResponse(JSONArray attendances){
        this.attendances = attendances;
    }

    public List<AttendanceResponse> getAttendanceList(){

        List<AttendanceResponse>  attendanceList  = new ArrayList<AttendanceResponse>();


        for(int i =0; i < attendances.length(); i++){
            JSONObject response = null;
            try {
                response = attendances.getJSONObject(i);
                attendanceList.add( new AttendanceResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return attendanceList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : AttendanceResponse.class.getDeclaredFields()){
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
        return attendances.toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
