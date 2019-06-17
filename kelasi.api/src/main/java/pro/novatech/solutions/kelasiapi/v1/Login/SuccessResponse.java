package pro.novatech.solutions.kelasiapi.v1.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by pro on 2018/02/10.
 */

public class SuccessResponse implements ApiResponse {

    private String  message;

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : UserAccessResponse.class.getDeclaredFields()){
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
        return null;
    }

    public SuccessResponse(JSONObject response) {

        for (Field f : SuccessResponse.class.getDeclaredFields()){

            try{
                f.set(this, response.optString(f.getName()));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    public String getMessage(){
        return message;
    }
}
