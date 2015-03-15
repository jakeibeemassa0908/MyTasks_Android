package com.infiniteloop.mytasks.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.infiniteloop.mytasks.Task;
import com.infiniteloop.mytasks.data.TaskContract.TaskEntry;
import com.infiniteloop.mytasks.data.TaskContract.CategoryEntry;

import java.util.Date;

/**
 * Created by theotherside on 11/03/15.
 */
public class TaskDataBaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=1;
    static final String DATABASE_NAME="task.db";

    public TaskDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TASK_TABLE="CREATE TABLE "+ TaskEntry.TABLE_NAME + " ("+
                TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TaskEntry.COLUMN_CAT_KEY + " INTEGER NOT NULL, "+
                TaskEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
                TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL, "+
                TaskEntry.COLUMN_TASK_TITLE + " TEXT NOT NULL);";
//
//                //set up the location category column as a foreign key to location table.
//                "FOREIGN KEY (" + TaskEntry.COLUMN_CAT_KEY + ") REFERENCES "+
//                CategoryEntry.TABLE_NAME + " ("+ CategoryEntry._ID + ");";



        final String SQL_CREATE_CATEGORY_TABLE="CREATE TABLE "+ CategoryEntry.TABLE_NAME + " ("+
                CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TASK_TABLE);
        //db.execSQL(SQL_CREATE_CATEGORY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertTask(Task task){
        ContentValues cv = new ContentValues();
        cv.put(TaskEntry.COLUMN_TASK_TITLE,task.getTitle());
        cv.put(TaskEntry.COLUMN_DATE,task.getCreationDate());
        cv.put(TaskEntry.COLUMN_PRIORITY,task.getPriority());
        cv.put(TaskEntry.COLUMN_CAT_KEY,task.getCategory());
        return getWritableDatabase().insert(TaskEntry.TABLE_NAME,null,cv);
    }

    public int deleteTask(Task task){
        long id = task.getId();
        if(id==-1) return -1;
        String selection = TaskEntry._ID + " LIKE ?";
        String[] selectionArgs= {String.valueOf(id)};
        return getWritableDatabase().delete(TaskEntry.TABLE_NAME,selection,selectionArgs);
    }

    public int updateTask(Task task){
        ContentValues cv= new ContentValues();
        cv.put(TaskEntry.COLUMN_TASK_TITLE,task.getTitle());
        cv.put(TaskEntry.COLUMN_DATE,task.getCreationDate());
        cv.put(TaskEntry.COLUMN_PRIORITY,task.getPriority());
        cv.put(TaskEntry.COLUMN_CAT_KEY,task.getCategory());

        String selection = TaskEntry._ID + " LIKE ?";
        String[] selectionArgs={String.valueOf(task.getId())};

        int count = getReadableDatabase().update(
                TaskEntry.TABLE_NAME,
                cv,
                selection,
                selectionArgs
        );
        return count;
    }


    public TaskCursor queryTasks(int queryCode){
        String selection;
        String[] selectionArgs;
        switch (queryCode){
            case 2:
                selection=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                selectionArgs= new String[]{String.valueOf(Task.VERY_HIGH_PRIORITY)};
                break;
            case 3:
                selection=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                selectionArgs= new String[]{String.valueOf(Task.HIGH_PRIORITY)};
                break;
            case 4:
                selection=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                selectionArgs= new String[]{String.valueOf(Task.NORMAL_PRIORITY)};
                break;
            case 5:
                selection=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                selectionArgs= new String[]{String.valueOf(Task.LOW_PRIORITY)};
                break;
            default:
                selection=null;
                selectionArgs=null;


        }
        //Equivalent of select * from task order by priority asc
        Cursor wrapped = getReadableDatabase().query(
                TaskEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                TaskEntry.COLUMN_DATE + " asc");
        return new TaskCursor(wrapped);
    }

    public TaskCursor queryTask(long rowId){
        String selection =TaskEntry._ID + " LIKE ?";
        String [] selectionArgs={String.valueOf(rowId)};
        Cursor wrapped = getReadableDatabase().query(TaskEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null,
            "1");
        return new TaskCursor(wrapped);
    }


    public static class TaskCursor extends CursorWrapper{
        public TaskCursor(Cursor c){
            super(c);

        }

        public Task getTask(){
            if(isBeforeFirst() || isAfterLast()) return null;
            Task task = new Task();
            task.setId(getLong(getColumnIndex(TaskEntry._ID)));
            task.setCategory(getLong(getColumnIndex(TaskEntry.COLUMN_CAT_KEY)));
            task.setCreationDate(new Date(getLong(getColumnIndex(TaskEntry.COLUMN_DATE))));
            task.setPriority(getInt(getColumnIndex(TaskEntry.COLUMN_PRIORITY)));
            task.setTitle(getString(getColumnIndex(TaskEntry.COLUMN_TASK_TITLE)));
            return task;
        }

    }
}