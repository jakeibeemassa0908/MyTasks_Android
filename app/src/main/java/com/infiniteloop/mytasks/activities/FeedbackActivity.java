package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.FeedbackFragment;

/**
 * Created by theotherside on 11/04/15.
 */
public class FeedbackActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new FeedbackFragment();
    }
}
