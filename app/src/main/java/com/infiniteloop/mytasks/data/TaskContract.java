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
        //Normalize the date to the begining of the (UTC) day
        Time time = new Time();
        time.set(date);
        int julianDay= Time.getJulianDay(date,time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class CategoryEntry implements BaseColumns{

        public static final String TABLE_NAME="category";

        public static final String COLUMN_NAME="name";
    }

    public static final class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME="task";

        public static final String COLUMN_CAT_KEY="category_id";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_TASK_TITLE="title";
        public static final String COLUMN_PRIORITY="priority";
        public static final String COLUMN_COMPLETED="completed";
    }
}
