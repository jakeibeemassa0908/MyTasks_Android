package com.infiniteloop.mytasks.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.infiniteloop.mytasks.data.TaskContract.*;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 11/03/15.
 */
public class TaskDataBaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=2;
    static final String DATABASE_NAME="task.db";

    final String SQL_CREATE_TASK_TABLE="CREATE TABLE "+ TaskEntry.TABLE_NAME + " ("+
            TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TaskEntry.COLUMN_CAT_KEY + " INTEGER NOT NULL, "+
            TaskEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
            TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL, "+
            TaskEntry.COLUMN_TASK_TITLE + " TEXT NOT NULL, " +
            TaskEntry.COLUMN_REMINDER +" INTEGER NOT NULL, " +
            TaskEntry.COLUMN_COMPLETED + " INTEGER NOT NULL);";



    final String SQL_CREATE_CATEGORY_TABLE="CREATE TABLE "+ CategoryEntry.TABLE_NAME + " ("+
            CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CategoryEntry.COLUMN_NAME + " TEXT NOT NULL);";

    final String SQL_CREATE_NOTE_TABLE="CREATE TABLE "+ NoteEntry.TABLE_NAME + " ("+
            NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NoteEntry.COLUMN_TASK_KEY + " INTEGER NOT NULL, "+
            NoteEntry.COLUMN_CREATED_DATE + " INTEGER NOT NULL, "+
            NoteEntry.COLUMN_EDITED_DATE + " INTEGER NOT NULL, "+
            NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, "+
            NoteEntry.COLUMN_CONTENT +" TEXT NOT NULL);";

    final String SQL_CREATE_PHOTO_TABLE="CREATE TABLE "+ PhotoEntry.TABLE_NAME + " ("+
            PhotoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            PhotoEntry.COLUMN_TASK_KEY + " INTEGER NOT NULL, "+
            PhotoEntry.COLUMN_FILENAME + " TEXT NOT NULL);";

    final String SQL_CREATE_CHECKLIST_TABLE ="CREATE TABLE "+ CheckListEntry.TABLE_NAME + " ("+
            CheckListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CheckListEntry.COLUMN_CREATED_DATE + " INTEGER NOT NULL, "+
            CheckListEntry.COLUMN_EDITED_DATE + " INTEGER NOT NULL, "+
            CheckListEntry.COLUMN_TASK_KEY + " INTEGER NOT NULL, "+
            CheckListEntry.COLUMN_TITLE + " TEXT NOT NULL);";

    final String SQL_CREATE_CHECKLISTITEM_TABLE = "CREATE TABLE "+ CheckListItemEntry.TABLE_NAME + " ("+
            CheckListItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CheckListItemEntry.COLUMN_CHECKLIST_KEY + " INTEGER NOT NULL, "+
            CheckListItemEntry.COLUMN_ITEM + " TEXT NOT NULL, "+
            CheckListItemEntry.COLUMN_COMPLETED + " INTEGER NOT NULL);";


    public static final String TAG= TaskDataBaseHelper.class.getSimpleName();
    public TaskDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * Create a new Database function
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //VERSION 1 TABLES
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

        //VERSION 2 TABLES
        db.execSQL(SQL_CREATE_NOTE_TABLE);
        db.execSQL(SQL_CREATE_CHECKLIST_TABLE);
        db.execSQL(SQL_CREATE_CHECKLISTITEM_TABLE);
        db.execSQL(SQL_CREATE_PHOTO_TABLE);
    }

    /**
     * Upgrade database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 1:
                db.execSQL(SQL_CREATE_NOTE_TABLE);
                db.execSQL(SQL_CREATE_CHECKLIST_TABLE);
                db.execSQL(SQL_CREATE_CHECKLISTITEM_TABLE);
                db.execSQL(SQL_CREATE_PHOTO_TABLE);
                //no break we want all the upgrades
        }
    }

    /**
     * Insert new Task
     * @param task
     * @return
     */
    public long insertTask(Task task){
        ContentValues cv = new ContentValues();
        cv.put(TaskEntry.COLUMN_TASK_TITLE,task.getTitle());
        cv.put(TaskEntry.COLUMN_DATE,task.getCreationDate());
        cv.put(TaskEntry.COLUMN_PRIORITY,task.getPriority());
        cv.put(TaskEntry.COLUMN_CAT_KEY,task.getCategory());
        cv.put(TaskEntry.COLUMN_COMPLETED,task.isCompleted());
        cv.put(TaskEntry.COLUMN_REMINDER,task.getReminder());
        return getWritableDatabase().insert(TaskEntry.TABLE_NAME,null,cv);
    }

    /**
     * Insert new Category
     * @param category
     * @return
     */
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

    /**
     * Get category from name
     * @param category
     * @return
     */
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

    /**
     * Query category from id
     * @param id
     * @return
     */
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

    /**
     * Delete task
     * @param task
     * @return
     */
    public int deleteTask(Task task){
        long id = task.getId();
        if(id==-1) return -1;
        String selection = TaskEntry._ID + " LIKE ?";
        String[] selectionArgs= {String.valueOf(id)};
        return getWritableDatabase().delete(TaskEntry.TABLE_NAME,selection,selectionArgs);
    }

    /**
     * Update Task with new task value
     * @param task the new task to be saved
     * @param completed whether the task is completed or not
     * @return
     */
    public int updateTask(Task task,boolean completed){
        ContentValues cv= new ContentValues();
        cv.put(TaskEntry.COLUMN_TASK_TITLE,task.getTitle());
        cv.put(TaskEntry.COLUMN_DATE,task.getCreationDate());
        cv.put(TaskEntry.COLUMN_PRIORITY,task.getPriority());
        cv.put(TaskEntry.COLUMN_CAT_KEY,task.getCategory());
        cv.put(TaskEntry.COLUMN_COMPLETED,completed?1:0);
        cv.put(TaskEntry.COLUMN_REMINDER,task.getReminder());

        String selection = TaskEntry._ID + " LIKE ?";
        String[] selectionArgs={String.valueOf(task.getId())};

        int count = getWritableDatabase().update(
                TaskEntry.TABLE_NAME,
                cv,
                selection,
                selectionArgs
        );
        return count;
    }


    /**
     * Query Tasks all task according to a query code
     * @param queryCode determines what task should be returned
     * @return
     */
    public TaskCursor queryTasks(int queryCode){
        String selection=TaskEntry.COLUMN_COMPLETED +" LIKE ? ";
        ArrayList<String>array= new ArrayList<String>();
        array.add(String.valueOf(0));
        String[] selectionArgs;
        switch (queryCode){
            case 1:
                selection+=" AND "+TaskEntry.COLUMN_REMINDER + " NOT LIKE ?"; //Task with reminder
                array.add(String.valueOf(-1));
                break;
            case 3:
                selection+=" AND "+TaskEntry.COLUMN_PRIORITY + " LIKE ?";  //Very high priority task
                array.add(String.valueOf(Task.VERY_HIGH_PRIORITY));
                break;
            case 4:
                selection+=" AND "+TaskEntry.COLUMN_PRIORITY + " LIKE ?"; //High priority task
                array.add(String.valueOf(Task.HIGH_PRIORITY));
                break;
            case 5:
                selection+=" AND "+TaskEntry.COLUMN_PRIORITY + " LIKE ?"; //Normal priority task
                array.add(String.valueOf(Task.NORMAL_PRIORITY));
                break;
            case 6:
                selection+=" AND "+TaskEntry.COLUMN_PRIORITY + " LIKE ?"; //Low priority task
                array.add(String.valueOf(Task.LOW_PRIORITY));
                break;
            case 2:
                selection=TaskEntry.COLUMN_COMPLETED + " LIKE ? ";  //Task completed
                array.remove(0);
                array.add(String.valueOf(1));
                break;
            default:
                if(queryCode>=100){
                    selection+=" AND "+TaskEntry.COLUMN_CAT_KEY + " LIKE ?"; //Task belonging to a certain category
                    array.add(String.valueOf(queryCode-100));
                }

        }
            selectionArgs=new String[array.size()];
            selectionArgs=array.toArray(selectionArgs);
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

    /**
     * Query a single Task from its ID
     * @param rowId
     * @return
     */
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

    /**
     * Return all categories in a Cursor
     * @return
     */
    public Cursor queryCategories() {
        String [] column = {CategoryEntry.COLUMN_NAME};
        Cursor wrapped = getReadableDatabase().query(
                CategoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        return new CategoryCursor(wrapped);
    }

    /**
     * Delete a category given its ID
     * @param id
     * @return
     */
    public int deleteCategory(long id) {
        String selection = CategoryEntry._ID + " LIKE ?";
        String[] selectionArgs= {String.valueOf(id)};
        return getWritableDatabase().delete(CategoryEntry.TABLE_NAME,selection,selectionArgs);
    }

    /**
     * Edit category name
     * @param id
     * @param name
     * @return
     */
    public int editCategory(long id,String name){
        ContentValues cv = new ContentValues();
        cv.put(CategoryEntry.COLUMN_NAME,name);

        String selection = CategoryEntry._ID + " LIKE ?";
        String[] selectionArgs={String.valueOf(id)};
        int count = getWritableDatabase().update(
                CategoryEntry.TABLE_NAME,
                cv,
                selection,
                selectionArgs
        );
        return count;
    }

    //========Note=========

    /**
     * Insert new note in database
     * @param note
     * @return
     */
    public long insertNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(NoteEntry.COLUMN_TASK_KEY,note.getTaskId());
        cv.put(NoteEntry.COLUMN_CONTENT,note.getNoteContent());
        cv.put(NoteEntry.COLUMN_CREATED_DATE,note.getCreationDate().getTime());
        cv.put(NoteEntry.COLUMN_EDITED_DATE,note.getLastEdit().getTime());
        cv.put(NoteEntry.COLUMN_TITLE,note.getTitle());
        return getWritableDatabase().insert(NoteEntry.TABLE_NAME,null,cv);
    }

    /**
     * Return all Notes in the database with taskId as given
     * @return
     */
    public NoteCursor queryNotes(long taskId){
        String selection =NoteEntry.COLUMN_TASK_KEY + " LIKE ?";
        String [] selectionArgs={String.valueOf(taskId)};
        //Equivalent of select * from note order by last_edit asc
        Cursor wrapped = getReadableDatabase().query(
                NoteEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                NoteEntry.COLUMN_EDITED_DATE + " desc");
        return new NoteCursor(wrapped);
    }

    /**
     * Update note
     * @param note
     * @return
     */
    public long updateNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(NoteEntry.COLUMN_TITLE,note.getTitle());
        cv.put(NoteEntry.COLUMN_CONTENT,note.getNoteContent());
        cv.put(NoteEntry.COLUMN_TASK_KEY,note.getTaskId());
        cv.put(NoteEntry.COLUMN_EDITED_DATE,new Date().getTime());

        String selection = NoteEntry._ID + " LIKE ? ";
        String [] selectionArgs = {String.valueOf(note.getId())};

        long count = getReadableDatabase().update(
                NoteEntry.TABLE_NAME,
                cv,
                selection,
                selectionArgs);

        return count;
    }

    /**
     * Delete single Note
     * @param noteId
     * @return
     */
    public boolean deleteNote(long noteId){
        String selection = NoteEntry._ID + " LIKE ? ";
        String [] selectionArgs ={String.valueOf(noteId)};
        return getWritableDatabase().delete(NoteEntry.TABLE_NAME,selection,selectionArgs)!=-1;
    }

    //========Checklist=========

    /**
     * Insert Checklist to db
     * @param checkList
     * @return
     */
    public long insertChecklist(CheckList checkList){
        ContentValues cv = new ContentValues();
        cv.put(CheckListEntry.COLUMN_TASK_KEY,checkList.getTaskId());
        cv.put(CheckListEntry.COLUMN_CREATED_DATE,checkList.getCreatedDate().getTime());
        cv.put(CheckListEntry.COLUMN_EDITED_DATE,checkList.getEditedDate().getTime());
        cv.put(CheckListEntry.COLUMN_TITLE,checkList.getName());
        return getWritableDatabase().insert(CheckListEntry.TABLE_NAME,null,cv);
    }

    /**
     * Insert new checklist items
     * @param items
     * @param checklistId
     * @return
     */
    public long insertChecklistItems(ArrayList<CheckListItem> items,long checklistId){
        for(int i=0;i< items.size();i++){
            ContentValues cv = new ContentValues();
            cv.put(CheckListItemEntry.COLUMN_CHECKLIST_KEY,checklistId);
            cv.put(CheckListItemEntry.COLUMN_COMPLETED,items.get(i).isCompleted()?1:0);
            cv.put(CheckListItemEntry.COLUMN_ITEM,items.get(i).getItem());
            getWritableDatabase().insert(CheckListItemEntry.TABLE_NAME,null,cv);
        }
        return 1;
    }

    /**
     * get all checklist belonging to a given task
     * @param taskId
     * @return
     */
    public ChecklistCursor queryChecklist(long taskId){
        String selection = CheckListEntry.COLUMN_TASK_KEY + " LIKE ?";
        String [] selectionArgs={String.valueOf(taskId)};

        //Equivalent to select * where task_key = taskId
        Cursor wrapped = getReadableDatabase().query(
                CheckListEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                CheckListEntry.COLUMN_EDITED_DATE + " desc");
        return new ChecklistCursor(wrapped);

    }

    /**
     * get a checklist items belonging to a checklist
     * @param checklistId
     * @return
     */
    public CheckListItemCursor queryChecklistItem(long checklistId){
        String selection = CheckListItemEntry.COLUMN_CHECKLIST_KEY + " LIKE ? ";
        String [] selectionArgs = {String.valueOf(checklistId)};

        Cursor wrapped = getReadableDatabase().query(
                CheckListItemEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                CheckListItemEntry._ID + " asc "
        );

        return new CheckListItemCursor(wrapped);
    }


    /**
     * Update checklist data except checklist items
     * @param checkList
     * @return
     */
    public int updateCheckList(CheckList checkList){
        String selection = CheckListEntry._ID + " LIKE ? ";
        String []selectionArgs = {String.valueOf(checkList.getId())};

        ContentValues cv = new ContentValues();
        cv.put(CheckListEntry.COLUMN_EDITED_DATE,new Date().getTime());
        cv.put(CheckListEntry.COLUMN_TITLE,checkList.getName());

        int count = getWritableDatabase().update(CheckListEntry.TABLE_NAME,cv,selection,selectionArgs);
        return count;
    }

    /**
     * Update checklist item data
     * @param checkList
     * @return
     */
    public int updateCheckListItem(CheckList checkList){
        ArrayList<CheckListItem> items = checkList.getChecklistItems();
        ArrayList<CheckListItem> newItems = new ArrayList<CheckListItem>();

        int count=0;
        for(int i=0;i<items.size();i++) {
            //If Item has no ID means it's a new item, skip it and add it to the list of new items
            if (items.get(i).getId() ==-1) {
                newItems.add(items.get(i));
                continue;
            }

            String selection = CheckListItemEntry._ID + " LIKE ?";
            String [] selectionArgs = {String.valueOf(items.get(i).getId())};

            ContentValues cv = new ContentValues();
            cv.put(CheckListItemEntry.COLUMN_ITEM,items.get(i).getItem());
            cv.put(CheckListItemEntry.COLUMN_COMPLETED,items.get(i).isCompleted()?1:0);
            count=getWritableDatabase().update(CheckListItemEntry.TABLE_NAME,cv,selection,selectionArgs);
        }


        //Add the new Items that were found without an id
        if(items.size()>0)
            count+= insertChecklistItems(newItems,checkList.getId());

        return count;
    }

    /**
     * Delete Single Checklist
     * @param id
     * @return
     */
    public boolean deleteCheckList(long id){
        String selection = CheckListEntry._ID + " LIKE ? ";
        String [] selectionArgs = {String.valueOf(id)};
        return getWritableDatabase().delete(CheckListEntry.TABLE_NAME,selection,selectionArgs)!=-1;

    }

    /**
     * Delete single ChecklistItem
     * @param id
     * @return
     */
    public boolean deleteCheckListItem(long id){
        String selection = CheckListItemEntry._ID + " LIKE ?";
        String [] selectionArgs = {String.valueOf(id)};
        return getWritableDatabase().delete(CheckListItemEntry.TABLE_NAME, selection, selectionArgs)!=-1;
    }



    //========Photo=================

    /**
     * Insert Photo into database
     * @param photo
     * @return ID of photo information newli added
     */
    public long insertPhoto(Photo photo){
        ContentValues cv = new ContentValues();
        cv.put(PhotoEntry.COLUMN_TASK_KEY,photo.getTaskId());
        cv.put(PhotoEntry.COLUMN_FILENAME,photo.getFilename());
        return getWritableDatabase().insert(PhotoEntry.TABLE_NAME,null,cv);
    }

    /**
     * Get single photo cursor
     * @param taskId
     * @return
     */
    public PhotoCursor getPhotos(long taskId){
        String selection = PhotoEntry.COLUMN_TASK_KEY + " LIKE ?";
        String [] selectionArgs = {String.valueOf(taskId)};

        //
        Cursor wrapped = getReadableDatabase().query(
                PhotoEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                PhotoEntry._ID + " desc");

        return new PhotoCursor(wrapped);
    }

    public boolean deletePhoto(long id){
        String selection = PhotoEntry._ID + " LIKE ? ";
        String [] selectionArgs = {String.valueOf(id)};
        return getWritableDatabase().delete(PhotoEntry.TABLE_NAME,selection,selectionArgs)!=-1;

    }
    //===========Cursors================


    /**
     * Customed cursor for Task
     */
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
            task.setReminder(new Date(getLong(getColumnIndex(TaskEntry.COLUMN_REMINDER))));
            return task;
        }

    }

    /**
     * Custom cursor for category
     */
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

    /**
     * Custom cursor for Note
     */

    public static class NoteCursor extends CursorWrapper{
        public NoteCursor(Cursor c){
            super(c);

        }

        public Note getNote(){
            if(isBeforeFirst() || isAfterLast()) return null;
            Note note = new Note();
            note.setId(getLong(getColumnIndex(NoteEntry._ID)));
            note.setTaskId(getLong(getColumnIndex(NoteEntry.COLUMN_TASK_KEY)));
            note.setCreationDate(new Date(getLong(getColumnIndex(NoteEntry.COLUMN_CREATED_DATE))));
            note.setLastEdit(new Date(getLong(getColumnIndex(NoteEntry.COLUMN_EDITED_DATE))));
            note.setNoteContent(getString(getColumnIndex(NoteEntry.COLUMN_CONTENT)));
            note.setTitle(getString(getColumnIndex(NoteEntry.COLUMN_TITLE)));
            return note;
        }

    }

    /**
     * Custom cursor for Checklist
     */
    public static class ChecklistCursor extends CursorWrapper{

        public ChecklistCursor(Cursor c){
            super(c);
        }

        public CheckList getChecklist(){
            if(isBeforeFirst() || isAfterLast()) return null;
            CheckList checklist = new CheckList();
            checklist.setId(getLong(getColumnIndex(CheckListEntry._ID)));
            checklist.setName(getString(getColumnIndex(CheckListEntry.COLUMN_TITLE)));
            checklist.setTaskId(getLong(getColumnIndex(CheckListEntry.COLUMN_TASK_KEY)));
            checklist.setEditedDate(new Date(getLong(getColumnIndex(CheckListEntry.COLUMN_EDITED_DATE))));
            checklist.setCreatedDate(new Date(getLong(getColumnIndex(CheckListEntry.COLUMN_CREATED_DATE))));
            return checklist;
        }
    }


    /**
     * custom cursor for checklistitems
     */

    public static class CheckListItemCursor extends CursorWrapper{

        public CheckListItemCursor(Cursor c){
            super(c);
        }

        public CheckListItem getChecklistItem(){

            if(isBeforeFirst() || isAfterLast())return null;
                CheckListItem checkListItem = new CheckListItem();
                checkListItem.setId(getLong(getColumnIndex(CheckListItemEntry._ID)));
                checkListItem.setCompleted(getLong(getColumnIndex(CheckListItemEntry.COLUMN_COMPLETED))==1);
                checkListItem.setItem(getString(getColumnIndex(CheckListItemEntry.COLUMN_ITEM)));
                return checkListItem;
        }
    }

    /**
     * Custom cursor for pictures
     */
    public static class PhotoCursor extends CursorWrapper{

        public PhotoCursor(Cursor c){
            super(c);
        }

        public Photo getPhoto(){
            if(isBeforeFirst() || isAfterLast()) return null;
            Photo photo = new Photo();
            photo.setId(getLong(getColumnIndex(PhotoEntry._ID)));
            photo.setFilename(getString(getColumnIndex(PhotoEntry.COLUMN_FILENAME)));
            photo.setTaskId(getLong(getColumnIndex(PhotoEntry.COLUMN_TASK_KEY)));
            return photo;
        }
    }

    /**
     * Get checklistitem array list
     * @param checklistId
     * @return
     */
    public  ArrayList<CheckListItem> getChecklistItems(long checklistId){
        ArrayList<CheckListItem> items = new ArrayList<CheckListItem>();

        CheckListItemCursor cursor = queryChecklistItem(checklistId);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            items.add(cursor.getChecklistItem());
            cursor.moveToNext();
        }
        return items;
    }

}