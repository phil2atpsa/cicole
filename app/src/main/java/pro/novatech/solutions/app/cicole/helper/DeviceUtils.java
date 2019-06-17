package pro.novatech.solutions.app.cicole.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by p.lukengu on 4/9/2017.
 */

public class DeviceUtils {

    private  int responseCode;

    private Context context = ApplicationGlobal.getContext();
    private static final DeviceUtils INSTANCE  = new DeviceUtils();

    public static DeviceUtils getInstance() {
        return INSTANCE ;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    public boolean DeviceIsConnected() {
        return isNetworkAvailable();


        /*if (isNetworkAvailable()) {
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.addHeader("User-Agent", "Test");
            httpClient.addHeader("Connection", "close");
            httpClient.setTimeout(1500);

            httpClient.get(context,"http://www.google.com", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    responseCode = statusCode;
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    responseCode = statusCode;

                }
            });
            return (responseCode == 200);
        }
        return false*/
    }

}

