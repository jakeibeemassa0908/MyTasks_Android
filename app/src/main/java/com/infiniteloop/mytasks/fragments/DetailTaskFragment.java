package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.infiniteloop.mytasks.activities.CheckListActivity;
import com.infiniteloop.mytasks.activities.NoteActivity;
import com.infiniteloop.mytasks.loaders.CursorLoader;
import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.services.ReminderService;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskLab;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by theotherside on 14/03/15.
 */
public class DetailTaskFragment extends VisibleFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;
    private Button mEditAlarm;
    private ArrayList<String> mPriorities;
    private Date mDateCaptured;
    private ArrayList<String> mCategoryList;
    private ImageButton mNotes, mImage,mCheckList;
    private ArrayList<String> mCurrentPhotoPath = new ArrayList<String>();
    ImageView mImageView;


    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PICK_IMAGE=2;


    private static final String TAG=DetailTaskFragment.class.getSimpleName();

    public static String EXTRA_TASK="com.infiniteloop.task";

    private Task mTask;
    private HashMap<String,Long> categoyIdName;

    public static DetailTaskFragment newInstance(Task task){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_TASK, task);
        DetailTaskFragment fragment = new DetailTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args =getArguments();
        mTask=args.getParcelable(EXTRA_TASK);
        super.onCreate(savedInstanceState);

        mPriorities=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.task_priority_array)));

        //Get the passed task priority and set it first in the spinner
        int taskPriorityPosition=mTask.getPriority();
        String taskPriority= mPriorities.get(taskPriorityPosition);
        mPriorities.remove(taskPriorityPosition);
        mPriorities.add(0,taskPriority);

        mCategoryList=new ArrayList<String>();
        mCategoryList.add("No Category");

        getLoaderManager().initLoader(0,null,this);

        setHasOptionsMenu(true);

        categoyIdName=new HashMap<String,Long>();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Helpers.REQUEST_TIME:
                    if(resultCode==Activity.RESULT_OK){
                        int day=data.getIntExtra(TimeAndDatePickerFragment.EXTRA_DAY,0);
                        int month=data.getIntExtra(TimeAndDatePickerFragment.EXTRA_MONTH,0);
                        int year=data.getIntExtra(TimeAndDatePickerFragment.EXTRA_YEAR,0);
                        int hour=data.getIntExtra(TimeAndDatePickerFragment.EXTRA_HOUR,0);
                        int minute=data.getIntExtra(TimeAndDatePickerFragment.EXTRA_MIN,0);
                        GregorianCalendar calendar = new GregorianCalendar(year,month,day,hour,minute);
                        mDateCaptured=calendar.getTime();
                        updateReminderButton(mDateCaptured);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == Activity.RESULT_OK){
                    for(String filename: mCurrentPhotoPath){
                        //Share the picture with phone's gallery
                        galleryPic(filename);

                        Bitmap imageBitmap=getImageBitmap(filename);
                        if(imageBitmap!=null){
                            mImageView.setImageBitmap(getTailoredBitmap(filename,mImageView));
                        }
                    }
                }
                break;

            case REQUEST_PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    //get the real path from Uri
                    String path = getRealPathFromURI(getActivity(),selectedImageUri);

                    Bitmap imageBitmap=getImageBitmap(path);
                    if(imageBitmap!=null){
                        mImageView.setImageBitmap(getTailoredBitmap(path,mImageView));
                    }

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void updateReminderButton(Date date) {
        mEditAlarm.setText(DateFormat.getDateTimeInstance().format(date));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.edit_task_fragment,container,false);
        mTitleEditText=(EditText)rootView.findViewById(R.id.edit_task_title_textview);
        mTitleEditText.setText(mTask.getTitle());

        mPrioritySpinner= (Spinner)rootView.findViewById(R.id.edit_task_priority_spinner);
        mPrioritySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mPriorities));

        mCategorySpinner= (Spinner) rootView.findViewById(R.id.edit_task_category_spinner);
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));

        mImageView = (ImageView)rootView.findViewById(R.id.task_image);

        mEditAlarm=(Button)rootView.findViewById(R.id.edit_Alarm);
        if(mTask.getReminder()==-1){
            mEditAlarm.setText(getResources().getString(R.string.set_reminder));
        }else{
            mEditAlarm.setText(DateFormat.getDateTimeInstance().format(new Date(mTask.getReminder())));
        }
        mEditAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeAndDatePickerFragment.DatePickerFragment pickers = new TimeAndDatePickerFragment.DatePickerFragment();
                pickers.setTargetFragment(DetailTaskFragment.this,Helpers.REQUEST_TIME);
                pickers.show(getFragmentManager(),"pickers");
            }
        });

        mEditAlarm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!mEditAlarm.getText().equals(getResources().getString(R.string.set_reminder))){
                    AlertDialog.Builder removeAlarmDialog = new AlertDialog.Builder(getActivity());
                    removeAlarmDialog.setMessage(R.string.removeAlarm);
                    removeAlarmDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //remove reminder
                            mEditAlarm.setText(getResources().getString(R.string.set_reminder));
                        }
                    });
                    removeAlarmDialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    removeAlarmDialog.show();

                    return true;
                }
                return true;
            }
        });

        //Bottom options

        mNotes = (ImageButton)rootView.findViewById(R.id.add_note);
        mNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NoteActivity.class);
                startActivity(intent);
            }
        });

        mImage =(ImageButton)rootView.findViewById(R.id.add_image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle(getString(R.string.add_picture));
                dialog.setContentView(R.layout.camera_dialog);

                //Set dialog size
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);

                //When clicked on Take photo
                View take_pick = dialog.findViewById(R.id.take_pic);
                take_pick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //Ensure that there's a camera activity to handle the intent
                        if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
                            dialog.dismiss();
                            //Create the file where the photo should go
                            File photoFile = null;
                            try{
                                photoFile=createImageFile();
                            }catch (IOException ex){
                                Log.e(TAG,ex.getMessage());
                            }
                            if(photoFile!=null){
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                            }
                        }

                    }
                });

                View open_gallery = dialog.findViewById(R.id.goto_gallery);
                open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE);
                    }
                });

                dialog.show();
            }
        });
        mCheckList=(ImageButton)rootView.findViewById(R.id.add_list);
        mCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckListActivity.class);
                startActivity(intent);
            }
        });

