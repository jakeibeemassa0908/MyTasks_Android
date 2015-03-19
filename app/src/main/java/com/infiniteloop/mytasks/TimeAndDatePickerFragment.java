package com.infiniteloop.mytasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by theotherside on 14/03/15.
 */
public class TimeAndDatePickerFragment {
    public static final String EXTRA_HOUR="com.infiniteloop.com.hour";
    public static final String EXTRA_MIN="com.infiniteloop.com.minutes";
    public static final String EXTRA_YEAR="com.infiniteloop.com.year";
    public static final String EXTRA_MONTH="com.infiniteloop.com.month";
    public static final String EXTRA_DAY="com.infiniteloop.com.day";


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public static TimePickerFragment newInstance(int year,int month,int day){
            Bundle args = new Bundle();
            args.putInt(EXTRA_YEAR,year);
            args.putInt(EXTRA_MONTH,month);
            args.putInt(EXTRA_DAY,day);

            TimePickerFragment dialog = new TimePickerFragment();
            dialog.setArguments(args);
            return dialog;

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Intent intent = new Intent();
            intent.putExtra(EXTRA_HOUR,hourOfDay);
            intent.putExtra(EXTRA_MIN,minute);
            intent.putExtra(EXTRA_YEAR,getArguments().getInt(EXTRA_YEAR));
            intent.putExtra(EXTRA_MONTH,getArguments().getInt(EXTRA_MONTH));
            intent.putExtra(EXTRA_DAY,getArguments().getInt(EXTRA_DAY));

            getTargetFragment().onActivityResult(EditTaskFragment.REQUEST_TIME, Activity.RESULT_OK,intent);
        }
    }



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment(){
            super();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            TimePickerFragment dialog =TimePickerFragment.newInstance(year,month,day);
            dialog.setTargetFragment(getTargetFragment(),EditTaskFragment.REQUEST_TIME);
            dialog.show(getFragmentManager(),"pickers");
        }
    }
}