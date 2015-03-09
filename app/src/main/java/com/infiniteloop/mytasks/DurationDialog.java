package com.infiniteloop.mytasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by theotherside on 09/03/15.
 */
public class DurationDialog extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.time_dialog,null);

        NumberPicker hourNumberPicker = (NumberPicker)v.findViewById(R.id.hour_picker);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(12);

        NumberPicker minutesNumberPicker = (NumberPicker)v.findViewById(R.id.minutes_picker);
        minutesNumberPicker.setMinValue(0);
        minutesNumberPicker.setMaxValue(59);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.select_duration))
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

    }
}
