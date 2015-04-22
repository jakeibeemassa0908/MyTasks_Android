package com.infiniteloop.mytasks.data;

/**
 * Created by theotherside on 22/04/15.
 */
public class CheckListItem {
    private long mId;
    private long mCheckList;
    private String mItem;
    private boolean mCompleted;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getCheckList() {
        return mCheckList;
    }

    public void setCheckList(long mCheckList) {
        this.mCheckList = mCheckList;
    }

    public String getItem() {
        return mItem;
    }

    public void setItem(String mItem) {
        this.mItem = mItem;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean mCompleted) {
        this.mCompleted = mCompleted;
    }
}
