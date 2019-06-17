package pro.novatech.solutions.app.cicole.fragment.notifications.service;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import org.json.JSONException;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import pro.novatech.solutions.app.cicole.MainActivity;
import pro.novatech.solutions.app.cicole.helper.ApplicationGlobal;
import pro.novatech.solutions.app.cicole.helper.DeviceUtils;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;


/**
 * Created by p.lukengu on 5/30/2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class NotificationJob extends JobService {

    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return true;
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

                        try {
                             ShortcutBadger.applyCountOrThrow(ApplicationGlobal.getContext(), badgeCount);

                        } catch (ShortcutBadgeException e) {
                            e.printStackTrace();
                        }

                        for (MessageResponse messageResponse : ((MessageResponse) object).getMessageList()) {
                            // if (!holder.contains(messageResponse)) {
                            createNotification(messageResponse);
                            //  holder.add(messageResponse);
                            //}
                        }

                    }

                    @Override
                    public void onFailure(KelasiApiException e) {

                    }
                }, false).NewMessages(user.getAuth_key());
            }




            jobFinished( (JobParameters) msg.obj, true );


            return true;
        }

    });


    private void createNotification(MessageResponse messageResponse){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageResponse.getSubject())
                        .setContentText(messageResponse.getMessage())
                        .setSound(soundUri)
                        .setAutoCancel(true);

        Bundle bundle = new Bundle();
        bundle.putInt("menu", R.id.nav_notification);
        bundle.putInt("selected_tab",0);
        bundle.putSerializable("message",messageResponse);

         Intent resultIntent = new Intent(this, MainActivity.class)
                 .putExtras(bundle);


// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
    //    stackBuilder.addParentStack(ResultActivity.class);
// Adds the Intent that starts the Activity to the top of the stack

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(messageResponse.getMessage_id(), mBuilder.build());
    }
}
