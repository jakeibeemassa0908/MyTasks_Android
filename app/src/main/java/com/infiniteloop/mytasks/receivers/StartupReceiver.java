package com.infiniteloop.mytasks.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.infiniteloop.mytasks.services.RestartAlarmService;

/**
 * Created by theotherside on 20/04/15.
 *
 * Receiver that starts when the phone is rebooted
 */
public class StartupReceiver extends BroadcastReceiver {

    public static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        //Start the ResetAlarm reminder service
        Intent resetAlarmIntent = new Intent(context,RestartAlarmService.class);
        context.startService(resetAlarmIntent);
    }
}
