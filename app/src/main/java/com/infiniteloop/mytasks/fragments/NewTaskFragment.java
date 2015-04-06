package com.infiniteloop.mytasks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.infiniteloop.mytasks.loaders.CursorLoader;
import com.infiniteloop.mytasks.Helpers;
import com.infiniteloop.mytasks.R;
import com.infiniteloop.mytasks.data.TaskLab;
import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskFragment extends VisibleFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = NewTaskFragment.class.getSimpleName();

    private Spinner mPrioritySpinner;
    private ImageView mCategoryAdd;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;
    private Button mSetAlarmButton;
    private Date mDateCaptured;
    ArrayList<String> mCategoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCategoryList=new ArrayList<String>();
        mCategoryList.add("No Category");
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task,container,false);

        mTitleEditText = (EditText) rootView.findViewById(R.id.task_title_textview);

        mPrioritySpinner = (Spinner)rootView.findViewById(R.id.task_priority_spinner);
        ArrayList<String> priorities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.task_priority_array)));
        mPrioritySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(), priorities));

        mCategorySpinner=(Spinner)rootView.findViewById(R.id.task_category_spinner);

        mCategoryAdd = (ImageView)rootView.findViewById(R.id.add_category_imageView);
        mCategoryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(getString(R.string.add_category));
                final EditText newCategory = new EditText(getActivity());
                dialog.setView(newCategory);
                dialog.setPositiveButton(getString(R.string.add),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mAddCategory=newCategory.getText().toString();
                        if(!mAddCategory.matches("")){
                            mCategoryList.add(0,mAddCategory);
                        }
                        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));
                    }
                });
                dialog.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        mSetAlarmButton = (Button)rootView.findViewById(R.id.setAlarm);
        mSetAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeAndDatePickerFragment.DatePickerFragment pickers = new TimeAndDatePickerFragment.DatePickerFragment();
                pickers.setTargetFragment(NewTaskFragment.this,Helpers.REQUEST_TIME);
                pickers.show(getFragmentManager(),"pickers");
            }
        });

        return rootView;
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
        mSetAlarmButton.setText(DateFormat.getDateTimeInstance().format(date));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task_menu:
                String title=mTitleEditText.getText().toString();
                String category=mCategorySpinner.getSelectedItem().toString();
                category=category.trim();
                String priority=mPrioritySpinner.getSelectedItem().toString();
                if(!title.matches("")){
                    boolean isCreated= TaskLab.get(getActivity()).createTask(getActivity(),title,priority,category,mDateCaptured);
                    if(isCreated){
                        Intent resultIntent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK,resultIntent);
                        getActivity().finish();
                    }else{
                        Log.d(TAG,"Task Not Created");
                    }
                }else{
                    Toast.makeText(getActivity(),R.string.empty_task_title,Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader.CategoryListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if(cursor.getCount()>0){mCategoryList.remove(0);}
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            mCategoryList.add(((TaskDataBaseHelper.CategoryCursor)cursor).getCategory().getCategoryName());
            cursor.moveToNext();
        }
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
