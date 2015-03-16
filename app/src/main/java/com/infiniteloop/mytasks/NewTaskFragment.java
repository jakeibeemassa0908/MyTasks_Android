package com.infiniteloop.mytasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskFragment extends Fragment {
    public static final String TAG = NewTaskFragment.class.getSimpleName();

    private Spinner mPrioritySpinner;
    private ImageView mCategoryAdd;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;
    ArrayList<String> mCategoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCategoryList=new ArrayList<String>();
        mCategoryList.add("No Category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task,container,false);

        mTitleEditText = (EditText) rootView.findViewById(R.id.task_title_textview);

        mPrioritySpinner = (Spinner)rootView.findViewById(R.id.task_priority_spinner);
        ArrayList<String> priorities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.task_priority_array)));
        mPrioritySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),priorities));

        mCategorySpinner=(Spinner)rootView.findViewById(R.id.task_category_spinner);
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),mCategoryList));

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

        return rootView;
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
                    boolean isCreated=TaskLab.get(getActivity()).createTask(getActivity(),title,priority,category);
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
}
