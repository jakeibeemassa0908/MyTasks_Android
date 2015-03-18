package com.infiniteloop.mytasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.infiniteloop.mytasks.data.TaskDataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;

    private ArrayList<String> mPriorities;

    private ImageView mCategoryAdd;

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
        mCategoryList=new ArrayList<String>();
        mCategoryList.add("No Category");

        getLoaderManager().initLoader(0,null,this);

        setHasOptionsMenu(true);

        categoyIdName=new HashMap<String,Long>();

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
    }

    private long getCatId(String s) {
        return categoyIdName.get(s);
    }

    private boolean hasDataChanged() {
        if(!mTask.getTitle().equals(mTitleEditText.getText().toString())) return true;
        if(mTask.getPriority()!= Helpers.getPriority(getActivity(),mPrioritySpinner.getSelectedItem().toString()))return true;
        if(mTask.getCategory()!=getCatId(mCategorySpinner.getSelectedItem().toString())) return true;
        return false;
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
            String categoryName=((TaskDataBaseHelper.CategoryCursor)cursor).getCategory().getCategoryName();
            Long categoryId=((TaskDataBaseHelper.CategoryCursor)cursor).getCategory().getId();
            categoyIdName.put(categoryName,categoryId);
            mCategoryList.add(categoryName);
            cursor.moveToNext();
        }
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
