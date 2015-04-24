package com.infiniteloop.mytasks.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by theotherside on 21/04/15.
 */
public class Note implements Parcelable {

    private long mId;
    private long mTaskId;
    private String mTitle;
    private String mNoteContent;
    private Date mCreationDate;
    private Date mLastEdit;


    public Note(String title,String content,long taskId){
        mTaskId = taskId;
        mTitle=title;
        mNoteContent=content;
    }

    public Note(){

    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNoteContent);
        dest.writeString(mTitle);
        dest.writeSerializable(mCreationDate);
        dest.writeSerializable(mLastEdit);
        dest.writeLong(mTaskId);
    }

    public static final Parcelable.Creator<Note> CREATOR
            = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel in) {
        mNoteContent = in.readString();
        mTitle = in.readString();
        mCreationDate = (Date)in.readSerializable();
        mLastEdit = (Date)in.readSerializable();
        mTaskId = in.readLong();
    }
}
