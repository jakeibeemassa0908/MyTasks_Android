package com.infiniteloop.mytasks.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.services.ReminderService;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 07/03/15.
 */

/**
 * Class that handles all the database query coming from the UI,
 * It is the interface between the Database methods and the UI calls
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

    /**
     * Create a new task
     * @param context
     * @param title
     * @param priority_s
     * @param category_s
     * @param reminderDate
     * @return true or false whether the task as been successfully added to the database or not
     */
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

    /**
     * save the task to the database
     * @param t
     * @return
     */
    private long saveTask(Task t){
        return mHelper.insertTask(t);
    }

    /**
     * Query list of tasks
     * @param queryCode selection code corresponding to item on the navigation drawer
     * @return a list of task matching the query code
     */
    public TaskDataBaseHelper.TaskCursor queryTasks(int queryCode){
        return mHelper.queryTasks(queryCode);
    }

    /**
     * Query a single task
     * @param id ID of the task to be queried
     * @return
     */
    public Task queryTask(long id){
        Task task = null;
        TaskDataBaseHelper.TaskCursor cursor = mHelper.queryTask(id);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            task = cursor.getTask();
        cursor.close();
        return task;
    }

    /**
     * Remove a task
     * @param context
     * @param task
     * @return whether or not the task has been successfully deleted
     */
    public boolean removeTask(Context context,Task task){
        int result =mHelper.deleteTask(task);
        setTaskAlarm(task, context, false);
        return result !=-1;
    }

    /**
     * Set a task as completed
     * @param task
     * @param context
     */
    public void setComplete(Task task,Context context){
        mHelper.updateTask(task,true);
        setTaskAlarm(task,context,false);
    }

    /**
     * Edit Task informations
     * @param newTask new task to be saved
     * @param context
     * @return
     */
    public boolean editTask(Task newTask,Context context){
        Task oldTask=queryTask(newTask.getId());

        //Remove the Alarm reminder on the old task
        setTaskAlarm(oldTask,context,false);
        //Set the Reminder for the newTask
        setTaskAlarm(newTask,context,true);

        int rowCount =mHelper.updateTask(newTask,false);
        return rowCount==1;
    }

    /**
     * Get single category from db given its ID
     * @param id
     * @return
     */
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

    /**
     * Delete single category from databse
     * @param id
     * @return
     */
    public boolean deleteCategory(long id){
        return mHelper.deleteCategory(id)!=-1;
    }

    /**
     * Edit the category name
     * @param id id of the category that needs to be edited
     * @param name the new category name
     * @return
     */
    public boolean editCat(long id, String name){
        int rowCount = mHelper.editCategory(id,name);
        return rowCount==1;
    }

    public Cursor getCategoryCursor(){
        return mHelper.queryCategories();
    }

    /**
     * return all categories name
     * @return
     */
    public ArrayList<Category> getCategories(){
        ArrayList<Category> categories = new ArrayList<Category>();
        TaskDataBaseHelper.CategoryCursor categoryCursor = (TaskDataBaseHelper.CategoryCursor)mHelper.queryCategories();
        if(categoryCursor!=null){
            categoryCursor.moveToFirst();
            for(int i=0;i<categoryCursor.getCount();i++){
                categories.add(categoryCursor.getCategory());
                categoryCursor.moveToNext();
            }
        }
        return categories;
    }

    /**
     * Set Task Alarm on or off
     * @param task the task whose alarm need to be set
     * @param context the application context
     * @param onOrOff the value of the switch, true =on ; false =off
     */
    public void setTaskAlarm(Task task,Context context,boolean onOrOff){
        if(task.getReminder()!=-1){
            activateServiceAlarm(context,task,onOrOff);
        }
    }

    /**
     * Set the alarm by creating a pending Intent that will trigger the service
     * @param context
     * @param t
     * @param activate
     */
    public void activateServiceAlarm(Context context,Task t,boolean activate){

        Intent i =new Intent(context,ReminderService.class);
        i.putExtra(ReminderService.EXTRA_NOTIF,t);
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

    //--------NOTES FUNCTIONS------

    /**
     * Create new note
     * @param note
     * @return
     */
    public long createNote(Note note){
        note.setCreationDate(new Date());
        note.setLastEdit(new Date());
        return mHelper.insertNote(note);
    }

    /**
     * Query all notes belonging to a certain task
     * @param taskId the tak id that contains the notes
     * @return
     */
    public TaskDataBaseHelper.NoteCursor queryNotes(long taskId){
        return mHelper.queryNotes(taskId);
    }

    public long updateNote(Note note){
        return mHelper.updateNote(note);
    }

    //--------CHECKLIST  FUNCTIONS------

    public long createCheckList(CheckList checkList){
        //insert checklist
        long checklistId=mHelper.insertChecklist(checkList);
        //retrieve the id and insert the checklist item
        if(checklistId !=-1)
            return mHelper.insertChecklistItems(checkList.getChecklistItems(),checklistId);
        return 0;
    }

    public TaskDataBaseHelper.ChecklistCursor queryChecklist(long taskId){
        return mHelper.queryChecklist(taskId);
    }

    public ArrayList<CheckListItem> getChecklistItems(long checklistId){
        return mHelper.getChecklistItems(checklistId);
    }

    public int updateCheckList(CheckList checkList){
         int count=0;
         count +=mHelper.updateCheckList(checkList);
         count +=mHelper.updateCheckListItem(checkList);
         return count;

    }

    //--------PHOTO  FUNCTIONS------

    public long insertPhoto(Photo photo){
        return mHelper.insertPhoto(photo);
    }

    public TaskDataBaseHelper.PhotoCursor getPhotos(long taskId){
        return mHelper.getPhotos(taskId);
    }
}
