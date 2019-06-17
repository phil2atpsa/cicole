package pro.novatech.solutions.kelasiapi.v1.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pro.novatech.solutions.kelasiapi.R;
import pro.novatech.solutions.kelasiapi.v1.HttpVerb;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiConfiguration;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiUtils;
import pro.novatech.solutions.kelasiapi.v1.MimeType;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;


/**
 * Created by p.lukengu on 4/1/2017.
 */

public class UserAccess extends AsyncHttpClient {

    private OnServiceResponseListener<Object> mCallBack;

    private Context mContext;
    private ProgressDialog progressBar;
    private boolean  showProgressDialog;



    public UserAccess(Context context, OnServiceResponseListener callback, boolean showProgressDialog) {
        mCallBack = callback;
        mContext = context;
        this.showProgressDialog = showProgressDialog;
        addHeader("Accept", MimeType.APPLICATION_JSON.type());
        if(this.showProgressDialog) {
            progressBar = new ProgressDialog(mContext);

            progressBar.setIndeterminate(true);
        }
    }

    private void sendRequest(final String url, RequestParams params, HttpVerb verb) {
       if(verb.equals(HttpVerb.GET)){
           progressBar.setMessage(mContext.getString(R.string.auth_attempt));
           progressBar.show();
           get(url,params, new JsonHttpResponseHandler(){
               @Override
               public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                   // If the response is JSONObject instead of expected JSONArray
                   Log.i("url ",url);
                   Log.i("Message ",response.toString());
                   UserAccessResponse userAccessResponse = new UserAccessResponse(response);
                   mCallBack.onSuccess(userAccessResponse);
                   if(showProgressDialog)
                       progressBar.dismiss();
               }


               @Override
               public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                   mCallBack.onFailure(new KelasiApiException(t.getMessage()));
                   if(showProgressDialog)
                       progressBar.dismiss();
               }
           });
       }
        if(verb.equals(HttpVerb.POST)){
            progressBar.setMessage(mContext.getString(R.string.login_attempt));
            progressBar.show();
            post(url,params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i("url ",url);
                    Log.i("Message ",response.toString());
                    UserAccessResponse userAccessResponse = new UserAccessResponse(response);
                    mCallBack.onSuccess(userAccessResponse);
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

                    mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                    if(showProgressDialog)
                        progressBar.dismiss();
                }
            });
        }


    }

    public void Authorize(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        sendRequest(KelasiApiConfiguration.END_POINT_URL+"access/auth",params, HttpVerb.GET);


    }


    public void Student(String access_token){

        if((showProgressDialog)){
            progressBar.setMessage(mContext.getString(R.string.please_wait));
            progressBar.show();
        }
        RequestParams params = new RequestParams("access-token", access_token);
        get(KelasiApiConfiguration.END_POINT_URL+"access/student",params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.i("Message ",response.toString());
                StudentResponse studentResponse = new StudentResponse(response);
                mCallBack.onSuccess(studentResponse);
                if(showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                mCallBack.onFailure(new KelasiApiException(t.getMessage()));
                if(showProgressDialog)
                    progressBar.dismiss();
            }
        });
    }

    public void Login(String username, String password){
        addHeader("Content-Type" ,"application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        if(showProgressDialog)
            progressBar.show();

        sendRequest(KelasiApiConfiguration.END_POINT_URL+"access/login",params, HttpVerb.POST);

    }

    

}
