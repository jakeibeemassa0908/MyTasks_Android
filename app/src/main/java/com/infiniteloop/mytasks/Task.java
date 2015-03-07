package com.infiniteloop.mytasks;

import java.util.Date;

/**
 * Created by theotherside on 07/03/15.
 */
public class Task {
    private Date mCreationDate;
    private long mId;
    private String mDescription;
    private int mPriority;
    private String mCategory;
    private String mVisibility;

    public Task(String description,int priority,String category,String visibility){
        mCreationDate = new Date();
        mPriority= priority;
        mDescription=description;
        mCategory=category;
        mVisibility=visibility;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
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
}
