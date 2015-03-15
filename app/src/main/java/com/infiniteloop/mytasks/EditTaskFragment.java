package com.infiniteloop.mytasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskFragment extends Fragment {

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private EditText mTitleEditText;

    private static final String TAG=EditTaskFragment.class.getSimpleName();

    public static String EXTRA_TASK="com.infiniteloop.task";

    private Task mTask;

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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.edit_task_fragment,container,false);
        mTitleEditText=(EditText)rootView.findViewById(R.id.edit_task_title_textview);
        mTitleEditText.setText(mTask.getTitle());

        mPrioritySpinner= (Spinner)rootView.findViewById(R.id.edit_task_priority_spinner);
        mPrioritySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),R.array.task_priority_array));

        mCategorySpinner= (Spinner) rootView.findViewById(R.id.edit_task_category_spinner);
        mCategorySpinner.setAdapter(Helpers.getSpinnerAdapter(getActivity(),R.array.task_category_array));

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
                    deleteDialog.setTitle("Edit Task");
                    deleteDialog.setMessage("Do you want to edit this task?");
                    deleteDialog.setPositiveButton("Edit",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean edited=TaskLab.get(getActivity()).editTask(mTask);
                            if(edited){
                                Intent resultIntent = new Intent();
                                getActivity().setResult(Activity.RESULT_OK,resultIntent);
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
        //mTask.setPriority(Helpers.getPriority(getActivity(),mPrioritySpinner.getSelectedItem().toString()));
    }

    private boolean hasDataChanged() {
        if(!mTask.getTitle().equals(mTitleEditText.getText().toString())) return true;
        //if(mTask.getPriority()!= Helpers.getPriority(getActivity(),mPrioritySpinner.getSelectedItem().toString()))return true;
        return false;
    }
}
