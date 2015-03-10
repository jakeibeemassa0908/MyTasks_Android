package com.infiniteloop.mytasks;

/**
 * Created by theotherside on 09/03/15.
 */
public class DrawerItem {

    private String mTitle;
    private int mImage;
    private int mType;

    public DrawerItem(String title, int image){
        mImage=image;
        mTitle=title;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }
}
