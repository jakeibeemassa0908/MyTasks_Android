package com.infiniteloop.mytasks;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by theotherside on 07/03/15.
 */
public class Task {
    private Date mCreationDate;
    private long mId;
    private String mtitle;
    private int mPriority;
    private long mCategory;
    private String mVisibility;
    private int mDurationHours;
    private int mDurationMinutes;
    private int mTotalDurationMinutes;

    public static final int LOW_PRIORITY=0;
    public static final int NORMAL_PRIORITY=1;
    public static final int HIGH_PRIORITY=2;
    public static final int VERY_HIGH_PRIORITY=3;


    @Override
    public String toString() {
       return DateFormat.getDateInstance().format(new Date(getCreationDate()));
    }

    public Task(){

    }

    public Task(String title,int priority,long category,int durationHours,int durationMinutes){
        mCreationDate = new Date();
        mPriority= priority;
        mtitle=title;
        mCategory=category;
        mTotalDurationMinutes = durationMinutes+(durationHours*60);
    }


    public long getCreationDate() {

        return mCreationDate.getTime();
    }

    public void setCreationDate(Date mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mtitle;
    }

    public void setTitle(String mDescription) {
        this.mtitle = mDescription;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public long getCategory() {
        return mCategory;
    }

    public void setCategory(long mCategory) {
        this.mCategory = mCategory;
    }

    public String getVisibility() {
        return mVisibility;
    }

    public void setVisibility(String mVisibility) {
        this.mVisibility = mVisibility;
    }

    public int getDurationHours() {
        return mDurationHours;
    }

    public void setDurationHours(int mDurationHours) {
        this.mDurationHours = mDurationHours;
    }

    public int getDurationMinutes() {
        return mDurationMinutes;
    }

    public void setDurationMinutes(int mDurationMinutes) {
        this.mDurationMinutes = mDurationMinutes;
    }

    public int getTotalDurationMinutes() {
        return mTotalDurationMinutes;
    }

    public void setTotalDurationMinutes(int mTotalDurationMinutes) {
        this.mDurationMinutes=mTotalDurationMinutes;
    }

    public void setId(long mId) {
        this.mId = mId;
    }
}
