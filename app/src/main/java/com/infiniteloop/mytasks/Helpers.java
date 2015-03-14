package com.infiniteloop.mytasks;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by theotherside on 14/03/15.
 */
public class Helpers {
    public  static ArrayAdapter getSpinnerAdapter(Context context,int arrayId){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                arrayId,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static int getPriority(Context c, String priority){
        if(priority.equals(c.getString(R.string.normal))){
            return Task.NORMAL_PRIORITY;
        }else if(priority.equals(c.getString(R.string.low))){
            return Task.LOW_PRIORITY;
        }else if(priority.equals(c.getString(R.string.high))){
            return Task.HIGH_PRIORITY;
        }else if(priority.equals(c.getString(R.string.very_high))){
            return Task.VERY_HIGH_PRIORITY;
        }else{
            return -1;
        }
    }
}
