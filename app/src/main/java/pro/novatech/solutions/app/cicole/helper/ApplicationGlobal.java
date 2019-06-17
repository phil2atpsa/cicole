package pro.novatech.solutions.app.cicole.helper;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import pro.novatech.solutions.app.cicole.fragment.notifications.service.NotificationJob;

public class ApplicationGlobal extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getPackageName(), NotificationJob.class.getName()));
        builder.setPeriodic(9000);
        builder.setPersisted(false);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

            if (mJobScheduler.schedule(builder.build()) <= JobScheduler.RESULT_FAILURE) {
                    //If something goes wrong
                    System.out.println("If something goes wrong");
            }
        }


    }

	public static Context getContext() {
		return context;
	}

}
