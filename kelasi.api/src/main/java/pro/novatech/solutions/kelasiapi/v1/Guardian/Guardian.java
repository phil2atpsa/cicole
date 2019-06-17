package pro.novatech.solutions.kelasiapi.v1.Guardian;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pro.novatech.solutions.kelasiapi.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiConfiguration;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiUtils;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.MimeType;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/2/2017.
 */

public class Guardian  extends AsyncHttpClient{

    private OnServiceResponseListener<Object> mCallBack;
    private Context mContext;
    private ProgressDialog progressBar;
    private boolean  showProgressDialog;

    public Guardian(Context context, OnServiceResponseListener callback, boolean showProgressDialog) {
        mCallBack = callback;
        mContext = context;
        this.showProgressDialog = showProgressDialog;
        addHeader("Accept", MimeType.APPLICATION_JSON.type());
        if(this.showProgressDialog) {
            progressBar = new ProgressDialog(mContext, R.style.TransparentProgressDialog);
            progressBar.setMessage("");
            progressBar.setIndeterminate(true);
        }
    }

    public void Correspondents(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        get(KelasiApiConfiguration.END_POINT_URL+"guardian/correspondants",params, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray users) {
                // Pull out the first event on the public timeline
                UserAccessResponse userAccessResponse = new UserAccessResponse(users);
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
                if(showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }

    public void Students(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        get(KelasiApiConfiguration.END_POINT_URL+"guardian/students",params, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray students) {
                // Pull out the first event on the public timeline
               StudentResponse studentResponse = new StudentResponse(students);
                mCallBack.onSuccess(studentResponse);

                if(showProgressDialog)
                    progressBar.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //  Log.e("ERROR", t.getMessage());
                //

                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                if( type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if( (type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY))){
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
                if(showProgressDialog)
                    progressBar.dismiss();
            }
        });



    }

}
