package com.infiniteloop.mytasks.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.infiniteloop.mytasks.services.ReminderService;

/**
 * Created by theotherside on 03/04/15.
 */
public class VisibleFragment extends Fragment {
    public static final String TAG = VisibleListFragment.class.getSimpleName();

    public BroadcastReceiver mOnShowNotification  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getActivity(),"Got a broadcast",Toast.LENGTH_LONG).show();
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
