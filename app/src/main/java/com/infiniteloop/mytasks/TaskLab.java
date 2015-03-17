package com.infiniteloop.mytasks;

import android.content.Context;
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

    public boolean  createTask(Context context,String title,String priority_s,String category_s){
            int priority=Helpers.getPriority(context,priority_s);
            if(priority!=-1){
                long category=mHelper.insertCategory(category_s);
                Task t = new Task(title,priority,category);
                long taskId=saveTask(t);
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

    public boolean removeTask(Task task){
        int result =mHelper.deleteTask(task);
        if(result==-1)return false;
        return true;
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
        return mHelper.queryCategoryName(id).getCategoryName();
    }

    public TaskDataBaseHelper.CategoryCursor getCategories() {
        return (TaskDataBaseHelper.CategoryCursor)mHelper.queryCategories();
    }
}
