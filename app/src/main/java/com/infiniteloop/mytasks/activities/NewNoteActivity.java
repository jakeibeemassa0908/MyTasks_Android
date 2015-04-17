package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.NewNoteFragment;

/**
 * Created by theotherside on 17/04/15.
 */
public class NewNoteActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NewNoteFragment();
    }
}
