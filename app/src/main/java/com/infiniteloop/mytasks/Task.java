package com.infiniteloop.mytasks;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by theotherside on 07/03/15.
 */
public class Task implements Parcelable {
    private Date mCreationDate;
    private long mId;
    private String mtitle;
    private int mPriority;
    private long mCategory;
    private String mVisibility;
    private boolean mCompleted;

    public static final int LOW_PRIORITY=0;
    public static final int NORMAL_PRIORITY=1;
    public static final int HIGH_PRIORITY=2;
    public static final int VERY_HIGH_PRIORITY=3;




    public String dateToString() {
       return DateFormat.getDateInstance().format(new Date(getCreationDate()));
    }

    public Task(){

    }

    public Task(String title,int priority,long category){
        mCreationDate = new Date();
        mPriority= priority;
        mtitle=title;
        mCategory=category;
        mCompleted=false;
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

    public void setId(long mId) {
        this.mId = mId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mCreationDate);
        dest.writeLong(mId);
        dest.writeString( mtitle);
        dest.writeInt(mPriority);
        dest.writeLong(mCategory);
        dest.writeString(mVisibility);
        dest.writeByte((byte)(mCompleted?1:0));
    }

    public static final Creator<Task> CREATOR= new Creator<Task>(){
        public Task createFromParcel(Parcel in){
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private Task(Parcel in){
        mCreationDate = (Date)in.readSerializable();
        mId = in.readLong();
        mtitle=in.readString();
        mPriority=in.readInt();
        mCategory=in.readLong();
        mVisibility=in.readString();
        mCompleted=in.readByte() !=0;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean mCompleted) {
        this.mCompleted = mCompleted;
    }
}
