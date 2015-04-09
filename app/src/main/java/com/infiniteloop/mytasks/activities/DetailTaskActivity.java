package com.infiniteloop.mytasks.activities;


import android.support.v4.app.Fragment;
import android.view.Menu;

import com.infiniteloop.mytasks.fragments.EditTaskFragment;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Task;

/**
 * Created by theotherside on 14/03/15.
 */
public class DetailTaskActivity extends SingleFragmentActivity {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_task_menu,menu);
        return true;
    }

    @Override
    protected Fragment createFragment() {
         Task task= getIntent().getExtras().getParcelable(EditTaskFragment.EXTRA_TASK);
        return EditTaskFragment.newInstance(task);
    }
}
