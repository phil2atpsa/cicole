package pro.novatech.solutions.app.cicole.fragment.notifications.service;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

import pro.novatech.solutions.app.cicole.fragment.notifications.callbacks.OnMessageCountListener;
import pro.novatech.solutions.app.cicole.helper.ApplicationGlobal;
import pro.novatech.solutions.app.cicole.helper.DeviceUtils;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

/**
 * Created by p.lukengu on 4/11/2017.
 */

public class IntentScheduler  extends Timer {

    private OnMessageCountListener messageCountListener;

    public IntentScheduler(OnMessageCountListener messageCountListener ){
        this.messageCountListener = messageCountListener;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(final Message msg) {

            DeviceUtils du = DeviceUtils.getInstance();
            MyPreferenceManager myPreferenceManager = new MyPreferenceManager(ApplicationGlobal.getContext());

            if(du.DeviceIsConnected() && myPreferenceManager.isUserSession()){
                UserAccessResponse user = null;
                try {
                    user = myPreferenceManager.getUserSession();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new pro.novatech.solutions.kelasiapi.v1.Notification.Message(ApplicationGlobal.getContext(), new OnServiceResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        int badgeCount = ((MessageResponse) object).getMessageList().size();
                        messageCountListener.OnMessageCount(badgeCount);
                    }

                    @Override
                    public void onFailure(KelasiApiException e) {

                    }
                }, false).NewMessages(user.getAuth_key());
            }







            return true;
        }

    });

    public void scheduleMessageCount(){
        schedule(new TimerTask() {
            @Override
            public void run() {
                mJobHandler.sendEmptyMessage(0);

            }
        },1000, 9000);
    }


}
