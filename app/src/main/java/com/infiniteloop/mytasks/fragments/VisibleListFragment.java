package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ListFragment;

import com.infiniteloop.mytasks.activities.AlarmActivity;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.services.ReminderService;

/**
 * Created by theotherside on 03/04/15.
 */
public class VisibleListFragment extends ListFragment {
    public static final String TAG = VisibleListFragment.class.getSimpleName();

    public BroadcastReceiver mOnShowNotification  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if we receive this, we're visible so cancel the notification
            setResultCode(Activity.RESULT_CANCELED);

            //start alamrm activity directly since app is running
            Task task = intent.getParcelableExtra("TASK");
            Intent notificationIntent = new Intent(getActivity(),AlarmActivity.class);
            notificationIntent.putExtra(ReminderService.EXTRA_ALARM_TASK,task);

            startActivity(notificationIntent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ReminderService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification,filter,ReminderService.PERM_PRIVATE,null);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mOnShowNotification);
    }
}
