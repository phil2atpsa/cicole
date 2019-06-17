package pro.novatech.solutions.kelasiapi.v1.Notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import pro.novatech.solutions.kelasiapi.R;
import pro.novatech.solutions.kelasiapi.v1.HttpVerb;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiConfiguration;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiUtils;
import pro.novatech.solutions.kelasiapi.v1.MimeType;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class Message extends AsyncHttpClient  {

    private OnServiceResponseListener<MessageResponse> mCallBack;
    private Context mContext;
    private ProgressDialog progressBar;
    private boolean  showProgressDialog;

    public Message(Context context, OnServiceResponseListener callback, boolean showProgressDialog) {
        mCallBack = callback;
        mContext = context;
        this.showProgressDialog = showProgressDialog;
        addHeader("Accept", MimeType.APPLICATION_JSON.type());
        if(this.showProgressDialog) {
            progressBar = new ProgressDialog(mContext, R.style.TransparentProgressDialog);
            //progressBar.setProgressStyle(R.style.TransparentProgressDialog);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar.setProgressDrawable(context.getDrawable(R.drawable.loading));
            } else {
                progressBar.setProgressDrawable( context.getResources().getDrawable(R.drawable.loading));
            }

            progressBar.show();
        }
    }


    private void sendRequest(String url, RequestParams params, HttpVerb verb, StringEntity entity){
        if(verb.equals(HttpVerb.GET)) {
            get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    MessageResponse messageResponse = new MessageResponse(response);
                    mCallBack.onSuccess(messageResponse);
                    if (showProgressDialog)
                        progressBar.dismiss();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    JSONArray array = null;
                    String error_message = res;

                    KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                    if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                        JSONObject object = null;
                        try {
                            object = new JSONObject(res);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                        try {
                            array = new JSONArray(res);
                            JSONObject object = array.getJSONObject(0);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    mCallBack.onFailure(new KelasiApiException(error_message));
                    if (showProgressDialog)
                        progressBar.dismiss();
                }
            });
        }
        if(verb.equals(HttpVerb.POST)) {
            post(mContext,url,entity, MimeType.APPLICATION_JSON.type(),new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    MessageResponse messsageResponse = new MessageResponse(response);
                    mCallBack.onSuccess(messsageResponse);
                    if(showProgressDialog)
                        progressBar.dismiss();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    //  Log.e("ERROR", t.getMessage());
                    JSONArray array = null;
                    String error_message = res;

                    KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                    if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                        JSONObject object = null;
                        try {
                            object = new JSONObject(res);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                        try {
                            array = new JSONArray(res);
                            JSONObject object = array.getJSONObject(0);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    mCallBack.onFailure(new KelasiApiException(error_message));

                    if(showProgressDialog)
                        progressBar.dismiss();
                }
            });
        }
        if(verb.equals(HttpVerb.DELETE)) {
            delete(url,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    MessageResponse messsageResponse = new MessageResponse(response);
                    mCallBack.onSuccess(messsageResponse);
                    if(showProgressDialog)
                        progressBar.dismiss();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    //  Log.e("ERROR", t.getMessage());
                    JSONArray array = null;
                    String error_message = res;

                    KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                    if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                        JSONObject object = null;
                        try {
                            object = new JSONObject(res);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                        try {
                            array = new JSONArray(res);
                            JSONObject object = array.getJSONObject(0);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    mCallBack.onFailure(new KelasiApiException(error_message));

                    if(showProgressDialog)
                        progressBar.dismiss();
                }
            });
        }
        if(verb.equals(HttpVerb.PUT)) {
            put(url,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    MessageResponse messsageResponse = new MessageResponse(response);
                    mCallBack.onSuccess(messsageResponse);
                    if(showProgressDialog)
                        progressBar.dismiss();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    //  Log.e("ERROR", t.getMessage());
                    JSONArray array = null;
                    String error_message = res;

                    KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                    if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                        JSONObject object = null;
                        try {
                            object = new JSONObject(res);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                        try {
                            array = new JSONArray(res);
                            JSONObject object = array.getJSONObject(0);
                            error_message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    mCallBack.onFailure(new KelasiApiException(error_message));

                    if(showProgressDialog)
                        progressBar.dismiss();
                }
            });
        }
    }

    public void NewMessages(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/new", params, HttpVerb.GET, null);



    }
    public void AllMessages(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/all", params, HttpVerb.GET, null);
    }
    public void SentMessages(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/sent", params, HttpVerb.GET, null);
    }

    public void SendReply(String access_token, Map message) throws UnsupportedEncodingException {
        JSONObject jsonParams  = new JSONObject(message);
        StringEntity entity = new StringEntity(jsonParams.toString());
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/reply?access-token="+access_token,null, HttpVerb.POST, entity);

    }
    public void SendMessage( String access_token, Map message) throws UnsupportedEncodingException {
        JSONObject jsonParams  = new JSONObject(message);
        StringEntity entity = new StringEntity(jsonParams.toString());
        Log.e("EP", KelasiApiConfiguration.END_POINT_URL+"notifications/send?access-token="+access_token);
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/send?access-token="+access_token,null, HttpVerb.POST, entity);


    }
    public void RemoveMessage(String access_token, String[] id){
        System.out.println(KelasiApiConfiguration.END_POINT_URL+"notifications/clean?access-token="+access_token+"&message_id="+ TextUtils.join(",",id));

        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/clean?access-token="+access_token+"&message_id="+ TextUtils.join(",",id),null, HttpVerb.DELETE, null);
    }
    public void MessageRead(String access_token,int message_id){
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"notifications/read?access-token="+access_token+"&message_id="+message_id,null, HttpVerb.PUT, null);
    }



}
