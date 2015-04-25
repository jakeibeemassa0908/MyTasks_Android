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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.infiniteloop.mytasks.activities.CheckListActivity;
import com.infiniteloop.mytasks.activities.NoteActivity;
import com.infiniteloop.mytasks.data.CheckList;
import com.infiniteloop.mytasks.data.Note;
import com.infiniteloop.mytasks.data.Photo;
import com.infiniteloop.mytasks.loaders.CursorLoader;
import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.loaders.SQLiteCursorLoader;
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
    private String mCurrentPhotoPath = "";

    private View mNoteLayout;
    private GridView mNoteGridView;

    private View mChecklistLayout;
    private GridView mListGridView;

    private View mImageLayout;
    private GridView mImageGridView;




    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PICK_IMAGE=2;
    public static final int REQUEST_NOTE=3;
    public static final int REQUEST_CHECKLIST=4;

    private static final int CATEGORY_LOADER=0;
    private static final int NOTE_LOADER=1;
    private static final int CHECKLIST_LOADER=2;
    private static final int PHOTO_LOADER=3;


    private static final String TAG=DetailTaskFragment.class.getSimpleName();

    public static String EXTRA_TASK="com.infiniteloop.task";

    public static Task mTask;
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

        getLoaderManager().initLoader(CATEGORY_LOADER,null,this);
        getLoaderManager().initLoader(NOTE_LOADER,null,this);
        getLoaderManager().initLoader(CHECKLIST_LOADER,null,this);
        getLoaderManager().initLoader(PHOTO_LOADER,null,this);


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
                    if(imageExists(mCurrentPhotoPath)){
                        //save pic in database
                        Photo photo = new Photo();
                        photo.setTaskId(mTask.getId());
                        photo.setFilename(mCurrentPhotoPath);
                        long result =TaskLab.get(getActivity()).insertPhoto(photo);
                        if(result!=-1){
                            //Share the picture with phone's gallery
                            galleryPic(mCurrentPhotoPath);
                            //refresh
                            getLoaderManager().restartLoader(PHOTO_LOADER,null,this);
                        }
                    }
                }
                break;

            case REQUEST_PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    //get the real path from Uri
                    String path = getRealPathFromURI(getActivity(),selectedImageUri);
                    Photo photo = new Photo();
                    photo.setTaskId(mTask.getId());
                    photo.setFilename(path);
                    //if photo saved reload
                    long result =TaskLab.get(getActivity()).insertPhoto(photo);
                    if(result!=-1){
                        //refresh
                        getLoaderManager().restartLoader(PHOTO_LOADER,null,this);
                    }
                }
                break;
            case REQUEST_NOTE:
                if(resultCode==Activity.RESULT_OK)
                    getLoaderManager().restartLoader(NOTE_LOADER,null,this);
                break;
            case REQUEST_CHECKLIST:
                if (resultCode == Activity.RESULT_OK)
                    getLoaderManager().restartLoader(CHECKLIST_LOADER,null,this);
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

        //======================Bottom options=======================

        mNotes = (ImageButton)rootView.findViewById(R.id.add_note);
        mNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NoteActivity.class);
                intent.putExtra(EXTRA_TASK,mTask);
                startActivityForResult(intent, REQUEST_NOTE);
            }
        });

        mCheckList=(ImageButton)rootView.findViewById(R.id.add_list);
        mCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckListActivity.class);
                intent.putExtra(EXTRA_TASK,mTask);
                startActivityForResult(intent, REQUEST_CHECKLIST);
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

                //When clicked on select Image

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

        //======================Bottom options END =======================

        /**
         * CheckList Gridview
         * **/

        mChecklistLayout = rootView.findViewById(R.id.checklist_layout);
        mChecklistLayout.setVisibility(View.GONE);

        mListGridView = (GridView)rootView.findViewById(R.id.gridview_list);
        mListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        /**
         * Image GridView
         */

        mImageLayout = rootView.findViewById(R.id.image_layout);
        mImageLayout.setVisibility(View.GONE);

        mImageGridView = (GridView)rootView.findViewById(R.id.gridview_image);
        mImageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        /**
         * Notes GridView
         */
        mNoteLayout  = rootView.findViewById(R.id.notes_layout);
        mNoteLayout.setVisibility(View.GONE);

        mNoteGridView = (GridView)rootView.findViewById(R.id.gridview_note);
        mNoteGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note =((Note)mNoteGridView.getAdapter().getItem(position));
                Intent intent = new Intent(getActivity(),NoteActivity.class);
                intent.putExtra(NoteFragment.EXTRA_NOTE,note);
                startActivityForResult(intent,REQUEST_NOTE);
            }
        });

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
        if(!mTask.getTitle().equals(mTitleEditText.getText().toString().trim())) return true;
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
        //Launch all the loader, No break so that the case fall through
        switch (i){
            case CATEGORY_LOADER:
                return new CursorLoader.CategoryListLoader(getActivity());
            case NOTE_LOADER:
                return new NoteCursorLoader(getActivity());
            case CHECKLIST_LOADER:
                return new ChecklistCursorLoader(getActivity());
            case PHOTO_LOADER:
                return new PhotoCursorLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int id = cursorLoader.getId();
        switch (id){
            case CATEGORY_LOADER:
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
                break;
            case NOTE_LOADER:
                if(cursor.getCount()>0){
                    ArrayList<Note> noteList= new ArrayList<Note>();
                    cursor.moveToFirst();
                    for(int i=0;i<cursor.getCount();i++){
                        noteList.add(((TaskDataBaseHelper.NoteCursor)cursor).getNote());
                        cursor.moveToNext();
                    }
                    mNoteGridView.setAdapter(new GridViewAdapter(getActivity(),noteList));
                    mNoteLayout.setVisibility(View.VISIBLE);
                }
                break;
            case CHECKLIST_LOADER:
                if(cursor.getCount()>0){
                    ArrayList<CheckList> checklists =new ArrayList<CheckList>();
                    cursor.moveToFirst();
                    for(int i=0;i<cursor.getCount();i++){
                        checklists.add(((TaskDataBaseHelper.ChecklistCursor)cursor).getChecklist());
                        cursor.moveToNext();
                    }
                    mListGridView.setAdapter(new GridViewAdapter(getActivity(),checklists));
                    mChecklistLayout.setVisibility(View.VISIBLE);
                }
                break;
            case PHOTO_LOADER:
                if(cursor.getCount()>0){
                    ArrayList<Photo> photolist = new ArrayList<Photo>();

                    cursor.moveToFirst();
                    for(int i =0;i<cursor.getCount();i++){
                        Photo photo = ((TaskDataBaseHelper.PhotoCursor)cursor).getPhoto();
                        if(!imageExists(photo.getFilename()))
                            continue;
                        photolist.add(photo);
                        cursor.moveToNext();
                    }

                    mImageGridView.setAdapter(new GridViewAdapter(getActivity(),photolist));
                    mImageLayout.setVisibility(View.VISIBLE);

                }
        }
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
        mCurrentPhotoPath=image.getAbsolutePath();
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
     * Verify if image exists
     * @param path
     * @return
     */
    public boolean imageExists(String path){
        File imgFile = new  File(path);

        if(imgFile.exists()){
            return true;
        }
        return false;
    }

    /**
     * Get bitmap tailored to the size of the imageview container for better memory management
     * @param path
     * @param imageView
     * @return
     */
    private Bitmap getTailoredBitmap(String path,ImageView imageView) {
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



    private class GridViewAdapter extends BaseAdapter{
        private Context mContext;
        ArrayList<?> mList;

        public GridViewAdapter(Context c,ArrayList<?>list){
            mContext=c;
            mList=list;
        }

        @Override
        public int getCount() {
            if(mList.size()<4)
                return mList.size();
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView =convertView;
            if(convertView==null){
                String titleText="";
                Drawable drawable=null;

                //Inflate the custom layout
               LayoutInflater inflater = getActivity().getLayoutInflater();
                myView = inflater.inflate(R.layout.custom_grid_item,null);

                TextView text = (TextView)myView.findViewById(R.id.grid_item_text);
                text.setVisibility(View.VISIBLE);
                ImageView image = (ImageView)myView.findViewById(R.id.grid_item_image);

                //Set layout properties
                myView.setLayoutParams(new GridView.LayoutParams(150, 200));
                myView.setPadding(0,0,0,0);

                //Check the type of list obtained
                if(mList.size()>0 && position<3){
                    if(mList.get(0) instanceof Note){
                        titleText = ((Note)mList.get(position)).getTitle();
                        drawable=getResources().getDrawable(R.drawable.ic_action_assignment);
                    }else if(mList.get(0) instanceof CheckList){
                        titleText = ((CheckList)mList.get(position)).getName();
                        drawable=getResources().getDrawable(R.drawable.ic_action_list);
                    }else if(mList.get(0) instanceof Photo){
                        //Remove the text bar
                        text.setVisibility(View.GONE);
                        myView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        String photoFilename =((Photo)mList.get(position)).getFilename();
                        image.setImageBitmap(getTailoredBitmap(photoFilename,image));
                        return myView;
                    }
                }else{
                    myView.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_custom_reverse));
                    drawable=getResources().getDrawable(R.drawable.ic_hardware_keyboard_arrow_right);
                    titleText="Show More";
                }

                text.setText(titleText);
                image.setImageDrawable(drawable);
            }

            return myView;
        }
    }



    public static class NoteCursorLoader extends SQLiteCursorLoader {

        public NoteCursorLoader(Context context){
            super (context);
        }

        @Override
        protected Cursor loadCursor() {
            //Query the list of runs
            return TaskLab.get(getContext()).queryNotes(mTask.getId());
        }
    }


    public static class ChecklistCursorLoader extends SQLiteCursorLoader{

        public ChecklistCursorLoader(Context context){
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return TaskLab.get(getContext()).queryChecklist(mTask.getId());
        }
    }

    public static class PhotoCursorLoader extends SQLiteCursorLoader{

        public PhotoCursorLoader(Context context){
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return TaskLab.get(getContext()).getPhotos(mTask.getId());
        }
    }

}