//
//        mCategoryAdd = (ImageView)rootView.findViewById(R.id.edit_add_category_imageView);
//        mCategoryAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//                dialog.setTitle(getString(R.string.add_category));
//                final EditText newCategory = new EditText(getActivity());
//                dialog.setView(newCategory);
//                dialog.setPositiveButton(getString(R.string.add),new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String mAddCategory=newCategory.getText().toString();
//                        if(!mAddCategory.matches("")){
//
//                            mCategoryList.add(0,mAddCategory);
//                        }
//                        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));
//                    }
//                });
//                dialog.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                dialog.show();
//            }
//        });

        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task_menu:
                if(!hasDataChanged()){
                    getActivity().finish();
                    return true;
                }else{
                    setNewTaskValues();
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
                    deleteDialog.setTitle(getString(R.string.edit_dialog_title));
                    deleteDialog.setMessage(R.string.edit_dialog_question);
                    deleteDialog.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean edited = TaskLab.get(getActivity()).editTask(mTask,getActivity());
                            if (edited) {
                                Intent resultIntent = new Intent();
                                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                                getActivity().finish();
                            }
                        }
                    });
                    deleteDialog.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    deleteDialog.show();
                    return true;

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setNewTaskValues() {
        mTask.setTitle(mTitleEditText.getText().toString().trim());
        mTask.setPriority(Helpers.getPriority(getActivity(),mPrioritySpinner.getSelectedItem().toString()));
        mTask.setCategory(getCatId(mCategorySpinner.getSelectedItem().toString()));
        if(mDateCaptured != null)
            mTask.setReminder(mDateCaptured);
        //If reminder has been removed, set it to -1
        if(mEditAlarm.getText().equals(getResources().getString(R.string.set_reminder)))
            mTask.setReminder(null);
        if(mTask.getReminder()!=-1){
            TaskLab.get(getActivity()).activateServiceAlarm(getActivity(), mTask, true);
        }
    }

    /**
     * Get category id from the category name by querying the HashMap
     * @param s category name
     * @return category id
     */
    private long getCatId(String s) {
        return categoyIdName.get(s);
    }

    /**
     * Check if the data has changed
     * @return true if any data has changed
     */
    private boolean hasDataChanged() {
        if(!mTask.getTitle().equals(mTitleEditText.getText().toString())) return true;
        if(mTask.getPriority()!= Helpers.getPriority(getActivity(),mPrioritySpinner.getSelectedItem().toString()))return true;
        if(mTask.getCategory()!=getCatId(mCategorySpinner.getSelectedItem().toString())) return true;
        if(mTask.getReminder()==-1){
            if(!mEditAlarm.getText().toString().equals(getResources().getString(R.string.set_reminder)))return true;
        }else{
            if(!DateFormat.getDateTimeInstance().format(mTask.getReminder()).equals(mEditAlarm.getText().toString()))return true;
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader.CategoryListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //If there are categories returned iterate through them
        if(cursor.getCount()>0){mCategoryList.remove(0);}
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            String categoryName=((TaskDataBaseHelper.CategoryCursor)cursor).getCategory().getCategoryName();
            Long categoryId=((TaskDataBaseHelper.CategoryCursor)cursor).getCategory().getId();
            //create a mapping between category name and Id
            categoyIdName.put(categoryName,categoryId);
            //set the current category of task to be the first in the spinner
            if(categoryId==mTask.getCategory()){
                mCategoryList.add(0,categoryName);
            }else{
                mCategoryList.add(categoryName);
            }
            cursor.moveToNext();
        }
        //repopulate the spinner
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Create a file, and save the file_name in the current file list for intent use
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image= File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        //Save a file :path for use whith ACTION_VIEW intents
        mCurrentPhotoPath.add(image.getAbsolutePath());
        return image;
    }

    /**
     * Make the picture available to the gallery
     */
    private void galleryPic(String filePath){
        Intent mediaSanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaSanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaSanIntent);
    }

    /**
     * Get the absolute path from the Uri
     * @param context
     * @param uri
     * @return
     */
    public String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    /**
     * Get the bitmap from the path
     * @param path
     * @return
     */
    public Bitmap getImageBitmap(String path){
        File imgFile = new  File(path);

        if(imgFile.exists()){
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return null;
    }

    /**
     * Get bitmap tailored to the size of the imageview container for better memory management
     * @param path
     * @param imageView
     * @return
     */
    private Bitmap getTailoredBitmap(String path,ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

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
