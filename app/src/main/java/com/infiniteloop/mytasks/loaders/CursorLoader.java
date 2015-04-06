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
            return TaskLab.get(getContext()).getCategories();
        }
    }



}
