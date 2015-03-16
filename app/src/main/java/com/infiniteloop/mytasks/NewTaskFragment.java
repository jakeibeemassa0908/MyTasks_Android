package com.infiniteloop.mytasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by theotherside on 07/03/15.
 */
public class NewTaskFragment extends Fragment {
    public static final String TAG = NewTaskFragment.class.getSimpleName();

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task,container,false);

        mTitleEditText = (EditText) rootView.findViewById(R.id.task_title_textview);

        mPrioritySpinner = (Spinner)rootView.findViewById(R.id.task_priority_spinner);
        mPrioritySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),R.array.task_priority_array));

        mCategorySpinner=(Spinner)rootView.findViewById(R.id.task_category_spinner);
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),R.array.task_category_array));

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task_menu:
                String title=mTitleEditText.getText().toString();
                //String category=mCategorySpinner.getSelectedItem().toString();
                long category=-1;
                String priority=mPrioritySpinner.getSelectedItem().toString();
               // String visibility=mVisibilitySpinner.getSelectedItem().toString();
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
