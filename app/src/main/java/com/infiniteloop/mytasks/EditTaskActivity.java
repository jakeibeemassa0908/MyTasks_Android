package com.infiniteloop.mytasks;


import android.support.v4.app.Fragment;
import android.view.Menu;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskActivity extends SingleFragmentActivity {



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
