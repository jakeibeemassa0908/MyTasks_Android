package com.infiniteloop.mytasks.activities;

import android.support.v4.app.Fragment;

import com.infiniteloop.mytasks.fragments.SigninFragment;

/**
 * Created by theotherside on 10/29/16.
 */

public class SigninActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SigninFragment.newInstance();
    }
}
