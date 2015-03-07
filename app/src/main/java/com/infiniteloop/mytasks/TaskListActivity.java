package com.infiniteloop.mytasks;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

public class TaskListActivity extends SingleFragmentActivity {
    private static final String TAG= TaskListActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
}
