package com.infiniteloop.mytasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by theotherside on 09/03/15.
 */
public class DurationDialog extends DialogFragment {

    public static String EXTRA_HOURS="com.infiniteloop.mytasks.hours";
    public static String EXTRA_MINUTES="com.infiniteloop.mytasks.minutes";

    private int mHours,mMinutes;

    public static DurationDialog newInstance(int hours,int minutes){
        Bundle args = new Bundle();
        args.putInt(EXTRA_HOURS,hours);
        args.putInt(EXTRA_MINUTES,minutes);

        DurationDialog fragment = new DurationDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.time_dialog,null);

        final NumberPicker hourNumberPicker = (NumberPicker)v.findViewById(R.id.hour_picker);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(12);

        final NumberPicker minutesNumberPicker = (NumberPicker)v.findViewById(R.id.minutes_picker);
        minutesNumberPicker.setMinValue(0);
        minutesNumberPicker.setMaxValue(59);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.select_duration))
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHours = hourNumberPicker.getValue();
                        mMinutes = minutesNumberPicker.getValue();
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .create();

    }

    private void sendResult(int resultCode){
        if(getTargetFragment()!= null){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_HOURS,mHours);
            intent.putExtra(EXTRA_MINUTES,mMinutes);
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(),resultCode,intent);
        }else{
            return;
        }
    }
}
