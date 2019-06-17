package pro.novatech.solutions.kelasiapi.v1.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by pro on 2018/02/17.
 */

public class AbsentDaysResponse implements ApiResponse, Serializable {

    private String reason;
    private String date;
    private JSONArray absentDays;


    public AbsentDaysResponse(){}

    public  AbsentDaysResponse(JSONObject response){
        for (Field f : TeacherMarksResponse.class.getDeclaredFields()){
            if("absentDays".equals(f.getName()))
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

    public AbsentDaysResponse(JSONArray absentDays){
        this.absentDays = absentDays;
    }

    public ArrayList<AbsentDaysResponse> getAbsentDays(){
        ArrayList<AbsentDaysResponse>  absentDaysResponseList  = new ArrayList<>();


        for(int i =0; i < absentDays.length(); i++){
            JSONObject response = null;
            try {
                response = absentDays.getJSONObject(i);
                absentDaysResponseList.add( new AbsentDaysResponse(response));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return absentDaysResponseList;


    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : TeacherMarksResponse.class.getDeclaredFields()){
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
        return absentDays.toString();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
