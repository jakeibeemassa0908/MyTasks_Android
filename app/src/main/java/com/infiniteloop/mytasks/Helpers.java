package com.infiniteloop.mytasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.infiniteloop.mytasks.data.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by theotherside on 14/03/15.
 */
public class Helpers {
    public static final int REQUEST_TIME=0;
    public  static ArrayAdapter getSpinnerAdapter(Context context,ArrayList<String> items){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,items);
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

    public static String dateToString(long dateToTime) {
        return DateFormat.getDateInstance().format(new Date(dateToTime));
    }

    /**
     * Get bitmap tailored to the size of the imageview container for better memory management
     * @param path
     * @param imageView
     * @return
     */
    public static Bitmap getTailoredBitmap(String path,ImageView imageView) {
        // Get the dimensions of the View
        int targetW = 150;
        int targetH = 200;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

        return bitmap;
    }
}
