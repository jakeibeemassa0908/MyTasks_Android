package com.infiniteloop.mytasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Task;

/**
 * Created by theotherside on 07/04/15.
 */
public class AlarmActivity extends ActionBarActivity {
    private Button mOkButton;
    private TextView mAlarmTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);

        Intent i = getIntent();
        Task t = i.getParcelableExtra("TaskEXTRA");

        View v=(View)findViewById(R.id.layout);

        //Set background color according to priority
        switch (t.getPriority()){

        }

        mOkButton = (Button)findViewById(R.id.ok_alarm);
        mAlarmTitle = (TextView)findViewById(R.id.alarm_title);
        mAlarmTitle.setText(t.getTitle());


    }
}
