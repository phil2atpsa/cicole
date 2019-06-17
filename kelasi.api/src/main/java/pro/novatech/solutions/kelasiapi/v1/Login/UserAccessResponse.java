package pro.novatech.solutions.kelasiapi.v1.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/1/2017.
 */

public class UserAccessResponse implements ApiResponse, Serializable {

    private int id;
    private int institution_id;
    private String  name;
    private String  surname;
    private String  username;
    private String  auth_key;
    private String  email;
    private String  created_at;
    private String  assignment;
    private String  access_token;
    private String  message;
    private String  mobile_phone;
    private JSONArray users;



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
        return users.toString();
    }

    private UserAccessResponse(){

    }

    public List<UserAccessResponse> getUserList(){
        List<UserAccessResponse>  userList  = new ArrayList<UserAccessResponse>();


        for(int i =0; i < users.length(); i++){
            JSONObject response = null;
            try {
                response = users.getJSONObject(i);
                userList.add( new UserAccessResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return userList;
    }
    public UserAccessResponse(JSONArray users) {
        this.users = users;
    }
    public UserAccessResponse(JSONObject response) {

        for (Field f : UserAccessResponse.class.getDeclaredFields()){
            if("users".equals(f.getName()))
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstitution_id() {
        return institution_id;
    }

    public void setInstitution_id(int institution_id) {
        this.institution_id = institution_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
