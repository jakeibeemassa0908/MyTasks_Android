package com.infiniteloop.mytasks.data;

import android.content.Context;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.services.ReminderService;

import java.util.ArrayList;
import java.util.Date;

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

    public boolean  createTask(Context context,String title,String priority_s,String category_s,Date reminderDate){
            int priority= Helpers.getPriority(context, priority_s);
            if(priority!=-1){
                long category=mHelper.insertCategory(category_s);
                Task t = new Task(title,priority,category);
                t.setReminder(reminderDate);
                saveTask(t);

                //SetReminder if reminder is set
                setTaskAlarm(t,context,true);
                mTasks.add(t);
                return true;
            }
        return false;
    }

    public long saveTask(Task t){
        return mHelper.insertTask(t);
    }

    public TaskDataBaseHelper.TaskCursor queryTasks(int queryCode){
        return mHelper.queryTasks(queryCode);
    }

    public Task queryTask(long id){
        Task task = null;
        TaskDataBaseHelper.TaskCursor cursor = mHelper.queryTask(id);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            task = cursor.getTask();
        cursor.close();
        return task;
    }

    public boolean removeTask(Context context,Task task){
        int result =mHelper.deleteTask(task);
        setTaskAlarm(task,context,false);
        return result !=-1;
    }

    public void setComplete(Task task,Context context){
        mHelper.updateTask(task,true);
        setTaskAlarm(task,context,false);
    }


    public boolean editTask(Task newTask,Context context){
        Task oldTask=queryTask(newTask.getId());

        //Remove the Alarm reminder on the old task
        setTaskAlarm(oldTask,context,false);
        //Set the Reminder for the newTask
        setTaskAlarm(newTask,context,true);

        int rowCount =mHelper.updateTask(newTask,false);
        return rowCount==1;
    }

    public Category queryCategory(long id){
        Category cat = mHelper.queryCategoryName(id);
        if(cat!=null)
            return cat;
        else
            return null;
    }

    public long insertCategory(String catName){
        return mHelper.insertCategory(catName);
    }

    public boolean deleteCategory(long id){
        return mHelper.deleteCategory(id)!=-1;
    }

    public boolean editCat(long id, String name){
        int rowCount = mHelper.editCategory(id,name);
        return rowCount==1;
    }


    public TaskDataBaseHelper.CategoryCursor getCategories() {
        return (TaskDataBaseHelper.CategoryCursor)mHelper.queryCategories();
    }

    /**
     * Remove the alarm pending on a Task
     * @param task
     * @param context
     */
    private void setTaskAlarm(Task task,Context context,boolean onOrOff){
        if(task.getReminder()!=-1){
            ReminderService.activateServiceAlarm(context,task,onOrOff);
        }
    }
}
