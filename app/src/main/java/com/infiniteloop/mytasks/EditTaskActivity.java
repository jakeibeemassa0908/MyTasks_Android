package com.infiniteloop.mytasks;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        long id = getIntent().getExtras().getLong(EditTaskFragment.EXTRA_TASK_ID,-1);
        return EditTaskFragment.newInstance(id);
    }
}
