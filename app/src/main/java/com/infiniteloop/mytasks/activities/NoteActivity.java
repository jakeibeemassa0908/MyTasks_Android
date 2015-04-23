package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;
import android.view.Menu;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.fragments.DetailTaskFragment;
import com.infiniteloop.mytasks.fragments.NoteFragment;

/**
 * Created by theotherside on 17/04/15.
 */
public class NoteActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Task task = getIntent().getParcelableExtra(DetailTaskFragment.EXTRA_TASK);
        return NoteFragment.newInstance(task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu,menu);
        return true;
    }
}
