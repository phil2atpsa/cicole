package pro.novatech.solutions.kelasiapi.v1.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/13/2017.
 */

public class SubjectResponse   implements ApiResponse, Serializable {


    private SubjectResponse(){}

    private SubjectResponse(JSONObject response){
        for (Field f : SubjectResponse.class.getDeclaredFields()){
            if("subjects".equals(f.getName()))
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

    public SubjectResponse(JSONArray subjects){
        this.subjects = subjects;
    }

    public List<SubjectResponse> getSubjectList(){
        List<SubjectResponse>  subjectList  = new ArrayList<SubjectResponse>();


        for(int i =0; i < subjects.length(); i++){
            JSONObject response = null;
            try {
                response = subjects.getJSONObject(i);
                subjectList.add( new SubjectResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return subjectList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : SubjectResponse.class.getDeclaredFields()){
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
        return subjects.toString();
    }

    private int subject_id;
    private String name;
    private String code;
    private JSONArray subjects;

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
