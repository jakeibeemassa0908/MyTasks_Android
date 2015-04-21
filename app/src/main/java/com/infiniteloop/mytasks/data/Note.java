package com.infiniteloop.mytasks.data;

import java.util.Date;

/**
 * Created by theotherside on 21/04/15.
 */
public class Note {

    private long mId;
    private long mTaskId;
    private String mTitle;
    private String mNoteContent;
    private Date mCreationDate;
    private Date mLastEdit;


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

    public String getNoteContent() {
        return mNoteContent;
    }

    public void setNoteContent(String mNoteContent) {
        this.mNoteContent = mNoteContent;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getLastEdit() {
        return mLastEdit;
    }

    public void setLastEdit(Date mLastEdit) {
        this.mLastEdit = mLastEdit;
    }
}
