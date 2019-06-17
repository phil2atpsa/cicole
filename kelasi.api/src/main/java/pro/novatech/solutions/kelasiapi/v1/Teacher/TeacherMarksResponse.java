package pro.novatech.solutions.kelasiapi.v1.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by pro on 2018/02/17.
 */

public class TeacherMarksResponse implements ApiResponse, Serializable {


    private int subject_id;
    private String subject_name;
    private String subject_code;
    private String exam_marks;
    private JSONArray teacher_marks;



    private TeacherMarksResponse(){}
    public  TeacherMarksResponse(JSONObject response){
        for (Field f : TeacherMarksResponse.class.getDeclaredFields()){
            if("teacher_marks".equals(f.getName()))
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

    public TeacherMarksResponse(JSONArray teacher_marks){
        this.teacher_marks = teacher_marks;
    }

    public List<TeacherMarksResponse> getTeacherMarksList(){
        List<TeacherMarksResponse>  teacherMarksList  = new ArrayList<TeacherMarksResponse>();


        for(int i =0; i < teacher_marks.length(); i++){
            JSONObject response = null;
            try {
                response = teacher_marks.getJSONObject(i);
                teacherMarksList.add( new TeacherMarksResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return teacherMarksList;
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
        return teacher_marks.toString();
    }


    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getExam_marks() {
        return exam_marks;
    }

    public void setExam_marks(String exam_marks) {
        this.exam_marks = exam_marks;
    }

    public JSONArray getTeacher_marks() {
        return teacher_marks;
    }

    public void setTeacher_marks(JSONArray teacher_marks) {
        this.teacher_marks = teacher_marks;
    }
}
