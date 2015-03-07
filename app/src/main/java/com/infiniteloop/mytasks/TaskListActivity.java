package com.infiniteloop.mytasks;

import android.support.v4.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity {
    private static final String TAG= TaskListActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}
