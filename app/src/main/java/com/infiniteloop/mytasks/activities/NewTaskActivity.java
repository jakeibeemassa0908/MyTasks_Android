package com.infiniteloop.mytasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import com.infiniteloop.mytasks.fragments.NewTaskFragment;
import com.infiniteloop.mytasks.R;

/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.new_task_menu,menu);
       return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);


        //Receive intent when this activity is started with an implicit intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String sharedText=null;

        if(Intent.ACTION_SEND.equals(action) && type!=null)
            if ("text/plain".equals(type)) {
                sharedText  = intent.getStringExtra(Intent.EXTRA_TEXT);
                if(sharedText!=null){
                    sharedText.trim();
                }
            }

        //set the fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = NewTaskFragment.newInstance(sharedText);

        fm.beginTransaction()
                 .add(R.id.container,fragment)
                 .commit();

        getSupportActionBar().setElevation(0);

    }

}
