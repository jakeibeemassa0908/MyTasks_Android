package com.infiniteloop.mytasks.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.infiniteloop.mytasks.R;

/**
 * Created by Jake Ibee Massa on 07/03/15.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {

    protected abstract Fragment createFragment();

    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.container,fragment)
                    .commit();
        }

        getSupportActionBar().setElevation(0);

    }
}
