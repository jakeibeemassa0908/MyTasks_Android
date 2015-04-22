package com.infiniteloop.mytasks.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 22/04/15.
 */
public class CheckList {

    private long mId;
    private long mTaskId;
    private String mName;
    private Date mCreatedDate;
    private Date mEditedDate;
    private ArrayList<CheckListItem> mChecklistItems;


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(Date mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public Date getEditedDate() {
        return mEditedDate;
    }

    public void setEditedDate(Date mEditedDate) {
        this.mEditedDate = mEditedDate;
    }

    public ArrayList<CheckListItem> getChecklistItems() {
        return mChecklistItems;
    }

    public void setChecklistItems(ArrayList<CheckListItem> mChecklistItems) {
        this.mChecklistItems = mChecklistItems;
    }

    public long getTaskId() {
        return mTaskId;
    }

    public void setTaskId(long mTaskId) {
        this.mTaskId = mTaskId;
    }
}
