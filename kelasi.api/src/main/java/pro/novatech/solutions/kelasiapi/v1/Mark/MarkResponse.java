package pro.novatech.solutions.kelasiapi.v1.Mark;

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

public class MarkResponse  implements ApiResponse, Serializable {

    private double marks;
    private String label;
    private String name;
    private double maximum_marks;
    private JSONArray mark_list;


    private MarkResponse(){}

    private MarkResponse(JSONObject response){
        for (Field f : MarkResponse.class.getDeclaredFields()){
            if("mark_list".equals(f.getName()))
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

    public MarkResponse(JSONArray mark_list){
        this.mark_list = mark_list;
    }

    public List<MarkResponse> getMarkList(){
        List<MarkResponse>  markList  = new ArrayList<MarkResponse>();


        for(int i =0; i < mark_list.length(); i++){
            JSONObject response = null;
            try {
                response = mark_list.getJSONObject(i);
                markList.add( new MarkResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return markList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : MarkResponse.class.getDeclaredFields()){
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
        return mark_list.toString();
    }


    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaximum_marks() {
        return maximum_marks;
    }

    public void setMaximum_marks(double maximum_marks) {
        this.maximum_marks = maximum_marks;
    }
}
