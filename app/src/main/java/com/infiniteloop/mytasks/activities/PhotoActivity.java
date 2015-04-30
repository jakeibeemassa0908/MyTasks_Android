package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.PhotoFragment;

/**
 * Created by theotherside on 30/04/15.
 */
public class PhotoActivity extends SingleFragmentActivity {

    private static final String TAG = PhotoActivity.class.getName();

    @Override
    protected Fragment createFragment() {
        String path = getIntent().getStringExtra(PhotoFragment.EXTRA_PICTURE);
        return  PhotoFragment.newInstance(path);
    }
}
