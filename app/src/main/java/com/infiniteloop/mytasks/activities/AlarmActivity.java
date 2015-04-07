package com.infiniteloop.mytasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.services.ReminderService;

/**
 * Created by theotherside on 07/04/15.
 */
public class AlarmActivity extends ActionBarActivity {

    private static final String TAG = AlarmActivity.class.getSimpleName();
    private Button mOkButton;
    private TextView mAlarmTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);

        onNewIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        Log.d(TAG,extras.toString());
        if(extras!=null)
            if(extras.containsKey(ReminderService.EXTRA_ALARM_TASK)) {
                Task t = intent.getParcelableExtra(ReminderService.EXTRA_ALARM_TASK);

                View v = findViewById(R.id.layout);

                //Set background color according to priority
                switch (t.getPriority()) {
                    case Task.VERY_HIGH_PRIORITY:
                        v.setBackgroundColor(getResources().getColor(R.color.dark_red));
                        break;

                    case Task.HIGH_PRIORITY:
                        v.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                        break;

                    case Task.NORMAL_PRIORITY:
                        v.setBackgroundColor(getResources().getColor(R.color.sunshine_dark_blue));
                        break;

                    case Task.LOW_PRIORITY:
                        v.setBackgroundColor(getResources().getColor(R.color.dark_green));

                }

                mOkButton = (Button) findViewById(R.id.ok_alarm);
                mOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmActivity.this.finish();
                    }
                });
                mAlarmTitle = (TextView) findViewById(R.id.alarm_title);
                mAlarmTitle.setText(t.getTitle());
            }


    }
}
