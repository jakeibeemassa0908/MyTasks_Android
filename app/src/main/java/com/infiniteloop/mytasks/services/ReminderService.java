package com.infiniteloop.mytasks.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.activities.AlarmActivity;
import com.infiniteloop.mytasks.activities.TaskListActivity;
import com.infiniteloop.mytasks.data.Task;

import java.util.Random;

/**
 * Created by theotherside on 23/03/15.
 */
public class ReminderService extends IntentService {

    public static final String TAG = ReminderService.class.getSimpleName();
    public static final String EXTRA_NOTIF="Notiication";
    public static final String ACTION_SHOW_NOTIFICATION=
            "com.infiniteloop.taskr.SHOW_NOTIFICATION";

    public static final String PERM_PRIVATE="" +
            "com.infiniteloop.mytasks.PRIVATE";

    public static final String EXTRA_ALARM_TASK="AlarmTask";

    // Start without a delay
    // Vibrate for 100 milliseconds
    // Sleep for 1000 milliseconds
    private long[] vibrationPattern = {100,100};


    public ReminderService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Task task = intent.getParcelableExtra(EXTRA_NOTIF);

        Intent notificationIntent = new Intent(this,AlarmActivity.class);
        notificationIntent.putExtra(EXTRA_ALARM_TASK,task);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pi = PendingIntent
                .getActivity(this, new Random().nextInt(), notificationIntent, 0);



        String title = task.getTitle();

        //get default notification sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        //Set Notification
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Alarm")
                .setColor(getResources().getColor(R.color.sunshine_blue))
                .setSmallIcon(R.drawable.ic_stat_t)
                .setContentTitle("New Alarm")
                .setContentText(title)
                .setContentIntent(pi)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(alarmSound)
                .setVibrate(vibrationPattern)
                .setAutoCancel(true)
                .build();


        showBackroundNotification(0,notification,task);
    }

    public static void activateServiceAlarm(Context context,Task t,boolean activate){

        Intent i =new Intent(context,ReminderService.class);
        i.putExtra(EXTRA_NOTIF,t);
        PendingIntent pi = PendingIntent.getService(context,(int)t.getId(),i,0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                if(activate){
                    alarmManager.set(AlarmManager.RTC,
                            t.getReminder(), pi);
                }else{
                    alarmManager.cancel(pi);
                    pi.cancel();
                }
    }

    void showBackroundNotification(int requestCode,Notification notification,Task t){
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra("REQUEST_CODE",requestCode);
        i.putExtra("NOTIFICATION",notification);
        i.putExtra("TASK",t);

        sendOrderedBroadcast(i,PERM_PRIVATE,null,null, Activity.RESULT_OK,null,null);
    }

}