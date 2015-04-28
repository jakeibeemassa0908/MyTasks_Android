package com.infiniteloop.mytasks.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.DefaultGridFragment;
import com.infiniteloop.mytasks.fragments.DetailTaskFragment;

/**
 * Created by theotherside on 26/04/15.
 */
public class DefaultGridActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {

        long taskId = getIntent().getLongExtra(DetailTaskFragment.EXTRA_TASK,-1);
        String listType = getIntent().getStringExtra(DefaultGridFragment.EXTRA_TYPE);

        return DefaultGridFragment.newInstance(taskId,listType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
