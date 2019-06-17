package pro.novatech.solutions.kelasiapi.v1.Exams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by pro on 2018/02/11.
 */

public class ExamsResponse implements ApiResponse, Serializable {
    private ExamsResponse(){}

    private ExamsResponse(JSONObject response){
        for (Field f : ExamsResponse.class.getDeclaredFields()){
            if("exams".equals(f.getName()))
                continue;

            try{
                if(f.getType() == String.class){
                    f.set(this, response.optString(f.getName()));
                }
                if(f.getType() == int.class){
                    f.set(this, response.optInt(f.getName()));
                }
                if(f.getType() == double.class){
                    f.set(this, response.optDouble(f.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public ExamsResponse(JSONArray exams){
        this.exams = exams;
    }

    public List<ExamsResponse> getExamsList(){
        List<ExamsResponse>  examsList  = new ArrayList<ExamsResponse>();


        for(int i =0; i < exams.length(); i++){
            JSONObject response = null;
            try {
                response = exams.getJSONObject(i);
                examsList.add( new ExamsResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return examsList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : ExamsResponse.class.getDeclaredFields()){
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
        return exams.toString();
    }



    private int  exam_id;
    private int  subject_id;
    private int  exam_group_id;
    private String exam_group_name;
    private String exam_name;
    private String exam_date;
    private String start_time;
    private String end_time;
    private double maximum_marks;
    private double minimum_marks;
    private JSONArray exams;

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getExam_group_id() {
        return exam_group_id;
    }

    public void setExam_group_id(int exam_group_id) {
        this.exam_group_id = exam_group_id;
    }

    public String getExam_group_name() {
        return exam_group_name;
    }

    public void setExam_group_name(String exam_group_name) {
        this.exam_group_name = exam_group_name;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getExam_date() {
        return exam_date;
    }

    public void setExam_date(String exam_date) {
        this.exam_date = exam_date;
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

    public double getMaximum_marks() {
        return maximum_marks;
    }

    public void setMaximum_marks(double maximum_marks) {
        this.maximum_marks = maximum_marks;
    }

    public double getMinimum_marks() {
        return minimum_marks;
    }

    public void setMinimum_marks(double minimum_marks) {
        this.minimum_marks = minimum_marks;
    }
}
