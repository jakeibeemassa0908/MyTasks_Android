package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.infiniteloop.mytasks.loaders.CursorLoader;
import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.services.ReminderService;
import com.infiniteloop.mytasks.data.Task;
import com.infiniteloop.mytasks.data.TaskLab;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskFragment extends VisibleFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;
    private Button mEditAlarm;

    private ArrayList<String> mPriorities;

    private Date mDateCaptured;
    private ArrayList<String> mCategoryList;

    private static final String TAG=EditTaskFragment.class.getSimpleName();

    public static String EXTRA_TASK="com.infiniteloop.task";

    private Task mTask;
    private HashMap<String,Long> categoyIdName;

    public static EditTaskFragment newInstance(Task task){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_TASK, task);
        EditTaskFragment fragment = new EditTaskFragment();
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
                        break;
                }
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
                pickers.setTargetFragment(EditTaskFragment.this,Helpers.REQUEST_TIME);
                pickers.show(getFragmentManager(),"pickers");
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
                            boolean edited = TaskLab.get(getActivity()).editTask(mTask);
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
        mTask.setTitle(mTitleEditText.getText().toString());
        mTask.setPriority(Helpers.getPriority(getActivity(),mPrioritySpinner.getSelectedItem().toString()));
        mTask.setCategory(getCatId(mCategorySpinner.getSelectedItem().toString()));
        mTask.setReminder(mDateCaptured);
        if(mTask.getReminder()!=-1){
            ReminderService.activateServiceAlarm(getActivity(), mTask, true);
        }
    }

    private long getCatId(String s) {
        return categoyIdName.get(s);
    }

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
}
