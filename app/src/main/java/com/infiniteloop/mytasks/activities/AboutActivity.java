package com.infiniteloop.mytasks.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.AboutFragment;

/**
 * Created by theotherside on 11/04/15.
 */
public class AboutActivity extends SingleFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }
}
