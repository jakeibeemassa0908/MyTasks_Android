package com.infiniteloop.mytasks.data;

/**
 * Created by theotherside on 16/03/15.
 */
public class Category {

    private String mCategoryName;
    private long mId;

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String nCategoryName) {
        this.mCategoryName = nCategoryName;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }
}
