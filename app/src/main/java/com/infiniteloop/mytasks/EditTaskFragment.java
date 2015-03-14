package com.infiniteloop.mytasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.infiniteloop.mytasks.data.TaskLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by theotherside on 14/03/15.
 */
public class EditTaskFragment extends Fragment {

    private Spinner mPrioritySpinner;
    private Spinner mCategorySpinner;
    private Spinner mVisibilitySpinner;
    private ImageButton mSetTimeButton;
    private EditText mTitleEditText;
    private int mDurationHours;
    private int mDurationMinutes;
    private EditText mDurationText;

    private static final String TAG=EditTaskFragment.class.getSimpleName();

    public static String EXTRA_TASK="com.infiniteloop.task";
    private static final int LOAD_TASK=0;

    private Task mTask;

    public static EditTaskFragment newInstance(Task task){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_TASK,task);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args =getArguments();
        mTask=args.getParcelable(EXTRA_TASK);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.edit_task_fragment,container,false);
        mTitleEditText=(EditText)rootView.findViewById(R.id.edit_task_title_textview);
        mTitleEditText.setFocusable(false);
        mTitleEditText.setText(mTask.getTitle());
        return rootView;
    }
}
