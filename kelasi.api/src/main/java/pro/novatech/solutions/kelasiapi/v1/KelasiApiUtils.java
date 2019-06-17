package pro.novatech.solutions.kelasiapi.v1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by p.lukengu on 4/4/2017.
 */

public class KelasiApiUtils {

    public enum  JSONTYPE {JSON_ARRAY, JSON_OBJECT, NOT_VALID_JSON}

    public static JSONTYPE isJSONValid(String test) {
        try {
            new JSONObject(test);
            return JSONTYPE.JSON_OBJECT;
        } catch (JSONException | NullPointerException ex) {
            try {
                new JSONArray(test);
                return JSONTYPE.JSON_ARRAY;
            } catch (JSONException | NullPointerException ex1) {

            }
        }
        return JSONTYPE.NOT_VALID_JSON;
    }
}
