package com.infiniteloop.mytasks.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.infiniteloop.mytasks.fragments.EditCategoryFragment;
import com.infiniteloop.mytasks.fragments.TaskListFragment;

/**
 * Created by theotherside on 10/04/15.
 */
public class EditCategoryActivity extends SingleFragmentActivity {

    private static final String TAG = EditCategoryActivity.class.getSimpleName();
    private long mPosition=0;

    @Override
    protected Fragment createFragment() {
        mPosition=getIntent().getIntExtra(TaskListFragment.EXTRA_POSITION,-1);
        return EditCategoryFragment.newInstance(mPosition);
    }
}
