package com.infiniteloop.mytasks;

import android.content.Context;

import java.util.ArrayList;
/**
 * Created by theotherside on 07/03/15.
 */
public class TaskLab {

    private static TaskLab sTaskLab;
    private ArrayList<Task> mTasks;

    private TaskLab(){
        mTasks= new ArrayList<Task>();
    }

    public static TaskLab get(){
        if(sTaskLab == null){
            sTaskLab= new TaskLab();
        }
        return sTaskLab;
    }

    public ArrayList<Task> getTasks(){

        return mTasks;
    }

    public boolean createTask(Context context,String title,String priority_s,String category, String visibility){
        if(!title.matches("")){
            int priority=getPriority(context,priority_s);
            if(priority!=-1){
                Task t = new Task(title,priority,category,visibility);
                mTasks.add(t);
                return true;
            }
        }
        return false;
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
