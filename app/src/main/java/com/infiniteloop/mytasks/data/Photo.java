package com.infiniteloop.mytasks.data;

import java.util.Date;

/**
 * Created by theotherside on 21/04/15.
 */
public class Photo {

    private long mId;
    private long mTaskId;
    private String mFilename;
    private Date mCreationDate;


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getTaskId() {
        return mTaskId;
    }

    public void setTaskId(long mTaskId) {
        this.mTaskId = mTaskId;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date mCreationDate) {
        this.mCreationDate = mCreationDate;
    }
}
