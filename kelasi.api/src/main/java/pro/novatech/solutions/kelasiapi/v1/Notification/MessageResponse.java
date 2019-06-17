package pro.novatech.solutions.kelasiapi.v1.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class MessageResponse implements ApiResponse, Serializable {

    private int message_id;
    private String subject;
    private String created_at;
    private String message;
    private String sender_id;
    private String receiver_id;
    private int is_read;
    private String sender;
    private String receiver;

    private JSONArray messages;


    private MessageResponse(){}

    public MessageResponse(JSONObject response){
        for (Field f : MessageResponse.class.getDeclaredFields()){
            if("messages".equals(f.getName()))
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

    public MessageResponse(JSONArray messages){
        this.messages = messages;
    }

    public List<MessageResponse> getMessageList(){
        List<MessageResponse>  messageList  = new ArrayList<MessageResponse>();


        for(int i =0; i < messages.length(); i++){
            JSONObject response = null;
            try {
                response = messages.getJSONObject(i);
                messageList.add( new MessageResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return messageList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : MessageResponse.class.getDeclaredFields()){
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
        return messages.toString();
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
