package com.infiniteloop.mytasks;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by theotherside on 23/03/15.
 */
public class ReminderService extends IntentService {

    private ReminderService mService;

    public ReminderService getInstance(){

        if(mService==null){
            mService=new ReminderService();
        }
        return mService;

    }
    public static final String TAG = ReminderService.class.getSimpleName();

    private ArrayList<Task> mTasksWithReminder= new ArrayList<Task>();

    public ReminderService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"Alarm");
    }

    public void setServiceAlarm(Context context){

        getAllTaskWithReminder();

            for(Task t:mTasksWithReminder){
                Intent i =new Intent(context,ReminderService.class);
                PendingIntent pi = PendingIntent.getService(context,0,i,0);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC,
                        t.getReminder(), pi);
            }


    }

    private ArrayList<Task> getAllTaskWithReminder() {
        //Load tasks
        TaskDataBaseHelper.TaskCursor cursor =TaskLab.get(this).queryTasks(1);

        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            Task t =cursor.getTask();
            mTasksWithReminder.add(t);
            cursor.moveToNext();
        }
        return mTasksWithReminder;
    }

}
