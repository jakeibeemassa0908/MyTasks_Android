package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;
import android.view.Menu;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.fragments.NoteFragment;

/**
 * Created by theotherside on 17/04/15.
 */
public class NoteActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NoteFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu,menu);
        return true;
    }
}
