package com.infiniteloop.mytasks;

import java.util.Date;

/**
 * Created by theotherside on 07/03/15.
 */
public class Task {
    private Date mCreationDate;
    private long mId;
    private String mtitle;
    private int mPriority;
    private String mCategory;
    private String mVisibility;
    private int mDurationHours;
    private int mDurationMinutes;

    public static final int LOW_PRIORITY=0;
    public static final int NORMAL_PRIORITY=1;
    public static final int HIGH_PRIORITY=2;
    public static final int VERY_HIGH_PRIORITY=3;

    public Task(String title,int priority,String category){
        mCreationDate = new Date();
        mPriority= priority;
        mtitle=title;
        mCategory=category;
    }


    public Date getCreationDate() {
        return mCreationDate;
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

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
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
}
