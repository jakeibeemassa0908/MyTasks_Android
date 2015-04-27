package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.DefaultGridFragment;

/**
 * Created by theotherside on 26/04/15.
 */
public class DefaultGridActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DefaultGridFragment();
    }
}
