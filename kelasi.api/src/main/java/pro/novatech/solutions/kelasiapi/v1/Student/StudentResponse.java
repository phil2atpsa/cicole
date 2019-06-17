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
 * Created by p.lukengu on 4/2/2017.
 */

public class StudentResponse implements ApiResponse, Serializable {

    private int  student_id;
    private  int institution_id;
    private String institution;
    private String  grade;
    private String  first_name;
    private String  middle_name;
    private String  last_name;
    private  JSONArray students;
    private  String  absent_days;


    private StudentResponse(){}

    public  StudentResponse(JSONObject response){
        for (Field f : StudentResponse.class.getDeclaredFields()){
            if("students".equals(f.getName()))
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

    public StudentResponse(JSONArray students){
       this.students = students;
    }

    public  List<StudentResponse> getStudentsList(){
        List<StudentResponse>  studentsList  = new ArrayList<StudentResponse>();


        for(int i =0; i < students.length(); i++){
            JSONObject response = null;
            try {
                response = students.getJSONObject(i);
                studentsList.add( new StudentResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            }

        return studentsList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : StudentResponse.class.getDeclaredFields()){
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
        return students.toString();
    }


    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getInstitution_id() {
        return institution_id;
    }

    public void setInstitution_id(int institution_id) {
        this.institution_id = institution_id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAbsent_days() {
        return absent_days;
    }

    public void setAbsent_days(String absent_days) {
        this.absent_days = absent_days;
    }
}
