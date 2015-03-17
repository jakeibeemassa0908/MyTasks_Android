package com.infiniteloop.mytasks.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.infiniteloop.mytasks.Task;
import com.infiniteloop.mytasks.data.TaskContract.TaskEntry;
import com.infiniteloop.mytasks.data.TaskContract.CategoryEntry;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 11/03/15.
 */
public class TaskDataBaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=1;
    static final String DATABASE_NAME="task.db";

    public static final String TAG= TaskDataBaseHelper.class.getSimpleName();
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
                TaskEntry.COLUMN_TASK_TITLE + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_COMPLETED + " INTEGER NOT NULL);";
//
//                //set up the location category column as a foreign key to location table.
//                "FOREIGN KEY (" + TaskEntry.COLUMN_CAT_KEY + ") REFERENCES "+
//                CategoryEntry.TABLE_NAME + " ("+ CategoryEntry._ID + ");";



        final String SQL_CREATE_CATEGORY_TABLE="CREATE TABLE "+ CategoryEntry.TABLE_NAME + " ("+
                CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);


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
        cv.put(TaskEntry.COLUMN_COMPLETED,task.isCompleted());
        return getWritableDatabase().insert(TaskEntry.TABLE_NAME,null,cv);
    }

    public long insertCategory(String category) {
        Category storedCat =queryCategoryName(category);
        if (storedCat!=null){
            if(category.equals(storedCat.getCategoryName())){
                return storedCat.getId();
            }
        }
        ContentValues cv = new ContentValues();
        cv.put(CategoryEntry.COLUMN_NAME,category);
        return getWritableDatabase().insert(CategoryEntry.TABLE_NAME,null,cv);
    }

    private Category queryCategoryName(String category) {
        String selection =CategoryEntry.COLUMN_NAME + " LIKE ?";
        String [] selectionArgs={String.valueOf(category)};
        Cursor wrapped = getReadableDatabase().query(CategoryEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1");
        wrapped.moveToFirst();
        return new CategoryCursor(wrapped).getCategory();
    }

    public Category queryCategoryName(long id) {
        String selection =CategoryEntry._ID + " = ?";
        String [] selectionArgs={String.valueOf(id)};
        Cursor wrapped = getReadableDatabase().query(CategoryEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
               null);
        wrapped.moveToFirst();
        return new CategoryCursor(wrapped).getCategory();
    }

    public int deleteTask(Task task){
        long id = task.getId();
        if(id==-1) return -1;
        String selection = TaskEntry._ID + " LIKE ?";
        String[] selectionArgs= {String.valueOf(id)};
        return getWritableDatabase().delete(TaskEntry.TABLE_NAME,selection,selectionArgs);
    }

    public int updateTask(Task task,boolean completed){
        ContentValues cv= new ContentValues();
        cv.put(TaskEntry.COLUMN_TASK_TITLE,task.getTitle());
        cv.put(TaskEntry.COLUMN_DATE,task.getCreationDate());
        cv.put(TaskEntry.COLUMN_PRIORITY,task.getPriority());
        cv.put(TaskEntry.COLUMN_CAT_KEY,task.getCategory());
        cv.put(TaskEntry.COLUMN_COMPLETED,completed?1:0);

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
        String selection=TaskEntry.COLUMN_COMPLETED +" LIKE ? AND ";
        ArrayList<String>array= new ArrayList<String>();
        array.add(String.valueOf(0));
        String[] selectionArgs;
        switch (queryCode){
            case 2:
                selection+=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                array.add(String.valueOf(Task.VERY_HIGH_PRIORITY));
                break;
            case 3:
                selection+=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                array.add(String.valueOf(Task.HIGH_PRIORITY));
                break;
            case 4:
                selection+=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                array.add(String.valueOf(Task.NORMAL_PRIORITY));
                break;
            case 5:
                selection+=TaskEntry.COLUMN_PRIORITY + " LIKE ?";
                array.add(String.valueOf(Task.LOW_PRIORITY));
                break;
            default:
                selection=null;
                array=null;


        }
        if(array==null){
            selectionArgs=null;
            selection=null;
        }
        else{
            selectionArgs=new String[array.size()];
            selectionArgs=array.toArray(selectionArgs);
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
            task.setCompleted(getInt(getColumnIndex(TaskEntry.COLUMN_COMPLETED))!=0);
            return task;
        }

    }

    public static class CategoryCursor extends CursorWrapper{
        public CategoryCursor(Cursor c){
            super(c);

        }

        public Category getCategory(){
            if(isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Category category = new Category();
            category.setCategoryName(getString(getColumnIndex(CategoryEntry.COLUMN_NAME)));
            category.setId(getLong(getColumnIndex(CategoryEntry._ID)));
            return category;
        }

    }
}