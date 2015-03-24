package com.infiniteloop.mytasks;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by theotherside on 23/03/15.
 */
public class ReminderService extends IntentService {
    public static final String TAG = ReminderService.class.getSimpleName();

    public ReminderService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Load tasks
        TaskDataBaseHelper.TaskCursor cursor =TaskLab.get(this).queryTasks(1);

        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            Task t =cursor.getTask();
            Log.d(TAG,"Time is "+ DateFormat.getTimeInstance().format(new Date(t.getReminder())));
            cursor.moveToNext();
        }

    }

    public static void setServiceAlarm(Context context){
        Intent i =new Intent(context,ReminderService.class);
        PendingIntent pi = PendingIntent.getService(context,0,i,0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),1000*30,pi);

        alarmManager.cancel(pi);
        pi.cancel();
    }

}
