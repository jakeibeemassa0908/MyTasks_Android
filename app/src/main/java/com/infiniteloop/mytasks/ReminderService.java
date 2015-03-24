package com.infiniteloop.mytasks;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

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
        Log.d(TAG,cursor.getCount()+"");
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            Task t =cursor.getTask();
            Log.d(TAG,t.getReminder()+" Reminder");
            cursor.moveToNext();
        }

    }

}
