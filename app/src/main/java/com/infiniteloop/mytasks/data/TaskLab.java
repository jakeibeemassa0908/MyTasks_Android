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
                if(t.getReminder()!=-1){
                    ReminderService.activateServiceAlarm(context, t, true);
                }

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
        if(task.getReminder()!=-1){
            ReminderService.activateServiceAlarm(context,task,false);
        }
        return result !=-1;
    }

    public void setComplete(Task task){
        mHelper.updateTask(task,true);
    }


    public boolean editTask(Task task){
        int rowCount =mHelper.updateTask(task,false);
        if( rowCount==1) return true;
        return false;
    }

    public String queryCatName(long id){
        Category cat = mHelper.queryCategoryName(id);
        if(cat!=null)
            return cat.getCategoryName();
        else
            return null;
    }

    public long insertCategory(String catName){
        return mHelper.insertCategory(catName);
    }

    public boolean deleteCategory(long id){
        return mHelper.deleteCategory(id)!=-1;
    }


    public TaskDataBaseHelper.CategoryCursor getCategories() {
        return (TaskDataBaseHelper.CategoryCursor)mHelper.queryCategories();
    }
}
