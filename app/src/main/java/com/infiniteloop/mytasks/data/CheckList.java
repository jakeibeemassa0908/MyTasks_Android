package com.infiniteloop.mytasks.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 22/04/15.
 */
public class CheckList implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mTaskId);
        dest.writeString(mName);
        dest.writeSerializable(mCreatedDate);
        dest.writeSerializable(mEditedDate);
    }

    public static final Parcelable.Creator<CheckList> CREATOR
            = new Parcelable.Creator<CheckList>() {
        public CheckList createFromParcel(Parcel in) {
            return new CheckList(in);
        }

        public CheckList[] newArray(int size) {
            return new CheckList[size];
        }
    };

    private CheckList(Parcel in){
        mId = in.readLong();
        mTaskId = in.readLong();
        mName=in.readString();
        mCreatedDate= (Date)in.readSerializable();
        mEditedDate = (Date)in.readSerializable();
    }
}
