package com.infiniteloop.mytasks.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Photo;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;
import com.infiniteloop.mytasks.data.TaskLab;
import com.infiniteloop.mytasks.fragments.DetailTaskFragment;
import com.infiniteloop.mytasks.fragments.PhotoFragment;

import java.util.ArrayList;

/**
 * Created by theotherside on 01/05/15.
 */
public class PhotoPagerActivity extends ActionBarActivity {

    private ViewPager mViewPager;
    private ArrayList<Photo> mPhotosPath;
    private long mTaskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        mTaskId = getIntent().getLongExtra(DetailTaskFragment.EXTRA_TASK,-1);


        mPhotosPath = new ArrayList<Photo>();


        if(mTaskId!=-1){
            TaskDataBaseHelper.PhotoCursor cursor =TaskLab.get(this).getPhotos(mTaskId);

            if(cursor!=null){
                cursor.moveToFirst();
                for(int i =0;i<cursor.getCount();i++){
                    Photo photo = cursor.getPhoto();
                    if(!Helpers.imageExists(photo.getFilename())){
                        continue;
                    }
                    mPhotosPath.add(photo);
                    cursor.moveToNext();
                }
            }
        }



        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {

                return PhotoFragment.newInstance(mPhotosPath.get(i).getFilename());
            }

            @Override
            public int getCount() {
                return  mPhotosPath.size();
            }
        });

        long photoId = getIntent().getLongExtra(PhotoFragment.EXTRA_PICTURE,-1);
        if(photoId!=-1){
            for(int i=0;i< mPhotosPath.size();i++){
                if(mPhotosPath.get(i).getId()==photoId){
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }
}
