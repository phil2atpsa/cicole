package pro.novatech.solutions.kelasiapi.v1.Institution;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pro.novatech.solutions.kelasiapi.R;
import pro.novatech.solutions.kelasiapi.v1.Events.EventResponse;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiConfiguration;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiUtils;
import pro.novatech.solutions.kelasiapi.v1.MimeType;
import pro.novatech.solutions.kelasiapi.v1.News.NewsResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

/**
 * Created by p.lukengu on 4/11/2017.
 */

public class Institution extends AsyncHttpClient {

    private OnServiceResponseListener<Object> mCallBack;
    private Context mContext;
    private ProgressDialog progressBar;
    private boolean  showProgressDialog;

    public Institution(Context context, OnServiceResponseListener callback, boolean showProgressDialog) {
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

    public void News(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);

        get(KelasiApiConfiguration.END_POINT_URL+"institution/news", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                NewsResponse newsResponse = new NewsResponse(response);
                mCallBack.onSuccess(newsResponse);
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

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });
    }

    public void Events(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);

        get(KelasiApiConfiguration.END_POINT_URL+"institution/events", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                EventResponse eventResponse = new EventResponse(response);
                mCallBack.onSuccess(eventResponse);
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

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }
}
