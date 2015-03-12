package com.infiniteloop.mytasks;

import android.content.Context;
import android.util.Log;

import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.util.ArrayList;
/**
 * Created by theotherside on 07/03/15.
 */
public class TaskLab {

    private static TaskLab sTaskLab;
    private ArrayList<Task> mTasks;
    private TaskDataBaseHelper mHelper;

    private TaskLab(Context context){

        mTasks= new ArrayList<Task>();
        mHelper= new TaskDataBaseHelper(context);
    }

    public static TaskLab get(Context c){
        if(sTaskLab == null){
            sTaskLab= new TaskLab(c);
        }
        return sTaskLab;
    }

    public ArrayList<Task> getTasks(){

        return mTasks;
    }

    public boolean createTask(Context context,String title,String priority_s,long category,int durationHours,int durationMinutes){
            Log.d(NewTaskFragment.TAG,"Entered");
            int priority=getPriority(context,priority_s);
            if(priority!=-1){
                Task t = new Task(title,priority,category,durationHours,durationMinutes);
                long taskId=saveTask(t);
                mTasks.add(t);
                return true;
            }
        return false;
    }

    public long saveTask(Task t){
        return mHelper.insertTask(t);
    }

    public TaskDataBaseHelper.TaskCursor queryTasks(){
        return mHelper.queryTasks();
    }

    public Task getTask(long taskId){
        /* TODO
         * get task from id
          * */
        return null;
    }

    private int getPriority(Context c, String priority){
        if(priority.equals(c.getString(R.string.normal))){
            return Task.NORMAL_PRIORITY;
        }else if(priority.equals(c.getString(R.string.low))){
            return Task.LOW_PRIORITY;
        }else if(priority.equals(c.getString(R.string.high))){
            return Task.HIGH_PRIORITY;
        }else if(priority.equals(c.getString(R.string.very_high))){
            return Task.VERY_HIGH_PRIORITY;
        }else{
            return -1;
        }
    }
}
