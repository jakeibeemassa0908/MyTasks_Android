package com.infiniteloop.mytasks.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.data.TaskLab;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 20/04/15.
 */
public class RestartAlarmService extends IntentService {

    private static final String TAG = RestartAlarmService.class.getSimpleName();
    private TaskLab mTaskLab;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * TAG is Used to name the worker thread, important only for debugging.
     */
    public RestartAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mTaskLab = TaskLab.get(this);

        //Query all task with reminder
        ArrayList<Task> taskList = new ArrayList<Task>();
        TaskDataBaseHelper.TaskCursor taskCursor=mTaskLab.queryTasks(1);

        if(taskCursor.getCount()>0){
            taskCursor.moveToFirst();
            for (int i=0;i<taskCursor.getCount();i++){
                taskList.add(taskCursor.getTask());
                taskCursor.moveToNext();
            }
        }

        //Set reminder for all task with reminders that are not overdue
        for(int i=0;i<taskList.size();i++){
            Task taskToResetAlarm =taskList.get(i);
            if(taskToResetAlarm.getReminder() > new Date().getTime()){
                   mTaskLab.setTaskAlarm(taskToResetAlarm,this,true);
            }
        }
    }
}
