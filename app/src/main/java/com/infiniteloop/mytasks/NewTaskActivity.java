package com.infiniteloop.mytasks;

import android.support.v4.app.Fragment;
import android.view.Menu;

/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {

        return new NewTaskFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.new_task_menu,menu);
       return true;
    }
}
