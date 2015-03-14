package com.infiniteloop.mytasks;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
         Task task= getIntent().getExtras().getParcelable(EditTaskFragment.EXTRA_TASK);
        return EditTaskFragment.newInstance(task);
    }
}
