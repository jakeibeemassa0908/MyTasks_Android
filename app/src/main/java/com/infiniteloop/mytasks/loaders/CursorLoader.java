package com.infiniteloop.mytasks.loaders;

import android.content.Context;
import android.database.Cursor;

import com.infiniteloop.mytasks.data.TaskLab;

/**
 * Created by theotherside on 17/03/15.
 */
public class CursorLoader {

    public static class CategoryListLoader extends SQLiteCursorLoader {

        public CategoryListLoader(Context context){
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return TaskLab.get(getContext()).getCategoryCursor();
        }
    }

    public static class NoteCursorLoader extends SQLiteCursorLoader {
        long mTaskid;

        public NoteCursorLoader(Context context,long taskId){
            super (context);
            mTaskid=taskId;
        }

        @Override
        protected Cursor loadCursor() {
            //Query the list of runs
            return TaskLab.get(getContext()).queryNotes(mTaskid);
        }
    }


    public static class ChecklistCursorLoader extends SQLiteCursorLoader{
        long mTaskid;

        public ChecklistCursorLoader(Context context,long taskId){
            super(context);
            mTaskid=taskId;
        }

        @Override
        protected Cursor loadCursor() {
            return TaskLab.get(getContext()).queryChecklist(mTaskid);
        }
    }

    public static class PhotoCursorLoader extends SQLiteCursorLoader{
        long mTaskId;

        public PhotoCursorLoader(Context context, long taskId){
            super(context);
            mTaskId= taskId;
        }

        @Override
        protected Cursor loadCursor() {
            return TaskLab.get(getContext()).getPhotos(mTaskId);
        }
    }



}
