package com.infiniteloop.mytasks.data;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by theotherside on 11/03/15.
 * Defines tables and column names for the task database
 */

public class TaskContract {

    //To make it easy to query for the exact date , we normalize all dates that go into
    // the database to the start of the Juilian day at UTC

    public static long normalizeDate(long date){
        //Normalize the date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(date);
        int julianDay= Time.getJulianDay(date,time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    //Category Table

    public static final class CategoryEntry implements BaseColumns{

        public static final String TABLE_NAME="category";

        public static final String COLUMN_NAME="name";
    }

    //Task Table

    public static final class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME="task";

        public static final String COLUMN_CAT_KEY="category_id";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_TASK_TITLE="title";
        public static final String COLUMN_PRIORITY="priority";
        public static final String COLUMN_COMPLETED="completed";
        public static final String COLUMN_REMINDER="reminder";
    }

    //Note Table

    public static final class NoteEntry implements BaseColumns{
        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_TASK_KEY = "task_id";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_CREATED_DATE="creation_date";
        public static final String COLUMN_EDITED_DATE ="edit_date";

    }

    //Checklist Table

    public static final class CheckListEntry implements BaseColumns{
        public static final String TABLE_NAME = "checklist";
        public static final String COLUMN_TASK_KEY = "task_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CREATED_DATE = "creation_date";
        public static final String COLUMN_EDITED_DATE = "edit_date";
    }

    //Photo Table
    public static final class PhotoEntry implements BaseColumns{
        public static final String TABLE_NAME = "photo";
        public static final String COLUMN_TASK_KEY="task_id";
        public static final String COLUMN_FILENAME = "filename";

    }
    //Checklist items Table

    public static final class CheckListItem implements BaseColumns{
        public static final String TABLE_NAME = "checklist_item";
        public static final String COLUMN_CHECKLIST_KEY ="checklist_id";
        public static final String COLUMN_ITEM = "item";
        public static final String COLUMN_COMPLETED = "completed";
    }

}
